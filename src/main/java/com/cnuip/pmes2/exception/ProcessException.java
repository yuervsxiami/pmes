package com.cnuip.pmes2.exception;

import com.cnuip.pmes2.util.ResponseEnum;

/**
 * Created by wangzhibin on 2018/1/12.
 */
public class ProcessException extends BussinessLogicException {

    public ProcessException(ResponseEnum exceptionEnum) {
        super(exceptionEnum);
    }

    public ProcessException() {
        super();
    }

    public ProcessException(String message) {
        super(message);
    }

    public ProcessException(Throwable cause) {
        super(cause);
    }

    public ProcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProcessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ProcessException(Throwable cause, ResponseEnum exceptionEnum) {
        super(cause, exceptionEnum);
    }

}
