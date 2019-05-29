package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Specialist extends BaseModel {
    private Long id;
    private String provinceName;
    private String college;
    private String name;
    private String nickName;
    private String phone;
    private String sex;
    private String nation;
    private String nativePlace;
    private String idCardNo;
    private LocalDateTime birthday;
    private String wchat;
    private String imageUrl;
    private String email;
    private String speciality;
    private String facultyDepartment;
    private String education;
    private String keyword;
    private Long editorId;
    private String editorName;
    private Short source;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
