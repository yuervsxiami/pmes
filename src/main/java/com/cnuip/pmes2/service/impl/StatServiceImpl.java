package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.controller.api.response.TaskRemindNum;
import com.cnuip.pmes2.controller.api.response.TaskUseTime;
import com.cnuip.pmes2.controller.api.response.UserRemindNum;
import com.cnuip.pmes2.repository.core.StatMapper;
import com.cnuip.pmes2.service.StatService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * StatServiceImpl
 *
 * @author: xiongwei
 * Date: 2018/3/11 下午8:50
 */
@Service
public class StatServiceImpl implements StatService {

    @Autowired
    private StatMapper statMapper;

    @Override
    public Map<String, Long> countPatentProcessOrders() {
        return this.statMapper.countPatentProcessOrders();
    }
    
    @Override
    public Map<String, Long> countProcessOrdersByProcessType(Long processType) {
        return this.statMapper.countProcessOrdersByProcessType(processType);
    }

    @Override
    public List<TaskUseTime> countUseTimeByProcess(Long processId) {
    	return this.statMapper.getUseTimeByProcess(processId);
    }
    
    @Override
    public List<TaskRemindNum> countTaskAlertNumByProcess(Long processId){
    	return this.statMapper.getAlertNum(processId);
    }

    @Override
    public List<TaskRemindNum> countTaskDueNumByProcess(Long processId){
    	return this.statMapper.getDueNum(processId);
    }

	@Override
	public List<UserRemindNum> countMaxUserAlert(Long processId) {
		return this.statMapper.getMaxUserAlert(processId);
	}

	@Override
	public List<UserRemindNum> countMaxUserDue(Long processId) {
		return this.statMapper.getMaxUserDue(processId);
	}
    
    @Override
    public Map<String, Long> countDetailForDueProcessOrders() {
        List<Map<String, Object>> stats = this.statMapper.countDetailForDueProcessOrders();
        return this.convertProcessTypes(stats);
    }

    @Override
    public Map<String, Long> countDetailForAlertProcessOrders() {
        List<Map<String, Object>> stats = this.statMapper.countDetailForAlertProcessOrders();
        return this.convertProcessTypes(stats);
    }

    @Override
    public Map<String, Long> countDetailForProcessingOrders() {
        List<Map<String, Object>> stats = this.statMapper.countDetailForProcessingOrders();
        return this.convertProcessTypes(stats);
    }

    @Override
    public Map<String, Long> countDetailForDoneOrders() {
        List<Map<String, Object>> stats = this.statMapper.countDetailForDoneOrders();
        return this.convertProcessTypes(stats);
    }

    private Map<String, Long> convertProcessTypes(List<Map<String, Object>> statArray) {
        Map<String, Long> stats = new HashMap<>();
        for (Map<String, Object> map: statArray) {
            stats.put(map.get("process_type").toString(), Long.parseLong(map.get("count").toString()));
        }
        Map<String, Long> converted = new HashMap<>(stats.size());
        String[] keys = new String[]{"", "batch", "basic", "deep", "value", "price"};
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            if (Strings.isNullOrEmpty(key)) {
                continue;
            }
            Long count = stats.get("" + i);
            converted.put(key, count == null ? 0 : count);
        }
        return converted;
    }
    
}
