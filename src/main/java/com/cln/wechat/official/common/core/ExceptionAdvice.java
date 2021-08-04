package com.cln.wechat.official.common.core;

import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.cln.wechat.official.common.core.ResultCode.PARAMETERS_ERROR;

/**
 * @ProjectName: wechat-official-accounts
 * @Package: com.cln.wechat.official.common.core
 * @ClassName: ExceptionAdvice
 * @Author: YUE
 * @Description:
 * @Date: 2021/8/03 17:41
 * @Version: 1.0
 */
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        // 从异常对象中拿到ObjectError对象
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        // 然后提取错误提示信息进行返回
        return Result.fail(PARAMETERS_ERROR.code(), objectError.getDefaultMessage());
    }

    @ExceptionHandler(ServiceException.class)
    public Result ServiceExceptionHandler(ServiceException e) {
        return Result.fail(e.getCode(), e.getMessage());
    }

}
