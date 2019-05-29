package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.controller.api.request.EnterpriseRequireSearchCondition;

import com.cnuip.pmes2.domain.core.ElProfessorTemp;
import com.cnuip.pmes2.domain.core.EnterpriseRequire;
import com.cnuip.pmes2.repository.core.EnterpriseRequireMapper;
import com.cnuip.pmes2.service.EnterpriseRequireService;
import com.cnuip.pmes2.service.MongoService;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/10/12.
 * Time: 17:02
 */
@Service
public class EnterpriseRequireServiceImpl implements EnterpriseRequireService{

    @Autowired
    EnterpriseRequireMapper enterpriseRequireMapper;

    @Autowired
    @Qualifier(value = "secondMongoTemplate")
    protected MongoTemplate mongoTemplate;

    @Autowired
    private MongoService mongoService;

    @Override
    public List<EnterpriseRequire> selectEnterpriseRequirement(EnterpriseRequireSearchCondition enterpriseRequireSearchCondition) {
        return enterpriseRequireMapper.selectEnterpriseRequireList(enterpriseRequireSearchCondition,enterpriseRequireSearchCondition.getPageNum(),enterpriseRequireSearchCondition.getPageSize());
    }

    @Override
    public EnterpriseRequire findById(Long erid) {
        return enterpriseRequireMapper.findEnterpriseRequireById(erid);
    }

    @Override
    public String findXmlDetail(String collegeName, String name) {
        List<Map> detail_collegePatent_pin = getDetailCollegePatentPin(collegeName, name);
        StringBuffer stringBuffer=new StringBuffer();
        detail_collegePatent_pin.forEach(map -> {
            stringBuffer.append(getStringFromXml(map.get("sysid").toString()));
        });
        return stringBuffer.toString();
    }

    @Override
    public List<Map> getDetailCollegePatentPin(String collegeName, String name) {
        if (StringUtils.isEmpty(collegeName) || StringUtils.isEmpty(name))
            throw new IllegalArgumentException("college name invalid");
        Query query = new Query();
        query.addCriteria(Criteria.where("pa").regex(".*?" +collegeName+ ".*"));
        query.addCriteria(Criteria.where("pinSplit").is(name));
        List<Map> detail_collegePatent_pin = mongoTemplate.find(query, Map.class, "detail_collegePatent_pin");
        detail_collegePatent_pin.forEach(map -> {
            Object object =  map.get("an");
            Query query1 = new Query();
            query1.addCriteria(Criteria.where("_id").is(object));
            Map mr_patentAn_detail = mongoTemplate.findOne(query1, Map.class, "mr_patentAn_detail");
            map.put("sysid",((Map)mr_patentAn_detail.get("value")).get("sysid"));
        });
        return detail_collegePatent_pin;
    }


    private String getStringFromXml(String sysid) {
        String xmlContent = this.mongoService.readXmlContentBySysId(sysid);
        WebClient webClient = new WebClient();
        HtmlPage page=null;
        try {
            URL url = new URL("http://www.example.com");
            page = HTMLParser.parseHtml(new StringWebResponse(xmlContent, url), webClient.getCurrentWindow());
        }catch (Exception e){
            e.printStackTrace();
        }
       return page.asText();
    }

    @Override
    public void saveKeyWords(ElProfessorTemp elProfessorTemp) {
        mongoTemplate.save(elProfessorTemp);
    }
}
