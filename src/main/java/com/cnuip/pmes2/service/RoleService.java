package com.cnuip.pmes2.service;

import java.util.List;

import com.cnuip.pmes2.controller.api.request.RoleAuthority;
import com.cnuip.pmes2.domain.core.Authority;
import com.cnuip.pmes2.domain.core.Role;
import com.cnuip.pmes2.exception.AuthException;

/**
* Create By Crixalis:
* 2017年12月28日 下午4:52:09
*/
public interface RoleService {

	Role add(Role role);
	
	Role update(Role role);
	
	Role selectByPrimaryKey(Long id);
	
	void delete(Long id);
	
	List<Role> selectAll(int pageSize,int pageNum);
	
	List<Authority> bindRoleAuthorities(RoleAuthority roleAuthority) throws AuthException;
}
