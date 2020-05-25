package com.ta.platform.authc.module.shiro.jwt;

import com.ey.tax.toolset.core.StrUtil;
import com.ta.platform.authc.config.properties.JwtProperties;
import com.ta.platform.authc.module.service.ILoginRedisService;
import com.ta.platform.common.api.ApiCode;
import com.ta.platform.common.api.vo.Result;
import com.ta.platform.common.constant.RequestConstant;
import com.ta.platform.common.tool.HttpServletResponseUtil;
import com.ta.platform.common.tool.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Creator: zhuji
 * Date: 5/20/2020
 * Time: 12:22 PM
 * Description: JWT授权过滤器
 */
@Slf4j
public class JwtFilter extends AuthenticatingFilter {

    private JwtProperties jwtProperties;

    private ILoginRedisService loginRedisService;

    public JwtFilter(ILoginRedisService loginRedisService, JwtProperties jwtProperties) {
        this.loginRedisService = loginRedisService;
        this.jwtProperties = jwtProperties;
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(RequestConstant.X_ACCESS_TOKEN);
        if(StrUtil.isBlank(token)){
            throw new AuthenticationException("token不能为空");
        }
        if(JwtUtil.isExpired(token)){
            throw new AuthenticationException("JWT Token已过期,token:" + token);
        }

        // 如果开启redis二次校验， 则先在redis中判断token是否存在
        if(jwtProperties.isRedisCheck() || jwtProperties.isSingleLogin()){
            boolean redisExpired = loginRedisService.exists(token);
            if (!redisExpired) {
                throw new AuthenticationException("Redis Token不存在,token:" + token);
            }
        }

        String username = JwtUtil.getUsername(token);
        String salt;
        if (jwtProperties.isSaltCheck()){
            salt = loginRedisService.getSalt(username);
        }else{
            salt = jwtProperties.getSecret();
        }

        return JwtToken.build(token, username, salt, jwtProperties.getExpireSecond());
    }

    /**
     * 访问失败处理
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        // 返回401
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // 设置响应码为401或者直接输出消息
        String url = httpServletRequest.getRequestURI();
        log.error("onAccessDenied url：{}", url);
        Result<Boolean> apiResult = Result.error(ApiCode.UNAUTHORIZED);
        HttpServletResponseUtil.printJson(httpServletResponse, apiResult);
        return false;
    }

    /**
     * 判断是否允许访问
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        String url = WebUtils.toHttp(request).getRequestURI();
        log.debug("isAccessAllowed url:{}", url);
        if (this.isLoginRequest(request, response)) {
            return true;
        }
        boolean allowed = false;
        try {
            allowed = executeLogin(request, response);
        } catch (Exception e) {
            log.error("访问错误", e);
        }
        return allowed || super.isPermissive(mappedValue);
    }

    /**
     * 登录成功处理
     * @param token
     * @param subject
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        String url = WebUtils.toHttp(request).getRequestURI();
        log.debug("鉴权成功,token:{},url:{}", token, url);
        // 刷新token
        JwtToken jwtToken = (JwtToken) token;
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        loginRedisService.refreshToken(jwtToken, httpServletResponse);
        return true;
    }

    /**
     * 登录失败处理
     *
     * @param token
     * @param e
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        log.error("登录失败，token:" + token + ",error:" + e.getMessage(), e);
        return false;
    }
}
