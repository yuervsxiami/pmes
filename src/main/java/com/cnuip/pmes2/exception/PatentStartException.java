package com.cnuip.pmes2.exception;

import com.cnuip.pmes2.util.ResponseEnum;

/**
 * Created by wangzhibin on 2018/1/25.
 */
public class PatentStartException extends BussinessLogicException {
    public PatentStartException(ResponseEnum exceptionEnum) {
        super(exceptionEnum);
    }

    public PatentStartException() {
        super();
    }

    public PatentStartException(String message) {
        super(message);
    }

    public PatentStartException(Throwable cause) {
        super(cause);
    }

    public PatentStartException(String message, Throwable cause) {
        super(message, cause);
    }

    public PatentStartException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public PatentStartException(Throwable cause, ResponseEnum exceptionEnum) {
        super(cause, exceptionEnum);
    }
}
