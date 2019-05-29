package com.cnuip.pmes2.service;

import com.cnuip.pmes2.domain.core.Expert;
import com.cnuip.pmes2.domain.core.IPCField;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ExpertService{

    Expert insert(Expert expert);

    int insertSelective(Expert expert);

    int insertList(List<Expert> experts);

    Expert update(Expert expert);

    Expert select(Long id);

    PageInfo<Expert> search(Expert expert, int pageSize, int pageNum);

    List<IPCField> selectAllIpcField();

    String uploadImage(MultipartFile file) throws Exception;
}
