package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.controller.api.request.MetaSearchCondition;
import com.cnuip.pmes2.domain.core.Meta;
import com.cnuip.pmes2.domain.core.MetaValue;
import com.cnuip.pmes2.domain.core.User;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.exception.MetaException;
import com.cnuip.pmes2.repository.core.MetaMapper;
import com.cnuip.pmes2.service.MetaService;
import com.cnuip.pmes2.util.ResponseEnum;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* Create By Crixalis:
* 2018年1月4日 下午2:39:16
*/
@Service
@Transactional(readOnly=true)
public class MetaServiceImpl implements MetaService{


	@Autowired
	private MetaMapper metaMapper;
	
	@Override
	public Meta selectByKey(String key) {
		return metaMapper.selectByKey(key);
	}

	@Override
	public Meta selectByPrimaryKey(Long id) {
		return metaMapper.selectByPrimaryKey(id);
	}

	@Override
	public PageInfo<Meta> search(MetaSearchCondition condition, int pageNum, int pageSize) {
		Page<Meta> page = (Page<Meta>) metaMapper.search(condition, pageNum, pageSize);
		return page.toPageInfo();
	}

	@Override
	public PageInfo<Meta> selectByType(Integer type, int pageNum, int pageSize) {
		Page<Meta> page = (Page<Meta>) metaMapper.selectByType(type, pageNum, pageSize);
		return page.toPageInfo();
	}

	@Override
	public List<Meta> selectAll() {
		List<Meta> list = metaMapper.selectAll();
		return list;
	}

	@Override
	public List<MetaValue> selectMetaValues(String key) {
		return metaMapper.selectMetaValue(key);
	}

	@Override
	public MetaValue selectMetaValue(Long id) {
		return metaMapper.selectValueByPrimaryKey(id);
	}

	@Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
	@Override
	public Meta addMeta(Meta meta, Long userId) throws MetaException {
		if(meta!=null && Strings.isNullOrEmpty(meta.getKey())) {
			throw new MetaException(ResponseEnum.META_NOKEY);
		}
		if(meta.getState()==null || (!meta.getState().equals(1) && !meta.getState().equals(0))){
			meta.setState(0);
		}
		if(meta.getValueType()==null) {
			meta.setValueType(1);//将值类型设置为字符串,暂定为1
		}
		checkUniqueInfo(meta.getKey(), 0);
		meta.setUserId(userId);
		metaMapper.addMeta(meta);
		if(meta.getValues()!=null && meta.getValues().size()>0) {
			for(MetaValue value:meta.getValues()) {
				value.setMetaKey(meta.getKey());
				value.setUserId(userId);
			}
			metaMapper.addMetaValues(meta.getValues());
		}
		return metaMapper.selectByKey(meta.getKey());
	}

	@Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
	@Override
	public MetaValue addMetaValue(MetaValue value) throws MetaException {
		if(value!=null && Strings.isNullOrEmpty(value.getMetaKey())) {
			throw new MetaException(ResponseEnum.META_NOKEY);
		}
		metaMapper.addMetaValue(value);
		return metaMapper.selectValueByPrimaryKey(value.getId());
	}

	@Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
	@Override
	public Meta updateMeta(Meta meta, Long userId) throws MetaException {
		if(meta!=null && Strings.isNullOrEmpty(meta.getKey())) {
			throw new MetaException(ResponseEnum.META_NOKEY);
		}
		checkUniqueInfo(meta.getKey(), 1);
		meta.setUserId(userId);
		metaMapper.updateMeta(meta);
		if(meta.getValues()!=null && meta.getValues().size()>0) {
			for(MetaValue value:meta.getValues()) {
				value.setMetaKey(meta.getKey());
				if(value.getId()==null) {
					value.setUserId(userId);
					metaMapper.addMetaValue(value);
				}
				if(value.getId()!=null) {
					metaMapper.updateMetaValue(value);
				}
			}
		}
		return metaMapper.selectByKey(meta.getKey());
	}

	@Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
	@Override
	public MetaValue updateMetaValue(MetaValue value) {
		metaMapper.updateMetaValue(value);
		return metaMapper.selectValueByPrimaryKey(value.getId());
	}

	@Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
	@Override
	public void deleteMetaValue(Long id) {
		metaMapper.deleteMetaValue(id);
	}

	@Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
	@Override
	public void changeMetaState(String key, Integer state) {
		metaMapper.changeMetaState(key, state);
	}
	
	private void checkUniqueInfo(String key,int limit) throws MetaException {
		if(metaMapper.checkKeyExist(key)>limit) {
			throw new MetaException(ResponseEnum.META_UNIQUEKEY);
		}
	}

}
