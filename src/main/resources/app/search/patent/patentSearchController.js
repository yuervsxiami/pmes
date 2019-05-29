/**
 * Created by xiongwei 2018/2/4 上午10:27.
 */


(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('Search')
    .controller('PatentSearchController', ['$scope', '$state', '$stateParams', 'ToastService', 'PatentSearchService', 'modifyFlag', PatentSearchController])
    .controller('PatentDetailController', ['$scope', '$state', '$stateParams', '$log', '$timeout', 'ToastService', 'PatentSearchService', 'LabelsetService',
      'PATENT_PROCESS_TYPE', PatentDetailController])
  ;

  function PatentSearchController($scope, $state, $stateParams, ToastService, PatentSearchService, modifyFlag) {
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });

    var self = this;
    self.loading = false;
    self.modifyFlag = modifyFlag;
    //配置分页基本参数
    self.paginationConf = {
      currentPage: 1,
      itemsPerPage: 10
    };
    // 查询条件
    self.searchCondition = {
    };

    self.reset = function(){
      self.searchCondition = {};
    };

    self.onDatetimeRangeChanged = function (property, start, end) {
      if (property === 'ad') {
        self.searchCondition.fromAd = start ? start.toDate().getTime() : start;
        self.searchCondition.toAd = end ? end.toDate().getTime() : end;
      } else if (property === 'od') {
        self.searchCondition.fromOd = start ? start.toDate().getTime() : start;
        self.searchCondition.toOd = end ? end.toDate().getTime() : end;

        self.searchCondition.fromPd = start ? start.toDate().getTime() : start;
        self.searchCondition.toPd = end ? end.toDate().getTime() : end;
      }
    };

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

    // 搜索
    self.search = function () {
      self.loading = true;
      self.searchCondition.pageSize = self.paginationConf.itemsPerPage;
      self.searchCondition.pageNum = self.paginationConf.currentPage;
      self.searchCondition.actInstanceId = $scope.nullIfSpaceOrEmpty(self.searchCondition.actInstanceId);
      self.searchCondition.actTaskId = $scope.nullIfSpaceOrEmpty(self.searchCondition.actTaskId);
      self.searchCondition.an = $scope.nullIfSpaceOrEmpty(self.searchCondition.an);
      self.searchCondition.ti = $scope.nullIfSpaceOrEmpty(self.searchCondition.ti);
      self.searchCondition.pa = $scope.nullIfSpaceOrEmpty(self.searchCondition.pa);
      self.searchCondition.pin = $scope.nullIfSpaceOrEmpty(self.searchCondition.pin);
      self.searchCondition.lastLegalStatus = $scope.nullIfSpaceOrEmpty(self.searchCondition.lastLegalStatus);
      PatentSearchService.searchCompletePatents(self.searchCondition).then(function (resp) {
        self.loading = false;
        if (resp.code === 0) {
          self.paginationConf.totalItems = resp.result.total;
          self.patents = resp.result.list;
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
      });
    };

    $scope.$watch(function () {
      return self.paginationConf.itemsPerPage + " " + self.paginationConf.currentPage;
    }, self.search);

    //
    self.search();

  }

	/**
   * 专利查询 - 专利详情
	 * @param $scope
	 * @param $state
	 * @param $stateParams
	 * @param $log
	 * @param $timeout
	 * @param ToastService
	 * @param PatentSearchService
	 * @param LabelsetService
	 * @param PATENT_PROCESS_TYPE
	 * @constructor
	 */
  function PatentDetailController($scope, $state, $stateParams, $log, $timeout, ToastService, PatentSearchService, LabelsetService, PATENT_PROCESS_TYPE) {
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });

    var self = this;
    self.loading = false;

    self.active = 0;
    self.tabs = PATENT_PROCESS_TYPE.filter(function (process) {
      return process.show;
    });
    self.onTabSelected = function(index) {
      var tab = self.tabs[index];
      if (!tab.labels) {
        self.findPatentLabels($stateParams.id, tab);
      } else if (tab === 3) {
        self.refreshTree();
      }
    };

    self.changeText = function(text){
      if (text==null || text.trim()==""){
        return text;
      }
      return text.replace(/;/g,"; ");
    };

    // 查询专利详情
    self.findPatentDetail = function (patentId) {
      PatentSearchService.findPatentDetail(patentId).then(function (resp) {
        self.loading = false;
        if (resp.code === 0) {
          self.patent = resp.result;
        }
        else {
          ToastService.toast({
            type: 'error',
            body: '查询专利详情出错：' + resp.message
          });
        }
      }).catch(function (reason) {
        self.loading = false;
        ToastService.errHandler(reason);
      });
    };

    // 查询流程标签
    self.findPatentLabels = function (patentId, tab) {
      tab.loading = true;
      PatentSearchService.findPatentInstanceLabelsByProcessType(patentId, tab.id).then(function (resp) {
        tab.loading = false;
        if (resp.code === 0) {
          tab.labels = resp.result;
          if (tab.id === 3) {
            // 如果是价值标引，查询价值体系，生成体系树
            tab.values = {};
            tab.labels.forEach(function (taskLabel) {
              var key = taskLabel.label.key;
              // if (self.valueKeys.indexOf(key) >= 0) {
              tab.values[key] = taskLabel.strValue || taskLabel.textValue;
              // }
            });
            self.getValueLabelsetLabels(tab);
          }
        }
        else {
          ToastService.toast({
            type: 'error',
            body: '查询专利详情出错：' + resp.message
          });
        }
      }).catch(function (reason) {
        tab.loading = false;
        ToastService.errHandler(reason);
      });
    };

    // 价值体系
    self.onChartCtrlAware = function (ctrl) {
      self.treeCtrl = ctrl;
    };
    // 读取价值标签体系的标签
    self.getValueLabelsetLabels = function (tab) {
      self.treeCtrl && self.treeCtrl.showLoading();
      LabelsetService.getLabelsetLabels(3).then(function (resp) {
        self.treeCtrl && self.treeCtrl.hideLoading();
        if (resp.code === 0) {
          tab.labelsetLabels = resp.result;
          // 构建价值体系树
          self.buildValueTree(tab);
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
    self.buildValueTree = function (tab) {
      var tree = tab.labelsetLabels.filter(function (label) {
        return label.parentId === 0;
      })[0];
      angular.merge(tab.tree, tree);
      self.setValueForTree(tab.values, tab.tree);
      self.refreshTree();
    };
    // 构建价值体系树
    self.setValueForTree = function(values, node) {
      if (node) {
        if (node.label) {
          var key = node.label.key;
          node.value = values[key];
          node.name = node.name + ':' + (node.value || '无');
        }
        if (node.children && node.children.length > 0) {
          node.children.forEach(function (node) {
            self.setValueForTree(values, node);
          });
        }
      }
    };
    // 刷新树
    self.refreshTree = function () {
      if (self.treeCtrl) {
        $timeout(function () {
          self.treeCtrl.setOption();
        }, 100);
      }
    };

    // init
    self.tabs.forEach(function (tab) {
      if (tab.id === 3) {
        tab.values = {};
        tab.tree = {
          id: 0,
          name: '专利价值评估'
        };
        // if (!tab.labels) {
        //   self.findPatentLabels($stateParams.id, tab);
        // }
      }
			self.findPatentLabels($stateParams.id, tab);
    });
    self.findPatentDetail($stateParams.id);

  }

})(angular, CONTEXT_PATH);
