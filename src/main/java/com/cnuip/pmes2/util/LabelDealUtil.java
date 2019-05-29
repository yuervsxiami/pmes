package com.cnuip.pmes2.util;

import com.cnuip.pmes2.domain.core.Label;
import com.cnuip.pmes2.domain.core.LabelsetLabel;
import com.cnuip.pmes2.domain.core.Patent;
import com.cnuip.pmes2.domain.core.TaskOrderLabel;
import com.cnuip.pmes2.repository.core.OriginalPatentMapper;
import com.cnuip.pmes2.repository.core.PatentMapper;
import com.cnuip.pmes2.repository.core.TaskOrderLabelMapper;
import com.cnuip.pmes2.service.CNIPRService;
import com.google.common.base.Strings;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;
import org.joda.time.Years;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.util.*;

public class LabelDealUtil {
	
	private Logger logger = LoggerFactory.getLogger(LabelDealUtil.class);

	private PatentMapper patentMapper;

	private TaskOrderLabelMapper taskOrderLabelMapper;

	private CNIPRService cNIPRService;

	private Patent patent;

	private Map<String, String> values;

	private String an;

	private boolean saveImmediately;

	private boolean isBatch;
	
	private Long taskOrderId;
	
	private OriginalPatentMapper originalPatentMapper;

	public String makeValue(Label label) {
		String result = findValue(label);
		if (result != null) {
			values.put(label.getKey(), result);
//			if (saveImmediately && taskOrderId != null) {
//				TaskOrderLabel taskOrderLabel = makeTaskOrder(label, result);
//				taskOrderLabelMapper.insert(taskOrderLabel);
//			}
		}
//		logger.info(label.getKey() + ":" + result);
		return result != null ? result.trim() : null;
	}
	
	public String makeSystemValue(LabelsetLabel labelsetLabel) {
		Double result = null;
		if(labelsetLabel != null && labelsetLabel.getType().equals(1)) {
			result = 0.0;
			if(labelsetLabel.getChildren() != null && labelsetLabel.getChildren().size() > 0) {
				for(LabelsetLabel child: labelsetLabel.getChildren()) {
					Double value = Double.parseDouble(makeSystemValue(child));
					if("economicValue".equals(labelsetLabel.getLabel().getKey()) 
						&& "patentPortfolio".equals(child.getLabel().getKey())) {
						if(value.equals(0.0)) {
							result += 0.2;
							continue;
						}
					}
					result += child.getWeight() * value;
				}
				result = Math.round(result * 100) / 10000.0;
			}
		}
		if(labelsetLabel != null && labelsetLabel.getType().equals(0)) {
			return get(labelsetLabel.getLabel().getKey());
		}
		if (result != null) {
			values.put(labelsetLabel.getLabel().getKey(), result + "");
			if (saveImmediately && taskOrderId != null) {
				TaskOrderLabel taskOrderLabel = makeTaskOrder(labelsetLabel.getLabel(), result + "");
				taskOrderLabelMapper.insert(taskOrderLabel);
				if("patentValue".equals(labelsetLabel.getLabel().getKey()) && this.an != null) {
					patentMapper.updatePatentValue(this.an, result + "");
				}
			}
		}
		return result + "";
	}
	
	private TaskOrderLabel makeTaskOrder(Label label, String result) {
		TaskOrderLabel taskOrderLabel = new TaskOrderLabel();
		taskOrderLabel.setTaskOrderId(this.taskOrderId);
		taskOrderLabel.setLabelId(label.getId());
		if (label.getValueType().equals(2)) {
			taskOrderLabel.setTextValue(result);
		} else {
			taskOrderLabel.setStrValue(result);
		}
		return taskOrderLabel;
	}

	private String findValue(Label label) {
		if (label == null) {
			return null;
		}
		switch (label.getIndexType()) {
		case 5:
			return label.getDefaultValue();
		case 1:
		case 2:
		case 3:
		case 4:
			switch (label.getSource() + "") {
			case "1":
				return getZhongGaoAutoLabel(label);
			case "3":
				return getPublishHouseAutoLabel(label);
			default:
				if(label.getDefaultValue()!=null) {
					return label.getDefaultValue();
				}
				switch (label.getValueType()) {
				case 1:
				case 2:
				case 4:
					return "";
				case 3:
					return System.currentTimeMillis() + "";
				case 5:
					return "0";
				case 6:
					return "false";
				}
				return "";
			}
		}
		return null;
	}

	/**
	 * 根据标签mapType判断怎么获取出版社提供的标签值
	 * 
	 * @param label
	 * @return
	 */
	private String getPublishHouseAutoLabel(Label label) {
		String result = null;
		switch (label.getMapType()) {
		case 1://直接获取
			result = getValueFromTable(label, this.an);
			break;
		case 2://引用接口
			result = getReferNum(this.an);
			break;
		case 3://相似接口
			result = getSimilarNum(this.an);
			break;
		case 4://从表中获取数据条数
			result = getValueCountFromTable(label, this.an);
			break;
		case 5://字符串切割段数
			result = getAmountBySpilt(label);
			break;
		case 7://特殊计算,第四层
			result = getSpecialCalResult(label);
			break;
		case 8://被引用接口
			result = getReferedNum(this.an);
			break;
		case 9://非引用接口
			result = getNonReferNum(this.an);
			break;
		}
		return result;
	}

	/**
	 * 根据标签mapType判断怎么获取中高提供的标签值
	 * 
	 * @param label
	 * @return
	 */
	private String getZhongGaoAutoLabel(Label label) {
		String result = null;
		if(label.getType().equals(1)) {
			switch (label.getMapType()) {
			case 6://在树中
				 result = getReferNum(this.an);
				break;
			case 7://特殊计算,第四层
			case 10://特殊计算,第三层
				result = getSpecialCalResult(label);
				break;
			}
		}
		if(label.getType().equals(2)) {
			
		}
		return result;
	}

	/**
	 * 根据标签key一一对应特殊计算公式
	 * 
	 * @param label
	 * @return
	 */
	private String getSpecialCalResult(Label label) {
		String result = "";
		switch (label.getKey()) {
		case "ecoValueSimilarPatentNumCV":
			result = getEcoValueSimilarPatentNumCV();
			break;
		case "semblanceValueCV":
			result = getSemblanceValueCV();
			break;
		case "marketCompetitivePower":
			result = getMarketCompetitivePower();
			break;
		case "remainingValidPeriod":
			result = getRemainingValidPeriod();
			break;
		case "durationOfPatentCV":
			result = getDurationOfPatentCV();
			break;
		case "durationOfPatent":
			result = getDurationOfPatent();
			break;
		case "timeLimitOfPatent":
			result = getTimeLimitOfPatent();
			break;
		case "costHandingStatusCV":
			result = getCostHandingStatusCV();
			break;
		case "costHandingStatus":
			result = getCostHandingStatus();
			break;
		case "patentMaintenance":
			result = getPatentMaintenance();
			break;
		case "familyPatentNumCV":
			result = getFamilyPatentNumCV();
			break;
		case "patentFamilySize":
			result = getPatentFamilySize();
			break;
		case "familyPatentCodeCV":
		case "familyPatentDistribution":
			result = getFamilyPatentCodeCV();
			break;
		case "patenteGeographuicalDistribution":
			result = getPatenteGeographuicalDistribution();
			break;
		case "inventorNumCV":
			result = getInventorNumCV();
			break;
		case "groupInfluence":
			result = getGroupInfluence();
			break;
		case "classiflcationCodeNum":
			result = getClassiflcationCodeNum();
			break;
		case "classiflcationCodeNumCV":
		case "classiflcationCodeNumCV2":
		case "classiflcationCodeNumCV3":
			result = getClassiflcationCodeNumCV();
			break;
		case "technicalCrossover":
		case "technicalCoverage":
			result = getTechnicalCrossover();
			break;
		case "nonPatentQuotationNumCV":
			result = getNonPatentQuotationNumCV();
			break;
		case "patentReferenceNumCV":
			result = getPatentReferenceNumCV();
			break;
		case "scientificCorrelationStrength":
			result = getScientificCorrelationStrength();
			break;
		case "tecValueSimilarPatentNumCV":
			result = getTecValueSimilarPatentNumCV();
			break;
		case "tecValueIndependentClaimsNumCV":
			result = getTecValueIndependentClaimsNumCV();
			break;
		case "tecValueDependentClaimsNumCV":
			result = getTecValueDependentClaimsNumCV();
			break;
		case "technicalDegree":
			result = getTechnicalDegree();
			break;
		case "meanOfSimilarityCV":
			result = getMeanOfSimilarityCV();
			break;
		case "patentNovelty":
			result = getPatentNovelty();
			break;
		case "pattypeCV":
			result = getPattypeCV();
			break;
		case "lastLegalStatusCV":
			result = getLastLegalStatusCV();
			break;
		case "stabilityOfLegalStatus":
			result = getStabilityOfLegalStatus();
			break;
		case "citedPatentNumCV":
			result = getCitedPatentNumCV();
			break;
		case "patentDependence":
			result = getPatentDependence();
			break;
		case "nonPatentReferenceNumCV":
			result = getNonPatentReferenceNumCV();
			break;
		case "nonPatentDependence":
			result = getNonPatentDependence();
			break;
		case "lawValueIndependentClaimsNumCV":
			result = getLawValueIndependentClaimsNumCV();
			break;
		case "lawValueDependentClaimsNumCV":
			result = getLawValueDependentClaimsNumCV();
			break;
		case "scopeOfRightProtection":
			result = getScopeOfRightProtection();
			break;
		case "gengraphicalCoverage":
			result = getGengraphicalCoverage();
			break;
		case "pattype":
			result = getPattype();
			break;
		case "lastLegalStatus":
			result = getLastLegalStatus();
			break;
		case "publicInAdvance":
			result = getPublicInAdvance();
			break;
		}
		return result;
	}

	/**
	 * 获取是否提前公开
	 * @return
	 */
	private String getPublicInAdvance() {
		Patent patent = getPatent();
		if(patent==null) {
			return "否";
		}
		Date ad = patent.getAd();
		Date od = patent.getOd();
		DateTime dt1 = new DateTime(ad);
		DateTime dt2 = new DateTime(od);
		int month = Months.monthsBetween(dt1, dt2).getMonths();
		if(month < 18) {
			return "是";
		}
		return "否";
	}

	/**
	 * 获取专利最新法律状态
	 * @return
	 */
	private String getLastLegalStatus() {
		Patent patent = getPatent();
		if(patent==null) {
			return "0";
		}
		return patent.getLastLegalStatus() + "";
	}

	/**
	 * 获取专利类型
	 * @return
	 */
	private String getPattype() {
		Patent patent = getPatent();
		if(patent==null) {
			return "0";
		}
		return patent.getType() + "";
	}

	/**
	 * 获取地域保护范围
	 * @return
	 */
	private String getGengraphicalCoverage() {
		return get("familyPatentDistribution");
	}

	/**
	 * 获取权利保护范围
	 * @return
	 */
	private String getScopeOfRightProtection() {
		Double lawValueDependentClaimsNumCV = Double.parseDouble(get("lawValueDependentClaimsNumCV"));
		Double lawValueIndependentClaimsNumCV = Double.parseDouble(get("lawValueIndependentClaimsNumCV"));
		Double classiflcationCodeNumCV3 = Double.parseDouble(get("classiflcationCodeNumCV3"));
		double scopeOfRightProtection = (50 * classiflcationCodeNumCV3 + 25 * lawValueDependentClaimsNumCV+ 25 * lawValueIndependentClaimsNumCV) / 100;
		return scopeOfRightProtection + "";
	}

	/**
	 * 获取法律价值从属权利要求数计算值
	 * @return
	 */
	private String getLawValueDependentClaimsNumCV() {
		double lawValueDependentClaimsNumCV = 0;
		Integer followRightNum = Integer.parseInt(get("followRightNum"));
		if (followRightNum >= 1 && followRightNum <= 5) {
			lawValueDependentClaimsNumCV = 1;
		} else if (followRightNum >= 6 && followRightNum <= 10) {
			lawValueDependentClaimsNumCV = 0.8;
		} else {
			lawValueDependentClaimsNumCV = 0.7;
		}
		return lawValueDependentClaimsNumCV + "";
	}

	/**
	 * 获取法律价值独立权利要求数计算值
	 * @return
	 */
	private String getLawValueIndependentClaimsNumCV() {
		double lawValueIndependentClaimsNumCV = 0;
		Integer exclusiveRightNum = Integer.parseInt(get("exclusiveRightNum"));
		switch (exclusiveRightNum) {
		case 1:
			lawValueIndependentClaimsNumCV = 1;
			break;
		case 2:
			lawValueIndependentClaimsNumCV = 0.9;
			break;
		default:
			lawValueIndependentClaimsNumCV = 0.8;
			break;
		}
		return lawValueIndependentClaimsNumCV + "";
	}

	/**
	 * 获取非专利依赖度
	 * @return
	 */
	private String getNonPatentDependence() {
		return get("nonPatentReferenceNumCV");
	}


	/**
	 * 获取非专利引文数计算值
	 * @return
	 */
	private String getNonPatentReferenceNumCV() {
		double nonPatentReferenceNumCV = 0;
		Integer citedNonPatentNum = Integer.parseInt(get("citedNonPatentNum"));
		switch (citedNonPatentNum) {
		case 0:
			nonPatentReferenceNumCV = 1;
			break;
		case 1:
			nonPatentReferenceNumCV = 0.9;
			break;
		case 2:
		case 3:
			nonPatentReferenceNumCV = 0.7;
			break;
		default:
			nonPatentReferenceNumCV = 0.5;
			break;
		}
		
		return nonPatentReferenceNumCV + "";
	}

	/**
	 * 获取专利依赖度
	 * @return
	 */
	private String getPatentDependence() {
		return get("citedPatentNumCV");
	}
	
	/**
	 * 获取引证专利数计算值
	 * @return
	 */
	private String getCitedPatentNumCV() {
		double citedPatentNumCV = 0;
		Integer citedPatentNum = Integer.parseInt(get("citedPatentNum"));
		switch (citedPatentNum) {
		case 0:
			citedPatentNumCV = 1;
			break;
		case 1:
		case 2:
		case 3:
			citedPatentNumCV = 0.9;
			break;
		case 4:
		case 5:
		case 6:
			citedPatentNumCV = 0.7;
			break;
		default:
			citedPatentNumCV = 0.5;
			break;
		}
		return citedPatentNumCV + "";
	}

	/**
	 * 获取法律地位稳固度
	 * @return
	 */
	private String getStabilityOfLegalStatus() {
		double stabilityOfLegalStatus = 0;
		Double lastLegalStatusCV = Double.parseDouble(get("lastLegalStatusCV"));
		Double pattypeCV = Double.parseDouble(get("pattypeCV"));
		stabilityOfLegalStatus = (50 * lastLegalStatusCV  + 50 * pattypeCV )/ 100;
		return stabilityOfLegalStatus + "";
	}

	/**
	 * 获取最新法律状态计算值
	 * @return
	 */
	private String getLastLegalStatusCV() {
		double lastLegalStatusCV = 0;
		Patent patent = getPatent();
		if(patent==null) {
			return lastLegalStatusCV + "";
		}
		if("有效".equals(patent.getLastLegalStatus())) {
			lastLegalStatusCV = 1;
		}
		if("在审".equals(patent.getLastLegalStatus())) {
			lastLegalStatusCV = 0.5;
		}
		return lastLegalStatusCV + "";
	}

	/**
	 * 获取专利类型计算值
	 * @return
	 */
	private String getPattypeCV() {
		double pattypeCV = 0.5;
		Patent patent = getPatent();
		if(patent==null) {
			return pattypeCV + "";
		}
		if(patent.getType().equals(1)) {
			pattypeCV = 1;
		}
		return pattypeCV + "";
	}

	/**
	 * 获取专利新颖度
	 * @return
	 */
	private String getPatentNovelty() {
		double patentNovelty = 0;
		Double tecValueSimilarPatentNumCV = Double.parseDouble(get("tecValueSimilarPatentNumCV"));
		Double meanOfSimilarityCV = Double.parseDouble(get("meanOfSimilarityCV"));
		patentNovelty = Math.round(tecValueSimilarPatentNumCV * 4000 + meanOfSimilarityCV * 6000) / 10000.0;
		return patentNovelty + "";
	}

	/**
	 * 获取相似度均值计算值
	 * @return
	 */
	private String getMeanOfSimilarityCV() {
		Double sim = Double.parseDouble(get("meanOfSimilarity"));
		if (sim == null) {
			sim = 0.5;
		}
		double meanOfSimilarityCV = 0;
		double k = 1 - sim; 
		if (k >= 0 && k <= 0.3) {
			meanOfSimilarityCV = 1;
		} else if (k > 0.3 && k <= 0.5) {
			meanOfSimilarityCV = 0.9;
		} else if (k > 0.5 && k < 0.8) {
			meanOfSimilarityCV = 0.8;
		} else {
			meanOfSimilarityCV = 0.6;
		}
		return meanOfSimilarityCV + "";
	}
	
	/**
	 * 获取技术价值相似专利数量计算值
	 * @return
	 */
	private String getTecValueSimilarPatentNumCV() {
		Integer maxSimilarNumberOfPatents = Integer.parseInt(GlobalVariableUtil.get("maxSimilarNumberOfPatents"));
		Integer minSimilarNumberOfPatents = Integer.parseInt(GlobalVariableUtil.get("minSimilarNumberOfPatents"));
		Integer similarNumberOfPatents = Integer.parseInt(get("similarNumberOfPatents"));	
		double tecValueSimilarPatentNumCV = Math.round(((1.0 * (maxSimilarNumberOfPatents - similarNumberOfPatents)
				/ (maxSimilarNumberOfPatents - minSimilarNumberOfPatents))/2 + 0.5)*10000)/10000.0;
		if(tecValueSimilarPatentNumCV<0) {
			tecValueSimilarPatentNumCV = 0.0;
		}
		return tecValueSimilarPatentNumCV + "";
	}

	/**
	 * 获取技术专业度
	 * @return
	 */
	private String getTechnicalDegree() {
		double tecValueIndependentClaimsNumCV = Double.parseDouble(get("tecValueIndependentClaimsNumCV"));
		double tecValueDependentClaimsNumCV = Double.parseDouble(get("tecValueDependentClaimsNumCV"));
		double technicalDegree = (60 * tecValueIndependentClaimsNumCV + 40 * tecValueDependentClaimsNumCV) / 100;
		return technicalDegree + "";
	}

	/**
	 * 获取技术价值从属权利要求数计算值
	 * @return
	 */
	private String getTecValueDependentClaimsNumCV() {
		Integer followRightNum = Integer.parseInt(get("followRightNum"));
		double tecValueDependentClaimsNumCV = 0;
		if (followRightNum >= 1 && followRightNum <= 5) {
			tecValueDependentClaimsNumCV = 1;
		} else if (followRightNum >= 6 && followRightNum <= 10) {
			tecValueDependentClaimsNumCV = 0.8;
		} else {
			tecValueDependentClaimsNumCV = 0.7;
		}
		return tecValueDependentClaimsNumCV + "";
	}

	/**
	 * 获取技术价值独立权利要求数计算值
	 * @return
	 */
	private String getTecValueIndependentClaimsNumCV() {
		Integer exclusiveRightNum = Integer.parseInt(get("exclusiveRightNum"));
		double tecValueIndependentClaimsNumCV = 0;
		switch (exclusiveRightNum) {
		case 0:
			tecValueIndependentClaimsNumCV = 0;
			break;
		case 1:
			tecValueIndependentClaimsNumCV = 0.8;
			break;
		case 2:
			tecValueIndependentClaimsNumCV = 0.9;
			break;
		default:
			tecValueIndependentClaimsNumCV = 1;
			break;
		}
		return tecValueIndependentClaimsNumCV + "";
	}

	/**
	 * 获取科学关联强度
	 * @return
	 */
	private String getScientificCorrelationStrength() {
		Double nonPatentQuotationNumCV = Double.parseDouble(get("nonPatentQuotationNumCV"));
		Double patentReferenceNumCV = Double.parseDouble(get("patentReferenceNumCV"));
		Double scientificCorrelationStrength = (nonPatentQuotationNumCV * 80 + patentReferenceNumCV *20) /100;
		return scientificCorrelationStrength + "";
	}

	/**
	 * 获取专利引用数量计算值
	 * @return
	 */
	private String getPatentReferenceNumCV() {
		Integer citedPatentNum = Integer.parseInt(get("citedPatentNum"));
		double patentReferenceNumCV = 0;
		switch (citedPatentNum) {
		case 0:
			patentReferenceNumCV = 0.5;
			break;
		case 1:
		case 2:
		case 3:
			patentReferenceNumCV = 0.7;
			break;
		case 4:
		case 5:
		case 6:
			patentReferenceNumCV = 0.9;
			break;
		default:
			patentReferenceNumCV = 1;
			break;
		}
		return patentReferenceNumCV + "";
	}

	/**
	 * 获取非专利引用数量计算值
	 * @return
	 */
	private String getNonPatentQuotationNumCV() {
		double nonPatentQuotationNumCV = 0;
		Integer citedNonPatentNum = Integer.parseInt(get("citedNonPatentNum"));
		switch (citedNonPatentNum) {
		case 0:
			nonPatentQuotationNumCV = 0.5;
			break;
		case 1:
			nonPatentQuotationNumCV = 0.7;
			break;
		case 2:
		case 3:
			nonPatentQuotationNumCV = 0.9;
			break;
		default:
			nonPatentQuotationNumCV = 1;
			break;
		}
		return nonPatentQuotationNumCV + "";
	}

	/**
	 * 获取技术交叉性
	 * 获取技术覆盖度
	 * 值相同
	 * @return
	 */
	private String getTechnicalCrossover() {
		return get("classiflcationCodeNumCV");
	}

	/**
	 * 获取分类号数量
	 * @return
	 */
	private String getClassiflcationCodeNum() {
		Patent patent = getPatent();
		if(patent == null) {
			return "0";
		}
		String sics = patent.getSic();
		String[] sic = sics.split(";");
		Integer classiflcationCodeNum = sic.length;
		return classiflcationCodeNum + "";
	}
	
	/**
	 * 获取分类号数量计算值
	 * @return
	 */
	private String getClassiflcationCodeNumCV() {
		Patent patent = getPatent();
		if(patent == null) {
			return "0";
		}
		String sics = patent.getSic();
		String[] sic = sics.split(";");
		Set<String> set = new HashSet();
		for (String x : sic) {
			set.add(x.substring(0, 1));
		}
		double classiflcationCodeNumCV = 0;
		if (set.size() == 2) {
			classiflcationCodeNumCV = 0.95;
		} else if (set.size() > 2) {
			classiflcationCodeNumCV = 1;
		} else if (set.size() == 1) {
			switch (sic.length) {
			case 1:
				classiflcationCodeNumCV = 0.5;
				break;
			case 2:
			case 3:
				classiflcationCodeNumCV = 0.7;
				break;
			case 4:
			case 5:
				classiflcationCodeNumCV = 0.8;
				break;
			default:
				classiflcationCodeNumCV = 0.9;
				break;
			}
		}
		return classiflcationCodeNumCV + "";
	}

	/**
	 * 获取团队影响力
	 * @return
	 */
	private String getGroupInfluence() {
		return get("inventorNumCV");
	}

	/**
	 * 获取发明人数量计算值
	 * @return
	 */
	private String getInventorNumCV() {
		Patent patent = getPatent();
		if(patent == null) {
			return "0";
		}
		String pin = patent.getPin();
		int num = pin.split(";").length;
		double inventorNumCV = 0;
		switch (num) {
		case 1:
			inventorNumCV = 0.5;
			break;
		case 2:
		case 3:
			inventorNumCV = 0.6;
			break;
		case 4:
		case 5:
		case 6:
			inventorNumCV = 0.8;
			break;
		case 7:
		case 8:
		case 9:
		case 10:
			inventorNumCV = 0.95;
			break;
		default:
			inventorNumCV = 1;
			break;
		}
		return inventorNumCV + "";
	}

	/**
	 * 获取专利地域分布
	 * @return
	 */
	private String getPatenteGeographuicalDistribution() {
		return get("familyPatentCodeCV");
	}

	/**
	 * 获取同族专利号计算值
	 * 获取同族专利分布
	 * 值相同
	 * @return
	 */
	private String getFamilyPatentCodeCV() {
		String siblingPatent = get("siblingPatent");
		if(Strings.isNullOrEmpty(siblingPatent)) {
			return "0";
		}
		int flag = 0;
		String familyPatentCodeCV = "";
		String[] tz = siblingPatent.split(";");
		List<String> tzlist = new ArrayList<String>();
		for (String x : tz) {
			tzlist.add(CalculateUtil.getCountry(x));
		}
		for (String y : tzlist) {
			if (y.equals("US") || y.equals("CA")) {
				flag = 2;
				break;
			} else if (y.equals("GB") || y.equals("FR") || y.equals("DE") || y.equals("JP") || y.equals("EP")) {
				flag = 1;
			} else {
				continue;
			}
		}
		if (flag == 2) {
			familyPatentCodeCV = "1";
		} else if (flag == 1) {
			familyPatentCodeCV = "0.8";
		} else {
			familyPatentCodeCV = "0.4";
		}
		return familyPatentCodeCV;
	}

	/**
	 * 获取专利族规模
	 * @return
	 */
	private String getPatentFamilySize() {
		return get("familyPatentNumCV");
	}

	/**
	 * 获取同族专利数量计算值
	 * @return
	 */
	private String getFamilyPatentNumCV() {
		Integer maxSiblingPatentNum = Integer.parseInt(GlobalVariableUtil.get("maxSiblingPatentNum"));
		Integer minSiblingPatentNum = Integer.parseInt(GlobalVariableUtil.get("minSiblingPatentNum"));
		Integer siblingPatentNum = Integer.parseInt(get("siblingPatentNum"));
		if(siblingPatentNum==null) {
			siblingPatentNum = 0;
		}
		double familyPatentNumCV = 1.0 * (maxSiblingPatentNum - siblingPatentNum)
				/ (maxSiblingPatentNum - minSiblingPatentNum);
		return familyPatentNumCV + "";
	}

	/**
	 * 获取专利维持状态
	 * @return
	 */
	private String getPatentMaintenance() {
		Double costHandingStatusCV = Double.parseDouble(get("costHandingStatusCV"));
		Patent patent = getPatent();
		if(patent == null) {
			return costHandingStatusCV + "";
		}
		String applyNum = CalculateUtil.getCode(this.an);
		Date ad = patent.getAd();
		Date now = new Date();
		DateTime dt1 = new DateTime(ad);
		DateTime dt2 = new DateTime(now);
		int year = Years.yearsBetween(dt1, dt2).getYears();
		String feeType = "第"+ year + "年年费";
		String feeState = originalPatentMapper.getFeeState(applyNum, feeType);
		if(feeState != null && "处理结束".equals(feeState)) {//TODO:去确认,feeState为空字符串的时候是不是缴费成功
			return "1";
		}
		return costHandingStatusCV + "";
	}
	
	/**
	 * 获取费用处理状态计算值
	 * @return
	 */
	private String getCostHandingStatusCV() {
		Patent patent = getPatent();
		if(patent == null) {
			return "0";
		}
		Date ad = patent.getAd();
		Date now = new Date();
		DateTime dt1 = new DateTime(ad);
		DateTime dt2 = new DateTime(now);
		int day = Days.daysBetween(dt1, dt2).getDays();
		int b = (int) Math.round(1.0*day/30); 
		double costHandingStatusCV = 0;
		switch (b) {
		case 1:
			costHandingStatusCV = 0.8;
			break;
		case 2:
			costHandingStatusCV = 0.7;
			break;
		case 3:
			costHandingStatusCV = 0.6;
			break;
		case 4:
			costHandingStatusCV = 0.3;
			break;
		case 5:
			costHandingStatusCV = 0.2;
			break;
		case 6:
			costHandingStatusCV = 0.1;
			break;
		}
		return costHandingStatusCV + "";
	}
	
	/**
	 * 获取费用处理状态
	 * @return
	 */
	private String getCostHandingStatus() {
		Patent patent = getPatent();
		if(patent == null) {
			return "未缴费";
		}
		String applyNum = CalculateUtil.getCode(this.an);
		Date ad = patent.getAd();
		Date now = new Date();
		DateTime dt1 = new DateTime(ad);
		DateTime dt2 = new DateTime(now);
		int year = Years.yearsBetween(dt1, dt2).getYears();
		String feeType = "第"+ year + "年年费";
		String feeState = originalPatentMapper.getFeeState(applyNum, feeType);
		if(feeState == null) {
			feeState = "未缴费";
		}
		return feeState + "";
	}

	/**
	 * 获取专利时限
	 * @return
	 */
	private String getTimeLimitOfPatent() {
		Double durationOfPatentCV = Double.parseDouble(get("durationOfPatentCV"));
		double timeLimitOfPatent = 0;
		if (durationOfPatentCV < 0 && durationOfPatentCV > 20) {
			System.out.println("输入的数据有问题");
		} else if (durationOfPatentCV >= 0 && durationOfPatentCV < 7) {
			timeLimitOfPatent = durationOfPatentCV * 0.043;
		} else if (durationOfPatentCV >= 7 && durationOfPatentCV < 13) {
			timeLimitOfPatent = 0.12 * durationOfPatentCV - 0.56;
		} else if (durationOfPatentCV >= 13 && durationOfPatentCV < 16) {
			timeLimitOfPatent = -0.23 * durationOfPatentCV + 3.99;
		} else if (durationOfPatentCV >= 16 && durationOfPatentCV <= 20) {
			timeLimitOfPatent = -0.075 * durationOfPatentCV + 1.5;
		}
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(3); 
		return nf.format(timeLimitOfPatent);
	}

	/**
	 * 获取剩余有效时间 专利申请日+专利有效时间-当前时间 单位为年向下取整
	 * @return
	 */
	private String getRemainingValidPeriod() {
		Patent patent = getPatent();
		if (patent == null) {
			return "0";
		}
		Date ad = patent.getAd();
		Date now = new Date();
		int effectYear = 0;
		if (patent.getType() == 1 || patent.getType() == 4) {
			effectYear = 20;
		} else {
			effectYear = 10;
		}
		DateTime dt1 = new DateTime(ad).plusYears(effectYear);//失效时间
		DateTime dt2 = new DateTime(now);//当前时间
		int year = Years.yearsBetween(dt2, dt1).getYears();
		if(year<0) {
			return "0.0";
		}
		DateTime dt3 = dt2.plusYears(year);//把年份变为失效时间的年份,月日不变
		int day = Days.daysBetween(dt3, dt1).getDays();
		Double remainingValidPeriod = Math.round((1.0 * day / 365.0 + year)*100)/100.0;
		if(remainingValidPeriod<0) {
			return "0.0";
		}
		return remainingValidPeriod + "";
	}

	/**
	 * 获取保护年限计算值 专利保护年限 - 剩余有效期/365
	 * @return
	 */
	private String getDurationOfPatentCV() {
		Patent patent = getPatent();
		if (patent == null) {
			return "0";
		}
		int effectYear = 0;
		if (patent.getType() == 1 || patent.getType() == 4) {
			effectYear = 20;
		} else {
			effectYear = 10;
		}
		Double remainingValidPeriod = Double.parseDouble(get("remainingValidPeriod"));
		Double durationOfPatentCV = Math.round((effectYear - remainingValidPeriod)*10000)/10000.0;
		return durationOfPatentCV + "";
	}

	/**
	 * 获取保护年限
	 * @return
	 */
	private String getDurationOfPatent() {
		Patent patent = getPatent();
		if (patent == null) {
			return "0";
		}
		int effectYear = 0;
		if (patent.getType() == 1 || patent.getType() == 4) {
			effectYear = 20;
		} else {
			effectYear = 10;
		}
		return effectYear + "";
	}

	/**
	 * 获取市场竞争力 0.4*经济价值相似专利数量计算值+0.6*相似度计算值
	 * 
	 * @return
	 */
	private String getMarketCompetitivePower() {
		Double ecoValueSimilarPatentNumCV = Double.parseDouble(get("ecoValueSimilarPatentNumCV"));
		Double semblanceValueCV = Double.parseDouble(get("semblanceValueCV"));
		Double marketCompetitivePower = Math.round(4000 * ecoValueSimilarPatentNumCV + 6000 * semblanceValueCV) / 10000.0;
		return marketCompetitivePower + "";
	}

	/**
	 * 获取经济价值相似专利数量计算值 (最大相似数-相似数)/(最大相似数-最小相似数)
	 * 
	 * @return
	 */
	private String getEcoValueSimilarPatentNumCV() {
		Integer maxSimilarNumberOfPatents = Integer.parseInt(GlobalVariableUtil.get("maxSimilarNumberOfPatents"));
		Integer minSimilarNumberOfPatents = Integer.parseInt(GlobalVariableUtil.get("minSimilarNumberOfPatents"));
		Integer similarNumberOfPatents = Integer.parseInt(get("similarNumberOfPatents"));	
		double ecoValueSimilarPatentNumCV = Math.round(1.0 * (maxSimilarNumberOfPatents - similarNumberOfPatents)
				/ (maxSimilarNumberOfPatents - minSimilarNumberOfPatents)*10000) /10000.0;
		if(ecoValueSimilarPatentNumCV<0) {
			ecoValueSimilarPatentNumCV = 0.0;
		}
		return ecoValueSimilarPatentNumCV + "";
	}

	/**
	 * 获取相似度计算值 1-相似度 相似度暂时取0.5
	 * 
	 * @return
	 */
	private String getSemblanceValueCV() {
		Double sim = Double.parseDouble(get("meanOfSimilarity"));
		if (sim == null) {
			sim = 0.5;
		}
		double semblanceValueCV = 1 - sim;
		return semblanceValueCV + "";
	}

	private String get(String key) {
		String result = values.get(key);
		if (result == null) {
			if(this.an !=null) {
				result = taskOrderLabelMapper.selectValueByKeyAndAn(key, this.an); 
				if(result != null) {
					values.put(key , result);
				}
			}
			if(result == null) {
				result = "0";
			}
		}
		return result;
	}

	/**
	 * 通过加工制定
	 * 
	 * @param label
	 * @return
	 */
	private String getAmountBySpilt(Label label) {
		String v = get(label.getMapRule());
		if("".equals(v) || v==null || v.length()==0) {
			return "0";
		}
		String[] array = v.split(";");
		return array.length + "";
	}

	/**
	 * 根据标签的映射方式
	 * 
	 * @param label
	 * @param an
	 * @return
	 */
	public String getValueFromTable(Label label, String an) {
		String mapRule = label.getMapRule();
		String table = mapRule.substring(0, mapRule.indexOf("."));
		String column = mapRule.substring(mapRule.indexOf(".") + 1);
		return originalPatentMapper.getLabelValue(table, column, an);
	}

	/**
	 * 根据标签的映射方式的条数
	 * 
	 * @param label
	 * @param an
	 * @return
	 */
	public String getValueCountFromTable(Label label, String an) {
		String mapRule = label.getMapRule();
		return originalPatentMapper.getLabelRowCount(mapRule, an) + "";
	}

	/**
	 * 根据an号获取引证专利数
	 * 
	 * @param an
	 * @return
	 */
	public String getReferNum(String an) {
		if(isBatch) {
			return "4";
		}
		Patent patent = getPatent();
		try {
			return cNIPRService.getReferNum(patent.getPnm()) + "";
		} catch (Exception e) {
			logger.debug("引证接口异常:", e);
			return "4";
		}
	}
	
	/**
	 * 根据an号获取被引证专利数
	 * 
	 * @param an
	 * @return
	 */
	public String getReferedNum(String an) {
		if(isBatch) {
			return null;
		}
		Patent patent = getPatent();
		try {
			return cNIPRService.getReferedNum(patent.getPnm()) + "";
		} catch (Exception e) {
			logger.debug("引证接口异常:", e);
			return null;
		}
	}
	
	/**
	 * 根据an号获取非专利引证数
	 * 
	 * @param an
	 * @return
	 */
	public String getNonReferNum(String an) {
		if(isBatch) {
			return "1";
		}
		Patent patent = getPatent();
		try {
			return cNIPRService.getNonReferNum(patent.getPnm()) + "";
		} catch (Exception e) {
			logger.debug("非专利引证接口异常:", e);
			return "1";
		}
	}

	/**
	 * 根据an号获取相似专利数
	 * 
	 * @param an
	 * @return
	 */
	public String getSimilarNum(String an) {
		if(isBatch) {
			return "600";
		}
		Patent patent = getPatent();
		try {
			return cNIPRService.getSimilarNum(patent.getPnm()) + "";
		} catch (Exception e) {
			logger.debug("相似异常:", e);
			return "600";
		}
	}

	public LabelDealUtil(PatentMapper patentMapper, OriginalPatentMapper originalPatentMapper,
			TaskOrderLabelMapper taskOrderLabelMapper, CNIPRService cNIPRService, Map<String, String> values, String an,
			Boolean saveImmediately, Long taskOrderId) {
		super();
		this.patentMapper = patentMapper;
		this.originalPatentMapper = originalPatentMapper;
		this.taskOrderLabelMapper = taskOrderLabelMapper;
		this.cNIPRService = cNIPRService;
		this.values = values;
		this.an = an;
		this.saveImmediately = saveImmediately;
		this.taskOrderId = taskOrderId;
	}

	public LabelDealUtil(PatentMapper patentMapper, OriginalPatentMapper originalPatentMapper,
			TaskOrderLabelMapper taskOrderLabelMapper, CNIPRService cNIPRService, String an, Boolean saveImmediately,
			Long taskOrderId) {
		super();
		this.patentMapper = patentMapper;
		this.originalPatentMapper = originalPatentMapper;
		this.taskOrderLabelMapper = taskOrderLabelMapper;
		this.cNIPRService = cNIPRService;
		this.an = an;
		this.saveImmediately = saveImmediately;
		this.taskOrderId = taskOrderId;
		this.values = new HashMap<>();
	}

	public Map<String, String> getValues() {
		return values;
	}

	public void setValues(Map<String, String> values) {
		this.values = values;
	}

	public void setValues(List<TaskOrderLabel> taskOrderLabels) {
		this.values = new HashMap<>();
		for (TaskOrderLabel taskOrderLabel : taskOrderLabels) {
			if (taskOrderLabel.getLabel() != null) {
				values.put(taskOrderLabel.getLabel().getKey(),
						taskOrderLabel.getStrValue() != null ? taskOrderLabel.getStrValue()
								: taskOrderLabel.getTextValue());
			}
		}
	}

	public String getAn() {
		return an;
	}

	public void setAn(String an) {
		this.an = an;
	}

	public boolean isSaveImmediately() {
		return saveImmediately;
	}

	public void setSaveImmediately(boolean saveImmediately) {
		this.saveImmediately = saveImmediately;
	}

	public Long getTaskOrderId() {
		return taskOrderId;
	}

	public void setTaskOrderId(Long taskOrderId) {
		this.taskOrderId = taskOrderId;
	}

	public void setOriginalPatentMapper(OriginalPatentMapper originalPatentMapper) {
		this.originalPatentMapper = originalPatentMapper;
	}

	public void setTaskOrderLabelMapper(TaskOrderLabelMapper taskOrderLabelMapper) {
		this.taskOrderLabelMapper = taskOrderLabelMapper;
	}

	public void setcNIPRService(CNIPRService cNIPRService) {
		this.cNIPRService = cNIPRService;
	}

	public boolean isBatch() {
		return isBatch;
	}

	public void setBatch(boolean isBatch) {
		this.isBatch = isBatch;
	}

	private Patent getPatent() {
		Patent patent = this.patent;
		if (patent == null) {
			patent = patentMapper.selectByAn(an);
		}
		return patent;
	}

}
