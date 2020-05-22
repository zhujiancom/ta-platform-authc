package com.ta.platform.authc.module.shiro.jwt;

import com.ey.tax.toolset.core.StrUtil;
import com.ey.tax.toolset.http.HttpUtil;
import com.ta.platform.authc.module.convert.SysUserConvert;
import com.ta.platform.authc.module.entity.SysUser;
import com.ta.platform.authc.module.service.ILoginRedisService;
import com.ta.platform.authc.module.service.ISysUserService;
import com.ta.platform.common.tool.ApplicationContextProvider;
import com.ta.platform.common.vo.LoginUserRedisVo;
import com.ta.platform.common.vo.LoginUserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Creator: zhuji
 * Date: 5/20/2020
 * Time: 11:47 AM
 * Description: Shiro 授权认证
 */
@Slf4j
@Component
public class JwtRealm extends AuthorizingRealm {

    private final ILoginRedisService loginReidsService;

    private final ISysUserService sysUserService;

    public JwtRealm(@Autowired ILoginRedisService loginReidsService,@Autowired ISysUserService sysUserService, @Autowired JwtCredentialsMatcher jwtCredentialsMatcher){
        this.loginReidsService = loginReidsService;
        this.sysUserService = sysUserService;
        this.setCredentialsMatcher(jwtCredentialsMatcher);
    }



    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null && token instanceof JwtToken;
    }

    /**
     * 权限信息认证(包括角色以及权限)是用户访问controller的时候才进行验证(redis存储的此处权限信息)
     * 触发检测用户权限时才会调用此方法，例如checkRole,checkPermission
     * @param principals 身份信息
     * @return SimpleAuthorizationInfo 角色权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("===============Shiro权限认证开始============ [ roles、permissions]==========");

        JwtToken jwtToken = (JwtToken) principals.getPrimaryPrincipal();

        String username = jwtToken.getUsername();
        //从redis中获取登录用户的角色和权限信息
        LoginUserRedisVo loginUserRedisVo = loginReidsService.getLoginSysUserRedisVo(username);
        // 设置角色
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 设置角色
        authorizationInfo.setRoles(loginUserRedisVo.getRoleCodes());
        // 设置权限
        authorizationInfo.setStringPermissions(loginUserRedisVo.getPermissionCodes());

        log.info("===============Shiro权限认证成功==============");
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.info("===============Shiro登录认证开始============");
        JwtToken jwtToken = (JwtToken) token;

        if(jwtToken == null){
            log.error("————————身份认证失败——————————IP地址:  "+ HttpUtil.getClientIP(ApplicationContextProvider.getHttpServletRequest()));
            throw new AuthenticationException("————————[身份认证失败:jwtToken 不能为空]————————IP地址:  "+ HttpUtil.getClientIP(ApplicationContextProvider.getHttpServletRequest()));
        }
        String salt = ((JwtToken) token).getSalt();
        if(StrUtil.isBlank(salt)){
            throw new AuthenticationException("salt不能为空");
        }
        String username = jwtToken.getUsername();
        SysUser sysUser = sysUserService.getUserByName(username);
        LoginUserVo loginUserVo = SysUserConvert.INSTANCE.sysUserToLoginSysUserVo(sysUser);
        log.info("===============Shiro登录认证成功============");
        return new SimpleAuthenticationInfo(loginUserVo, jwtToken, getName());
    }
}
