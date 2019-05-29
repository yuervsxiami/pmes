package com.cnuip.pmes2.service;

import com.cnuip.pmes2.domain.el.ElProfessor;
import com.github.pagehelper.PageInfo;

/**
 * 主要处理保存专家的业务逻辑
 * 可能包含关键词匹配专家,企业需求匹配专家,企业信息匹配专家,专利匹配专家
 * @auhor Crixalis
 * @date 2018/10/11 14:04
 */
public interface MatchProfessorService {

	/**
	 * 最简单的匹配,只输入一个关键词
	 * @return
	 */
	PageInfo<ElProfessor> simpleMatch(String keywords, int pageSize, int pageNum);

}
