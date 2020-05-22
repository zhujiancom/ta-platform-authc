package com.ta.platform.authc.module.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ey.tax.toolset.core.StrUtil;
import com.ey.tax.toolset.core.collection.CollectionUtil;
import com.ta.platform.authc.module.entity.SysRole;
import com.ta.platform.authc.module.entity.SysUserRole;
import com.ta.platform.authc.module.service.ISysRoleService;
import com.ta.platform.authc.module.service.ISysUserRoleService;
import com.ta.platform.authc.module.vo.SysRoleUserMapModel;
import com.ta.platform.authc.module.vo.SysUserRoleVO;
import com.ta.platform.authc.query.QueryGenerator;
import com.ta.platform.common.api.ApiCode;
import com.ta.platform.common.api.vo.Result;
import com.ta.platform.common.constant.CommonConstant;
import com.ta.platform.common.service.ISysBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Creator: zhuji
 * Date: 4/23/2020
 * Time: 10:41 AM
 * Description:
 */
@Slf4j
@RestController
@RequestMapping(value = "/sys/role")
public class SysRoleController {

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private ISysBaseService sysBaseService;

    /**
     * 查找所有的角色
     *
     * @return
     */
    @GetMapping(value = "/all-list")
    public Result<List<Object>> listAll() {
        List<SysRole> list = sysRoleService.list();
        if (CollectionUtil.isEmpty(list)) {
            return Result.result(ApiCode.FAIL, "未找到角色列表信息", null);
        } else {
            List<Object> roleList = list.stream().map(item -> JSONObject.toJSON(item)).collect(Collectors.toList());
            return Result.ok(roleList);
        }
    }

    @GetMapping(value = "/user-role-list")
    public Result<List<Object>> userRoleList(@RequestParam("userid") String userId) {
        List<SysUserRole> userRoles = sysUserRoleService.list(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, userId));
        if (CollectionUtil.isEmpty(userRoles)) {
            return Result.result(ApiCode.FAIL, StrUtil.format("未找到用户{}相关的角色信息", userId), null);
        } else {
            List<Object> roleIdList = userRoles.stream().map(ur -> ur.getRoleId()).collect(Collectors.toList());
            return Result.ok(roleIdList);
        }
    }

    @GetMapping(value = "/page-list")
    public Result<Object> rolesPageList(SysRole role, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                        HttpServletRequest request) {
        QueryWrapper<SysRole> queryWrapper = QueryGenerator.initQueryWrapper(role, request.getParameterMap());
        Page<SysRole> page = new Page(pageNo, pageSize);
        IPage<SysRole> pageList = sysRoleService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    @PostMapping(value = "/add")
    public Result<Boolean> add(@RequestBody SysRole role) {
        try {
            sysRoleService.save(role);
            return Result.ok();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error();
        }
    }

    @PutMapping(value = "/edit")
    public Result<Boolean> result(@RequestBody SysRole role) {
        try {
            sysBaseService.addLog(StrUtil.format("编辑角色【{}】，id:{}", role.getRoleName(), role.getId()), CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_3);
            SysRole sysRole = sysRoleService.getById(role.getId());
            if (sysRole == null) {
                return Result.error("未找到对应的实体");
            } else {
                boolean ok = sysRoleService.updateById(role);
                if (ok) {
                    return Result.ok();
                } else {
                    return Result.error("数据无法更新！");
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error();
        }
    }

    @DeleteMapping(value = "/delte")
    public Result<Boolean> deleteRole(@RequestParam(name = "id", required = true) String id) {
        boolean ok = sysRoleService.removeById(id);
        sysBaseService.addLog(StrUtil.format("删除角色，id：{}", id), CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_4);
        if (ok) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }

    /**
     * 查找角色下的用户
     *
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping(value = "/page-list-by-role")
    public Result<Object> userListByRole(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                         @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                         HttpServletRequest request) {
        String roleId = request.getParameter("roleId");
        sysBaseService.addLog(StrUtil.format("分页查询角色{}下所有用户", roleId), CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_1);
        Page<SysRoleUserMapModel> page = new Page(pageNo, pageSize);
        IPage<SysRoleUserMapModel> pageList = sysUserRoleService.getUserListByRole(page, roleId);
        return Result.ok(pageList);
    }

    @DeleteMapping(value = "/userrole/delete")
    public Result<Boolean> deleteSysUserRole(@RequestParam(name = "id", required = true) String id) {
        boolean ok = sysUserRoleService.removeById(id);
        if (ok) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }

    @PostMapping(value = "/adduser")
    public Result<Boolean> addSysUserRole(@RequestBody SysUserRoleVO sysUserRoleVO) {
        sysBaseService.addLog(StrUtil.format("给角色【{}】添加用户", sysUserRoleVO.getRoleId()), CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_3);
        if (CollectionUtil.isEmpty(sysUserRoleVO.getUserIds())) {
            return Result.error("没有选择要添加的用户");
        } else {
            List<SysUserRole> sysUserRoles = sysUserRoleVO.getUserIds().stream().map(userId -> new SysUserRole(userId, sysUserRoleVO.getRoleId())).collect(Collectors.toList());
            boolean ok = sysUserRoleService.saveBatch(sysUserRoles);
            if (ok) {
                return Result.ok();
            } else {
                return Result.error();
            }
        }
    }

}
