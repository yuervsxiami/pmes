package com.cnuip.pmes2.exception;

import com.cnuip.pmes2.util.ResponseEnum;

/**
* Create By Crixalis:
* 2017年5月22日 下午2:39:07
*/
public class OrganizationException extends BussinessLogicException {

	public OrganizationException(ResponseEnum exceptionEnum) {
		super(exceptionEnum);
	}

	public OrganizationException() {
		super();
	}

	public OrganizationException(String message) {
		super(message);
	}

	public OrganizationException(Throwable cause) {
		super(cause);
	}

	public OrganizationException(String message, Throwable cause) {
		super(message, cause);
	}

	public OrganizationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
