package com.cnuip.pmes2.service.impl;

import java.util.List;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnuip.pmes2.domain.core.Patent;
import com.cnuip.pmes2.service.EnterpriseRequireMatchService;
import com.github.pagehelper.PageInfo;

/**
* Create By Crixalis:
* 2018年2月7日 下午4:10:13
*/
@Service
@Transactional(readOnly=true)
public class EnterpriseRequireMatchServiceImpl implements EnterpriseRequireMatchService {

//    @Autowired
////    private PatentSearchService patentSearchService;
    
//	@Override
//	public PageInfo<Patent> match(String searchContent, int pageNum, int pageSize) {
//    	  // 分页参数
//        Pageable pageable = new PageRequest(pageNum - 1, pageSize);
//
//        // Function Score Query
//        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery()
//                .add(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("ti", searchContent)),
//                    ScoreFunctionBuilders.weightFactorFunction(50))
//                .add(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("cl", searchContent)),
//                		ScoreFunctionBuilders.weightFactorFunction(50))
//                .add(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("keyword", searchContent)),
//                		ScoreFunctionBuilders.weightFactorFunction(100))
//                .add(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("sic", searchContent)),
//                		ScoreFunctionBuilders.weightFactorFunction(100))
//                .add(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("ab", searchContent)),
//                        ScoreFunctionBuilders.weightFactorFunction(100)
//                );
//
//        // 创建搜索 DSL 查询
//        SearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withPageable(pageable)
//                .withQuery(functionScoreQueryBuilder)
//                .build();
//
//        Page<Patent> searchPageResults = patentSearchService.search(searchQuery);
//        List<Patent> patents = searchPageResults.getContent();
//        PageInfo<Patent> pageInfo = new PageInfo<Patent>();
//        pageInfo.setList(patents);
//        pageInfo.setPageNum(pageNum);
//        pageInfo.setPages(pageSize);
//        pageInfo.setTotal(searchPageResults.getTotalElements());
//		return pageInfo;
//	}

}
