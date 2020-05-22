package com.ta.platform.authc.module.vo;

import lombok.Data;

/**
 * Creator: zhuji
 * Date: 3/19/2020
 * Time: 3:27 PM
 * Description: 用户所属部门名称信息
 */
@Data
public class SysUserDepVo {
    private String userId;

    private String departId;

    private String departName;

    private String orgCode;

    private boolean dutyFlag;
}
