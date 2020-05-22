package com.ta.platform.authc.module.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户部门表
 */
@TableName("t_sys_user_depart")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUserDepart implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**主键id*/
    @TableId(type = IdType.ASSIGN_ID)
	private String id;
	/**用户id*/
	private String userId;
	/**部门id*/
	private String depId;

	/**
	 * 是否是部门负责人
	 */
	private boolean dutyFlag;

	public SysUserDepart(String id, String userId, String depId) {
		super();
		this.id = id;
		this.userId = userId;
		this.depId = depId;
	}

	public SysUserDepart(String id, String departId) {
		this.userId = id;
		this.depId = departId;
	}
	
}
