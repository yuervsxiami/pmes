package com.cnuip.pmes2.domain.inventory;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/6/20.
 * Time: 15:49
 */
@Document(collection = "mr_patentAn_detail")
@Data
public class PatentInfoAn {
    @Id
    private String an;
    private ValuePatent value;
    private Map<String,Object> valueMap;
    private int type;
    private List<String> sectionNameList;
    private List<String> lastLegalStatusList;
}
