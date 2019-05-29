package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.controller.api.request.SpecialistInfo;
import com.cnuip.pmes2.controller.api.request.SpecialistRequest;
import com.cnuip.pmes2.domain.core.*;
import com.cnuip.pmes2.repository.core.CollegeMapper;
import com.cnuip.pmes2.repository.core.SpecialistMapper;
import com.cnuip.pmes2.repository.core.TitleMapper;
import com.cnuip.pmes2.service.SpecialistService;
import com.cnuip.pmes2.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class SpecialistServiceImpl implements SpecialistService {

    private SpecialistMapper specialistMapper;
    private UserService userService;
    private CollegeMapper collegeMapper;
    private TitleMapper titleMapper;

    private MongoTemplate mongoTemplate;

    public SpecialistServiceImpl(SpecialistMapper specialistMapper, UserService userService, CollegeMapper collegeMapper, TitleMapper titleMapper, @Qualifier(value = "secondMongoTemplate") MongoTemplate mongoTemplate) {
        this.specialistMapper = specialistMapper;
        this.userService = userService;
        this.collegeMapper = collegeMapper;
        this.titleMapper = titleMapper;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void insert(Long userId, SpecialistInfo specialistInfo) {
        User user = userService.selectUserByPrimaryKey(userId);
        Specialist specialist = specialistInfo.getSpecialist();
        if (null != specialist) {
            specialist.setEditorId(userId);
            specialist.setEditorName(user.getName());
        }
        long id = specialistMapper.insert(specialist);

        SpecialistExt specialistExt = specialistInfo.getSpecialistExt();
        specialistExt.setExpertId(id);
        specialistMapper.insertExt(specialistExt);

        List<SpecialistKeyword> specialistKeywords = specialistInfo.getSpecialistKeywords();
        if (null != specialistKeywords) {
            specialistKeywords.forEach(specialistKeyword -> specialistKeyword.setExpertId(id));
            specialistMapper.insertKeyword(specialistKeywords);
        }

        List<SpecialistPaper> specialistPapers = specialistInfo.getSpecialistPapers();
        if (null != specialistPapers) {
            specialistPapers.forEach(specialistPaper -> specialistPaper.setExpertId(id));
            specialistMapper.insertPaper(specialistPapers);
        }

        List<SpecialistTitle> specialistTitles = specialistInfo.getSpecialistTitles();
        if (null != specialistTitles) {
            specialistTitles.forEach(specialistTitle -> specialistTitle.setExpertId(id));
            specialistMapper.insertTitle(specialistTitles);
        }

        List<SpecialistIpc> specialistIpcs = specialistInfo.getSpecialistIpcs();
        if (null != specialistIpcs) {
            specialistIpcs.forEach(specialistIpc -> specialistIpc.setExpertId(id));
            specialistMapper.insertIpc(specialistIpcs);
        }

        List<SpecialistNic> specialistNics = specialistInfo.getSpecialistNics();
        if (null != specialistNics) {
            specialistNics.forEach(specialistNic -> specialistNic.setExpertId(id));
            specialistMapper.insertNic(specialistNics);
        }

        List<SpecialistNtcc> specialistNtccs = specialistInfo.getSpecialistNtccs();
        if (null != specialistNtccs) {
            specialistNtccs.forEach(specialistNtcc -> specialistNtcc.setExpertId(id));
            specialistMapper.insertNtcc(specialistNtccs);
        }

        List<String> codes = new ArrayList<>();
        if (null != specialistIpcs) {
            List<String> preCodes = specialistIpcs.stream().map(SpecialistIpc::getPreCode).collect(Collectors.toList());
            codes = specialistIpcs.stream().map(SpecialistIpc::getCode).collect(Collectors.toList());
            codes.addAll(preCodes);
        }

        Set<String> s = new HashSet<>();
        if (null != specialistNics) {
            List<String> collect = specialistNics.stream().map(SpecialistNic::getCode).collect(Collectors.toList());
            collect.forEach(nic -> {
                if (nic.length() == 1) {
                    s.add(nic);
                } else if (nic.length() == 3) {
                    s.add(nic);
                    s.add(nic.substring(0, 1));
                } else if (nic.length() == 4) {
                    s.add(nic);
                    s.add(nic.substring(0, 1));
                    s.add(nic.substring(0, 3));
                }
            });
        }

        Set<String> set=new HashSet<>();
        if (null != specialistNtccs) {
            List<String> collect = specialistNtccs.stream().map(SpecialistNtcc::getCode).collect(Collectors.toList());
            for (String ntcc : collect) {
                if(ntcc.length()==1){
                    set.add(ntcc);
                }else if(ntcc.length()==2){
                    set.add(ntcc);
                    set.add(ntcc.substring(0,1));
                }else if(ntcc.length()==4) {
                    set.add(ntcc);
                    set.add(ntcc.substring(0, 1));
                    set.add(ntcc.substring(0, 3));
                }
            }
        }

        List<String> finalCodes = codes;
        new Thread(() -> {
            Query query = new Query();
            query.addCriteria(Criteria.where("ph").is(specialist.getCollege()).andOperator(Criteria.where("name").is(specialist.getName())));
            List<Map> multi = mongoTemplate.find(query, Map.class, "app_expert");
            if (multi.isEmpty()) {
                mongoTemplate.insert(specialistInfo, "app_expert");
            } else {
                Map one = multi.get(0);
                ObjectMapper objectMapper = new ObjectMapper();
                String valueAsString;
                try {
                    valueAsString = objectMapper.writeValueAsString(specialistInfo);
                    Map readValue = objectMapper.readValue(valueAsString, Map.class);
                    one.putAll(readValue);
                    one.put("relationIpc", finalCodes);
                    one.put("relationNic", s);
                    one.put("relationNtcc", set);
                    //
                    mongoTemplate.remove(query, "app_expert");
                    mongoTemplate.insert(one, "app_expert");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void update(Long userId, SpecialistInfo specialistInfo) {
        Specialist specialist = specialistInfo.getSpecialist();
        if (null == specialist)
            return;
        Long id = specialist.getId();
        User user = userService.selectUserByPrimaryKey(userId);
        specialist.setEditorId(userId);
        specialist.setEditorName(user.getName());
        specialistMapper.update(specialist);
        SpecialistExt specialistExt = specialistInfo.getSpecialistExt();
        if (null != specialistExt)
            specialistMapper.updateExt(specialistExt);

        List<SpecialistKeyword> specialistKeywords = specialistInfo.getSpecialistKeywords();
        if (null != specialistKeywords) {
            specialistMapper.deleteKeyword(id);
            specialistMapper.insertKeyword(specialistKeywords);
        }

        List<SpecialistPaper> specialistPapers = specialistInfo.getSpecialistPapers();
        if (null != specialistPapers) {
            specialistMapper.deletePaper(id);
            specialistMapper.insertPaper(specialistPapers);
        }

        List<SpecialistTitle> specialistTitles = specialistInfo.getSpecialistTitles();
        if (null != specialistTitles) {
            specialistMapper.deleteTitle(id);
            specialistMapper.insertTitle(specialistTitles);
        }

        List<SpecialistIpc> specialistIpcs = specialistInfo.getSpecialistIpcs();
        if (null != specialistIpcs) {
            specialistMapper.deleteIpc(id);
            specialistMapper.insertIpc(specialistIpcs);
        }

        List<SpecialistNic> specialistNics = specialistInfo.getSpecialistNics();
        if (null != specialistNics) {
            specialistMapper.deleteNic(id);
            specialistMapper.insertNic(specialistNics);
        }

        List<SpecialistNtcc> specialistNtccs = specialistInfo.getSpecialistNtccs();
        if (null != specialistNtccs) {
            specialistMapper.deleteNtcc(id);
            specialistMapper.insertNtcc(specialistNtccs);
        }

        SpecialistInfo now = findOne(id);
        List<String> codes = new ArrayList<>();
        if (null != now.getSpecialistIpcs()) {
            List<String> preCodes = now.getSpecialistIpcs().stream().map(SpecialistIpc::getPreCode).collect(Collectors.toList());
            codes = now.getSpecialistIpcs().stream().map(SpecialistIpc::getCode).collect(Collectors.toList());
            codes.addAll(preCodes);
        }

        Set<String> s = new HashSet<>();
        if (null != now.getSpecialistNics()) {
            List<String> collect = now.getSpecialistNics().stream().map(SpecialistNic::getCode).collect(Collectors.toList());
            collect.forEach(nic -> {
                if (nic.length() == 1) {
                    s.add(nic);
                } else if (nic.length() == 3) {
                    s.add(nic);
                    s.add(nic.substring(0, 1));
                } else if (nic.length() == 4) {
                    s.add(nic);
                    s.add(nic.substring(0, 1));
                    s.add(nic.substring(0, 3));
                }
            });
        }

        Set<String> set=new HashSet<>();
        if (null != now.getSpecialistNtccs()) {
            List<String> collect = now.getSpecialistNtccs().stream().map(SpecialistNtcc::getCode).collect(Collectors.toList());
            for (String ntcc : collect) {
                if(ntcc.length()==1){
                    set.add(ntcc);
                }else if(ntcc.length()==2){
                    set.add(ntcc);
                    set.add(ntcc.substring(0,1));
                }else if(ntcc.length()==4) {
                    set.add(ntcc);
                    set.add(ntcc.substring(0, 1));
                    set.add(ntcc.substring(0, 3));
                }
            }
        }

        List<String> finalCodes = codes;
        new Thread(() -> {
            Query query = new Query();
            query.addCriteria(Criteria.where("ph").is(specialist.getCollege()).andOperator(Criteria.where("name").is(specialist.getName())));
            List<Map> multi = mongoTemplate.find(query, Map.class, "app_expert");
            if (!multi.isEmpty()) {
                Map one = multi.get(0);
                ObjectMapper objectMapper = new ObjectMapper();
                String valueAsString;
                try {
                    valueAsString = objectMapper.writeValueAsString(now);
                    Map readValue = objectMapper.readValue(valueAsString, Map.class);
                    one.putAll(readValue);
                    one.put("relationIpc", finalCodes);
                    one.put("relationNic", s);
                    one.put("relationNtcc", set);

                    mongoTemplate.remove(query, "app_expert");
                    mongoTemplate.insert(one, "app_expert");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }).start();

    }

    @Override
    public SpecialistInfo findOne(Long id) {
        if (null == id)
            return null;
        Specialist specialist = specialistMapper.findById(id);
        SpecialistExt specialistExt = specialistMapper.findExtById(id);
        List<SpecialistIpc> specialistIpcs = specialistMapper.findIpcById(id);
        List<SpecialistKeyword> specialistKeywords = specialistMapper.findKeywordById(id);
        List<SpecialistNic> specialistNics = specialistMapper.findNicById(id);
        List<SpecialistTitle> specialistTitles = specialistMapper.findTitleById(id);
        List<SpecialistNtcc> specialistNtccs = specialistMapper.findNtccById(id);
        List<SpecialistPaper> specialistPapers = specialistMapper.findPaperById(id);

        SpecialistInfo specialistInfo = new SpecialistInfo();
        specialistInfo.setSpecialist(specialist);
        specialistInfo.setSpecialistExt(specialistExt);
        specialistInfo.setSpecialistIpcs(specialistIpcs);
        specialistInfo.setSpecialistKeywords(specialistKeywords);
        specialistInfo.setSpecialistPapers(specialistPapers);
        specialistInfo.setSpecialistNtccs(specialistNtccs);
        specialistInfo.setSpecialistNics(specialistNics);
        specialistInfo.setSpecialistTitles(specialistTitles);

        return specialistInfo;
    }

    @Override
    public Map<String, List<College>> colleges() {
        List<College> colleges = collegeMapper.selectAll();
        return colleges.stream().collect(Collectors.groupingBy(College::getProvinceName, Collectors.toList()));
    }

    @Override
    public List<Title> titles() {
        return titleMapper.selectAll();
    }

    @Override
    public Map<String, Object> specialistPage(SpecialistRequest specialistRequest) {
        Query query = new Query();
        String provinceName = specialistRequest.getProvinceName();
        if (null != provinceName)
            query.addCriteria(Criteria.where("specialist.provinceName").is(provinceName));

        String college = specialistRequest.getCollege();
        if (null != college)
            query.addCriteria(Criteria.where("specialist.college").is(college));

        String facultyDepartment = specialistRequest.getFacultyDepartment();
        if (null != facultyDepartment)
            query.addCriteria(Criteria.where("specialist.facultyDepartment").is(facultyDepartment));

        String name = specialistRequest.getName();
        if (null != name)
            query.addCriteria(Criteria.where("specialist.name").is(name));

        String phone = specialistRequest.getPhone();
        if (null != provinceName)
            query.addCriteria(Criteria.where("specialist.phone").is(phone));

        Short source = specialistRequest.getSource();
        if (null != source)
            query.addCriteria(Criteria.where("specialist.source").is(source));

        String speciality = specialistRequest.getSpeciality();
        if (null != speciality)
            query.addCriteria(Criteria.where("specialist.speciality").is(speciality));

        String keywords = specialistRequest.getKeywords();
        if (null != keywords && !"".equals(keywords)) {
            String[] split = keywords.split(",");
            Criteria in = Criteria.where("specialistKeywords.content").in(split[0]);
            for (int i = 1; i < split.length; i++) {
                in = in.orOperator(Criteria.where("specialistKeywords.content").in(split[i]));
            }
            query.addCriteria(in);
        }

        List<String> ipcs = specialistRequest.getIpcs();
        List<String> nics = specialistRequest.getNics();
        List<String> ntccs = specialistRequest.getNtccs();

        if (null != ipcs && !ipcs.isEmpty()) {
            Criteria in = Criteria.where("ipcArray").in(ipcs.get(0));
            for (int i = 1; i < ipcs.size(); i++) {
                in = in.orOperator(Criteria.where("ipcArray").in(ipcs.get(i)));
            }
            for (String ipc : ipcs) {
                in = in.orOperator(Criteria.where("relationIpc").in(ipc));
            }
            query.addCriteria(in);
        }

        if (null != nics && !nics.isEmpty()) {
            Criteria in = Criteria.where("relationNic").in(nics.get(0));
            for (int i = 1; i < nics.size(); i++) {
                in = in.orOperator(Criteria.where("relationNic").in(nics.get(i)));
            }
            query.addCriteria(in);
        }

        if (null != ntccs && !ntccs.isEmpty()) {
            Criteria in = Criteria.where("relationNtcc").in(ntccs.get(0));
            for (int i = 1; i < ntccs.size(); i++) {
                in = in.orOperator(Criteria.where("relationNtcc").in(ntccs.get(i)));
            }
            query.addCriteria(in);
        }

        int pageSize = specialistRequest.getPageSize();
        int pageNum = specialistRequest.getPageNum();

        long count = mongoTemplate.count(query, "app_expert");
        query.skip((pageNum - 1) * pageSize).limit(pageSize);
        List<Map> specialists = mongoTemplate.find(query, Map.class, "app_expert");
        long pages = count / pageSize + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("data", specialists);
        map.put("count", count);
        map.put("pages", pages);
        return map;
    }
}
