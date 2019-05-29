package com.cnuip.pmes2.repository.core;

import com.cnuip.pmes2.controller.api.request.PatentTaskOrderLabelSearchCondition;
import com.cnuip.pmes2.domain.core.LabelValue;
import com.cnuip.pmes2.domain.core.Labelset;
import com.cnuip.pmes2.domain.core.Patent;
import com.cnuip.pmes2.domain.core.TaskOrderLabel;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskOrderLabelMapper {

	TaskOrderLabel selectByPrimaryKey(Long id);

	List<TaskOrderLabel> selectByTaskOrderId(@Param("taskOrderId")Long taskOrderId, @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);

	List<TaskOrderLabel> selectByProcessOrderId(@Param("processOrderId")Long processOrderId);

	@Select("SELECT * FROM p_task_order_label WHERE task_order_id = #{taskOrderId}")
	@ResultMap("baseMap")
	List<TaskOrderLabel> findByTaskOrderId(Long taskOrderId);

	int insert(TaskOrderLabel label);
	int update(TaskOrderLabel label);
	int delete(Long id);
	int deleteByTaskOrderId(Long taskOrderId);
	
	int batchInsert(@Param("taskOrderLabels")List<TaskOrderLabel> taskOrderLabels);

	List<TaskOrderLabel> getRecentLabelByTaskOrderId(@Param("ids") List<Long> ids);
	
	List<TaskOrderLabel> getLabelByProcessOrder(@Param("processOrderId")Long processOrderId, @Param("labelIds")List<Long> labelIds);
	
	String selectValueByKeyAndAn(@Param("key")String key, @Param("an")String an);
	
	String selectValueByTaskOrderIdAndLabelId(@Param("taskOrderId")Long taskOrderId, @Param("labelId")Long labelId);
	
	List<LabelValue> selectLabelValues(@Param("taskOrderId")Long taskOrderId, @Param("labelIds")List<Long> labelIds);

	Labelset getLabelsetByTaskOrderId(Long taskOrderId);
	Patent getPatentByTaskOrderId(Long taskOrderId);
	List<TaskOrderLabel> patentSearch(@Param("condition")PatentTaskOrderLabelSearchCondition condition, @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);

	/**
	 * 根据专利id和流程类型查询此类型已完成定单的所有标签和值
	 * @param patentId
	 * @param processType
	 * @return
	 */
	@Select("SELECT * FROM p_task_order_label WHERE task_order_id in (SELECT id FROM p_task_order WHERE state = 1 and process_order_id IN" +
			"(SELECT max(id) FROM p_process_order where state = 1 and instance_id = #{patentId} and process_type = #{processType}))")
	@ResultMap("baseMap")
	List<TaskOrderLabel> findLatestProcessLabels(@Param("patentId") Long patentId, @Param("processType") Integer processType);

	/**
	 * 根据专利id查询已完成定单的所有标签和值
	 * @param patentId
	 * @return
	 */
	@Select("SELECT * FROM p_task_order_label WHERE task_order_id in (SELECT id FROM p_task_order WHERE state = 1 and process_order_id IN" +
			"(SELECT max(id) FROM p_process_order where state = 1 and instance_type = 1 and instance_id = #{patentId} and process_type != 1 " +
			"GROUP BY process_type ORDER BY process_type ASC))")
	@ResultMap("baseMap")
	List<TaskOrderLabel> findAllPatentLabels(@Param("patentId") Long patentId);

	@Delete("delete from p_task_order_label where task_order_id in (${ids})")
	int deleteLabelByTaskOrderIds(@Param("ids") String ids);

}
