package com.ta.platform.authc.module.controller;

import com.ta.platform.authc.module.service.ILoginService;
import com.ta.platform.authc.vo.LoginUserTokenVo;
import com.ta.platform.common.api.ApiCode;
import com.ta.platform.common.api.vo.Result;
import com.ta.platform.common.system.model.SysLoginModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Creator: zhuji
 * Date: 5/20/2020
 * Time: 3:36 PM
 * Description:
 */
@Slf4j
@RestController
@RequestMapping(value = "/sys")
@Api(value = "系统登录API", tags = {"系统登录"})
public class LoginController {

    @Autowired
    private ILoginService loginService;

    @ApiOperation("登录接口")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result<LoginUserTokenVo> login(@RequestBody SysLoginModel sysLoginModel){
        LoginUserTokenVo loginSysUserTokenVo = loginService.login(sysLoginModel);
        return Result.ok(loginSysUserTokenVo);
    }

    /**
     * 后台生成图形验证码
     * @param key
     */
    @ApiOperation("生成验证码")
    @GetMapping(value = "/randomImage/{key}")
    public Result<String> randomImage(@PathVariable String key){
        String verifyCode = loginService.generateVerifyCode(key);
        return Result.ok(verifyCode, "验证码生成成功");
    }

    /**
     * 退出登录
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/logout")
    public Result<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            loginService.logout(request);
            return Result.ok();
        } catch (Exception e) {
            return Result.error("登出失败， Token无效!");
        }
    }
}
