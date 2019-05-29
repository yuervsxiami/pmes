package com.cnuip.pmes2.repository.core;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.cnuip.pmes2.controller.api.response.TaskRemindNum;
import com.cnuip.pmes2.controller.api.response.TaskUseTime;
import com.cnuip.pmes2.controller.api.response.UserRemindNum;

import java.util.List;
import java.util.Map;

/**
 * StatMapper: 统计相关方法
 *
 * @author: xiongwei
 * Date: 2018/3/11 下午8:22
 */
@Repository
public interface StatMapper {

    /**
     * 统计专利定单总体概况
     * @return
     */
    @Select("select sum(has_due) as dueCount, sum(has_alert) as alertCount, sum(IF(state = 0, 1, 0)) as handlingCount, " +
            "sum(state) as doneCount from p_process_order WHERE process_type in (2,3,4,5)")
    Map<String, Long> countPatentProcessOrders();
    
    /**
     * 统计指定类型定单总体概况
     * @return
     */
    @Select("select sum(has_due) as dueCount, sum(has_alert) as alertCount, sum(IF(state = 0, 1, 0)) as handlingCount, " +
            "sum(state) as doneCount from p_process_order WHERE process_type = #{processType}")
    Map<String, Long> countProcessOrdersByProcessType(Long processType);
    
    /**
     * 统计指定类型定单各环节时间使用情况
     * @return
     */
    @Select("SELECT task_type AS taskType,max(use_time) AS max,min(use_time) AS min,ceil(avg(use_time)) AS avg " +
    		"FROM (SELECT (UNIX_TIMESTAMP(o.update_time) - UNIX_TIMESTAMP(o.create_time)) AS use_time, pt.task_type FROM (SELECT task_type,id FROM p_process_task WHERE process_id = #{processId}) AS pt " + 
    		"LEFT JOIN p_task_order AS o "+
    		"ON o.process_task_id = pt.id AND o.state = 1 ) AS t " +
    		"GROUP by task_type")
    List<TaskUseTime> getUseTimeByProcess(Long processId);
    
    /**
     * 统计指定类型定单各环节预警工单数
     * @return
     */
    @Select("SELECT pt.task_type AS taskType,count(o.id) AS count FROM (SELECT task_type,id FROM p_process_task WHERE process_id = #{processId}) AS pt " + 
    		"LEFT JOIN p_task_order AS o " + 
    		"ON o.process_task_id = pt.id AND o.state = 0 AND o.has_alert = 1 " +
    		"GROUP by task_type")
    List<TaskRemindNum> getAlertNum(Long processId);
    
    /**
     * 统计指定类型定单各环节超时工单数
     * @return
     */
    @Select("SELECT pt.task_type AS taskType,count(o.id) AS count FROM (SELECT task_type,id FROM p_process_task WHERE process_id = #{processId}) AS pt " + 
    		"LEFT JOIN p_task_order AS o " + 
    		"ON o.process_task_id = pt.id AND o.state = 0 AND o.has_due = 1 " +
    		"GROUP by task_type")
    List<TaskRemindNum> getDueNum(Long processId);

    /**
     * 统计指定类型定单预警工单最多的人
     * @return
     */
    @Select("SELECT count(*) AS count,(SELECT name FROM p_user WHERE id = user_id) AS name FROM p_task_order "+
    		"WHERE process_task_id in (SELECT id FROM p_process_task WHERE process_id = #{processId}) "+
    		"AND state = 0 AND has_alert = 1 " +
    		"GROUP BY user_id " + 
    		"ORDER BY count DESC " + 
    		"LIMIT 10")
    List<UserRemindNum> getMaxUserAlert(Long processId);
    
    /**
     * 统计指定类型定单超时工单最多的人
     * @return
     */
    @Select("SELECT count(*) AS count,(SELECT name FROM p_user WHERE id = user_id) AS name FROM p_task_order "+
    		"WHERE process_task_id in (SELECT id FROM p_process_task WHERE process_id = #{processId}) "+
    		"AND state = 0 AND has_due = 1 " +
    		"GROUP BY user_id " + 
    		"ORDER BY count DESC " + 
    		"LIMIT 10")
    List<UserRemindNum> getMaxUserDue(Long processId);
    
    /**
     * 按流程统计超时定单
     * @return
     */
    @Select("SELECT process_type, count(1) as count from p_process_order where state = 0 and process_type in (2,3,4,5) and has_due = 1 " +
            "GROUP BY process_type")
    List<Map<String, Object>> countDetailForDueProcessOrders();

    /**
     * 按流程统计预警定单
     * @return
     */
    @Select("SELECT process_type, count(1) as count from p_process_order where state = 0 and process_type in (2,3,4,5) and has_alert = 1 " +
            "GROUP BY process_type")
    List<Map<String, Object>> countDetailForAlertProcessOrders();

    /**
     * 按流程统计进行中定单
     * @return
     */
    @Select("SELECT process_type, count(1) as count from p_process_order where process_type in (2,3,4,5) and state = 0 " +
            "GROUP BY process_type")
    List<Map<String, Object>> countDetailForProcessingOrders();

    /**
     * 按流程统计已完成定单
     * @return
     */
    @Select("SELECT process_type, count(1) as count from p_process_order where process_type in (2,3,4,5) and state = 1 " +
            "GROUP BY process_type")
    List<Map<String, Object>> countDetailForDoneOrders();
}
