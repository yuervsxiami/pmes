package com.cnuip.pmes2.controller.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * CurrentProcessRect
 *
 * @author: xiongwei
 * Date: 2018/1/28 下午7:19
 */
@Data
@AllArgsConstructor
public class CurrentProcessRect {

    private int x;
    private int y;
    private int width;
    private int height;

}
