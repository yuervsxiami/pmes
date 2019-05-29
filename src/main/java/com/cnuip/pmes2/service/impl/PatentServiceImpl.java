package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.controller.api.response.PatentAllDetail;
import com.cnuip.pmes2.controller.api.response.PatentStatusInfo;
import com.cnuip.pmes2.domain.core.ElPatent;
import com.cnuip.pmes2.domain.core.IndexEr;
import com.cnuip.pmes2.domain.core.Patent;
import com.cnuip.pmes2.domain.core.TaskOrderLabel;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.repository.core.PatentMapper;
import com.cnuip.pmes2.repository.core.ProcessOrderMapper;
import com.cnuip.pmes2.repository.core.TaskOrderLabelMapper;
import com.cnuip.pmes2.repository.core.TaskOrderMapper;
import com.cnuip.pmes2.repository.el.ElPatentRepository;
import com.cnuip.pmes2.repository.inventory.*;
import com.cnuip.pmes2.service.PatentEvaluateService;
import com.cnuip.pmes2.service.PatentService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.elasticsearch.common.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * PatentServiceImpl
 *
 * @author: xiongwei
 * Date: 2018/2/7 下午1:51
 */
@Transactional(readOnly = true)
@Service
public class PatentServiceImpl implements PatentService {

    @Autowired
    private PatentMapper patentMapper;
    @Autowired
    private TaskOrderLabelMapper taskOrderLabelMapper;
    @Autowired
    private ElPatentRepository elPatentRepository;
    @Autowired
    private ProcessOrderMapper processOrderMapper;
    @Autowired
    private TaskOrderMapper taskOrderMapper;
    @Autowired
    private PatentEvaluateService patentEvaluateService;
    @Autowired
    private LegalStatusInfoMapper legalStatusInfoMapper;
    @Autowired
    private PatprsexploitationInfoMapper patprsexploitationInfoMapper;
    @Autowired
    private PatprspreservationInfoMapper patprspreservationInfoMapper;
    @Autowired
    private PatprstransferInfoMapper patprstransferInfoMapper;
    @Autowired
    private ScoreInfoMapper scoreInfoMapper;
    @Autowired
    private PatfeeInfoMapper patfeeInfoMapper;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Patent findByAn(String an) {
        return this.patentMapper.selectByAn(an);
    }

    @Override
    public List<ElPatent> findUpdatedElPatents(Date lastUpdate, int pageNum, int pageSize) {
        List<ElPatent> patents = this.patentMapper.findUpdatedElPatents(lastUpdate, pageNum, pageSize);
        for (ElPatent patent: patents) {
            this.mergeWithBatchLabels(patent);
        }
        return patents;
    }

    @Override
    public Patent findPatentByIdWithFullLabels(Long id) {
        Patent patent = this.elPatentRepository.findById(id);
        if (patent == null || patent.getLatestLabels() == null || patent.getLatestLabels().size() == 0) {
            patent = this.patentMapper.findPatentByIdWithFullLabels(id);
            if (patent != null) {
                this.mergeWithBatchLabels(patent);
            }
        }
        return patent;
    }

    @Override
    public Patent findPatentByIdWithManualLabels(Long id) {
        return this.patentMapper.findPatentByIdWithFullLabels(id);
    }

    @Override
    public Patent selectSimpleByPrimaryKey(Long id) {
        return patentMapper.selectSimpleByPrimaryKey(id);
    }

    @Override
    public Patent findPatentByAnWithFullLabels(String an) {
        Patent patent = null;
        List<ElPatent> patents = this.elPatentRepository.findByAn(an);
        //检查查找的专利数量
        if(patents!=null || patents.size()>0) {
            if(patents.size()==1) {
            	patent = patents.get(0);
			}
			if(patents.size()>1) {
            	Patent temporary = this.patentMapper.selectByAn(an);
            	for(Patent p: patents) {
					if(temporary.getId().equals(p.getId())) {
						patent = p;
					}
				}
			}
        }
        if (patent == null || patent.getLatestLabels() == null || patent.getLatestLabels().size() == 0) {
            patent = this.patentMapper.findPatentByAnWithFullLabels(an);
            if (patent != null) {
                this.mergeWithBatchLabels(patent);
            }
        }
        return patent;
    }

    @Override
    public void mergeWithBatchLabels(Patent patent) {
        List<TaskOrderLabel> batchLabels = this.taskOrderLabelMapper.findLatestProcessLabels(patent.getId(), 1);
        if (batchLabels.size() == 0) {
            ElPatent elPatent = this.elPatentRepository.findById(patent.getId());
            if (elPatent != null && elPatent.getLatestLabels() != null) {
                batchLabels.addAll(elPatent.getLatestLabels());
            }
        }
        if (batchLabels.size() == 0) {
            return;
        }
        Map<String, TaskOrderLabel> all = new HashMap<>();
        for (TaskOrderLabel label: batchLabels) {
            all.put(label.getLabel().getKey(), label);
        }
        if(patent.getLatestLabels()!=null && patent.getLatestLabels().size()>0) {
            for (TaskOrderLabel label: patent.getLatestLabels()) {
                all.put(label.getLabel().getKey(), label);
            }
        }
        List<TaskOrderLabel> allLabels = new ArrayList<>();
        allLabels.addAll(all.values());
        patent.setLatestLabels(allLabels);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized void deleteIndexedPatentFastOrders(Long id) throws BussinessLogicException {
        List<Long> processOrderIds = processOrderMapper.findOrderIdsByInstanceIdAndProcessType(id, 1);
        List<Long> taskOrderIds = new ArrayList<>();
        if (processOrderIds.size() > 0) {
            taskOrderIds.addAll(taskOrderMapper.findOrderIdsByProcessIds(Strings.collectionToDelimitedString(processOrderIds, ",")));
        }
        try {
            if (taskOrderIds.size() > 0) {
                this.taskOrderLabelMapper.deleteLabelByTaskOrderIds(Strings.collectionToDelimitedString(taskOrderIds, ","));
            }
            if (taskOrderIds.size() > 0) {
                this.taskOrderMapper.deleteOrderByIds(Strings.collectionToDelimitedString(taskOrderIds, ","));
            }
            if (processOrderIds.size() > 0) {
                this.processOrderMapper.deleteOrderByIds(Strings.collectionToDelimitedString(processOrderIds, ","));
            }
        } catch (Exception e) {
            throw new BussinessLogicException("删除已索引专利定单数据出错", e);
        }
    }

    @Override
    public PageInfo<IndexEr> selectIndexError(int pageSize, int pageNum) {
        Page<IndexEr> page = (Page<IndexEr>) patentMapper.selectIndexError(pageNum,pageSize);
        return page.toPageInfo();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateHasBatchIndexed(Long id) {
        return this.patentMapper.updateHasBatchIndexed(id);
    }

	@Override
	public PatentAllDetail findPatentDetail(String an) {
        PatentAllDetail detail = new PatentAllDetail();
        Patent patent = findPatentByAnWithFullLabels(an);
        if (patent != null) {
            Map<String, String> labels = new HashMap<>();
            if(patent.getLatestLabels() != null && patent.getLatestLabels().size()>0) {
                for (TaskOrderLabel label : patent.getLatestLabels()) {
                    String strVal = label.getStrValue();
                    labels.put(label.getLabel().getKey(), com.google.common.base.Strings.isNullOrEmpty(strVal) ? label.getTextValue() : strVal);
                }
            }
            detail.setLabels(labels);
            if (labels.size() > 0) {
                try {
                    detail.setEvaluation(patentEvaluateService.expPatentValues(labels));
                } catch (Exception e) {
                    logger.error("解读价值评估出错", e);
                }
            }
            patent.setLatestLabels(null);
            detail.setPatent(patent);
        }
		return detail;
	}

    @Override
	public String findPatentValue(String an) {
        Patent patent = findPatentByAnWithFullLabels(an);
        if(patent==null) {
            return "0.0";
        }
        return patent.getPatentValue();
    }

	@Override
	public PatentStatusInfo findPatentStatusInfo(String an) {
        PatentStatusInfo patentStatusInfo = new PatentStatusInfo();
        patentStatusInfo.setAn(an);
        patentStatusInfo.setLegalStatusInfos(legalStatusInfoMapper.findByAn(an));
        patentStatusInfo.setPatprsexploitationInfos(patprsexploitationInfoMapper.findByAn(an));
        patentStatusInfo.setPatprspreservationInfos(patprspreservationInfoMapper.findByAn(an));
        patentStatusInfo.setPatprstransferInfos(patprstransferInfoMapper.findByAn(an));
        patentStatusInfo.setScoreInfos(scoreInfoMapper.findByAn(an));
        patentStatusInfo.setPatfeeInfos(patfeeInfoMapper.findByAn(an));
		return patentStatusInfo;
	}

    @Override
    public List<Patent> findPatentValueList(List<String> anList) {
        return patentMapper.findPatentValueList(anList);
    }
}
