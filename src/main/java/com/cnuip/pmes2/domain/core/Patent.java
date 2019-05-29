package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.constant.Patents;
import com.cnuip.pmes2.domain.BaseModel;
import com.cnuip.pmes2.domain.inventory.PatentInfo;
import com.cnuip.pmes2.util.CalculateUtil;
import com.cnuip.pmes2.util.DateUtil;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Strings;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Document(indexName = "pmes", type = "patent")
public class Patent extends BaseModel {

	//PMES2.0中的专利id
	@Id
	private Long id;
	private Integer instanceType = 1;// 固定的值，方便查询所有
	//专利名称
	@Field(analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
	private String ti;
	//专利类型
	private Integer type;
	//申请日
	@Field(format = DateFormat.basic_date_time)
	private Date ad;
	//申请号
	@Field(index = FieldIndex.not_analyzed)
	private String an;
	//公开日
	@Field(format = DateFormat.basic_date_time)
	private Date od;
	//公开号
	@Field(index = FieldIndex.not_analyzed)
	private String onm;
	//公告日
	@Field(format = DateFormat.basic_date_time)
	private Date pd;
	//公告号
	@Field(index = FieldIndex.not_analyzed)
	private String pnm;
	//审查员
	@Field(analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
	private String examinant;
	//分类号
	private String sic;
	//主分类号
	private String pic;
	//专利国别
	private String country;
	//国省代码
	private String province;
	//分案原申请号
	@Field(index = FieldIndex.not_analyzed)
	private String dan;
	//优先权号
	@Field(index = FieldIndex.not_analyzed)
	private String prc;
	//优先权日
	private String prd;
	//摘要
	@Field(analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
	private String ab;
	//摘要附图路径
	@Field(index =FieldIndex.not_analyzed)
	private String abPicPath;
	//pdf全文路径
	@Field(index = FieldIndex.not_analyzed)
	private String pdfUrl;
	//法律状态
	@Field(index = FieldIndex.not_analyzed)
	private String lastLegalStatus;
	//申请(专利权)人
	@Field(analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
	private String pa;
	//发明人
	@Field(analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
	private String pin;
	//出版社专利id
	@Field(index = FieldIndex.not_analyzed)
	private String idPatent;
	//段名
	@Field(index = FieldIndex.not_analyzed)
	private String sectionName;
	//出版社系统中id
	private String sysId;
	//出版社数据创建时间
	@Field(format = DateFormat.basic_date_time)
	private Date oCreateTime;
	//出版社数据修改时间
	@Field(format = DateFormat.basic_date_time)
	private Date oUpdateTime;
	@Field(format = DateFormat.basic_date_time)
	private Date createTime;
	@Field(format = DateFormat.basic_date_time)
	private Date updateTime;
    // 计算得到的专利价值
	private String patentValue;
	@Field(type = FieldType.Double)
	private Double value;

	private PatentIndex index;
	@Field(analyzer = "ik_smart", searchAnalyzer = "ik_smart")
	private String cl; // 权利要求书

	private List<ProcessOrder> processOrders;

	private ProcessOrder baseIndexOrder;
	private ProcessOrder deepIndexOrder;
	private ProcessOrder valueIndexOrder;
	private ProcessOrder priceIndexOrder;
    // 是否批量快速处理过, 0 - 未处理，1 - 已处理
	private Integer hasBatchIndexed;
	// 流程处理状态， 0 - 流程未处理完，1 - 所有流程已处理完
	private Integer indexState;

	// 最新的标签：二选一
	@Field(type = FieldType.Nested, ignoreFields = {"patent", "user"})
	private List<TaskOrderLabel> latestLabels;
	private List<InstanceLabel> instanceLabels;
	private Map<String, Object> labels;

	public Patent(PatentInfo patentInfo) {
		this.idPatent = patentInfo.getPatentId()+ "";
		this.sectionName = Strings.nullToEmpty(patentInfo.getSectionName()).trim();
		this.sysId = Strings.nullToEmpty(patentInfo.getSysId()).trim();
		this.oCreateTime = patentInfo.getCreatTime();
		this.oUpdateTime = patentInfo.getUpdateTime();
		this.ti = Strings.nullToEmpty(patentInfo.getTi()).trim();
//		this.type = Integer.parseInt(patentInfo.getExtra().getPatType().trim());
		this.type = Patents.Types.valueOf(patentInfo.getSectionName()).getValue();
		this.an = Strings.nullToEmpty(patentInfo.getAn()).trim();
		this.pnm = Strings.nullToEmpty(patentInfo.getPnm()).trim();
		this.onm = Strings.nullToEmpty(patentInfo.getPnm()).trim();
		this.examinant = Strings.nullToEmpty(patentInfo.getExtra().getExaminant()).trim();
		this.sic = Strings.nullToEmpty(patentInfo.getExtra().getSic()).trim();
		this.pic = Strings.nullToEmpty(patentInfo.getExtra().getPic()).trim();
		this.province = Strings.nullToEmpty(patentInfo.getExtra().getCo()).trim();
		this.dan = Strings.nullToEmpty(patentInfo.getExtra().getDan()).trim();
		this.ab = Strings.nullToEmpty(patentInfo.getAb()).trim();
		this.pa = Strings.nullToEmpty(patentInfo.getPa()).trim();
		this.pin = Strings.nullToEmpty(patentInfo.getPin()).trim();
		if(!"null".equals(patentInfo.getExtra().getAbPicPath())) {
			this.abPicPath = Strings.nullToEmpty(patentInfo.getExtra().getAbPicPath()).trim();
		}
		if(patentInfo.getExtra().getCl() != null && !"null".equals(patentInfo.getExtra().getCl())) {
			this.cl = Strings.nullToEmpty(patentInfo.getExtra().getCl()).trim();
		}
		this.pdfUrl = Strings.nullToEmpty(patentInfo.getExtra().getPdfUrl()).trim();
		this.lastLegalStatus = Strings.nullToEmpty(patentInfo.getLastLegalStatus()).trim();
		if(!Strings.isNullOrEmpty(patentInfo.getAd())) {
			this.ad = DateUtil.parse("yyyy.MM.dd", patentInfo.getAd());
		}
		if(!Strings.isNullOrEmpty(patentInfo.getPd())) {
			this.od = DateUtil.parse("yyyy.MM.dd", patentInfo.getPd());
			this.pd = DateUtil.parse("yyyy.MM.dd", patentInfo.getPd());
		}
		this.country = CalculateUtil.getCountry(this.an);
		List<String> prResult = CalculateUtil.splitPr(patentInfo.getExtra().getPr());
		this.prd = prResult.get(0);
		this.prc = prResult.get(1);
	}
	
	public Patent(PatentInfo patentInfo, int mode) {
		this.idPatent = patentInfo.getPatentId()+ "";
		this.sectionName = Strings.nullToEmpty(patentInfo.getSectionName()).trim();
		this.sysId = Strings.nullToEmpty(patentInfo.getSysId()).trim();
		this.oCreateTime = patentInfo.getCreatTime();
		this.oUpdateTime = patentInfo.getUpdateTime();
		this.ti = Strings.nullToEmpty(patentInfo.getTi()).trim();
//		this.type = Integer.parseInt(patentInfo.getExtra().getPatType().trim());
		this.type = Patents.Types.valueOf(patentInfo.getSectionName()).getValue();
		this.an = Strings.nullToEmpty(patentInfo.getAn()).trim();
		this.pnm = Strings.nullToEmpty(patentInfo.getPnm()).trim();
		this.onm = Strings.nullToEmpty(patentInfo.getOnm()).trim();
		this.examinant = Strings.nullToEmpty(patentInfo.getExtra().getExaminant()).trim();
		this.sic = Strings.nullToEmpty(patentInfo.getExtra().getSic()).trim();
		this.pic = Strings.nullToEmpty(patentInfo.getExtra().getPic()).trim();
		this.province = Strings.nullToEmpty(patentInfo.getExtra().getCo()).trim();
		this.dan = Strings.nullToEmpty(patentInfo.getExtra().getDan()).trim();
		this.ab = Strings.nullToEmpty(patentInfo.getAb()).trim();
		this.pa = Strings.nullToEmpty(patentInfo.getPa()).trim();
		this.pin = Strings.nullToEmpty(patentInfo.getPin()).trim().replaceAll(" ","");
		if(!"null".equals(patentInfo.getExtra().getAbPicPath())) {
			this.abPicPath = Strings.nullToEmpty(patentInfo.getExtra().getAbPicPath()).trim();
		}
		if(patentInfo.getExtra().getCl() != null && !"null".equals(patentInfo.getExtra().getCl())) {
			this.cl = Strings.nullToEmpty(patentInfo.getExtra().getCl()).trim();
		}
		this.pdfUrl = Strings.nullToEmpty(patentInfo.getExtra().getPdfUrl()).trim();
		this.lastLegalStatus = Strings.nullToEmpty(patentInfo.getLastLegalStatus()).trim();
		if(!Strings.isNullOrEmpty(patentInfo.getAd())) {
			this.ad = DateUtil.parse("yyyy.MM.dd", patentInfo.getAd());
		}
		if(!Strings.isNullOrEmpty(patentInfo.getPd())) {
			this.pd = DateUtil.parse("yyyy.MM.dd", patentInfo.getPd());
		}
		if(!Strings.isNullOrEmpty(patentInfo.getOd())) {
			this.od = DateUtil.parse("yyyy.MM.dd", patentInfo.getOd());
		}
		this.country = CalculateUtil.getCountry(this.an);
		List<String> prResult = CalculateUtil.splitPr(patentInfo.getExtra().getPr());
		this.prd = prResult.get(0);
		this.prc = prResult.get(1);
		this.hasBatchIndexed = 0;
	}
	
}
