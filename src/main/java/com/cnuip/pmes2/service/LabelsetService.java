package com.cnuip.pmes2.service;

import com.cnuip.pmes2.controller.api.request.LabelsetSearchCondition;
import com.cnuip.pmes2.domain.core.Labelset;
import com.cnuip.pmes2.domain.core.LabelsetLabel;
import com.cnuip.pmes2.exception.LabelsetException;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by wangzhibin on 2018/1/11.
 */
public interface LabelsetService {

    Labelset selectByPrimaryKey(Long id);

    PageInfo<Labelset> selectAll(int pageNum, int pageSize);

    PageInfo<Labelset> selectByType(Integer type, int pageNum, int pageSize);

    PageInfo<Labelset> search(LabelsetSearchCondition condition, int pageNum, int pageSize);

    List<Labelset> findByType(Integer type);

    List<LabelsetLabel> selectLabelsetLabels(Long id);

    LabelsetLabel selectParentLabelsetLabel(Long parentId);

    List<LabelsetLabel> selectChildLabelsetLabels(Long id);

    Labelset addLabelset(Labelset labelset, Long userId) throws LabelsetException;

    Labelset updateLabelset(Labelset labelset, Long userId) throws LabelsetException;

    Labelset updateLabelsetName(Labelset labelset, Long userId) throws LabelsetException;

    LabelsetLabel updateLabelsetLabel(LabelsetLabel labelsetLabel) throws LabelsetException;

    void changeLabelsetState(long id, Integer state) throws LabelsetException;

    Labelset copyLabelset(Long id, long userId) throws LabelsetException;

    void changeLabelsetFieldValue(Long id, String field, Object value) throws LabelsetException;
}
