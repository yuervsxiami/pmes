package com.cnuip.pmes2.controller.api;
/**
* Create By Crixalis:
* 2017年5月19日 上午10:56:34
*/

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.exception.UploadException;
import com.cnuip.pmes2.util.ResponseEnum;


public class BussinessLogicExceptionHandler {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@ExceptionHandler(BussinessLogicException.class)
	public ApiResponse cExceptionHandler(BussinessLogicException e) {
		ApiResponse response = new ApiResponse<>();
		response.setCode(e.getExceptionEnum().getCode());
//		response.setError(e);
//		e.printStackTrace();
		response.setMessage(e.getExceptionEnum().getMsg());
		response.setDetailMessage(e.getExceptionEnum().getMsg());
		if(e instanceof UploadException) {
			response.setMessage(((UploadException)e).getMessage());
		}
		logger.error(e.getExceptionEnum().getMsg(), e.getCause());
		return response;
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
    public ApiResponse handleException(MaxUploadSizeExceededException e) {
		ApiResponse response = new ApiResponse<>();
		response.setCode(ResponseEnum.UPLOAD_OVERSIZEFILE.getCode());
		response.setMessage(ResponseEnum.UPLOAD_OVERSIZEFILE.getMsg());
		response.setDetailMessage(ResponseEnum.UPLOAD_OVERSIZEFILE.getMsg());
        return response;
    }
	
}
