package com.cnuip.pmes2.exception;

import com.cnuip.pmes2.util.ResponseEnum;

/**
* Create By Crixalis:
* 2017年5月22日 下午2:39:07
*/
public class AuthException extends BussinessLogicException {

	public AuthException(ResponseEnum exceptionEnum) {
		super(exceptionEnum);
	}

	public AuthException() {
		super();
	}

	public AuthException(String message) {
		super(message);
	}

	public AuthException(Throwable cause) {
		super(cause);
	}

	public AuthException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
