package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.controller.api.request.PatentInfo;
import com.cnuip.pmes2.controller.api.request.QutationBean;
import com.cnuip.pmes2.controller.api.request.SimilarityBean;
import com.cnuip.pmes2.service.CNIPRService;
import com.cnuip.pmes2.util.StringToObjectUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import java.util.List;
import java.util.Map;

/**
 * CNIPRServiceImpl
 *
 * @author: xiongwei
 * Date: 2018/1/22 下午5:42
 */
@Service
public class CNIPRServiceImpl implements CNIPRService {

    @Autowired
    private RestOperations restOperations;

	private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String doGet(String url, Map<String, Object> params) {
        String resp = null;
        try {
            resp = restOperations.getForObject(url, String.class, params);
        } catch (RestClientException ex) {
            ex.printStackTrace();
        }
        return resp;
    }

    @Override
    public String doPost(String url, MultiValueMap<String, Object> params) {
        String resp = null;
        try {
            resp = restOperations.postForObject(url, params, String.class);
        } catch (RestClientException ex) {
            ex.printStackTrace();
        }
        return resp;
    }

	@Override
	public int getReferNum(String an) {
        final String url = "/cit";
        final MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("pns", an);
        String resp = doPost(url, params);
		QutationBean qutation = StringToObjectUtil.toQutation(resp);
		if(!"1".equals(qutation.getReturncode())) {
			return 4;
		}
		int count = 0;
		if(qutation!=null) {
			if(qutation.getScyyzzlList()!=null) {
				count += qutation.getScyyzzlList().size();
			}
			if(qutation.getSqryzzlList()!=null) {
				count += qutation.getSqryzzlList().size();
			}
		}
		return count;
	}

	@Override
	public List<Map<String,String>> getSqryzzlList(String an) {
		try{
			final String url = "/cit";
			final MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
			params.add("pns", an);
			String resp = doPost(url, params);
			QutationBean qutation = StringToObjectUtil.toQutation(resp);
			if(qutation!=null) {
				return qutation.getSqryzzlList();
			}
		} catch (Exception e) {
			logger.error("调用印证接口出错",e);
			return null;
		}
		return null;
	}

	@Override
	public int getNonReferNum(String an) {
        final String url = "/cit";
        final MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("pns", an);
        String resp = doPost(url, params);
		QutationBean qutation = StringToObjectUtil.toQutation(resp);
		if(!"1".equals(qutation.getReturncode())) {
			return 4;
		}
		int count = 0;
		if(qutation!=null) {
			if(qutation.getScyyzfzlList()!=null) {
				count += qutation.getScyyzfzlList().size();
			}
		}
		return count;
	}

	@Override
	public int getReferedNum(String an) {
        final String url = "/cit";
        final MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("pns", an);
        String resp = doPost(url, params);
		QutationBean qutation = StringToObjectUtil.toQutation(resp);
		if(!"1".equals(qutation.getReturncode())) {
			return 4;
		}
		int count = 0;
		if(qutation!=null) {
			if(qutation.getSqrbyzzlList()!=null) {
				count += qutation.getSqrbyzzlList().size();
			}
			if(qutation.getScybyzzlList()!=null) {
				count += qutation.getScybyzzlList().size();
			}
		}
		return count;
	}

	@Override
	public int getSimilarNum(String an) {
        final String url = "/similar";
        final MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("strSources", "fmzl,syxx,wgzl,fmsq");
        params.add("similarWhere", "pnm=('"+ an +"')");
        params.add("startIndex", "0");
        params.add("endIndex", "0");
        String resp = doPost(url, params);
        SimilarityBean bean = StringToObjectUtil.toSimlarity(resp);
        if(bean.getReturncode() == 1) {
        	return bean.getRecordCount();
        } else {
        	return 0;
        }
	}

	@Override
	public List<PatentInfo> getSimilarList(String an) {
		try{
			final String url = "/similar";
			final MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
			params.add("strSources", "fmzl,syxx,wgzl,fmsq");
			params.add("similarWhere", "pnm=('"+ an +"')");
			params.add("startIndex", "0");
			params.add("endIndex", "10");
			String resp = doPost(url, params);
			SimilarityBean bean = StringToObjectUtil.toSimlarity(resp);
			if(bean!=null) {
				return bean.getPatentInfoList();
			}
		} catch (Exception e) {
			logger.error("调用印证接口出错",e);
			return null;
		}
		return null;
	}


}
