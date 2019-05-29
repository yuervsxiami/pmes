package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.domain.core.Patent;
import com.cnuip.pmes2.domain.core.TaskOrderLabel;
import com.cnuip.pmes2.service.PatentEvaluateService;
import com.cnuip.pmes2.service.PatentService;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * PatentEvaluateServiceImpl
 *
 * @author: xiongwei
 * Date: 2018/3/5 下午3:51
 */
@Service
public class PatentEvaluateServiceImpl implements PatentEvaluateService {

    private final Logger logger = LoggerFactory.getLogger(PatentEvaluateServiceImpl.class);

    @Autowired
    private PatentService patentService;

    @Cacheable(value = "RedisCache", key = "#an + '-PatentValueExp'")
    @Override
    public Map<String, String> evaluatePatentValue(String an) {
        Patent patent = patentService.findPatentByAnWithFullLabels(an);
        if (patent != null) {
            Map<String, String> labels = new HashMap<>();
            for (TaskOrderLabel label: patent.getLatestLabels()) {
                String strVal = label.getStrValue();
                labels.put(label.getLabel().getKey(), Strings.isNullOrEmpty(strVal) ? label.getTextValue() : strVal);
            }
            if (labels.size() > 0) {
                try {
                    return this.expPatentValues(labels);
                } catch (Exception e) {
                    logger.error("解读价值评估出错", e);
                }
            }
        }
        return null;
    }

    @Override
    public Map<String, String> expPatentValues(Map<String, String> values) {
        Map<String, String> valueExps = new HashMap<>();

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        //1.生成法律价值评分解读
        String a1 = values.get("legalValue");
        double a11 = Double.parseDouble(a1);
        a1 = nf.format(a11 * 100);
        String b1 = pj5(0.4, 0.5, 0.7, 0.9, a11);

        String c1 = values.get("stabilityOfLegalStatus");
        double c11 = Double.parseDouble(c1);
        c1 = nf.format(c11 * 100);
        String d1 = pj5(0.3, 0.5, 0.7, 0.75, c11);

        String e1 = values.get("patentStability");
        double e11 = Double.parseDouble(e1);
        e1 = nf.format(e11 * 100);
        String f1 = pj5(0.3, 0.6, 0.8, 0.9, e11);

        String g1 = values.get("patentDependence");
        double g11 = Double.parseDouble(g1);
        g1 = nf.format(g11 * 100);
        String h1 = pj4(0.5, 0.7, 0.9, g11);

        String i1 = values.get("nonPatentDependence");
        double i11 = Double.parseDouble(i1);
        i1 = nf.format(i11 * 100);
        String j1 = pj4(0.4, 0.8, 0.9, i11);

        String k1 = values.get("dependency");
        double k11 = Double.parseDouble(k1);
        k1 = nf.format(k11 * 100);
        String l1 = pj5(0.2, 0.3, 0.5, 0.8, k11);

        String m1 = values.get("scopeOfRightProtection");
        double m11 = Double.parseDouble(m1);
        m1 = nf.format(m11 * 100);
        String n1 = pj5(0.4, 0.6, 0.75, 0.85, m11);

        String o1 = values.get("gengraphicalCoverage");
        double o11 = Double.parseDouble(o1);
        o1 = nf.format(o11 * 100);
        String p1 = pj4(0.4, 0.6, 0.91, o11);

        String q1 = values.get("pantentBreadth");
        double q11 = Double.parseDouble(q1);
        q1 = nf.format(q11 * 100);
        String r1 = pj5(0.4, 0.6, 0.8, 0.85, q11);

        String fr = "该专利的法律价值总体得分为" + a1 + "，法律价值" + b1 + "；"
                + "首先，专利法律地位稳固度得分为" + c1 + "，法律地位稳固度" + d1 + "。因此，专利稳定性得分为" + e1 + "，专利稳定性" + f1 + "。"
                + "其次，专利依赖度得分为" + g1 + "，专利依赖度" + h1 + "，非专利依赖度得分为" + i1 + "，非专利依赖度" + j1 + "。因此，依赖性得分为" + k1 + "，依赖性" + l1 + "；"
                + "最后，权利保护范围得分为" + m1 + "，权利保护范围" + n1 + "，地域保护范围得分为" + o1 + "，地域保护范围" + p1 + "。因此，专利宽度得分为" + q1 + "，专利宽度" + r1 + "。";

        valueExps.put("fljz", fr);

        //2.生成经济价值评分解读
        String a2 = values.get("economicValue");
        double a22 = Double.parseDouble(a2);
        a2 = nf.format(a22 * 100);
        String b2 = pj5(0.4, 0.5, 0.65, 0.9, a22);

        String c2 = values.get("marketCompetitivePower");
        double c22 = Double.parseDouble(c2);
        c2 = nf.format(c22 * 100);
        String d2 = pj3(0.5, 0.8, c22);

        String e2 = values.get("marketCompetitiveness");
        double e22 = Double.parseDouble(e2);
        e2 = nf.format(e22 * 100);
        String f2 = pj5(0.3, 0.5, 0.65, 0.8, e22);

        String g2 = values.get("timeLimitOfPatent");
        double g22 = Double.parseDouble(g2);
        g2 = nf.format(g22 * 100);
        String h2 = pj5(0.3, 0.55, 0.75, 0.9, g22);

        String i2 = values.get("patentMaintenance");
        double i22 = Double.parseDouble(i2);
        i2 = nf.format(i22 * 100);
        String j2 = pj5(0.3, 0.6, 0.8, 0.9, i22);

        String k2 = values.get("patentEconomicLife");
        double k22 = Double.parseDouble(k2);
        k2 = nf.format(k22 * 100);
        String l2 = pj5(0.3, 0.5, 0.65, 0.8, k22);

        String m2 = values.get("patentFamilySize"); // 专利族规模
        double m22 = Double.parseDouble(m2);
        m2 = nf.format(m22 * 100);
        String n2 = pj4(0.4, 0.5, 0.8, m22);

        String o2 = values.get("marketCompetitivePower"); //
        double o22 = Double.parseDouble(o2);
        o2 = nf.format(o22 * 100);
        String p2 = pj4(0.4, 0.6, 0.9, o22);

        String q2 = values.get("patentPortfolio"); // patentPortfolio
        double q22 = Double.parseDouble(q2);
        q2 = nf.format(q22 * 100);
        String r2 = pj5(0.2, 0.26, 0.45, 0.75, q22);

        String jr = "该专利的经济价值总体得分为" + a2 + "，经济价值" + b2 + "；"
                + "首先，市场竞争得分为" + c2 + "，市场竞争" + d2 + "。因此，市场竞争能力得分为" + e2 + "，竞争能力" + f2 + "。"
                + "再者，专利时限得分为" + g2 + "，维护长度" + h2 + "；专利维持状态得分为" + i2 + "，维持状态" + j2 + "；因此，专利经济寿命得分为" + k2 + "，寿命" + l2 + "。"
                + "最后，专利族规模得分为" + m2 + "，规模" + n2 + "；专利族地域分布得分为" + o2 + "，分布范围" + p2 + "；因此，专利布局得分为" + q2 + "，专利布局" + r2 + "。";

        valueExps.put("jjjz", jr);

        //3.生成技术价值评分解读
        String a3 = values.get("technologicalValue");
        double a33 = Double.parseDouble(a3);
        a3 = nf.format(a33 * 100);
        String b3 = pj5(0.65, 0.75, 0.81, 0.9, a33);

        String c3 = values.get("patentNovelty");
        double c33 = Double.parseDouble(c3);
        c3 = nf.format(c33 * 100);
        String d3 = pj4(0.6, 0.8, 0.9, c33);

        String e3 = values.get("technologyFungibility");
        double e33 = Double.parseDouble(e3);
        e3 = nf.format(e33 * 100);
        String f3 = pj5(0.6, 0.7, 0.85, 0.9, e33);

        String g3 = values.get("technicalCrossover");
        double g33 = Double.parseDouble(g3);
        g3 = nf.format(g33 * 100);
        String h3 = pj5(0.5, 0.7, 0.8, 0.9, g33);

        String i3 = values.get("scientificCorrelationStrength");
        double i33 = Double.parseDouble(i3);
        i3 = nf.format(i33 * 100);
        String j3 = pj5(0.6, 0.71, 0.81, 0.91, i33);

        String k3 = values.get("groupInfluence");
        double k33 = Double.parseDouble(k3);
        k3 = nf.format(k33 * 100);
        String l3 = pj5(0.6, 0.8, 0.85, 0.9, k33);

        String m3 = values.get("technologyAdvancement");
        double m33 = Double.parseDouble(m3);
        m3 = nf.format(m33 * 100);
        String n3 = pj5(0.5, 0.65, 0.8, 0.9, m33);

        String o3 = values.get("technicalCoverage");
        double o33 = Double.parseDouble(o3);
        o3 = nf.format(o33 * 100);
        String p3 = pj5(0.5, 0.6, 0.8, 0.95, o33);

        String q3 = values.get("technicalDegree");
        double q33 = Double.parseDouble(q3);
        q3 = nf.format(q33 * 100);
        String r3 = pj5(0.55, 0.7, 0.8, 0.9, q33);

        String s3 = values.get("technicalApplicationRange");
        double s33 = Double.parseDouble(s3);
        s3 = nf.format(s33 * 100);
        String t3 = pj5(0.6, 0.7, 0.77, 0.9, s33);

        String tr = "该专利的技术价值总体得分为" + a3 + "，技术价值" + b3 + "；"
                + "首先，专利新颖度得分为" + c3 + "，新颖度" + d3 + "；因此，技术可替代性得分为" + e3 + "，可替代性" + f3 + "。"
                + "其次，技术交叉性得分为" + g3 + "，交叉性" + h3 + "；科学关联强度得分为" + i3 + "，关联强度" + j3 + "；团队影响力得分为" + k3 + "，团队影响力" + l3 + "。因此，技术先进性得分为" + m3 + "，技术先进性" + n3 + "。"
                + "最后，技术覆盖度得分为" + o3 + "，技术覆盖度" + p3 + "；技术专业度得分为" + q3 + "，技术专业度" + r3 + "。因此，技术应用范围得分为" + s3 + "，技术应用范围" + t3 + "。";

        valueExps.put("jsjz", tr);

        return valueExps;
    }

    //3级评价
    private String pj3(double a, double b, double k) {
        String pj = null;
        if (k < a) {
            pj = "差";
        } else if (k < b) {
            pj = "中等";
        } else {
            pj = "优秀";
        }
        return pj;
    }

    //4级评价
    private String pj4(double a, double b, double c, double k) {
        String pj = null;
        if (k < a) {
            pj = "差";
        } else if (k < b) {
            pj = "一般";
        } else if (k < c) {
            pj = "良好";
        } else {
            pj = "优秀";
        }
        return pj;
    }

    //5级评价
    private String pj5(double a, double b, double c, double d, double k) {
        String pj = null;
        if (k < a) {
            pj = "差";
        } else if (k < b) {
            pj = "一般";
        } else if (k < c) {
            pj = "中等";
        } else if (k < d) {
            pj = "良好";
        } else {
            pj = "优秀";
        }
        return pj;
    }

}
