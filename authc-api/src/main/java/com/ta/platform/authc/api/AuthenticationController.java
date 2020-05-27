package com.ta.platform.authc.api;

import com.alibaba.fastjson.JSON;
import com.ta.platform.authc.module.service.ILoginRedisService;
import com.ta.platform.common.api.ApiCode;
import com.ta.platform.common.api.vo.Result;
import com.ta.platform.common.constant.RequestConstant;
import com.ta.platform.common.tool.JwtUtil;
import com.ta.platform.common.vo.LoginUserRedisVo;
import com.ta.platform.common.vo.LoginUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Creator: zhuji
 * Date: 4/24/2020
 * Time: 4:59 PM
 * Description:
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@Api(tags = "用户鉴权服务")
public class AuthenticationController {

    @Autowired
    private ILoginRedisService loginRedisService;

    @RequestMapping(value = "/user", method = RequestMethod.GET, produces = {"application/json"})
    @ApiOperation("获取当前登录人的完整信息")
    public Result<LoginUserRedisVo> getLoginUser(@RequestParam(value = RequestConstant.X_ACCESS_TOKEN) String token){

        try {
            String userName = JwtUtil.getUsername(token);
            LoginUserRedisVo loginUserRedisVo = loginRedisService.getLoginSysUserRedisVo(userName);
            return Result.ok(loginUserRedisVo);
        } catch (Exception e) {
            return Result.result(ApiCode.AUTHENTICATION_EXCEPTION,"没有找到用户登录信息， 请重新登录！", null);
        }
    }
}
