package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SpecialistKeyword extends BaseModel {
    private Long id;
    private Long expertId;
    private String content;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
