package com.ta.platform.authc.vo;

import com.ta.platform.common.vo.LoginUserVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Creator: zhuji
 * Date: 5/20/2020
 * Time: 4:39 PM
 * Description: 登录用户对象，响应给前端
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ApiModel("登录用户信息TokenVO")
public class LoginUserTokenVo implements Serializable {

    private static final long serialVersionUID = -7024960141094130815L;

    @ApiModelProperty("token")
    private String token;

    private LoginUserVo userInfo;

    private int multipleDepart;
}
