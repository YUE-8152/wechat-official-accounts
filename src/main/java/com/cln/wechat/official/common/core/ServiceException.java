package com.cln.wechat.official.common.core;

/**
 * @ProjectName: wechat-official-accounts
 * @Package: com.cln.wechat.official.common.core
 * @ClassName: ServiceException
 * @Author: YUE
 * @Description:
 * @Date: 2021/8/3 15:41
 * @Version: 1.0
 */
public class ServiceException extends RuntimeException {
    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public ServiceException setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ServiceException setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public ServiceException() {
        this(500,"系统内部异常，请联系管理员");
    }

    public ServiceException(String msg) {
        this(500,msg);
    }

    public ServiceException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

