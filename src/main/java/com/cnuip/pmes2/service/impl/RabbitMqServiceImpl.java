package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.domain.core.EnterpriseRequire;
import com.cnuip.pmes2.domain.el.ElProfessor;
import com.cnuip.pmes2.domain.el.Requirement;
import com.cnuip.pmes2.domain.el.User;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.repository.core.EnterpriseRequireMapper;
import com.cnuip.pmes2.repository.el.ElProfessorRepository;
import com.cnuip.pmes2.service.RabbitMqService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/11/28.
 * Time: 10:15
 */
@Service
public class RabbitMqServiceImpl implements RabbitMqService {

    private static RestTemplate restTemplate;

    private static ObjectMapper objectMapper;

    static {
        restTemplate = new RestTemplate();
        StringHttpMessageConverter m = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        restTemplate.setMessageConverters(Collections.singletonList(m));
        objectMapper = new ObjectMapper();
    }

    @Value("${tg.url}")
    private String tgurl;

    @Autowired
    ElProfessorRepository elProfessorRepository;

    @Autowired
    EnterpriseRequireMapper enterpriseRequireMapper;


    @Override
    public ElProfessor saveProfessor(String userStr) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User user ;
        ElProfessor elProfessor;
        try {
            user = objectMapper.readValue(userStr, User.class);
            elProfessor = new ElProfessor();
            elProfessor.setId(user.getId());
            elProfessor.setCollegeName(user.getOrganizationName());
            elProfessor.setName(user.getRealName());
            StringBuilder url = new StringBuilder();
            final Map<String, Object> params = new HashMap<>();
            url.append(tgurl + "?assignee={assignee}&expert={expert}");
            params.put("assignee", user.getOrganizationName());
            params.put("expert", user.getRealName());
            ResponseEntity<String> exchange = restTemplate.exchange(url.toString(), HttpMethod.GET, addHeader("getExpertRecommendationKeywords"), String.class, params);
            final Map map = objectMapper.readValue(exchange.getBody(), Map.class);
            if (map.get("success").equals(true)) {
                if (map.get("result") != null) {
                    Map result = (Map) map.get("result");
                    if (result.get("keywords") != null && Integer.parseInt(result.get("status").toString()) == 200) {
                        Map keywordResult = (Map) result.get("keywords");
                        List<String> keywords = (ArrayList) keywordResult.get("keywords");
                        List<Double> scores = (ArrayList) keywordResult.get("scores");
                        elProfessor.setScores(StringUtils.join(scores, " "));
                        elProfessor.setKeyWords(StringUtils.join(keywords, " "));
                    }
                }
                //加入
                if (elProfessorRepository.exists(elProfessor.getId())) {
                    elProfessorRepository.delete(elProfessor.getId());
                    elProfessorRepository.save(elProfessor);
                } else {
                    elProfessorRepository.save(elProfessor);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return elProfessor;
    }


    @Override
    public Requirement saveRequirement(String requirementStr) throws Exception {
        ObjectMapper objectMapper=new ObjectMapper();
        Requirement requirement = objectMapper.readValue(requirementStr, Requirement.class);
        EnterpriseRequire enterpriseRequire=new EnterpriseRequire();
        try {
            enterpriseRequire.setOriginCreateTime(requirement.getCreatedTime());
            enterpriseRequire.setOriginId(requirement.getId());
            enterpriseRequire.setSource("cnuip");
            enterpriseRequire.setRequirementType(requirement.getRequirementType());
            enterpriseRequire.setEnterpriseType(requirement.getEnterpriseType());
            enterpriseRequire.setKeywords(requirement.getLabel());
            enterpriseRequire.setEnterpriseId(requirement.getUserId());
            enterpriseRequire.setClassifyId(requirement.getClassifyId());
            enterpriseRequire.setTitle(requirement.getTitle());
            enterpriseRequire.setCompanyName(requirement.getUsername());
            enterpriseRequire.setCode(requirement.getCode());
            enterpriseRequire.setRequirement(requirement.getContent());
            //加入
            enterpriseRequireMapper.save(enterpriseRequire);
        }catch (Exception e){
            e.printStackTrace();
            throw  e;
        }
        return  requirement;
    }


    private HttpEntity<String> addHeader(String apiName) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("API_NAME", apiName);
        requestHeaders.add("APP_ID", "1");
        return new HttpEntity<>(null, requestHeaders);
    }

}
