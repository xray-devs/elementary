package com.elementary.core.result;

import lombok.Data;
import com.alibaba.fastjson.JSON;


/**
 * @author Mr丶s
 * @ClassName Result
 * @Version V1.0
 * @Date 2018/12/6 17:00
 * @Description
 */
@Data
public class Result<T> {

    /**
     * 默认成功的code
     */
    private static final String DEFAULT_SUCCESS_CODE = "10000";

    /**
     * 默认失败
     */
    private static final String DEFAULT_FAIL_CODE = "-1";

    /**
     * 状态
     */
    private boolean status;

    /**
     * 接口请求消息
     */
    private String message;

    /**
     * 中文提示信息
     */
    private String cnMessage;

    /**
     * 接口返回编码
     */
    private Object responseCode;

    /**
     * 集体业务编码
     */
    private String subCode;

    /**
     * 业务信息
     */
    private String subMsg;

    /**
     * 结果对象
     */
    private T entry;

    /**
     * 具体异常类
     */
    private Exception e;

    public Result() {

    }

    /**
     * 成功
     *
     * @return
     */
    public static Result<Object> success() {
        Result<Object> result = new Result<>();
        result.setStatus(true);
        result.setResponseCode(DEFAULT_SUCCESS_CODE);
        return result;
    }

    public static Result<Object> success(String msg) {
        Result<Object> result = success();
        result.setMessage(msg);
        result.setEntry(msg);
        return result;
    }

    public static Result<Object> success(String msg, String entry) {
        Result<Object> result = success();
        result.setMessage(msg);
        result.setEntry(entry);
        return result;
    }

    public static Result<Object> success(String msg, Object responseCode) {
        Result<Object> result = success(msg);
        result.setResponseCode(responseCode);
        return result;
    }

    public static Result<Object> success(Object entry) {
        Result<Object> result = success();
        result.setEntry(entry);
        return result;
    }

    public static Result<Object> fail() {
        Result<Object> result = new Result<>();
        result.setStatus(false);
        result.setResponseCode(DEFAULT_FAIL_CODE);
        return result;
    }

    public static Result<Object> fail(String msg) {
        Result<Object> result = fail();
        result.setMessage(msg);
        return result;
    }

    public static Result<Object> fail(String msg, Exception e) {
        Result<Object> result = fail();
        result.setMessage(msg);
        result.setE(e);
        return result;
    }

    public static Result<Object> fail(String msg, Object responseCode) {
        Result<Object> result = fail(msg);
        result.setResponseCode(responseCode);
        return result;
    }

    public static Result<Object> fail(String msg, Object responseCode, Exception e) {
        Result<Object> result = fail(msg,e);
        result.setResponseCode(responseCode);
        return result;
    }

    public static Result<Object> fail(String cnMessage, String msg, Object responseCode) {
        Result<Object> result = fail(msg);
        result.setCnMessage(cnMessage);
        result.setResponseCode(responseCode);
        return result;
    }

    public static Result<Object> fail(Object entry) {
        Result<Object> result = fail();
        result.setEntry(entry);
        return result;
    }

    public Object getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Object responseCode) {
        this.responseCode = responseCode;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getEntry() {
        return entry;
    }

    public void setEntry(T entry) {
        this.entry = entry;
    }

    public String getCnMessage() {
        return cnMessage;
    }

    public void setCnMessage(String cnMessage) {
        this.cnMessage = cnMessage;
    }

    public Exception getE() {
        return e;
    }

    public void setE(Exception e) {
        this.e = e;
    }

    public String toJson() {
        return JSON.toJSONString(this);
    }

}
