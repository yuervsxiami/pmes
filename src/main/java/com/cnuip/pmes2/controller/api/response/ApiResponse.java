package com.cnuip.pmes2.controller.api.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * ApiResponse
 *
 * @author: xiongwei
 * Date: 2017/5/11 下午12:34
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ApiResponse<T> {

    private int code = 0;
    private String message = "success";
    private T result;
    private Throwable error;
    private String detailMessage;

    public int getCode() {
        return code;
    }

    public void setCode(final int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(final T result) {
        this.result = result;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }
}
