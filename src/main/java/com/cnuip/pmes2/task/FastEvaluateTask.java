package com.cnuip.pmes2.task;

import com.cnuip.pmes2.service.BatchQuickService;
import com.cnuip.pmes2.service.ProcessOrderService;
import com.cnuip.pmes2.service.TaskOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * FastEvaluateTask
 *
 * @author: xiongwei
 * Date: 2018/2/1 上午11:18
 */
//@Component
public class FastEvaluateTask {

    private Logger logger = LoggerFactory.getLogger(FastEvaluateTask.class);

    @Autowired
    private BatchQuickService batchQuickService;

    @Autowired
    private TaskOrderService taskOrderService;
    
    @Autowired
    private ProcessOrderService processOrderService;

	/**
	 * 如果an号在p_batch_update_patent_error中,则重新跑一次
	 */
	@Scheduled(fixedDelay = 3600000)
    public void dealBatchUpdatePatentError() {
    	logger.info("*************处理专利信息同步错误开始*************");
    	batchQuickService.dealError();
    	logger.info("*************处理专利信息同步错误结束*************");
    }

	/**
	 * 从view中找超时预警且未标记的工单
	 */
	@Scheduled(fixedDelay = 60000)
    public void taskOrderRemind() {
    	logger.info("*************工单超时预警监测开始*************");
    	taskOrderService.alertTaskOrder();
    	taskOrderService.dueTaskOrder();
    	logger.info("*************工单超时预警监测结束*************");
    }

	/**
	 * 从view中找超时预警且未标记的定单
	 */
    @Scheduled(fixedDelay = 60000)
    public void processOrderRemind() {
    	logger.info("*************定单超时预警监测开始*************");
    	processOrderService.alertProcessOrder();
    	processOrderService.dueProcessOrder();
    	logger.info("*************定单超时预警监测结束*************");
    }

	/**
	 * 从sub1查询an尾号为参数的专利,参数为10时,尾号为X
	 * 如果sub1的更新时间在上次更新时间之后
	 */
	@Scheduled(fixedDelay = 3600000)
    public void batchUpdatePatentAn0() {
        batchQuickService.batchUpdatePatentWithAnEndNum(0);
    }

	@Scheduled(fixedDelay = 3600000)
	public void batchUpdatePatentAn1() {
	  	batchQuickService.batchUpdatePatentWithAnEndNum(1);
	}

	@Scheduled(fixedDelay = 3600000)
	public void batchUpdatePatentAn2() {
	  	batchQuickService.batchUpdatePatentWithAnEndNum(2);
	}

	@Scheduled(fixedDelay = 3600000)
	public void batchUpdatePatentAn3() {
	  	batchQuickService.batchUpdatePatentWithAnEndNum(3);
	}

	@Scheduled(fixedDelay = 3600000)
	public void batchUpdatePatentAn4() {
	  	batchQuickService.batchUpdatePatentWithAnEndNum(4);
	}

	@Scheduled(fixedDelay = 3600000)
	public void batchUpdatePatentAn5() {
	  	batchQuickService.batchUpdatePatentWithAnEndNum(5);
	}

	@Scheduled(fixedDelay = 3600000)
	public void batchUpdatePatentAn6() {
	  	batchQuickService.batchUpdatePatentWithAnEndNum(6);
	}

	@Scheduled(fixedDelay = 3600000)
	public void batchUpdatePatentAn7() {
	  	batchQuickService.batchUpdatePatentWithAnEndNum(7);
	}

	@Scheduled(fixedDelay = 3600000)
	public void batchUpdatePatentAn8() {
	  	batchQuickService.batchUpdatePatentWithAnEndNum(8);
	}

	@Scheduled(fixedDelay = 3600000)
	public void batchUpdatePatentAn9() {
	  	batchQuickService.batchUpdatePatentWithAnEndNum(9);
	}

    @Scheduled(fixedDelay = 3600000)
    public void batchUpdatePatentAn10() {
        batchQuickService.batchUpdatePatentWithAnEndNum(10);
    }

	/**
	 * 从sub3查询an尾号为参数的专利,参数为10时,尾号为X
	 * 如果sub1的更新时间在上次更新时间之后
	 */
	@Scheduled(fixedDelay = 3600000)
    public void batchUpdateSub3PatentAn0() {
        batchQuickService.batchUpdateSub3PatentWithAnEndNum(0);
    }

	@Scheduled(fixedDelay = 3600000)
	public void batchUpdateSub3PatentAn1() {
	  	batchQuickService.batchUpdateSub3PatentWithAnEndNum(1);
	}

	@Scheduled(fixedDelay = 3600000)
	public void batchUpdateSub3PatentAn2() {
	  	batchQuickService.batchUpdateSub3PatentWithAnEndNum(2);
	}

	@Scheduled(fixedDelay = 3600000)
	public void batchUpdateSub3PatentAn3() {
	  	batchQuickService.batchUpdateSub3PatentWithAnEndNum(3);
	}

	@Scheduled(fixedDelay = 3600000)
	public void batchUpdateSub3PatentAn4() {
	  	batchQuickService.batchUpdateSub3PatentWithAnEndNum(4);
	}

	@Scheduled(fixedDelay = 3600000)
	public void batchUpdateSub3PatentAn5() {
	  	batchQuickService.batchUpdateSub3PatentWithAnEndNum(5);
	}

	@Scheduled(fixedDelay = 3600000)
	public void batchUpdateSub3PatentAn6() {
	  	batchQuickService.batchUpdateSub3PatentWithAnEndNum(6);
	}

	@Scheduled(fixedDelay = 3600000)
	public void batchUpdateSub3PatentAn7() {
	  	batchQuickService.batchUpdateSub3PatentWithAnEndNum(7);
	}

	@Scheduled(fixedDelay = 3600000)
	public void batchUpdateSub3PatentAn8() {
	  	batchQuickService.batchUpdateSub3PatentWithAnEndNum(8);
	}

	@Scheduled(fixedDelay = 3600000)
	public void batchUpdateSub3PatentAn9() {
	  	batchQuickService.batchUpdateSub3PatentWithAnEndNum(9);
	}

    @Scheduled(fixedDelay = 3600000)
    public void batchUpdateSub3PatentAn10() {
        batchQuickService.batchUpdateSub3PatentWithAnEndNum(10);
    }

}
