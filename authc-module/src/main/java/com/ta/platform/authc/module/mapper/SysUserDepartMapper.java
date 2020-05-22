package com.ta.platform.authc.module.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ta.platform.authc.module.entity.SysUserDepart;
import com.ta.platform.authc.module.vo.SysUserDepVo;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SysUserDepartMapper extends BaseMapper<SysUserDepart> {
	
	List<SysUserDepVo> getUserDepartByUid(@Param("userId") String userId);
}
