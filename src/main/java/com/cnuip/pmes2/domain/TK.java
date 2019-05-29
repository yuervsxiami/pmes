package com.cnuip.pmes2.domain;

import com.cnuip.pmes2.domain.core.IPCField;
import com.cnuip.pmes2.domain.core.NationalEconomy;
import com.cnuip.pmes2.domain.core.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @auhor Crixalis
 * @date 2018/3/21 14:27
 */
@Data
public class TK {

    private Integer id;
    private String an;
    private Integer similarTotal;
    private BigDecimal similarAvg;
    private Integer quoteTotal;
    private Integer byQuoteTotal;
}
