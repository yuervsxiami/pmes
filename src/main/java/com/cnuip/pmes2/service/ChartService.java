package com.cnuip.pmes2.service;

import com.cnuip.pmes2.domain.statistics.PatentParam;

import java.util.List;
import java.util.Map;

public interface ChartService {
    List<Map> provinces();

    List<Map> colleges(String provinceName);

    List<Map> provinceNumber();

    Map<String, Object> collegePatentList(String provinceName, String collegeName, int pageNum, int pageSize);

    Map<String, Object> provincePatentList(PatentParam param, int pageNum, int pageSize);

    List<Map> collegePatentSort(String type, String legState, int limit);

    List<Map> collegeTypeStat(String name);

    List<Map> collegeLegStat(String name);

    List<Map> collegeStatLastTenYear(String name);

    List<Map> collegeIpc(String name, String collection, int limit);

    List<Map> collegeIpcTopYear(String name);

    List<Map> collegeIpcTopValue(String name);

    List<Map> collegePartner(String name);

    List<Map> collegePin(String name, int limit);

    List<Map> collegePinPartner(String name, String pinSplit);

    List<Map> byQuoteTotal(String name, int limit);

    Map<String, Object> collegePmesValue(String name);

    Map<String, Object> pinPmesValue(String collegeName, String name);

    String getExpertRecommendation(String collegeName, String name);
}
