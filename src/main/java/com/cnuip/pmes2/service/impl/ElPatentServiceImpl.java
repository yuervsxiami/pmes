package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.controller.api.request.PatentValueStatCondition;
import com.cnuip.pmes2.controller.api.response.PatentValueSpread;
import com.cnuip.pmes2.controller.api.response.StatItem;
import com.cnuip.pmes2.domain.core.ElPatent;
import com.cnuip.pmes2.domain.core.TaskOrderLabel;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.repository.core.*;
import com.cnuip.pmes2.repository.el.ElPatentRepository;
import com.cnuip.pmes2.service.ElPatentService;
import com.cnuip.pmes2.service.MongoService;
import com.cnuip.pmes2.service.PatentService;
import com.cnuip.pmes2.util.HtmlUtil;
import com.github.pagehelper.Page;
import com.google.common.base.Strings;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.avg;
import static org.elasticsearch.search.aggregations.AggregationBuilders.range;

/**
 * ElPatentServiceImpl
 *
 * @author: xiongwei
 * Date: 2018/2/7 下午3:21
 */
@Service
public class ElPatentServiceImpl implements ElPatentService {

    private final Logger logger = LoggerFactory.getLogger(ElPatentServiceImpl.class);

    @Autowired
    private ElPatentRepository elPatentRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private PatentMapper patentMapper;
    @Autowired
    private MongoService mongoService;
    @Autowired
    private TaskOrderLabelMapper taskOrderLabelMapper;
    @Autowired
    private PatentService patentService;
    @Autowired
    private ProcessOrderMapper processOrderMapper;
    @Autowired
    private LabelMapper labelMapper;
    @Autowired
    private InstanceLabelMapper instanceLabelMapper;

    @Override
    public ElPatent findLastUpdate() {
        ElPatent patent = null;
        try {
            List<ElPatent> patents = this.elPatentRepository.findByInstanceTypeOrderByUpdateTimeDesc(1, new PageRequest(0, 1));
            patent = patents.size() > 0 ? patents.get(0) : null;
        } catch(Exception e) {
            logger.error("查询最新更新的索引出错：", e);
        }
        return patent;
    }

    @Override
    public Iterable<ElPatent> findAll(Sort sort) {
        return this.elPatentRepository.findAll(sort);
    }

    @Override
    public Iterable<ElPatent> findAll() {
        return this.elPatentRepository.findAll();
    }

    @Override
    public org.springframework.data.domain.Page<ElPatent> findAll(Pageable pageable) {
        return this.elPatentRepository.findAll(pageable);
    }

    @Override
    public Iterable<ElPatent> save(Iterable<ElPatent> patents) {
        return this.elPatentRepository.save(patents);
    }

    @Override
    public void delete(ElPatent patent) {
        this.elPatentRepository.delete(patent);
    }

    @Override
    public void delete(Long id) {
        this.elPatentRepository.delete(id);
    }

    @Override
    public void deleteAll() {
        this.elPatentRepository.deleteAll();
    }

    @Override
    public void autoIndex() {
        int pages = 1;
        int size = 100;
        while (pages > 0 && size == 100) {
            ElPatent lastUpdatePatent = this.findLastUpdate();
            Page<ElPatent> page = (Page<ElPatent>) this.patentMapper.findUpdatedElPatents(lastUpdatePatent != null ? lastUpdatePatent.getUpdateTime() : null,
                    1, size);
            pages = page.getPages();
            size = page.size();
            if (size > 0) {
                List<ElPatent> patents = page.getResult();
                for (ElPatent elPatent: patents) {
                    // 读取最新的标签
                    elPatent.setLatestLabels(instanceLabelMapper.findAllPatentLabels(elPatent.getId()));
                    patentService.mergeWithBatchLabels(elPatent);
                    // 转化专利价值
                    if (!Strings.isNullOrEmpty(elPatent.getPatentValue())) {
                        elPatent.setValue(Double.valueOf(elPatent.getPatentValue()));
                    }
                    try {
                        if(elPatent.getLatestLabels()==null || elPatent.getLatestLabels().size()==0) {
                            patents.remove(elPatent);
                            break;
                        }
                        this.patentService.deleteIndexedPatentFastOrders(elPatent.getId());
                    } catch (BussinessLogicException e) {
                        logger.error("删除专利定单出错", e);
                    }
                    // 读取XML文件内容
                    String xmlContent = mongoService.readXmlContentBySysId(elPatent.getSysId());
                    elPatent.setDescription(HtmlUtil.parseHtmlToText(xmlContent));
                }
                this.elPatentRepository.save(patents);
            }
            logger.info("自动索引专利数量：" + size + " totalPages=" + pages);
        }

    }

    @Override
    public void batchIndexForSuffix(String suffix) {
        int count;
        int page = 1;
        int pageSize = 10000;
        do {
            List<String> patents = null;
            if (Strings.isNullOrEmpty(suffix)) {
                patents = this.patentMapper.findPatents4BatchIndexAll(pageSize * (page - 1), pageSize);
            } else {
                patents = this.patentMapper.findPatents4BatchIndex(suffix, pageSize * (page - 1), pageSize);
            }
            count = patents.size();
            if (count > 0) {
                for (String an: patents) {
//                    ElPatent patent = elPatentRepository.findByAn(an);
//                    if (patent != null && patent.getLatestLabels() != null && patent.getLatestLabels().size() > 0) {
//                        try {
//                            this.patentService.deleteIndexedPatentFastOrders(patent.getId());
//                            logger.info("专利[an=" + an + "] 定单删除成功！");
//                        } catch (BussinessLogicException e) {
//                            logger.error("删除专利定单出错", e);
//                        }
//                        logger.info("专利[an=" + an + "]已索引！");
//                        continue;
//                    }
//                    if (patent == null) {
//                        patent = patentMapper.findElPatentByAn(an);
//                    }
                    ElPatent patent = patentMapper.findElPatentByAn(an);
                    patent.setLatestLabels(taskOrderLabelMapper.findLatestProcessLabels(patent.getId(), 1));
                    // 读取XML文件内容
                    String xmlContent = mongoService.readXmlContentBySysId(patent.getSysId());
                    patent.setDescription(HtmlUtil.parseHtmlToText(xmlContent));
                    this.elPatentRepository.save(patent);
                    try {
                        this.patentService.deleteIndexedPatentFastOrders(patent.getId());
                        logger.info("专利[an=" + an + "] 定单删除成功！");
                    } catch (BussinessLogicException e) {
                        logger.error("删除专利定单出错", e);
                    }
                    logger.info("专利[an=" + an + "]索引完成！");
                }
            }
            logger.info("自动索引专利数量：" + count + " page=" + page);
            page++;
        } while (count > 0);
    }

    @Override
    public void batchIndexForMode(int mode) {
        int count;
        int pageSize = 10000;
        do {
            List<Long> patents = this.processOrderMapper.findBatchIndexPatentIdsWithMode(mode, 0, pageSize);
            count = patents.size();
            for (Long id: patents) {
                ElPatent patent = patentMapper.findElPatentById(id);
                patent.setLatestLabels(taskOrderLabelMapper.findLatestProcessLabels(patent.getId(), 1));
                // 读取XML文件内容
                String xmlContent = mongoService.readXmlContentBySysId(patent.getSysId());
                patent.setDescription(HtmlUtil.parseHtmlToText(xmlContent));
                // 转化专利价值
                if (!Strings.isNullOrEmpty(patent.getPatentValue())) {
                    patent.setValue(Double.valueOf(patent.getPatentValue()));
                }
                this.elPatentRepository.save(patent);
                try {
                    this.patentService.deleteIndexedPatentFastOrders(patent.getId());
                    logger.info("专利[an=" + patent.getAn() + "] 定单删除成功！");
                } catch (BussinessLogicException e) {
                    logger.error("删除专利定单出错", e);
                }
                logger.info("专利[an=" + patent.getAn() + "]索引完成！");
            }
            logger.info("自动索引专利[mode=" + mode + "]数量：" + count);
        } while (count > 0);
    }

    @Override
    public void batchFixIndexForMode(int mode) {
        int count;
        int pageSize = 10000;
        do {
            List<Long> patents = this.patentMapper.findPatent4(mode, 0, pageSize);
            count = patents.size();
            for (Long id: patents) {
                ElPatent patent = elPatentRepository.findById(id);
                if (patent != null && patent.getLastLegalStatus() != null) {
                    for (TaskOrderLabel label: patent.getLatestLabels()) {
                        if (label.getLabel().getId() == 260L) {
                            label.setStrValue("20");
                            label.getLabel().setName("保护年限");
                            break;
                        }
                    }
                    elPatentRepository.save(patent);
                }
                logger.info("专利[an=" + patent.getAn() + "]索引更新完成！");
            }
            logger.info("自动索引专利[mode=" + mode + "]数量：" + count);
        } while (count > 0);
    }

    @Override
    public org.springframework.data.domain.Page<ElPatent> searchByKeywords(String keywords, Pageable pageable) {

        QueryBuilder queryBuilder;
        if (!Strings.isNullOrEmpty(keywords)) {
            queryBuilder = QueryBuilders.boolQuery();
            BoolQueryBuilder boolQueryBuilder = ((BoolQueryBuilder)queryBuilder);
            boolQueryBuilder.should(QueryBuilders.matchQuery("keyword", keywords).analyzer("ik_max_word").boost(20));
            boolQueryBuilder.must(QueryBuilders.matchQuery("ti", keywords).analyzer("ik_max_word").boost(6));
            boolQueryBuilder.must(QueryBuilders.matchQuery("ab", keywords).analyzer("ik_max_word").boost(3));
            boolQueryBuilder.should(QueryBuilders.matchPhraseQuery("sic", keywords).analyzer("ik_smart").boost(3));
            boolQueryBuilder.should(QueryBuilders.matchQuery("cl", keywords).analyzer("ik_max_word").boost(0.5f));
            boolQueryBuilder.should(QueryBuilders.matchQuery("independentItem", keywords).analyzer("ik_max_word").boost(0.2f));
        } else {
            queryBuilder = matchAllQuery();
        }
        logger.info("searching.boolQuery=" + queryBuilder.toString());
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(pageable)
                .build();
        org.springframework.data.domain.Page<ElPatent> page = elasticsearchTemplate.queryForPage(searchQuery, ElPatent.class);
        logger.info("searching.result.size()=" + page.getTotalElements());
        return page;
    }

    @Override
    public void batchUpdatePatentValue() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.mustNot(QueryBuilders.existsQuery("value"));
        boolQueryBuilder.must(QueryBuilders.existsQuery("patentValue"));
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withIndices("pmes")
                .withTypes("patent")
                .withPageable(new PageRequest(0,500))
                .build();
        org.springframework.data.domain.Page<ElPatent> page = elasticsearchTemplate.queryForPage(searchQuery, ElPatent.class);
        while (page.getContent().size() > 0) {
            for (ElPatent patent: page.getContent()) {
                // 转化专利价值
                if (!Strings.isNullOrEmpty(patent.getPatentValue())) {
                    patent.setValue(Double.valueOf(patent.getPatentValue()));
                }
            }
            this.elPatentRepository.save(page);
            logger.info("更新专利价值数量：" + page);
            // re search
            page = elasticsearchTemplate.queryForPage(searchQuery, ElPatent.class);
        }
    }

    @Override
    public PatentValueSpread statPatentValue(PatentValueStatCondition condition) {
        BoolQueryBuilder queryBuilder = makeQueryBuilder(condition);
        // given
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withSearchType(SearchType.DEFAULT)
                .withIndices("pmes").withTypes("patent")
                .addAggregation(avg("avg_value").field("value"))
                .addAggregation(range("range_value").field("value")
                        .addRange("0~50", 0, 0.5)
                        .addRange("50~55",0.5, 0.55)
                        .addRange("55~60",0.55, 0.60)
                        .addRange("60~65",0.60, 0.65)
                        .addRange("65~70",0.65, 0.70)
                        .addRange("70~75",0.70, 0.75)
                        .addRange("75~80",0.75, 0.80)
                        .addRange("80~85",0.80, 0.85)
                        .addRange("85~100",0.85, 1.0)
                )
                .build();
        // 取值
        PatentValueSpread spread = new PatentValueSpread();
        // when
        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                spread.setTotalHits(response.getHits().totalHits());
                return response.getAggregations();
            }
        });
        Avg avg = aggregations.get("avg_value");
        spread.setAvg(avg.getValue());
        if (spread.getAvg() == Double.NaN) {
            spread.setAvg(.0);
        }
        Range range = aggregations.get("range_value");
        List<StatItem<Long>> ranges = new ArrayList<>();
        for (Range.Bucket bucket: range.getBuckets()) {
            StatItem<Long> item = new StatItem<>();
            item.setName(bucket.getKeyAsString());
            item.setValue(bucket.getDocCount());
            ranges.add(item);
        }
        spread.setRanges(ranges);
        return spread;
    }

    @Override
    public double statPatentLabelCoverage(PatentValueStatCondition condition, String labelKey) {
        BoolQueryBuilder queryBuilder = makeQueryBuilder(condition);
        // given
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withSearchType(SearchType.DEFAULT)
                .withIndices("pmes").withTypes("patent")
                .build();
        // when
        long totalHints = elasticsearchTemplate.count(searchQuery);
        BoolQueryBuilder labelKeyQueryBuilder = QueryBuilders.boolQuery();
        labelKeyQueryBuilder.must(QueryBuilders.matchPhraseQuery("label.key", labelKey));
        labelKeyQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery("strValue", ""));
        queryBuilder.must(QueryBuilders.nestedQuery("latestLabels", labelKeyQueryBuilder));
        // given
        SearchQuery labelKeySearchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withSearchType(SearchType.DEFAULT)
                .withIndices("pmes").withTypes("patent")
                .build();
        // when
        long validHint = elasticsearchTemplate.count(labelKeySearchQuery);
        return totalHints == 0 ? .0 : (validHint / totalHints);
    }

    private BoolQueryBuilder makeQueryBuilder(PatentValueStatCondition condition) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // 有效和在审
        BoolQueryBuilder statusQueryBuilder = QueryBuilders.boolQuery();
        statusQueryBuilder.should(QueryBuilders.matchPhraseQuery("lastLegalStatus", "有效"));
        statusQueryBuilder.should(QueryBuilders.matchPhraseQuery("lastLegalStatus", "在审"));
        queryBuilder.must(statusQueryBuilder);
        // 专利类型
        if (condition.getPatentType() != null) {
            queryBuilder.must(QueryBuilders.termQuery("type", condition.getPatentType().getValue()));
        }
        // 发明人
        if (!Strings.isNullOrEmpty(condition.getPin())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("pin", condition.getPin()));
        }
        // 权利人
        if (!Strings.isNullOrEmpty(condition.getPa())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("pa", condition.getPa()));
        }
        // 分类号
        if (!Strings.isNullOrEmpty(condition.getSic())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("sic", condition.getSic()));
        }
        if (condition.getFromADate() != null || condition.getToADate() != null) {
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("ad");
            if (condition.getFromADate() != null) {
                rangeQueryBuilder.gte(condition.getFromADate().getTime());
            }
            if (condition.getToADate() != null) {
                rangeQueryBuilder.lte(condition.getToADate().getTime());
            }
            queryBuilder.must(rangeQueryBuilder);
        }
        if (condition.getFromODate() != null || condition.getToODate() != null) {
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("od");
            if (condition.getFromODate() != null) {
                rangeQueryBuilder.gte(condition.getFromODate().getTime());
            }
            if (condition.getToODate() != null) {
                rangeQueryBuilder.lte(condition.getToODate().getTime());
            }
            queryBuilder.must(rangeQueryBuilder);
        }
        return queryBuilder;
    }

}
