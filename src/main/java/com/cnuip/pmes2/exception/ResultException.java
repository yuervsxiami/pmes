package com.cnuip.pmes2.exception;

import com.cnuip.pmes2.util.ResponseEnum;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2019/5/21.
 * Time: 14:13
 */
public class ResultException extends BussinessLogicException {
    public ResultException(ResponseEnum exceptionEnum) {
        super(exceptionEnum);
    }

    public ResultException() {
        super();
    }

    public ResultException(String message) {
        super(message);
    }

    public ResultException(Throwable cause) {
        super(cause);
    }

    public ResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResultException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    public ResultException(Throwable cause, ResponseEnum exceptionEnum) {
        super(cause, exceptionEnum);
    }
}
