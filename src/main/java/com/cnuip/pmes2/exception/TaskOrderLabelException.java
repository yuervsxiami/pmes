package com.cnuip.pmes2.exception;

import com.cnuip.pmes2.util.ResponseEnum;

/**
 * Created by wangzhibin on 2018/1/26.
 */
public class TaskOrderLabelException extends BussinessLogicException {
    public TaskOrderLabelException(ResponseEnum exceptionEnum) {
        super(exceptionEnum);
    }

    public TaskOrderLabelException() {
        super();
    }

    public TaskOrderLabelException(String message) {
        super(message);
    }

    public TaskOrderLabelException(Throwable cause) {
        super(cause);
    }

    public TaskOrderLabelException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskOrderLabelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public TaskOrderLabelException(Throwable cause, ResponseEnum exceptionEnum) {
        super(cause, exceptionEnum);
    }

}