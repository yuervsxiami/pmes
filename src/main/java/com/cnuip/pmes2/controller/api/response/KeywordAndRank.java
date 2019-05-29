package com.cnuip.pmes2.controller.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @auhor Crixalis
 * @date 2018/10/22 10:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeywordAndRank {

	private String keyword;
	private float rank;

}
