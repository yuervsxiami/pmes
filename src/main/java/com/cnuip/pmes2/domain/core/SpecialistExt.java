package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SpecialistExt extends BaseModel {
    private Long id;
    private Long expertId;
    private String direction;
    private String honor;
    private String undertakeSubject;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
