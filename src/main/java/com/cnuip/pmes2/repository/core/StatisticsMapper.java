package com.cnuip.pmes2.repository.core;

import com.cnuip.pmes2.domain.statistics.IndexOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @auhor Crixalis
 * @date 2018/3/28 14:40
 */
@Repository
public interface StatisticsMapper {

	List<IndexOrder> getPatentIndexOrderNum(@Param("pa")String pa, @Param("pin")String pin, @Param("startTime")Date startTime, @Param("endTime")Date endTime);

	/**
	 * 获取个流程下完成订单数
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<IndexOrder> getProcessOrderNum(@Param("startTime")Date startTime, @Param("endTime")Date endTime);

}
