package com.cnuip.pmes2.repository.core;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.cnuip.pmes2.domain.core.Role;


/**
* Create By Crixalis:
* 2017年12月27日 下午2:50:44
*/
@Repository
public interface RoleMapper {

	Role selectByPrimaryKey(Long id);
	
	List<Role> selectAll(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize);
	
	List<Role> selectByParentId(Long parentId);
	
	int add(Role role);
	
	int update(Role role);
	
	int delete(Long id);
	
	/**
	 * 给角色添加对应的权限
	 * 
	 * @param role
	 */
	int addRoleAuthorities(Role role);
	
	/**
	 * 删除角色下的所有权限
	 * @param roleId
	 * @return
	 */
	int deleteRoleAuthorities(Long roleId);
}
