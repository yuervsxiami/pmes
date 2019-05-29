package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangzhibin on 2018/1/11.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Data
public class LabelsetLabel extends BaseModel {
    private Long id;
    private Long labelsetId; //标签体系id
    private Labelset labelset;
    private Long labelId; //标签id
    private Label label;
    private Long parentId; //父级id
    private LabelsetLabel parent;
    private List<LabelsetLabel> children = new ArrayList<>();
    private Integer type; //节点类型
    private String name; //节点名称
    private Double weight; //权重
    private String docUrl; //计算公式文档
    private Date createTime;
    private Date updateTime;
}
