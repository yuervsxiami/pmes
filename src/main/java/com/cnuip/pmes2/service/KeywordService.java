package com.cnuip.pmes2.service;

import com.cnuip.pmes2.controller.api.response.KeywordAndRank;
import com.cnuip.pmes2.domain.core.RequirementProfessor;
import com.cnuip.pmes2.domain.el.ElProfessor;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface KeywordService {
    PageInfo<ElProfessor> match(String expression, int pageNum, int pageSize);

    List<KeywordAndRank> content(String text, int topn);

    /**
     * 根据语段提取关键词
     * 1.根据分词器对语段进行分词;
     * 2.对分词结果进行词性过滤;
     * 3.把过滤后的分词结果进行投票打分;
     * 4.用户自定义过滤
     * @param text
     * @param topn
     * @return
     */
    List<KeywordAndRank> extractKeyword(String text, int topn);

    String correlation(String keywords, int topn);

    void saveProfessor(List<RequirementProfessor> professors);

    PageInfo<RequirementProfessor> findByRqId(long rqId, int pageNum, int pageSize);

    /**
     * 提取专家关键词
     * @param collegeName
     * @param name
     * @return
     */
    List<KeywordAndRank> extardProfessorKeyword(String collegeName, String name);

}
