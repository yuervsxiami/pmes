package com.cnuip.pmes2.domain.core;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @auhor Crixalis
 * @date 2018/5/25 13:56
 */
@Getter
@Setter
public class TaskSkipInfo {

	private Long id;
	private Long processId;
	private String name;
	private String taskType;
	private Integer state;
	private String userIds;
	private List<Long> userIdList;
	private Integer size;

}
