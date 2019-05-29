package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.config.MongoDBConfig;
import com.cnuip.pmes2.domain.inventory.PatentInfoAn;
import com.cnuip.pmes2.service.MongoDetailService;
import com.cnuip.pmes2.service.MongoService;
import com.mongodb.gridfs.GridFSDBFile;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * MongoServiceImpl
 *
 * @author: xiongwei
 * Date: 2018/3/16 上午10:32
 */
@Service
public class MongoDetailServiceImpl implements MongoDetailService {

    private final Logger logger = LoggerFactory.getLogger(MongoDetailServiceImpl.class);

    @Autowired
    @Qualifier(value = "secondMongoTemplate")
    protected MongoTemplate secondMongoTemplate;

    @Override
    public List<PatentInfoAn> findPatentInfoAn(PatentInfoAn patentInfoAn) {
        Query query = new Query();
        Criteria criteria=null;
        if(patentInfoAn!=null){
            if(StringUtils.isNotBlank(patentInfoAn.getAn())){
                criteria = Criteria.where("_id").regex(".*?" +patentInfoAn.getAn()+ ".*");
            }
//            if(patentInfoAn.getSectionNameList()!=null && patentInfoAn.getSectionNameList().size()>0){
//                criteria=Criteria.where("value.sectionName").in(patentInfoAn.getSectionNameList());
//            }
            if (StringUtils.isNotBlank(patentInfoAn.getValue().getPa()) && patentInfoAn.getType()==1) {
                criteria = Criteria.where("value.pa").is(patentInfoAn.getValue().getPa());
            }else if(StringUtils.isNotBlank(patentInfoAn.getValue().getPa())){
                criteria = Criteria.where("value.pa").regex(".*?" +patentInfoAn.getValue().getPa()+ ".*");
            }
//            if(patentInfoAn.getLastLegalStatusList()!=null && patentInfoAn.getLastLegalStatusList().size()>0){
//                criteria=Criteria.where("value.lastLegalStatus").in(patentInfoAn.getLastLegalStatusList());
//            }
            if(StringUtils.isNotBlank(patentInfoAn.getValue().getTi())){
                criteria = Criteria.where("value.ti").regex(".*?" +patentInfoAn.getValue().getTi()+ ".*");
            }
            if(StringUtils.isNotBlank(patentInfoAn.getValue().getPin())){
                criteria = Criteria.where("value.pin").regex(".*?" +patentInfoAn.getValue().getPin()+ ".*");
            }
            query.addCriteria(criteria);
        }
        List<PatentInfoAn> patentInfoList=secondMongoTemplate.find(query, PatentInfoAn.class);
        return patentInfoList;
    }

    @Override
    public List<PatentInfoAn> findPatentInfoAnList(List ans) {
        Query query = new Query();
        if (ans!=null && ans.size()>0) {
            Criteria criteria = Criteria.where("_id").in(ans);
            query.addCriteria(criteria);
        }
        List<PatentInfoAn> patentInfoList=secondMongoTemplate.find(query, PatentInfoAn.class);
        return patentInfoList;
    }
}
