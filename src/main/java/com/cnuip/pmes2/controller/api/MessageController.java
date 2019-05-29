package com.cnuip.pmes2.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.domain.core.Message;
import com.cnuip.pmes2.domain.core.User;
import com.cnuip.pmes2.service.MessageService;
import com.cnuip.pmes2.util.UserUtil;
import com.github.pagehelper.PageInfo;

/**
* Create By Crixalis:
* 2018年3月9日 上午10:36:10
*/
@RestController
@RequestMapping("/api/message/")
public class MessageController extends BussinessLogicExceptionHandler {
	
	@Autowired
	private MessageService messageService;
	
    @RequestMapping(value = "/", method = RequestMethod.GET)
	public ApiResponse<PageInfo<Message>> get(
			Authentication authentication,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = "1") int pageNum) {
		ApiResponse<PageInfo<Message>> res = new ApiResponse<>();
		User user = UserUtil.getUser(authentication);
		res.setResult(messageService.selectByUser(user.getId(), pageNum, pageSize));
		return res;
	}
	
}
