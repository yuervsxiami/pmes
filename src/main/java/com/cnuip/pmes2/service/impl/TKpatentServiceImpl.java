package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.domain.tkResultBean.*;
import com.cnuip.pmes2.service.TKpatentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.cnuip.pmes2.util.StringToObjectUtil.*;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/6/27.
 * Time: 10:17
 */
@Service
public class TKpatentServiceImpl implements TKpatentService {
    private static RestTemplate restTemplate;

    private static ObjectMapper objectMapper;

    static {
        restTemplate = new RestTemplate();
        StringHttpMessageConverter m = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        restTemplate.setMessageConverters(Collections.singletonList(m));
        objectMapper = new ObjectMapper();
    }

    public static RestTemplate restTemplate() {
        return restTemplate;
    }


    @Value("${tg.url}")
    private String tgurl;

    @Value("${tg.appid}")
    private String appid;

    private Logger logger = LoggerFactory.getLogger(getClass());


    private  HttpEntity<String> addHeader(String apiName) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("API_NAME", apiName);
        requestHeaders.add("APP_ID", appid);
        return new HttpEntity<>(null, requestHeaders);
    }

    @Override
    public TKKeyWordResult getCorrelationKeyWord(String keywords, Integer topn) {
        StringBuilder url = new StringBuilder();
        url.append(tgurl + "?keywords={keywords}");
          final Map<String, Object> params = new HashMap<>();
        if (topn != null) {
            url.append(" &topn={topn}");
            params.put("topn", topn);
        }
        params.put("keywords", keywords);
        ResponseEntity<String> exchange = restTemplate.exchange(url.toString(), HttpMethod.GET, addHeader("getCorrelationKeyWord"), String.class, params);
        TKKeyWordResult tkResult = toTKResult(exchange.getBody());
        return tkResult;
    }

    @Override
    public TKContentKeyWordResult getContentKeyWord(String text, Integer topn, String pos) {
        StringBuilder url = new StringBuilder();
        url.append(tgurl + "?text={text}");
        final Map<String, Object> params = new HashMap<>();
        if (topn != null) {
            url.append(" &topn={topn}");
            params.put("topn", topn);
        }
        if (pos != null) {
            url.append(" &pos={pos}");
            params.put("pos", pos);
        }
        params.put("text", text);
        ResponseEntity<String> exchange = restTemplate.exchange(url.toString(), HttpMethod.GET, addHeader("getContentKeyWord"), String.class, params);
        TKContentKeyWordResult tkResult = toTKContentKeyWordResult(exchange.getBody());
        return tkResult;
    }

    @Override
    public TKContentKeyWordResult getKeyWordByAppNo(String appNo, Integer topn, String pos) {
        StringBuilder url = new StringBuilder();
        url.append(tgurl + "?appNo={appNo}");
        final Map<String, Object> params = new HashMap<>();
        if (topn != null) {
            params.put("topn", topn);
        }
        if (pos != null) {
            params.put("pos", pos);
        }
        params.put("appNo", appNo);
        ResponseEntity<String> exchange = restTemplate.exchange(url.toString(), HttpMethod.GET, addHeader("getKeyWordByAppNo"), String.class, params);
        TKContentKeyWordResult tkResult = toTKContentKeyWordResult(exchange.getBody());
        return tkResult;

    }

    @Override
    public TKPatentSimilarityResult getPatentSimilarity(String appNo) {
        final Map<String, Object> params = new HashMap<>();
        StringBuilder url = new StringBuilder();
        url.append(tgurl + "?appNo={appNo}");
        params.put("appNo", appNo);
        ResponseEntity<String> exchange = restTemplate.exchange(url.toString(), HttpMethod.GET, addHeader("getPatentSimilarity"), String.class, params);
        TKPatentSimilarityResult tkResult = toTKPatentSimilarityResult(exchange.getBody());
        return tkResult;
    }

    /**
     * pageIndex目前是必填的，天弓后期会做成默认1
     * @param appNo
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public TKSimilarityInfoResult getSimilarityInfo(String appNo, Integer pageIndex, Integer pageSize) {
        StringBuilder url = new StringBuilder();
        url.append(tgurl + "?appNo={appNo}&pageIndex={pageIndex}");
        final Map<String, Object> params = new HashMap<>();
        params.put("pageIndex", pageIndex);
        if (pageSize != null) {
            url.append(" &pageSize={pageSize}");
            params.put("pageSize", pageSize);
        }
        params.put("appNo", appNo);
        ResponseEntity<String> exchange = restTemplate.exchange(url.toString(), HttpMethod.GET, addHeader("getSimilarityInfo"), String.class, params);
        TKSimilarityInfoResult tkResult = toTKSimilarityInfoResult(exchange.getBody());
        return tkResult;
    }

    @Override
    public TKQuoteNumberResult getPatentQuoteNumber(String appNo) {
        StringBuilder url = new StringBuilder();
        final Map<String, Object> params = new HashMap<>();
        url.append(tgurl + "?appNo={appNo}");
        params.put("appNo", appNo);
        ResponseEntity<String> exchange = restTemplate.exchange(url.toString(), HttpMethod.GET, addHeader("getPatentQuoteNumber"), String.class, params);
        TKQuoteNumberResult tkResult = toTKQuoteNumberResult(exchange.getBody());
        return tkResult;
    }

    @Override
    public TKPatentQuoteListResult getPatentQuoteList(String appNo) {
        StringBuilder url = new StringBuilder();
        final Map<String, Object> params = new HashMap<>();
        url.append(tgurl + "?appNo={appNo}");
        params.put("appNo", appNo);
        ResponseEntity<String> exchange = restTemplate.exchange(url.toString(), HttpMethod.GET, addHeader("getPatentQuoteList"), String.class, params);
        TKPatentQuoteListResult tkResult = toTKPatentQuoteListResult(exchange.getBody());
        return tkResult;
    }

    @Override
    public TKExpertRecomResult getExpertRecommendation(String assignee, String expert) {
        StringBuilder url = new StringBuilder();
        final Map<String, Object> params = new HashMap<>();
        url.append(tgurl + "?assignee={assignee}&expert={expert}");
        params.put("assignee", assignee);
        params.put("expert",expert);
        ResponseEntity<String> exchange = restTemplate.exchange(url.toString(), HttpMethod.GET, addHeader("getExpertRecommendation"), String.class, params);
        TKExpertRecomResult tkResult=new TKExpertRecomResult();
       try {
           tkResult = toTKExpertRecomResult(exchange.getBody());
       }catch (Exception e){
           e.printStackTrace();
           return null;
       }
        return tkResult;
    }

    @Override
    public TKCompanyRecoResult getCompanyRecommendData(String companyName) {
        StringBuilder url = new StringBuilder();
        final Map<String, Object> params = new HashMap<>();
        url.append(tgurl + "?companyName={companyName}");
        params.put("companyName", companyName);
        ResponseEntity<String> exchange = restTemplate.exchange(url.toString(), HttpMethod.GET, addHeader("getCompanyRecommendData"), String.class, params);
        TKCompanyRecoResult tkResult = toTKCompanyRecoResult(exchange.getBody());
        return tkResult;
    }

    @Override
    public TKCompanyRecoResult getCompanyByRegionRecommendData(String companyName, String region) {
        StringBuilder url = new StringBuilder();
        final Map<String, Object> params = new HashMap<>();
        url.append(tgurl + "?companyName={companyName}&region={region}");
        params.put("companyName", companyName);
        params.put("region",region);
        ResponseEntity<String> exchange = restTemplate.exchange(url.toString(), HttpMethod.GET, addHeader("getCompanyByRegionRecommendData"), String.class, params);
        TKCompanyRecoResult tkResult = toTKCompanyRecoResult(exchange.getBody());
        return tkResult;
    }

    @Override
    public TKIPCResult getCompanyStatisticsIPC(String companyName) {
        StringBuilder url = new StringBuilder();
        final Map<String, Object> params = new HashMap<>();
        params.put("companyName", companyName);
        url.append(tgurl + "?companyName={companyName}");
        ResponseEntity<String> exchange = restTemplate.exchange(url.toString(), HttpMethod.GET, addHeader("getCompanyStatisticsIPC"), String.class, params);
        TKIPCResult tkResult = toTKIPCResult(exchange.getBody());
        return tkResult;
    }

    @Override
    public TKDetailIPCResult getCompanyDetailIPC(String companyName, String ipc, Integer pageIndex, Integer pageSize) {
        StringBuilder url = new StringBuilder();
        final Map<String, Object> params = new HashMap<>();
        url.append(tgurl + "?companyName={companyName}&ipc={ipc}");
        if (pageIndex != null) {
            url.append("&pageIndex={pageIndex}");
            params.put("pageIndex", pageIndex);
        }
        if (pageSize != null) {
            url.append("&pageSize={pageSize}");
            params.put("pageSize", pageSize);
        }
        params.put("companyName", companyName);
        params.put("ipc",ipc);
        ResponseEntity<String> exchange = restTemplate.exchange(url.toString(), HttpMethod.GET, addHeader("getCompanyDetailIPC"), String.class, params);
        TKDetailIPCResult tkResult = toTKDetailIPCResult(exchange.getBody());
        return tkResult;
    }

    @Override
    public TKCompanyOnceNameResult getCompanyOnceName(String companyName) {
        StringBuilder url = new StringBuilder();
        final Map<String, Object> params = new HashMap<>();
        params.put("companyName", companyName);
        url.append(tgurl + "?companyName={companyName}");
        ResponseEntity<String> exchange = restTemplate.exchange(url.toString(), HttpMethod.GET, addHeader("getCompanyOnceName"), String.class, params);
        TKCompanyOnceNameResult tkResult = toTKCompanyOnceNameResult(exchange.getBody());
        return tkResult;
    }

    @Override
    public TKCompanyPatentResult getAllCompanyPatentList(String patentHolderName, Integer pageIndex, Integer pageSize) {
        StringBuilder url = new StringBuilder();
        final Map<String, Object> params = new HashMap<>();
        url.append(tgurl + "?patentHolderName={patentHolderName}");
        if (pageIndex != null) {
            url.append(" &pageIndex={pageIndex}");
            params.put("pageIndex", pageIndex);
        }
        if (pageSize != null) {
            url.append(" &pageSize={pageSize}");
            params.put("pageSize", pageSize);
        }
        params.put("patentHolderName", patentHolderName);
        ResponseEntity<String> exchange = restTemplate.exchange(url.toString(), HttpMethod.GET, addHeader("getAllCompanyPatentList"), String.class, params);
        TKCompanyPatentResult tkResult = toTKCompanyPatentResult(exchange.getBody());
        return tkResult;
    }

    @Override
    public TKPatentListChangeResult getCompanyPatentList(String patentHolderName, String startTime) {
        StringBuilder url = new StringBuilder();
        final Map<String, Object> params = new HashMap<>();
        url.append(tgurl + "?patentHolderName={patentHolderName}&startTime={startTime}");
        params.put("patentHolderName", patentHolderName);
        params.put("startTime",startTime);
        ResponseEntity<String> exchange = restTemplate.exchange(url.toString(), HttpMethod.GET, addHeader("getCompanyPatentList"), String.class, params);
        TKPatentListChangeResult tkResult = toTKPatentListChangeResult(exchange.getBody());
        return tkResult;
    }
}
