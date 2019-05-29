package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/10/9.
 * Time: 9:56
 */
@Data
@NoArgsConstructor
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Document(indexName = "pmes_test2", type = "professor")
public class ElProfessor extends BaseModel {
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
}
