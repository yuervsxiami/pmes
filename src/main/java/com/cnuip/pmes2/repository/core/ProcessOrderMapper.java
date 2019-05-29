package com.cnuip.pmes2.repository.core;

import com.cnuip.pmes2.controller.api.request.ProcessOrderSearchCondition;
import com.cnuip.pmes2.domain.core.ProcessOrder;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessOrderMapper {

    ProcessOrder selectSimpleByPrimaryKey(Long id);

    ProcessOrder selectDashboardProcessOrderByKey(Long id);

    List<ProcessOrder> selectSimpleByInstanceId(@Param("instanceId") Long instanceId, @Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

    List<ProcessOrder> selectSimpleByInstanceType(@Param("instanceType") Integer instanceType, @Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

    List<ProcessOrder> selectSimpleByProcessType(@Param("processType") Integer processType, @Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

    ProcessOrder getSimpleBaseIndexOrder(Long instanceId);

    ProcessOrder getSimpleValueIndexOrder(Long instanceId);

    ProcessOrder getSimplePriceIndexOrder(Long instanceId);

    ProcessOrder getSimpleDeepIndexOrder(Long instanceId);

    ProcessOrder findSimpleAssessmentOrderByInstanceId(Long instanceId);

    ProcessOrder findAssessmentOrderByInstanceId(Long instanceId);

    ProcessOrder findValueIndexOrderByInstanceId(Long instanceId);

    ProcessOrder getUnfinishedIndexOrder(@Param("instanceId") Long instanceId, @Param("processType") Integer processType);

    ProcessOrder selectByPrimaryKey(Long id);

    List<ProcessOrder> selectByInstanceId(@Param("instanceId") Long instanceId, @Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

    List<ProcessOrder> selectByInstanceType(@Param("instanceType") Integer instanceType, @Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

    List<ProcessOrder> selectByProcessType(@Param("processType") Integer processType, @Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

    int insert(ProcessOrder order);

    int delete(Long id);

    int changeState(@Param("id") Long id, @Param("state") Integer state);

    int changeStateByActId(@Param("String") Long actTaskId, @Param("state") Integer state);

    String getFirstActTaskId(String actProcessId);

    @Select("Select * from p_process_order where id = #{id}")
    @ResultMap("processOrderMap3")
    ProcessOrder findByOrderId(Long id);

    /**
     * 根据专利id查询此专利已经处理的流程类型
     *
     * @param instanceId
     * @return
     */
    @Select("SELECT process_type from p_process_order where instance_id = #{instanceId} and process_type != 1 and instance_type = 1 " +
            "and state = 1 group by process_type order by process_type asc")
    List<Integer> findProcessTypesByInstanceId(Long instanceId);

    List<ProcessOrder> searchPatent(@Param("condition") ProcessOrderSearchCondition condition, @Param("pageNum") int pageNum, @Param("pageSize") int pageSize);
    List<ProcessOrder> searchRequirement(@Param("condition") ProcessOrderSearchCondition condition, @Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

    List<ProcessOrder> getAllProcessOrders(@Param("instanceType") Integer instanceType, @Param("instanceId") Long instanceId, @Param("processId") Long processId);
    
    List<ProcessOrder> getAlertProcessOrder();
    
    List<ProcessOrder> getDueProcessOrder();
	
	void updateHasAlert(List<Long> ids);
	
	void updateHasDue(List<Long> ids);

    @Select("SELECT IFNULL(count(*),0) FROM p_process_order WHERE state = 1 and instance_id =  #{instanceId}")
	int checkBatchQuickFinish(Long instanceId);

    /**
     * 查询指定专利的指定流程的定单id
     * @param instanceId
     * @param processType
     * @return
     */
    @Select("SELECT id from p_process_order where instance_id = #{instanceId} and process_type = #{processType} and instance_type = 1")
    List<Long> findOrderIdsByInstanceIdAndProcessType(@Param("instanceId") Long instanceId, @Param("processType") int processType);

    @Delete("delete from p_process_order where id in (${ids})")
    int deleteOrderByIds(@Param("ids") String ids);

    List<ProcessOrder> selectNeedSnapshot();

    @Select("SELECT instance_id from p_process_order where process_type = 1 AND instance_type = 1 AND state = 1 AND instance_id % 10 = #{mode} order by id asc limit ${start}, ${rows}")
    List<Long> findBatchIndexPatentIdsWithMode(@Param("mode") int mode, @Param("start")int start, @Param("rows")int rows);

    int getAliveProcessOrderNum(Integer type);

    List<ProcessOrder> findAllHumanIndexed();


}
