package com.cnuip.pmes2.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnuip.pmes2.constant.TimedTaskType;
import com.cnuip.pmes2.domain.core.Patent;
import com.cnuip.pmes2.domain.core.TimedTask;
import com.cnuip.pmes2.domain.inventory.LegalStatusInfo;
import com.cnuip.pmes2.domain.inventory.PatentInfo;
import com.cnuip.pmes2.domain.inventory.PatentSub3Info;
import com.cnuip.pmes2.domain.inventory.PatfeeInfo;
import com.cnuip.pmes2.domain.inventory.PatprsexploitationInfo;
import com.cnuip.pmes2.domain.inventory.PatprspreservationInfo;
import com.cnuip.pmes2.domain.inventory.PatprstransferInfo;
import com.cnuip.pmes2.domain.inventory.ScoreInfo;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.repository.core.OriginalPatentMapper;
import com.cnuip.pmes2.repository.core.TimedTaskMapper;
import com.cnuip.pmes2.repository.inventory.LegalStatusInfoMapper;
import com.cnuip.pmes2.repository.inventory.PatentInfoMapper;
import com.cnuip.pmes2.repository.inventory.PatfeeInfoMapper;
import com.cnuip.pmes2.repository.inventory.PatprsexploitationInfoMapper;
import com.cnuip.pmes2.repository.inventory.PatprspreservationInfoMapper;
import com.cnuip.pmes2.repository.inventory.PatprstransferInfoMapper;
import com.cnuip.pmes2.repository.inventory.ScoreInfoMapper;
import com.cnuip.pmes2.service.OriginalPatentService;

/**
* Create By Crixalis:
* 2018年3月3日 下午4:40:40
*/
@Service
public class OriginalPatentServiceImpl implements OriginalPatentService {
	
	@Autowired
	private OriginalPatentMapper originalPatentMapper;
	@Autowired
	private LegalStatusInfoMapper legalStatusInfoMapper;
	@Autowired
	private PatentInfoMapper patentInfoMapper;
	@Autowired
	private PatfeeInfoMapper patfeeInfoMapper;
	@Autowired
	private PatprsexploitationInfoMapper patprsexploitationInfoMapper;
	@Autowired
	private PatprspreservationInfoMapper patprspreservationInfoMapper;
	@Autowired
	private PatprstransferInfoMapper patprstransferInfoMapper;
	@Autowired
	private ScoreInfoMapper scoreInfoMapper;
	@Autowired
	private TimedTaskMapper timedTaskMapper;

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void synchronizeSub1() {
		logger.info("*********同步sub1开始*********");
//		Date lastUpdateTime = originalPatentMapper.findSub1LastUpdateTime();
		int type = TimedTaskType.BatchUpdateSub1.getType();
		Date lastUpdateTime = timedTaskMapper.selectLastUpdateTime(type);
		if(lastUpdateTime == null) {
			lastUpdateTime = new Date(1519833600000L);
		}
		int total = patentInfoMapper.selectTotalAfterTimeNum(lastUpdateTime);
		if(total == 0) {
			logger.info("待同步sub1数=0,终止同步");
			return;
		}
		TimedTask timedTask = new TimedTask(type, total,lastUpdateTime);
		timedTaskMapper.insert(timedTask);
		logger.info("lastUpdateTime=" + lastUpdateTime + " 待同步sub1数=" + total);
		int pageSize = 1000;
		for(int i = 0; i < Math.ceil(1.0 * total / pageSize) + 2; i++) {
			try {
				List<PatentInfo> list = patentInfoMapper.findSub1AfterTime(lastUpdateTime, i * pageSize, pageSize);;
				if(list.size() == 0) {
					break;
				}
				originalPatentMapper.replaceSub1(list);
				int amount = Math.min(pageSize, total - i * pageSize);
				timedTaskMapper.updateFinishAmount(timedTask.getId(), amount);
				logger.info("sub1完成"+ amount +"条");
			} catch (Exception e) {
				logger.error("同步sub1数据出错", e);
			}
		}
		timedTaskMapper.finish(timedTask.getId());
		logger.info("*********同步sub1结束*********");
	}
	
	@Override
	public void synchronizeSub3() {
		logger.info("*********同步sub3开始*********");
//		Date lastUpdateTime = originalPatentMapper.findSub3LastUpdateTime();
		int type = TimedTaskType.BatchUpdateSub3.getType();
		Date lastUpdateTime = timedTaskMapper.selectLastUpdateTime(type);
		if(lastUpdateTime == null) {
			lastUpdateTime = new Date(1519833600000L);
		}
		int total = patentInfoMapper.selectTotalSub3AfterTimeNum(lastUpdateTime);
		if(total == 0) {
			logger.info("待同步sub3数=0,终止同步");
			return;
		}
		TimedTask timedTask = new TimedTask(type, total,lastUpdateTime);
		timedTaskMapper.insert(timedTask);
		logger.info("lastUpdateTime=" + lastUpdateTime + " 待同步sub3数=" + total);
		int pageSize = 1000;
		for(int i = 0; i < Math.ceil(1.0 * total / pageSize) + 2; i++) {
			try{
				List<PatentSub3Info> list = patentInfoMapper.findSub3AfterTime(lastUpdateTime, i * pageSize, pageSize);
				if(list.size() == 0) {
					break;
				}
				originalPatentMapper.replaceSub3(list);
				int amount = Math.min(pageSize, total - i * pageSize);
				timedTaskMapper.updateFinishAmount(timedTask.getId(), amount);
				logger.info("sub3完成"+ amount +"条");
			} catch (Exception e) {
				logger.error("同步sub3数据出错", e);
			}
		}
		timedTaskMapper.finish(timedTask.getId());
		logger.info("*********同步sub3结束*********");
	}
	
	@Override
	public void synchronizeLegalStatus() {
		logger.info("*********同步legalStatus开始*********");
//		Date lastUpdateTime = originalPatentMapper.findLegalStatusLastUpdateTime();
		int type = TimedTaskType.BatchUpdateLegalStatus.getType();
		Date lastUpdateTime = timedTaskMapper.selectLastUpdateTime(type);
		if(lastUpdateTime == null) {
			lastUpdateTime = new Date(1519833600000L);
		}
		int total = legalStatusInfoMapper.getTotal(lastUpdateTime);
		if(total == 0) {
			logger.info("待同步legalStatus数=0,终止同步");
			return;
		}
		TimedTask timedTask = new TimedTask(type, total,lastUpdateTime);
		timedTaskMapper.insert(timedTask);
		logger.info("lastUpdateTime=" + lastUpdateTime + " 待同步legalStatus数=" + total);
		int pageSize = 1000;
		for(int i = 0; i < Math.ceil(1.0 * total / pageSize) + 2; i++) {
			try {
				List<LegalStatusInfo> list = legalStatusInfoMapper.findAfterTime(lastUpdateTime, i * pageSize, pageSize);
				if(list.size() == 0) {
					break;
				}
				originalPatentMapper.replaceLegal(list);
				int amount = Math.min(pageSize, total - i * pageSize);
				timedTaskMapper.updateFinishAmount(timedTask.getId(), amount);
				logger.info("legalStatus完成"+ amount +"条");
			} catch (Exception e) {
				logger.error("同步legalStatus数据出错", e);
			}
		}
		timedTaskMapper.finish(timedTask.getId());
		logger.info("*********同步legalStatus结束*********");
	}
	
	@Override
	public void synchronizeFee() {
		logger.info("*********同步fee开始*********");
//		Date lastUpdateTime = originalPatentMapper.findFeeLastUpdateTime();
		int type = TimedTaskType.BatchUpdateFee.getType();
		Date lastUpdateTime = timedTaskMapper.selectLastUpdateTime(type);
		if(lastUpdateTime == null) {
			lastUpdateTime = new Date(1519833600000L);
		}
		int total = patfeeInfoMapper.getTotal(lastUpdateTime);
		if(total == 0) {
			logger.info("待同步fee数=0,终止同步");
			return;
		}
		TimedTask timedTask = new TimedTask(type, total,lastUpdateTime);
		timedTaskMapper.insert(timedTask);
		logger.info("lastUpdateTime=" + lastUpdateTime + " 待同步fee数=" + total);
		int pageSize = 1000;
		for(int i = 0; i < Math.ceil(1.0 * total / pageSize) + 2; i++) {
			try {
				List<PatfeeInfo> list = patfeeInfoMapper.findAfterTime(lastUpdateTime, i * pageSize, pageSize);
				if(list.size() == 0) {
					break;
				}
				originalPatentMapper.replaceFee(list);
				int amount = Math.min(pageSize, total - i * pageSize);
				timedTaskMapper.updateFinishAmount(timedTask.getId(), amount);
				logger.info("fee完成"+ amount +"条");
			} catch (Exception e) {
				logger.error("同步fee数据出错", e);
			}
		}
		timedTaskMapper.finish(timedTask.getId());
		logger.info("*********同步fee结束*********");
	}
	
	@Override
	public void synchronizePrsexploitation() {
		logger.info("*********同步prsexploitation开始*********");
//		Date lastUpdateTime = originalPatentMapper.findPrsexploitationLastUpdateTime();
		int type = TimedTaskType.BatchUpdatePrsexploitation.getType();
		Date lastUpdateTime = timedTaskMapper.selectLastUpdateTime(type);
		if(lastUpdateTime == null) {
			lastUpdateTime = new Date(1519833600000L);
		}
		int total = patprsexploitationInfoMapper.getTotal(lastUpdateTime);
		if(total == 0) {
			logger.info("待同步prsexploitation数=0,终止同步");
			return;
		}
		TimedTask timedTask = new TimedTask(type, total,lastUpdateTime);
		timedTaskMapper.insert(timedTask);
		logger.info("lastUpdateTime=" + lastUpdateTime + " 待同步prsexploitation数=" + total);
		int pageSize = 1000;
		for(int i = 0; i < Math.ceil(1.0 * total / pageSize) + 2; i++) {
			try {
				List<PatprsexploitationInfo> list = patprsexploitationInfoMapper.findAfterTime(lastUpdateTime, i * pageSize, pageSize);
				if(list.size() == 0) {
					break;
				}
				originalPatentMapper.replacePrsexploitation(list);
				int amount = Math.min(pageSize, total - i * pageSize);
				timedTaskMapper.updateFinishAmount(timedTask.getId(), amount);
				logger.info("prsexploitation完成"+ amount +"条");
			} catch (Exception e) {
				logger.error("同步prsexploitation数据出错", e);
			}
		}
		timedTaskMapper.finish(timedTask.getId());
		logger.info("*********同步prsexploitation结束*********");
	}
	
	@Override
	public void synchronizePrspreservation() {
		logger.info("*********同步prspreservation开始*********");
//		Date lastUpdateTime = originalPatentMapper.findPrspreservationLastUpdateTime();
		int type = TimedTaskType.BatchUpdatePrspreservation.getType();
		Date lastUpdateTime = timedTaskMapper.selectLastUpdateTime(type);
		if(lastUpdateTime == null) {
			lastUpdateTime = new Date(1519833600000L);
		}
		int total = patprspreservationInfoMapper.getTotal(lastUpdateTime);
		if(total == 0) {
			logger.info("待同步prspreservation数=0,终止同步");
			return;
		}
		TimedTask timedTask = new TimedTask(type, total,lastUpdateTime);
		timedTaskMapper.insert(timedTask);
		logger.info("lastUpdateTime=" + lastUpdateTime + " 待同步prspreservation数=" + total);
		int pageSize = 1000;
		for(int i = 0; i < Math.ceil(1.0 * total / pageSize) +2; i++) {
			try {
				List<PatprspreservationInfo> list = patprspreservationInfoMapper.findAfterTime(lastUpdateTime, i * pageSize, pageSize);
				if(list.size() == 0) {
					break;
				}
				originalPatentMapper.replacePrspreservation(list);
				int amount = Math.min(pageSize, total - i * pageSize);
				timedTaskMapper.updateFinishAmount(timedTask.getId(), amount);
				logger.info("prspreservation完成"+ amount +"条");
			} catch (Exception e) {
				logger.error("同步prspreservation数据出错", e);
			}
		}
		timedTaskMapper.finish(timedTask.getId());
		logger.info("*********同步prspreservation结束*********");
	}
	
	@Override
	public void synchronizePrstransfer() {
		logger.info("*********同步prstransfer开始*********");
//		Date lastUpdateTime = originalPatentMapper.findPrstransferLastUpdateTime();
		int type = TimedTaskType.BatchUpdatePrstransfer.getType();
		Date lastUpdateTime = timedTaskMapper.selectLastUpdateTime(type);
		if(lastUpdateTime == null) {
			lastUpdateTime = new Date(1519833600000L);
		}
		int total = patprstransferInfoMapper.getTotal(lastUpdateTime);
		if(total == 0) {
			logger.info("待同步lastUpdateTime数=0,终止同步");
			return;
		}
		TimedTask timedTask = new TimedTask(type, total,lastUpdateTime);
		timedTaskMapper.insert(timedTask);
		logger.info("lastUpdateTime=" + lastUpdateTime + " 待同步prstransfer数=" + total);
		int pageSize = 1000;
		for(int i = 0; i < Math.ceil(1.0 * total / pageSize) + 2; i++) {
			try {
				List<PatprstransferInfo> list = patprstransferInfoMapper.findAfterTime(lastUpdateTime, i * pageSize, pageSize);
				if(list.size() == 0) {
					break;
				}
				originalPatentMapper.replacePrstransfer(list);
				int amount = Math.min(pageSize, total - i * pageSize);
				timedTaskMapper.updateFinishAmount(timedTask.getId(), amount);
				logger.info("prstransfer完成"+ amount +"条");
			} catch (Exception e) {
				logger.error("同步prstransfer数据出错", e);
			}
		}
		timedTaskMapper.finish(timedTask.getId());
		logger.info("*********同步prstransfer结束*********");
	}
	
	@Override
	public void synchronizeScore() {
		logger.info("*********同步score开始*********");
//		Date lastUpdateTime = originalPatentMapper.findScoreLastUpdateTime();
		int type = TimedTaskType.BatchUpdateScore.getType();
		Date lastUpdateTime = timedTaskMapper.selectLastUpdateTime(type);
		if(lastUpdateTime == null) {
			lastUpdateTime = new Date(1519833600000L);
		}
		int total = scoreInfoMapper.getTotal(lastUpdateTime);
		if(total == 0) {
			logger.info("待同步score数=0,终止同步");
			return;
		}
		TimedTask timedTask = new TimedTask(type, total,lastUpdateTime);
		timedTaskMapper.insert(timedTask);
		logger.info("lastUpdateTime=" + lastUpdateTime + " 待同步score数=" + total);
		int pageSize = 1000;
		for(int i = 0; i < Math.ceil(1.0 * total / pageSize) + 2; i++) {
			try {
				List<ScoreInfo> list = scoreInfoMapper.findAfterTime(lastUpdateTime, i * pageSize, pageSize);
				if(list.size() == 0) {
					break;
				}
				originalPatentMapper.replaceScoreInfo(list);
				int amount = Math.min(pageSize, total - i * pageSize);
				timedTaskMapper.updateFinishAmount(timedTask.getId(), amount);
				logger.info("score完成"+ amount +"条");
			} catch (Exception e) {
				logger.error("同步score数据出错", e);
			}
		}
		timedTaskMapper.finish(timedTask.getId());
		logger.info("*********同步score结束*********");
	}

}
