package com.cnuip.pmes2.domain.core;

import lombok.Data;

/**
 * @auhor Crixalis
 * @date 2018/3/23 19:16
 */
@Data
public class PatentIndex {

    private Long id;
    private String an;
    private Integer hasBaseIndex;
    private Integer hasBaseIndexing;
    private Integer hasValueIndex;
    private Integer hasValueIndexing;
    private Integer hasPriceIndex;
    private Integer hasPriceIndexing;
    private Integer hasDeepIndex;
    private Integer hasDeepIndexing;


}
