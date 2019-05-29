package com.cnuip.pmes2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnuip.pmes2.domain.core.Message;
import com.cnuip.pmes2.repository.core.MessageMapper;
import com.cnuip.pmes2.service.MessageService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

/**
* Create By Crixalis:
* 2018年3月9日 上午10:14:07
*/
@Service
@Transactional(readOnly=true)
public class MessageServiceImpl implements MessageService{

	@Autowired
	private MessageMapper messageMapper;
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public PageInfo<Message> selectByUser(Long userId, int pageNum, int pageSize) {
		Page<Message> page = (Page<Message>) messageMapper.selectByUser(pageNum, pageSize, userId);
		messageMapper.readAll(userId);
		return page.toPageInfo();
	}

	@Override
	public void sendMessage(Message message) {
		messageMapper.add(message);
	} 
	
}
