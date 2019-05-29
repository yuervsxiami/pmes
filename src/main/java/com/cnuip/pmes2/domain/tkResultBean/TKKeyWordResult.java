package com.cnuip.pmes2.domain.tkResultBean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/6/27.
 * Time: 9:36
 */
@Getter
@Setter
public class TKKeyWordResult implements Serializable{
    private Boolean success;
    private String message;
    private Object error;
    private DataResult result;
}
