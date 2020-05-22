package com.ta.platform.authc.module.service;

import com.ta.platform.authc.module.shiro.jwt.JwtToken;
import com.ta.platform.common.vo.LoginUserRedisVo;
import com.ta.platform.common.vo.LoginUserVo;

import javax.servlet.http.HttpServletResponse;

/**
 * Creator: zhuji
 * Date: 5/20/2020
 * Time: 11:04 AM
 * Description: 登录信息Redis缓存操作服务
 */
public interface ILoginRedisService {
    /**
     * 刷新登录信息
     *
     * @param oldToken
     * @param username
     * @param newJwtToken
     */
    void refreshLoginInfo(String oldToken, String username, JwtToken newJwtToken);

    /**
     * 通过用户名，从缓存中获取登录用户LoginSysUserRedisVo
     *
     * @param username
     * @return
     */
    LoginUserRedisVo getLoginSysUserRedisVo(String username);

    /**
     * 判断token在redis中是否存在
     *
     * @param token
     * @return
     */
    boolean exists(String token);

    /**
     * 通过用户名称获取盐值
     * @param username
     * @return
     */
    String getSalt(String username);

    /**
     * 如果(当前时间+倒计时) > 过期时间，则刷新token
     * 并更新缓存
     * 当前token失效，返回新token
     * 当前请求有效，返回状态码：460
     * 前端下次使用新token
     * 如果token继续发往后台，则提示，此token已失效，请使用新token，不在返回新token，返回状态码：461
     *
     * @param jwtToken
     * @param httpServletResponse
     * @throws Exception
     */
    void refreshToken(JwtToken jwtToken, HttpServletResponse httpServletResponse) throws Exception;

    /**
     * 缓存登录信息
     * @param jwtToken
     * @param loginSysUserVo
     */
    void cacheLoginInfo(JwtToken jwtToken, LoginUserVo loginSysUserVo);

    /**
     * 删除用户所有登录缓存
     * @param username
     */
    void deleteUserAllCache(String username);

    /**
     * 删除对应用户的Redis缓存
     *
     * @param token
     * @param username
     */
    void deleteLoginInfo(String token, String username);
}
