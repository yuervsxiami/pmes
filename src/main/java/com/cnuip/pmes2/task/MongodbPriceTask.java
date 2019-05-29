package com.cnuip.pmes2.task;

import com.cnuip.pmes2.controller.api.request.PatentStartSearchCondition;
import com.cnuip.pmes2.domain.core.Patent;
import com.cnuip.pmes2.domain.inventory.PatentEvaluateInfo;
import com.cnuip.pmes2.repository.core.PatentMapper;
import com.cnuip.pmes2.service.PatentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.stream.IntStream;

@Slf4j
//@Component
public class MongodbPriceTask {
    @Autowired
    private PatentService patentService;

    @Autowired
    private PatentMapper patentMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier(value = "secondMongoTemplate")
    protected MongoTemplate secondMongoTemplate;

    /**
     * 每3个小时同步一次
     */
    @Scheduled(fixedDelay = 10800)
    public void batchSyncMongodb() throws Exception {
        final int pageSize = 1000;
        //获取mongdb最大时间  max(updateTime)
        Date lastCreateTime = getLastCreateTime();
        if (lastCreateTime == null)
            return;
        log.info("e_patentInfo mongodb LastUpdateTime " + lastCreateTime);
        //获取要更新的PATENT
        PatentStartSearchCondition condition = new PatentStartSearchCondition();
        condition.setLastCreateTime(lastCreateTime);
        Page<Patent> page = (Page<Patent>) patentMapper.searchOrderByCreateTimeAsc(condition, 1, pageSize);
        PageInfo<Patent> pageInfo = page.toPageInfo();
        //插入or更新mongdb数据
        for (int i = 1; i <= pageInfo.getPages(); i++) {
            Page<Patent> patentPage = (Page<Patent>) patentMapper.searchOrderByCreateTimeAsc(condition, i, pageSize);
            for (Patent patent : patentPage) {
                String an = patent.getAn();
                String pnm = patent.getPnm();
                String signString = pnm + "njlg" + "cpig2xdim60hnryk" +
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                String url = "http://e.cnipr.com/services/rs/score/loadPriceInfo/njlg";
                String result = restTemplate.getForObject(url + "/{an}/{sign}", String.class, an, DigestUtils.md5DigestAsHex(signString.getBytes()).toUpperCase());
                ObjectMapper objectMapper = new ObjectMapper();
                Map map = objectMapper.readValue(result, Map.class);
                map.put("an", an);
                map.put("pnm", pnm);
                secondMongoTemplate.save(map, "patentPrice");
            }
        }
    }

    private Date getLastCreateTime() throws ParseException {
        String dateStr = "2011-10-01 10:20:16";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date maxDate = dateFormat.parse(dateStr);
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC, "create_time"));
        Map price = secondMongoTemplate.findOne(query, Map.class, "patentPrice");
        if (price != null) {
            return dateFormat.parse(price.get("create_time").toString());
        }
        return maxDate;
    }
}
