package com.cnuip.pmes2.exception;

import com.cnuip.pmes2.util.ResponseEnum;

/**
* Create By Crixalis:
* 2017年12月29日 下午2:41:04
*/
public class UserException extends BussinessLogicException {

	public UserException(ResponseEnum exceptionEnum) {
		super(exceptionEnum);
	}

	public UserException() {
		super();
	}

	public UserException(String message) {
		super(message);
	}

	public UserException(Throwable cause) {
		super(cause);
	}

	public UserException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
