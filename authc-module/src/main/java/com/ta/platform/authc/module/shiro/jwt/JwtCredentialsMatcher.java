package com.ta.platform.authc.module.shiro.jwt;

import com.ta.platform.common.system.api.ISysBaseAPI;
import com.ta.platform.common.tool.JwtUtil;
import com.ta.platform.common.vo.LoginUserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Creator: zhuji
 * Date: 5/20/2020
 * Time: 11:09 AM
 * Description: JWT证书匹配, 校验token有效性
 */
@Slf4j
@Component
public class JwtCredentialsMatcher implements CredentialsMatcher {

    private final ISysBaseAPI sysBaseAPI;

    public JwtCredentialsMatcher(@Autowired ISysBaseAPI sysBaseAPI) {
        this.sysBaseAPI = sysBaseAPI;
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        log.info("———校验token是否有效———");
        try {
            String token = authenticationToken.getCredentials().toString();
            JwtToken jwtToken = (JwtToken) authenticationToken;
            String username = jwtToken.getUsername();
            String salt = jwtToken.getSalt();
            LoginUserVo loginUser = (LoginUserVo) authenticationInfo.getPrincipals().getPrimaryPrincipal();
            if (loginUser == null) {
                throw new AuthenticationException("用户不存在!");
            }
            // 判断用户状态
            if (loginUser.getStatus() != 1) {
                throw new AuthenticationException("账号已被锁定,请联系管理员!");
            }
            return JwtUtil.verify(token, username, salt);
        } catch (Exception e) {
            log.error("JWT Token CredentialsMatch Exception:" + e.getMessage(), e);
        }
        return false;
    }
}
