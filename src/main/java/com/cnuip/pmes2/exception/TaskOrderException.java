package com.cnuip.pmes2.exception;

import com.cnuip.pmes2.util.ResponseEnum;

/**
 * Created by wangzhibin on 2018/1/25.
 */
public class TaskOrderException extends BussinessLogicException {
    public TaskOrderException(ResponseEnum exceptionEnum) {
        super(exceptionEnum);
    }

    public TaskOrderException() {
        super();
    }

    public TaskOrderException(String message) {
        super(message);
    }

    public TaskOrderException(Throwable cause) {
        super(cause);
    }

    public TaskOrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskOrderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public TaskOrderException(Throwable cause, ResponseEnum exceptionEnum) {
        super(cause, exceptionEnum);
    }

}