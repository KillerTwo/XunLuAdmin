package org.wm.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * @创建人 sk
 * @创建时间 2022/3/11
 * @描述 通用返回实体
 */
public class ResponseResult<T> {
    private Integer code;

    private String msg;

    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private ResponseResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ResponseResult<T> success() {
        return ResponseResult.success(null);
    }

    public static <T> ResponseResult<T> success(String msg) {
        return ResponseResult.success(msg, null);
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static <T> ResponseResult<T> success(T data) {
        return ResponseResult.success("操作成功", data);
    }

    public static <T> ResponseResult<T> success(String msg, T data) {
        return new ResponseResult(HttpStatus.OK.value(), msg, data);
    }

    /**
     * 返回错误消息
     * @return 警告消息
     */
    public static <T> ResponseResult<T> error() {
        return ResponseResult.error(null, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static <T> ResponseResult<T> error(String msg) {
        return ResponseResult.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static <T> ResponseResult<T> error(String msg, T data) {
        return new ResponseResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  返回内容
     * @return 警告消息
     */
    public static <T> ResponseResult<T> error(int code, String msg) {
        return new ResponseResult(code, msg, null);
    }
}
