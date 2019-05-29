/**
 * Created by wangzhibin on 2018/1/15.
 */
(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('Configuration')
    .controller('ProcessController', ['$scope', 'ToastService', 'ModalService', 'ProcessService', 'ProcessAction', ProcessController])
    .controller('ProcessEditerController', ['$uibModalInstance', 'ToastService', 'FormService', 'ProcessService', 'editConfig', ProcessEditerController])
    .controller('SetLabelsetController', ['$uibModalInstance', 'ToastService', 'FormService', 'LabelsetService', 'ProcessService','editConfig', SetLabelsetController])
    .controller('ChangeTimeController', ['$uibModalInstance', 'ToastService', 'FormService', 'ProcessService','editConfig', ChangeTimeController])
  ;

  function ProcessEditerController($uibModalInstance, ToastService, FormService, ProcessService, editConfig) {
    var self = this;
    angular.merge(self, FormService);

    self.init = function() {
      self.loading = false;
      self.process = {name: ""};
      self.title = "添加流程模版";
      if (editConfig.action == "update") {
        self.process = editConfig.process;
        self.title = "修改流程模版";
      }
    }

    self.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };

    self.ok = function () {
      self.loading = true;
      ProcessService.edit(self.process).then(function (resp) {
        self.loading = false;
        if (resp && resp.code === 0) {
          $uibModalInstance.close(resp.result);
        } else {
          ToastService.toast({
            type: 'error',
            body: resp.message
          });
        }
      }).catch(function (err) {
        self.loading = false;
        ToastService.toast({
          type: 'error',
          body: err.message
        });
      });
    };

    self.init();
  }

  function SetLabelsetController($uibModalInstance, ToastService, FormService, LabelsetService, ProcessService, editConfig) {
    var self = this;
    angular.merge(self, FormService);

    self.init = function() {
      self.loading = false;
      self.labelsets = [];
      self.process = editConfig.process;
      console.log(self.process)
      self.getAllLabelsets();
    }

    self.getAllLabelsets = function() {
      var searchCondition = {
        pageSize: 9999,
        pageNum: 1
      };
			LabelsetService.findByType(self.process.type).then(function (resp) {
        if (resp && resp.code === 0) {
          self.labelsets = self.labelsets.concat(resp.result);
        } else {
          ToastService.toast({
            type: "error",
            body: '获取标签体系失败'
          });
        }
      }).catch(ToastService.errHandler);
    }

    self.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };

    self.ok = function () {
      self.loading = true;
      ProcessService.setLabelset(self.process).then(function (resp) {
        self.loading = false;
        if (resp && resp.code === 0) {
          $uibModalInstance.close(resp.result);
        } else {
          ToastService.toast({
            type: 'error',
            body: resp.message
          });
        }
      }).catch(function (err) {
        self.loading = false;
        ToastService.toast({
          type: 'error',
          body: err.message
        });
      });
    };

    self.init();
  }

  function ChangeTimeController($uibModalInstance, ToastService, FormService, ProcessService, editConfig) {
    var self = this;
    angular.merge(self, FormService);

    self.init = function() {
      self.loading = false;
      self.process = editConfig.process;
    }

    self.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };

    self.ok = function () {
      self.loading = true;
      ProcessService.changeTime(self.process).then(function (resp) {
        self.loading = false;
        if (resp && resp.code === 0) {
          $uibModalInstance.close(resp.result);
        } else {
          ToastService.toast({
            type: 'error',
            body: resp.message
          });
        }
      }).catch(function (err) {
        self.loading = false;
        ToastService.toast({
          type: 'error',
          body: err.message
        });
      });
    };

    self.init();
  }

  function ProcessController($scope, ToastService ,ModalService  ,ProcessService, ProcessAction) {

    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });

    var self = this;

    self.init=function() {
      self.loading = false;
      self.contextPath = CONTEXT_PATH;
      self.maxDate = Date.now();
      self.searchCondition = {
      };

      //配置分页基本参数
      self.paginationConf = {
        currentPage: 1,
        itemsPerPage: 10
      };

      $scope.$watch(function () {
        return self.paginationConf.itemsPerPage + " " + self.paginationConf.currentPage;
      }, self.search);

    };

    self.reset=function () {
      self.searchCondition = {};
    }

    self.onDatetimeRangeChanged = function (start, end) {
      self.searchCondition.fromTime = start ?start.toDate().getTime() : start;
      self.searchCondition.toTime = end ? end.toDate().getTime() : end;
    };

    // 搜索
    self.search = function () {
      self.loading = true;
      self.searchCondition.pageSize = self.paginationConf.itemsPerPage;
      self.searchCondition.pageNum = self.paginationConf.currentPage;
      ProcessService.search(self.searchCondition).then(function (resp) {
        self.loading = false;
        if (resp.code === 0) {
          self.paginationConf.totalItems = resp.result.total;
          self.processes = resp.result.list;
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

    // 添加流程模版
    self.add = function () {
      ProcessAction.processEditer({action: "add"}).then(function (process) {
        ToastService.toast({
          type: 'success',
          body: '添加流程模版成功！'
        });
        self.search();
      }).catch(function (reason) {
        ToastService.errHandler(reason);
      });
    };

    // 修改流程模版
    self.update = function (data) {
      ProcessAction.processEditer({action: "update", process: data}).then(function (process) {
        ToastService.toast({
          type: 'success',
          body: '修改流程模版成功！'
        });
        self.search();
      }).catch(function (reason) {
        ToastService.errHandler(reason);
      });
    };

    // 设置标签体系
    self.setLabelset = function (data) {
      ProcessAction.setLabelset({process: data, labelsets: self.labelsets}).then(function (resp) {
        ToastService.toast({
          type: 'success',
          body: '标签体系设置成功！'
        });
        self.search();
      }).catch(function (reason) {
        ToastService.errHandler(reason);
      });
    }

    // 删除标签体系
    self.removeLabelset = function (id) {
      ModalService.openAlert({
        title: '提示',
        message: '您确定要删除该流程模版的标签体系吗?'
      }).then(function (code) {
        if (code === 0) {
          ProcessService.removeLabelset(id).then(function (resp) {
            if (resp && resp.code === 0) {
              self.search();
              ToastService.toast({
                type: 'success',
                body: resp.result// "已成功删除标签体系！"
              });
            }
          }).catch(function (err) {
            ToastService.toast({
              type: 'error',
              body: err.message
            });
          });
        }

      }).catch(function (err) {
        $log.info(err);
      });
    };

    // 启用、禁用
    self.changeState = function (process) {
      process.disabled = true;
      ProcessService.changeState(process.id).then(function (resp) {
				process.disabled = false;
        if (resp && resp.code === 0) {
          self.search();
          ToastService.toast({
            type: 'success',
            body: resp.result
          });
        } else {
					ToastService.toast({
						type: 'error',
						body: resp.message
					});

        }
      }).catch(function (err) {
				process.disabled = false;
        ToastService.toast({
          type: 'error',
          body: err.message
        });
      });
    };

    // 配置时间
    self.changeTime = function (data) {
      ProcessAction.changeTime({process: data}).then(function (resp) {
        ToastService.toast({
          type: 'success',
          body: '预警时间和超时时间设置成功！'
        });
        self.search();
      }).catch(function (reason) {
        ToastService.errHandler(reason);
      });
    }
    self.init();
  }

})(angular, CONTEXT_PATH);