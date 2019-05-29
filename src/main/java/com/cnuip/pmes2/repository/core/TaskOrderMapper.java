package com.cnuip.pmes2.repository.core;

import com.cnuip.pmes2.controller.api.request.PatentTaskOrderSearchCondition;
import com.cnuip.pmes2.domain.core.*;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskOrderMapper {
	
	TaskOrder selectSimpleByPrimaryKey(Long id);
	TaskOrder selectByPrimaryKey(Long id);
	TaskOrder selectByActTaskId(String actTaskId);

	List<TaskOrder> selectNeedSkipByProcessTaskId(@Param("processTaskIds") List<Long> processTaskIds);
	
	TaskOrder selectRecentChargebackOrder(@Param("processOrderId")Long processOrderId, @Param("taskType")Integer taskType);
	
	TaskOrder selectRecentOrderByType(@Param("processOrderId")Long processOrderId, @Param("taskType")Integer taskType);
	
	List<TaskOrder> selectByProcessOrderId(Long processOrderId);

	@Select("SELECT * FROM p_task_order WHERE process_order_id = #{processOrderId}")
	@ResultMap("simpleTaskOrderMap")
	List<TaskOrder> findOrdersByProcessOrderId(Long processOrderId);

	@Select("SELECT * FROM p_task_order WHERE process_order_id = #{processOrderId}")
	@ResultMap("mapForOrderDetail")
	List<TaskOrder> findSimpleOrdersByProcessOrderId(Long processOrderId);

	List<Long> selectAliveIdsByProcessOrderId(Long processOrderId);
	
	int insert(TaskOrder order);
	int delete(Long id);
	int changeUser(@Param("id")Long id, @Param("userId")Long userId);
	int changeUserByActTaskId(@Param("actTaskId")String actTaskId, @Param("userId")Long userId);
	int changeState(@Param("id")Long id, @Param("state")Integer state);
	int changeStateByActTaskId(@Param("actTaskId")String actTaskId, @Param("state")Integer state);
	
	int deal(@Param("id")Long id, @Param("userId")Long userId);
	
	void redeploy(@Param("id")Long id, @Param("userId")Long userId);
	
	int updateProcessOrderIdByActTaskId(@Param("prId")Long prId, @Param("actTaskId")String actTaskId);

	List<TaskOrder> getMyPatentTaskOrder(@Param("condition")PatentTaskOrderSearchCondition condition, @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);
	
	List<TaskOrder> getMyEnterpriseTaskOrder(@Param("condition")Enterprise condition, @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);
	
	List<TaskOrder> getMyEnterpriseRequirementTaskOrder(@Param("condition")EnterpriseRequirement condition, @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);
	
	List<TaskOrder> getMyMatchTaskOrder(@Param("condition")Match condition, @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);
	
	List<TaskOrder> patentSearch(@Param("condition")PatentTaskOrderSearchCondition condition, @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);

	Patent getPatent(Long processOrderId);
	
	Enterprise getEnterprise(Long processOrderId);

	EnterpriseRequirement getEnterpriseRequirement(Long processOrderId);
	
	Match getMatch(Long processOrderId);

	ProcessOrder getProcessOrder(Long processOrderId);
	
	List<TaskOrder> getAlertTaskOrder();
	
	List<TaskOrder> getDueTaskOrder();
	
	void updateHasAlert(List<Long> ids);
	
	void updateHasDue(List<Long> ids);

	List<TaskOrder> findDueOrders(@Param("user")User user, @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);
	List<TaskOrder> findAlertOrders(@Param("user")User user, @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);
	List<TaskOrder> findUnfinishedOrders(@Param("user")User user, @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);
	List<TaskOrder> findFinishedOrders(@Param("user")User user, @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);
	List<TaskOrder> findBackOrders(@Param("user")User user, @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);

	/**
	 * 查询指定定单的工单id
	 * @param ids
	 * @return
	 */
	@Select("SELECT id from p_task_order where process_order_id in (${ids})")
	List<Long> findOrderIdsByProcessIds(@Param("ids") String ids);

	@Delete("delete from p_task_order where id in (${ids})")
	int deleteOrderByIds(@Param("ids") String ids);
}
