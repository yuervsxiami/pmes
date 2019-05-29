package com.cnuip.pmes2.domain.el;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

/**
 * @auhor Crixalis
 * @date 2018/10/11 13:49
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(indexName = "pmes_professor", type = "professor")
public class ElProfessor implements Scoreable, Highlightable {

	@Id
	private Long id;

	@Field(index = FieldIndex.not_analyzed,type= FieldType.String)
	private String collegeName;

	@Field(index = FieldIndex.not_analyzed,type= FieldType.String)
	private String name;

	@Field(index=FieldIndex.analyzed,analyzer = "ik_max_word", searchAnalyzer = "ik_max_word",type= FieldType.String)
	private String keyWords;

	@Field(type= FieldType.String)
	private String scores;

	private float _score;

	private int isTKHandle;

	@Override
	public void setScore(float score) {
		this._score = score;
	}
}
