package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;

import java.util.Date;
import java.util.List;

/**
 * LabelChangeHistory
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LabelChangeHistory extends BaseModel {

    private Long id;
    private Long patentId;
    private Long labelId;
    private String beforeStrValue;
    private String afterStrValue;
    private String beforeTextValue;
    private String afterTextValue;
    private Long userId;
    private Date createTime;
    private Date updateTime;
    private User user;

    public LabelChangeHistory(Long patentId, Long labelId, String beforeStrValue, String afterStrValue, String beforeTextValue, String afterTextValue, Long userId) {
        this.patentId = patentId;
        this.labelId = labelId;
        this.beforeStrValue = beforeStrValue;
        this.afterStrValue = afterStrValue;
        this.beforeTextValue = beforeTextValue;
        this.afterTextValue = afterTextValue;
        this.userId = userId;
    }
}
