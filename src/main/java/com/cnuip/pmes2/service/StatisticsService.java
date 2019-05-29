package com.cnuip.pmes2.service;

import com.cnuip.pmes2.domain.statistics.IndexOrder;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @auhor Crixalis
 * @date 2018/3/28 16:44
 */
public interface StatisticsService {

	List<IndexOrder> getPatentIndexOrderNum(String pa, String pin, Date startTime, Date endTime);

	/**
	 * 获取个流程下完成订单数
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<IndexOrder> getProcessOrderNum(Date startTime, Date endTime);

}
