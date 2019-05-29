package com.cnuip.pmes2.repository.core;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import com.cnuip.pmes2.domain.core.IPCField;
import org.springframework.stereotype.Repository;

@Repository
public interface IPCFieldMapper {

    List<IPCField> search(String ipcFields);

    List<IPCField> selectTop();

    List<IPCField> selectByParentId(Long parentId);

}
