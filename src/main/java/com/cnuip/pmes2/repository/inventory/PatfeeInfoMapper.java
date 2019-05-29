package com.cnuip.pmes2.repository.inventory;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.cnuip.pmes2.domain.inventory.PatfeeInfo;

/**
* Create By Crixalis:
* 2018年3月3日 下午2:21:40
*/
@Repository
public interface PatfeeInfoMapper {

	@Select("SELECT * FROM st_patfeeinfo WHERE creatTime >= #{lastUpdateTime} ORDER BY creatTime ASC LIMIT ${start},${limit}")
	List<PatfeeInfo> findAfterTime(@Param("lastUpdateTime") Date lastUpdateTime, @Param("start") int start, @Param("limit") int limit);
	
	@Select("SELECT count(*) FROM st_patfeeinfo WHERE creatTime >= #{lastUpdateTime}")
	Integer getTotal(@Param("lastUpdateTime") Date lastUpdateTime);

	@Select("SELECT * FROM st_patfeeinfo WHERE APPLYNUM = substring(#{an},3) ORDER BY creatTime DESC")
	List<PatfeeInfo> findByAn(@Param("an")String an);

}
