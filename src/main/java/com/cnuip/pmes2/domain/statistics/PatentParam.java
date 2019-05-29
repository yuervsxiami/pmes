package com.cnuip.pmes2.domain.statistics;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatentParam {
    private String patType;
    private String lastLegalStatus;
    private String an;
    private String ti;
    private String pa;
    private String pin;
    private String ad;
    private String pd;
    private String provinceName;
    private String collegeName;
    private Double startValue;
    private Double endValue;
}
