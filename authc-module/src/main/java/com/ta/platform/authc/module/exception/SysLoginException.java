package com.ta.platform.authc.module.exception;

import com.ta.platform.common.api.ApiCode;
import com.ta.platform.common.exception.PlatformException;

/**
 * Creator: zhuji
 * Date: 5/19/2020
 * Time: 6:21 PM
 * Description: 用户登录异常
 */
public class SysLoginException extends PlatformException {
    public SysLoginException(String message) {
        super(message);
    }

    public SysLoginException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public SysLoginException(ApiCode apiCode) {
        super(apiCode);
    }
}
