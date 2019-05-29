package com.cnuip.pmes2.controller.api.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Create By Crixalis: 2017年5月24日 上午11:28:19
 */
@Data
@NoArgsConstructor
public class RoleAuthority {

	private Long roleId;
	private List<Long> authIds;

}
