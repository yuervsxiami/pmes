package com.cnuip.pmes2.service;

import com.cnuip.pmes2.domain.el.ElProfessor;
import com.cnuip.pmes2.domain.el.Requirement;
import com.cnuip.pmes2.exception.BussinessLogicException;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/11/28.
 * Time: 10:13
 */
public interface RabbitMqService {
    ElProfessor saveProfessor(String userStr) throws Exception;
    Requirement saveRequirement(String requirementStr) throws Exception;
}
