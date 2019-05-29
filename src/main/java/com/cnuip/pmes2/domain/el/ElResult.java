package com.cnuip.pmes2.domain.el;

import com.cnuip.pmes2.domain.BaseModel;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2019/5/24.
 * Time: 14:20
 */

@Data
@NoArgsConstructor
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Document(indexName = "result", type = "result")
public class ElResult extends BaseModel{

    @Id
    private Integer id;

    @Field(type = FieldType.String,analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String name;

    @Field(index = FieldIndex.not_analyzed,type= FieldType.String)
    private String maturity;

    @Field(type= FieldType.String,analyzer = "whitespace", searchAnalyzer = "whitespace")
    private String mode;

    @Field(index = FieldIndex.not_analyzed,type= FieldType.String)
    private String provinceName;

    @Field(index = FieldIndex.not_analyzed,type= FieldType.String)
    private String college;

    @Field(index = FieldIndex.not_analyzed,type= FieldType.String)
    private String facultyDepartment;

    @Field(index = FieldIndex.not_analyzed,type= FieldType.String)
    private String linkman;

    @Field(index = FieldIndex.not_analyzed,type= FieldType.String)
    private String phone;

    @Field(index = FieldIndex.not_analyzed,type= FieldType.String)
    private String email;

    @Field(index = FieldIndex.not_analyzed,type= FieldType.String)
    private String holder;

    @Field(index = FieldIndex.not_analyzed,type= FieldType.String)
    private String holderPhone;

    @Field(index = FieldIndex.not_analyzed,type= FieldType.String)
    private String source;

    private Long editorId;

    private String editorName;

    @Field(type=FieldType.Date,format = DateFormat.basic_date_time)
    private Date createdTime;

    @Field(type=FieldType.Date,format = DateFormat.basic_date_time)
    private Date updatedTime;

    @Field(type= FieldType.String,analyzer = "whitespace", searchAnalyzer = "whitespace")
    private String ipcs;
    @Field(type= FieldType.String,analyzer = "whitespace", searchAnalyzer = "whitespace")
    private String nics;
    @Field(type= FieldType.String,analyzer = "whitespace", searchAnalyzer = "whitespace")
    private String ntccs;

    @Field(type= FieldType.String,analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String introduction;

    private String innovationPoint;


    private String technicalIndicator;
    private String applicationDomain;
    private String marketOutlook;
    private String teamIntroduction;

    @Field(type= FieldType.String,analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String content;
}
