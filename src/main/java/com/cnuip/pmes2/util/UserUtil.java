package com.cnuip.pmes2.util;

import org.springframework.security.core.Authentication;

import com.cnuip.pmes2.domain.core.User;

/**
* Create By Crixalis:
* 2017年6月15日 下午4:26:08
*/
public class UserUtil {

	public static User getUser (Authentication authentication) {
		return ((User) authentication.getPrincipal());
	}
	
}
