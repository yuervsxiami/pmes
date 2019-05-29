package com.cnuip.pmes2.controller.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* Create By Crixalis:
* 2018年2月6日 下午2:08:22
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseTaskOrderSearchCondtion extends TaskOrderSearchCondition{
	
    private String name;//名称
    private String nationalEconomyField; // 国民经济领域
    private Integer provinceId;
    private Integer cityId;
    private Integer districtId;

}
