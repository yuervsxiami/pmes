package com.cnuip.pmes2.repository.inventory;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.cnuip.pmes2.domain.inventory.PatprspreservationInfo;

/**
* Create By Crixalis:
* 2018年3月3日 下午2:24:14
*/
@Repository
public interface PatprspreservationInfoMapper {

	@Select("SELECT * FROM st_patprspreservationinfo WHERE creatTime >= #{lastUpdateTime} ORDER BY creatTime ASC LIMIT ${start},${limit}")
	List<PatprspreservationInfo> findAfterTime(@Param("lastUpdateTime") Date lastUpdateTime, @Param("start") int start, @Param("limit") int limit);
	
	@Select("SELECT count(*) FROM st_patprspreservationinfo WHERE creatTime >= #{lastUpdateTime}")
	Integer getTotal(@Param("lastUpdateTime") Date lastUpdateTime);

	@Select("SELECT * FROM st_patprspreservationinfo WHERE an = #{an} ORDER BY creatTime DESC")
	List<PatprspreservationInfo> findByAn(@Param("an")String an);

}
