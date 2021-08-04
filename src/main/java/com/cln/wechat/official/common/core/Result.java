package com.cln.wechat.official.common.core;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pagehelper.PageInfo;

import java.io.Serializable;

/**
 * @ProjectName: wechat-official-accounts
 * @Package: com.cln.wechat.official.common.core
 * @ClassName: Result
 * @Author: YUE
 * @Description:
 * @Date: 2021/8/3 15:39
 * @Version: 1.0
 */
public class Result<T> implements Serializable{
    private int status;
    private String msg;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public Result setStatus(int status) {
        this.status = status;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public Result setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public Result setData(T data) {
        this.data = data;
        return this;
    }

    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";

    public static Result success() {
        return new Result()
                .setStatus(ResultCode.SUCCESS.code())
                .setMsg(DEFAULT_SUCCESS_MESSAGE);
    }

    public static Result success(PageInfo pageInfo) {
        return new Result()
                .setStatus(ResultCode.SUCCESS.code())
                .setMsg(DEFAULT_SUCCESS_MESSAGE)
                .setData(new BaseResult(pageInfo.getList(), pageInfo.getTotal()));
    }

    public static <T> Result<T> success(T data) {
        return new Result()
                .setStatus(ResultCode.SUCCESS.code())
                .setMsg(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);
    }

    public static Result fail(String message) {
        return new Result()
                .setStatus(ResultCode.FAIL.code())
                .setMsg(message);
    }

    public static Result fail(int status, String message) {
        return new Result()
                .setStatus(status)
                .setMsg(message);
    }

    public static Result fail(int status, String message, Object date) {
        return new Result()
                .setStatus(status)
                .setMsg(message).setData(date);
    }

    public static Result systemError() {
        return new Result()
                .setStatus(ResultCode.INTERNAL_SERVER_ERROR.code())
                .setMsg(ResultCode.INTERNAL_SERVER_ERROR.msg());
    }

    private static class BaseResult implements Serializable {
        private Object list;
        private Long total;

        public Object getList() {
            return list;
        }

        public void setList(Object list) {
            this.list = list;
        }

        public Long getTotal() {
            return total;
        }

        public void setTotal(Long total) {
            this.total = total;
        }

        public BaseResult(Object list, Long total) {
            this.list = list;
            this.total = total;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}

