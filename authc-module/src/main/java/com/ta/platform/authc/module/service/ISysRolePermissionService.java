package com.ta.platform.authc.module.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ta.platform.authc.module.entity.SysRolePermission;

/**
 * Creator: zhuji
 * Date: 5/25/2020
 * Time: 4:57 PM
 * Description:
 */
public interface ISysRolePermissionService extends IService<SysRolePermission> {

    /**
     * 保存授权 将上次的权限和这次作比较 差异处理提高效率
     * @param roleId
     * @param permissionIds
     * @param lastPermissionIds
     */
    void saveRolePermission(String roleId, String permissionIds, String lastPermissionIds);
}
