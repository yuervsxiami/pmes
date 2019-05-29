package com.cnuip.pmes2.service;

import com.cnuip.pmes2.domain.tkResultBean.*;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/6/27.
 * Time: 9:39
 */
public interface TKpatentService {
    //1.	相关词检索接口
    TKKeyWordResult getCorrelationKeyWord(String keywords, Integer topn);
    //2.	内容关键词接口
    TKContentKeyWordResult getContentKeyWord(String text, Integer topn, String pos);
    //3.	专利关键接口
    TKContentKeyWordResult getKeyWordByAppNo(String appNo, Integer topn, String pos);
    //4.	专利相似度总览接口
    TKPatentSimilarityResult getPatentSimilarity(String appNo);
    //5.	专利相似度详情接口
    TKSimilarityInfoResult getSimilarityInfo(String appNo, Integer pageIndex, Integer pageSize);
    //6.	专利引用总览接口
    TKQuoteNumberResult getPatentQuoteNumber(String appNo);
    //7.	专利引用详情接口
    TKPatentQuoteListResult getPatentQuoteList(String appNo);
    //8.	专家标引接口
    TKExpertRecomResult getExpertRecommendation(String assignee, String expert);
    //9.	企业标引数据接口
    TKCompanyRecoResult getCompanyRecommendData(String companyName);
    //10.	企业标引数据地区接口
    TKCompanyRecoResult getCompanyByRegionRecommendData(String companyName, String region);
    //11.	企业IPC统计接口
    TKIPCResult getCompanyStatisticsIPC(String companyName);
    //12.	企业IPC专利详情接口
    TKDetailIPCResult getCompanyDetailIPC(String companyName, String ipc, Integer pageIndex, Integer pageSize);
    //13.	企业合并信息
    TKCompanyOnceNameResult getCompanyOnceName(String companyName);
    //14.	专利权人专利清单接口
    TKCompanyPatentResult getAllCompanyPatentList(String patentHolderName, Integer pageIndex, Integer pageSize);
    //15.	专利权人增量专利清单接口
    TKPatentListChangeResult getCompanyPatentList(String patentHolderName, String startTime);
}
