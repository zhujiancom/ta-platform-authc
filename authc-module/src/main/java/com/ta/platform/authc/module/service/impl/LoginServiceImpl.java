package com.ta.platform.authc.module.service.impl;

import com.ey.tax.toolset.core.RandomUtil;
import com.ey.tax.toolset.core.StrUtil;
import com.ey.tax.toolset.core.collection.CollectionUtil;
import com.ey.tax.toolset.crypto.digest.DigestUtil;
import com.ta.platform.authc.config.properties.JwtProperties;
import com.ta.platform.authc.config.properties.ShiroProperties;
import com.ta.platform.authc.module.convert.SysUserConvert;
import com.ta.platform.authc.module.entity.SysUser;
import com.ta.platform.authc.module.exception.SysLoginException;
import com.ta.platform.authc.module.exception.VerificationCodeException;
import com.ta.platform.authc.module.service.ILoginRedisService;
import com.ta.platform.authc.module.service.ILoginService;
import com.ta.platform.authc.module.service.ISysUserService;
import com.ta.platform.authc.module.shiro.jwt.JwtToken;
import com.ta.platform.authc.module.vo.SysUserDepVo;
import com.ta.platform.authc.vo.LoginUserTokenVo;
import com.ta.platform.common.constant.CommonConstant;
import com.ta.platform.common.service.ISysBaseService;
import com.ta.platform.common.system.model.SysLoginModel;
import com.ta.platform.common.tool.EncryptUtil;
import com.ta.platform.common.tool.JwtUtil;
import com.ta.platform.common.tool.RandImageUtil;
import com.ta.platform.common.tool.RedisUtil;
import com.ta.platform.common.vo.LoginUserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Set;

/**
 * Creator: zhuji
 * Date: 5/20/2020
 * Time: 5:39 PM
 * Description:
 */
@Slf4j
@Service
public class LoginServiceImpl implements ILoginService {
    private static final String BASE_CHECK_CODES = "qwertyuiplkjhgfdsazxcvbnmQWERTYUPLKJHGFDSAZXCVBNM1234567890";

    @Autowired
    private ISysBaseService sysBaseService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private ShiroProperties shiroProperties;

    @Autowired
    private ILoginRedisService loginRedisService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public LoginUserTokenVo login(SysLoginModel loginParam){
        // 校验验证码
        checkVerifyCode(loginParam.getCaptcha(), loginParam.getCheckKey());
        String username = loginParam.getUsername();
        String password = loginParam.getPassword();
        SysUser sysUser = sysUserService.getUserByName(username);
        sysUserService.checkUserIsEffective(sysUser);
        String userPassword = EncryptUtil.encrypt(username, password, sysUser.getSalt());
        String sysPassword = sysUser.getPassword();
        if (!sysPassword.equals(userPassword)) {
            throw new SysLoginException("用户名或密码错误");
        }
        // 将系统用户对象转换成登录用户对象
        LoginUserVo loginSysUserVo = SysUserConvert.INSTANCE.sysUserToLoginSysUserVo(sysUser);

        // 获取部门
        List<SysUserDepVo> departs = sysUser.getDepartments();
        int multipleDepartFlag;
        if(CollectionUtil.isEmpty(departs)){
            multipleDepartFlag = 0;
        }else if(departs.size() == 1){
            // 更新用户当前所属部门
            sysUserService.updateUserDepart(username, departs.get(0).getOrgCode());
            multipleDepartFlag = 1;
        }else{
            // 由前台控制用户当前所属部门
            multipleDepartFlag = 2;
        }
        //获取用户的角色
        // 设置用户拥有的角色集合，比如“admin,test”
        Set<String> roleSet = sysUserService.getUserRolesSet(username);
        loginSysUserVo.setRoleCodes(roleSet);
        // 获取登录用户的权限
        // 设置用户拥有的权限集合，比如“sys:role:add,sys:user:add”
        Set<String> permissionSet = sysUserService.getUserPermissionsSet(username);
        loginSysUserVo.setPermissionCodes(permissionSet);

        // 生成token字符串并返回
        Long expireSecond = jwtProperties.getExpireSecond();
        String token = JwtUtil.sign(username, sysPassword, Duration.ofSeconds(expireSecond));
        log.debug(StrUtil.format("========= 用户登录， 生成token-{}", token));

        // 创建AuthenticationToken
        JwtToken jwtToken = JwtToken.build(token, username, sysPassword, expireSecond);

        boolean enableShiro = shiroProperties.isEnable();
        if(enableShiro){
            Subject subject = SecurityUtils.getSubject();
            // 执行登录
            subject.login(jwtToken);
        }else {
            log.warn("未启用Shiro");
        }

        // 缓存登录信息到redis
        loginRedisService.cacheLoginInfo(jwtToken, loginSysUserVo);
        sysBaseService.addLog("用户名: " + username + ",登录成功！", CommonConstant.LOG_TYPE_1, null);

        // 返回token和登录用户信息对象
        LoginUserTokenVo loginSysUserTokenVo = LoginUserTokenVo.builder()
                .token(token)
                .userInfo(loginSysUserVo)
                .multipleDepart(multipleDepartFlag).build();
        return loginSysUserTokenVo;
    }

    @Override
    public void logout(HttpServletRequest request) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        //注销
        subject.logout();
        // 获取token
        String token = JwtUtil.getToken(request);
        String username = JwtUtil.getUsername(token);
        // 删除Redis缓存信息
        loginRedisService.deleteLoginInfo(token, username);
        log.info("登出成功,username:{},token:{}", username, token);
    }

    @Override
    public void checkVerifyCode(String verifyToken, String key) {
        if (StringUtils.isBlank(verifyToken)) {
            throw new VerificationCodeException("请输入验证码");
        }
        String lowercaseVerifyToken = verifyToken.toLowerCase();
        String realKey = DigestUtil.md5Hex(lowercaseVerifyToken+key, "utf-8");
        String checkVerifyToken = (String) redisUtil.get(realKey);
        if (StringUtils.isBlank(checkVerifyToken)) {
            throw new VerificationCodeException("验证码已过期或不正确");
        }
        if(!checkVerifyToken.equals(lowercaseVerifyToken)) {
            throw new VerificationCodeException("验证码错误");
        }
        // 验证码校验成功，删除Redis缓存
        redisUtil.del(realKey);
    }

    @Override
    public String generateVerifyCode(String key) {
        try {
            String code = RandomUtil.randomString(BASE_CHECK_CODES,4);
            String lowerCaseCode = code.toLowerCase();
            String realKey = DigestUtil.md5Hex(lowerCaseCode+key, "utf-8");
            redisUtil.set(realKey, lowerCaseCode, 60);
            String base64 = RandImageUtil.generate(code);
            return base64;
        } catch (IOException e) {
            log.error("获取验证码出错", e.getMessage());
            throw new VerificationCodeException("获取验证吗出错");
        }
    }
}
