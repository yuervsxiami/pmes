package com.cnuip.pmes2.service;

import com.cnuip.pmes2.controller.api.request.PatentInfo;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

/**
 * CNIPRService
 *
 * @author: xiongwei
 * Date: 2018/1/22 下午5:08
 */
public interface CNIPRService {

    String doGet(String url, Map<String, Object> params);

    String doPost(String url, MultiValueMap<String, Object> params);

    int getReferNum(String an);
    
    int getSimilarNum(String an);

	List<Map<String,String>> getSqryzzlList(String an);

	int getNonReferNum(String an);

	int getReferedNum(String an);

	List<PatentInfo> getSimilarList(String an);

}
