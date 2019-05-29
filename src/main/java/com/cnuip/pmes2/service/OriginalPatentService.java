package com.cnuip.pmes2.service;
/**
* Create By Crixalis:
* 2018年3月3日 下午4:32:29
*/
public interface OriginalPatentService {
	
	void synchronizeSub1();

	void synchronizeSub3();

	void synchronizeLegalStatus();

	void synchronizeFee();

	void synchronizePrsexploitation();

	void synchronizePrspreservation();

	void synchronizePrstransfer();

	void synchronizeScore();

}
