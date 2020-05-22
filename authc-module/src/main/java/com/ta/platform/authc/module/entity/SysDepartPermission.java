package com.ta.platform.authc.module.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 部门权限表
 */
@TableName("t_sys_depart_permission")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="sys_depart_permission对象", description="部门权限表")
public class SysDepartPermission {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private String id;
	/**部门id*/
    @ApiModelProperty(value = "部门id")
	private String departId;
	/**权限id*/
    @ApiModelProperty(value = "权限id")
	private String permissionId;
	/**数据规则id*/
	@ApiModelProperty(value = "数据规则id")
	private String dataRuleIds;

	public SysDepartPermission() {

	}

	public SysDepartPermission(String departId, String permissionId) {
		this.departId = departId;
		this.permissionId = permissionId;
	}
}
