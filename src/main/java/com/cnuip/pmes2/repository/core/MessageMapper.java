package com.cnuip.pmes2.repository.core;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.cnuip.pmes2.domain.core.Message;


/**
* Create By Crixalis:
* 2017年6月5日 下午4:26:15
*/
@Repository
public interface MessageMapper {
	
	Message selectByPK(Long id);
	
	List<Message> selectByUser(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize, @Param("userId") Long userId);
	
	int add(Message message);
	
	int update(Message message);
    
    int delete(Long id);

	void read(Long id);

	int unreadNum(Long userId);
	
	int readAll(Long userId);
	
}
