package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.core.TaskOrderLabel;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

/**
 * 测试elsearch内置对象
 * @auhor Crixalis
 * @date 2018/4/17 10:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Document(indexName = "pmes_test", type = "elObj")
public class ElObject {

	private Long id;
	private Long processOrderId;
	@Field(type = FieldType.Nested, includeInParent = false)
	private List<TaskOrderLabel> labels;

}
