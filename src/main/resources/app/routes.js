/**
 * Created by xiongwei on 2017/1/3.
 */

(function (angular, CONTEXT_PATH) {
  'use strict';

  angular
    .module('PMES2')
    .config(['$locationProvider', '$stateProvider', '$urlRouterProvider', function ($locationProvider, $stateProvider, $urlRouterProvider) {
      // 关闭html5模式
      $locationProvider.html5Mode(false);
      // ui router
      $stateProvider
        .state('main', {
          abstract: true,
          templateUrl: CONTEXT_PATH + '/static/tpls/content.html'
        })
        .state('login', {
          url: '/login',
          templateUrl: CONTEXT_PATH + '/static/tpls/login.html',
          controller: 'LoginCtrl',
        })
        .state('main.console', {
          abstract: true,
          views: {
            'header@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/header/index.html',
              controller: 'NavCtrl',
              controllerAs: 'nav'
            },
            'sidebar@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/sidebar/index.html',
              controller: 'SidebarCtrl',
              controllerAs: 'sidebar'
            },
          }
        })
        // dashboard
        .state('main.console.dashboard', {
          url: '/dashboard'
        })
        // 个人工作台
        .state('main.console.dashboard.personal', {
          url: '/personal',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/dashboard/personal.html',
              controller: 'DashboardPersonalController',
              controllerAs: 'dpc'
            }
          }
        })
        // 专利加工管理员工作台
        .state('main.console.dashboard.patent', {
          url: '/patent',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/dashboard/patent.html',
              controller: 'PatentDashboardCtrl',
              controllerAs: 'pdc'
            }
          }
        })
        // 企业信息管理员工作台
        .state('main.console.dashboard.enterprise', {
          url: '/enterprise',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/dashboard/enterprise.html',
              controller: 'EnterpriseDashboardCtrl',
              controllerAs: 'pdc'
            }
          }
        })
        // 企业需求管理员工作台
        .state('main.console.dashboard.enterpriseReq', {
          url: '/enterpriseReq',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/dashboard/enterpriseReq.html',
              controller: 'EnterpriseDashboardCtrl',
              controllerAs: 'pdc'
            }
          }
        })
        // 企业信息管理员工作台
        .state('main.console.dashboard.match', {
          url: '/match',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/dashboard/match.html',
              controller: 'EnterpriseDashboardCtrl',
              controllerAs: 'pdc'
            }
          }
        })
        // 系统管理
        .state('main.console.system', {
          url: '/system'
        })
        // 用户管理
        .state('main.console.system.users', {
          url: '/users',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/system/user/users.html',
              controller: 'UserCtrl',
              controllerAs: 'user'
            }
          }
        })
        // 添加用户
        .state('main.console.system.users.add', {
          url: '/add',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/system/user/add.html',
              controller: 'UserEditController',
              controllerAs: 'UserEditCtrl'
            }
          }
        })
        //  用户编辑
        .state('main.console.system.users.edit', {
          url: '/edit/:id',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/system/user/edit.html',
              controller: 'UserEditController',
              controllerAs: 'UserEditCtrl'
            }
          }
        })
        // 角色管理
        .state('main.console.system.roles', {
          url: '/roles',
          views: {
            'content@main': {
              templateUrl:  CONTEXT_PATH + '/static/tpls/system/role/roles.html',
              controller: 'RoleCtrl',
              controllerAs: 'role'
            }
          }
        })
        // 查看角色权限
        .state('main.console.system.roles.check', {
          url: '/check/:id',
          views: {
            'content@main': {
              templateUrl:  CONTEXT_PATH + '/static/tpls/system/role/roleCheck.html',
              controller: 'RoleCheckController',
              controllerAs: 'roleCheck'
            }
          }
        })
        // 权限管理
        .state('main.console.system.authority', {
          url: '/authority',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/system/authority/authority.html',
              controller: 'AuthorityCtrl',
              controllerAs: 'authority',
            }
          }
        })
        // 组织机构管理
        .state('main.console.system.organization', {
          url: '/organization',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/system/organization/organization.html',
              controller: 'OrganizationCtrl',
              controllerAs: 'organization',
            }
          }
        })
        // 配置管理
        .state('main.console.configuration', {
          url: '/configuration'
        })
        // 元数据管理
        .state('main.console.configuration.metas', {
          url: '/metas',
          views: {
              'content@main': {
                templateUrl: CONTEXT_PATH + '/static/tpls/configuration/meta/metas.html',
                controller: 'MetaCtrl',
                controllerAs: 'meta',
              }
          }
        })
        // 标签管理
        .state('main.console.configuration.labels', {
          url: '/labels',
          views: {
              'content@main': {
                templateUrl: CONTEXT_PATH + '/static/tpls/configuration/label/labels.html',
                controller: 'LabelController',
                controllerAs: 'label',
              }
          }
        })
        // 标签体系管理
        .state('main.console.configuration.label-sets', {
          url: '/labelsets',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/configuration/labelset/labelsets.html',
              controller: 'LabelsetController',
              controllerAs: 'labelset',
            }
          }
        })
        // 流程模版管理
        .state('main.console.configuration.processes', {
          url: '/processes',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/configuration/process/processes.html',
              controller: 'ProcessController',
              controllerAs: 'process',
            }
          }
        })
        // 环节管理
        .state('main.console.configuration.tasks', {
          url: '/tasks',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/configuration/task/tasks.html',
              controller: 'ProcessTaskController',
              controllerAs: 'processTask',
            }
          }
        })
        // 流程
        .state('main.console.process', {
          url: '/process',
          abstract: true
        })
        .state('main.console.process.order', {
          url: '/order',
          abstract: true
        })
        // 专利定单查询
        .state('main.console.process.order.patent', {
          url: '/patent?instanceType',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/processorder/patent.html',
              controller: 'ProcessOrderQueryController',
              controllerAs: 'poq'
            }
          }
        })
        // 企业需求定单查询
        .state('main.console.process.order.requirement', {
          url: '/requirement?instanceType',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/processorder/requirement.html',
              controller: 'ProcessOrderQueryController',
              controllerAs: 'poq'
            }
          }
        })
        // 流程定单详情
        .state('main.console.process.order.detail', {
          url: '/detail/:orderId',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/processorder/detail.html',
              controller: 'ProcessDetailController',
              controllerAs: 'pdc'
            }
          }
        })
        // 多流程定单详情
        .state('main.console.process.order.details', {
          url: '/details?instanceType&instanceId',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/processorder/details.html',
              controller: 'ProcessDetailsController',
              controllerAs: 'pdcs'
            }
          }
        })
        // 专利
        .state('main.console.patent', {
          url: '/patent',
          abstract: true
        })
        // 启动专利流程
        .state('main.console.patent.start', {
          url: '/start',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/patent/start/index.html',
              controller: 'PatentProcessStartController',
              controllerAs: 'pps'
            }
          }
        })
        // 专利价值快速评估
        .state('main.console.patent.assessment', {
          url: '/assessment',
        })
        // 人工快速评估
        .state('main.console.patent.assessment.humen', {
          url: '/human',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/patent/assessment/humen/index.html',
              controller: 'HumenController',
              controllerAs: 'humen'
            }
          }
        })
        // 快速评估查询
          .state('main.console.patent.assessment.list', {
              url: '/humanList',
              views: {
                  'content@main': {
                      templateUrl: CONTEXT_PATH + '/static/tpls/patent/assessment/humenList/index.html',
                      controller: 'HumenController',
                      controllerAs: 'humen'
                  }
              }
          })
        // 人工快速评估详情
        .state('main.console.patent.assessment.detail', {
          url: '/detail/:id',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/patent/assessment/humen/detail.html',
              controller: 'AssessmentController',
              controllerAs: 'ac'
            }
          }
        })
        // 任务跟踪
        .state('main.console.patent.assessment.task', {
          url: '/task',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/patent/assessment/task/index.html',
              controller: 'TimedTaskController',
              controllerAs: 'ttc'
            }
          }
        })
				// 任务跟踪
					.state('main.console.patent.assessment.task.detail', {
						url: '/detail/:id',
						views: {
							'content@main': {
								templateUrl: CONTEXT_PATH + '/static/tpls/patent/assessment/task/detail.html',
								controller: 'TimedTaskDetailController',
								controllerAs: 'ttdc'
							}
						}
					})
				// 错误记录
        .state('main.console.patent.assessment.errLog', {
          url: '/errLog',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/patent/assessment/task/errLog.html',
              controller: 'IndexErrorController',
              controllerAs: 'ie'
            }
          }
        })
        // 专利流程定单
        .state('main.console.patent.process', {
          url: '/process',
          abstract: true
        })
        // 专利工单
        .state('main.console.patent.process.order', {
          url: '/order?processType&taskType&actTaskId',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/patent/order/index.html',
              controller: 'TaskOrderController',
              controllerAs: 'taskOrderCtrl'
            }
          }
        })
        // 专利标引工单
        .state('main.console.patent.process.index', {
          url: '/index?processType&taskType&taskOrderId&backUrl',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/instance/indexOrder.html',
              controller: 'IndexOrderController',
              controllerAs: 'indexOrderCtrl'
            }
          }
        })
        // 专利审核工单
        .state('main.console.patent.process.audit', {
          url: '/audit?processType&taskType&taskOrderId',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/instance/audit.html',
              controller: 'AuditOrderController',
              controllerAs: 'auditCtrl'
            }
          }
        })
        // 价值评估评估详情
        .state('main.console.patent.process.detail', {
          url: '/detail/:id?orderId',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/instance/valueIndexDetail.html',
              controller: 'ValueIndexDetailController',
              controllerAs: 'vid'
            }
          }
        })
        // 专利标签变更管理
        .state('main.console.patent.modify', {
          url: '/modify',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/search/patent/index.html',
              controller: 'PatentSearchController',
              controllerAs: 'psc'
            }
          },
          resolve: {
            modifyFlag: function () {
              return true;
            }
          }
        })
        .state('main.console.patent.modify.detail', {
          url: '/detail/:id',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/patent/modify/index.html',
              controller: 'PatentModifyController',
              controllerAs: 'pmc'
            }
          }
        })
        // 企业信息流程
        .state('main.console.enterprise', {
          url: '/enterprise',
          abstract: true
        })
        // 企业信息列表，流程启动入口
        .state('main.console.enterprise.start', {
          url: '/start',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/enterprise/start.html',
              controller: 'EnterpriseListCtrl',
              controllerAs: 'elc'
            }
          }
        })
        // 企业信息添加和修改
        .state('main.console.enterprise.add', {
          url: '/add',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/enterprise/edit.html',
              controller: 'EnterpriseEditCtrl',
              controllerAs: 'eec'
            }
          },
          resolve: {
            isEdit: function () {
              return false;
            }
          }
        })
        .state('main.console.enterprise.edit', {
          url: '/edit/:id',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/enterprise/edit.html',
              controller: 'EnterpriseEditCtrl',
              controllerAs: 'eec'
            }
          },
          resolve: {
            isEdit: function () {
              return true;
            }
          }
        })
        // 企业信息流程定单
        .state('main.console.enterprise.process', {
          url: '/process',
          abstract: true
        })
        // 企业信息工单
        .state('main.console.enterprise.process.order', {
          url: '/order?processType&taskType&actTaskId',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/enterprise/info/index.html',
              controller: 'TaskOrderController',
              controllerAs: 'taskOrderCtrl'
            }
          }
        })

        // 企业需求流程
        .state('main.console.enterpriseRequirement', {
          url: '/requirement',
          abstract: true
        })
        // 企业信息列表，流程启动入口
        .state('main.console.enterpriseRequirement.start', {
          url: '/start',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/enterprise/require.html',
              controller: 'EnterpriseRequirementListCtrl',
              controllerAs: 'erlc'
            }
          }
        })
        // 企业需求流程定单
        .state('main.console.enterpriseRequirement.process', {
          url: '/process',
          abstract: true
        })
        // 企业需求工单
        .state('main.console.enterpriseRequirement.process.order', {
          url: '/order?processType&taskType&actTaskId',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/enterprise/require/index.html',
              controller: 'TaskOrderController',
              controllerAs: 'taskOrderCtrl'
            }
          }
        })
        // 匹配管理
        .state('main.console.match', {
          url: '/match',
          abstract: true
        })
        // 匹配专利
        .state('main.console.match.patent', {
          url: '/patent',
          abstract: true
        })
        // 匹配专利流程定单
        .state('main.console.match.patent.process', {
          url: '/process',
          abstract: true
        })
        // 匹配专利工单
        .state('main.console.match.patent.process.order', {
          url: '/order?processType&taskType&actTaskId',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/enterprise/match/patent.html',
              controller: 'TaskOrderController',
              controllerAs: 'taskOrderCtrl'
            }
          }
        })
        // 企业需求匹配专利详情
        .state('main.console.match.patent.detail', {
          url: '/detail?taskOrderId&keywords&backUrl',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/enterprise/match/detail.html',
              controller: 'ErPMatchDetailController',
              controllerAs: 'matchDetailCtrl'
            }
          }
        })
				// 企业需求匹配专家
        .state('main.console.match.rmp', {
          url: '/rmp',
					views: {
						'content@main': {
							templateUrl: CONTEXT_PATH + '/static/tpls/match/professor/index.html',
							controller: 'MatchProfessorController',
							controllerAs: 'matchCtrl'
						}
					}
        })
				// 企业需求匹配专家详情
        .state('main.console.match.rmp.detail', {
          url: '/detail/:id',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/match/professor/detail.html',
              controller: 'MatchProfessorDetailController',
              controllerAs: 'matchDetailCtrl'
            }
          }
        })
				// 企业需求匹配专家匹配
        .state('main.console.match.rmp.match', {
          url: '/match/:id',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/match/professor/match.html',
              controller: 'MatchProfessorActionController',
              controllerAs: 'matchCtrl'
            }
          }
        })
				// 企业需求匹配专家结果
        .state('main.console.match.rmp.result', {
          url: '/result/:id',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/match/professor/result.html',
              controller: 'MatchProfessorResultController',
              controllerAs: 'matchCtrl'
            }
          }
        })

				// 企业需求匹配专家匹配
        .state('main.console.match.pk', {
          url: '/pk',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/match/professor/pk.html',
              controller: 'PKController',
              controllerAs: 'pkCtrl'
            }
          }
        })
        // 综合查询
        .state('main.console.search', {
          url: '/search',
          abstract: true
        })
        //专利分组检索
        .state('main.console.search.group', {
          url: '/group',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/search/group/index.html',
              controller: 'GroupSearchController',
              controllerAs: 'gsc'
            }
          },
          resolve: {
            modifyFlag: function () {
              return false;
            }
          }
        })
        //添加分组
        .state('main.console.search.group.mainGroup', {
          url: '/mainGroup/',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/search/group/mainGroup.html',
              controller: 'GroupSearchController',
              controllerAs: 'gsc'
            }
          },
          resolve: {
            modifyFlag: function () {
              return false;
            }
          }
        })
        //添加子分组
        .state('main.console.search.group.childGroup', {
          url: '/childGroup/:id',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/search/group/childGroup.html',
              controller: 'GroupSearchController',
              controllerAs: 'gsc'
            }
          }
        })
        // 专利分组检索详情
        .state('main.console.search.group.detail', {
          url: '/detail/:id',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/search/patent/detail.html',
              controller: 'GroupDetailController',
              controllerAs: 'gdc'
            }
          }
        })

        // 专利查询
        .state('main.console.search.patent', {
          url: '/patent',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/search/patent/index.html',
              controller: 'PatentSearchController',
              controllerAs: 'psc'
            }
          },
          resolve: {
            modifyFlag: function () {
              return false;
            }
          }
        })
        // 专利查询
        .state('main.console.search.patent.detail', {
          url: '/detail/:id',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/search/patent/detail.html',
              controller: 'PatentDetailController',
              controllerAs: 'pdc'
            }
          }
        })
        // 企业查询
        .state('main.console.search.enterprise', {
          url: '/enterprise',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/search/enterprise/index.html',
              controller: 'EnterpriseSearchController',
              controllerAs: 'esc'
            }
          }
        })
        .state('main.console.search.enterprise.detail', {
          url: '/detail/:id',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/search/enterprise/detail.html',
              controller: 'EnterpriseDetailController',
              controllerAs: 'edc'
            }
          }
        })
        // 企业需求查询
        .state('main.console.search.requirement', {
          url: '/requirement',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/search/requirement/index.html',
              controller: 'RequirementSearchController',
              controllerAs: 'rsc'
            }
          }
        })
        .state('main.console.search.requirement.detail', {
          url: '/detail/:id',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/search/requirement/detail.html',
              controller: 'RequirementDetailController',
              controllerAs: 'rdc'
            }
          }
        })
        // 搜索
        .state('main.console.elsearch', {
          url: '/elsearch',
          abstract: true
        })
        // 专利搜索
        .state('main.console.elsearch.patent', {
          url: '/patent?keywords',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/elsearch/patent/index.html',
              controller: 'ElPatentCtrl',
              controllerAs: 'elp'
            }
          }
        })
        // 消息列表
        .state('main.console.message', {
          url: '/message',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/message/index.html',
              controller: 'MessageController',
              controllerAs: 'mes'
            }
          }
        })
        // 专家管理
        .state('main.console.expert', {
            url: '/expert',
            abstract: true
        })
        //专家标引
        .state('main.console.expert.list', {
            url: '/list',
            views: {
                'content@main': {
                    templateUrl: CONTEXT_PATH + '/static/tpls/expert/index.html',
                    controller: 'ExpertController',
                    controllerAs: 'ExCtrl'
                }
            }
        })
        .state('main.console.expert.add', {
            url: '/add',
            views: {
                'content@main': {
                    templateUrl: CONTEXT_PATH + '/static/tpls/expert/edit.html',
                    controller: 'ExpertEditController',
                    controllerAs: 'ExEditCtrl'
                }
            }
        })
        .state('main.console.expert.update', {
            url: '/update/:id',
            views: {
                'content@main': {
                    templateUrl: CONTEXT_PATH + '/static/tpls/expert/edit.html',
                    controller: 'ExpertEditController',
                    controllerAs: 'ExEditCtrl'
                }
            }
        })
        .state('main.console.expert.detail', {
            url: '/detail/:id',
            views: {
                'content@main': {
                    templateUrl: CONTEXT_PATH + '/static/tpls/expert/detail.html',
                    controller: 'ExpertEditController',
                    controllerAs: 'ExEditCtrl'
                }
            }
        })
        // 专家库
        .state('main.console.expert.library', {
          url: '/library',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/expert/library.html',
              controller: 'ExpertLibraryController',
              controllerAs: 'ExLiCtrl'
            }
          }
        })
        .state('main.console.expert.libraryAdd', {
          url: '/libraryAdd',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/expert/libraryEdit.html',
              controller: 'ExpertLibraryEditController',
              controllerAs: 'ExLiEditCtrl'
            }
          }
        })
				// 统计
        .state('main.console.statistics', {
          url: '/statistics',
          abstract: true
        })
        .state('main.console.statistics.workload', {
          url: '/workload',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/statistics/workload.html',
              controller: 'WorkloadCtrl',
              controllerAs: 'wlCtrl'
            }
          }
        })
        //专利流程定单完成量
        .state('main.console.statistics.workload.patentIndexOrder', {
          url: '/patentIndexOrder',
          views: {
            'statistics@main.console.statistics.workload': {
              templateUrl: CONTEXT_PATH + '/static/tpls/statistics/workload/patentIndexOrder.html',
              controller: 'patentIndexOrderController',
              controllerAs: 'pioCtrl'
            }
          }
        })
				//各流程定单完成量
        .state('main.console.statistics.workload.indexOrder', {
          url: '/indexOrder',
          views: {
            'statistics@main.console.statistics.workload': {
              templateUrl: CONTEXT_PATH + '/static/tpls/statistics/workload/indexOrder.html',
              controller: 'indexOrderController',
              controllerAs: 'pioCtrl'
            }
          }
        })
        .state('main.console.statistics.patent', {
          url: '/patent',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/statistics/patent.html',
              controller: 'WorkloadCtrl',
              controllerAs: 'wlCtrl'
            }
          }
        })
				//专利价值分布和平均分
        .state('main.console.statistics.patent.patentValue', {
          url: '/patentValue',
          views: {
            'statistics@main.console.statistics.patent': {
              templateUrl: CONTEXT_PATH + '/static/tpls/statistics/patent/patentValue.html',
              controller: 'patentPatentValueController',
              controllerAs: 'pioCtrl'
            }
          }
        })
        // 地区统计
        .state('main.console.statistics.area', {
          url: '/area',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/statistics/area.html',
              controller: 'AreaStatCtrl',
              controllerAs: 'areaCtrl'
            }
          }
        })
				// 高校省份专利统计
        .state('main.console.statistics.area.patent', {
          url: '/patent?provinceName',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/statistics/area/collegeProvincePatent.html',
              controller: 'CollegeProvincePatentCtrl',
              controllerAs: 'cppCtrl'
            }
          }
        })
				// 高校统计
        .state('main.console.statistics.college', {
          url: '/college?collegeName&provinceName',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/statistics/college.html',
              controller: 'CollegeStatCtrl',
              controllerAs: 'colCtrl'
            }
          }
        })
				// 高校专利统计
        .state('main.console.statistics.college.patent', {
          url: '/patent?startValue&endValue',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/statistics/college/collegePmesValuePatent.html',
              controller: 'CollegePmesValuePatentCtrl',
              controllerAs: 'cpvpCtrl'
            }
          }
        })
				// 专家统计
        .state('main.console.statistics.pin', {
          url: '/pin?provinceName&collegeName&pinName',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/statistics/pin.html',
              controller: 'PinStatCtrl',
              controllerAs: 'pinCtrl'
            }
          }
        })
				// 专家专利统计
        .state('main.console.statistics.pin.patent', {
          url: '/patent?startValue&endValue&pin',
          views: {
            'content@main': {
              templateUrl: CONTEXT_PATH + '/static/tpls/statistics/pin/pinPmesValuePatent.html',
              controller: 'PinPmesValuePatentCtrl',
              controllerAs: 'cpvpCtrl'
            }
          }
        })
      ;
      // 默认页面
      $urlRouterProvider.otherwise(function ($injector, $location) {
        var $state = $injector.get("$state");
        $state.go("main.console.dashboard.personal");
      });
    }])
    .run(['$rootScope', '$state', '$log', 'ToastService', function ($rootScope, $state, $log, ToastService) {
      // $rootScope.$on('$viewContentLoading', function (event) {
      //   $log.info('$viewContentLoading', event);
      // });
      $rootScope.$on('$stateNotFound', function (event, unfoundState, fromState, fromParams) {
        $log.warn('$stateNotFound', unfoundState.to); // "lazy.state"
        ToastService.toast({
          type: 'error',
          body: '您所访问的页面不存在！'
        });
      });
      $rootScope.$on('$stateChangeError', function(event, toState, toParams, fromState, fromParams, error){
        $log.warn('$stateChangeError', unfoundState.to); // "lazy.state"
        ToastService.toast({
          type: 'error',
          body: '出错了:' + error.message
        });
      });
    }])
  ;

})(angular, CONTEXT_PATH);