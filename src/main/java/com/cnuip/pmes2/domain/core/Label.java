package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;

import java.util.Date;

/**
 * Created by wangzhibin on 2018/1/11.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Data
public class Label extends BaseModel {

	protected Long id;
    @Field(index = FieldIndex.not_analyzed)
	protected String key;
    @Field(analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    protected String name; //标签名称
    protected Integer type; //标签类型
    protected Integer valueType; //标签值类型
    protected Long minValue; //最小值
    protected Long maxValue; //最大值
    protected Integer indexType; //标引方式'
    protected Boolean showInProcess; //是否在流程中显示'
    protected Boolean isRequired; //是否必须'
    protected Boolean isMultiple; //是否多选
    protected String defaultValue; //默认值
    protected String metaKey;
    protected Integer state; //标签状态'
    protected Long source; //来源
    protected String remark; //备注
    protected String mapRule;
    protected Integer mapType;
    protected Long userId;
    protected User user;
    @Field(format = DateFormat.basic_date_time)
    protected Date createTime;
    @Field(format = DateFormat.basic_date_time)
    protected Date updateTime;
    private String value;
    
}
