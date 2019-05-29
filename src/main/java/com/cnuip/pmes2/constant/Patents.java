package com.cnuip.pmes2.constant;

/**
 * Patents
 *
 * @author: xiongwei
 * Date: 2018/1/28 下午3:38
 */
public abstract class Patents {

    public enum Types {
        FMZL(1, "发明专利"),
        SYXX(2, "实用新型"),
        WGZL(3, "外观设计"),
        FMSQ(4, "发明授权");

        private Integer value;
        private String name;
        Types(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 元数据类型
     */
    public enum MetaType {
        Patent(1, "专利标签元数据"),
        EnterpriseInfo(2, "企业信息标签元数据"),
        EnterpriseRequire(3, "企业需求标签元数据"),
        Expert(4, "专家标签元数据"),
        Process(5, "流程元数据"),
        Common(6, "通用元数据");

        private Integer value;
        private String name;
        MetaType(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 标签值类型
     */
    public enum LabelValueType {
        Text(1, "单行文本"),
        Text_Multi(2, "多行文本"),
        Time(3, "时间"),
        Attachment(4, "附件"),
        Number(5, "数字"),
        Boolean(6, "布尔");

        private Integer value;
        private String name;
        LabelValueType(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 标签类型
     */
    public enum LabelType {
        Patent(1, "专利标签"),
        EnterpriseInfo(2, "企业信息标签"),
        EnterpriseRequire(3, "企业需求标签"),
        Expert(4, "专家标签");

        private Integer value;
        private String name;
        LabelType(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 流程定单类型
     */
    public enum ProcessOrderType {

        Patent(1), // 专利
        EnterpriseInfo(2), // 企业信息
        EnterpriseRequire(3), // 企业需求
        Expert(4); // 专家

        private Integer value;
        ProcessOrderType(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    /**
     * 流程类型
     */
    public enum ProcessType {

        Patent(1, ""), // 专利
        EnterpriseInfo(2, ""), // 企业信息
        EnterpriseRequire(3, ""), // 企业需求
        Expert(4, ""); // 专家

        private Integer value;
        private String name;
        ProcessType(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 专利索引类型
     */
    public enum PatentIndexType {
        AutoGet(1, "自动获取"),
        AutoCompute(2, "自动计算"),
        HalfAutoGet(3, "自动获取人工修改"),
        HalfAutoCompute(4, "自动计算人工修改"),
        Manual(5, "人工标引");

        private Integer value;
        private String name;
        PatentIndexType(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 专利标签来源
     */
    public enum PatentLabelSource {
        ZhongGao(1, "中高"),
        SIPO(2, "国家知识产权局"),
        CNIPR(3, "出版社"),
        Innography(4, "innography"),
        Zhihuiya(5, "智慧芽"),
        Inventor(6, "发明人"),
        FiveStars(7, "五星资产评估"),
        CNUIP(8, "中高交易平台"),
        Spider(9, "爬虫"),
        PatentBag(10, "专利书包"); //

        private Integer value;
        private String name;
        PatentLabelSource(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

}
