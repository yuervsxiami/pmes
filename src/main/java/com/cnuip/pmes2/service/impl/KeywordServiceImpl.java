package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.controller.api.response.KeywordAndRank;
import com.cnuip.pmes2.domain.core.EnterpriseRequire;
import com.cnuip.pmes2.domain.core.PatentXml;
import com.cnuip.pmes2.domain.core.RequirementProfessor;
import com.cnuip.pmes2.domain.el.ElProfessor;
import com.cnuip.pmes2.repository.core.EnterpriseRequireMapper;
import com.cnuip.pmes2.repository.core.RequirementProfessorMapper;
import com.cnuip.pmes2.repository.el.ElProfessorRepository;
import com.cnuip.pmes2.resolver.LocalDtdResolver;
import com.cnuip.pmes2.service.EnterpriseRequireService;
import com.cnuip.pmes2.service.KeywordService;
import com.cnuip.pmes2.util.XmlUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.hankcs.hanlp.algorithm.MaxHeap;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.summary.TextRankKeyword;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import com.mongodb.gridfs.GridFSDBFile;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class KeywordServiceImpl implements KeywordService {
    private ElProfessorRepository elProfessorRepository;
    private RequirementProfessorMapper requirementProfessorMapper;
    private EnterpriseRequireMapper enterpriseRequireMapper;
    private AmqpTemplate amqpTemplate;
    private EnterpriseRequireService enterpriseRequireService;
    private Map<String, String> blacklistMap;
	private GridFsOperations gridFsOperations;
	private RestTemplate restTemplate;

	private Pattern labelPattern = Pattern.compile("</?[^>]+>", Pattern.CASE_INSENSITIVE);
	private Pattern symbolPattern = Pattern.compile("\\s*|\t|\r|\n");

    public KeywordServiceImpl(ElProfessorRepository elProfessorRepository, RequirementProfessorMapper requirementProfessorMapper, EnterpriseRequireMapper enterpriseRequireMapper, AmqpTemplate amqpTemplate, EnterpriseRequireService enterpriseRequireService,
							  GridFsOperations gridFsOperations,RestTemplate restTemplate) {
        this.elProfessorRepository = elProfessorRepository;
        this.requirementProfessorMapper = requirementProfessorMapper;
        this.enterpriseRequireMapper = enterpriseRequireMapper;
        this.amqpTemplate = amqpTemplate;
        this.enterpriseRequireService = enterpriseRequireService;
        this.gridFsOperations = gridFsOperations;
        this.restTemplate=restTemplate;
    }

    @Value("${service-pmes.url}")
    private String url;

    @Override
    public PageInfo<ElProfessor> match(String expression, int pageNum, int pageSize) {
        Pageable pageable = new PageRequest(pageNum, pageSize);
        SearchQuery query = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(QueryBuilders.queryStringQuery(expression).defaultField("keyWords"))
                .withHighlightFields(
                        new HighlightBuilder.Field("keyWords")
                                .preTags("<span class=\"hightlight\">")
                                .postTags("</span>")
                )
                .build();
        return changePageToPageInfo(elProfessorRepository.search(query));
    }

    @Override
    public List<KeywordAndRank> content(String text, int topn) {
//        try {
//            String exp = "https://www.tiikong.com/api/patentInterfaceList/getContentKeyWordSearch.do?token=BRzz3J0ld4Ra8mqu&text="
//                    + URLEncoder.encode(text, "utf-8") + "&topn=" + topn;
//            return Arrays.asList(getForUrl(exp));
//        }  catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
        return extractKeyword(text, topn);
    }

    @Override
    public List<KeywordAndRank> extractKeyword(String text, int topn) {
        // 1.根据分词器对语段进行分词
        List<Term> termList = StandardTokenizer.segment(text);
        // 2.对分词结果进行词性过滤和长短
        List<Term> filteredList = filterTermPos(termList);
        // 3.把过滤后的分词结果进行投票打分
//        List<KeywordAndRank> keywordList = new TextRankKeyword().getKeywords(filteredList, topn * 2);
        List<KeywordAndRank> keywordList = getKeywordAndRank(filteredList, topn * 2);
        // 4.用户自定义过滤
        List<KeywordAndRank> resultList = customFilter(topn, keywordList);
        return resultList;
    }

    /**
     * 把分词结果投票打分
     * @param termList
     * @param topn
     * @return
     */
    private List<KeywordAndRank> getKeywordAndRank(List<Term> termList, int topn) {
        TextRankKeyword textRankKeyword = new TextRankKeyword();
        Set<Map.Entry<String, Float>> entrySet = this.top(topn, textRankKeyword.getTermAndRank(termList)).entrySet();
        Iterator var5 = entrySet.iterator();
        List<KeywordAndRank> keywordList = new ArrayList<>(topn);
        while(var5.hasNext()) {
            Map.Entry<String, Float> entry = (Map.Entry)var5.next();
            keywordList.add(new KeywordAndRank(entry.getKey(), entry.getValue()));
        }
        return keywordList;
    }

    /**
     * 给关键字打分排序
     * @param size
     * @param map
     * @return
     */
    private Map<String, Float> top(int size, Map<String, Float> map) {
        Map<String, Float> result = new LinkedHashMap();
        Iterator<Map.Entry<String, Float>> var4 = (new MaxHeap<Map.Entry<String, Float>>(size, Comparator.comparing(o -> ((Float) o.getValue())))).addAll(map.entrySet()).toList().iterator();

        while(var4.hasNext()) {
            Map.Entry<String, Float> entry = var4.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }


    /**
     * 用户自定义过滤器
     * 根据黑名单来过滤关键词,直到满足用户需求
     * @param topn
     * @param keywordList
     * @return
     */
    private List<KeywordAndRank> customFilter(int topn, List<KeywordAndRank> keywordList) {
        //加载黑名单map
        fixBlacklistMap();
        List<KeywordAndRank> resultList = new ArrayList<>(topn);
        int count = 0;
        for(KeywordAndRank keyword: keywordList) {
            // 如果黑名单里面有这个关键词则不进行返回
            if(blacklistMap.containsKey(keyword.getKeyword())) {
                continue;
            }
            resultList.add(keyword);
            if(++count >= topn) {
                break;
            }
        }
        return resultList;
    }

    /**
     * 只保留词性为n,v,a,d,g开头的token, 并且过滤掉长度为1的token
     * @param termList
     */
    private List<Term> filterTermPos(List<Term> termList) {
        List<Term> filteredList = new ArrayList<>(termList.size());
        for (Term term : termList) {
            if ((term.nature.startsWith("n") || term.nature.startsWith("v") || term.nature.startsWith("a") ||
                term.nature.startsWith("d") || term.nature.startsWith("g")) && term.word.length() >= 2) {
                filteredList.add(term);
            }
        }
        return filteredList;
    }

    @Override
    public String correlation(String keywords, int topn) {
        try {
            String exp = "https://www.tiikong.com/api/patentInterfaceList/getCorrelationKeyWordSearch.do?token=BRzz3J0ld4Ra8mqu&keywords="
                    + URLEncoder.encode(keywords, "utf-8") + "&topn=" + topn;
            return getForUrl(exp);
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加载黑名单map
     */
    public void fixBlacklistMap() {
        if(blacklistMap != null) {
            return;
        }
        blacklistMap = new HashMap<>();
        BufferedReader br = null;
        int maxWhiteLine = 5;
        try {
            File file = ResourceUtils.getFile("classpath:data/blacklist.txt");
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line = br.readLine(); // 读取第一行
            while (line != null) {
                // 如果字符串不为空,则缓存入黑名单文件
                if(line != null && !line.trim().isEmpty()) {
                    blacklistMap.put(line, " ");
                } else {
                    maxWhiteLine--;
                }
                if(maxWhiteLine <= 0) {
                    break;
                }
                // 如果 line 为空说明读完了
                line = br.readLine(); // 读取下一行
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void saveProfessor(List<RequirementProfessor> professors) {
        if (!professors.isEmpty()) {
            requirementProfessorMapper.insert(professors);
            long requirementId = professors.get(0).getRequirementId();
            enterpriseRequireMapper.updatePushState(requirementId);
            EnterpriseRequire enterpriseRequire = enterpriseRequireMapper.findEnterpriseRequireById(requirementId);
            HashMap<String, Object> map = new HashMap<>();
            map.put("enterpriseRequire", enterpriseRequire);
            map.put("professors", professors);
            try {
                restTemplate.put(url,new ObjectMapper().writeValueAsString(map));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    private PageInfo changePageToPageInfo(Page page) {
        PageInfo pageInfo = new PageInfo<>();
        pageInfo.setList(page.getContent());
        pageInfo.setTotal(page.getTotalElements());
        pageInfo.setPages(page.getTotalPages());
        return pageInfo;
    }

    @Override
    public PageInfo<RequirementProfessor> findByRqId(long rqId, int pageNum, int pageSize) {
        com.github.pagehelper.Page<RequirementProfessor> page = (com.github.pagehelper.Page<RequirementProfessor>) requirementProfessorMapper.findByRqId(rqId, pageNum, pageSize);
        return page.toPageInfo();
    }

    @Override
    public List<KeywordAndRank> extardProfessorKeyword(String collegeName, String name) {
    	// 1.获取所有专家的专利
		List<Map> detail_collegePatent_pin = enterpriseRequireService.getDetailCollegePatentPin(collegeName, name);
		List<PatentXml> patentXmls = new ArrayList<>(detail_collegePatent_pin.size());
		detail_collegePatent_pin.forEach(map -> {
			String patType = (String)map.get("patType");
			// 如果不是外观专利才进行
			if( patType != null && !patType.equals("WGZL")) {
				GridFSDBFile file = this.gridFsOperations.findOne(new Query().addCriteria(Criteria.where("_id").is(map.get("sysid"))));
				PatentXml patentXml = analysisXml(file);
				if(patentXml!=null) {
					patentXmls.add(patentXml);
				}
			}
		});
		StringBuilder sb = new StringBuilder();
		patentXmls.forEach(patentXml -> sb.append(patentXml.toKeywordContent()));
        return extractKeyword(sb.toString(), 200);
    }

    private PatentXml analysisXml(GridFSDBFile file) {
		InputStream fin = null;
		try {
			SAXReader sax = new SAXReader();
			sax.setEntityResolver(new LocalDtdResolver());
			fin = file.getInputStream();
			Document doc = sax.read(fin);
			PatentXml patentXml = new PatentXml();
			patentXml.setType(XmlUtil.selectElement(doc, "/cn-patent-document/cn-bibliographic-data/application-reference").attributeValue("appl-type"));
			patentXml.setName(XmlUtil.selectElement(doc, "/cn-patent-document/cn-bibliographic-data/invention-title").getText());
			patentXml.setClaims(filterSymbol(filterLabel(XmlUtil.selectNodeText(doc, "/cn-patent-document/application-body/claims/claim/claim-text"))));
			patentXml.setDescription(filterSymbol(filterLabel(XmlUtil.selectNodeText(doc, "/cn-patent-document/application-body/description/p"))));
			patentXml.setAb(XmlUtil.selectElement(doc, "/cn-patent-document/cn-bibliographic-data/abstract/p").getText());
			return patentXml;
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			if(fin != null) {
				try {
					fin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

    private String getForUrl(String exp) throws Exception {
        URL url;
        InputStream inputStream = null;
        try {
            url = new URL(exp);
            inputStream = url.openStream();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            return new String(output.toByteArray(), "utf-8");
        }  finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

	/**
	 * 过滤文本中<></>标签
	 * @param content
	 * @return
	 */
	public String filterLabel(String content) {
		return labelPattern.matcher(content).replaceAll("");
	}

	/**
	 * 过滤文本中\n \t \r 等符号
	 * @param content
	 * @return
	 */
	public String filterSymbol(String content) {
		return symbolPattern.matcher(content).replaceAll("");
	}
}
