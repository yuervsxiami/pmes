/**
 * Created by wangzhibin on 2018/1/15.
 */
(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('Configuration')
    .controller('ProcessTaskController', ['$scope', 'ToastService', 'ModalService', 'ProcessService', 'ProcessTaskService', 'ProcessTaskAction', ProcessTaskController])
    .controller('AddProcessTaskController', ['$uibModalInstance', 'ToastService', 'FormService', 'ProcessTaskService', AddProcessTaskController])
    .controller('ChangeTimeController', ['$uibModalInstance', 'ToastService', 'FormService', 'ProcessTaskService','editConfig', ChangeTimeController])
    .controller('AddTaskLabelController', ['$scope', '$uibModalInstance', '$filter', 'ToastService', 'ModalService', 'FormService', 'ProcessService', 'ProcessTaskService','editConfig', AddTaskLabelController])
    .controller('ChangeRoleController', ['$uibModalInstance', 'ToastService', 'FormService', 'RoleService', 'ProcessTaskService','editConfig', ChangeRoleController])
  ;


  function AddProcessTaskController($uibModalInstance, ToastService, FormService, ProcessTaskService) {
    var self = this;
    angular.merge(self, FormService);

    self.init = function() {
      self.loading = false;
      self.processTask = {name: ""};
    }

    self.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };

    self.ok = function () {
      self.loading = true;
      ProcessTaskService.add(self.processTask).then(function (resp) {
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

  function ChangeTimeController($uibModalInstance, ToastService, FormService, ProcessTaskService, editConfig) {
    var self = this;
    angular.merge(self, FormService);

    self.init = function() {
      self.loading = false;
      self.processTask = editConfig.processTask;
    }

    self.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };

    self.ok = function () {
      self.loading = true;
      ProcessTaskService.changeTime(self.processTask).then(function (resp) {
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

  function AddTaskLabelController($scope, $uibModalInstance, $filter, ToastService, ModalService, FormService, ProcessService, ProcessTaskService, editConfig) {
    var self = this;
    angular.merge(self, FormService);

    self.init = function() {
      self.loading = false;
      // 当前环节
      self.processTask = editConfig.processTask;

      // 体系
      self.sourceObject= {
        labels: [], // 标签列表
        keyword: "", // 过滤标签关键字
        allSelected: false // 是否全选
      };

      // 环节
      self.targetObject= {
        labels: [], // 标签列表
        keyword: "", // 过滤标签关键字
        allSelected: false // 是否全选
      };

      self.processes = [];
      // 获取全部流程模版, 设置 self.process, 初始化 label 列表
      self.getAllProcesses();
    }

    // 获取未包含标签
    self.getAllSourceLabels = function(l) {
      l.forEach(function (item) {
        if (self.targetObject.labels.length == 0) {
          self.sourceObject.labels.push({
            selected: false,
            id: item.label.id,
            name: item.label.name,
            source: item.label.source
          });
        }
        else {
          if (self.targetObject.labels.filter(function (label) {
              return item.label.id == label.id;
            }).length == 0) {
            self.sourceObject.labels.push({
              selected: false,
              id: item.label.id,
              name: item.label.name,
              source: item.label.source
            });
          }
        }
      });
    }

    // 获取已包含标签
    self.getAllTargetLabels = function(l) {
      l.forEach(function (item) {
        self.targetObject.labels.push({
          selected: false,
          id: item.label.id,
          name: item.label.name,
          source: item.label.source
        });
      });
    }

    // 流程模版变化
    self.changeProcess = function() {
      self.sourceObject.allSelected = false;
      self.targetObject.allSelected = false;

      // 选定模版=原来环节所属模版
      if (self.process == null && self.processTask.process == null) {
        self.sourceObject.labels = [];
        self.targetObject.labels = [];
        return;
      }
      else if (self.process == self.processTask.process) {
        self.getAllTargetLabels(self.processTask.labels);
        if (self.process != null && self.process.labelset != null) {
          self.getAllSourceLabels(self.process.labelset.labelsetLabels);
        }
        else {
          self.sourceObject.labels = [];
        }
        return;
      }

      // 刷新标签列表
      self.targetObject.labels = [];
      if (self.process != null && self.process.labelset != null) {
        self.getAllSourceLabels(self.process.labelset.labelsetLabels);
      }
      else {
        self.sourceObject.labels = [];
      }
    }

    // 获取全部流程模版,并设置当前process
    self.getAllProcesses = function() {
      var searchCondition = {
        pageSize: 9999,
        pageNum: 1
      };
      ProcessService.get(editConfig.processTask.processId).then(function (resp) {
        if (resp && resp.code === 0) {
          self.processes.push(resp.result);
					// 设置当前process
					self.process = resp.result;
          // 初始化 label 列表
					ProcessTaskService.getLabels(editConfig.processTask.id).then(function (res) {
						if (res && res.code === 0) {
							self.processTask.labels = res.result;
							if (self.processTask.labels != null) {
								self.getAllTargetLabels(self.processTask.labels);
							}
							if (self.process != null && self.process.labelsetId != null) {
								self.getAllSourceLabels(self.process.labelset.labelsetLabels);
							}
						}
						else {
							ToastService.toast({
								type: "error",
								body: '获取环节标签失败'
							});
						}
					}).catch(ToastService.errHandler);
        }
        else {
          ToastService.toast({
            type: "error",
            body: '获取流程模版失败'
          });
        }
      }).catch(ToastService.errHandler);
    }

    // 添加选定的标签
    self.addLabels = function() {
      var i = self.targetObject.labels.length;
      if (i == 0) {
        ToastService.toast({
          type: 'warn',
          body: '请至少选择一个标签！'
        });
        return;
      }
      while (i--) {
        var item = self.targetObject.labels[i];
        if (item.selected && item.name.indexOf(self.targetObject.keyword)>=0) {
          var label = angular.copy(item);
          label.selected = false;
          self.sourceObject.labels.unshift(label);
          self.targetObject.labels.splice(i, 1);
        }
      }
      self.clearSelectedState();
    }

    // 移除选定的标签
    self.removeLabels = function(){
      var i = self.sourceObject.labels.length;
      if (i == 0) {
        ToastService.toast({
          type: 'warn',
          body: '请至少选择一个标签！'
        });
        return;
      }
      while (i--) {
        var item = self.sourceObject.labels[i];
        if (item.selected && item.name.indexOf(self.sourceObject.keyword)>=0) {
          var label = angular.copy(item);
          label.selected = false;
          self.targetObject.labels.unshift(label);
          self.sourceObject.labels.splice(i, 1);
        }
      }
      self.clearSelectedState();
    }

    // 清除所有选中标记
    self.clearSelectedState = function () {
      self.sourceObject.allSelected = false;
      self.targetObject.allSelected = false;
      self.sourceObject.labels.forEach(function(item){
        item.selected = false;
      })
      self.targetObject.labels.forEach(function(item){
        item.selected = false;
      })
    }

    // allselected checkbox change
    self.changeAllSelected = function (o) {
      o.labels.filter(function (item) {
        return item.name.indexOf(o.keyword) < 0
      }).forEach(function (item) {
        item.selected = false;
      });

      o.labels.filter(function (item) {
        return item.name.indexOf(o.keyword) >= 0
      }).forEach(function (item) {
        item.selected = o.allSelected;
      });
    }

    self.changeSelected = function (o) {
      o.labels.filter(function (item) {
        return item.name.indexOf(o.keyword) < 0
      }).forEach(function (item) {
        item.selected = false;
      });
      // 复合筛选条件个数
      var num1 = o.labels.filter(function (item) {
        return item.name.indexOf(o.keyword) >= 0
      }).length;
      // 选中个数
      var num2 = o.labels.filter(function (item) {
        return item.name.indexOf(o.keyword) >= 0 && item.selected;
      }).length;
      o.allSelected = num1==num2;
    }

    self.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };

    self.ok = function () {
      // 删除标签
      if (self.process != null && self.targetObject.labels.length == 0) {
        ModalService.openAlert({
          title: '提示',
          message: '没有为环节配置任何标签，确定保存吗?'
        }).then(function (code) {
          if (code === 0) {
            self.updateLabels();
          }
        }).catch(function (err) {
          $log.info(err);
        });
      }
      // 删除流程模版
      else if (self.process == null) {
        ModalService.openAlert({
          title: '提示',
          message: '没有为环节配置流程模版，确定保存吗?'
        }).then(function (code) {
          if (code === 0) {
            self.updateLabels();
          }
        }).catch(function (err) {
          $log.info(err);
        });
      }
      else {
        self.updateLabels();
      }
    };

    // 更新标签
    self.updateLabels=function() {
      self.loading = true;
      self.processTask.processId = self.process==null ? null : self.process.id;
      self.processTask.labels = [];
      self.targetObject.labels.forEach(function (item) {
        self.processTask.labels.push({
          labelId: item.id
        });
      });

      ProcessTaskService.addTaskLabel(self.processTask).then(function (resp) {
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
    }

    self.init();

  }

  function ChangeRoleController($uibModalInstance, ToastService, FormService, RoleService, ProcessTaskService, editConfig) {
    var self = this;
    angular.merge(self, FormService);

    self.init = function() {
      self.loading = false;
      self.roles = [];
      self.processTask = editConfig.processTask;
      self.getAllRoles();
    }

    self.getAllRoles = function() {
      RoleService.getRoles().then(function (resp) {
        if (resp && resp.code === 0) {
          self.roles = self.roles.concat(resp.result);
        } else {
          ToastService.toast({
            type: "error",
            body: '获取角色失败'
          });
        }
      }).catch(ToastService.errHandler);
    }

    self.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };

    self.ok = function () {
      self.loading = true;
      ProcessTaskService.changeRole(self.processTask).then(function (resp) {
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

  function ProcessTaskController($scope, ToastService, ModalService, ProcessService, ProcessTaskService, ProcessTaskAction) {
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });

    var self = this;
    self.init = function() {
      self.loading = false;
      self.contextPath = CONTEXT_PATH;
      self.maxDate = Date.now();
      self.searchCondition = {};

      //配置分页基本参数
      self.paginationConf = {
        currentPage: 1,
        itemsPerPage: 10
      };

      $scope.$watch(function () {
        return self.paginationConf.itemsPerPage + " " + self.paginationConf.currentPage;
      }, self.search);

      self.getAllProcesses();
    }

    self.onDatetimeRangeChanged = function (start, end) {
      self.searchCondition.fromTime = start ? start.toDate().getTime() : start;
      self.searchCondition.toTime = end ? end.toDate().getTime() : end;
    };

    $scope.$watch(function () {
      return self.paginationConf.itemsPerPage + " " + self.paginationConf.currentPage;
    }, self.search);

    self.processes = [];
    self.getAllProcesses = function () {
      var searchCondition = {
        pageSize: 9999,
        pageNum: 1
      };
      ProcessService.search(searchCondition).then(function (resp) {
        if (resp && resp.code === 0) {
          self.processes = self.processes.concat(resp.result.list);
        } else {
          ToastService.toast({
            type: "error",
            body: '获取流程模版失败'
          });
        }
      }).catch(ToastService.errHandler);
    }

    // 搜索
    self.search = function () {
      self.loading = true;
      self.searchCondition.pageSize = self.paginationConf.itemsPerPage;
      self.searchCondition.pageNum = self.paginationConf.currentPage;
      ProcessTaskService.search(self.searchCondition).then(function (resp) {
        self.loading = false;
        if (resp.code === 0) {
          self.paginationConf.totalItems = resp.result.total;
          self.processTasks = resp.result.list;
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

    // 添加
    self.add = function () {
      ProcessTaskAction.addProcessTask().then(function (processTask) {
        ToastService.toast({
          type: 'success',
          body: '添加环节成功！'
        });
        self.search();
      }).catch(function (reason) {
        ToastService.errHandler(reason);
      });
    };

    // 启用、禁用
    self.changeState = function (id) {
      ProcessTaskService.changeState(id).then(function (resp) {
        if (resp && resp.code === 0) {
          self.search();
          ToastService.toast({
            type: 'success',
            body: resp.result
          });
        }
      }).catch(function (err) {
        ToastService.toast({
          type: 'error',
          body: err.message
        });
      });
    };

    // 环节配置标签
    self.addTaskLabel = function (data) {
      ProcessTaskAction.addTaskLabel({processTask: data}).then(function (resp) {
        ToastService.toast({
          type: 'success',
          body: '环节配置标签成功！'
        });
        self.search();
      }).catch(function (reason) {
        ToastService.errHandler(reason);
      });
    }

    // 配置时间
    self.changeTime = function (data) {
      ProcessTaskAction.changeTime({processTask: data}).then(function (resp) {
        ToastService.toast({
          type: 'success',
          body: '预警时间和超时时间设置成功！'
        });
        self.search();
      }).catch(function (reason) {
        ToastService.errHandler(reason);
      });
    }

    // 配置角色
    self.changeRole = function (data) {
      ProcessTaskAction.changeRole({processTask: data}).then(function (resp) {
        ToastService.toast({
          type: 'success',
          body: '角色设置成功！'
        });
        self.search();
      }).catch(function (reason) {
        ToastService.errHandler(reason);
      });
    }

    self.init();
  }

})(angular, CONTEXT_PATH);