/**
 * Created by wangzhibin on 2018/1/29.
 */
(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('Patent')
    .controller('HumenController', ['$scope', '$filter', 'ToastService', 'PatentService', 'HumenService','$timeout', HumenController])
    .controller('AssessmentController', ['$scope', '$stateParams', 'ToastService', 'HumenService', 'LabelsetService', 'EXTRA_VALUE_LABELS', AssessmentController])
  ;
  function HumenController($scope, $filter, ToastService, PatentService, HumenService) {
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });
    var self = this;
    self.mode = 1;
    self.init = function () {
      self.loading = false;
      //配置分页基本参数
      self.paginationConf = {
        currentPage: 1,
        itemsPerPage: 10
      };
      // 查询条件
      self.reset();
      $scope.$watch(function () {
        return self.paginationConf.itemsPerPage + " " + self.paginationConf.currentPage;
      }, self.search);
    };
    // 重置
    self.reset = function () {
      self.ans = null;
      self.searchCondition = {
        searchType:'0'
      };
    };
    /*
    * 新增导出数据文件功能
    */
    //导出弹框
    self.model = function () {
      switch (self.mode){
        case 1:
          var condition = self.searchCondition.an||self.searchCondition.ti||self.searchCondition.pin||self.searchCondition.pa||(self.searchCondition.fromAd&&self.searchCondition.toAd);
          if(condition){
            $('#myModal').modal('show');
          }else{
            $('#myModal').modal('hide');
            ToastService.toast({
              type: 'error',
              body: '请输入查询条件'
            });
          }
          break;
        case 2:
          var ans = self.ans;
          if(ans){
            $('#myModal').modal('show');
          }else {
            $('#myModal').modal('hide');
            ToastService.toast({
              type: 'error',
              body: '请输入查询条件'
            });
          }
          break;
        default:
          break;
      }
      $scope.allSelected = true;
      $scope.selectAll($scope.allSelected);
      $scope.allSelectedOne = true;
      $scope.selectAllStatus($scope.allSelectedOne);
    };
    //导出文档
    var count = 0;
    $scope.exportExcel = function () {
      if(count==0){
        count++;
        $scope.hasChecked = [];
        $scope.hasCheckedOne = [];
        for(var i=0;i<$scope.checked.length;i++){
          // var index = $scope.checked[i];
          $scope.hasChecked.push($scope.dataLists[i]);
        }
        for(var j=0;j<$scope.checkedOne.length;j++){
          var indexAssess = $scope.checkedOne[j];
          // console.log($scope.dataAssess[j].id);
          $scope.hasCheckedOne.push($scope.dataAssess[j]);
        }
        self.export();
      }else{
        ToastService.toast({
          type: 'warn',
          body: "正在导出，请稍等"
        });
        return;
      }
    };
    //条件参数
    self.export = function () {
      self.exportCondition = self.searchCondition;
      self.exportCondition.mode = self.mode;
      self.exportCondition.dataLists = $scope.hasChecked.concat($scope.hasCheckedOne);
      self.exportCondition.ans = $scope.nullIfSpaceOrEmpty(self.ans)==null ? null : $scope.nullIfSpaceOrEmpty(self.ans).replace(/\n/g,',').split(',');
      HumenService.exportExcel(self.exportCondition).then(function (res) {
        count=0;
        if(res.result){
          window.location.href=res.result;
          $('#myModal').modal('hide');
        }else{
          ToastService.toast({
            type: 'error',
            body: "返回结果错误"
          });
        }
      }).catch(function () {
        ToastService.errHandler = "请求超时"
      });
    };
    $scope.dataLists = [
      {
        'id':1001,
        'label':'AN',
        'name':'an'
      },
      {
        'id':1002,
        'label':'标题',
        'name':'ti'
      },
      {
        'id':1003,
        'label':'专利类型',
        'name':'sectionName'
      },
      {
        'id':1004,
        'label':'申请日',
        'name':'ad'
      },
      {
        'id':1005,
        'label':'公开日',
        'name':'pd'
      },
      {
        'id':1006,
        'label':'公开号',
        'name':'pnm'
      },
      {
        'id':1007,
        'label':'分类号',
        'name':'sic'
      },
      {
        'id':1008,
        'label':'主分类号',
        'name':'pic'
      },
      {
        'id':1009,
        'label':'摘要',
        'name':'ab'
      },
      {
        'id':1010,
        'label':'法律状态',
        'name':'lastLegalStatus'
      },
      {
        'id':1011,
        'label':'申请人',
        'name':'pa'
      },
      {
        'id':1012,
        'label':'发明人',
        'name':'pin'
      },
      {
        'id':1013,
        'label':'关键词',
        'name':'trskeyword'
      }
    ];
    $scope.dataAssess = [
      {
        'id':0,
        'label':'专利价值',
        'name':'patentValue'
      },
      {
        'id':1,
        'label':'法律价值',
        'name':'legalValue'
      },
      {
        'id':2,
        'label':'经济价值',
        'name':'economicValue'
      },
      {
        'id':3,
        'label':'技术价值',
        'name':'technologicalValue'
      },

      {
        'id':4,
        'label':'市场竞争能力',
        'name':'marketCompetitiveness'
      },
      {
        'id':5,
        'label':'专利经济寿命',
        'name':'patentEconomicLife'
      },
      {
        'id':6,
        'label':'专利布局',
        'name':'patentPortfolio'
      },
      {
        'id':7,
        'label':'技术先进性',
        'name':'technologyAdvancement'
      },
      {
        'id':8,
        'label':'技术应用范围',
        'name':'technicalApplicationRange'
      },
      {
        'id':9,
        'label':'技术可替代性',
        'name':'technologyFungibility'
      },
      {
        'id':10,
        'label':'专利稳定性',
        'name':'patentStability'
      },
      {
        'id':11,
        'label':'依赖性',
        'name':'dependency'
      },
      {
        'id':12,
        'label':'专利宽度',
        'name':'pantentBreadth'
      },
      {
        'id':13,
        'label':'专利时限',
        'name':'timeLimitOfPatent'
      },
      {
        'id':14,
        'label':'专利维持状态',
        'name':'patentMaintenance'
      },
      {
        'id':15,
        'label':'专利族规模',
        'name':'patentFamilySize'
      },
      {
        'id':16,
        'label':'专利族地域分布',
        'name':'patenteGeographuicalDistribution'
      },
      {
        'id':17,
        'label':'团队影响力',
        'name':'groupInfluence'
      },
      {
        'id':18,
        'label':'技术交叉性',
        'name':'technicalCrossover'
      },
      {
        'id':19,
        'label':'科学关联强度',
        'name':'scientificCorrelationStrength'
      },
      {
        'id':20,
        'label':'技术有用性',
        'name':'technicalUsefulness'
      },
      {
        'id':21,
        'label':'技术覆盖度',
        'name':'technicalCoverage'
      },

      {
        'id':22,
        'label':'技术专业度',
        'name':'technicalDegree'
      },
      {
        'id':23,
        'label':'专利新颖度',
        'name':'patentNovelty'
      },
      {
        'id':24,
        'label':'法律地位稳固度',
        'name':'stabilityOfLegalStatus'
      },
      {
        'id':25,
        'label':'专利依赖度',
        'name':'patentDependence'
      },
      {
        'id':26,
        'label':'非专利依赖度',
        'name':'nonPatentDependence'
      },
      {
        'id':27,
        'label':'权利保护范围',
        'name':'scopeOfRightProtection'
      },
      {
        'id':28,
        'label':'地域保护范围',
        'name':'gengraphicalCoverage'
      },
      {
        'id':29,
        'label':'相似专利数量',
        'name':'similarNumberOfPatents'
      },
      {
        'id':30,
        'label':'已保护年限',
        'name':'durationOfPatentCV'
      },
      {
        'id':31,
        'label':'费用处理状态',
        'name':'costHandingStatus'
      },
      {
        'id':32,
        'label':'同族专利项',
        'name':'siblingPatent'
      },
      {
        'id':33,
        'label':'同族专利数',
        'name':'siblingPatentNum'
      },
      {
        'id':34,
        'label':'发明人数量',
        'name':'inventorNum'
      },
      {
        'id':35,
        'label':'分类号数量',
        'name':'classiflcationCodeNum'
      },
      {
        'id':36,
        'label':'引证专利数',
        'name':'citedPatentNum'
      },
      {
        'id':37,
        'label':'引证非专利数',
        'name':'citedNonPatentNum'
      },
      {
        'id':38,
        'label':'独权数',
        'name':'exclusiveRightNum'
      },
      {
        'id':39,
        'label':'从权数',
        'name':'followRightNum'
      },
      {
        'id':40,
        'label':'相似度均值',
        'name':'meanOfSimilarity'
      }
    ];
    /*基础数据、全选、单选、多选*/
    $scope.checked = [];
    $scope.selectAll = function (allSelected) {
      if (allSelected) {
        $scope.checked = [];
        angular.forEach($scope.dataLists, function (dataBasic) {
          dataBasic.checked = true;
          $scope.checked.push(dataBasic.id);
        });
      } else {
        angular.forEach($scope.dataLists, function (dataBasic) {
          dataBasic.checked = false;
          $scope.checked = [];
        });
      }
    };
    $scope.selectOne = function (dataBasicId) {
      // console.log(dataBasicId);
      var index = $scope.checked.indexOf(dataBasicId);
      if (index >= 0) {
        $scope.checked.splice(index, 1);
      } else {
        $scope.checked.push(dataBasicId);
      }
      if ($scope.dataLists.length === $scope.checked.length) {
        $scope.allSelected = true;
      } else {
        $scope.allSelected = false;
      }
    };
    /*价值评估数据、全选、单选、多选*/
    $scope.checkedOne = [];
    $scope.selectAllStatus = function (allSelectedOne) {
      if (allSelectedOne) {
        $scope.checkedOne = [];
        angular.forEach($scope.dataAssess, function (dataAss) {
          dataAss.checkedOne = true;
          $scope.checkedOne.push(dataAss.id);
        });
      } else {
        angular.forEach($scope.dataAssess, function (dataAss) {
          dataAss.checkedOne = false;
          $scope.checkedOne = [];
        });
      }
    };
    $scope.selectOneStatus = function (dataAssessId) {
      var index = $scope.checkedOne.indexOf(dataAssessId);
      if (index >= 0) {
        $scope.checkedOne.splice(index, 1);
      } else {
        $scope.checkedOne.push(dataAssessId);
      }
      if ($scope.dataAssess.length === $scope.checkedOne.length) {
        $scope.allSelectedOne = true;
      } else {
        $scope.allSelectedOne = false;
      }
    };
    //
    self.onPatentTypeSelected = function (selected) {
      self.searchCondition.types = [];
      if (selected == null) {
        return;
      }
      selected.forEach(function (m) {
        self.searchCondition.types.push(m.value == null ? m.name : m.value);
      })
    };
    self.onLegalStatusSelected = function (selected) {
      self.searchCondition.lastLegalStatus = [];
      if (selected == null) {
        return;
      }
      selected.forEach(function (m) {
        self.searchCondition.lastLegalStatus.push(m.value == null ? m.name : m.value);
      })
    };

    // allselected checkbox change
    self.changeAllSelected = function () {
      self.patents.forEach(function (item) {
        item.selected = self.allSelected;
      });
    }

    self.changeSelected = function () {
      // 选中个数
      var num = self.patents.filter(function (item) {
        return item.selected
      }).length;
      self.allSelected = num == self.patents.length;
    };

    // 搜索
    self.search = function () {
      self.loading = true;
      self.searchCondition.pageSize = self.paginationConf.itemsPerPage;
      self.searchCondition.pageNum = self.paginationConf.currentPage;
      self.searchCondition.ti = $scope.nullIfSpaceOrEmpty(self.searchCondition.ti);
      self.searchCondition.an = $scope.nullIfSpaceOrEmpty(self.searchCondition.an);
      self.searchCondition.ans = $scope.nullIfSpaceOrEmpty(self.ans)==null ? null : $scope.nullIfSpaceOrEmpty(self.ans).replace(/\n/g,',').split(',');
      self.searchCondition.onm = $scope.nullIfSpaceOrEmpty(self.searchCondition.onm);
      self.searchCondition.pnm = $scope.nullIfSpaceOrEmpty(self.searchCondition.pnm);
      self.searchCondition.pa = $scope.nullIfSpaceOrEmpty(self.searchCondition.pa);
      self.searchCondition.searchType = $scope.nullIfSpaceOrEmpty(self.searchCondition.searchType);
      self.searchCondition.pin = $scope.nullIfSpaceOrEmpty(self.searchCondition.pin);
      self.searchCondition.username = $scope.nullIfSpaceOrEmpty(self.searchCondition.username);
      HumenService.search(self.mode, self.searchCondition).then(function (resp) {
        self.loading = false;
        if (resp.code === 0) {

          self.paginationConf.totalItems = resp.result.total;
          self.patents = [];
          resp.result.list.forEach(function (data) {
            self.patents.push({selected: false, data: data});
          });
          self.searching = false;
        }
        else {
          ToastService.toast({
            type: 'error',
            body: resp.message
          });
        }
      }).catch(function (reason) {
        self.loading = false;
        ToastService.errHandler(reason);
      })
    };

    self.start = function (patent) {
      patent.disabled = true;
      HumenService.start(patent.id).then(function (resp) {
        patent.disabled = false;
        if (resp.code === 0) {
          ToastService.toast({
              type: 'success',
              body: resp.result
          });
          patent.hasBatchIndexed = 1;
          patent.updateTime = new Date();
        }
        else {
          ToastService.toast({
              type: 'error',
              body: '重新评估失败'
          });
        }
      }).catch(function (reason) {
          patent.disabled = false;
          ToastService.errHandler(reason);
      })
    };

    self.init();
  }

  /**
   * 专利价值快速评估详情
   * @param $scope
   * @param $stateParams
   * @param ToastService
   * @param HumenService
   * @constructor
   */
  function AssessmentController($scope, $stateParams, ToastService, HumenService, LabelsetService, EXTRA_VALUE_LABELS) {
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });

    var self = this;
    self.loading = false;
    self.tree = {
      id: 0,
      name: '专利价值快速评估'
    };
    self.labelGroups = []; // 专利价值标签的分组，每三个一组，显示成表格
    self.valueKeys = ['patentValue', 'technologicalValue', 'economicValue', 'legalValue'];
    // 专利处理详情
    self.getDetail = function (patentId) {
      self.loading = true;
      HumenService.detail(patentId).then(function (resp) {
        self.loading = false;
        if (resp.code === 0) {
          self.patent = resp.result;
          // console.log( self.patent,"==========")
          if (self.patent.assessmentOrder) {
            self.patent.assessmentOrder.taskOrders.forEach(function (order) {
              order.labels.forEach(function (taskLabel) {
                var key = taskLabel.label.key;
                // if (self.valueKeys.indexOf(key) >= 0) {
                self.patent[key] = taskLabel.strValue || taskLabel.textValue;
                // }
              });
            });
            // 读取标签体系
            self.getValueLabelsetLabels();
          } else {
            ToastService.toast({
              type: 'error',
              body: '此专利尚未进行价值评估！'
            });
          }
        } else {
          ToastService.toast({
            type: 'error',
            body: resp.message
          });
        }
      }).catch(function (reason) {
        self.loading = false;
        ToastService.errHandler(reason);
      });
    };

    // 读取价值标签体系的标签
    self.getValueLabelsetLabels = function () {
      self.treeCtrl && self.treeCtrl.showLoading();
      LabelsetService.getLabelsetLabels(1).then(function (resp) {
        self.treeCtrl && self.treeCtrl.hideLoading();
        if (resp.code === 0) {
          self.labelsetLabels = resp.result;
          self.labelGroups.splice(0, self.labelGroups.length);
          var cols = 2;
          var valueLabels = self.labelsetLabels.filter(function (label) {
            // return label.parentId >= 0;
            return true;//暂时将所有标签返回
          });
          angular.forEach(EXTRA_VALUE_LABELS, function (l) {
            valueLabels.push({
              label: l
            });
          });
          var rows = valueLabels.length % 3 === 0 ? valueLabels.length / 3 : (Math.floor(valueLabels.length / 3) + 1);
          for (var i = 0; i < rows; i++) {
            self.labelGroups.push(valueLabels.slice(i * cols, i * cols + cols));
          }
          // 构建价值体系树
          self.buildValueTree();
        } else {
          ToastService.toast({
            type: 'error',
            body: resp.message
          });
        }
      }).catch(function (reason) {
        self.treeCtrl && self.treeCtrl.hideLoading();
        ToastService.errHandler(reason);
      });
    };

    // 构建价值体系树
    function setValueForTree(node) {
      if (node) {
        if (node.label) {
          var key = node.label.key;
          node.value = self.patent[key];
          node.name = node.name + ':' + node.value;
        }
        if (node.children && node.children.length > 0) {
          node.children.forEach(setValueForTree);
        }
      }
    }
    self.buildValueTree = function () {
      var tree = self.labelsetLabels.filter(function (label) {
        return label.parentId === 0;
      })[0];
      angular.merge(self.tree, tree);
      setValueForTree(self.tree);
      self.refreshTree();
    };
    // 刷新树
    self.refreshTree = function () {
      if (self.treeCtrl) {
        self.treeCtrl.setOption();
      }
    };
    self.onChartCtrlAware = function (ctrl) {
      self.treeCtrl = ctrl;
    };

    // 获取专利详情
    self.getDetail($stateParams.id);
  }

})(angular, CONTEXT_PATH);
