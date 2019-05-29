package com.cnuip.pmes2.task;

import com.cnuip.pmes2.service.ElPatentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * PatentIndexTask
 *
 * @author: xiongwei
 * Date: 2018/2/7 下午3:33
 * 此类会把批量任务同步到搜索引擎,下面注释掉的是为了批量跑,开的多个任务,不用开启
 */
//@Component
public class PatentIndexTask {
    private Logger logger = LoggerFactory.getLogger(PatentIndexTask.class);

    @Autowired
    private ElPatentService elPatentService;

    /**
     * 每隔1分查询
     */
    @Scheduled(fixedDelay = 60000)
    public void autoIndex() {
        this.elPatentService.autoIndex();
    }

    /**
     * 批量归档批量计算的专利数据
    @Scheduled(fixedDelay = 1000)
    public void batchIndexAll() {
        this.elPatentService.batchIndexForSuffix("0");
    }

    @Scheduled(fixedDelay = 1000)
    public void batchIndex0() {
        this.elPatentService.batchIndexForSuffix("0");
    }

    @Scheduled(fixedDelay = 1000)
    public void batchIndex1() {
        this.elPatentService.batchIndexForSuffix("1");
    }

    @Scheduled(fixedDelay = 1000)
    public void batchIndex2() {
        this.elPatentService.batchIndexForSuffix("2");
    }

    @Scheduled(fixedDelay = 1000)
    public void batchIndex3() {
        this.elPatentService.batchIndexForSuffix("3");
    }

    @Scheduled(fixedDelay = 1000)
    public void batchIndex4() {
        this.elPatentService.batchIndexForSuffix("4");
    }

    @Scheduled(fixedDelay = 1000)
    public void batchIndex5() {
        this.elPatentService.batchIndexForSuffix("5");
    }

    @Scheduled(fixedDelay = 1000)
    public void batchIndex6() {
        this.elPatentService.batchIndexForSuffix("6");
    }

    @Scheduled(fixedDelay = 1000)
    public void batchIndex7() {
        this.elPatentService.batchIndexForSuffix("7");
    }

    @Scheduled(fixedDelay = 1000)
    public void batchIndex8() {
        this.elPatentService.batchIndexForSuffix("8");
    }

    @Scheduled(fixedDelay = 1000)
    public void batchIndex9() {
        this.elPatentService.batchIndexForSuffix("9");
    }

    @Scheduled(fixedDelay = 1000)
    public void batchIndexX() {
        this.elPatentService.batchIndexForSuffix("X");
    }

    @Scheduled(fixedDelay = 600000)
    public void batchUpdatePatentValue() {
        this.elPatentService.batchUpdatePatentValue();
    }
     */

    /**
     * 批量归档批量计算的专利数据
    @Scheduled(fixedDelay = 600000)
    public void batchIndex0() {
        this.elPatentService.batchIndexForMode(0);
    }

    @Scheduled(fixedDelay = 600000)
    public void batchIndex1() {
        this.elPatentService.batchIndexForMode(1);
    }

    @Scheduled(fixedDelay = 600000)
    public void batchIndex2() {
        this.elPatentService.batchIndexForMode(2);
    }

    @Scheduled(fixedDelay = 600000)
    public void batchIndex3() {
        this.elPatentService.batchIndexForMode(3);
    }

    @Scheduled(fixedDelay = 600000)
    public void batchIndex4() {
        this.elPatentService.batchIndexForMode(4);
    }

    @Scheduled(fixedDelay = 600000)
    public void batchIndex5() {
        this.elPatentService.batchIndexForMode(5);
    }

    @Scheduled(fixedDelay = 600000)
    public void batchIndex6() {
        this.elPatentService.batchIndexForMode(6);
    }

    @Scheduled(fixedDelay = 600000)
    public void batchIndex7() {
        this.elPatentService.batchIndexForMode(7);
    }

    @Scheduled(fixedDelay = 600000)
    public void batchIndex8() {
        this.elPatentService.batchIndexForMode(8);
    }

    @Scheduled(fixedDelay = 600000)
    public void batchIndex9() {
        this.elPatentService.batchIndexForMode(9);
    }*/

    /**
     * 更新type=4的专利
    @Scheduled(fixedDelay = 600000)
    public void batchFixIndex0() {
        this.elPatentService.batchFixIndexForMode(0);
    }

    @Scheduled(fixedDelay = 600000)
    public void batchFixIndex1() {
        this.elPatentService.batchFixIndexForMode(1);
    }

    @Scheduled(fixedDelay = 600000)
    public void batchFixIndex2() {
        this.elPatentService.batchFixIndexForMode(2);
    }

    @Scheduled(fixedDelay = 600000)
    public void batchFixIndex3() {
        this.elPatentService.batchFixIndexForMode(3);
    }

    @Scheduled(fixedDelay = 600000)
    public void batchFixIndex4() {
        this.elPatentService.batchFixIndexForMode(4);
    }

    @Scheduled(fixedDelay = 600000)
    public void batchFixIndex5() {
        this.elPatentService.batchFixIndexForMode(5);
    }

    @Scheduled(fixedDelay = 600000)
    public void batchFixIndex6() {
        this.elPatentService.batchFixIndexForMode(6);
    }

    @Scheduled(fixedDelay = 600000)
    public void batchFixIndex7() {
        this.elPatentService.batchFixIndexForMode(7);
    }

    @Scheduled(fixedDelay = 600000)
    public void batchFixIndex8() {
        this.elPatentService.batchFixIndexForMode(8);
    }

    @Scheduled(fixedDelay = 600000)
    public void batchFixIndex9() {
        this.elPatentService.batchFixIndexForMode(9);
    }
    */

}
