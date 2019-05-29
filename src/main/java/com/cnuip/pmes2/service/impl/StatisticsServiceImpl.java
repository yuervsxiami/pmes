package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.domain.statistics.IndexOrder;
import com.cnuip.pmes2.repository.core.StatisticsMapper;
import com.cnuip.pmes2.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @auhor Crixalis
 * @date 2018/3/28 16:47
 */
@Service
@Transactional(readOnly = true)
public class StatisticsServiceImpl implements StatisticsService{

	@Autowired
	private StatisticsMapper statisticsMapper;

	@Override
	public List<IndexOrder> getPatentIndexOrderNum(String pa, String pin, Date startTime, Date endTime) {
		return statisticsMapper.getPatentIndexOrderNum(pa,pin,startTime,endTime);
	}

	@Override
	public List<IndexOrder> getProcessOrderNum(Date startTime, Date endTime) {
		return statisticsMapper.getProcessOrderNum(startTime,endTime);
	}
}
