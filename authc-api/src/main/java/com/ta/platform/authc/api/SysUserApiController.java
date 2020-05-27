package com.ta.platform.authc.api;

import com.ta.platform.authc.module.entity.SysUser;
import com.ta.platform.authc.module.service.ISysUserService;
import com.ta.platform.common.api.vo.Result;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Creator: zhuji
 * Date: 5/26/2020
 * Time: 6:50 PM
 * Description:
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
@Api(tags = "用户信息相关服务")
public class SysUserApiController {

    @Autowired
    private ISysUserService sysUserService;

    @GetMapping(value = "/all-list")
    public Result<String[]> getAllUsers(){
        List<SysUser> userList = sysUserService.list();
        List<String> userIds = userList.stream().map(u -> u.getId()).collect(Collectors.toList());
        return Result.ok(userIds.toArray(new String[0]));
    }
}
