package com.ta.platform.authc.module.shiro.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ey.tax.toolset.http.HttpUtil;
import com.ta.platform.common.tool.JwtUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.shiro.authc.HostAuthenticationToken;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Creator: zhuji
 * Date: 5/20/2020
 * Time: 11:03 AM
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class JwtToken implements HostAuthenticationToken {
    private String host;

    private String token;

    private String username;

    /**
     * 登录盐值， 可以是用户密码
     */
    private String salt;

    /**
     * 多长时间过期，默认一小时
     */
    private long expireSecond;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 过期日期
     */
    private Date expireDate;

    private String principal;

    private String credentials;

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    public static JwtToken build(String token, String username, String salt, long expireSecond) {
        DecodedJWT decodedJwt = JwtUtil.getJwtInfo(token);
        Date createDate = decodedJwt.getIssuedAt();
        Date expireDate = decodedJwt.getExpiresAt();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ip = HttpUtil.getClientIP(request);
        return JwtToken.builder()
                .username(username)
                .token(token)
                .salt(salt)
                .expireSecond(expireSecond)
                .createDate(createDate)
                .expireDate(expireDate)
                .host(ip)
                .build();
    }
}
