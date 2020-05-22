package com.ta.platform.authc.module.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ta.platform.authc.module.entity.SysRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 */
@Component
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * @Description: 删除角色与用户关系
     */
    @Delete("delete from sys_user_role where role_id = #{roleId}")
    void deleteRoleUserRelation(@Param("roleId") String roleId);


    /**
     * @Description: 删除角色与权限关系
     */
    @Delete("delete from sys_role_permission where role_id = #{roleId}")
    void deleteRolePermissionRelation(@Param("roleId") String roleId);

}
