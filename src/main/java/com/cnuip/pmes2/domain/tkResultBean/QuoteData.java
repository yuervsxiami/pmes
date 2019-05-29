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
 * Time: 15:45
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteData {
    private String appNo;
    private String pubNo;
    private String title;
    private List assigneesName;
    private String pa;
}
