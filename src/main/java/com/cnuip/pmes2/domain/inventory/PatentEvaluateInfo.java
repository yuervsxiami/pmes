package com.cnuip.pmes2.domain.inventory;

import lombok.Data;
import org.bson.types.Decimal128;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


import java.util.Date;

@Document(collection = "e_patentInfo")
@Data
public class PatentEvaluateInfo {

    @Id
    private String id;//ID系统自动生成

    @Field("an")
    private String an;//专利申请号

    @Field("ti")
    private String ti;//专利名称

    @Field("sectionName")
    private String sectionName;//专利类型

    @Field("lastLegalStatus")
    private String lastLegalStatus;//专利法律状态

    @Field("updateTime")
    private Date updateTime;//业务更新时间

    @Field("createTime")
    private Date createTime;//业务生成时间

    @Field("patentValue")
    private Decimal128 patentValue;//专利价值

    @Field("economicValue")
    private Decimal128 economicValue;//经济价值

    @Field("technologicalValue")
    private Decimal128 technologicalValue;//技术价值

    @Field("legalValue")
    private Decimal128 legalValue;//法律价值

    @Field("marketCompetitiveness")
    private Decimal128 marketCompetitiveness;//市场竞争能力

    @Field("patentEconomicLife")
    private Decimal128 patentEconomicLife;//专利经济寿命

    @Field("patentPortfolio")
    private Decimal128 patentPortfolio;//专利布局

    @Field("technologyAdvancement")
    private Decimal128 technologyAdvancement;//技术先进性

    @Field("technicalApplicationRange")
    private Decimal128 technicalApplicationRange;//技术应用范围

    @Field("patentStability")
    private Decimal128 patentStability;//专利稳定性

    @Field("pantentBreadth")
    private Decimal128 pantentBreadth;//专利宽度

    @Field("patentDependence")
    private Decimal128 patentDependence;//专利依赖度

    @Field("nonPatentDependence")
    private Decimal128 nonPatentDependence;//非专利依赖度

}