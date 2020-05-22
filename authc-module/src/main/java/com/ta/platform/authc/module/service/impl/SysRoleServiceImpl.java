package com.ta.platform.authc.module.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ta.platform.authc.module.entity.SysRole;
import com.ta.platform.authc.module.mapper.SysRoleMapper;
import com.ta.platform.authc.module.service.ISysRoleService;
import org.springframework.stereotype.Service;

/**
 * Creator: zhuji
 * Date: 4/23/2020
 * Time: 10:46 AM
 * Description: 角色服务类
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
}
