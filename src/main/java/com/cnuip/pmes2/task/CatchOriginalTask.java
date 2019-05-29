package com.cnuip.pmes2.task;

import com.cnuip.pmes2.service.OriginalPatentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
* Create By Crixalis:
* 2018年3月10日 下午4:55:40
 * 该类为同步8张表的方法,建议开启
*/
//@Component
public class CatchOriginalTask {
	
	@Autowired
	private OriginalPatentService originalPatentService;

    @Scheduled(cron="0 59 12,23 * * ?")
	public void synchronizeSub1(){
		originalPatentService.synchronizeSub1();
	}

    @Scheduled(cron="0 59 12,23 * * ?")
	public void synchronizeSub3(){
		originalPatentService.synchronizeSub3();
	}

    @Scheduled(cron="0 59 12,23 * * ?")
	public void synchronizeLegalStatus(){
		originalPatentService.synchronizeLegalStatus();
	}

    @Scheduled(cron="0 59 12,23 * * ?")
	public void synchronizeFee(){
		originalPatentService.synchronizeFee();
	}

    @Scheduled(cron="0 59 12,23 * * ?")
	public void synchronizePrsexploitation(){
		originalPatentService.synchronizePrsexploitation();
	}

    @Scheduled(cron="0 59 12,23 * * ?")
	public void synchronizePrspreservation(){
		originalPatentService.synchronizePrspreservation();
	}

    @Scheduled(cron="0 59 12,23 * * ?")
	public void synchronizePrstransfer(){
		originalPatentService.synchronizePrstransfer();
	}

    //@Scheduled(cron="0 0 0 */1 * *")
	public void synchronizeScore(){
		originalPatentService.synchronizeScore();
	}
}
