package com.ta.platform.authc.module.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ta.platform.authc.module.entity.SysPermission;
import com.ta.platform.common.exception.PlatformException;

import java.util.List;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 */
public interface ISysPermissionService extends IService<SysPermission> {
	
//	List<TreeModel> queryListByParentId(String parentId);
//
//	/**真实删除*/
//	void deletePermission(String id) throws PlatformException;
//	/**逻辑删除*/
//	void deletePermissionLogical(String id) throws PlatformException;

	void addPermission(SysPermission sysPermission) throws PlatformException;

	void editPermission(SysPermission sysPermission) throws PlatformException;

	List<SysPermission> queryByUser(String username);

//	/**
//	 * 根据permissionId删除其关联的SysPermissionDataRule表中的数据
//	 *
//	 * @param id
//	 * @return
//	 */
//	void deletePermRuleByPermId(String id);
//
//	/**
//	  * 查询出带有特殊符号的菜单地址的集合
//	 * @return
//	 */
//	List<String> queryPermissionUrlWithStar();
}
