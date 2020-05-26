package com.ta.platform.authc.module.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ey.tax.toolset.core.ArrayUtil;
import com.ey.tax.toolset.core.StrUtil;
import com.ey.tax.toolset.core.collection.CollectionUtil;
import com.google.common.base.Joiner;
import com.ta.platform.authc.module.entity.SysRolePermission;
import com.ta.platform.authc.module.mapper.SysRolePermissionMapper;
import com.ta.platform.authc.module.service.ISysRolePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Creator: zhuji
 * Date: 5/25/2020
 * Time: 4:57 PM
 * Description:
 */
@Slf4j
@Service
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> implements ISysRolePermissionService {
    @Override
    public void saveRolePermission(String roleId, String permissionIds, String lastPermissionIds) {
        List<String> currentPermissions = StrUtil.split(permissionIds,',');
        List<String> lastPermissions = StrUtil.split(lastPermissionIds, ',');

        // add
        List<String> addPermissions = getDiff(lastPermissions, currentPermissions);
        if(CollectionUtil.isNotEmpty(addPermissions)){
            List<SysRolePermission> list = new ArrayList<>();
            for(String p : addPermissions){
                if(StrUtil.isNotBlank(p)){
                    SysRolePermission rolePermission = new SysRolePermission(roleId, p);
                    list.add(rolePermission);
                }
            }
            this.saveBatch(list);
        }

        // remove
        List<String> deletePermissions = getDiff(currentPermissions, lastPermissions);
        if(CollectionUtil.isNotEmpty(deletePermissions)){
            deletePermissions.stream().forEach(p -> this.remove(new QueryWrapper<SysRolePermission>().lambda().eq(SysRolePermission::getRoleId, roleId).eq(SysRolePermission::getPermissionId, p)));
        }
    }

    /**
     * 从second中找出first中没有的元素
     * @param first
     * @param second
     * @return
     */
    private List<String> getDiff(List<String> first, List<String> second){
        if(CollectionUtil.isEmpty(second)){
            return null;
        }
        if(CollectionUtil.isEmpty(first)){
            return second;
        }

        return second.stream().filter(s -> !first.contains(s)).collect(Collectors.toList());
    }
}
