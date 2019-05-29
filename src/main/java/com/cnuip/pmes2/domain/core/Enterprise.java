package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Enterprise
 *
 * @author: xiongwei
 * Date: 2018/2/5 下午8:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Enterprise extends BaseModel {

    public Enterprise(String name, String unifiedSocialCreditCode, String nationalEconomyField, Integer type,
			Integer provinceId, Integer cityId, Integer districtId, Date optDateFrom, Date optDateTo) {
		super();
		this.name = name;
		this.unifiedSocialCreditCode = unifiedSocialCreditCode;
		this.nationalEconomyField = nationalEconomyField;
		this.type = type;
		this.provinceId = provinceId;
		this.cityId = cityId;
		this.districtId = districtId;
		this.optDateFrom = optDateFrom;
		this.optDateTo = optDateTo;
	}

	private Long id;
    private String name;
    private String unifiedSocialCreditCode; // 统一社会信息用代码
    private String nationalEconomyField; // 国民经济领域
    // 国有企业、集体所有制、私营企业、有限责任公司、股份有限公司、有限合伙企业、联营企业、外商投资企业、个人独资企业
    private NationalEconomy nationalEconomy;
    private Integer type;
    // 大型企业、中型企业、小型企业、微型企业
    private Integer scaleType;
    private String certification; // 企业认证
    private Integer state; // 0-流程未处理，1-流程已处理
    // 地区信息
    private Integer provinceId;
    private Region province;
    private Integer cityId;
    private Region city;
    private Integer districtId;
    private Region district;
    private String address; // 地址
    private String contact; // 联系人
    private String phone; // 联系电话
    private Integer hasIndexing;//是否有流程开启

    private Long userId;
    private User user;

    private Date createTime;
    private Date updateTime;

    // 操作的开始时间
    @JsonIgnore
    private Date optDateFrom;
    // 操作的结束时间
    @JsonIgnore
    private Date optDateTo;
    // 分页参数
    @JsonIgnore
    private Integer pageNum;
    @JsonIgnore
    private Integer pageSize;
    //流程类型
    @JsonIgnore
    private Integer processType;
    //定单号
    @JsonIgnore
    private String actInstanceId;
    //环节类型
    @JsonIgnore
    private Integer taskType;
    //工单号
    @JsonIgnore
    private String actTaskId;
    //工单状态
    @JsonIgnore
    private Integer taskState;

    // 关联的最新的定单
    private ProcessOrder processOrder;

}
