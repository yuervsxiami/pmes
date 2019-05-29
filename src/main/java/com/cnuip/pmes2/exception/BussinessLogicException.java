package com.cnuip.pmes2.exception;

import com.cnuip.pmes2.util.ResponseEnum;

/**
* Create By Crixalis:
* 2017年5月18日 下午2:52:09
*/
public class BussinessLogicException extends Exception {

	private ResponseEnum exceptionEnum;
	
	public ResponseEnum getExceptionEnum() {
		return exceptionEnum;
	}

	public void setExceptionEnum(ResponseEnum exceptionEnum) {
		this.exceptionEnum = exceptionEnum;
	}

	public BussinessLogicException(ResponseEnum exceptionEnum) {
		super(exceptionEnum.getMsg());
		this.exceptionEnum = exceptionEnum;
	}

	public BussinessLogicException() {
		super();
	}

	public BussinessLogicException(String message) {
		super(message);
	}

	public BussinessLogicException(Throwable cause) {
		super(cause);
	}

	public BussinessLogicException(Throwable cause, ResponseEnum exceptionEnum) {
		super(cause);
		this.exceptionEnum = exceptionEnum;
	}

	public BussinessLogicException(String message, Throwable cause) {
		super(message, cause);
	}

	public BussinessLogicException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
