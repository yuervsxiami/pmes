package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2019/5/24.
 * Time: 14:36
 */
@Setter
@Getter
public class ResultKeyWord extends BaseModel{
    private Integer id;
    private Integer resultId;
    private String content;
    private Date createdTime;
    private Date updatedTime;
}
