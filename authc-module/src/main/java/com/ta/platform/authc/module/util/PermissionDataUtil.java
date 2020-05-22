package com.ta.platform.authc.module.util;

import com.alibaba.fastjson.JSONObject;
import com.ey.tax.toolset.core.StrUtil;
import com.ey.tax.toolset.crypto.digest.DigestUtil;
import com.ta.platform.authc.module.entity.SysPermission;
import com.ta.platform.common.constant.CommonConstant;

import java.util.List;

/**
 * Creator: zhuji
 * Date: 4/9/2020
 * Time: 2:29 PM
 * Description: 权限工具类
 */
public class PermissionDataUtil {
    /**
     * 判读是否拥有homepage菜单
     * @param metaList
     * @return
     */
    public static boolean hasHomePage(List<SysPermission> metaList){
        boolean hasHomePageMenu = false;
        for(SysPermission permission : metaList){
            if(permission.isHomepage()){
                hasHomePageMenu = true;
                break;
            }
        }
        return hasHomePageMenu;
    }

    /**
     * 根据菜单配置生成路由json
     * Menu_type : 类型(0：一级菜单 1：子菜单 2：按钮)
     *
     * @param permission
     * @return
     */
    public static JSONObject getPermissionJsonObject(SysPermission permission){
        JSONObject jsonObject = new JSONObject();
        if(permission.getMenuType().equals(CommonConstant.MENU_TYPE_2)){
            return null;
        }else if(permission.getMenuType().equals(CommonConstant.MENU_TYPE_0) || permission.getMenuType().equals(CommonConstant.MENU_TYPE_1)){
            jsonObject.put("id", permission.getId());
            if(permission.isRoute()){
                jsonObject.put("route", "1"); // 表示生成路由
            }else{
                jsonObject.put("route", "0"); // 表示不生成路由
            }

            if(isDomainUrl(permission.getUrl())){
                jsonObject.put("path", DigestUtil.md5Hex(permission.getUrl()));
            }else{
                jsonObject.put("path", permission.getUrl());
            }

            // 路由名称（通过URL生成路由名称， 路由名称工前端开发页面跳转使用）
            if(StrUtil.isNotEmpty(permission.getComponentName())){
                jsonObject.put("name", permission.getComponentName());
            }else{
                jsonObject.put("name", urlToRouteName(permission.getUrl()));
            }

            // 是否隐藏路由， 默认是显示的
            if(permission.isHidden()){
                jsonObject.put("hidden", true);
            }

            // 是否聚合路由
            if(permission.isAlwaysShow()){
                jsonObject.put("alwaysShow", true);
            }
            jsonObject.put("component", permission.getComponent());
            JSONObject meta = new JSONObject();
            // 由用户设置是否缓存页面
            if(permission.isKeepAlive()){
                meta.put("keepAlive", true);
            }else{
                meta.put("keepAlive", false);
            }

            // 外部链接打开方式
            if (permission.isInternalOrExternal()){
                meta.put("internalOrExternal", true);
            }else{
                meta.put("internalOrExternal", false);
            }

            meta.put("title", permission.getName());

            if(StrUtil.isEmpty(permission.getParentId())){
                // 一级菜单跳转地址
                jsonObject.put("redirect", permission.getRedirect());
            }
            // 设置图标
            if(StrUtil.isNotEmpty(permission.getIcon())){
                meta.put("icon", permission.getIcon());
            }
            if(isDomainUrl(permission.getUrl())){
                meta.put("url", permission.getUrl());
            }
            jsonObject.put("meta", meta);
        }

        return jsonObject;
    }

    /**
     * 判断菜单配置的跳转地址是否是外网地址
     * 支持特殊格式： {{ window._CONFIG['domianURL'] }}/druid/ {{ JS代码片段 }}，前台解析会自动执行JS代码片段
     * @param url
     * @return
     */
    public static boolean isDomainUrl(String url){
        if(StrUtil.isNotBlank(url) && (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("{{"))){
            return true;
        }
        return false;
    }

    /**
     * 通过url生成路由名称（去掉url前缀斜杠， 替换内容中的'/'为'-'）
     * 例： URL = /isystem/role  RouteName = isystem-role
     * @param url
     * @return
     */
    public static String urlToRouteName(String url){
        if(StrUtil.isNotEmpty(url)){
            if(url.startsWith("/")){
                url = url.substring(1);
            }
            url = url.replace("/", "-");

            // 特殊标记
            url = url.replace(":", "@");
            return url;
        }
        return "";
    }

    public static SysPermission formatPermission(SysPermission permission){
        if(permission == null){
            return null;
        }

        // 请求URL
        if (StrUtil.isNotEmpty(permission.getUrl())) {
            String url = permission.getUrl();
            if (!url.startsWith("http") && !url.startsWith("/")&&!url.trim().startsWith("{{")) {
                url = "/" + url;
            }
            permission.setUrl(url);
        }

        if(0 == permission.getMenuType() && StrUtil.isEmpty(permission.getComponent())){
            permission.setComponent("layouts/ZRouteView");
        }
        return permission;
    }
}
