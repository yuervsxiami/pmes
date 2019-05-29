package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Title extends BaseModel {
    private Integer id;
    private String code;
    private String name;
    private Integer level;
    private String preCode;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
