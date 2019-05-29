package com.cnuip.pmes2.domain.tkResultBean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/6/28.
 * Time: 14:55
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimilarityInfo {
    private Integer score;
    private String title;
    private String appNo;
    private String pubNo;
    private String[] assigneesName;
    private String pa;
}
