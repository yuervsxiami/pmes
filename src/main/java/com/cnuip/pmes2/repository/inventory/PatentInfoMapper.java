package com.cnuip.pmes2.repository.inventory;

import com.cnuip.pmes2.domain.inventory.PatentInfo;
import com.cnuip.pmes2.domain.inventory.PatentSub3Info;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * PatentInfoMapper
 *
 * @author: xiongwei
 * Date: 2017/12/23 下午12:00
 */
@Repository
public interface PatentInfoMapper {

    @Select("select * from st_patentinfo_sub1 where IDPATENT = #{id}")
    @ResultMap("basicInfoMap")
    PatentInfo findById(Long id);

    @Select("select * from st_patentinfo_sub1 where an like '%${id}%'")
    @ResultMap("patentInfoMap")
    List<PatentInfo> findByAn(Long id);

    @Select("select * from st_patentinfo_sub1")
    @ResultMap("patentInfoMap")
    List<PatentInfo> findByPage(@Param("query") PatentInfo query, @Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

    List<PatentInfo> findAll(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize);
    
    List<PatentInfo> findAfterTime(@Param("lastUpdateTime") Date lastUpdateTime, @Param("pageNum") int pageNum, @Param("pageSize") int pageSize);
    
    List<PatentInfo> findSub1AfterTime(@Param("lastUpdateTime") Date lastUpdateTime, @Param("start") int start, @Param("limit") int limit);
    
    List<PatentSub3Info> findSub3AfterTime(@Param("lastUpdateTime") Date lastUpdateTime, @Param("start") int start, @Param("limit") int limit);
    
    int selectTotalAfterTimeNum(@Param("lastUpdateTime") Date lastUpdateTime);
    
    int selectTotalSub3AfterTimeNum(@Param("lastUpdateTime") Date lastUpdateTime);
    
    PatentSub3Info selectSub3BySysId(@Param("sysId")String sysId);
    
    int selectTotalNum();
    
    String getLabelValue(@Param("table")String table, @Param("column")String column, @Param("an")String an);
    
    int getLabelRowCount(@Param("table")String table, @Param("an")String an);
    
    /**
     * 获取费用处理状态
     */
    String getFeeState(@Param("applyNum")String applyNum, @Param("feeType") String feeType);
    
}
