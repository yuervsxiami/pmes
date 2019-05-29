package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.domain.el.ElResult;
import com.cnuip.pmes2.repository.el.ElResultRepository;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2019/5/24.
 * Time: 14:07
 */
public class SynchronizedElsThread implements Runnable{

    private ElResultRepository elResultRepository;

    private ElResult elResult;

    public SynchronizedElsThread(ElResultRepository elResultRepository,ElResult elResult) {
        this.elResult=elResult;
        this.elResultRepository=elResultRepository;
    }

    @Override
    public void run() {
        elResultRepository.save(elResult);
    }
}
