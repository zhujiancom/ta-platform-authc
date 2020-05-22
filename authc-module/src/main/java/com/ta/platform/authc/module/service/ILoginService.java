package com.ta.platform.authc.module.service;

import com.ta.platform.authc.vo.LoginUserTokenVo;
import com.ta.platform.common.system.model.SysLoginModel;

import javax.servlet.http.HttpServletRequest;

/**
 * Creator: zhuji
 * Date: 5/20/2020
 * Time: 5:45 PM
 * Description:
 */
public interface ILoginService {
    /**
     * 登录
     *
     * @param loginParam
     * @return
     * @throws Exception
     */
    LoginUserTokenVo login(SysLoginModel loginParam);

    /**
     * 退出
     * @param request
     * @throws Exception
     */
    void logout(HttpServletRequest request) throws Exception;

    /**
     * 检查验证码是否正确
     *
     * @param verifyToken
     * @param key
     * @throws Exception
     */
    void checkVerifyCode(String verifyToken, String key);

    /**
     * 生成验证码
     * @param key
     * @return
     */
    String generateVerifyCode(String key);
}
