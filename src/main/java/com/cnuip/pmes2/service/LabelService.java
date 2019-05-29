package com.cnuip.pmes2.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cnuip.pmes2.controller.api.request.LabelSearchCondition;
import com.cnuip.pmes2.domain.core.Label;
import com.cnuip.pmes2.exception.LabelException;
import com.github.pagehelper.PageInfo;

/**
* Create By Crixalis:
* 2018年1月11日 下午1:48:01
*/
public interface LabelService {

	Label selectByPrimaryKey(Long id);
	
	Label selectByKey(String key);
	
	List<Label> selectByType(Integer type, int pageNum, int pageSize);
	
	Label add(Label label) throws LabelException;
	
	Label update(Label label) throws LabelException;
	
	void changeState(Label label) throws LabelException;
	
	PageInfo<Label> search(LabelSearchCondition condition, int pageNum, int pageSize);
	
}
