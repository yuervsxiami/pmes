package com.cnuip.pmes2.controller.api.request;

import lombok.Data;

/**
* Create By Crixalis:
* 2018年1月9日 下午5:34:51
*/
@Data
public class ModifyPasswordParam {

	private Long userId;
	private String oldPassword;
	private String newPassword;
	
}
