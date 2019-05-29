package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SpecialistPaper extends BaseModel {
    private Long id;
    private Long expertId;
    private String type;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
