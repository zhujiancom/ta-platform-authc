package com.ta.platform.authc.module.exception;

import com.ta.platform.common.api.ApiCode;
import com.ta.platform.common.exception.PlatformException;

/**
 * Creator: zhuji
 * Date: 5/19/2020
 * Time: 6:22 PM
 * Description: 验证码校验异常
 */
public class VerificationCodeException extends PlatformException {
    public VerificationCodeException(String message) {
        super(message);
    }

    public VerificationCodeException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public VerificationCodeException(ApiCode apiCode) {
        super(apiCode);
    }
}
