package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.controller.api.request.ResultSearchCondition;
import com.cnuip.pmes2.controller.api.response.IpcVo;
import com.cnuip.pmes2.controller.api.response.NicVo;
import com.cnuip.pmes2.controller.api.response.NtccVo;
import com.cnuip.pmes2.domain.core.*;
import com.cnuip.pmes2.domain.el.ElResult;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.repository.core.ResultMapper;
import com.cnuip.pmes2.repository.el.ElResultRepository;
import com.cnuip.pmes2.service.ResultService;
import com.cnuip.pmes2.service.UserService;
import com.cnuip.pmes2.util.TreeUtils;
import com.google.common.collect.Lists;
import org.apache.poi.ss.formula.functions.T;
import org.elasticsearch.bootstrap.Elasticsearch;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Beans;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2019/5/21.
 * Time: 14:36
 */
@Service
public class ResultServiceImpl implements ResultService {

    @Autowired
    UserService userService;

    @Autowired
    ResultMapper resultMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    ElResultRepository elResultRepository;



    @Override
    @Transactional
    public void add(Long userId, Result result) throws BussinessLogicException {
        User user = userService.selectUserByPrimaryKey(userId);
        result.setEditorId(userId);
        result.setEditorName(user.getName());
        resultMapper.save(result);
        if(result.getIpcIds()!=null) {
            Set<String> ipcs = getIpcs(result.getIpcIds());
            ipcs.forEach(s->saveIpc(s,result.getId()));
        }
        if(result.getNicIds()!=null) {
            Set<String> nics = getNics(result.getNicIds());
            nics.forEach(s->saveNic(s,result.getId()));
        }
        if(result.getNtccIds()!=null) {
            Set<String> ntccs = getNtccs(result.getNtccIds());
            ntccs.forEach(s->saveNtcc(s,result.getId()));
        }

        ResultExt resultExt=new ResultExt();
        BeanUtils.copyProperties(result,resultExt);
        resultExt.setResultId(result.getId());
        resultMapper.saveResultExt(resultExt);
        ResultKeyWord resultKeyWord=new ResultKeyWord();
        BeanUtils.copyProperties(result,resultKeyWord);
        resultKeyWord.setResultId(result.getId());
        resultMapper.saveResultKeyword(resultKeyWord);
        ElResult elResult=new ElResult();
        BeanUtils.copyProperties(result,elResult);
        elResult.setMode(result.getMode().replace(","," "));
        elResult.setIpcs(result.getIpcIds().replaceAll(","," "));
        elResult.setNics(result.getNicIds().replaceAll(","," "));
        elResult.setNtccs(result.getNtccIds().replaceAll(","," "));
        new Thread(new SynchronizedElsThread(elResultRepository,elResult)).start();
    }

    @Override
    @Transactional
    public void update(Long userId, Result result) throws BussinessLogicException {
        User user = userService.selectUserByPrimaryKey(userId);
        result.setEditorId(userId);
        result.setEditorName(user.getName());
        if(result.getIpcIds()!=null){
            resultMapper.deleteIpc(result.getId());
            Set<String> ipcs=getIpcs(result.getIpcIds());
            ipcs.forEach(s->saveIpc(s,result.getId()));
        }
        if(result.getNicIds()!=null){
            resultMapper.deleteNic(result.getId());
            Set<String> nics=getNics(result.getNicIds());
            nics.forEach(s->saveNic(s,result.getId()));
        }
        if(result.getNtccIds()!=null){
            resultMapper.deleteNtcc(result.getId());
            Set<String> ntccs=getNtccs(result.getNtccIds());
            ntccs.forEach(s->saveNtcc(s,result.getId()));
        }
        ResultExt resultExt=new ResultExt();
        BeanUtils.copyProperties(result,resultExt);
        resultExt.setResultId(result.getId());
        resultExt.setUpdatedTime(new Date());
        resultMapper.updateResultExt(resultExt);
        ResultKeyWord resultKeyWord=new ResultKeyWord();
        BeanUtils.copyProperties(result,resultKeyWord);
        resultKeyWord.setResultId(result.getId());
        resultKeyWord.setUpdatedTime(new Date());
        resultMapper.updateResultKeyWord(resultKeyWord);
        resultMapper.update(result);
        ElResult elResult=new ElResult();
        BeanUtils.copyProperties(result,elResult);
        elResult.setIpcs(result.getIpcIds().replaceAll(","," "));
        elResult.setNics(result.getNicIds().replaceAll(","," "));
        elResult.setNtccs(result.getNtccIds().replaceAll(","," "));
        elResult.setUpdatedTime(new Date());
        new Thread(new SynchronizedElsUpdateThread(elResultRepository,elResult)).start();
    }

    @Override
    public List<IpcVo> getIpc() {
        final Object ipc = redisTemplate.opsForValue().get("ipc");
        if (ipc!=null){
            return (List<IpcVo>)ipc;
        }
        List<Ipc> ipcs=resultMapper.findIpcAll();
        List<IpcVo> ipcVos=new ArrayList<>();
        for (Ipc e : ipcs) {
            IpcVo ipcVo = new IpcVo();
            BeanUtils.copyProperties(e, ipcVo);
            ipcVos.add(ipcVo);
        }
        List<IpcVo> ipcVoList=TreeUtils.listToTree(ipcVos);
        redisTemplate.opsForValue().set("ipc",ipcVoList);
        return ipcVoList;
    }

    @Override
    public List<NicVo> getNic() {
        final Object nic = redisTemplate.opsForValue().get("nic");
        if (nic!=null){
            return (List<NicVo>)nic;
        }
        List<Nic> nics=resultMapper.findNicAll();
        List<NicVo> nicVos=new ArrayList<>();
        for (Nic n : nics) {
            NicVo nicVo = new NicVo();
            BeanUtils.copyProperties(n, nicVo);
            nicVos.add(nicVo);
        }
        List<NicVo> nicVoList=TreeUtils.listToTree(nicVos);
        redisTemplate.opsForValue().set("nic",nicVoList);
        return nicVoList;
    }

    @Override
    public List<NtccVo> getNtcc() {
        final Object ntcc = redisTemplate.opsForValue().get("ntcc");
        if (ntcc!=null){
            return (List<NtccVo>)ntcc;
        }
        List<Ntcc> ntccs=resultMapper.findNtccAll();
        List<NtccVo> ntccVos=new ArrayList<>();
        for (Ntcc n : ntccs) {
            NtccVo ntccVo = new NtccVo();
            BeanUtils.copyProperties(n, ntccVo);
            ntccVos.add(ntccVo);
        }
        List<NtccVo> ntccVoList=TreeUtils.listToTree(ntccVos);
        redisTemplate.opsForValue().set("ntcc",ntccVoList);
        return ntccVoList;
    }

    @Override
    public List<Ipc> getIpcFirst() {
        return resultMapper.getIpcFirst();
    }

    @Override
    public List<Ipc> childIpcByCode(String code) {
        return resultMapper.childIpcByCode(code);
    }

    @Override
    public List<Nic> getNicFirst() {
        return resultMapper.getNicFirst();
    }

    @Override
    public List<Nic> childNicByCode(String code) {
        return resultMapper.childNicByCode(code);
    }

    @Override
    public List<Ntcc> getNtccFirst() {
        return resultMapper.getNtccFirst();
    }

    @Override
    public List<Ntcc> childNtccByCode(String code) {
        return resultMapper.childNtccByCode(code);
    }

    @Override
    public List<ElResult> getResultList(ResultSearchCondition resultSearchCondition) {
        if (resultSearchCondition.getPageNum() == null) {
            resultSearchCondition.setPageNum(1);
        }
        if (resultSearchCondition.getPageSize() == null) {
            resultSearchCondition.setPageSize(10);
        }
        Pageable pageable=new PageRequest(resultSearchCondition.getPageNum()-1,resultSearchCondition.getPageSize());
        BoolQueryBuilder queryBuilder= QueryBuilders.boolQuery();
        buildParam(queryBuilder,resultSearchCondition);
        Page<ElResult> page=elResultRepository.search(queryBuilder,pageable);
        return page.getContent();
    }

    @Override
    public ElResult getResultDetail(Long id) {
        return elResultRepository.findOne(id);
    }

    @Override
    public List<ElResult> export() {
        final Iterable all = elResultRepository.findAll();
        final List<ElResult> list = Lists.newArrayList(all);
        return list;
    }

    private void buildParam(BoolQueryBuilder queryBuilder, ResultSearchCondition resultSearchCondition) {
        if(resultSearchCondition.getCollege()!=null){
            QueryBuilder q = QueryBuilders.termQuery("college", resultSearchCondition.getCollege());
            queryBuilder.must(q);
        }
        if(resultSearchCondition.getMode()!=null){
            QueryBuilder q = QueryBuilders.matchQuery("mode",resultSearchCondition.getMode());
            queryBuilder.must(q);
        }
        if(resultSearchCondition.getName()!=null){
            QueryBuilder q=QueryBuilders.matchQuery("name",resultSearchCondition.getName());
            queryBuilder.must(q);
        }
        if(resultSearchCondition.getSource()!=null){
            QueryBuilder q=QueryBuilders.termQuery("source",resultSearchCondition.getSource());
            queryBuilder.must(q);
        }
        if(resultSearchCondition.getMaturity()!=null){
            QueryBuilder q=QueryBuilders.termQuery("maturity",resultSearchCondition.getMaturity());
            queryBuilder.must(q);
        }
        if(resultSearchCondition.getProvinceName()!=null){
            QueryBuilder q=QueryBuilders.termQuery("provinceName",resultSearchCondition.getProvinceName());
            queryBuilder.must(q);
        }
        if(resultSearchCondition.getPhone()!=null){
            QueryBuilder q=QueryBuilders.termQuery("phone",resultSearchCondition.getPhone());
            queryBuilder.must(q);
        }
        if(resultSearchCondition.getIpcCodes()!=null){
            String[] ipcs=resultSearchCondition.getIpcCodes().split(",");
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            for (String ipc : ipcs) {
                QueryBuilder b=QueryBuilders.matchQuery("ipcs",ipc);
                boolQueryBuilder.should(b);
            }
        }
        if(resultSearchCondition.getNicCodes()!=null){
            String[] nics=resultSearchCondition.getNicCodes().split(",");
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            for (String nic : nics) {
                QueryBuilder b=QueryBuilders.matchQuery("nics",nic);
                boolQueryBuilder.should(b);
            }
        }
        if(resultSearchCondition.getNtccCodes()!=null){
            String[] ntccs=resultSearchCondition.getNtccCodes().split(",");
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            for (String ntcc : ntccs) {
                QueryBuilder b=QueryBuilders.matchQuery("ntccs",ntcc);
                boolQueryBuilder.should(b);
            }
        }
    }

    private void saveIpc(String code, int resultId) {
        Ipc ipc=resultMapper.findIpcByCode(code);
        ResultIpc resultIpc=new ResultIpc();
        BeanUtils.copyProperties(ipc,resultIpc);
        resultIpc.setIpcId(ipc.getId());
        resultIpc.setResultId(resultId);
        resultMapper.saveIpc(resultIpc);
    }

    private void saveNic(String code, int resultId) {
        Nic nic=resultMapper.findNicByCode(code);
        ResultNic resultNic=new ResultNic();
        BeanUtils.copyProperties(nic,resultNic);
        resultNic.setNicId(nic.getId());
        resultNic.setResultId(resultId);
        resultMapper.saveNic(resultNic);
    }

    private void saveNtcc(String code, int resultId) {
        Ntcc ntcc=resultMapper.findNtccByCode(code);
        ResultNtcc resultNtcc=new ResultNtcc();
        BeanUtils.copyProperties(ntcc,resultNtcc);
        resultNtcc.setResultId(resultId);
        resultNtcc.setNtccId(ntcc.getId());
        resultMapper.saveNtcc(resultNtcc);
    }

    private  Set<String> getIpcs(String ipcIds) {
        String [] ipcs=ipcIds.split(",");
        Set s=new HashSet();
        for (String ipc : ipcs) {
            if(ipc.length()==1){
                s.add(ipc);
            }
            else if(ipc.length()==3){
                s.add(ipc.substring(0,1));
                s.add(ipc);
            }

        }
        return s;
    }

    private Set getNics(String nicIds) {
        Set s=new HashSet();
        String nics[]=nicIds.split(",");
        for (String nic : nics) {
            if(nic.length()==1){
                s.add(nic);
            }else if(nic.length()==3){
                s.add(nic);
                s.add(nic.substring(0,1));
            }else if(nic.length()==4) {
                s.add(nic);
                s.add(nic.substring(0, 1));
                s.add(nic.substring(0, 3));
            }
        }
        return s;
    }

    private Set getNtccs(String ntccIds) {
        Set s=new HashSet();
        String ntccs[]=ntccIds.split(",");
        for (String ntcc : ntccs) {
            if(ntcc.length()==1){
                s.add(ntcc);
            }else if(ntcc.length()==2){
                s.add(ntcc);
                s.add(ntcc.substring(0,1));
            }else if(ntcc.length()==4) {
                s.add(ntcc);
                s.add(ntcc.substring(0, 1));
                s.add(ntcc.substring(0, 2));
            }
        }
        return s;
    }

}
