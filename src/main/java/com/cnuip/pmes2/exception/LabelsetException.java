package com.cnuip.pmes2.exception;

import com.cnuip.pmes2.util.ResponseEnum;

/**
 * Created by wangzhibin on 2018/1/11.
 */
public class LabelsetException extends BussinessLogicException {

    public LabelsetException(ResponseEnum exceptionEnum) {
        super(exceptionEnum);
    }

    public LabelsetException(Throwable cause, ResponseEnum exceptionEnum) {
        super(cause, exceptionEnum);
    }

    public LabelsetException() {
        super();
    }

    public LabelsetException(String message) {
        super(message);
    }

    public LabelsetException(Throwable cause) {
        super(cause);
    }

    public LabelsetException(String message, Throwable cause) {
        super(message, cause);
    }

    public LabelsetException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}
