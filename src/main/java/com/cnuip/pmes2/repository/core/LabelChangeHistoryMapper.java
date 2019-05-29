package com.cnuip.pmes2.repository.core;

import com.cnuip.pmes2.controller.api.request.LabelsetSearchCondition;
import com.cnuip.pmes2.domain.core.LabelChangeHistory;
import com.cnuip.pmes2.domain.core.Labelset;
import com.cnuip.pmes2.domain.core.LabelsetLabel;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by sukai on 2018/5/29.
 */
@Repository
public interface LabelChangeHistoryMapper {

    int insert(LabelChangeHistory labelChangeHistory);

    List<LabelChangeHistory> select(@Param("patentId")Long patentId, @Param("labelId")Long labelId);

}
