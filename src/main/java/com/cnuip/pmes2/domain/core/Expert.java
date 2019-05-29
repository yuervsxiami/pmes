package com.cnuip.pmes2.domain.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;

import java.util.Date;
import java.util.List;

/**
 * @auhor Crixalis
 * @date 2018/3/21 14:27
 */
@Data
public class Expert {

    private Long id;
    private String institutions;//所属院校
    private String name;//名称
    private String ipcField;//ipc领域
    private String keyword;//关键词
    private Long userId;//最后一次修改人
    private String idNumber;//身份证号
    private String phone;
    private Integer gender;//性别
    @Field(format = DateFormat.basic_date_time)
    private Date createTime;
    @Field(format = DateFormat.basic_date_time)
    private Date updateTime;
    private String introduce;//简介
    private String specialties;//特长
    private User user;

    private List<IPCField> ipcFieldList;

    private List<NationalEconomy> specialtyList;

    // 操作的开始时间
    @JsonIgnore
    private Date optDateFrom;
    // 操作的结束时间
    @JsonIgnore
    private Date optDateTo;

}
