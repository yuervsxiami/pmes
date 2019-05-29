package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@NoArgsConstructor
public class TaskOrderLabel extends BaseModel {
	private Long id;
	private Long taskOrderId; // 工单id
	private Long labelId;
	@Field(type = FieldType.Nested)
	private Label label; // 标签
	@Field(analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
	private String strValue;
	@Field(analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
	private String textValue;
	private Long userId;
	private Integer valueSource = 0; // 标签值来源:0-批量计算,1-人工流程
	private User user;
	@Field(format = DateFormat.basic_date_time)
	private Date createTime;
	@Field(format = DateFormat.basic_date_time)
	private Date updateTime;
	private Patent patent;
	private Labelset labelset;
}
