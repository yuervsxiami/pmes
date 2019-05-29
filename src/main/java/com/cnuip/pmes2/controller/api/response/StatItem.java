package com.cnuip.pmes2.controller.api.response;

import lombok.Data;

/**
 * StatItem
 *
 * @author: xiongwei
 * Date: 2018/3/29 下午2:57
 */
@Data
public class StatItem<T> {

    private String name;
    private T value;

}
