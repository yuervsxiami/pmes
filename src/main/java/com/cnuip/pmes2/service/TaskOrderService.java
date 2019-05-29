package com.cnuip.pmes2.service;

import com.cnuip.pmes2.controller.api.request.PatentTaskOrderSearchCondition;
import com.cnuip.pmes2.controller.api.request.TaskOrderDealParam;
import com.cnuip.pmes2.controller.api.response.HumanIndexAuditResponse;
import com.cnuip.pmes2.controller.api.response.HumanIndexResponse;
import com.cnuip.pmes2.domain.core.*;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.exception.TaskOrderException;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wangzhibin on 2018/1/25.
 */
public interface TaskOrderService extends BaseService {

    TaskOrder selectByPrimaryKey(Long id);

    List<TaskOrder> selectByProcessOrderId(Long processOrderId);

    PageInfo<TaskOrder> patentSearch(PatentTaskOrderSearchCondition condition, int pageNum, int pageSize);

    TaskOrder insert(TaskOrder order) throws TaskOrderException;
    void delete(Long id) throws TaskOrderException;

    void changeUser(Long id, Long userId) throws TaskOrderException;
    void changeState(Long id, Integer state) throws TaskOrderException;

    PageInfo<TaskOrder> getMyPatentTask(User user, PatentTaskOrderSearchCondition condition, int pageNum, int pageSize) throws TaskOrderException;
    
    PageInfo<TaskOrder> getMyEnterpriseTask(User user, Enterprise condition, int pageNum, int pageSize) throws TaskOrderException;
    
    PageInfo<TaskOrder> getMyEnterpriseRequirementTask(User user, EnterpriseRequirement condition, int pageNum, int pageSize) throws TaskOrderException;
    
    PageInfo<TaskOrder> getMyMatchTask(User user, Match condition, int pageNum, int pageSize) throws TaskOrderException;
    
    /**
     * 获取我自己的工单
     */
	PageInfo<TaskOrder> getMyTask(User user, int pageNum, int pageSize);

	/**
	 * 获取在某个环节下绑定的标签
	 * @param taskOrderId
	 * @return
	 */
	List<TaskOrderLabel> getLabelInTask(Long taskOrderId, User user) throws TaskOrderException;

	/**
	 * 获取自动标引和半自动标引的对应值
	 * @param taskOrderId
	 * @return
	 * @throws TaskOrderException 
	 */
	List<TaskOrderLabel> doAutoIndex(Long taskOrderId, User user) throws TaskOrderException;

	@Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
	void autoDoIndex();

	@Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
	void autoDoSemiAudit();

	@Transactional(rollbackFor = {BussinessLogicException.class, Exception.class})
	void autoDoAudit();

	void autoDoAssignOrder();

	/**
	 * 统一化处理流程工单的方法
	 * @param param
	 * @param user
	 * @throws TaskOrderException
	 */
	void dealTaskOrder(TaskOrderDealParam param, User user) throws TaskOrderException;

	/**
	 * 获取人工标引下的标签和退单原因
	 * @param taskOrderId
	 * @return
	 * @throws TaskOrderException 
	 */
	HumanIndexResponse getHumanLabel(Long taskOrderId, User user) throws TaskOrderException;

	/**
	 * 获取人工标引审核下的半自动标引结果和自动标引结果
	 * @param taskOrderId
	 * @param user
	 * @return
	 * @throws TaskOrderException
	 */
	HumanIndexAuditResponse getHumanResult(Long taskOrderId, User user) throws TaskOrderException;

	List<TaskOrderLabel> doValueIndex(Long taskOrderId, User user) throws TaskOrderException;

	PageInfo<User> getCandidateUser(Long taskOrderId, User user, int pageSize, int pageNum) throws TaskOrderException;
	
	PageInfo<User> getRedeployCandidateUser(Long taskOrderId, User user, int pageSize, int pageNum)
			throws TaskOrderException;

	/**
	 * 根据专利id和流程类型查询此类型已完成定单的所有标签和值
	 * @param patentId
	 * @param processType
	 * @return
	 * @throws TaskOrderException
	 */
	List<TaskOrderLabel> findAllLabels(Long patentId, Integer processType);

	List<InstanceLabel> findAllInstanceLabels(Long patentId, Integer processType);

	/**
	 * 根据专利id查询已完成定单的所有标签和值
	 * @param patentId
	 * @return
	 * @throws TaskOrderException
	 */
	List<TaskOrderLabel> findAllLabels(Long patentId);

	/**
	 * 查询专利快速评估详情
	 */
	ValueIndexPatent findValueIndexPatentById(Long id);

	/**
	 * 根据定单id查询专利评估数据
	 * @param id
	 * @return
	 */
	HumanAssessmentPatent findPatentByOrderId(Long id);

	HumanIndexAuditResponse getSemiAutoResult(Long taskOrderId, User user) throws TaskOrderException;

	HumanIndexResponse getSemiLabel(Long taskOrderId, User user) throws TaskOrderException;

	void holdTaskOrder(TaskOrderDealParam param, User user) throws TaskOrderException;
	
	/**
	 * 把预警的taskOrder进行预警并发送站内消息
	 */
	void alertTaskOrder();

	/**
	 * 把超时的taskOrder进行超时标记并发送站内消息
	 */
	void dueTaskOrder();

	PageInfo<TaskOrder> findDueOrders(User user, int pageNum, int pageSize);
	PageInfo<TaskOrder> findAlertOrders(User user, int pageNum, int pageSize);
	PageInfo<TaskOrder> findUnfinishedOrders(User user, int pageNum, int pageSize);
	PageInfo<TaskOrder> findFinishedOrders(User user, int pageNum, int pageSize);
	PageInfo<TaskOrder> findBackOrders(User user, int pageNum, int pageSize);

	void dealRedeployOrder(TaskOrderDealParam param, User user) throws TaskOrderException;

}
