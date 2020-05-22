package com.ta.platform.authc.module.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ta.platform.authc.module.entity.SysUserRole;
import com.ta.platform.authc.module.vo.SysRoleUserMapModel;

/**
 * Creator: zhuji
 * Date: 4/23/2020
 * Time: 11:10 AM
 * Description:
 */
public interface ISysUserRoleService extends IService<SysUserRole> {
    IPage<SysRoleUserMapModel> getUserListByRole(Page page, String roleId);
}
