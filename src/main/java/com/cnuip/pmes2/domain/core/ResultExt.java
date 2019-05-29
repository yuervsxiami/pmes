package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.access.method.P;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2019/5/24.
 * Time: 14:29
 */
@Setter
@Getter
public class ResultExt extends BaseModel {

    private Integer id;
    private Integer resultId;
    private String introduction;
    private String innovationPoint;
    private String technicalIndicator;
    private String applicationDomain;
    private String marketOutlook;
    private String teamIntroduction;
    private String remark;
    private Date createdTime;
    private Date updatedTime;


}
