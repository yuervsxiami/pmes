package com.cnuip.pmes2.exception;

import com.cnuip.pmes2.util.ResponseEnum;

/**
 * Created by wangzhibin on 2018/1/13.
 */
public class ProcessTaskException extends BussinessLogicException {
    public ProcessTaskException(ResponseEnum exceptionEnum) {
        super(exceptionEnum);
    }

    public ProcessTaskException() {
        super();
    }

    public ProcessTaskException(String message) {
        super(message);
    }

    public ProcessTaskException(Throwable cause) {
        super(cause);
    }

    public ProcessTaskException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProcessTaskException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    public ProcessTaskException(Throwable cause, ResponseEnum exceptionEnum) {
        super(cause, exceptionEnum);
    }

}
