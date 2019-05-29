package com.cnuip.pmes2.exception;

import com.cnuip.pmes2.util.ResponseEnum;

/**
* Create By Crixalis:
* 2018年1月16日 下午3:55:31
*/
public class ProcessInstanceException extends BussinessLogicException {

	public ProcessInstanceException(ResponseEnum exceptionEnum) {
		super(exceptionEnum);
	}

	public ProcessInstanceException() {
	}

	public ProcessInstanceException(String message) {
		super(message);
	}

	public ProcessInstanceException(Throwable cause) {
		super(cause);
	}

	public ProcessInstanceException(Throwable cause, ResponseEnum exceptionEnum) {
		super(cause, exceptionEnum);
	}

	public ProcessInstanceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProcessInstanceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
