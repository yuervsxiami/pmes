package com.cnuip.pmes2.repository.core;

import com.cnuip.pmes2.domain.core.InstanceLabel;
import com.cnuip.pmes2.domain.core.TaskOrderLabel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @auhor Crixalis
 * @date 2018/4/13 14:48
 */
@Repository
public interface InstanceLabelMapper {

	/**
	 * 流程结束先将流程下标签插入
	 * @param labels
	 */
	void replaceLabels(@Param("labels") List<InstanceLabel> labels);

	/**
	 * 流程下标签插入后,再把需要更新的标签更新
	 * @param label
	 */
	void updateLabels(InstanceLabel label);

	InstanceLabel selectByPrimaryKey(Long id);

	List<InstanceLabel> findByInstanceAndLabelIds(@Param("labelIds") List<Long> labelIds, @Param("instanceId") Long instanceId, @Param("instanceType")Integer instanceType);

	List<InstanceLabel> findByInstanceId(@Param("instanceId") Long instanceId);

	List<TaskOrderLabel> findAllPatentLabels(@Param("instanceId") Long instanceId);

	List<InstanceLabel> findLatestProcessLabels(@Param("patentId") Long patentId, @Param("processType") Integer processType);

}
