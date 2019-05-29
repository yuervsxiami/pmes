package com.cnuip.pmes2.config;

import com.cnuip.pmes2.domain.el.Highlightable;
import com.cnuip.pmes2.domain.el.Scoreable;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.DefaultResultMapper;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.convert.MappingElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

/**
 * @auhor Crixalis
 * @date 2018/10/11 13:52
 */
@Component
public class ElTemplateConfig {

	@Bean
	public ElasticsearchTemplate elasticsearchTemplate(Client client) throws Exception {
		MappingElasticsearchConverter converter = new MappingElasticsearchConverter(new SimpleElasticsearchMappingContext());
		MatchResultsMapper mapper = new MatchResultsMapper(converter.getMappingContext());
		return new ElasticsearchTemplate(client, converter, mapper);
	};

	/**
	 * 对获取的结果进行二次加工
	 */
	class MatchResultsMapper extends DefaultResultMapper {
		public MatchResultsMapper(MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> mappingContext) {
			super(mappingContext);
		}

		@Override
		public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
			AggregatedPage<T> resultPage = super.mapResults(response, clazz, pageable);
			Iterator<T> it = resultPage.getContent().iterator();
			for (SearchHit hit : response.getHits()) {
				if (hit != null) {
					T next = it.next();
					// 如果实例需要获取得分
					if (next instanceof Scoreable) {
						dealScoreableObject(hit, (Scoreable) next);
					}
					// 如果实例需要高亮
					if(next instanceof Highlightable) {
						dealHightlightableObject(hit, next);
					}
				}
			}
			return resultPage;
		}

		/**
		 * 处理需要获取得分的对象
		 * @param hit
		 * @param next
		 * @param <T>
		 */
		private <T> void dealScoreableObject(SearchHit hit, Scoreable next) {
			next.setScore(hit.score());
		}

		/**
		 * 处理需要高亮的实例,将被高亮处理过的字段替换为高亮处理后的结果
		 * @param hit
		 * @param next
		 * @param <T>
		 */
		private <T> void dealHightlightableObject(SearchHit hit, T next) {
			Class claz = next.getClass(); // 实例类型
			Field[] fields = claz.getDeclaredFields(); // 实例内部属性
			Map<String, HighlightField> highlightFieldMap =  hit.getHighlightFields(); // 已高亮处理过的map
			for(String key: highlightFieldMap.keySet() ) {
				HighlightField highlightField = highlightFieldMap.get(key);
				for(Field field: fields) {
					// 如果属性名称相同,则把处理过的属性赋值
					if(field.getName().equals(highlightField.getName())) {
						Method method = null;
						try {
							method = claz.getMethod("set" + toUpperCaseFirstOne(highlightField.getName()), String.class);
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
							break;
						}
						try {
							StringBuilder fragment = new StringBuilder();
							for(Text frag: highlightField.getFragments()) {
								fragment.append(frag.toString());
							}
							method.invoke(next, fragment.toString());
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		private String toUpperCaseFirstOne(String s){
			if(Character.isUpperCase(s.charAt(0)))
				return s;
			else
				return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
		}

	}

}
