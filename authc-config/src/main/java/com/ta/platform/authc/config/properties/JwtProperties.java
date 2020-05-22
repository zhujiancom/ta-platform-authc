package com.ta.platform.authc.config.properties;

import com.ta.platform.authc.config.constant.JwtConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Creator: zhuji
 * Date: 5/20/2020
 * Time: 12:36 PM
 * Description: jwt属性配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "taplatform.jwt")
public class JwtProperties {
    /**
     * token失效时间,默认1小时，60*60=3600
     */
    private Long expireSecond = JwtConstant.JWT_DEFAULT_EXPIRE_SECOND;

    /**
     * 密码
     */
    private String secret = JwtConstant.JWT_DEFAULT_SECRET;

    /**
     * 是否刷新token，默认为true
     */
    private boolean refreshToken = true;

    /**
     * 刷新token倒计时，默认10分钟，10*60=600
     */
    private Integer refreshTokenCountdown;

    /**
     * redis校验jwt token是否存在
     */
    private boolean redisCheck;

    /**
     * 单用户登录，一个用户只能又一个有效的token
     */
    private boolean singleLogin;

    /**
     * 是否进行盐值校验
     */
    private boolean saltCheck;
}
