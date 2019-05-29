package com.cnuip.pmes2.repository.core;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.cnuip.pmes2.domain.core.Authority;


/**
* Create By Crixalis:
* 2017年12月27日 下午2:50:44
*/
@Repository
public interface AuthorityMapper {

	Authority selectByPrimaryKey(Long id);
	
	List<Authority> selectAll(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize);
	
	List<Authority> selectByParentId(Long parentId);

	List<Authority> selectByRoleId(Long roleId);
	
	int add(Authority authority);
	
	int update(Authority authority);
	
	void delete(Long id);
	
}
