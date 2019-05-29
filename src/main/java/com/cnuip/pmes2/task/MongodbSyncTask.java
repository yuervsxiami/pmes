package com.cnuip.pmes2.task;

import com.cnuip.pmes2.controller.api.request.PatentStartSearchCondition;
import com.cnuip.pmes2.domain.core.Patent;
import com.cnuip.pmes2.domain.inventory.PatentEvaluateInfo;
import com.cnuip.pmes2.repository.core.PatentMapper;
import com.cnuip.pmes2.service.PatentService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.bson.types.Decimal128;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * 从mysql 或者搜索引擎同步数据至mongodb
 */
//@Component
public class MongodbSyncTask{

    private Logger logger = LoggerFactory.getLogger(MongodbSyncTask.class);

    @Autowired
    private PatentService patentService;

    @Autowired
    private PatentMapper patentMapper;

    @Autowired
    @Qualifier(value = "secondMongoTemplate")
    protected MongoTemplate secondMongoTemplate;
    /**
     *每3个小时同步一次
     */
    @Scheduled(fixedDelay = 10800)
    public void batchSyncMongodb() throws Exception{
        final int pageSize = 1000;
        //获取mongdb最大时间  max(updateTime)
        Date lastUpdateTime = getLastUpdateTime();
        if(lastUpdateTime == null)
            return;
        logger.info("e_patentInfo mongodb LastUpdateTime "+lastUpdateTime);
        //获取要更新的PATENT
        PatentStartSearchCondition condition = new PatentStartSearchCondition();
        condition.setLastUpdateTime(lastUpdateTime);
        Page<Patent> page = (Page<Patent>)patentMapper.searchOrderByUpdateTimeAsc(condition,1,pageSize);
        PageInfo<Patent> pageInfo = page.toPageInfo();
        //插入or更新mongdb数据
        //获取搜索引擎数据
        Map<String,String> feildMap = getField(new PatentEvaluateInfo());
        IntStream.range(1, pageInfo.getPages()+1).forEach((pageNum)->{
            Page<Patent> patentPage = (Page<Patent>)patentMapper.searchOrderByUpdateTimeAsc(condition,pageNum,pageSize);
            patentPage.forEach((item)->{
                //外观专利不入库
                if(!item.getSectionName().equals("WGZL")){
                    logger.info(item.getAn()+" "+item.getLastLegalStatus()+" "+item.getSectionName()+" "+item.getUpdateTime());
                    //组装数据
                    PatentEvaluateInfo patentEvaluateInfo = buildPatent(item,feildMap);
                    //更新数据
                    if(patentEvaluateInfo!=null){
                        updatePatentEvaluateInfo(patentEvaluateInfo,"e_patentInfo");
                    }
                }
            });
        });
    }

    private Date getLastUpdateTime() throws ParseException {
        String dateStr = "2011-10-1 10:20:16";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date maxDate = dateFormat.parse(dateStr);
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC, "updateTime"));
        PatentEvaluateInfo patentEvaluateInfo = secondMongoTemplate.findOne(query,PatentEvaluateInfo.class);
        if(patentEvaluateInfo != null ){
            return patentEvaluateInfo.getUpdateTime();
        }
        return maxDate;
    }

    private void updatePatentEvaluateInfo(PatentEvaluateInfo patentEvaluateInfo,String collectionName){
        Query query = new Query();
        Criteria criteria = Criteria.where("an").is(patentEvaluateInfo.getAn());
        query.addCriteria(criteria);
        secondMongoTemplate.remove(query,collectionName);
        secondMongoTemplate.insert(patentEvaluateInfo,collectionName);
    }

    private PatentEvaluateInfo buildPatent(Patent item, Map<String,String> fieldMap){
        Patent patent = this.patentService.findPatentByAnWithFullLabels(item.getAn());
        PatentEvaluateInfo patentEvaluateInfo = new PatentEvaluateInfo();
        patentEvaluateInfo.setAn(item.getAn());
        patentEvaluateInfo.setTi(item.getTi());
        patentEvaluateInfo.setSectionName(item.getSectionName());
        patentEvaluateInfo.setLastLegalStatus(item.getLastLegalStatus());
        patentEvaluateInfo.setUpdateTime(item.getUpdateTime());
        patentEvaluateInfo.setCreateTime(item.getCreateTime());
        if(patent.getLatestLabels() != null){
            patent.getLatestLabels().forEach((taskOrderLabel)->{
                if(taskOrderLabel.getLabel()!=null){
                    try{
                        String key = taskOrderLabel.getLabel().getKey();
                        if(fieldMap.containsKey(key)){
                            Field field = patentEvaluateInfo.getClass().getDeclaredField(key);
                            field.setAccessible(true);
                            if( taskOrderLabel.getLabel().getValueType() == 5){//double 类型
                                field.set(patentEvaluateInfo, new Decimal128(new BigDecimal(taskOrderLabel.getStrValue())));
                            }
                            //System.out.println("key = "+key +" value "+ taskOrderLabel.getStrValue());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            //System.out.println(patentEvaluateInfo);
            return patentEvaluateInfo;
        }
        return null;
    }

    private Map<String,String> getField(PatentEvaluateInfo patentEvaluateInfo){
        HashMap<String,String> feildMap = new HashMap<String,String>();
        Field[] fields = patentEvaluateInfo.getClass().getDeclaredFields();
        for(int i=0;i<fields.length;i++){
            feildMap.put(fields[i].getName(),fields[i].getName());
        }
        return feildMap;
    }
}