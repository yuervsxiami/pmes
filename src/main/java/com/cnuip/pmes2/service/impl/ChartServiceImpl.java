package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.domain.statistics.PatentParam;
import com.cnuip.pmes2.service.ChartService;
import org.bson.types.Decimal128;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ChartServiceImpl implements ChartService {
    @Autowired
    @Qualifier(value = "secondMongoTemplate")
    protected MongoTemplate mongoTemplate;
    @Autowired
    private RestTemplate restTemplate;

    @Value("${tg.url}")
    private String tgurl;

    @Value("${tg.appid}")
    private String appid;

    @Override
    public List<Map> provinces() {
        Query query = new Query();
        query.with(new Sort(new Sort.Order(Sort.Direction.ASC, "_id")));
        return mongoTemplate.find(query, Map.class, "stat_college_province");
    }

    @Override
    public List<Map> colleges(String provinceName) {
        if (StringUtils.isEmpty(provinceName))
            throw new IllegalArgumentException("college name invalid");
        Query query = new Query();
        query.addCriteria(Criteria.where("province").is(provinceName));
        return mongoTemplate.find(query, Map.class, "collegeDetail").stream().map(map -> {
            Map<Object, Object> result = new HashMap<>();
            result.put("name", map.get("name"));
            return result;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Map> provinceNumber() {
        List<Map> college_province = mongoTemplate.findAll(Map.class, "stat_college_province");
        List<Map> stat_patent_province = mongoTemplate.findAll(Map.class, "stat_patent_province");
        Map patentMap = stat_patent_province.stream().collect(Collectors.toMap(o -> o.get("_id"), o -> o.get("count")));
        return college_province.stream().map(map -> {
            String id = map.get("_id").toString();
            HashMap<String, Object> m = new HashMap<>();
            m.put("_id", id);
            m.put("collegeCount", map.get("count"));
            m.put("patentCount", patentMap.get(id));
            return m;
        }).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> collegePatentList(String provinceName, String collegeName, int pageNum, int pageSize) {
        Query query = new Query();
        if (!StringUtils.isEmpty(provinceName))
            query.addCriteria(Criteria.where("value.province").is(provinceName));
        if (!StringUtils.isEmpty(collegeName))
            query.addCriteria(Criteria.where("_id").regex(".*?\\" + collegeName + ".*"));
        long count = mongoTemplate.count(query, "stat_collegeInfo");
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "value.count"))).skip((pageNum - 1) * pageSize).limit(pageSize);
        List<Map> stat_collegeInfo = mongoTemplate.find(query, Map.class, "stat_collegeInfo");
        long pages = count / pageSize + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("data", stat_collegeInfo);
        map.put("count", count);
        map.put("pages", pages);
        return map;
    }

    @Override
    public Map<String, Object> provincePatentList(PatentParam param, int pageNum, int pageSize) {
        Query query = new Query();
        if (!StringUtils.isEmpty(param.getAd()))
            query.addCriteria(Criteria.where("ad").is(param.getAd()));
        if (!StringUtils.isEmpty(param.getAn()))
            query.addCriteria(Criteria.where("an").is(param.getAn()));
        if (!StringUtils.isEmpty(param.getLastLegalStatus()))
            query.addCriteria(Criteria.where("lastLegalStatus").is(param.getLastLegalStatus()));
        if (!StringUtils.isEmpty(param.getPa()))
            query.addCriteria(Criteria.where("pa").is(param.getPa()));
        if (!StringUtils.isEmpty(param.getPatType()))
            query.addCriteria(Criteria.where("patType").is(param.getPatType()));
        if (!StringUtils.isEmpty(param.getTi()))
            query.addCriteria(Criteria.where("ti").is(param.getTi()));
        if (!StringUtils.isEmpty(param.getPin()))
            query.addCriteria(Criteria.where("pin").regex(".*?\\" + param.getPin() + ".*"));
        if (!StringUtils.isEmpty(param.getPd()))
            query.addCriteria(Criteria.where("pd").is(param.getPd()));

        if (!StringUtils.isEmpty(param.getProvinceName()))
            query.addCriteria(Criteria.where("province").is(param.getProvinceName()));
        if (!StringUtils.isEmpty(param.getCollegeName()))
            query.addCriteria(Criteria.where("collegeName").is(param.getCollegeName()));

        Criteria criteria = Criteria.where("patentValue");
        criteria.ne(null);
        if (!StringUtils.isEmpty(param.getStartValue())) {
            criteria.gte(param.getStartValue());
        }
        if (!StringUtils.isEmpty(param.getEndValue())) {
            criteria.lte(param.getEndValue());
        }
        query.addCriteria(criteria);

        long count = mongoTemplate.count(query, "detail_collegePatent");
        query.skip((pageNum - 1) * pageSize).limit(pageSize);

        List<Map> detail_collegePatent = mongoTemplate.find(query, Map.class, "detail_collegePatent");
        detail_collegePatent.forEach(map -> {
            Decimal128 patentValue = (Decimal128) map.get("patentValue");
            if (null != patentValue) {
                map.put("patentValue", patentValue.bigDecimalValue().doubleValue());
            } else {
                map.put("patentValue", null);
            }
        });
        long pages = count / pageSize + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("data", detail_collegePatent);
        map.put("count", count);
        map.put("pages", pages);
        return map;
    }

    @Override
    public List<Map> collegePatentSort(String type, String legState, int limit) {
        Query query = new Query();
        if (StringUtils.isEmpty(type) && StringUtils.isEmpty(legState)) {
            query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "count"))).limit(limit);
            return mongoTemplate.find(query, Map.class, "stat_patent_all");
        }

        if (StringUtils.isEmpty(type) && !StringUtils.isEmpty(legState)) {
            query.addCriteria(Criteria.where("_id.lastLegalStatus").is(legState));
            query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "count"))).limit(limit);
            return mongoTemplate.find(query, Map.class, "stat_patent_legstat");
        }

        if (!StringUtils.isEmpty(type) && StringUtils.isEmpty(legState)) {
            query.addCriteria(Criteria.where("_id.patType").is(type));
            query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "count"))).limit(limit);
            return mongoTemplate.find(query, Map.class, "stat_patent_type");
        }

        if (!StringUtils.isEmpty(type) && !StringUtils.isEmpty(legState)) {
            query.addCriteria(Criteria.where("_id.lastLegalStatus").is(legState));
            query.addCriteria(Criteria.where("_id.patType").is(type));
            query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "count"))).limit(limit);
            return mongoTemplate.find(query, Map.class, "stat_patent_type_legstat");
        }
        return null;
    }

    @Override
    public List<Map> collegeTypeStat(String name) {
        if (StringUtils.isEmpty(name))
            throw new IllegalArgumentException("college name invalid");
        Query query = new Query();
        query.addCriteria(Criteria.where("_id.collegeName").is(name));
        List<Map> stat_patent_type = mongoTemplate.find(query, Map.class, "stat_patent_type");
        stat_patent_type.forEach(map -> {
            Object object = ((Map) map.get("_id")).get("patType");
            Query query1 = new Query();
            query1.addCriteria(Criteria.where("_id.patType").is(object));
            query1.addCriteria(Criteria.where("_id.collegeName").is(name));
            List<Map> maps = mongoTemplate.find(query1, Map.class, "stat_patent_type_legstat");
            map.put("legStat", maps.stream().map(map1 -> {
                Map<Object, Object> result = new HashMap<>();
                result.put("count", map1.get("count"));
                result.put("lastLegalStatus", ((Map) map1.get("_id")).get("lastLegalStatus"));
                return result;
            }).collect(Collectors.toList()));
        });
        return stat_patent_type;
    }

    @Override
    public List<Map> collegeLegStat(String name) {
        if (StringUtils.isEmpty(name))
            throw new IllegalArgumentException("college name invalid");
        Query query = new Query();
        query.addCriteria(Criteria.where("_id.collegeName").is(name));
        List<Map> stat_patent_legstat = mongoTemplate.find(query, Map.class, "stat_patent_legstat");
        stat_patent_legstat.forEach(map -> {
            Object object = ((Map) map.get("_id")).get("lastLegalStatus");
            Query query1 = new Query();
            query1.addCriteria(Criteria.where("_id.lastLegalStatus").is(object));
            query1.addCriteria(Criteria.where("_id.collegeName").is(name));
            List<Map> maps = mongoTemplate.find(query1, Map.class, "stat_patent_type_legstat");
            map.put("patType", maps.stream().map(map1 -> {
                Map<Object, Object> result = new HashMap<>();
                result.put("count", map1.get("count"));
                result.put("patType", ((Map) map1.get("_id")).get("patType"));
                return result;
            }).collect(Collectors.toList()));
        });

        return stat_patent_legstat;
    }

    @Override
    public List<Map> collegeStatLastTenYear(String name) {
        if (StringUtils.isEmpty(name))
            throw new IllegalArgumentException("college name invalid");
        Query query = new Query();
        query.addCriteria(Criteria.where("value.collegeName").is(name));
        query.addCriteria(Criteria.where("value.year").gt("2008").lte("2018"));
        query.with(new Sort(new Sort.Order(Sort.Direction.ASC, "value.year")));
        return mongoTemplate.find(query, Map.class, "stat_collegeInfoYear");
    }

    @Override
    public List<Map> collegeIpc(String name, String collection, int limit) {
        if (StringUtils.isEmpty(name))
            throw new IllegalArgumentException("college name invalid");
        Query query = new Query();
        query.addCriteria(Criteria.where("_id.collegeName").is(name));
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "count"))).limit(limit);
        return mongoTemplate.find(query, Map.class, "stat_ipc_" + collection);
    }

    @Override
    public List<Map> collegeIpcTopYear(String name) {
        List<Map> stat_ipc_all = collegeIpc(name, "all", 10);
        List<String> codes = stat_ipc_all.stream().map(map -> ((Map) map.get("_id")).get("codeThree").toString()).collect(Collectors.toList());

        Query query = new Query();
        query.addCriteria(Criteria.where("_id.collegeName").is(name));
        query.addCriteria(Criteria.where("_id.codeThree").in(codes));
        return mongoTemplate.find(query, Map.class, "stat_ipc_year");
    }

    @Override
    public List<Map> collegeIpcTopValue(String name) {
        List<Map> stat_ipc_all = collegeIpc(name, "all", 10);
        stat_ipc_all.forEach(map -> {
            String code = ((Map) map.get("_id")).get("codeThree").toString();
            Query query = new Query();
            query.addCriteria(Criteria.where("collegeName").is(name));
            query.addCriteria(Criteria.where("codeThree").is(code));
            query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "patentValue"))).limit(5);
            List<Map> detail_collegePatent_iPC = mongoTemplate.find(query, Map.class, "detail_collegePatent_iPC");
            map.put("patent", detail_collegePatent_iPC.stream().map(map1 -> {
                Map<Object, Object> result = new HashMap<>();
                result.put("ti", map1.get("ti").toString());
                result.put("an", map1.get("an").toString());
                if (null != map1.get("patentValue")) {
                    result.put("patentValue", ((Decimal128) map1.get("patentValue")).bigDecimalValue().doubleValue());
                } else {
                    result.put("patentValue", null);
                }
                return result;
            }).collect(Collectors.toList()));
        });
        return stat_ipc_all;
    }

    @Override
    public List<Map> collegePartner(String name) {
        if (StringUtils.isEmpty(name))
            throw new IllegalArgumentException("college name invalid");
        Query query = new Query();
        query.addCriteria(Criteria.where("_id.collegeName").is(name));
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "count"))).limit(10);

        return mongoTemplate.find(query, Map.class, "stat_pa_partner");
    }

    @Override
    public List<Map> collegePin(String name, int limit) {
        if (StringUtils.isEmpty(name))
            throw new IllegalArgumentException("college name invalid");
        Query query = new Query();
        query.addCriteria(Criteria.where("value.collegeName").is(name));
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "value.count"))).limit(limit);

        return mongoTemplate.find(query, Map.class, "stat_pin");
    }

    @Override
    public List<Map> collegePinPartner(String name, String pinSplit) {
        if (StringUtils.isEmpty(name))
            throw new IllegalArgumentException("college name invalid");
        Query query = new Query();
        query.addCriteria(Criteria.where("value.collegeName").is(name));
        query.addCriteria(Criteria.where("value.pinSplit").is(pinSplit));

        return mongoTemplate.find(query, Map.class, "stat_pin_partner");
    }

    @Override
    public List<Map> byQuoteTotal(String name, int limit) {
        if (StringUtils.isEmpty(name))
            throw new IllegalArgumentException("college name invalid");
        Query query = new Query();
        query.addCriteria(Criteria.where("collegeName").is(name));
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "byQuoteTotal"))).limit(limit);

        return mongoTemplate.find(query, Map.class, "detail_collegePatent");
    }

    @Override
    public Map<String, Object> collegePmesValue(String name) {
        if (StringUtils.isEmpty(name))
            throw new IllegalArgumentException("college name invalid");
        Query query = new Query();
        query.addCriteria(Criteria.where("_id.collegeName").is(name));
        query.with(new Sort(new Sort.Order(Sort.Direction.ASC, "_id.title")));
        List<Map> stat_college_value = mongoTemplate.find(query, Map.class, "stat_college_value");
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("distri", stat_college_value);
        query = new Query();
        query.addCriteria(Criteria.where("_id.collegeName").is(name));
        Map map = mongoTemplate.findOne(query, Map.class, "stat_college_value_avg");
        Object avg = null;
        if (null != map) {
            avg = map.get("avg");
        }
        if (null != avg)
            hashMap.put("avg", ((Decimal128) avg).bigDecimalValue().doubleValue());
        else
            hashMap.put("avg", null);

        return hashMap;
    }

    @Override
    public Map<String, Object> pinPmesValue(String collegeName, String name) {
        if (StringUtils.isEmpty(collegeName))
            throw new IllegalArgumentException("college name invalid");
        if (StringUtils.isEmpty(name))
            throw new IllegalArgumentException("pin name invalid");
        Query query = new Query();
        query.addCriteria(Criteria.where("_id.collegeName").is(collegeName));
        query.addCriteria(Criteria.where("_id.pin").is(name));
        query.with(new Sort(new Sort.Order(Sort.Direction.ASC, "_id.title")));
        List<Map> stat_pin_value = mongoTemplate.find(query, Map.class, "stat_pin_value");
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("distri", stat_pin_value);
        query = new Query();
        query.addCriteria(Criteria.where("_id.collegeName").is(collegeName));
        query.addCriteria(Criteria.where("_id.pin").is(name));
        Map map = mongoTemplate.findOne(query, Map.class, "stat_pin_value_avg");
        Object avg = null;
        if (null != map) {
            avg = map.get("avg");
        }
        if (null != avg)
            hashMap.put("avg", ((Decimal128) avg).bigDecimalValue().doubleValue());
        else
            hashMap.put("avg", null);

        return hashMap;
    }

    @Override
    public String getExpertRecommendation(String collegeName, String name) {
        String url = tgurl + "?assignee={assignee}&expert={expert}";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("API_NAME", "getExpertRecommendation");
        requestHeaders.add("APP_ID", appid);
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(null, requestHeaders), String.class, collegeName, name).getBody();
    }
}
