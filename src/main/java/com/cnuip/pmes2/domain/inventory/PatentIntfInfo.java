package com.cnuip.pmes2.domain.inventory;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "e_patentIntfInfo")
@Data
public class PatentIntfInfo {
    private String an;
    private Integer quoteTotal;
    private Integer byQuoteTotal;
    private Integer total;
    private Double avgScore;
    private Date createTime;

}