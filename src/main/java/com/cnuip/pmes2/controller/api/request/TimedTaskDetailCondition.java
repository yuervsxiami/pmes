package com.cnuip.pmes2.controller.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @auhor Crixalis
 * @date 2018/5/31 11:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimedTaskDetailCondition {

	private Long id;
	private List<String> lastLegalStatus;

}
