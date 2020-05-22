package com.ta.platform.authc.config;

import com.alibaba.fastjson.JSON;
import com.ey.tax.toolset.core.converter.ConvertUtil;
import com.ta.platform.authc.config.properties.JwtProperties;
import com.ta.platform.authc.config.properties.ShiroProperties;
import com.ta.platform.authc.module.service.ILoginRedisService;
import com.ta.platform.authc.module.shiro.jwt.JwtCredentialsMatcher;
import com.ta.platform.authc.module.shiro.jwt.JwtFilter;
import com.ta.platform.authc.module.shiro.jwt.JwtRealm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.util.StringUtils;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Creator: zhuji
 * Date: 5/20/2020
 * Time: 11:08 AM
 * Description:
 */
@Slf4j
@Configuration
public class ShiroConfig {
    /**
     * JWT过滤器名称
     */
    private static final String JWT_FILTER_NAME = "jwtFilter";

    /**
     * Shiro过滤器名称
     */
    private static final String SHIRO_FILTER_NAME = "shiroFilter";

    /**
     * anon
     */
    private static final String ANON = "anon";

    @Value("${spring.redis.port}")
    private String port;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(JwtRealm jwtRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(jwtRealm);

        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-
         * StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        //自定义缓存实现,使用redis
        securityManager.setCacheManager(redisCacheManager());
        return securityManager;
    }

    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    public RedisCacheManager redisCacheManager() {
        log.info("===============(1)创建缓存管理器RedisCacheManager");
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        //redis中针对不同用户缓存(此处的id需要对应user实体中的id字段,用于唯一标识)
        redisCacheManager.setPrincipalIdFieldName("id");
        //用户权限信息缓存时间
        redisCacheManager.setExpire(200000);
        return redisCacheManager;
    }

    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public RedisManager redisManager() {
        log.info("===============(2)创建RedisManager,连接Redis..URL= " + host + ":" + port);
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(ConvertUtil.toInt(port));
        redisManager.setTimeout(0);
        if (!StringUtils.isEmpty(redisPassword)) {
            redisManager.setPassword(redisPassword);
        }
        return redisManager;
    }

    /**
     * Filter Chain定义说明
     * <p>
     * 1、一个URL可以配置多个Filter，使用逗号分隔
     * 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     */
    @Bean(SHIRO_FILTER_NAME)
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager, ILoginRedisService loginRedisService, ShiroProperties shiroProperties, JwtProperties jwtProperties) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filterMap = getFilterMap(loginRedisService, jwtProperties);
        shiroFilterFactoryBean.setFilters(filterMap);
        Map<String, String> filterChainMap = getFilterChainDefinitionMap(shiroProperties);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainMap);
        shiroFilterFactoryBean.setUnauthorizedUrl("/sys/common/403");
        shiroFilterFactoryBean.setLoginUrl("/sys/login");
        return shiroFilterFactoryBean;
    }

    /**
     * 获取filter map
     *
     * @return
     */
    private Map<String, Filter> getFilterMap(ILoginRedisService loginRedisService,
                                             JwtProperties jwtProperties) {
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put(JWT_FILTER_NAME, new JwtFilter(loginRedisService,jwtProperties));
        return filterMap;
    }

    /**
     * Shiro路径权限配置
     * @param shiroProperties
     * @return
     */
    private Map<String, String> getFilterChainDefinitionMap(ShiroProperties shiroProperties) {
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 获取排除的路径
        List<String[]> anonList = shiroProperties.getAnon();
        log.debug("anonList:{}", JSON.toJSONString(anonList));
        if (CollectionUtils.isNotEmpty(anonList)) {
            anonList.forEach(anonArray -> {
                if (ArrayUtils.isNotEmpty(anonArray)) {
                    for (String anonPath : anonArray) {
                        filterChainDefinitionMap.put(anonPath, ANON);
                    }
                }
            });
        }

        // 如果启用shiro，则设置最后一个设置为JWTFilter，否则全部路径放行
        if (shiroProperties.isEnable()) {
            filterChainDefinitionMap.put("/**", JWT_FILTER_NAME);
        } else {
            filterChainDefinitionMap.put("/**", ANON);
        }

        log.debug("filterChainMap:{}", JSON.toJSONString(filterChainDefinitionMap));

        return filterChainDefinitionMap;
    }

    /**
     * 下面的代码是添加注解支持
     *
     * @return
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
