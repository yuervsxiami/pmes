package com.cnuip.pmes2.domain.tkResultBean;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/6/28.
 * Time: 16:48
 */
@Setter
@Getter
public class TKIPCResult {
    private boolean success;
    private String message;
    private Object error;
    private IPCTotal result;
}
