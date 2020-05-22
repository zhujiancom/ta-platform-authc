package com.ta.platform.authc.module.vo;

import com.ta.platform.common.system.model.TreeModel;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * <p>
 * 部门表 存储树结构数据的实体类
 * <p>
 * 
 */
@Data
@SuperBuilder
public class SysDepartTreeModel extends TreeModel<SysDepartTreeModel> {
    private String id;

    /** 对应status字段, 前端数据树中的disableCheckbox属性*/
    private boolean disableCheckbox;

    private String departName;

    private String departNameEn;

    private String departNameAbbr;

    private Integer departOrder;

    private Object description;
    
    private String orgCategory;

    private String orgType;

    private String orgCode;

    private String orgCode2;

    private Integer orgLevel;

    private String phone;

    private String mobile;

    private String fax;

    private String address;

    private String memo;

    private String status;

    private String delFlag;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;
}
