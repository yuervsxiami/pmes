package com.cnuip.pmes2.controller.api.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SpecialistRequest {
    private String provinceName;
    private String college;
    private String name;
    private String facultyDepartment;
    private String speciality;
    private String phone;
    private Short source;
    private String keywords;
    private List<String> ipcs;
    private List<String> nics;
    private List<String> ntccs;
    private int pageSize = 10;
    private int pageNum = 1;
}
