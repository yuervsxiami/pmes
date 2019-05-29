package com.cnuip.pmes2.repository.core;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.cnuip.pmes2.domain.core.Organization;


/**
* Create By Crixalis:
* 2017年12月27日 下午2:50:44
*/
@Repository
public interface OrganizationMapper {

	Organization selectByPrimaryKey(Long id);
	
	List<Organization> selectAll(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize);
	
	List<Organization> selectByParentId(Long parentId);
	
	int add(Organization organization);
	
	int update(Organization organization);
	
	int delete(Long id);
	
}
