package com.ta.platform.authc.module.exception;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.ta.platform.common.api.ApiCode;
import com.ta.platform.common.api.vo.Result;
import com.ta.platform.common.exception.GlobalExceptionHandler;
import com.ta.platform.common.exception.PlatformException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Creator: zhuji
 * Date: 5/20/2020
 * Time: 7:08 PM
 * Description:
 */
@ControllerAdvice
@RestController
@Slf4j
public class AuthExceptionHandler extends GlobalExceptionHandler {
    /**
     * 系统登录异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = SysLoginException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Boolean> sysLoginExceptionHandler(SysLoginException exception) {
        printRequestDetail();
        printApiCodeException(ApiCode.LOGIN_EXCEPTION, exception);
        return Result.error(ApiCode.LOGIN_EXCEPTION);
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler({PlatformException.class})
    @ResponseStatus(HttpStatus.OK)
    public Result<Object> platformException(PlatformException exception){
        printRequestDetail();
        log.error("PlatformException:", exception);
        Integer errorCode;
        if (exception instanceof BusinessException) {
            errorCode = ApiCode.BUSINESS_EXCEPTION.getCode();
        } else if (exception instanceof VerificationCodeException) {
            errorCode = ApiCode.VERIFICATION_CODE_EXCEPTION.getCode();
        } else {
            errorCode = ApiCode.SPRING_BOOT_PLUS_EXCEPTION.getCode();
        }
        return Result.builder().code(errorCode).message(exception.getMessage()).build();
    }

    /**
     * 登录授权异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = AuthenticationException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Boolean> authenticationExceptionHandler(AuthenticationException exception) {
        printRequestDetail();
        printApiCodeException(ApiCode.AUTHENTICATION_EXCEPTION, exception);
        return Result.error(ApiCode.AUTHENTICATION_EXCEPTION);
    }

    /**
     * 未认证异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = UnauthenticatedException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Boolean> unauthenticatedExceptionHandler(UnauthenticatedException exception) {
        printRequestDetail();
        printApiCodeException(ApiCode.UNAUTHENTICATED_EXCEPTION, exception);
        return Result.error(ApiCode.UNAUTHENTICATED_EXCEPTION);
    }

    /**
     * 未授权异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Boolean> unauthorizedExceptionHandler(UnauthorizedException exception) {
        printRequestDetail();
        printApiCodeException(ApiCode.UNAUTHORIZED_EXCEPTION, exception);
        return Result.error(ApiCode.UNAUTHORIZED_EXCEPTION);
    }

    /**
     * JWT Token解析异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = JWTDecodeException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Boolean> jWTDecodeExceptionHandler(JWTDecodeException exception) {
        printRequestDetail();
        printApiCodeException(ApiCode.JWTDECODE_EXCEPTION, exception);
        return Result.error(ApiCode.JWTDECODE_EXCEPTION);
    }
}
