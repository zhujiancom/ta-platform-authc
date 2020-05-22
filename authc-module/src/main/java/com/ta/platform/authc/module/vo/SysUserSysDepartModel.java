package com.ta.platform.authc.module.vo;

import com.ta.platform.authc.module.entity.SysDepart;
import com.ta.platform.authc.module.entity.SysUser;
import lombok.Data;

/**
 * Creator: zhuji
 * Date: 3/19/2020
 * Time: 3:29 PM
 * Description: 用户部门模型 包含 SysUser 和 SysDepart 的 Model
 */
@Data
public class SysUserSysDepartModel {
    private SysUser sysUser;
    private SysDepart sysDepart;
}
