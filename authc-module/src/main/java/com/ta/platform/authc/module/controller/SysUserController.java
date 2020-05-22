package com.ta.platform.authc.module.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ey.tax.toolset.core.RandomUtil;
import com.ey.tax.toolset.core.StrUtil;
import com.ey.tax.toolset.core.collection.CollectionUtil;
import com.ta.platform.authc.module.entity.SysUser;
import com.ta.platform.authc.module.entity.SysUserRole;
import com.ta.platform.authc.module.service.ISysUserRoleService;
import com.ta.platform.authc.module.service.ISysUserService;
import com.ta.platform.authc.module.vo.SysUserDepVo;
import com.ta.platform.authc.query.QueryGenerator;
import com.ta.platform.common.api.vo.Result;
import com.ta.platform.common.constant.CommonConstant;
import com.ta.platform.common.service.ISysBaseService;
import com.ta.platform.common.tool.EncryptUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Creator: zhuji
 * Date: 4/14/2020
 * Time: 2:55 PM
 * Description: 系统用户相关操作
 */
@Slf4j
@RestController
@RequestMapping("/sys/user")
@Api("系统用户相关操作")
public class SysUserController {
    @Resource
    private ISysUserService sysUserService;

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private ISysBaseService sysBaseService;

    @GetMapping(value = "/info")
    public Result<?> getUserInfo(@RequestParam("id") String id) {
        try {
            SysUser sysUser = sysUserService.getById(id);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("model", sysUser);

            // 查找用户所拥有的角色
            List<SysUserRole> userRoles = sysUserRoleService.list(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, id));
            List<Object> roleIdList = userRoles.stream().map(ur -> ur.getRoleId()).collect(Collectors.toList());
            jsonObject.put("selectedRole", roleIdList);
            return Result.ok(jsonObject, "获取用户信息成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("获取用户信息出错！");
        }
    }

    /**
     * 首页用户重置密码
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.PUT)
    public Result<?> changPassword(@RequestBody JSONObject json) {
        String username = json.getString("username");
        String oldpassword = json.getString("oldpassword");
        String password = json.getString("password");
        String confirmpassword = json.getString("confirmpassword");
        SysUser user = this.sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
        if (user == null) {
            return Result.error("用户不存在！");
        }
        return sysUserService.resetPassword(username, oldpassword, password, confirmpassword);
    }

    @RequestMapping(value = "/page-list", method = RequestMethod.GET)
    public Result<Object> getAllUserPageList(SysUser user,
                                             @RequestParam("pageNo") Integer pageNo,
                                             @RequestParam("pageSize") Integer pageSize,
                                             HttpServletRequest req) {
        QueryWrapper<SysUser> queryWrapper = QueryGenerator.initQueryWrapper(user, req.getParameterMap());
        Page<SysUser> page = new Page<>(pageNo, pageSize);
        IPage<SysUser> pageList = sysUserService.page(page, queryWrapper);

        // 查询用户所属的部门
        List<String> userIds = pageList.getRecords().stream().map(SysUser::getId).collect(Collectors.toList());
        Map<String, List<SysUserDepVo>> userDepList = sysUserService.getDepsByUserIds(userIds);

        pageList.getRecords().forEach(item -> {
            item.setDepartments(userDepList.get(item.getId()));
        });

//        List<JSONObject> realPageList = pageList.getRecords().stream().map(item -> DictHelper.parseDictField(item)).collect(Collectors.toList());
//        IPage newPageList = pageList;
//        newPageList.setRecords(realPageList);

//        result.setResult(JSONObject.toJSON(newPageList));

        return Result.ok(pageList);
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result<?> addUser(@RequestBody JSONObject jsonObject) {
        String selectedRoles = jsonObject.getString("selectedroles");
        String selectedDeparts = jsonObject.getString("selecteddeparts");
        try {
            SysUser user = JSON.parseObject(jsonObject.toJSONString(), SysUser.class);
            user.setCreateTime(new Date());
            String salt = RandomUtil.randomString(8);
            String passwordEncode = EncryptUtil.encrypt(user.getUsername(), user.getPassword(), salt);
            user.setPassword(passwordEncode);
            user.setStatus(Integer.valueOf(CommonConstant.STATUS_1));
            user.setDelFlag(String.valueOf(CommonConstant.DEL_FLAG_0));
            user.setSalt(salt);
            sysUserService.addUserWithRole(user, selectedRoles);
            sysUserService.addUserWithDepart(user, selectedDeparts);
            sysBaseService.addLog(StrUtil.format("新增用户【{}】", user.getUsername()),CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_2);
            return Result.ok();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Result.error();
    }

    @RequestMapping(value = "edit", method = RequestMethod.PUT)
    public Result<?> edit(@RequestBody JSONObject jsonObject) {
        try {
            String userId = jsonObject.getString("id");
            SysUser sysUser = sysUserService.getById(userId);
            sysBaseService.addLog(StrUtil.format("编辑用户【{}】，id:{}", sysUser.getUsername(), userId),CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_3);
            if (sysUser == null) {
                return Result.error(StrUtil.format("未找到此用户[id={}]数据", userId));
            } else {
                SysUser user = JSON.parseObject(jsonObject.toJSONString(), SysUser.class);
                user.setUpdateTime(new Date());
                user.setPassword(sysUser.getPassword());
                String roles = jsonObject.getString("selectedroles");
                String departs = jsonObject.getString("selecteddeparts");
                sysUserService.editUserWithRole(user, roles);
                sysUserService.editUserWithDepart(user, departs);
                return Result.ok();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public Result<?> delete(@RequestParam("id") String id) {
        sysBaseService.addLog(StrUtil.format("删除用户，id：{}", id) ,CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_4);
        try {
            boolean ok = sysUserService.removeById(id);
            if (ok) {
                return Result.ok();
            } else {
                return Result.error();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("删除失败," + e.getMessage());
        }
    }

    /**
     * 批量删除用户
     */
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        sysBaseService.addLog(StrUtil.format("批量删除用户， ids：{}" ,ids) ,CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_4);
        this.sysUserService.deleteBatchUsers(ids);
        return Result.ok("批量删除用户成功");
    }

    @PutMapping(value = "/frozenBatch")
    public Result<?> frozenUser(@RequestBody JSONObject jsonObject) {
        try {
            String ids = jsonObject.getString("ids");
            String status = jsonObject.getString("status");
            List<String> idArray = StrUtil.split(ids, ',');
            if (CollectionUtil.isNotEmpty(idArray)) {
                idArray.stream().filter(id -> StrUtil.isNotEmpty(id)).forEach(id -> {
                    this.sysUserService.update(new SysUser().setStatus(Integer.parseInt(status)),
                            new UpdateWrapper<SysUser>().lambda().eq(SysUser::getId, id));
                });
            }
            return Result.ok();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Result.error();
    }

    /**
     * 获取被逻辑删除的用户列表，无分页
     *
     * @return logicDeletedUserList
     */
    @GetMapping("/recycleBin")
    public Result getRecycleBin() {
        List<SysUser> logicDeletedUserList = sysUserService.queryLogicDeleted();
        if (logicDeletedUserList.size() > 0) {
            // 批量查询用户的所属部门
            // step.1 先拿到全部的 userIds
            List<String> userIds = logicDeletedUserList.stream().map(SysUser::getId).collect(Collectors.toList());
            // step.2 通过 userIds，一次性查询用户的所属部门名字
            Map<String, String> useDepNames = sysUserService.getDepNamesByUserIds(userIds);
            logicDeletedUserList.forEach(item -> item.setOrgCode(useDepNames.get(item.getId())));
        }
        return Result.ok(logicDeletedUserList);
    }

    /**
     * 还原被逻辑删除的用户
     *
     * @param userIds 被还原的用户ID，是个 list 集合
     * @return
     */
    @PutMapping("/recycleBin")
    public Result putRecycleBin(@RequestBody List<String> userIds, HttpServletRequest request) {
        if (userIds != null && userIds.size() > 0) {
            SysUser updateUser = new SysUser();
            sysUserService.revertLogicDeleted(userIds, updateUser);
        }
        return Result.ok("还原成功");
    }

    /**
     * 彻底删除用户
     *
     * @param userIds 被删除的用户ID，多个id用半角逗号分割
     * @return
     */
    @DeleteMapping("/recycleBin")
    public Result deleteRecycleBin(@RequestParam("userIds") String userIds) {
        if (StringUtils.isNotBlank(userIds)) {
            sysUserService.removeLogicDeleted(Arrays.asList(userIds.split(",")));
        }
        return Result.ok("删除成功");
    }
}
