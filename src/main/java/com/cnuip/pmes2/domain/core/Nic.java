package com.cnuip.pmes2.domain.core;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2019/5/21.
 * Time: 16:38
 */
@Setter
@Getter
public class Nic implements Serializable{
    private Integer id;
    private String code;
    private String name;
    private Integer level;
    private String preCode;
    private String description;
}
