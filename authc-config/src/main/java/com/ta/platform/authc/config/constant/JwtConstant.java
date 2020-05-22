package com.ta.platform.authc.config.constant;

/**
 * Creator: zhuji
 * Date: 5/20/2020
 * Time: 12:58 PM
 * Description:
 */
public interface JwtConstant {
    /**
     * JWT 默认过期时间，3600L，单位秒
     */
    Long JWT_DEFAULT_EXPIRE_SECOND = 3600L;

    /**
     * JWT Token默认密钥
     */
    String JWT_DEFAULT_SECRET = "888888";

    /**
     * JWT用户名
     */
    String JWT_USERNAME = "username";

    /**
     * JWT刷新新token响应状态码
     */
    int JWT_REFRESH_TOKEN_CODE = 460;

    /**
     * JWT刷新新token响应状态码，
     * Redis中不存在，但jwt未过期，不生成新的token，返回361状态码
     */
    int JWT_INVALID_TOKEN_CODE = 461;
}
