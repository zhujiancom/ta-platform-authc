package com.ta.platform.authc.module.convert;

import com.ta.platform.authc.module.entity.SysUser;
import com.ta.platform.common.vo.LoginUserVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Creator: zhuji
 * Date: 5/20/2020
 * Time: 5:18 PM
 * Description: 系统用户对象属性转换器
 */
@Mapper
public interface SysUserConvert {
    SysUserConvert INSTANCE = Mappers.getMapper(SysUserConvert.class);

    LoginUserVo sysUserToLoginSysUserVo(SysUser sysUser);
}
