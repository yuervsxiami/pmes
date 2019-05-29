package com.cnuip.pmes2.exception;

import com.cnuip.pmes2.util.ResponseEnum;

/**
 * Created by wangzhibin on 2018/1/26.
 */
public class ProcessOrderException extends BussinessLogicException {
    public ProcessOrderException(ResponseEnum exceptionEnum) {
        super(exceptionEnum);
    }

    public ProcessOrderException() {
        super();
    }

    public ProcessOrderException(String message) {
        super(message);
    }

    public ProcessOrderException(Throwable cause) {
        super(cause);
    }

    public ProcessOrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProcessOrderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ProcessOrderException(Throwable cause, ResponseEnum exceptionEnum) {
        super(cause, exceptionEnum);
    }

}