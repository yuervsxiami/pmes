package com.cnuip.pmes2.repository.core;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import com.cnuip.pmes2.domain.core.Expert;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpertMapper {
    int insert(@Param("expert") Expert expert);

    int insertSelective(@Param("expert") Expert expert);

    int insertList(@Param("experts") List<Expert> experts);

    int update(@Param("expert") Expert expert);

    Expert selectByPrimaryKey(Long id);

    List<Expert> search(@Param("expert") Expert expert, @Param("pageSize")int pageSize, @Param("pageNum")int pageNum);

}
