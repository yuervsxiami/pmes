package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SpecialistTitle extends BaseModel {
    private Long id;
    private Long expertId;
    private Long titleId;
    private String code;
    private String name;
    private Short level;
    private String preCode;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
