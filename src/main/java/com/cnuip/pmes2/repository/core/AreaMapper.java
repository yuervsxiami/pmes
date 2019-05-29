package com.cnuip.pmes2.repository.core;

import com.cnuip.pmes2.domain.core.Area;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AreaMapper
 *
 * @author: xiongwei
 * Date: 2018/2/5 下午8:57
 */
//@Repository
public interface AreaMapper {

    int save(Area area);

    Area findById(Long id);

    int update(Area area);

    int delete(Area area);

    List<Area> find(Area area);

    List<Area> findByPage(@Param("query")Area area, @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);

}
