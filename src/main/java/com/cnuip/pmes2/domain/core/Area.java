package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Area
 *
 * @author: xiongwei
 * Date: 2018/2/5 下午8:36
 */
@Data
public class Area extends BaseModel {

    private Long id;
    private String name;
    private Long parentId;
    private Area parentArea;
    private List<Area> subAreas;

    private Date createTime;
    private Date updateTime;
}
