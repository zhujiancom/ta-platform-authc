package com.ta.platform.authc.module.service.impl;

import com.ta.platform.authc.config.constant.CommonRedisKey;
import com.ta.platform.authc.config.constant.JwtConstant;
import com.ta.platform.authc.config.properties.JwtProperties;
import com.ta.platform.authc.module.service.ILoginRedisService;
import com.ta.platform.authc.module.shiro.jwt.JwtToken;
import com.ta.platform.common.bean.ClientInfo;
import com.ta.platform.common.constant.RequestConstant;
import com.ta.platform.common.convert.LoginUserRedisVoConvert;
import com.ta.platform.common.tool.ApplicationContextProvider;
import com.ta.platform.common.tool.ClientInfoUtil;
import com.ta.platform.common.tool.JwtUtil;
import com.ta.platform.common.vo.LoginUserRedisVo;
import com.ta.platform.common.vo.LoginUserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Creator: zhuji
 * Date: 5/20/2020
 * Time: 12:28 PM
 * Description:
 */
@Slf4j
@Service
public class LoginRedisServiceImpl implements ILoginRedisService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public void refreshLoginInfo(String oldToken, String username, JwtToken newJwtToken) {
        // 获取缓存的登录用户信息
        LoginUserRedisVo loginSysUserRedisVo = getLoginSysUserRedisVo(username);
        // 删除之前的token信息
        deleteLoginInfo(oldToken, username);
        // 缓存登录信息
        cacheLoginInfo(newJwtToken, loginSysUserRedisVo);
    }

    @Override
    public LoginUserRedisVo getLoginSysUserRedisVo(String username) {
        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException("username不能为空");
        }
        return (LoginUserRedisVo) redisTemplate.opsForValue().get(String.format(CommonRedisKey.LOGIN_USER, username));
    }

    //TODO 错误
    @Override
    public boolean exists(String token) {
        if (token == null) {
            throw new IllegalArgumentException("token不能为空");
        }
        String tokenMd5 = DigestUtils.md5Hex(token);
        Object object = redisTemplate.opsForValue().get(String.format(CommonRedisKey.LOGIN_TOKEN, tokenMd5));
        return object != null;
    }

    @Override
    public String getSalt(String username) {
        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException("username不能为空");
        }
        String salt = (String) redisTemplate.opsForValue().get(String.format(CommonRedisKey.LOGIN_SALT, username));
        return salt;
    }

    @Override
    public void refreshToken(JwtToken jwtToken, HttpServletResponse httpServletResponse) throws Exception {
        if (jwtToken == null) {
            return;
        }
        String token = jwtToken.getToken();
        if (StringUtils.isBlank(token)) {
            return;
        }
        // 判断是否刷新token
        boolean isRefreshToken = jwtProperties.isRefreshToken();
        if (!isRefreshToken) {
            return;
        }
        // 获取过期时间
        Date expireDate = JwtUtil.getExpireDate(token);
        // 获取倒计时
        Integer countdown = jwtProperties.getRefreshTokenCountdown();
        // 如果(当前时间+倒计时) > 过期时间，则刷新token
        boolean refresh = DateUtils.addSeconds(new Date(), countdown).after(expireDate);

        if (!refresh) {
            return;
        }

        // 如果token继续发往后台，则提示，此token已失效，请使用新token，不在返回新token，返回状态码：461
        // 如果Redis缓存中没有，JwtToken没有过期，则说明，已经刷新token
        boolean exists = this.exists(token);
        if (!exists) {
            httpServletResponse.setStatus(JwtConstant.JWT_INVALID_TOKEN_CODE);
            throw new AuthenticationException("token已无效，请使用已刷新的token");
        }
        String username = jwtToken.getUsername();
        String salt = jwtToken.getSalt();
        Long expireSecond = jwtProperties.getExpireSecond();
        // 生成新token字符串
        String newToken = JwtUtil.sign(username, salt, Duration.ofSeconds(expireSecond));
        // 生成新JwtToken对象
        JwtToken newJwtToken = JwtToken.build(newToken, username, salt, expireSecond);
        // 更新redis缓存
        refreshLoginInfo(token, username, newJwtToken);
        log.debug("刷新token成功，原token:{}，新token:{}", token, newToken);
        // 设置响应头
        // 刷新token
        httpServletResponse.setStatus(JwtConstant.JWT_REFRESH_TOKEN_CODE);
        httpServletResponse.setHeader(RequestConstant.X_ACCESS_TOKEN, newToken);
    }

    @Override
    public void cacheLoginInfo(JwtToken jwtToken, LoginUserVo loginSysUserVo) {
        if (jwtToken == null) {
            throw new IllegalArgumentException("jwtToken不能为空");
        }
        if (loginSysUserVo == null) {
            throw new IllegalArgumentException("loginSysUserVo不能为空");
        }

        // token
        String token = jwtToken.getToken();
        // 盐值
        String salt = jwtToken.getSalt();
        // 登录用户名称
        String username = loginSysUserVo.getUsername();
        // token md5值
        String tokenMd5 = DigestUtils.md5Hex(token);

        // 用户客户端信息
        ClientInfo clientInfo = ClientInfoUtil.get(ApplicationContextProvider.getHttpServletRequest());

        LoginUserRedisVo loginUserRedisVo = LoginUserRedisVoConvert.INSTANCE.toLoginUserRedisVo(loginSysUserVo);
        loginUserRedisVo.setClientInfo(clientInfo);

        // Redis过期时间与JwtToken过期时间一致
        Duration expireDuration = Duration.ofSeconds(jwtToken.getExpireSecond());
        // 判断是否启用单个用户登录，如果是，这每个用户只有一个有效token
        boolean singleLogin = jwtProperties.isSingleLogin();
        if (singleLogin) {
            deleteUserAllCache(username);
        }

        // 1. tokenMd5:jwtTokenRedisVo
        String loginTokenRedisKey = String.format(CommonRedisKey.LOGIN_TOKEN, tokenMd5);
        redisTemplate.opsForValue().set(loginTokenRedisKey, jwtToken, expireDuration);
        // 2. username:loginSysUserRedisVo
        redisTemplate.opsForValue().set(String.format(CommonRedisKey.LOGIN_USER, username), loginUserRedisVo, expireDuration);
        // 3. salt hash,方便获取盐值鉴权
        redisTemplate.opsForValue().set(String.format(CommonRedisKey.LOGIN_SALT, username), salt, expireDuration);
        // 4. login user token
        redisTemplate.opsForValue().set(String.format(CommonRedisKey.LOGIN_USER_TOKEN, username, tokenMd5), loginTokenRedisKey, expireDuration);
    }

    @Override
    public void deleteUserAllCache(String username) {
        Set<String> userTokenMd5Set = redisTemplate.keys(String.format(CommonRedisKey.LOGIN_USER_ALL_TOKEN, username));
        if (CollectionUtils.isEmpty(userTokenMd5Set)) {
            return;
        }

        // 1. 删除登录用户的所有token信息
        List<String> tokenMd5List = redisTemplate.opsForValue().multiGet(userTokenMd5Set);
        redisTemplate.delete(tokenMd5List);
        // 2. 删除登录用户的所有user:token信息
        redisTemplate.delete(userTokenMd5Set);
        // 3. 删除登录用户信息
        redisTemplate.delete(String.format(CommonRedisKey.LOGIN_USER, username));
        // 4. 删除登录用户盐值信息
        redisTemplate.delete(String.format(CommonRedisKey.LOGIN_SALT, username));
    }

    @Override
    public void deleteLoginInfo(String token, String username) {
        if (token == null) {
            throw new IllegalArgumentException("token不能为空");
        }
        if (username == null) {
            throw new IllegalArgumentException("username不能为空");
        }
        String tokenMd5 = DigestUtils.md5Hex(token);
        // 1. delete tokenMd5
        redisTemplate.delete(String.format(CommonRedisKey.LOGIN_TOKEN, tokenMd5));
        // 2. delete username
        redisTemplate.delete(String.format(CommonRedisKey.LOGIN_USER, username));
        // 3. delete salt
        redisTemplate.delete(String.format(CommonRedisKey.LOGIN_SALT, username));
        // 4. delete user token
        redisTemplate.delete(String.format(CommonRedisKey.LOGIN_USER_TOKEN, username, tokenMd5));
    }


}
