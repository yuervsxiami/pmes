package com.cnuip.pmes2.repository.inventory;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.cnuip.pmes2.domain.inventory.PatprstransferInfo;

/**
* Create By Crixalis:
* 2018年3月3日 下午2:34:23
*/
@Repository
public interface PatprstransferInfoMapper {

	@Select("SELECT * FROM st_patprstransferinfo WHERE creatTime >= #{lastUpdateTime} ORDER BY creatTime ASC LIMIT ${start},${limit}")
	List<PatprstransferInfo> findAfterTime(@Param("lastUpdateTime") Date lastUpdateTime, @Param("start") int start, @Param("limit") int limit);
	
	@Select("SELECT count(*) FROM st_patprstransferinfo WHERE creatTime >= #{lastUpdateTime}")
	Integer getTotal(@Param("lastUpdateTime") Date lastUpdateTime);

	@Select("SELECT * FROM st_patprstransferinfo WHERE an = #{an} ORDER BY creatTime DESC")
	List<PatprstransferInfo> findByAn(@Param("an")String an);

}
