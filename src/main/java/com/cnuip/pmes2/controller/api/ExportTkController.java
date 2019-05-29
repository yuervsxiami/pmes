package com.cnuip.pmes2.controller.api;

import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.domain.TK;
import com.cnuip.pmes2.domain.tkResultBean.SimilarityInfo;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.repository.core.TKMapper;
import com.cnuip.pmes2.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 专利价值快速评估相关接口
 * Created by wangzhibin on 2018/1/29.
 */
@RestController
@RequestMapping("/api/patent/assessment/tk")
public class ExportTkController extends BussinessLogicExceptionHandler {

    private static ObjectMapper objectMapper;
    private static String tkurl = "https://www.tiikong.com/api/patentInterfaceList/";
    private static String token = "BRzz3J0ld4Ra8mqu";

    static {
        objectMapper = new ObjectMapper();
    }

    @Autowired
    TKMapper tkMapper;

    @Autowired
    private RestOperations restOperations;

    @Autowired
    private TKpatentService tKpatentService;

    @GetMapping(value = "/exporttk")
    public ApiResponse<String> exporttk() throws BussinessLogicException {
        ApiResponse<String> res = new ApiResponse<>();
        List<TK> list = tkMapper.findAll();
        for (TK tk : list) {
            getSimilarTotal(tk);
            getSimilarValue(tk);
            getQuote(tk);
            tkMapper.updateTk(tk);
            System.out.println("======"+tk.getAn()+"执行完毕=====");
        }
        return  res;
    }


    public String doGet(String url, Map<String, Object> params) {
        String resp = null;
        try {
            resp = restOperations.getForObject(url, String.class, params);
        } catch (RestClientException ex) {
            ex.printStackTrace();
        }
        return resp;
    }


    private void getSimilarTotal(TK tk) {
        Map map = new HashMap();
        String appNo = tk.getAn();
        //获取天弓相似度专利数
        StringBuilder url = new StringBuilder();
        final Map<String, Object> params = new HashMap<>();
        url.append(tkurl + "getPatentSimilarity.do?token={token}&appNo={appNo}");
        params.put("token", token);
        params.put("appNo", appNo.substring(2, appNo.length()));
        String resp = doGet(url.toString(), params);
        try {
            map = objectMapper.readValue(resp, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (map != null && map.get("success").equals(true)) {
            if (map.get("result") != null) {
                tk.setSimilarTotal(Integer.parseInt(map.get("result").toString()));
            }
        }
    }

    private void getSimilarValue(TK tk) {
        String appNo = tk.getAn();
        if(tKpatentService.getSimilarityInfo(appNo.substring(2, appNo.length()), 1, null).getResult()!=null) {
            List<SimilarityInfo> similarityInfoList = tKpatentService.getSimilarityInfo(appNo.substring(2, appNo.length()), 1, null).getResult().getData();
            List<BigDecimal> list = new ArrayList();
            BigDecimal bigss = BigDecimal.ZERO;
            if (similarityInfoList != null) {
                for (SimilarityInfo similarityInfo : similarityInfoList) {
                    list.add(new BigDecimal(similarityInfo.getScore()));
                }
                for (BigDecimal big : list) {
                    bigss = bigss.add(big);
                }
                if (similarityInfoList.size() > 0) {
                    tk.setSimilarAvg(ConvertNumber(bigss, similarityInfoList.size(), 2));
                }
            }
        }
    }

    private void getQuote(TK tk) {
        Map map = new HashMap();
        String appNo = tk.getAn();
        //获取天弓相似度专利数
        StringBuilder url = new StringBuilder();
        final Map<String, Object> params = new HashMap<>();
        url.append(tkurl + "getPatentQuoteNumber.do?token={token}&appNo={appNo}");
        params.put("token", token);
        params.put("appNo", appNo.substring(2, appNo.length()));
        String resp = doGet(url.toString(), params);
        try {
            map = objectMapper.readValue(resp, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (map != null && map.get("success").equals(true)) {
            if (map.get("result") != null) {
                Map mapresult=(Map)map.get("result");
                if (mapresult != null) {
                    if (mapresult.get("quoteTotal") != null) {
                        tk.setQuoteTotal(Integer.parseInt(mapresult.get("quoteTotal").toString()));
                    }
                    if (mapresult.get("byQuoteTotal") != null) {
                        tk.setByQuoteTotal(Integer.parseInt(mapresult.get("byQuoteTotal").toString()));
                    }
                }
            }
        }
    }


    public BigDecimal ConvertNumber(BigDecimal bigDecimal, int divnum, int num) {
        double a = bigDecimal.doubleValue();
        a = a / divnum;
        String numString = "0.";
        for (int i = 0; i < num; i++) {
            numString += "0";
        }
        DecimalFormat df = new DecimalFormat(numString);
        return new BigDecimal(df.format(a).toString());
    }

}
