package com.cnuip.pmes2.service;

import java.util.List;


import com.cnuip.pmes2.domain.core.Organization;
import com.cnuip.pmes2.exception.OrganizationException;

/**
* Create By Crixalis:
* 2017年12月28日 下午3:15:20
*/
public interface OrganizationService {

	Organization selectByPrimaryKey(Long id);
	
	List<Organization> selectAll(int pageNum, int pageSize);
	
	List<Organization> selectByParentId(Long parentId);

	Organization add(Organization organization);
	
	Organization update(Organization organization);
	
	void delete(Long id) throws OrganizationException;
	
}
