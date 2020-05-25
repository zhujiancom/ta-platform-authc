package com.ta.platform.authc.module.vo;

import com.ta.platform.common.aspect.annotation.Dict;
import com.ta.platform.common.aspect.annotation.DictSupport;
import com.ta.platform.common.system.model.TreeModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * Creator: zhuji
 * Date: 5/6/2020
 * Time: 2:54 PM
 * Description:
 */
@Data
@SuperBuilder
@DictSupport
public class SysPermissionTreeModel extends TreeModel<SysPermissionTreeModel> {

    /**
     * id
     */
    private String id;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 菜单权限编码
     */
    private String perms;
    /**
     * 权限策略1显示2禁用
     */
    private String permsType;

    /**
     * 组件
     */
    private String component;

    /**
     * 跳转网页链接
     */
    private String url;

    /**
     * 一级菜单跳转地址
     */
    private String redirect;

    /**
     * 菜单排序
     */
    private Double sortNo;

    /**
     * 类型（0：一级菜单；1：子菜单 ；2：按钮权限）
     */
    @Dict(dictCode = "menu_type")
    private Integer menuType;

    /**
     * 是否路由菜单: 0:不是  1:是（默认值1）
     */
    private boolean route;


    /**
     * 是否路缓存页面: 0:不是  1:是（默认值1）
     */
    private boolean keepAlive;

    /**
     * 描述
     */
    private String description;

    /**
     * 删除状态 0正常 1已删除
     */
    private Integer delFlag;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * alwaysShow
     */
    private boolean alwaysShow;
    /**
     * 是否隐藏路由菜单: 0否,1是（默认值0）
     */
    private boolean hidden;

    /**
     * 按钮权限状态(0无效1有效)
     */
    private String status;

    /**
     * 外链菜单打开方式 0/内部打开 1/外部打开
     */
    private boolean internalOrExternal;
}
