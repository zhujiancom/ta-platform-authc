package com.ta.platform.authc.module.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ey.tax.toolset.core.StrUtil;
import com.ey.tax.toolset.core.collection.CollectionUtil;
import com.ta.platform.authc.module.entity.SysPermission;
import com.ta.platform.authc.module.entity.SysRolePermission;
import com.ta.platform.authc.module.service.ILoginRedisService;
import com.ta.platform.authc.module.service.ISysPermissionService;
import com.ta.platform.authc.module.service.ISysRolePermissionService;
import com.ta.platform.authc.module.service.treebuild.PermissionTreeModelBuilder;
import com.ta.platform.authc.module.util.PermissionDataUtil;
import com.ta.platform.authc.module.vo.SysPermissionTreeModel;
import com.ta.platform.common.api.ApiCode;
import com.ta.platform.common.api.vo.Result;
import com.ta.platform.common.constant.CommonConstant;
import com.ta.platform.common.system.model.TreeModel;
import com.ta.platform.common.tool.DictHelper;
import com.ta.platform.common.tool.JwtUtil;
import com.ta.platform.common.tool.TreeModelUtil;
import com.ta.platform.common.vo.LoginUserRedisVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Creator: zhuji
 * Date: 4/9/2020
 * Time: 10:31 AM
 * Description: 用户权限接口
 */
@Slf4j
@RestController
@RequestMapping(value = "/sys/permission")
@Api(tags = "用户权限")
public class SysPermissionController {

    @Autowired
    private ISysPermissionService permissionService;

    @Autowired
    private ISysRolePermissionService rolePermissionService;

    @Autowired
    private PermissionTreeModelBuilder permissionTreeModelBuildCallback;

    @Autowired
    private ILoginRedisService loginRedisService;

    /**
     * 加载用户菜单和权限
     *
     * @param token
     * @return
     */
    @ApiOperation("获取用户菜单和权限接口")
    @RequestMapping(value = "/getUserPermissionByToken", method = RequestMethod.GET)
    public Result getUserPermissionByToken(@RequestParam(name = "token", required = true) String token) {
        try {
            if (StrUtil.isEmpty(token)) {
                return Result.error("Token 不允许为空， 请尝试重新登陆");
            }
            log.info(StrUtil.format(" ------ 通过令牌获取用户拥有的访问菜单 ---- TOKEN[{}] ", token));

            String username = JwtUtil.getUsername(token);
            LoginUserRedisVo loginUserRedisVo = loginRedisService.getLoginSysUserRedisVo(username);
            List<SysPermission> metaList = new ArrayList<>();
            if(loginUserRedisVo.getSuperUser() == 1){
                // 所有权限
                // 查询所有权限
                LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<>();
                query.eq(SysPermission::getDelFlag, CommonConstant.DEL_FLAG_0);
                query.orderByAsc(SysPermission::getSortNo);
                metaList = permissionService.list(query);
            }else{
                metaList = permissionService.queryByUser(username);
            }
            if (!PermissionDataUtil.hasHomePage(metaList)) {
                SysPermission homepageMenu = permissionService.list(new LambdaQueryWrapper<SysPermission>().eq(SysPermission::isHomepage, 1)).get(0);
                metaList.add(homepageMenu);
            }

            JSONObject json = new JSONObject();
            // 格式化菜单权限数组
            JSONArray menuJsonArray = new JSONArray();
            getPermissionJsonArray(menuJsonArray, metaList, null);

            // 格式化鉴权数组
            JSONArray authJsonArray = new JSONArray();
            getAuthJsonArray(authJsonArray, metaList);

            // 查询所有按钮权限
            LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<>();
            query.eq(SysPermission::getDelFlag, CommonConstant.DEL_FLAG_0);
            query.eq(SysPermission::getMenuType, CommonConstant.MENU_TYPE_2);
            List<SysPermission> allAuthList = permissionService.list(query);

            JSONArray allAuthJsonArray = new JSONArray();
            allAuthList.stream().forEach(meta -> {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("action", meta.getPerms());
                jsonObject.put("status", meta.getStatus());
                jsonObject.put("type", meta.getPermsType());
                jsonObject.put("describe", meta.getName());
                allAuthJsonArray.add(jsonObject);
            });

            // 路由菜单
            json.put("menu", menuJsonArray);
            // 按钮权限
            json.put("auth", authJsonArray);
            // 全部权限配置 （按钮权限， 访问权限）
            json.put("allAuth", allAuthJsonArray);

            return Result.ok(json, "加载权限信息成功");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return Result.error(ApiCode.FAIL.getCode(), "加载权限信息失败:" + e.getMessage());
        }
    }

    private void getPermissionJsonArray(JSONArray jsonArray, List<SysPermission> metaList, JSONObject parentJson) {
        for (SysPermission permission : metaList) {
            if (permission.getMenuType() == null) {
                continue;
            }
            String tempPid = permission.getParentId();
            JSONObject permissionInfo = PermissionDataUtil.getPermissionJsonObject(permission);
            if (permissionInfo == null) {
                continue;
            }
            if (parentJson == null && StrUtil.isEmpty(tempPid)) {
                jsonArray.add(permissionInfo);
                if (!permission.isLeaf()) { // 如果菜单不是叶子结点， 则递归获取其下所有菜单信息
                    getPermissionJsonArray(jsonArray, metaList, permissionInfo);
                }
            } else if (parentJson != null && StrUtil.isNotEmpty(tempPid) && tempPid.equals(parentJson.getString("id"))) {
                if (permission.getMenuType().equals(CommonConstant.MENU_TYPE_2)) { // 按钮权限
                    JSONObject metaInfo = parentJson.getJSONObject("meta");
                    if (metaInfo.containsKey("permissionList")) {
                        metaInfo.getJSONArray("permissionList").add(permissionInfo);
                    } else {
                        JSONArray permissionList = new JSONArray();
                        permissionList.add(permissionInfo);
                        metaInfo.put("permissionList", permissionList);
                    }
                } else if (permission.getMenuType().equals(CommonConstant.MENU_TYPE_0) || permission.getMenuType().equals(CommonConstant.MENU_TYPE_1)) {
                    if (parentJson.containsKey("children")) {
                        parentJson.getJSONArray("children").add(permissionInfo);
                    } else {
                        JSONArray children = new JSONArray();
                        children.add(permissionInfo);
                        parentJson.put("children", children);
                    }

                    // 如果菜单不是叶子结点， 则递归获取其下所有菜单信息
                    if (!permission.isLeaf()) {
                        getPermissionJsonArray(jsonArray, metaList, permissionInfo);
                    }
                }
            }
        }
    }

    /**
     * 获取权限JSON数组
     *
     * @param jsonArray
     * @param metaList
     */
    private void getAuthJsonArray(JSONArray jsonArray, List<SysPermission> metaList) {
        metaList.stream().filter(p -> p.getMenuType() != null).forEach(p -> {
            JSONObject metaInfo = null;
            if (p.getMenuType().equals(CommonConstant.MENU_TYPE_2) && CommonConstant.STATUS_1.equals(p.getStatus())) {
                metaInfo = new JSONObject();
                metaInfo.put("action", p.getPerms());
                metaInfo.put("type", p.getPermsType());
                metaInfo.put("describe", p.getName());
                jsonArray.add(metaInfo);
            }
        });
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<Object> list() {
        try {
            LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<SysPermission>();
            query.eq(SysPermission::getDelFlag, CommonConstant.DEL_FLAG_0);
            query.orderByAsc(SysPermission::getSortNo);
            List<SysPermission> list = permissionService.list(query);
            Map<String, Object> parameter = new HashMap<>();
//            parameter.put("title", "hasDataRule");
//            parameter.put("a-tree", false);
            List<SysPermissionTreeModel> treeList = TreeModelUtil.getTreeModelList(list, null, permissionTreeModelBuildCallback,parameter);
            List<JSONObject> realTreeList = treeList.stream().map(item -> DictHelper.parseDictField(item)).collect(Collectors.toList());
            return Result.ok(realTreeList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error(ApiCode.FAIL.getCode(), "获取权限列表数据失败！");
        }
    }

    /**
     * 添加菜单
     *
     * @param permission
     * @return
     */
    @RequiresRoles({"admin"})
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<?> add(@RequestBody SysPermission permission) {
        try {
            permission = PermissionDataUtil.formatPermission(permission);
            permissionService.addPermission(permission);
            return Result.ok(permission);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error();
        }
    }

    /**
     * 编辑菜单
     *
     * @param permission
     * @return
     */
    @RequiresRoles({"admin"})
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody SysPermission permission) {
        try {
            permission = PermissionDataUtil.formatPermission(permission);
            permissionService.editPermission(permission);
            return Result.ok();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error();
        }
    }

    /**
     * 查询角色树形列表
     * @return
     */
    @GetMapping(value = "/queryTreeList")
    public Result queryTreeList(){
        List<String> ids = new ArrayList<>();
        LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<SysPermission>();
        query.eq(SysPermission::getDelFlag, CommonConstant.DEL_FLAG_0);
        query.orderByAsc(SysPermission::getSortNo);
        List<SysPermission> list = permissionService.list(query);
        for (SysPermission sysPer : list) {
            ids.add(sysPer.getId());
        }
        Map<String,Object> parameter = new HashMap<>();
        List<SysPermissionTreeModel> treeNodes = TreeModelUtil.getTreeModelList(list, null, permissionTreeModelBuildCallback,parameter);
        Map<String, Object> result = new HashMap<>();
        result.put("treeNodes", treeNodes);
        result.put("ids", ids);

        return Result.ok(result);
    }

    /**
     * 查询角色授权
     * @param roleId
     * @return
     */
    @GetMapping(value = "/queryPermissionsByRole")
    public Result<List<String>> queryPermissionsByRole(@RequestParam(name = "roleId") String roleId){
        List<SysRolePermission> list = rolePermissionService.list(new QueryWrapper<SysRolePermission>().lambda().eq(SysRolePermission::getRoleId, roleId));
        if(CollectionUtil.isNotEmpty(list)){
            List<String> result = list.stream().map(rp -> String.valueOf(rp.getPermissionId())).collect(Collectors.toList());
            return Result.ok(result);
        }
        return Result.ok(new ArrayList<>());
    }
}
