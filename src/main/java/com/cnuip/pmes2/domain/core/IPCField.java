package com.cnuip.pmes2.domain.core;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @auhor Crixalis
 * @date 2018/3/21 16:48
 */
@Data
public class IPCField {

    private Long id;
    private String code;
    private String name;
    private Integer parentId;
    private Date createTime;
    private Date updateTime;

    private List<IPCField> sonIPCFields;

}
