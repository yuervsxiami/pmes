package com.cnuip.pmes2.controller.api.response;

import com.cnuip.pmes2.domain.core.Ntcc;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2019/5/23.
 * Time: 15:02
 */
@Setter
@Getter
public class NtccVo extends Ntcc implements Serializable{

    private List<NtccVo> children;
}
