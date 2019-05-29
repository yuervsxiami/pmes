package com.cnuip.pmes2.exception;

import com.cnuip.pmes2.util.ResponseEnum;

/**
* Create By Crixalis:
* 2017年12月29日 下午2:41:04
*/
public class MetaException extends BussinessLogicException {

	public MetaException(ResponseEnum exceptionEnum) {
		super(exceptionEnum);
	}

	public MetaException() {
		super();
	}

	public MetaException(String message) {
		super(message);
	}

	public MetaException(Throwable cause) {
		super(cause);
	}

	public MetaException(String message, Throwable cause) {
		super(message, cause);
	}

	public MetaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
