package com.cnuip.pmes2.service.impl;

import com.UpYun;
import com.cnuip.pmes2.domain.core.IPCField;
import com.cnuip.pmes2.repository.core.IPCFieldMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import com.cnuip.pmes2.domain.core.Expert;
import com.cnuip.pmes2.repository.core.ExpertMapper;
import com.cnuip.pmes2.service.ExpertService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(rollbackFor = Exception.class)
public class ExpertServiceImpl implements ExpertService{

    private static final UpYun UP_YUN = new UpYun("bucket-cnuip", "cnuipadmin", "CA2203!A");

    @Resource
    private ExpertMapper expertMapper;

    @Autowired
    private IPCFieldMapper ipcFieldMapper;

    public Expert insert(Expert expert){
        expertMapper.insert(expert);
        return expertMapper.selectByPrimaryKey(expert.getId());
    }

    @Override
    public int insertSelective(Expert expert){
        return expertMapper.insertSelective(expert);
    }

    @Override
    public int insertList(List<Expert> experts){
        return expertMapper.insertList(experts);
    }

    @Override
    public Expert update(Expert expert){
        expertMapper.update(expert);
        return expertMapper.selectByPrimaryKey(expert.getId());
    }

    @Override
    public Expert select(Long id) {
        return expertMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<Expert> search(Expert expert, int pageSize, int pageNum) {
        Page<Expert> page = (Page<Expert>) expertMapper.search(expert,pageSize,pageNum);
        return page.toPageInfo();
    }

    @Override
    public List<IPCField> selectAllIpcField() {
        return ipcFieldMapper.selectTop();
    }

    @Override
    public String uploadImage(MultipartFile file) throws Exception{

        String filePath="/home/headImage/"+file.getOriginalFilename();
        File f = new File(filePath);

        FileUtils.copyInputStreamToFile(file.getInputStream(), f);
        //上传云盘
        Boolean flag;
        try {
            flag = UP_YUN.writeFile(filePath,f);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        if (!flag) {
            throw new RuntimeException("上传失败，请重新上传");
        }
        String url="https://image.cnuip.com"+filePath;
        return url;
    }

}
