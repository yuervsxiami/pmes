package com.cnuip.pmes2.repository.core;

import com.cnuip.pmes2.controller.api.request.PatentStartSearchCondition;
import com.cnuip.pmes2.domain.core.*;
import com.cnuip.pmes2.domain.inventory.PatentSub3Info;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
* Create By Crixalis:
* 2018年1月22日 下午2:50:57
*/
@Repository
public interface PatentMapper {

	Date selectLastUpdateTime();
	
	Date selectOupdateTimeByAn(String an);
	
	Patent selectSimpleByPrimaryKey(Long id);
	
	Patent selectByPrimaryKey(Long id);
	
	Patent selectByAn(String an);
	
	int updateHasBatchIndexed(Long id);

	int updateHasNotBatchIndexed(Long id);

	int insert(Patent patent);

	int batchInsert(@Param("patents") List<Patent> patents);
	
	int update(Patent patent);
	
	int updatePatentValue(@Param("an")String an, @Param("value")String value);
	
	int updatePatentInfo(@Param("id")Long id, @Param("value")String value, @Param("keyword")String keyword, @Param("independentItem")String independentItem);
	
	int checkAnIsExist(String an);

	List<Patent> search(@Param("condition")PatentStartSearchCondition condition,
						@Param("pageNum")int pageNum, @Param("pageSize")int pageSize);

	List<Patent> searchWithAns(@Param("ans")List<String> ans,
						@Param("pageNum")int pageNum, @Param("pageSize")int pageSize);

	/**
	 * 专利价值快速评估结果查询
	 * @param condition
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	List<Patent> searchHumanAssessment(@Param("condition")PatentStartSearchCondition condition,
									   @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);

	List<Patent> searchHumanAssessmentWithAns(@Param("ans")List<String> ans,
									   @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);
	/**
	 * 查询专利快速评估详情
	 * @param id
	 * @return
	 */
	@Select("select * from p_patent where id = #{id}")
	@ResultMap("assessmentPatentMap")
	HumanAssessmentPatent findAssessmentPatentById(Long id);

	/**
	 * 查询专利价值评估详情
	 * @param id
	 * @return
	 */
	@Select("select * from p_patent where id = #{id}")
	@ResultMap("valueIndexPatentMap")
	ValueIndexPatent findValueIndexPatentById(Long id);

	/**
	 * 更改专利流程处理状态
	 * @param id
	 * @param state
	 * @return
	 */
	@Update("update p_patent set index_state = #{state} where id = #{id}")
	int changeIndexState(@Param("id") Long id, @Param("state") int state);

	List<ElPatent> findUpdatedElPatents(@Param("lastUpdateTime") Date lastUpdateTime,
										@Param("pageNum")int pageNum, @Param("pageSize")int pageSize);

	/**
	 * 根据id查询专利详情以及最新的标签
	 * @param id
	 * @return
	 */
	@Select("select * from p_patent where id = #{id}")
	@ResultMap("fullLabelMap")
	Patent findPatentByIdWithFullLabels(Long id);

	/**
	 * 根据申请号查询专利详情以及最新的标签
	 * @param an
	 * @return
	 */
	@Select("select * from p_patent where an = #{an}")
	@ResultMap("fullLabelMap")
	Patent findPatentByAnWithFullLabels(@Param("an") String an);
	
	int selectTotalAfterTimeNum(@Param("lastUpdateTime") Date lastUpdateTime, @Param("endNum") int endNum);
	
	int selectTotalAfterTimeByTypeNum(@Param("lastUpdateTime") Date lastUpdateTime, @Param("type") int type);
	
	List<Patent> selectPatentUpdateAfterTime(@Param("lastUpdateTime") Date lastUpdateTime, @Param("endNum") int endNum,
			 @Param("start") int start, @Param("limit") int limit);

	List<Patent> selectIdsUpdateAfterTime(@Param("lastUpdateTime") Date lastUpdateTime,
										  @Param("start") int start, @Param("limit") int limit);

	List<Patent> selectPatentUpdateAfterTimeByType(@Param("lastUpdateTime") Date lastUpdateTime, @Param("type") int type,
			@Param("start") int start, @Param("limit") int limit);

	@Select("select id from p_patent where last_legal_status = '有效'")
	List<Long> selectYouxiaoZhuanli(@Param("pageNum")int pageNum, @Param("pageSize")int pageSize);

	@Select("select * from p_patent where an = #{an}")
	@ResultMap("elPatentMap")
	ElPatent findElPatentByAn(@Param("an") String an);

	@Select("select * from p_patent where id = #{id}")
	@ResultMap("elPatentMap")
	ElPatent findElPatentById(long id);

	/**
	 * 用于批量索引处理
	 * @param start
	 * @param rows
	 * @return
	 */
	@Select("select an from p_patent where has_batch_indexed = 1 and RIGHT(an, 1) = #{suffix} order by id asc limit ${start}, ${rows}")
	List<String> findPatents4BatchIndex(@Param("suffix") String suffix, @Param("start")int start, @Param("rows")int rows);

	/**
	 * 用于批量索引处理
	 * @param start
	 * @param rows
	 * @return
	 */
	@Select("select an from p_patent where has_batch_indexed = 1 order by id asc limit ${start}, ${rows}")
	List<String> findPatents4BatchIndexAll(@Param("start")int start, @Param("rows")int rows);

	/**
	 * 根据时间查询需要更新的专利id
	 * @param start
	 * @param rows
	 * @return
	 */
	@Select("select id from p_patent where update_time >= #{updateTime} order by id asc limit ${start}, ${rows}")
	List<Long> findPatents4BatchIndexAfterDate(@Param("updateTime")Date updateTime, @Param("start")int start, @Param("rows")int rows);

	List<Patent> findUnindexedPatentByEndNum(@Param("endNum") int endNum);

	void addIndexError(@Param("an")String an,@Param("reason")String reason);

	List<IndexEr> selectIndexError(@Param("pageNum")int pageNum, @Param("pageSize")int pageSize);

	@Select("select id from p_patent where type = 4 and last_legal_status != '无效' and id % 10 = #{mode} order by id asc limit ${start}, ${rows}")
	List<Long> findPatent4(@Param("mode") int mode, @Param("start")int start, @Param("rows")int rows);

	List<Patent> searchOrderByUpdateTimeAsc(@Param("condition")PatentStartSearchCondition condition,
											@Param("pageNum")int pageNum, @Param("pageSize")int pageSize);

	List<Patent> searchOrderByCreateTimeAsc(@Param("condition")PatentStartSearchCondition condition,
											@Param("pageNum")int pageNum, @Param("pageSize")int pageSize);

	List<PatentSub3Info> searchKeywordsByEndNum(@Param("endNum")int endNum, @Param("lastSYSID") String lastSYSID);

	List<Patent> findPatentValueList(@Param("anList") List<String> anList);
}
