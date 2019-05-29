package com.cnuip.pmes2.domain.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class RequirementProfessor implements Serializable {
    private long id;
    private long requirementId;
    private long originId ;
    private String professorRemoteKey;
    private String professorName;
    private String professorCollegeName;
    private float score;
    private float matchingDegree;
    private String keywords;
    private Date createTime;
    private Date updateTime;
}
