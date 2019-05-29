package com.cnuip.pmes2.task;

import com.cnuip.pmes2.service.ElPatentService;
import com.cnuip.pmes2.service.SegmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author: xiongwei
 * Date: 2018/2/7 下午3:33
 * 此类会把批量任务同步到搜索引擎,下面注释掉的是为了批量跑,开的多个任务,不用开启
 */
//@Component
public class SegmentTask {
    private Logger logger = LoggerFactory.getLogger(SegmentTask.class);

    @Autowired
    private SegmentService segmentService;

//    @Scheduled(fixedDelay = 60000000)
//    public void segment() {
//        segmentService.generateTxt(0);
//        segmentService.generateTxt(1);
//        segmentService.generateTxt(2);
//        segmentService.generateTxt(3);
//        segmentService.generateTxt(4);
//        segmentService.generateTxt(5);
//        segmentService.generateTxt(6);
//        segmentService.generateTxt(7);
//        segmentService.generateTxt(8);
//        segmentService.generateTxt(9);
//        segmentService.generateTxt(10);
//    }

    @Scheduled(fixedDelay = 60000000)
    public void segment0() {
        segmentService.generateTxt(0);
    }

    @Scheduled(fixedDelay = 60000000)
    public void segment1() {
        segmentService.generateTxt(1);
    }

    @Scheduled(fixedDelay = 60000000)
    public void segment2() {
        segmentService.generateTxt(2);
    }

    @Scheduled(fixedDelay = 60000000)
    public void segment3() {
        segmentService.generateTxt(3);
    }

    @Scheduled(fixedDelay = 60000000)
    public void segment4() {
        segmentService.generateTxt(4);
    }

    @Scheduled(fixedDelay = 60000000)
    public void segment5() {
        segmentService.generateTxt(5);
    }

    @Scheduled(fixedDelay = 60000000)
    public void segment6() {
        segmentService.generateTxt(6);
    }

    @Scheduled(fixedDelay = 60000000)
    public void segment7() {
        segmentService.generateTxt(7);
    }

    @Scheduled(fixedDelay = 60000000)
    public void segment8() {
        segmentService.generateTxt(8);
    }

    @Scheduled(fixedDelay = 60000000)
    public void segment9() {
        segmentService.generateTxt(9);
    }

    @Scheduled(fixedDelay = 60000000)
    public void segment10() {
        segmentService.generateTxt(10);
    }

}
