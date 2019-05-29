package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

/**
 * Created by wangzhibin on 2018/3/6.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Data
public class NationalEconomy extends BaseModel {
    private Long id;
    private String name;
    private String code;
    private Long parentId;
}
