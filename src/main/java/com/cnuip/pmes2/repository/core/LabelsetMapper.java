package com.cnuip.pmes2.repository.core;

import com.cnuip.pmes2.controller.api.request.LabelsetSearchCondition;
import com.cnuip.pmes2.domain.core.Labelset;
import com.cnuip.pmes2.domain.core.LabelsetLabel;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wangzhibin on 2018/1/11.
 */
@Repository
public interface LabelsetMapper {

    int selectMaxVersion(Integer type);

	Labelset selectSimpleByPrimaryKey(Long id);
	
    Labelset selectByPrimaryKey(Long id);
    
    Labelset selectByProcessTaskId(Long processTaskId);

    List<Labelset> selectAll(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

    List<Labelset> selectByType(@Param("type") Integer type, @Param("pageNum") int pageNum, @Param("pageSize") int pageSize);
    
    Labelset selectAliveByType(@Param("type") Integer type);
    
    List<LabelsetLabel> selectValueSystem(Long labelsetId);
    
    List<LabelsetLabel> selectLabelsetLabels(Long id);

    LabelsetLabel selectParentLabelsetLabel(Long parentId);

    List<LabelsetLabel> selectChildLabelsetLabels(Long id);

    LabelsetLabel selectLabelsetLabel(Long id);

    int addLabelset(Labelset Labelset);

    int addLabelsetLabel(LabelsetLabel LabelsetLabel);

    @Delete("delete from p_labelset_label where id = #{id}")
    int removeLabelsetLabel(Long id);

    int addLabelsetLabels(List<LabelsetLabel> LabelsetLabels);

    int copyLabelsetLabels(@Param("labelsetLabels") List<LabelsetLabel> labelsetLabels, @Param("labelSetId") Long labelsetId);

    int updateLabelset(Labelset Labelset);

    /**
     * 修改标签
     * @param LabelsetLabel
     * @return
     */
    int updateLabelsetLabel(LabelsetLabel LabelsetLabel);

    /**
     * 移除所有标签
     * @param id
     * @return
     */
    int removeLabelsetLabels(Long id);

    int changeLabelsetState(@Param("id") long id, @Param("state") Integer state);

    List<Labelset> search(@Param("condition") LabelsetSearchCondition condition,
                          @Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

    List<Labelset> findByType(@Param("type") Integer type);

    int changeLabelsetFieldValue(@Param("id") long id, @Param("field") String field, @Param("value") Object value);

}
