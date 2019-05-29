package com.cnuip.pmes2.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnuip.pmes2.domain.core.Authority;
import com.cnuip.pmes2.exception.AuthException;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.repository.core.AuthorityMapper;
import com.cnuip.pmes2.service.AuthorityService;
import com.cnuip.pmes2.util.ResponseEnum;

/**
* Create By Crixalis:
* 2017年12月28日 下午3:25:35
*/
@Service
@Transactional(readOnly=true)
public class AuthorityServiceImpl implements AuthorityService {
	
	@Autowired
	private AuthorityMapper authorityMapper;

	@Override
	public Authority selectByPrimaryKey(Long id) {
		return authorityMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Authority> selectAll(int pageNum, int pageSize) {
		return authorityMapper.selectAll(pageNum, pageSize);
	}

	@Override
	public List<Authority> selectByParentId(Long parentId) {
		return authorityMapper.selectByParentId(parentId);
	}

	@Override
	public List<Authority> selectByRoleId(Long roleId) {
		return authorityMapper.selectByRoleId(roleId);
	}


	@Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
	@Override
	public Authority add(Authority authority) {
		authorityMapper.add(authority);
		return authorityMapper.selectByPrimaryKey(authority.getId());
	}

	@Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
	@Override
	public Authority update(Authority authority) {
		authorityMapper.update(authority);
		return authorityMapper.selectByPrimaryKey(authority.getId());
	}

	@Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
	@Override
	public void delete(Long id) throws AuthException {
		List<Authority> sons = authorityMapper.selectByParentId(id);
		if(sons!=null && sons.size()>0) {
			throw new AuthException(ResponseEnum.AUTH_HASSON);
		}
		authorityMapper.delete(id);
	}

}
