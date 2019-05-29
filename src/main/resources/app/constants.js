/**
 * Created by xiongwei on 2016/12/28.
 */

(function (angular) {
  'use strict';

  angular.module('CONSTANTS', [])
    .constant('GLOBAL_EVENTS', {
      // auth
      UnAuthenticated: 'UnAuthenticated',
      UnAuthorized: 'UnAuthorized',
      // error
      ServerException: 'ServerException',
      // toast
      PendingToast: 'PendingToast'
    })
    //元数据类型
    .constant('META_TYPE', [
      {id: 1, name: '专利标签元数据'},
      {id: 2, name: '企业信息标签元数据'},
      {id: 3, name: '企业需求标签元数据'},
      {id: 4, name: '专家标签元数据'},
      {id: 5, name: '流程元数据'},
      {id: 6, name: '通用元数据'}
    ])
    //值类型
    .constant('VALUE_TYPE', [
      {id: 1, name: '单行文本'},
      {id: 2, name: '多行文本'},
      {id: 3, name: '时间'},
      {id: 4, name: '附件'},
      {id: 5, name: '数字'},
      {id: 6, name: '布尔'}
    ])
    //标签类型
    .constant('LABEL_TYPE', [
      {id: 1, name: '专利标签'},
      {id: 2, name: '企业信息标签'},
      {id: 3, name: '企业需求标签'},
      {id: 4, name: '专家标签'}
    ])
		//专利类型
    .constant('PATENT_TYPE', {
      "FMZL": "发明专利",
      "SYXX": "实用新型",
      "WGZL": "外观专利",
    })
    //专利标引方式
    .constant('PATENT_INDEX_TYPE', [
      {id: 1, name: '自动获取'},
      {id: 2, name: '自动计算'},
      {id: 3, name: '自动获取人工修改'},
      {id: 4, name: '自动计算人工修改'},
      {id: 5, name: '人工标引'}
    ])
    //专利标签来源
    .constant('PATENT_LABEL_SOURCE', [
      {id: 1, name: '中高'},
      {id: 2, name: '国家知识产权局'},
      {id: 3, name: '出版社'},
      {id: 4, name: 'innography'},
      {id: 5, name: '智慧芽'},
      {id: 6, name: '发明人'},
      {id: 7, name: '五星资产评估'}
    ])
    //企业信息标引方式
    .constant('ENTERPRISE_INFO_INDEX_TYPE', [
      {id: 1, name: '自动获取'},
      {id: 5, name: '人工标引'}
    ])
    //企业信息标签来源
    .constant('ENTERPRISE_INFO_LABEL_SOURCE', [
      {id: 8, name: '中高交易平台'},
      {id: 2, name: '中高'},
      {id: 9, name: '爬虫'}
    ])
    //企业需求标引方式
    .constant('ENTERPRISE_REQUIREMENT_INDEX_TYPE', [
      {id: 1, name: '自动获取'},
      {id: 5, name: '人工标引'}
    ])
    //企业需求标签来源
    .constant('ENTERPRISE_REQUIREMENT_LABEL_SOURCE', [
      {id: 8, name: '中高交易平台'},
      {id: 2, name: '中高'},
    ])
    //专家标签来源
    .constant('PROFESSOR_LABEL_SOURCE', [
      {id: 10, name: '专利书包'}
    ])
    // 节点类型
    .constant('TREE_NODE_TYPES', [
      {id: 0, name: '叶子节点'},
      {id: 1, name: '非叶子节点'}
    ])
    // 实例类型
    .constant('INSTANCE_TYPE',[
      {id: 1, name: '专利实例', key: 'Patent'},
      {id: 2, name: '企业信息实例', key: 'EnterpriseInfo'},
      {id: 3, name: '企业需求实例', key: 'EnterpriseRequire'},
      {id: 4, name: '专家实例', key: 'Expert'},
      {id: 5, name: '匹配实例', key: 'MATCH'},
    ])
    // 专利流程类型
    .constant('PATENT_PROCESS_TYPE', [
      {id: 1, name: '专利批量处理', key: 'batchUpdate',       show: false},
      {id: 2, name: '专利基础标引', key: 'patentBasicIndex',  show: true},
      {id: 5, name: '专利深度标引', key: 'patentDeepIndex',   show: true},
      {id: 3, name: '专利价值评估', key: 'patentValueIndex',  show: true},
      {id: 4, name: '专利价格评估', key: 'patentPriceIndex',  show: true},
      {id: 6, name: '企业信息评估', key: 'enterpriseInfoIndex',  show: false},
      {id: 7, name: '企业需求评估', key: 'enterpriseRequireIndex',  show: false},
      {id: 8, name: '企业需求匹配', key: 'enterpriseRequireMatch',  show: false},
    ])
    .constant('PATENT_PROCESS_IMAGES', [
      {id: 2, url: '/static/images/patentBasicIndex.png'},
      {id: 5, url: '/static/images/patentDeepIndex.png'},
      {id: 4, url: '/static/images/patentPriceIndex.png'},
      {id: 3, url: '/static/images/patentValueIndex.png'},
      {id: 6, url: '/static/images/enterpriseInfoIndex.png'},
      {id: 7, url: '/static/images/enterpriseRequireIndex.png'}
    ])
    // 专利流程环节类型
    .constant('PATENT_TASK_TYPE', [
      {id: 1, name: '派单', key: 'AssignOrder'},
      {id: 2, name: '自动标引', key: 'AutoIndex'},
      {id: 3, name: '人工标引', key: 'ManualIndex'},
      {id: 4, name: '人工标引审核', key: 'ManualIndexAudit'},
      {id: 5, name: '价值评估', key: 'ValueIndex'},
      {id: 6, name: '价格评估', key: 'PriceIndex'},
      {id: 7, name: '深度标引', key: 'DeepIndex'},
      {id: 8, name: '半自动标引', key: 'SemiAutoIndex'},
      {id: 9, name: '人工筛选', key: 'ArtificialSelection'},
      {id: 10, name: '半自动标引审核', key: 'SemiAutoIndexAudit'}
    ])
    // 定单状态
    .constant('PROCESS_ORDER_STATE', [
      {id: 0, name: '未完成'},
      {id: 1, name: '已完成'},
    ])
    // 工单状态
    .constant('TASK_ORDER_STATE', [
      {id: 0, name: '未完成'},
      {id: 1, name: '已完成'},
      {id: 2, name: '被退单'},
    ])
    .constant('TIMED_TASK_SEARCH_TYPES',[
      {name:'同步任务', types: "2,3,4,5,6,7,8,9"},
      {name:'检查更新任务', types: "110,111,112,113,114,115,116,117,118,119,120,130,131,132,133,134,135,136,137,138,139,140"},
      {name:'自动计算任务', types: "200,201,202,203,204,205,206,207,208,209"},
    ])
    // 定时任务类型
    .constant('TIMED_TASK_TYPE', [
      {id: 1, name: '批量更新专利'},
      {id: 2, name: '同步sub1'},
      {id: 3, name: '同步sub3'},
      {id: 4, name: '同步st_legalstatusinfo'},
      {id: 5, name: '同步st_patfeeinfo'},
      {id: 6, name: '同步st_patprsexploitationinfo'},
      {id: 7, name: '同步st_patprspreservationinfo'},
      {id: 8, name: '同步st_patprstransferinfo'},
      {id: 9, name: '同步st_scoreinfo'},
      {id: 10, name: '批量快速计算专利任务0'},
      {id: 11, name: '批量快速计算专利任务1'},
      {id: 12, name: '批量快速计算专利任务2'},
      {id: 13, name: '批量快速计算专利任务3'},
      {id: 14, name: '批量快速计算专利任务4'},
      {id: 15, name: '批量快速计算专利任务5'},
      {id: 16, name: '批量快速计算专利任务6'},
      {id: 17, name: '批量快速计算专利任务7'},
      {id: 18, name: '批量快速计算专利任务8'},
      {id: 19, name: '批量快速计算专利任务9'},
      {id: 20, name: '批量快速计算专利任务10'},
      {id: 21, name: '批量快速计算专利任务11'},
      {id: 22, name: '批量快速计算专利任务12'},
      {id: 23, name: '批量快速计算专利任务13'},
      {id: 24, name: '批量快速计算专利任务14'},
      {id: 25, name: '批量快速计算专利任务15'},
      {id: 26, name: '批量快速计算专利任务16'},
      {id: 27, name: '批量快速计算专利任务17'},
      {id: 28, name: '批量快速计算专利任务18'},
      {id: 29, name: '批量快速计算专利任务19'},
      {id: 30, name: '批量快速计算专利任务20'},
      {id: 31, name: '批量快速计算专利任务21'},
      {id: 32, name: '批量快速计算专利任务22'},
      {id: 33, name: '批量快速计算专利任务23'},
      {id: 34, name: '批量快速计算专利任务24'},
      {id: 35, name: '批量快速计算专利任务25'},
      {id: 36, name: '批量快速计算专利任务26'},
      {id: 37, name: '批量快速计算专利任务27'},
      {id: 38, name: '批量快速计算专利任务28'},
      {id: 39, name: '批量快速计算专利任务29'},
      {id: 110, name: '检查an尾号为0的专利更新'},
      {id: 111, name: '检查an尾号为1的专利更新'},
      {id: 112, name: '检查an尾号为2的专利更新'},
      {id: 113, name: '检查an尾号为3的专利更新'},
      {id: 114, name: '检查an尾号为4的专利更新'},
      {id: 115, name: '检查an尾号为5的专利更新'},
      {id: 116, name: '检查an尾号为6的专利更新'},
      {id: 117, name: '检查an尾号为7的专利更新'},
      {id: 118, name: '检查an尾号为8的专利更新'},
      {id: 119, name: '检查an尾号为9的专利更新'},
      {id: 120, name: '检查an尾号为X的专利更新'},
			{id: 200, name: '自动计算专利id尾号为0的专利'},
			{id: 201, name: '自动计算专利id尾号为1的专利'},
			{id: 202, name: '自动计算专利id尾号为2的专利'},
			{id: 203, name: '自动计算专利id尾号为3的专利'},
			{id: 204, name: '自动计算专利id尾号为4的专利'},
			{id: 205, name: '自动计算专利id尾号为5的专利'},
			{id: 206, name: '自动计算专利id尾号为6的专利'},
			{id: 207, name: '自动计算专利id尾号为7的专利'},
			{id: 208, name: '自动计算专利id尾号为8的专利'},
			{id: 209, name: '自动计算专利id尾号为9的专利'},
    ])
    .constant('EXTRA_VALUE_LABELS', [
      {
        key: 'ecoValueSimilarPatentNumCV',
        name: '经济价值相似专利数量计算值'
      },
      {
        key: 'durationOfPatentCV',
        name: '保护年限计算值'
      },{
        key: 'costHandingStatusCV',
        name: '费用处理状态计算值'
      },
      {
        key: 'familyPatentNumCV',
        name: '同族专利数量计算值'
      },{
        key: 'familyPatentCodeCV',
        name: '同族专利号计算值'
      },
      {
        key: 'inventorNumCV',
        name: '发明人数量计算值'
      },{
        key: 'classiflcationCodeNumCV',
        name: '分类号数量计算值'
      },
      {
        key: 'nonPatentQuotationNumCV',
        name: '非专利引用数量计算值'
      },{
        key: 'patentReferenceNumCV',
        name: '专利引用数量计算值'
      },
      {
        key: 'classiflcationCodeNumCV',
        name: '分类号数量计算值'
      },{
        key: 'tecValueIndependentClaimsNumCV',
        name: '技术价值独立权利要求数计算值'
      },
      {
        key: 'tecValueDependentClaimsNumCV',
        name: '技术价值从属权利要求数计算值'
      },{
        key: 'tecValueSimilarPatentNumCV',
        name: '技术价值相似专利数量计算值'
      },
      {
        key: 'meanOfSimilarityCV',
        name: '相似度均值计算值'
      },{
        key: 'pattypeCV',
        name: '专利类型计算值'
      },
      {
        key: 'lastLegalStatusCV',
        name: '最新法律状态计算值'
      },{
        key: 'citedPatentNumCV',
        name: '引证专利数计算值'
      },
      {
        key: 'nonPatentReferenceNumCV',
        name: '非专利引文数计算值'
      },{
        key: 'lawValueIndependentClaimsNumCV',
        name: '法律价值独立权利要求数计算值'
      },{
        key: 'lawValueDependentClaimsNumCV',
        name: '法律价值从属权利要求数计算值'
      },{
        key: 'familyPatentDistribution',
        name: '同族专利分布'
      }
    ])
    // 默认省份高校
    .constant('DEFAULT_PROVINCE_COLLEGE', {
			provinceName: "江苏省",
			collegeName: "南京理工大学",
		})
		// 默认省份高校专家
    .constant('DEFAULT_PROVINCE_COLLEGE_PIN', {
      provinceName: "江苏省",
      collegeName: "南京理工大学",
      pinName: "王辉"
    })
  ;
})(angular);