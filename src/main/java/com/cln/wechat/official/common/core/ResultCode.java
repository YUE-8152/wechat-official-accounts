package com.cln.wechat.official.common.core;

/**
 * @ProjectName: wechat-official-accounts
 * @Package: com.cln.wechat.official.common.core
 * @ClassName: ResultCode
 * @Author: YUE
 * @Description:
 * @Date: 2021/8/3 15:39
 * @Version: 1.0
 */
public enum ResultCode {
    SUCCESS(200,"成功"),
    FAIL(400,"失败"),
    UNAUTHORIZED(401,"未认证（签名错误）"),
    TOKEN_ERROR(401,"无效token"),
    TOKEN_EXPIRED(402,"token过期"),
    NOT_FOUND(404,"接口不存在"),//接口不存在
    PARAMETERS_ERROR(4100, "参数校验失败"),
    INTERNAL_SERVER_ERROR(500,"系统异常"),//服务器内部错误
    NOT_SUPPORT_AISLE(10012, "不支持该支付通道");

    private final int code;
    private final String msg;

    ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int code() {
        return code;
    }

    public String msg() {
        return msg;
    }
}
