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

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/10/9.
 * Time: 9:56
 */
@Data
@NoArgsConstructor
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Document(indexName = "pmes_test2_tmp", type = "professor")
public class ElProfessorTemp extends BaseModel {

    @Field(index = FieldIndex.not_analyzed,type= FieldType.String)
    private String collegeName;

    @Field(index = FieldIndex.not_analyzed,type= FieldType.String)
    private String name;

    @Field(index=FieldIndex.analyzed,analyzer = "ik_max_word", searchAnalyzer = "ik_max_word",type= FieldType.String)
    private String keyWords;

}
