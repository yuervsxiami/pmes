package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.controller.api.request.LabelsetSearchCondition;
import com.cnuip.pmes2.domain.core.Labelset;
import com.cnuip.pmes2.domain.core.LabelsetLabel;
import com.cnuip.pmes2.domain.core.ProcessTask;
import com.cnuip.pmes2.domain.core.ProcessTaskLabel;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.exception.LabelsetException;
import com.cnuip.pmes2.repository.core.LabelsetMapper;
import com.cnuip.pmes2.repository.core.ProcessTaskMapper;
import com.cnuip.pmes2.service.LabelsetService;
import com.cnuip.pmes2.util.ResponseEnum;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wangzhibin on 2018/1/11.
 */

@Service
@Transactional(readOnly=true)
public class LabelsetServiceImpl implements LabelsetService {

    @Autowired
    private LabelsetMapper labelsetMapper;

    @Autowired
    private ProcessTaskMapper processTaskMapper;

    @Override
    public Labelset selectByPrimaryKey(Long id){return labelsetMapper.selectByPrimaryKey(id);}

    @Override
    public PageInfo<Labelset> selectAll(int pageNum, int pageSize){
        Page<Labelset> page = (Page<Labelset>) labelsetMapper.selectAll(pageNum,pageSize);
        return page.toPageInfo();
    }

    @Override
    public PageInfo<Labelset> selectByType(Integer type, int pageNum, int pageSize) {
        Page<Labelset> page = (Page<Labelset>) labelsetMapper.selectByType(type, pageNum, pageSize);
        return page.toPageInfo();
    }

    @Override
    public PageInfo<Labelset> search(LabelsetSearchCondition condition, int pageNum, int pageSize){
        Page<Labelset> page = (Page<Labelset>) labelsetMapper.search(condition, pageNum, pageSize);
        return page.toPageInfo();
    }

    @Override
    public List<Labelset> findByType(Integer type){
        return labelsetMapper.findByType(type);
    };

    @Override
    public List<LabelsetLabel> selectLabelsetLabels(Long id){
        return labelsetMapper.selectLabelsetLabels(id);
    }

    @Override
    public LabelsetLabel selectParentLabelsetLabel(Long parentId){
        return labelsetMapper.selectParentLabelsetLabel(parentId);
    }

    @Override
    public List<LabelsetLabel> selectChildLabelsetLabels(Long id){
        return labelsetMapper.selectChildLabelsetLabels(id);
    }

    @Transactional(rollbackFor=BussinessLogicException.class)
    @Override
    public Labelset addLabelset(Labelset labelset, Long userId) throws LabelsetException {
        labelset.setUserId(userId);
        if(labelset.getType()==null) {
            throw new LabelsetException(ResponseEnum.LABELSET_MUST_HAVE_TYPE);
        }
        labelset.setVersion(labelsetMapper.selectMaxVersion(labelset.getType()) + 1);
        if (labelset.getState() == null || (!labelset.getState().equals(1) && !labelset.getState().equals(0))) {
            labelset.setState(1);
        }
        //复制标签体系下的标签
        List<LabelsetLabel> labelsetLabels = null;
        if(labelset.getId() != null) {
            labelsetLabels = labelsetMapper.selectLabelsetLabels(labelset.getId());
        }
        //复制体系环节下的标签
        List<ProcessTaskLabel> processTaskLabels = null;
        if(labelset.getId() != null) {
            processTaskLabels = processTaskMapper.selectProcessTaskLabelsByLabelsetId(labelset.getId());
        }
        //这里插入标签体系,id发生变化
        labelsetMapper.addLabelset(labelset);
        //粘贴标签体系下的标签
        if(labelsetLabels!=null && labelsetLabels.size()>0) {
            labelsetMapper.copyLabelsetLabels(labelsetLabels, labelset.getId());
        }
        //粘贴体系环节下的标签
        if(processTaskLabels!=null && processTaskLabels.size()>0) {
            processTaskMapper.copyProcessTaskLabels(processTaskLabels,labelset.getId());
        }
        return labelsetMapper.selectByPrimaryKey(labelset.getId());
    }

    @Transactional(rollbackFor=BussinessLogicException.class)
    @Override
    public Labelset updateLabelset(Labelset labelset, Long userId) throws LabelsetException {
        try {
            labelset.setUserId(userId);
            labelsetMapper.updateLabelset(labelset);

//            Labelset originalLabelset = labelsetMapper.selectByPrimaryKey(labelset.getId());

            List<LabelsetLabel> originalLabels = labelsetMapper.selectLabelsetLabels(labelset.getId());
            List<LabelsetLabel> labels = labelset.getLabelsetLabels();
            // 删除
            if(originalLabels != null && originalLabels.size() > 0) {
                for (LabelsetLabel label: originalLabels) {
                    if (!this.labelExists(label, labels) && label.getId() != null) {
                        this.labelsetMapper.removeLabelsetLabel(label.getId());
                    }
                }
            }
            // 增和修改
            for (LabelsetLabel label: labels) {
                if (this.labelExists(label, originalLabels)) {
                    this.labelsetMapper.updateLabelsetLabel(label);
                } else {
                    this.labelsetMapper.addLabelsetLabel(label);
                }
            }
        } catch (Exception e) {
            throw new LabelsetException(e, ResponseEnum.LABELSET_UPDATE_LABELS_ERROR);
        }
        return labelsetMapper.selectByPrimaryKey(labelset.getId());
    }

    @Transactional(rollbackFor=BussinessLogicException.class)
    @Override
    public Labelset updateLabelsetName(Labelset labelset, Long userId) throws LabelsetException {
        try {
            labelset.setUserId(userId);
            labelsetMapper.updateLabelset(labelset);
        } catch (Exception e) {
            throw new LabelsetException(e, ResponseEnum.LABELSET_UPDATE_LABELS_ERROR);
        }
        return labelsetMapper.selectByPrimaryKey(labelset.getId());
    }

    private boolean labelExists(LabelsetLabel label, List<LabelsetLabel> labels) {
        boolean exists = false;
        if (labels != null && labels.size() > 0) {
            for (LabelsetLabel l: labels) {
                if (l.getLabelId() == label.getLabelId()) {
                    exists = true;
                    break;
                }
            }
        }
        return exists;
    }

    @Transactional(rollbackFor=BussinessLogicException.class)
    @Override
    public void changeLabelsetState(long id, Integer state) throws LabelsetException {
        try {
            labelsetMapper.changeLabelsetState(id, state);
        } catch (Exception e) {
            throw new LabelsetException(e, ResponseEnum.LABELSET_UPDATE_STATE_ERROR);
        }
    }

    // 更新指定字段内容
    @Transactional(rollbackFor=BussinessLogicException.class)
    @Override
    public void changeLabelsetFieldValue(Long id, String field, Object value) throws LabelsetException {
        labelsetMapper.changeLabelsetFieldValue(id, field, value);
    }

    // 生成新版本
    @Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
    @Override
    public Labelset copyLabelset(Long id, long userId) throws LabelsetException {
        Labelset labelset = labelsetMapper.selectByPrimaryKey(id);

        if (labelset==null){
            throw new LabelsetException(ResponseEnum.LABELSET_NOTEXISTS);
        }

        List<LabelsetLabel> labels = new ArrayList<>();
        Collections.copy(labels, labelset.getLabelsetLabels());

        try {
            labelset.setId(null);
            labelset.setUserId(userId);
            labelset.setVersion(labelset.getVersion() + 1); // 升级版本号
            labelsetMapper.addLabelset(labelset);

            Long labelsetId = labelset.getId();
            labels.forEach(item -> {
                item.setId(null);
                item.setLabelsetId(labelsetId);
                item.setParentId(null); // 删除父节点，需要重新设置标签关系
            });

            labelsetMapper.addLabelsetLabels(labels);
        } catch (Exception e) {
            throw new LabelsetException(e, ResponseEnum.LABELSET_NOTEXISTS);
        }
        return labelsetMapper.selectByPrimaryKey(labelset.getId());
    }

    // 更新标签体系节点
    @Transactional(rollbackFor=BussinessLogicException.class)
    @Override
    public LabelsetLabel updateLabelsetLabel(LabelsetLabel labelsetLabel) throws LabelsetException {
        labelsetMapper.updateLabelsetLabel(labelsetLabel);
        return labelsetMapper.selectLabelsetLabel(labelsetLabel.getId());
    }
}
