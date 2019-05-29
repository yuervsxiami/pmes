package com.cnuip.pmes2.controller.api.response;

import com.cnuip.pmes2.domain.core.Patent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by wangzhibin on 2018/1/29.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentResultResponse extends Patent {
    private String patentValue;
    private String legalValue;
    private String economicValue;
    private String technicalValue;
}
