package com.cnuip.pmes2.service;

import com.cnuip.pmes2.domain.core.Patent;
import com.cnuip.pmes2.domain.core.TaskOrderLabel;
import com.cnuip.pmes2.domain.inventory.PatentInfo;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.util.LabelDealUtil;

import java.util.List;

public interface BatchQuickService {

	void startOneBatchProcess(Patent patent) throws BussinessLogicException;

	LabelDealUtil calOnePatent(Patent patent) throws BussinessLogicException;

	List<TaskOrderLabel> getRecentLabelValueByTaskId(List<Long> taskIds);

	Patent dealOnePatentInfo(PatentInfo patentInfo) throws BussinessLogicException;

	/**
	 * 批量流程
	 * @param total 开启多少条,如果为null则跑完所有
	 */
	void batchUpdate(Integer total);
	
	/**
	 * 把专利更新到专利主表
	 */
	void batchUpdatePatent();
	
	/**
	 * 把专利主表中的数据进行批量快速标引
	 * @param endNum 专利an号的结尾,0-9和x,x时传10
	 */
	void batchQuickIndex(int endNum);

	void dealError();

	void batchUpdatePatentWithEndNum(int endNum);

	void batchUpdatePatentWithAnEndNum(int endNum);

	void batchUpdateSub3PatentWithAnEndNum(int endNum);

	void batchUpdatePatentByType(int pType);

	void batchQuickIndexByType(int pType);

	void batchInsert();

	void batchQuickWithEndNum(Integer endNum);

}
