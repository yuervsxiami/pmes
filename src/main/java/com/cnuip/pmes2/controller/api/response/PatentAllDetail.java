package com.cnuip.pmes2.controller.api.response;

import com.cnuip.pmes2.controller.api.request.PatentInfo;
import com.cnuip.pmes2.domain.core.Patent;
import com.cnuip.pmes2.domain.tkResultBean.QuoteData;
import com.cnuip.pmes2.domain.tkResultBean.SimilarityInfo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @auhor Crixalis
 * @date 2018/5/3 14:56
 */
@Data
public class PatentAllDetail{

	//专利属性
	private Patent patent;
	//专利标签
	private Map<String,String> labels;
	//专利说明
	private Map<String,String> evaluation;
	//申请人印证专利
	private List<QuoteData> sqryzzlList;
	//相似专利信息
	private List<SimilarityInfo> similarList;

}
