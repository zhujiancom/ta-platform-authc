package com.ta.platform.authc.module.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ta.platform.authc.module.entity.SysPermissionDataRule;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 权限规则 Mapper 接口
 * </p>
 *
 */
@Component
public interface SysPermissionDataRuleMapper extends BaseMapper<SysPermissionDataRule> {
	
	/**
	  * 根据用户名和权限id查询
	 * @param username
	 * @param permissionId
	 * @return
	 */
	List<String> queryDataRuleIds(@Param("username") String username, @Param("permissionId") String permissionId);

}
