package com.cnuip.pmes2.exception;

import com.cnuip.pmes2.util.ResponseEnum;

/**
* Create By Crixalis:
* 2017年12月29日 下午2:41:04
*/
public class LabelException extends BussinessLogicException {

	public LabelException(ResponseEnum exceptionEnum) {
		super(exceptionEnum);
	}

	public LabelException() {
		super();
	}

	public LabelException(String message) {
		super(message);
	}

	public LabelException(Throwable cause) {
		super(cause);
	}

	public LabelException(String message, Throwable cause) {
		super(message, cause);
	}

	public LabelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
