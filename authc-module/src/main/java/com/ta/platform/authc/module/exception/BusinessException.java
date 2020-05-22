package com.ta.platform.authc.module.exception;

import com.ta.platform.common.api.ApiCode;
import com.ta.platform.common.exception.PlatformException;

/**
 * Creator: zhuji
 * Date: 5/19/2020
 * Time: 6:20 PM
 * Description: 业务异常
 */
public class BusinessException extends PlatformException {
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public BusinessException(ApiCode apiCode) {
        super(apiCode);
    }
}
