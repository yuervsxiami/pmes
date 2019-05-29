package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by wangzhibin on 2018/1/11.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Data
public class Labelset extends BaseModel {
    private Long id;
    private String name;
    private Integer type; //标签体系类型
    private Long userId; //操作人id'
    private User user;
    private Integer state;
    private String docUrl; //文档url'
    private Integer version; //版本
    private Date createTime;
    private Date updateTime;
    private List<LabelsetLabel> labelsetLabels;
}