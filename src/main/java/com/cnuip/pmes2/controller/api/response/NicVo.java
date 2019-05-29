package com.cnuip.pmes2.controller.api.response;

import com.cnuip.pmes2.domain.core.Nic;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2019/5/23.
 * Time: 14:36
 */
@Setter
@Getter
public class NicVo extends Nic implements Serializable {
    private List<NicVo> children;
}
