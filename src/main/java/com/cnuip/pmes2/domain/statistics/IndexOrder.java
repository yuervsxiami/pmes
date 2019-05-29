package com.cnuip.pmes2.domain.statistics;

import com.cnuip.pmes2.domain.core.Labelset;
import com.cnuip.pmes2.domain.core.Process;
import lombok.Data;

/**
 * @auhor Crixalis
 * @date 2018/3/28 15:03
 *
 * 指定流程下的完成订单数
 *
 */
@Data
public class IndexOrder extends Process{

	private Integer orderNum;

}
