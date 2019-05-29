package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SpecialistIpc extends BaseModel {
    private Long id;
    private Long expertId;
    private Long ipcId;
    private String code;
    private String description;
    private Short level;
    private String preCode;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
