/**
 * Created by wangzhibin on 2018/1/31.
 */

(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('Instance')
    .controller('TaskOrderController', ['$scope', '$interval', '$state', '$stateParams', '$filter', '$location',
      'ToastService', 'ModalService', 'TaskOrderService', 'TaskOrderAction', 'UserService',
      'PATENT_PROCESS_TYPE', 'PATENT_TASK_TYPE', 'TASK_ORDER_STATE', 'AuthService', TaskOrderController])
    .controller('AssignOrderController', ['$uibModalInstance', 'ToastService', 'FormService',
      'TaskOrderService', 'modalParams', AssignOrderController])
    .controller('RedeployOrderController', ['$uibModalInstance', 'ToastService', 'FormService',
      'TaskOrderService', 'modalParams', RedeployOrderController])
    .controller('IndexOrderController', ['$scope', '$state', '$stateParams', '$filter', 'ToastService', 'FormService',
      'TaskOrderService', 'PATENT_PROCESS_TYPE', 'PATENT_TASK_TYPE', 'PATENT_LABEL_SOURCE', IndexOrderController])
    .controller('AuditOrderController', ['ToastService', 'FormService','TaskOrderService', '$scope', '$stateParams', 'TaskOrderAction', 'ModalService',
      'PATENT_PROCESS_TYPE', 'PATENT_TASK_TYPE', 'TASK_ORDER_STATE', AuditOrderController])
    .controller('ChargebackReasonEditerController', ['$uibModalInstance', 'ToastService', 'params', 'changeLabelSourceFilter', ChargebackReasonEditerController])
    .controller('ErPMatchDetailController', ['$scope', '$state', '$stateParams', '$filter', 'ToastService', 'FormService', 'TaskOrderService', ErPMatchDetailController])
    .controller('ValueIndexDetailController', ['$scope', '$stateParams', 'ToastService', 'TaskOrderService', 'LabelsetService', 'EXTRA_VALUE_LABELS', ValueIndexDetailController])
  ;

  // 派单
  function AssignOrderController($uibModalInstance, ToastService, FormService, TaskOrderService, modalParams) {
    var self = this;
    angular.merge(self, FormService);

    self.init = function() {
      self.loading = false;
      self.processType = modalParams.processType;
      self.taskType = modalParams.taskType;
      self.taskOrders = modalParams.taskOrders;
      self.keyword = "";
      self.selectedUserId = -1;
      self.users = [];
      self.getUsers()
    }

    // 获得所有用户
    self.getUsers = function () {
      TaskOrderService.getUsers(self.taskOrders[0].id).then(function (resp) {
        if (resp && resp.code === 0) {
          self.users = self.users.concat(resp.result.list);
        } else {
          ToastService.toast({
            type: "error",
            body: '获取用户列表失败:' + resp.message
          });
        }
      }).catch(ToastService.errHandler);
    }

    self.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };

    self.ok = function () {
      if (self.selectedUserId==-1){
        ToastService.toast({
          type: 'warn',
          body: '请分配一名操作人！'
        });
        return;
      };
      var taskOrderDealParams = [];
      self.taskOrders.forEach(function (data) {
        taskOrderDealParams.push({taskOrderId: data.id, assignUserId: self.selectedUserId});
      });
      self.loading = true;
      TaskOrderService.save(taskOrderDealParams).then(function (resp) {
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

  // 转派
  function RedeployOrderController($uibModalInstance, ToastService, FormService, TaskOrderService, modalParams) {
    var self = this;
    angular.merge(self, FormService);

    self.init = function() {
      self.loading = false;
      self.processType = modalParams.processType;
      self.taskType = modalParams.taskType;
      self.taskOrders = modalParams.taskOrders;
      self.keyword = "";
      self.selectedUserId = -1;
      self.users = [];
      self.getUsers()
    }

    // 获得所有用户
    self.getUsers = function () {
      TaskOrderService.getRedeployCandidateUser(self.taskOrders[0].id).then(function (resp) {
        if (resp && resp.code === 0) {
          self.users = self.users.concat(resp.result.list);
        } else {
          ToastService.toast({
            type: "error",
            body: '获取用户列表失败:' + resp.message
          });
        }
      }).catch(ToastService.errHandler);
    };

    self.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };

    self.ok = function () {
      if (self.selectedUserId==-1){
        ToastService.toast({
          type: 'warn',
          body: '请分配一名操作人！'
        });
        return;
      };
      var taskOrderDealParams = [];
      self.taskOrders.forEach(function (data) {
        taskOrderDealParams.push({taskOrderId: data.id, assignUserId: self.selectedUserId});
      });
      self.loading = true;
      TaskOrderService.redeploy(taskOrderDealParams).then(function (resp) {
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

  // 标引
  function IndexOrderController($scope, $state, $stateParams, $filter, ToastService, FormService, TaskOrderService,
                                PATENT_PROCESS_TYPE, PATENT_TASK_TYPE, PATENT_LABEL_SOURCE){
    var self = this;
    angular.merge(self, FormService);

    self.init = function() {
      self.loading = false;
      self.processType = PATENT_PROCESS_TYPE.filter(function (type) {
        return type.key == $stateParams.processType;
      })[0];
      self.taskType = PATENT_TASK_TYPE.filter(function (type) {
        return type.key == $stateParams.taskType;
      })[0];
      self.taskOrderId = $stateParams.taskOrderId;
      self.backUrl = $stateParams.backUrl;
      self.sources = PATENT_LABEL_SOURCE;
      self.taskOrderLabels = [];
      self.reason = null;

      self.isPatent = self.processType.key.toLowerCase().indexOf('patent')>=0;
      self.isEnterprise = self.processType.key.toLowerCase().indexOf('enterprise')>=0;

      self.isAutoIndex = self.taskType.key=="AutoIndex";
      self.isSemiAutoIndex = self.taskType.key=="SemiAutoIndex";
      self.isManualIndex = self.taskType.key=="ManualIndex";
      self.isValueIndex = self.taskType.key=="ValueIndex";
      self.isPriceIndex = self.taskType.key=="PriceIndex";

      self.isReadOnly = self.isAutoIndex || self.isValueIndex;

      if(self.isAutoIndex) {
        self.getAutoIndexLabels();
      }
      if(self.isSemiAutoIndex) {
    	  self.getSemiAutoIndexLabels();
      }
      else if(self.isManualIndex) {
        self.getManualIndexLabels();
      }
      else if(self.isValueIndex) {
        self.getValueIndexLabels();
      }
      else if(self.isPriceIndex) {
        self.getPriceIndexLabels();
      }
    }

    // 获得所有标签 (自动、半自动)
    self.getAutoIndexLabels = function () {
      self.loading = true;
      TaskOrderService.getAutoIndexLabels(self.taskOrderId).then(function (resp) {
        self.loading = false;
        if (resp && resp.code === 0) {
          self.taskOrderLabels = self.taskOrderLabels.concat(resp.result);
          self.tidyTaskOrderLabels();
        } else {
          ToastService.toast({
            type: "error",
            body: '获取工单标签列表失败:' + resp.message
          });
        }
      }).catch(function(reason){
        self.loading = false;
        ToastService.errHandler(reason);
      });
    }

    // 获得所有标签 (人工)
    self.getManualIndexLabels = function () {
      self.loading = true;
      TaskOrderService.getManualIndexLabels(self.taskOrderId).then(function (resp) {
        self.loading = false;
        if (resp && resp.code === 0) {
          self.taskOrderLabels = self.taskOrderLabels.concat(resp.result.labels);
          self.reason = resp.result.reason;
          self.tidyTaskOrderLabels();
        } else {
          ToastService.toast({
            type: "error",
            body: '获取工单标签列表失败:' + resp.message
          });
        }
      }).catch(function(reason){
        self.loading = false;
        ToastService.errHandler(reason);
      });
    }

    // 获得所有标签 (半自动)
    self.getSemiAutoIndexLabels = function () {
      self.loading = true;
      TaskOrderService.getSemiAutoIndexLabels(self.taskOrderId).then(function (resp) {
        self.loading = false;
        console.log(resp);
        if (resp && resp.code === 0) {
          self.taskOrderLabels = self.taskOrderLabels.concat(resp.result.labels);
          self.reason = resp.result.reason;
          self.tidyTaskOrderLabels();
        } else {
          ToastService.toast({
            type: "error",
            body: '获取工单标签列表失败:' + resp.message
          });
        }
      }).catch(function(reason){
        self.loading = false;
        ToastService.errHandler(reason);
      });
    }

    // 获得所有标签 (价值评估)
    self.getValueIndexLabels = function () {
      self.loading = true;
      TaskOrderService.getValueIndexLabels(self.taskOrderId).then(function (resp) {
        self.loading = false;
        if (resp && resp.code === 0) {
          self.taskOrderLabels = self.taskOrderLabels.concat(resp.result);
          self.tidyTaskOrderLabels();
        } else {
          ToastService.toast({
            type: "error",
            body: '获取工单标签列表失败:' + resp.message
          });
        }
      }).catch(function(reason){
        self.loading = false;
        ToastService.errHandler(reason);
      });
    }

    // 获得所有标签 (价格评估)
    self.getPriceIndexLabels = function () {
      self.loading = true;
      TaskOrderService.getPriceIndexLabels(self.taskOrderId).then(function (resp) {
        self.loading = false;
        if (resp && resp.code === 0) {
          self.taskOrderLabels = self.taskOrderLabels.concat(resp.result);
          self.tidyTaskOrderLabels();
        } else {
          ToastService.toast({
            type: "error",
            body: '获取工单标签列表失败:' + resp.message
          });
        }
      }).catch(function(reason){
        self.loading = false;
        ToastService.errHandler(reason);
      });
    }

    //整理 label 默认值等数据
    self.tidyTaskOrderLabels = function(){
      self.taskOrderLabels.forEach(function(data) {
        if (data.label.valueType == 1 || data.label.valueType == 2) {
          if (data.label.value == null) {
            data.label.value = data.label.defaultValue;
          }
        }
        if (data.label.valueType == 5) {
          data.label.minValue = parseFloat(data.label.minValue) == NaN ? 0 : parseFloat(data.label.minValue);
          data.label.maxValue = parseFloat(data.label.maxValue) == NaN ? 0 : parseFloat(data.label.maxValue);
          data.label.defaultValue = parseFloat(data.label.defaultValue) == NaN ? 0 : parseFloat(data.label.defaultValue);
          if (data.label.value == null) {
            data.label.value = data.label.defaultValue;
          }
          else {
            data.label.value = parseFloat(data.label.value) == NaN ? 0 : parseFloat(data.label.value);
          }
        }
      });
    }


    self.validCtrl = function(form, data, checkAll) {
      if (self.isAutoIndex){
        return false;
      }
      var hasError = false;
      var ctrls = form.$$controls.filter(function (ctrl) {
        return ctrl.$name == "tb" + data.label.key;
      });
      if (ctrls.length == 1) {
        // text box
        if (data.label.valueType == 1 && data.label.metaKey == null) {
          hasError = (ctrls[0].$dirty || checkAll) && ctrls[0].$invalid;
        }
        else if (data.label.metaKey != null) {
          if (data.label.isRequired) {
            hasError = (ctrls[0].$dirty || checkAll) && (data.label.value == null || data.label.value == "");
          }
        }
        // text area
        else if (data.label.valueType == 2) {
          hasError = (ctrls[0].$dirty || checkAll) && ctrls[0].$invalid;
        }
        // date
        else if (data.label.valueType == 3) {
          hasError = (ctrls[0].$dirty || checkAll) && ctrls[0].$invalid;
        }
        // number
        else if (data.label.valueType == 5) {
          if (data.label.minValue == data.label.maxValue) {
            hasError = (ctrls[0].$dirty || checkAll) && ctrls[0].$invalid;
          }
          else {
            var value = parseFloat(data.label.value);
            hasError = (ctrls[0].$dirty || checkAll) &&
              (ctrls[0].$invalid || value == NaN || value < data.label.minValue || value > data.label.maxValue)
          }
        }
      }
      return hasError;
    }

    self.canSave = function(form) {
      if (self.isAutoIndex){
        return true;
      }
      var errors = self.taskOrderLabels.filter(function (data) {
        return self.validCtrl(form, data, true);
      });
      return errors = null || errors.length == 0;
    }

    self.back = function () {
      window.location.href = self.backUrl;
    };

    self.save = function () {
      var taskOrderDealParams = [{taskOrderId: self.taskOrderId, caledLabels: self.taskOrderLabels}];
      self.loading = true;
      TaskOrderService.save(taskOrderDealParams).then(function (resp) {
        self.loading = false;
        if (resp && resp.code === 0) {
          ToastService.toast({
            type: 'success',
            body: '标引成功!'
          });
          self.back();
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

    self.hold = function () {
      var taskOrderDealParams = {taskOrderId: self.taskOrderId, caledLabels: self.taskOrderLabels};
      self.loading = true;
      TaskOrderService.hold(taskOrderDealParams).then(function (resp) {
        self.loading = false;
        if (resp && resp.code === 0) {
          ToastService.toast({
            type: 'success',
            body: '保存成功!'
          });
          self.back();
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

  //退单原因编辑
  function ChargebackReasonEditerController($uibModalInstance, ToastService, params, changeLabelSourceFilter) {

    var self = this;

    self.init = function () {
      self.labels = params.labels;
      self.reason1 = params.reason1;
      if(params.lastLabels && params.lastLabels.length > 0) {
        for(var i=0; i<self.labels.length; i++) {
          for(var j=0; j< params.lastLabels.length; j++) {
            if(self.labels[i].id === params.lastLabels[j].id) {
              self.labels[i].id = params.lastLabels[j];
              break;
            }
          }
        }
      }
    };

    self.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };

    self.ok = function () {
      self.makeReason();
      $uibModalInstance.close({
        reason1: self.reason1,
        reason2: self.reason2,
        lastLabels:self.selectedLabels
      });
    };

    self.makeReason = function () {
      self.reason2 = "";
      self.selectedLabels = [];
      for(var i=0; i<self.labels.length; i++) {
        var label = self.labels[i];
        if(label.selected && label.reason) {
          self.reason2 = self.reason2 +
            "<p>名称:" + label.label.name +
            ",来源:" + changeLabelSourceFilter(label.label.source) +
            ",原因:" + label.reason + ";</p>";
          self.selectedLabels.push(label);
        }
      }
    };

    self.init();

  };

  // 审核工单
  function AuditOrderController(ToastService, FormService, TaskOrderService, $scope, $stateParams, TaskOrderAction, ModalService,
                                PATENT_PROCESS_TYPE, PATENT_TASK_TYPE, PATENT_LABEL_SOURCE) {
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });
    var self = this;
    angular.merge(self, FormService);

    self.init = function() {
      self.loading = false;
      self.processType = PATENT_PROCESS_TYPE.filter(function (type) {
        return type.key == $stateParams.processType;
      })[0];
      self.taskType = PATENT_TASK_TYPE.filter(function (type) {
        return type.key == $stateParams.taskType;
      })[0];

      self.isPatent = self.processType.key.toLowerCase().indexOf('patent')>=0;
      self.isEnterprise = self.processType.key.toLowerCase().indexOf('enterprise')>=0;

      self.taskOrderId = $stateParams.taskOrderId;
      self.reason = "";
      self.maudit();
    }

    self.getValue = function (label) {
      if(label.strValue || label.strValue === '') {
        return label.strValue;
      }
      return label.textValue || '';
    }

    self.maudit = function () {
      self.loading = true;
      var result = null;
      if(self.taskType.id === 4) {
    	  result = TaskOrderService.maudit(self.taskOrderId);
      } else if (self.taskType.id === 10) {
    	  result = TaskOrderService.semiaudit(self.taskOrderId);
      } else {
//    	  self.back();
    	  return;
      }
      result.then(function (resp) {
        self.loading = false;
        if (resp.code === 0) {
          if(self.taskType.id === 4) {
        	  self.manualAuditOrder = resp.result.manualAuditOrder;
        	  self.labels = [].concat(self.manualAuditOrder ? self.manualAuditOrder.labels : []);
          }
          if(self.taskType.id === 10) {
              self.semiAutoOrder = resp.result.semiAutoOrder;
              self.labels = [].concat(self.semiAutoOrder ? self.semiAutoOrder.labels : []);
          }
          self.lastReason = resp.result.reason;
        } else {
          ToastService.toast({
            type: "error",
            body: '获取待审核信息失败' + resp.message
          });
          if(resp.message === '您未拥有该工单的处理权限或者该工单已被别人处理') {
            self.back();
          }
        }
      }).catch(function (reason) {
        self.loading = false;
        ToastService.errHandler(reason);
      })
    }

    self.back = function () {
      window.history.back();
    };

    self.editReason = function () {
      if(self.loading) {
        return;
      }
      // 原因编辑
      TaskOrderAction.openReasonEditer({
        labels: self.labels,
        lastLabels: self.lastLabels,
        reason1: self.reason1
      }).then(function (result) {
        self.reason1 = result.reason1;
        self.reason2 = result.reason2;
        self.lastLabels = self.lastLabels;
        self.reason = (self.reason1?("<p>" + self.reason1 + "。</p>"):"") + (self.reason2?("<p>退单标签详情:</p>" + self.reason2 ): "")
      }).catch(function (reason) {
        ToastService.errHandler(reason);
      });
    };

    self.pass = function () {
      self.deal(true);
    };

    self.chargeback = function () {
      if(!self.reason1 && !self.reason2) {
        ModalService.openAlert({
          title: '提示',
          message: '您未填写退单理由,是否确认要退单?'
        }).then(function (code) {
          if (code === 0) {
            self.deal(false);
          }
        }).catch(function (err) {
          $log.info(err);
        });
        return;
      }
      self.deal(false);
    };

    self.deal = function (pass) {
      self.submitting = true;
      TaskOrderService.deal({
        taskOrderId:self.taskOrderId,
        pass: pass?true:false,
        reason:(self.reason1?("<p>" + self.reason1 + "。</p>"):"") + (self.reason2?("<p>退单标签详情:</p>" + self.reason2 ): "")
      }).then(function (resp) {
        self.submitting = false;
        if (resp.code === 0) {
          ToastService.toast({
            type: "success",
            body: pass?"处理成功":"退单成功"
          });
          self.back();
        } else {
          ToastService.toast({
            type: "error",
            body: '处理失败' + resp.message
          });
          if(resp.message === '您未拥有该工单的处理权限或者该工单已被别人处理') {
            self.back();
          }
        }
      }).catch(function (reason) {
        self.submitting = false;
        self.loading = false;
        ToastService.errHandler(reason);
      })
    };

    self.init();
  };

  // 匹配详情
  function ErPMatchDetailController($scope, $state, $stateParams, $filter, ToastService, FormService, TaskOrderService){
    var self = this;
    angular.merge(self, FormService);

    self.init = function() {
      self.loading = false;

      self.taskOrderId = $stateParams.taskOrderId;
      self.backUrl = $stateParams.backUrl;
      self.patents = [];

      self.qry = {
        keywords: $stateParams.keywords,
      };

      //配置分页基本参数
      self.paginationConf = {
        currentPage: 1,
        itemsPerPage: 10
      };

      $scope.$watch(function () {
        return self.paginationConf.itemsPerPage + " " + self.paginationConf.currentPage;
      }, self.getAllPatents);

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

    // 获得所有匹配的专利
    self.getAllPatents = function () {
      self.loading = true;
      self.qry.pageSize = self.paginationConf.itemsPerPage;
      self.qry.pageNum = self.paginationConf.currentPage || 1;

      TaskOrderService.searchPatent(self.qry).then(function (resp) {
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
      });

    };

    self.back = function () {
      window.location.href = self.backUrl;
    };

    self.save = function () {
      var matchResult = "";
      self.patents.forEach(function (item) {
        if (item.selected) {
          if (matchResult == "") {
            matchResult = matchResult + item.data.id;
          }
          else {
            matchResult = matchResult + "," + item.data.id;
          }
        }
      });
      var taskOrderDealParams = [{taskOrderId: self.taskOrderId, matchResult: matchResult}];
      self.loading = true;
      TaskOrderService.save(taskOrderDealParams).then(function (resp) {
        self.loading = false;
        if (resp && resp.code === 0) {
          ToastService.toast({
            type: 'success',
            body: '人工筛选成功!'
          });
          self.back();
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

  // 评估详情
  function ValueIndexDetailController($scope, $stateParams, ToastService, TaskOrderService, LabelsetService, EXTRA_VALUE_LABELS) {
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });

    var self = this;
    self.loading = false;
    self.tree = {
      id: 0,
      name: '专利价值评估'
    };
    self.labelGroups = []; // 专利价值标签的分组，每三个一组，显示成表格
    self.valueKeys = ['patentValue', 'technologicalValue', 'economicValue', 'legalValue'];
    // 专利处理详情
    self.getDetail = function (patentId, orderId) {
      self.loading = true;
      TaskOrderService.getValueIndexDetail(patentId, orderId).then(function (resp) {
        self.loading = false;
        if (resp.code === 0) {
          self.patent = resp.result;
          self.patent.valueIndexOrder.taskOrders.forEach(function (order) {
            order.labels.forEach(function (taskLabel) {
              var key = taskLabel.label.key;
              self.patent[key] = taskLabel.strValue || taskLabel.textValue;
            });
          });
          // 读取标签体系
          self.getValueLabelsetLabels();
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
      LabelsetService.getLabelsetLabels(3).then(function (resp) {
        self.treeCtrl && self.treeCtrl.hideLoading();
        if (resp.code === 0) {
          self.labelsetLabels = resp.result;
          self.labelGroups.splice(0, self.labelGroups.length);
          var cols = 3;
          var valueLabels = self.labelsetLabels.filter(function (label) {
            return label.parentId >= 0;
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
    self.getDetail($stateParams.id, $stateParams.orderId);
  }

  function TaskOrderController($scope, $interval, $state, $stateParams, $filter, $location,
                               ToastService, ModalService  ,TaskOrderService, TaskOrderAction, UserService,
                               PATENT_PROCESS_TYPE, PATENT_TASK_TYPE, TASK_ORDER_STATE, AuthService) {
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });

    var self = this;

    self.init = function () {
      self.loading = false;
      self.contextPath = CONTEXT_PATH;
      self.allSelected = false;
      self.autoRefresh = true;
      self.processType = PATENT_PROCESS_TYPE.filter(function (type) {
        return type.key == $stateParams.processType;
      })[0];
      self.taskType = PATENT_TASK_TYPE.filter(function (type) {
        return type.key == $stateParams.taskType;
      })[0];
      self.taskOrderStates = TASK_ORDER_STATE;
      self.users = [];
      self.getAllUsers();

      self.period = "5";
      self.periods = [
        {id: 1, name: "1分钟"},
        {id: 2, name: "2分钟"},
        {id: 3, name: "3分钟"},
        {id: 4, name: "4分钟"},
        {id: 5, name: "5分钟"}
      ];

			AuthService.getLoginUser().then(function (loginUser) {
				self.user = loginUser;
				if(self.searchCondition && self.user.role.id != 1) {
					self.searchCondition.userId = self.user.id;
				}
			}).catch(ToastService.errHandler);

      self.isPatent = self.processType.key.toLowerCase().indexOf('patent')>=0;
      self.isEnterprise = self.processType.key.toLowerCase().indexOf('enterprise')>=0;

      self.showCheckBox = self.taskType.key == 'AssignOrder' ||
        self.taskType.key == 'AutoIndex' ||
        self.taskType.key == 'ManualIndex' ||
        self.taskType.key == 'ManualIndexAudit' ||
        self.taskType.key == 'SemiAutoIndex' ||
        self.taskType.key == 'SemiAutoIndexAudit'
      ;

      self.showResend = self.taskType.key == 'AutoIndex' ||
        self.taskType.key == 'ManualIndex' ||
        self.taskType.key == 'ManualIndexAudit' ||
        self.taskType.key == 'SemiAutoIndex' ||
        self.taskType.key == 'SemiAutoIndexAudit' ||
        self.taskType.key == 'ManualIndexAudit'
      ;

      self.maxDate = Date.now();
      // 查询条件
			self.reset();

      //配置分页基本参数
      self.paginationConf = {
        currentPage: 1,
        itemsPerPage: 10
      };

      $scope.$watch(function () {
        return self.paginationConf.itemsPerPage + " " + self.paginationConf.currentPage;
      }, self.search);

      // 创建刷新定时器
      self.createTimer();
    }

    self.reset = function(){
      self.searchCondition = {
        processType: self.processType.id,
        taskType: self.taskType.id,
        actTaskId: $stateParams.actTaskId,
      };
      self.nationalEconomyField = null;
      self.currentNationalEconomyField = null;
      self.region = null;
      if(self.user && self.user.role.id != 1) {
        self.searchCondition.userId = self.user.id;
      }
    };

    self.openNationalDlg = function () {
      ModalService.openNationalDlg(self.nationalEconomyField).then(function(data){
        self.nationalEconomyField = data;
        self.currentNationalEconomyField = $scope.getNationalEconomy(data);
      }).catch(function (err) {
        $log.info(err);
      });
    };

    self.openRegionDlg = function () {
      ModalService.openRegionDlg(self.region).then(function (data) {
        self.region = data;
      }).catch(function (err) {
        $log.info(err);
      });
    };

    // 是否定时刷新
    self.onAutoRefreshChanged = function() {
      if (self.timer != null) {
        $interval.cancel(self.timer);
      }
      self.createTimer();
    }

    // 创建刷新定时器
    self.createTimer = function(){
      self.timer = $interval(function () {
        if (self.autoRefresh) {
          self.search();
        }
      }, 1000 * 60 * self.period);   //间隔n秒定时执行
    }
    $scope.$on('$destroy', function () {
      if (self.timer != null) {
        $interval.cancel(self.timer);
      }
    });

    self.onDatetimeRangeChanged = function (property, start, end) {
      if (property == "ad") {
        self.searchCondition.fromAd = start ? start.toDate().getTime() : start;
        self.searchCondition.toAd = end ? end.toDate().getTime() : end;
      }
      else if (property == "up") {
        self.searchCondition.fromUpdateTime = start ? start.toDate().getTime() : start;
        self.searchCondition.toUpdateTime = end ? end.toDate().getTime() : end;
      }
    };

    self.getAllUsers = function () {
      UserService.getUsers(1, 9999).then(function (resp) {
        if (resp && resp.code === 0) {
          self.users = self.users.concat(resp.result.list);
        } else {
          ToastService.toast({
            type: "error",
            body: '获取用户失败'
          });
        }
      }).catch(ToastService.errHandler);
    }

    // allselected checkbox change
    self.changeAllSelected = function () {
      self.taskOrders.forEach(function (item) {
        item.selected = self.allSelected && item.data.state == 0;
      });
    }

    self.changeSelected = function () {
      // 选中个数
      var num = self.taskOrders.filter(function (item) {
        return item.selected
      }).length;
      self.allSelected = num == self.taskOrders.filter(function (item) {
          return item.data.state == 0;
        }).length;
    }

    // 搜索
    self.search = function () {
      self.loading = true;
      self.searchCondition.pageSize = self.paginationConf.itemsPerPage;
      self.searchCondition.pageNum = self.paginationConf.currentPage;

      if (self.processType.key == "enterpriseInfoIndex") {
        // 企业信息
        var qry = {};
        qry.processType = self.searchCondition.processType;
        qry.taskType = self.searchCondition.taskType;
        qry.actInstanceId = $scope.nullIfSpaceOrEmpty(self.searchCondition.actInstanceId);
        qry.actTaskId = $scope.nullIfSpaceOrEmpty(self.searchCondition.actTaskId);
        qry.name = $scope.nullIfSpaceOrEmpty(self.searchCondition.name);
        qry.nationalEconomyField = self.currentNationalEconomyField==null ? null : self.currentNationalEconomyField.code;
        qry.provinceId = self.region == null ? null : (self.region.t1 == null ? null : self.region.t1.id);
        qry.cityId = self.region == null ? null : (self.region.t2 == null ? null : self.region.t2.id);
        qry.districtId = self.region == null ? null : (self.region.t3 == null ? null : self.region.t3.id);
        qry.taskState = self.searchCondition.state;
        qry.userId = self.searchCondition.userId;
        qry.optDateFrom = self.searchCondition.fromUpdateTime;
        qry.optDateTo = self.searchCondition.toUpdateTime;
        qry.pageSize = self.searchCondition.pageSize;
        qry.pageNum = self.searchCondition.pageNum;
        TaskOrderService.searchEnterpriseInfoTaskOrder(qry).then(function (resp) {
          self.loading = false;
          if (resp.code === 0) {
            self.paginationConf.totalItems = resp.result.total;
            self.taskOrders = [];
            resp.result.list.forEach(function (data) {
              self.taskOrders.push({selected: false, data: data});
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
        });
      } else if (self.processType.key == "enterpriseRequireIndex") {
        // 企业需求
        var qry = {};
        qry.processType = self.searchCondition.processType;
        qry.taskType = self.searchCondition.taskType;
        qry.actInstanceId = $scope.nullIfSpaceOrEmpty(self.searchCondition.actInstanceId);
        qry.actTaskId = $scope.nullIfSpaceOrEmpty(self.searchCondition.actTaskId);
        qry.requirement = $scope.nullIfSpaceOrEmpty(self.searchCondition.requirement);
        qry.taskState = self.searchCondition.state;
        qry.userId = self.searchCondition.userId;
        qry.optDateFrom = self.searchCondition.fromUpdateTime;
        qry.optDateTo = self.searchCondition.toUpdateTime;
        if ($scope.nullIfSpaceOrEmpty(self.searchCondition.name) != null ||
          self.nationalEconomyField != null ||
          self.region !=null ) {
          qry["enterprise.name"] = $scope.nullIfSpaceOrEmpty(self.searchCondition.name);
          qry["enterprise.nationalEconomyField"] = self.currentNationalEconomyField == null ? null : self.currentNationalEconomyField.code;
          qry["enterprise.provinceId"] = self.region == null ? null : (self.region.t1 == null ? null : self.region.t1.id);
          qry["enterprise.cityId"] = self.region == null ? null : (self.region.t2 == null ? null : self.region.t2.id);
          qry["enterprise.districtId"] = self.region == null ? null : (self.region.t3 == null ? null : self.region.t3.id);
        }
        qry.pageSize = self.searchCondition.pageSize;
        qry.pageNum = self.searchCondition.pageNum;

        TaskOrderService.searchEnterpriseRequirementTaskOrder(qry).then(function (resp) {
          self.loading = false;
          if (resp.code === 0) {
            self.paginationConf.totalItems = resp.result.total;
            self.taskOrders = [];
            resp.result.list.forEach(function (data) {
              self.taskOrders.push({selected: false, data: data});
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
        });
      } else if (self.processType.key == "enterpriseRequireMatch") {
        // 需求匹配专利
        var qry = {};
        qry.processType = self.searchCondition.processType;
        qry.taskType = self.searchCondition.taskType;
        qry.actInstanceId = $scope.nullIfSpaceOrEmpty(self.searchCondition.actInstanceId);
        qry.actTaskId = $scope.nullIfSpaceOrEmpty(self.searchCondition.actTaskId);
        qry.taskState = self.searchCondition.state;
        qry.userId = self.searchCondition.userId;
        qry.optDateFrom = self.searchCondition.fromUpdateTime;
        qry.optDateTo = self.searchCondition.toUpdateTime;
        if ($scope.nullIfSpaceOrEmpty(self.searchCondition.keywords) != null ||
          $scope.nullIfSpaceOrEmpty(self.searchCondition.requirement) != null) {
          qry["enterpriseRequirement.keywords"] = $scope.nullIfSpaceOrEmpty(self.searchCondition.keywords);
          qry["enterpriseRequirement.requirement"] = $scope.nullIfSpaceOrEmpty(self.searchCondition.requirement);
        }
        if ($scope.nullIfSpaceOrEmpty(self.searchCondition.name) != null ||
          $scope.nullIfSpaceOrEmpty(self.searchCondition.unifiedSocialCreditCode) != null ||
          self.nationalEconomyField != null ||
          self.region != null ||
          $scope.nullIfSpaceOrEmpty(self.searchCondition.type) != null) {
          qry["enterpriseRequirement.enterprise.name"] = $scope.nullIfSpaceOrEmpty(self.searchCondition.name);
          qry["enterpriseRequirement.enterprise.unifiedSocialCreditCode"] = $scope.nullIfSpaceOrEmpty(self.searchCondition.unifiedSocialCreditCode);
          qry["enterpriseRequirement.enterprise.nationalEconomyField"] = self.currentNationalEconomyField == null ? null : self.currentNationalEconomyField.code;
          qry["enterpriseRequirement.enterprise.provinceId"] = self.region == null ? null : (self.region.t1 == null ? null : self.region.t1.id);
          qry["enterpriseRequirement.enterprise.cityId"] = self.region == null ? null : (self.region.t2 == null ? null : self.region.t2.id);
          qry["enterpriseRequirement.enterprise.districtId"] = self.region == null ? null : (self.region.t3 == null ? null : self.region.t3.id);
          qry["enterpriseRequirement.enterprise.type"] = $scope.nullIfSpaceOrEmpty(self.searchCondition.type);
        }
        qry.pageSize = self.searchCondition.pageSize;
        qry.pageNum = self.searchCondition.pageNum;

        TaskOrderService.searchEnterpriseRequirementMatchPatentTaskOrder(qry).then(function (resp) {
          self.loading = false;
          if (resp.code === 0) {
            self.paginationConf.totalItems = resp.result.total;
            self.taskOrders = [];
            resp.result.list.forEach(function (data) {
              self.taskOrders.push({selected: false, data: data});
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
        });
      } else {
        // 专利
        var qry = {};
        qry.processType = self.searchCondition.processType;
        qry.taskType = self.searchCondition.taskType;
        qry.actInstanceId = $scope.nullIfSpaceOrEmpty(self.searchCondition.actInstanceId);
        qry.actTaskId = $scope.nullIfSpaceOrEmpty(self.searchCondition.actTaskId);
        qry.state = self.searchCondition.state;
        qry.an = $scope.nullIfSpaceOrEmpty(self.searchCondition.an);
        qry.ti = $scope.nullIfSpaceOrEmpty(self.searchCondition.ti);
        qry.fromAd = self.searchCondition.fromAd;
        qry.toAd = self.searchCondition.toAd;
        qry.pa = $scope.nullIfSpaceOrEmpty(self.searchCondition.pa);
        qry.pin = $scope.nullIfSpaceOrEmpty(self.searchCondition.pin);
        qry.lastLegalStatus = $scope.nullIfSpaceOrEmpty(self.searchCondition.lastLegalStatus);
        qry.userId = self.searchCondition.userId;
        qry.fromUpdateTime = self.searchCondition.fromUpdateTime;
        qry.toUpdateTime = self.searchCondition.toUpdateTime;
        qry.pageSize = self.searchCondition.pageSize;
        qry.pageNum = self.searchCondition.pageNum;
        TaskOrderService.searchPatentTaskOrder(qry).then(function (resp) {
          self.loading = false;
          if (resp.code === 0) {
            self.paginationConf.totalItems = resp.result.total;
            self.taskOrders = [];
            resp.result.list.forEach(function (data) {
              self.taskOrders.push({selected: false, data: data});
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
        });
      }
    };

    // 处理工单
    self.doTask = function (taskOrder) {
      var taskOrders = [];
      if (taskOrder == null) {
        self.taskOrders.forEach(function (item) {
          if (item.selected) {
            taskOrders.push(item.data);
          }
        });
        if (taskOrders.length == 0) {
          ToastService.toast({
            type: 'warn',
            body: '请至少选择一个工单！'
          });
          return;
        }
      }
      else {
        taskOrders.push(taskOrder);
      }

      switch (self.taskType.key) {
        case "AssignOrder" :
          // 派单
          TaskOrderAction.openAssignOrderEditer({
            processType: self.processType,
            taskType: self.taskType,
            taskOrders: taskOrders
          }).then(function (result) {
            ToastService.toast({
              type: 'success',
              body: '派单成功！'
            });
            self.search();
          }).catch(function (reason) {
            ToastService.errHandler(reason);
          });
          break;
        case "AutoIndex" :
        // 自动标引
        case "SemiAutoIndex" :
        // 半自动标引
        case "ManualIndex" :
        // 人工标引
        case "ValueIndex" :
        // 价值评估
        case "PriceIndex" :
          // 价格评估
          $state.go('main.console.patent.process.index', {
            processType: self.processType.key,
            taskType: self.taskType.key,
            taskOrderId: taskOrder.id,
            backUrl: $location.absUrl()
          });
          break;
        case "ManualIndexAudit" :
        	// 人工标引审核
        case "SemiAutoIndexAudit" :
        	// 半自动标引审核
          $state.go('main.console.patent.process.audit', {
            processType: self.processType.key,
            taskType: self.taskType.key,
            taskOrderId: taskOrder.id,
            backUrl: $location.absUrl()
          });
          break;
        case "ArtificialSelection" :
          // 人工筛选
          $state.go('main.console.match.patent.detail', {
            taskOrderId: taskOrder.id,
            keywords: taskOrder.match.enterpriseRequirement.keywords || taskOrder.match.enterpriseRequirement.requirement,
            backUrl: $location.absUrl()
          });

        default:
          break;
      }
    }

    // 转派
    self.doRedeploy=function(taskOrder) {
      var taskOrders = [];
      taskOrders.push(taskOrder);
      TaskOrderAction.openRedeployOrderEditer({
        processType: self.processType,
        taskType: self.taskType,
        taskOrders: taskOrders
      }).then(function (result) {
        ToastService.toast({
          type: 'success',
          body: '转派成功！'
        });
        self.search();
      }).catch(function (reason) {
        ToastService.errHandler(reason);
      });
    }

    self.init();
  }

})(angular, CONTEXT_PATH);

