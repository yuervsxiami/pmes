package com.cnuip.pmes2.repository.core;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cnuip.pmes2.domain.core.Patent;
import com.cnuip.pmes2.domain.inventory.LegalStatusInfo;
import com.cnuip.pmes2.domain.inventory.PatentInfo;
import com.cnuip.pmes2.domain.inventory.PatentSub3Info;
import com.cnuip.pmes2.domain.inventory.PatfeeInfo;
import com.cnuip.pmes2.domain.inventory.PatprsexploitationInfo;
import com.cnuip.pmes2.domain.inventory.PatprspreservationInfo;
import com.cnuip.pmes2.domain.inventory.PatprstransferInfo;
import com.cnuip.pmes2.domain.inventory.ScoreInfo;
import org.springframework.stereotype.Repository;

/**
* Create By Crixalis:
* 2018年3月3日 下午3:25:39
*/
@Repository
public interface OriginalPatentMapper {
	
	Date findSub1LastUpdateTime();
	Date findSub3LastUpdateTime();
	Date findLegalStatusLastUpdateTime();
	Date findFeeLastUpdateTime();
	Date findPrsexploitationLastUpdateTime();
	Date findPrspreservationLastUpdateTime();
	Date findPrstransferLastUpdateTime();
	Date findScoreLastUpdateTime();
	
	void replaceSub1(List<PatentInfo> list);
	void replaceSub3(List<PatentSub3Info> list);
	void replaceLegal(List<LegalStatusInfo> list);
	void replaceFee(List<PatfeeInfo> list);
	void replacePrsexploitation(List<PatprsexploitationInfo> list);
	void replacePrspreservation(List<PatprspreservationInfo> list);
	void replacePrstransfer(List<PatprstransferInfo> list);
	void replaceScoreInfo(List<ScoreInfo> list);

    String getLabelValue(@Param("table")String table, @Param("column")String column, @Param("an")String an);
    
    int getLabelRowCount(@Param("table")String table, @Param("an")String an);
    
    /**
     * 获取费用处理状态
     */
    String getFeeState(@Param("applyNum")String applyNum, @Param("feeType") String feeType);
	
	List<PatentInfo> findAfterTime(@Param("lastUpdateTime") Date lastUpdateTime, @Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

	List<String> findAnAfterTime(@Param("lastUpdateTime") Date lastUpdateTime, @Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

	List<String> findUndeal(@Param("limit")int limit);
	
	List<String> findAnAfterTimeWithEndNum(@Param("lastUpdateTime") Date lastUpdateTime, @Param("endNum") int endNum, @Param("start") int start, @Param("limit") int limit);

	List<String> findSub3AnAfterTimeWithEndNum(@Param("lastUpdateTime") Date lastUpdateTime, @Param("endNum") int endNum, @Param("start") int start, @Param("limit") int limit);

	List<String> findAnAfterTimeByType(@Param("lastUpdateTime") Date lastUpdateTime, @Param("type") int type, @Param("start") int start, @Param("limit") int limit);
	
	List<String> findDealError();
	
	void insertDealError(String an);
	
	void deleteDealError(String an);
	
	int selectTotalAfterTimeNum(@Param("lastUpdateTime") Date lastUpdateTime);
	
	int selectTotalAfterTimeNumWithEndNum(@Param("lastUpdateTime") Date lastUpdateTime, @Param("endNum") int endNum);

	int selectSub3TotalAfterTimeNumWithEndNum(@Param("lastUpdateTime") Date lastUpdateTime, @Param("endNum") int endNum);

	int selectTotalAfterTimeNumByType(@Param("lastUpdateTime") Date lastUpdateTime,  @Param("type") int type);
	
	PatentInfo findRecentInfo(String an);

}
