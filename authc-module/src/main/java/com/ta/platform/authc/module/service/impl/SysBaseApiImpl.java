package com.ta.platform.authc.module.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ey.tax.toolset.core.StrUtil;
import com.ta.platform.authc.module.entity.SysUser;
import com.ta.platform.authc.module.mapper.SysUserMapper;
import com.ta.platform.common.constant.CacheConstant;
import com.ta.platform.common.system.api.ISysBaseAPI;
import com.ta.platform.common.system.model.ComboModel;
import com.ta.platform.common.system.model.DictModel;
import com.ta.platform.common.system.model.SysCategoryModel;
import com.ta.platform.common.vo.LoginUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Creator: zhuji
 * Date: 5/20/2020
 * Time: 5:14 PM
 * Description:
 */
@Service
public class SysBaseApiImpl implements ISysBaseAPI {
    @Autowired
    private SysUserMapper userMapper;

    @Override
    public void addLog(String LogContent, Integer logType, Integer operatetype) {

    }

    @Cacheable(cacheNames= CacheConstant.SYS_USERS_CACHE, key="#username")
    @Override
    public LoginUserVo getUserByName(String username) {
        if(StrUtil.isEmpty(username)) {
            return null;
        }
        LoginUserVo loginUser = new LoginUserVo();
        SysUser sysUser = userMapper.getUserByName(username);
        if(sysUser==null) {
            return null;
        }




        return loginUser;
    }

    @Override
    public LoginUserVo getUserById(String id) {
        return null;
    }

    @Override
    public List<String> getRolesByUsername(String username) {
        return null;
    }

    @Override
    public List<String> getDepartIdsByUsername(String username) {
        return null;
    }

    @Override
    public List<String> getDepartNamesByUsername(String username) {
        return null;
    }

    @Override
    public String getDatabaseType() throws SQLException {
        return null;
    }

    @Override
    public List<SysCategoryModel> queryAllDSysCategory() {
        return null;
    }

    @Override
    public List<DictModel> queryAllDepartBackDictModel() {
        return null;
    }

    @Override
    public void sendSysAnnouncement(String fromUser, String toUser, String title, String msgContent) {

    }

    @Override
    public void sendSysAnnouncement(String fromUser, String toUser, String title, Map<String, String> map, String templateCode) {

    }

    @Override
    public void sendSysAnnouncement(String fromUser, String toUser, String title, Map<String, String> map, String templateCode, String busType, String busId) {

    }

    @Override
    public String parseTemplateByCode(String templateCode, Map<String, String> map) {
        return null;
    }

    @Override
    public void sendSysAnnouncement(String fromUser, String toUser, String title, String msgContent, String setMsgCategory) {

    }

    @Override
    public void sendSysAnnouncement(String fromUser, String toUser, String title, String msgContent, String setMsgCategory, String busType, String busId) {

    }

    @Override
    public void updateSysAnnounReadFlag(String busType, String busId) {

    }

    @Override
    public List<DictModel> queryFilterTableDictInfo(String table, String text, String code, String filterSql) {
        return null;
    }

    @Override
    public List<String> queryTableDictByKeys(String table, String text, String code, String[] keyArray) {
        return null;
    }

    @Override
    public List<ComboModel> queryAllUser() {
        return null;
    }

    @Override
    public JSONObject queryAllUser(String[] userIds, int pageNo, int pageSize) {
        return null;
    }

    @Override
    public List<ComboModel> queryAllRole() {
        return null;
    }

    @Override
    public List<ComboModel> queryAllRole(String[] roleIds) {
        return null;
    }

    @Override
    public List<String> getRoleIdsByUsername(String username) {
        return null;
    }

    @Override
    public String getDepartIdsByOrgCode(String orgCode) {
        return null;
    }

    @Override
    public List<String> getDeptHeadByDepId(String deptId) {
        return null;
    }
}
