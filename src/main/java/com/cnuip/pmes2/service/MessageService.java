package com.cnuip.pmes2.service;
/**
* Create By Crixalis:
* 2018年3月9日 上午9:51:44
*/

import com.cnuip.pmes2.domain.core.Message;
import com.github.pagehelper.PageInfo;

public interface MessageService {
	
	PageInfo<Message> selectByUser(Long userId, int pageNum, int pageSize);

	void sendMessage(Message message);
	
}
