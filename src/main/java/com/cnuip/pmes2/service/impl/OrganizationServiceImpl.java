package com.cnuip.pmes2.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnuip.pmes2.domain.core.Authority;
import com.cnuip.pmes2.domain.core.Organization;
import com.cnuip.pmes2.exception.AuthException;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.exception.OrganizationException;
import com.cnuip.pmes2.repository.core.OrganizationMapper;
import com.cnuip.pmes2.service.OrganizationService;
import com.cnuip.pmes2.util.ResponseEnum;

/**
* Create By Crixalis:
* 2017年12月28日 下午3:25:35
*/
@Service
@Transactional(readOnly=true)
public class OrganizationServiceImpl implements OrganizationService {
	
	@Autowired
	private OrganizationMapper organizationMapper;

	@Override
	public Organization selectByPrimaryKey(Long id) {
		return organizationMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Organization> selectAll(int pageNum, int pageSize) {
		return organizationMapper.selectAll(pageNum, pageSize);
	}

	@Override
	public List<Organization> selectByParentId(Long parentId) {
		return organizationMapper.selectByParentId(parentId);
	}

	@Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
	@Override
	public Organization add(Organization organization) {
		organizationMapper.add(organization);
		return organizationMapper.selectByPrimaryKey(organization.getId());
	}

	@Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
	@Override
	public Organization update(Organization organization) {
		organizationMapper.update(organization);
		return organizationMapper.selectByPrimaryKey(organization.getId());
	}

	@Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
	@Override
	public void delete(Long id) throws OrganizationException {
		List<Organization> sons = organizationMapper.selectByParentId(id);
		if(sons!=null && sons.size()>0) {
			throw new OrganizationException(ResponseEnum.ORGANIZATION_HASSON);
		}
		organizationMapper.delete(id);
	}

}
