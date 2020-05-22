package com.ta.platform.authc.module.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ta.platform.authc.module.entity.SysUserRole;
import com.ta.platform.authc.module.mapper.SysUserRoleMapper;
import com.ta.platform.authc.module.service.ISysUserRoleService;
import com.ta.platform.authc.module.vo.SysRoleUserMapModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Creator: zhuji
 * Date: 4/23/2020
 * Time: 11:10 AM
 * Description:
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public IPage<SysRoleUserMapModel> getUserListByRole(Page page, String roleId) {
        return sysUserRoleMapper.getUserListByRoleId(page, roleId);
    }
}
