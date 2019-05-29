package com.cnuip.pmes2.service;

import java.util.List;


import com.cnuip.pmes2.domain.core.Authority;
import com.cnuip.pmes2.exception.AuthException;

/**
* Create By Crixalis:
* 2017年12月28日 下午3:15:20
*/
public interface AuthorityService {

	Authority selectByPrimaryKey(Long id);
	
	List<Authority> selectAll(int pageNum, int pageSize);
	
	List<Authority> selectByParentId(Long parentId);

	List<Authority> selectByRoleId(Long roleId);
	
	Authority add(Authority authority);
	
	Authority update(Authority authority);
	
	void delete(Long id) throws AuthException;
	
}
