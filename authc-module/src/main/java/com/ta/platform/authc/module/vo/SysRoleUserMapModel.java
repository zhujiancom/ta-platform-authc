package com.ta.platform.authc.module.vo;

import com.ta.platform.common.aspect.annotation.Dict;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Creator: zhuji
 * Date: 4/27/2020
 * Time: 7:12 PM
 * Description: 角色管理用户模型
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysRoleUserMapModel implements Serializable {
    private String id;

    private String roleId;

    private String userId;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 用户名称
     */
    private String realName;

    /**
     * 用户状态
     */
    @Dict(dictCode = "user_status")
    private String status;

    /**
     * 工号
     */
    private String workNo;
}
