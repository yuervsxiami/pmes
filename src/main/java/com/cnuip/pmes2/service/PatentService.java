package com.cnuip.pmes2.service;

import com.cnuip.pmes2.controller.api.response.PatentAllDetail;
import com.cnuip.pmes2.controller.api.response.PatentStatusInfo;
import com.cnuip.pmes2.domain.core.ElPatent;
import com.cnuip.pmes2.domain.core.IndexEr;
import com.cnuip.pmes2.domain.core.Patent;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.github.pagehelper.PageInfo;
import org.springframework.cache.annotation.Cacheable;

import java.util.Date;
import java.util.List;

/**
 * PatentService
 *
 * @author: xiongwei
 * Date: 2018/2/7 下午1:49
 */
public interface PatentService {

    Patent findByAn(String an);

    /**
     * 根据最新更新时间查询需要索引的专利
     * @param lastUpdate
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<ElPatent> findUpdatedElPatents(Date lastUpdate, int pageNum, int pageSize);

    /**
     * 根据id查询专利详情以及最新的标签
     * @param id
     * @return
     */
    Patent findPatentByIdWithFullLabels(Long id);

    /**
     * 根据id查询专利详情以及最新的人工标签
     * @param id
     * @return
     */
    Patent findPatentByIdWithManualLabels(Long id);

    Patent selectSimpleByPrimaryKey(Long id);

    /**
     * 根据申请号查询专利详情以及最新的标签
     * @param an
     * @return
     */
    Patent findPatentByAnWithFullLabels(String an);

    void mergeWithBatchLabels(Patent patent);

    /**
     * 删除已索引专利的定单与工单数据
     * @param id
     */
    void deleteIndexedPatentFastOrders(Long id) throws BussinessLogicException;

    PageInfo<IndexEr> selectIndexError(int pageSize, int pageNum);

    /**
     * 标记专利有更新
     * @param id
     * @return
     */
    int updateHasBatchIndexed(Long id);

    PatentAllDetail findPatentDetail(String an);

	String findPatentValue(String an);

    /**
     * 查找专利信息(法律,质押保全,实施许可,转让,评分)
     * @param an
     * @return
     */
    @Cacheable(value="statusInfos", key = "#an")
	PatentStatusInfo findPatentStatusInfo(String an);

    /**
     * 查找专利价值
     * @param anList
     * @return
     */
    List<Patent> findPatentValueList(List<String> anList);
}
