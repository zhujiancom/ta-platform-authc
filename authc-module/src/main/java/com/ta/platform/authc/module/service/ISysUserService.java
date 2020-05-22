package com.ta.platform.authc.module.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ta.platform.authc.module.entity.SysUser;
import com.ta.platform.authc.module.vo.SysUserDepVo;
import com.ta.platform.authc.module.vo.SysUserSysDepartModel;
import com.ta.platform.common.api.vo.Result;
import com.ta.platform.common.system.model.SysUserCacheInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Creator: zhuji
 * Date: 5/20/2020
 * Time: 5:52 PM
 * Description:
 */
public interface ISysUserService extends IService<SysUser> {
    /**
     * 重置密码
     *
     * @param username
     * @param oldpassword
     * @param newpassword
     * @param confirmpassword
     * @return
     */
    Result<?> resetPassword(String username, String oldpassword, String newpassword, String confirmpassword);

    /**
     * 修改密码
     *
     * @param sysUser
     * @return
     */
    Result<?> changePassword(SysUser sysUser);

    /**
     * 删除用户
     * @param userId
     * @return
     */
    boolean deleteUser(String userId);

    /**
     * 批量删除用户
     * @param userIds
     * @return
     */
    boolean deleteBatchUsers(String userIds);

    SysUser getUserByName(String username);

    /**
     * 添加用户和用户角色关系
     * @param user
     * @param roles
     */
    void addUserWithRole(SysUser user, String roles);


    /**
     * 修改用户和用户角色关系
     * @param user
     * @param roles
     */
    void editUserWithRole(SysUser user, String roles);

    /**
     * 获取用户的授权角色
     * @param username
     * @return
     */
    List<String> getRole(String username);

    /**
     * 查询用户信息包括 部门信息
     * @param username
     * @return
     */
    SysUserCacheInfo getCacheUser(String username);

    /**
     * 根据部门Id查询
     * @param
     * @return
     */
    IPage<SysUser> getUserByDepId(Page<SysUser> page, String departId, String username);

    /**
     * 根据部门Ids查询
     * @param
     * @return
     */
    IPage<SysUser> getUserByDepIds(Page<SysUser> page, List<String> departIds, String username);

    /**
     * 根据 userIds查询，查询用户所属部门的名称（多个部门名逗号隔开）
     * @param
     * @return
     */
    Map<String,List<SysUserDepVo>> getDepsByUserIds(List<String> userIds);

    /**
     * 根据部门 Id 和 QueryWrapper 查询
     *
     * @param page
     * @param departId
     * @param queryWrapper
     * @return
     */
    IPage<SysUser> getUserByDepartIdAndQueryWrapper(Page<SysUser> page, String departId, QueryWrapper<SysUser> queryWrapper);

    /**
     * 根据 orgCode 查询用户，包括子部门下的用户
     *
     * @param orgCode
     * @param userParams 用户查询条件，可为空
     * @param page 分页参数
     * @return
     */
    IPage<SysUserSysDepartModel> queryUserByOrgCode(String orgCode, SysUser userParams, IPage page);

    /**
     * 根据角色Id查询
     * @param
     * @return
     */
    IPage<SysUser> getUserByRoleId(Page<SysUser> page, String roleId, String username);

    /**
     * 通过用户名获取用户角色集合
     *
     * @param username 用户名
     * @return 角色集合
     */
    Set<String> getUserRolesSet(String username);

    /**
     * 通过用户名获取用户权限集合
     *
     * @param username 用户名
     * @return 权限集合
     */
    Set<String> getUserPermissionsSet(String username);

    /**
     * 根据用户名设置部门ID
     * @param username
     * @param orgCode
     */
    void updateUserDepart(String username, String orgCode);

    /**
     * 根据手机号获取用户名和密码
     */
    SysUser getUserByPhone(String phone);


    /**
     * 根据邮箱获取用户
     */
    SysUser getUserByEmail(String email);


    /**
     * 添加用户和用户部门关系
     * @param user
     * @param selectedParts
     */
    void addUserWithDepart(SysUser user, String selectedParts);

    /**
     * 编辑用户和用户部门关系
     * @param user
     * @param departs
     */
    void editUserWithDepart(SysUser user, String departs);

    /**
     * 校验用户是否有效
     * @param sysUser
     * @return
     */
    void checkUserIsEffective(SysUser sysUser);

    /**
     * 查询被逻辑删除的用户
     */
    List<SysUser> queryLogicDeleted();

    /**
     * 查询被逻辑删除的用户（可拼装查询条件）
     */
    List<SysUser> queryLogicDeleted(LambdaQueryWrapper<SysUser> wrapper);

    /**
     * 还原被逻辑删除的用户
     */
    boolean revertLogicDeleted(List<String> userIds, SysUser updateEntity);

    /**
     * 彻底删除被逻辑删除的用户
     */
    boolean removeLogicDeleted(List<String> userIds);

    /**
     * 根据 userIds查询，查询用户所属部门的名称（多个部门名逗号隔开）
     * @param
     * @return
     */
    Map<String, String> getDepNamesByUserIds(List<String> userIds);
}
