package com.cnuip.pmes2.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnuip.pmes2.controller.api.request.LabelSearchCondition;
import com.cnuip.pmes2.domain.core.Label;
import com.cnuip.pmes2.exception.LabelException;
import com.cnuip.pmes2.repository.core.LabelMapper;
import com.cnuip.pmes2.service.LabelService;
import com.cnuip.pmes2.util.ResponseEnum;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;

/**
* Create By Crixalis:
* 2018年1月11日 下午1:54

*/
@Service
@Transactional(readOnly=true)
public class LabelServiceImpl implements LabelService{
	
	@Autowired
	private LabelMapper labelMapper;

	@Override
	public Label selectByPrimaryKey(Long id) {
		return labelMapper.selectByPrimaryKey(id);
	}

	@Override
	public Label selectByKey(String key) {
		return labelMapper.selectByKey(key);
	}

	@Override
	public List<Label> selectByType(Integer type, int pageNum, int pageSize) {
		return labelMapper.selectByType(type, pageNum, pageSize);
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public Label add(Label label) throws LabelException{
		if(label!=null && Strings.isNullOrEmpty(label.getKey())) {
			throw new LabelException(ResponseEnum.LABEL_NOKEY);
		}
		if(label.getState()==null || (!label.getState().equals(1) && !label.getState().equals(0))){
			label.setState(0);
		}
		checkUniqueKey(label.getKey(), 0);
		labelMapper.add(label);
		return labelMapper.selectByPrimaryKey(label.getId());
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public Label update(Label label) throws LabelException {
		if(label!=null && Strings.isNullOrEmpty(label.getKey())) {
			throw new LabelException(ResponseEnum.LABEL_NOKEY);
		}
		checkUniqueKey(label.getKey(), 1);
		labelMapper.update(label);
		return labelMapper.selectByPrimaryKey(label.getId());
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void changeState(Label label) throws LabelException {
		if(labelMapper.checkHasUsed(label.getId())>0) {
			throw new LabelException(ResponseEnum.LABEL_HASUSERD);
		}
		labelMapper.changeState(label.getId(), label.getState());
	}

	@Override
	public PageInfo<Label> search(LabelSearchCondition condition, int pageNum, int pageSize) {
		Page<Label> page = (Page<Label>) labelMapper.search(condition, pageNum, pageSize);
		return page.toPageInfo();
	}
	
	private void checkUniqueKey(String key,int limit) throws LabelException {
		if(labelMapper.checkKeyExist(key)>limit) {
			throw new LabelException(ResponseEnum.LABEL_UNIQUEKEY);
		}
	}

}
