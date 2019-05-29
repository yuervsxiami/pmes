package com.cnuip.pmes2.task;

import com.cnuip.pmes2.service.BatchQuickService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
public class UpdatePatentTask {

    private Logger logger = LoggerFactory.getLogger(UpdatePatentTask.class);

    @Autowired
    private BatchQuickService batchQuickService;

    /**
     * 自动找sub1中有但是专利主表中没有的专利,如果有,则把信息记录进入专利主表
     */
    @Scheduled(fixedDelay = 3600000)
    public void batchInsertPatent() {
        logger.info("*************把专利信息插入至主表开始*************");
        batchQuickService.batchInsert();
        logger.info("*************把专利信息插入至主表结束*************");
    }

    /**
     * 找has_batch_indexed为0,且法律状态不为无效的专利,如果有则跑一遍批量流程,参数为专利主表id的尾号
     */
    @Scheduled(fixedDelay = 3600000)
    public void batchQuick0() {
        batchQuickService.batchQuickWithEndNum(0);
    }

    @Scheduled(fixedDelay = 3600000)
    public void batchQuick1() {
        batchQuickService.batchQuickWithEndNum(1);
    }

    @Scheduled(fixedDelay = 3600000)
    public void batchQuick2() {
        batchQuickService.batchQuickWithEndNum(2);
    }

    @Scheduled(fixedDelay = 3600000)
    public void batchQuick3() {
        batchQuickService.batchQuickWithEndNum(3);
    }

    @Scheduled(fixedDelay = 3600000)
    public void batchQuick4() {
        batchQuickService.batchQuickWithEndNum(4);
    }

    @Scheduled(fixedDelay = 3600000)
    public void batchQuick5() {
        batchQuickService.batchQuickWithEndNum(5);
    }

    @Scheduled(fixedDelay = 3600000)
    public void batchQuick6() {
        batchQuickService.batchQuickWithEndNum(6);
    }

    @Scheduled(fixedDelay = 3600000)
    public void batchQuick7() {
        batchQuickService.batchQuickWithEndNum(7);
    }

    @Scheduled(fixedDelay = 3600000)
    public void batchQuick8() {
        batchQuickService.batchQuickWithEndNum(8);
    }

    @Scheduled(fixedDelay = 3600000)
    public void batchQuick9() {
        batchQuickService.batchQuickWithEndNum(9);
    }

}
