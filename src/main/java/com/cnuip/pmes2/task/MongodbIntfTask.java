package com.cnuip.pmes2.task;

import com.cnuip.pmes2.controller.api.request.PatentStartSearchCondition;
import com.cnuip.pmes2.domain.core.Patent;
import com.cnuip.pmes2.domain.inventory.PatentIntfInfo;
import com.cnuip.pmes2.domain.tkResultBean.SimilarityInfo;
import com.cnuip.pmes2.domain.tkResultBean.TKQuoteNumberResult;
import com.cnuip.pmes2.domain.tkResultBean.TKSimilarityInfoResult;
import com.cnuip.pmes2.repository.core.PatentMapper;
import com.cnuip.pmes2.service.TKpatentService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/9/18.
 * Time: 11:06
 */

public class MongodbIntfTask {
    private Logger logger = LoggerFactory.getLogger(MongodbIntfTask.class);

    @Autowired
    @Qualifier(value = "secondMongoTemplate")
    protected MongoTemplate secondMongoTemplate;

    @Autowired
    private TKpatentService tKpatentService;

    @Autowired
    private PatentMapper patentMapper;



    public void batchSyncMongodbIntf() throws Exception{
        ExecutorService service = Executors.newFixedThreadPool(5);

        final int pageSize = 1000;
        //获取mongdb最大时间  max(updateTime)
        Date lastCreateTime = getLastCreateTime();
        if(lastCreateTime == null)
            return;
        logger.info("PatentIntfInfo mongodb lastCreateTime "+lastCreateTime);
        PatentStartSearchCondition condition = new PatentStartSearchCondition();
        condition.setLastCreateTime(lastCreateTime);
        Page<Patent> page = (Page<Patent>)patentMapper.searchOrderByCreateTimeAsc(condition,1,pageSize);
        PageInfo<Patent> pageInfo = page.toPageInfo();
        IntStream.range(1, pageInfo.getPages()).forEach((pageNum)->{
            Page<Patent> patentPage = (Page<Patent>)patentMapper.searchOrderByCreateTimeAsc(condition,pageNum,pageSize);
            patentPage.forEach((item)->{
                service.execute(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            logger.info(item.getAn()+" begin");
                            saveIntfToMongoDb(item);
                            logger.info(item.getAn()+" end");
                        }catch (Exception e){
                            e.printStackTrace();
                           logger.error(e.getMessage());
                        }
                    }
                });
            });
        });
        // 关闭启动线程
        service.shutdown();
        // 等待子线程结束，再继续执行下面的代码
        service.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
    }

    private void saveIntfToMongoDb(Patent patent) {
        secondMongoTemplate.save(builderPatentIntfInfo(patent));
    }


    private PatentIntfInfo builderPatentIntfInfo(Patent patent){
        PatentIntfInfo patentIntfInfo=new PatentIntfInfo();
        patentIntfInfo.setAn(patent.getAn());
        patentIntfInfo.setCreateTime(patent.getCreateTime());
        try{
            TKQuoteNumberResult patentQuoteNumber = tKpatentService.getPatentQuoteNumber(patent.getAn());
            if(patentQuoteNumber!=null){
                patentIntfInfo.setQuoteTotal(patentQuoteNumber.getResult().getQuoteTotal()!=null?patentQuoteNumber.getResult().getQuoteTotal():0);
                patentIntfInfo.setByQuoteTotal(patentQuoteNumber.getResult().getByQuoteTotal()!=null?patentQuoteNumber.getResult().getByQuoteTotal():0);
            }
            TKSimilarityInfoResult similarityInfo = tKpatentService.getSimilarityInfo(patent.getAn(), 1, 20);
            if(similarityInfo!=null){
                patentIntfInfo.setTotal(similarityInfo.getResult().getTotal()!=null?similarityInfo.getResult().getTotal():0);
                Integer totalScore=0;
                if(similarityInfo.getResult().getData().size()>0) {
                    for (SimilarityInfo s : similarityInfo.getResult().getData()) {
                        totalScore = totalScore + s.getScore();
                    }
                    double avg = totalScore / similarityInfo.getResult().getData().size();
                    BigDecimal b = new BigDecimal(avg);
                    double avgScore = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    patentIntfInfo.setAvgScore(avgScore);
                }
            }
        }catch (Exception e){
             e.printStackTrace();
             logger.error(e.getMessage());
        }
        return patentIntfInfo;
    }

    private Date getLastCreateTime() throws ParseException {
        String dateStr = "1979-01-01 10:20:16";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date maxDate = dateFormat.parse(dateStr);
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC, "createTime"));
        PatentIntfInfo PatentIntfInfo = secondMongoTemplate.findOne(query,PatentIntfInfo.class);
        if(PatentIntfInfo != null ){
            return PatentIntfInfo.getCreateTime();
        }
        return maxDate;
    }
}
