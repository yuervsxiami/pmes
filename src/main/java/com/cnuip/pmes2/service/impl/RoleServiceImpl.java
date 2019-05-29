package com.cnuip.pmes2.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnuip.pmes2.controller.api.request.RoleAuthority;
import com.cnuip.pmes2.domain.core.Authority;
import com.cnuip.pmes2.domain.core.Role;
import com.cnuip.pmes2.exception.AuthException;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.repository.core.AuthorityMapper;
import com.cnuip.pmes2.repository.core.RoleMapper;
import com.cnuip.pmes2.service.RoleService;
import com.cnuip.pmes2.util.ResponseEnum;

/**
* Create By Crixalis:
* 2017年12月29日 上午9:46:06
*/
@Service
@Transactional(readOnly=true)
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private RoleMapper roleMapper;
	
	@Autowired
	private AuthorityMapper authorityMapper;
	
	@Autowired
	private IdentityService identityService;

	@Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
	@Override
	public Role add(Role role) {
		roleMapper.add(role);
		addActGroup(role);
		return roleMapper.selectByPrimaryKey(role.getId());
	}

	@Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
	@Override
	public Role update(Role role) {
		roleMapper.update(role);
		return roleMapper.selectByPrimaryKey(role.getId());
	}

	@Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
	@Override
	public void delete(Long id) {
		roleMapper.delete(id);
	}

	@Override
	public List<Role> selectAll(int pageSize, int pageNum) {
		List<Role> roles = roleMapper.selectAll(pageNum, pageSize);
		return roles;
	}

	@Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
	@Override
	public List<Authority> bindRoleAuthorities(RoleAuthority roleAuthority) throws AuthException {
		Role role = roleMapper.selectByPrimaryKey(roleAuthority.getRoleId());
		if (role == null) {
			throw new AuthException(ResponseEnum.ROLE_ISNOTEXIST);
		}
		roleMapper.deleteRoleAuthorities(roleAuthority.getRoleId());
		List<Authority> auths = new ArrayList<>();
		for (Long aId : roleAuthority.getAuthIds()) {
			auths.add(new Authority(aId));
		}
		role.setAuthorities(auths);
		roleMapper.addRoleAuthorities(role);
		return authorityMapper.selectByRoleId(role.getId());
	}

	@Override
	public Role selectByPrimaryKey(Long id) {
		Role role = roleMapper.selectByPrimaryKey(id);
		role.setAuthorities(authorityMapper.selectByRoleId(id));
		return role;
	}
	
	private void addActGroup(Role role) {
		Group group = identityService.newGroup("role"+ role.getId());
		group.setName(role.getName());
		identityService.saveGroup(group);
	}

}
