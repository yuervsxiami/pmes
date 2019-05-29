package com.cnuip.pmes2.domain.core;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2019/5/21.
 * Time: 13:59
 */
@Setter
@Getter
public class ResultNtcc implements Serializable {
    private Integer id;
    private Integer resultId;
    private Integer ntccId;
    private String code;
    private String name;
    private Integer level;
    private String preCode;
    private Date createdTime;
    private Date updatedTime;
}
