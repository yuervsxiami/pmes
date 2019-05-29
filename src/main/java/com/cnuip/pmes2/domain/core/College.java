package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class College extends BaseModel {
    private Integer id;
    private String name;
    private String provinceName;
    private String logo;
    private String introduction;
}
