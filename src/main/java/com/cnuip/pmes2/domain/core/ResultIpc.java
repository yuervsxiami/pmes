package com.cnuip.pmes2.domain.core;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2019/5/21.
 * Time: 13:54
 */
@Setter
@Getter
public class ResultIpc implements Serializable {
    private Integer id;
    private Integer resultId;
    private Integer ipcId;
    private String code;
    private Integer level;
    private String preCode;
    private String description;
    private Date createdTime;
    private Date updatedTime;
}
