package com.cnuip.pmes2.controller.api.v1;

import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.controller.api.response.PatentAllDetail;
import com.cnuip.pmes2.controller.api.response.PatentStatusInfo;
import com.cnuip.pmes2.controller.api.response.PatentValue;
import com.cnuip.pmes2.domain.core.Patent;
import com.cnuip.pmes2.domain.core.TaskOrderLabel;
import com.cnuip.pmes2.domain.tkResultBean.QuoteData;
import com.cnuip.pmes2.domain.tkResultBean.SimilarityInfo;
import com.cnuip.pmes2.service.*;
import com.google.common.base.Strings;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.similarities.Similarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * PatentController
 *
 * @author: xiongwei
 * Date: 2018/3/5 下午5:31
 */
@RestController("patentControllerV1")
@RequestMapping("/api/v1/patent")
public class PatentController {
    private Logger logger = LoggerFactory.getLogger(PatentController.class);

    @Autowired
    private PatentService patentService;
    @Autowired
    private PatentEvaluateService patentEvaluateService;
    @Autowired
    private MongoService mongoService;
    @Autowired
    private CNIPRService cniprService;
    @Autowired
    private TKpatentService tKpatentService;

    /**
     * @param an
     * @return
     */
    @RequestMapping("/detail")
    public ApiResponse<Patent> findPatentDetail(String an, @RequestParam(required = false, defaultValue = "true") boolean map) {
        ApiResponse<Patent> resp = new ApiResponse<>();
        Patent patent = this.patentService.findPatentByAnWithFullLabels(an);
        if (patent != null) {
            if (map) {
                Map<String, Object> labels = new HashMap<>();
                List<TaskOrderLabel> orderLabels = patent.getLatestLabels();
                if (orderLabels != null && orderLabels.size() > 0) {
                    for (TaskOrderLabel label : orderLabels) {
                        labels.put(label.getLabel().getKey(), Strings.isNullOrEmpty(label.getStrValue()) ?
                                label.getTextValue() : label.getStrValue());
                    }
                }
                patent.setLatestLabels(null);
                patent.setLabels(labels);
            }
            resp.setResult(patent);
        } else {
            resp.setCode(-1);
            resp.setMessage("专利不存在");
        }
        return resp;
    }

    /**
     * @param an
     * @return
     */
    @RequestMapping("/value/exp")
    public ApiResponse<Map<String, String>> patentValueExp(String an) {
        ApiResponse<Map<String, String>> resp = new ApiResponse<>();
        Map<String, String> exp = this.patentEvaluateService.evaluatePatentValue(an);
        if (exp != null) {
            resp.setResult(exp);
        } else {
            resp.setCode(-1);
            resp.setMessage("专利不存在或没有进行价值评估!");
        }
        return resp;
    }

    /**
     * 读取专利对应的xml文件内容
     *
     * @param an
     * @param sysId
     * @return
     */
    @RequestMapping(value = "/xml")
    public ApiResponse<String> patentXmlContent(String an, String sysId) {
        ApiResponse<String> resp = new ApiResponse<>();
        String content = null;
        if (!Strings.isNullOrEmpty(sysId)) {
            content = mongoService.readXmlContentBySysId(sysId);
        } else {
            Patent patent = this.patentService.findByAn(an);
            if (patent != null) {
                content = mongoService.readXmlContentBySysId(patent.getSysId());
            }
        }
        resp.setResult(content);
        return resp;
    }

    @RequestMapping("/report")
    public ApiResponse<PatentAllDetail> patentAllDetail(@RequestParam(required = true) String an) {
        ApiResponse<PatentAllDetail> resp = new ApiResponse<>();
        PatentAllDetail detail = patentService.findPatentDetail(an);//专利详情
        resp.setResult(detail);
        return resp;
    }

    @RequestMapping("/similar")
    public ApiResponse<PatentAllDetail> patentSimilar(@RequestParam(required = true) String an) {
        ApiResponse<PatentAllDetail> resp = new ApiResponse<>();
        PatentAllDetail detail = new PatentAllDetail();
        String appNo = an;
        List<SimilarityInfo> similarityInfoList = tKpatentService.getSimilarityInfo(appNo.substring(2, appNo.length()), 1, 10).getResult().getData();
        if (similarityInfoList != null) {
            for (SimilarityInfo similarityInfo : similarityInfoList) {
                similarityInfo.setPa(StringUtils.join(similarityInfo.getAssigneesName(), ","));
            }
        }
        detail.setSimilarList(similarityInfoList);//相似度
        resp.setResult(detail);
        return resp;
    }

    @RequestMapping("/quote")
    public ApiResponse<PatentAllDetail> patentQuote(@RequestParam(required = true) String an) {
        ApiResponse<PatentAllDetail> resp = new ApiResponse<>();
        PatentAllDetail detail = new PatentAllDetail();
        //引用天弓接口
        String appNo = an;
        List<QuoteData> quoteDataList = tKpatentService.getPatentQuoteList(appNo.substring(2, appNo.length())).getResult().getQuoteData();
        if (quoteDataList != null) {
            for (QuoteData quoteData : quoteDataList) {
                quoteData.setPa(StringUtils.join(quoteData.getAssigneesName(), ","));
            }
        }
        detail.setSqryzzlList(quoteDataList);//引用
        resp.setResult(detail);
        return resp;
    }

    @RequestMapping("/value")
    public ApiResponse<String> value(@RequestParam(required = true) String an) {
        ApiResponse<String> resp = new ApiResponse<>();
        resp.setResult(patentService.findPatentValue(an));
        return resp;
    }

    @RequestMapping("/status")
    public ApiResponse<PatentStatusInfo> status(@RequestParam(required = true) String an) {
        ApiResponse<PatentStatusInfo> resp = new ApiResponse<>();
        resp.setResult(patentService.findPatentStatusInfo(an));
        return resp;
    }


    @PostMapping("/patentValueList")
    public ApiResponse<List<PatentValue>> patentValueList(@RequestBody(required = true) String ans) {
        logger.info("ans "+ans);
        ApiResponse<List<PatentValue>> resp = new ApiResponse<List<PatentValue>>();
        List<PatentValue> result = new ArrayList<PatentValue>();

        if(StringUtils.isNotEmpty(ans)){
            String[] ansArray = ans.split(";");
            List<String> dataList = Arrays.asList(ansArray);

            int pointDataLimit = 100;
            List<String> newList = new ArrayList<String>();
            for (int i=0;i<dataList.size();i++){
                //分批次处理
                newList.add(dataList.get(i));
                if(pointDataLimit == newList.size()||i == dataList.size()-1){
                    List<Patent> patentList = patentService.findPatentValueList(newList);
                    if(patentList != null){
                        for(Patent item:patentList){
                            PatentValue patentValue = new PatentValue();
                            patentValue.setAn(item.getAn());
                            patentValue.setValue(item.getPatentValue());
                            result.add(patentValue);
                        }
                    }
                    newList.clear();
                }
            }
        }
        resp.setResult(result);
        return resp;
    }


}
