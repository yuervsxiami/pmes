/**
 * Created by wangzhibin on 2018/1/23.
 */

(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('Patent')
    .controller('PatentProcessStartController', ['$scope', '$filter', '$location', 'ModalService','ToastService', 'PatentService', 'PatentStartService', 'PATENT_PROCESS_TYPE', PatentProcessStartController])
  ;

  function PatentProcessStartController($scope, $filter, $location, ModalService, ToastService, PatentService, PatentStartService, PATENT_PROCESS_TYPE) {
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });
    var self = this;
    self.mode = 1;
    self.init = function () {
      self.backUrl = $location.absUrl();
      self.processTypes = PATENT_PROCESS_TYPE.filter(function(type){return type.show});
      self.allSelected = false;
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
    self.reset = function(){
      self.ans = null;
      self.searchCondition = {
      };
    }

    self.onDatetimeRangeChanged = function (property, start, end) {
      if (property=="ad"){
        self.searchCondition.fromAd = start ? start.toDate().getTime() : start;
        self.searchCondition.toAd = end ? end.toDate().getTime() : end;
      } else if (property=="od"){
        self.searchCondition.fromOd = start ? start.toDate().getTime() : start;
        self.searchCondition.fromPd = start ? start.toDate().getTime() : start;
        self.searchCondition.toOd = end ? end.toDate().getTime() : end;
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
    }

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
      self.searchCondition.pin = $scope.nullIfSpaceOrEmpty(self.searchCondition.pin);
      self.searchCondition.username = $scope.nullIfSpaceOrEmpty(self.searchCondition.username);
      PatentStartService.search(self.mode, self.searchCondition).then(function (resp) {
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

    // 启动流程
    self.startProcess = function (processType, patent) {
      var patents = [];
      // 生成需启动专利列表
      if (patent == null) {
        self.patents.forEach(function (p) {
          if (p.selected) {
            patents.push(p.data);
          }
        })
      }
      else {
        patents.push(patent);
      }

      if (patents.length == 0) {
        ToastService.toast({
          type: 'warn',
          body: '请至少选择一个专利！'
        });
        return;
      }
      // 启动价值评估流程时，判断专利是否已进行价值评估
      var hasValueIndex = processType == 3 &&
        patents.filter(function (p) {
          return p.valueIndexOrder != null;
        }).length > 0;
      if (hasValueIndex) {
        var message = '专利已进行评估，是否启动？';
        if (patents.length > 1) {
          message = '部分专利已进行评估，是否启动？';
        }
        ModalService.openAlert({
          title: '提示',
          message: message
        }).then(function (code) {
          if (code === 0) {
            self.request(processType, patents);
          }
        }).catch(function (err) {
          $log.info(err);
        });
      }
      else {
        self.request(processType, patents);
      }
    }
    self.request=function(processType, patents) {
      self.loading = true;
      PatentStartService.startProcess(processType, patents).then(function (resp) {
        if (resp && resp.code === 0) {
          // self.search();
          ToastService.toast({
            type: 'success',
            body: resp.result
          });
          switch (processType) {
            case 2:
              patents.forEach(function (p) {
                if(!p.index) {
                  p.index = {};
                }
                p.index.hasBaseIndexing = 1;
              });
              break;
            case 3:
              patents.forEach(function (p) {
                  if(!p.index) {
                      p.index = {};
                  }
                  p.index.hasValueIndexing = 1;
              });
              break;
            case 4:
              patents.forEach(function (p) {
                  if(!p.index) {
                      p.index = {};
                  }
                  p.index.hasPriceIndexing = 1;
              });
              break;
            case 5:
              patents.forEach(function (p) {
                  if(!p.index) {
                      p.index = {};
                  }
                  p.index.hasDeepIndexing = 1;
              });
              break;
          }
        }
        else{
          ToastService.toast({
            type: 'info',
            body: resp.message
          });
        }
      }).catch(function (err) {
        ToastService.toast({
          type: 'error',
          body: err.message
        });
      }).finally(function () {
        self.loading = false;
      });
    }

    self.init();
  }

})(angular, CONTEXT_PATH);