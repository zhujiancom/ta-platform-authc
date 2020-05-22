package com.ta.platform.authc.module.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ta.platform.authc.module.entity.SysUserRole;
import com.ta.platform.authc.module.vo.SysRoleUserMapModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 用户角色表 Mapper 接口
 * </p>
 *
 */
@Component
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

	@Select("select role_code from t_sys_role where id in (select role_id from t_sys_user_role where user_id = (select id from t_sys_user where username=#{username}))")
	List<String> getRoleByUserName(@Param("username") String username);

	@Select("select id from t_sys_role where id in (select role_id from t_sys_user_role where user_id = (select id from t_sys_user where username=#{username}))")
	List<String> getRoleIdByUserName(@Param("username") String username);

	IPage<SysRoleUserMapModel> getUserListByRoleId(Page page, @Param("roleId") String roleId);
}
