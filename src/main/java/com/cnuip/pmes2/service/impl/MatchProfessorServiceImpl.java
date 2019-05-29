package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.config.SystemProperties;
import com.cnuip.pmes2.domain.el.ElProfessor;
import com.cnuip.pmes2.repository.el.ElProfessorRepository;
import com.cnuip.pmes2.service.MatchProfessorService;
import com.github.pagehelper.PageInfo;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 主要处理保存专家的业务逻辑
 * 可能包含关键词匹配专家,企业需求匹配专家,企业信息匹配专家,专利匹配专家
 * @auhor Crixalis
 * @date 2018/10/11 14:04
 */
@Service
@Transactional(readOnly = true)
public class MatchProfessorServiceImpl implements MatchProfessorService{

	@Autowired
	private ElProfessorRepository elProfessorRepository;

	@Autowired
	private SystemProperties systemProperties;

	@Override
	public PageInfo<ElProfessor> simpleMatch(String keywords, int pageSize, int pageNum) {
		Pageable pageable = new PageRequest(pageNum, pageSize);
		MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("keyWords", keywords).analyzer("ik_max_word");
		return match(pageable, matchQueryBuilder);
	}

	/**
	 * 进行搜索
	 * @param pageable 分页参数
	 * @param queryBuilder 搜索条件
	 * @return
	 */
	public PageInfo<ElProfessor> match(Pageable pageable, QueryBuilder queryBuilder) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withPageable(pageable)
				.withQuery(queryBuilder)
				.withHighlightFields(new HighlightBuilder.Field("keyWords")
						.preTags(systemProperties.getHighlightProperty().getPre())
						.postTags(systemProperties.getHighlightProperty().getPost()))
				.build();
		Page<ElProfessor> elProfessorPage = elProfessorRepository.search(searchQuery);
		return changePageToPageInfo(elProfessorPage);

	}

	/**
	 * 把keywords转化为boolQuery
	 * @param keywords
	 * @return
	 */
	private BoolQueryBuilder makeBoolQueryBuilder(String... keywords) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		for(String keyword: keywords) {
			boolQueryBuilder.should(QueryBuilders.matchQuery("keyWords", keyword));
		}
		return boolQueryBuilder;
	}

	private PageInfo changePageToPageInfo(Page page) {
		PageInfo pageInfo = new PageInfo<>();
		pageInfo.setList(page.getContent());
		pageInfo.setTotal(page.getTotalElements());
		pageInfo.setPages(page.getTotalPages());
		return pageInfo;
	}

}
