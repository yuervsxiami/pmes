/**
 * Created by xiongwei 2018/2/4 上午10:27.
 */


(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('ProcessOrder')
    .controller('ProcessDetailController', ['$scope', '$state', '$stateParams', 'ToastService', 'ProcessOrderService',
      'ProcessOrderAction', 'ActService', 'PATENT_PROCESS_TYPE', 'PATENT_PROCESS_IMAGES', ProcessDetailController])
    .controller('TaskOrderDetailController', ['$scope', '$uibModalInstance', 'ToastService', 'ProcessOrderService', 'taskOrder', TaskOrderDetailController])
    .controller('ProcessOrderDetailController', ['$scope', '$uibModalInstance', 'ToastService', 'ProcessOrderService', 'processOrder', ProcessOrderDetailController])
    .controller('ProcessOrderQueryController', ['$scope', '$state', '$stateParams', 'ModalService', 'ToastService', 'ProcessOrderService',
      'ProcessOrderAction', 'ActService', 'INSTANCE_TYPE', 'PROCESS_ORDER_STATE', ProcessOrderQueryController])
    .controller('ProcessDetailsController', ['$scope', '$state', '$stateParams', 'ToastService', 'ProcessOrderService',
      'ProcessOrderAction', 'ActService', 'PATENT_PROCESS_TYPE', ProcessDetailsController])
  ;

  function ProcessDetailController($scope, $state, $stateParams, ToastService, ProcessOrderService, ProcessOrderAction, ActService, PATENT_PROCESS_TYPE, PATENT_PROCESS_IMAGES) {
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });

    var self = this;
    self.loading = false;

    // 获取流程图url
    self.processImageUrl = function (processType) {
      var processImageInfo = PATENT_PROCESS_IMAGES.filter(function (p) {
        return p.id == processType;
      })[0];
      /**
      var latestTask = self.processOrder.taskOrders[self.processOrder.taskOrders.length - 1];
//      return processType == 0 ? '' : CONTEXT_PATH + '/api/act/image/' + processInfo.key;
      return processType == 0 ? '' : CONTEXT_PATH + '/api/act/image/' + latestTask.actTaskId;
       */
      // console.log('processType', processType);
      return processImageInfo ? (CONTEXT_PATH + processImageInfo.url) : '';
    };

    // 读取定单详情
    self.getOrderDetail = function (processOrderId) {
      self.loading = true;
      ProcessOrderService.processOrderDetail(processOrderId).then(function (resp) {
        self.loading = false;
        if (resp.code === 0) {
          self.processOrder = resp.result;
          // 查询流程进度信息
          self.getCurrentProcessLocation();
        } else {
          ToastService.toast({
            type: 'error',
            body: '获取流程详情出错：' + resp.message
          });
        }
      }).catch(function (err) {
        self.loading = false;
        ToastService.errHandler(err);
      });
    };
		// 查看工单详情
		self.viewTaskOrder = function (taskOrder) {
			ProcessOrderAction.viewTaskOrderDetail(taskOrder).catch(function (reason) {  });
		};
		// 查看定单详情
		self.viewProcessOrder = function () {
			ProcessOrderAction.viewProcessOrderDetail(self.processOrder).catch(function (reason) {  });
		};
    // 查询流程执行进度
    function getImageWidth(url, callback){
      var img = new Image();
      img.src = url;

      // 如果图片被缓存，则直接返回缓存数据
      if(img.complete){
        callback(img.width, img.height);
      }else{
        // 完全加载完毕的事件
        img.onload = function(){
          callback(img.width, img.height);
        }
      }
    }
    self.getCurrentProcessLocation = function () {
      var workflow = PATENT_PROCESS_TYPE.filter(function (flow) {
        return flow.id === self.processOrder.processType;
      })[0];
      var latestTask = self.processOrder.taskOrders[self.processOrder.taskOrders.length - 1];
      ActService.getCurrentProcessLocation(workflow.key, latestTask.actTaskId).then(function (resp) {
        if (resp.code === 0) {
          self.currentProcessLocation = resp.result;
          if (self.currentProcessLocation && typeof self.currentProcessLocation.width !== 'undefined') {
            var indicator = angular.element('#process_current_indicator');
            var image = angular.element('.img-responsive');
            var imgSrc = image.attr("src");
            $(window).on('resize', function () {
              getImageWidth(imgSrc, function (width, height) {
                var scale = image.width() / width;
                indicator.css('border', '2px solid red');
                indicator.css('width', (self.currentProcessLocation.width * scale) + 'px');
                indicator.css('height', (self.currentProcessLocation.height * scale) + 'px');
                indicator.css('left', (self.currentProcessLocation.x * scale + 10) + 'px');
                indicator.css('top', (self.currentProcessLocation.y * scale + 10) + 'px');
              });
            });
            $(window).trigger('resize');
          }
        } else {
          ToastService.toast({
            type: 'error',
            body: '获取流程进度信息出错：' + resp.message
          });
        }
      }).catch(function (err) {
        ToastService.errHandler(err);
      });
    };

    // 查询定单详情
    self.getOrderDetail($stateParams.orderId);
  }

  function ProcessDetailsController($scope, $state, $stateParams, ToastService, ProcessOrderService, ProcessOrderAction, ActService, PATENT_PROCESS_TYPE) {
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });

    var self = this;

    self.init = function () {
      self.loading = false;
      self.instanceType = $stateParams.instanceType;
      self.instanceId = $stateParams.instanceId;
      self.processOrders = [];

      self.getAllProcesses();
    }

    self.processes = [];
    self.getAllProcesses = function () {
      self.loading = true;
      ProcessOrderService.getAllProcesses(self.instanceType).then(function (resp) {
        self.loading = false;
        if (resp && resp.code === 0) {
          self.processes = self.processes.concat(resp.result);
          self.getAllProcessOrders();
        } else {
          ToastService.toast({
            type: "error",
            body: '获取流程模版失败'
          });
        }
      }).catch(function (err) {
        self.loading = false;
        ToastService.errHandler(err);
      });
    }

    // 读取所有定单
    self.getAllProcessOrders = function () {
      self.loading = true;
      var qry = {
        instanceType: self.instanceType,
        instanceId: self.instanceId,
        processId: null
      };

      ProcessOrderService.getAllProcessOrders(qry).then(function (resp) {
        self.loading = false;
        if (resp.code === 0) {
          self.processOrders = [];
          self.processOrders = self.processOrders.concat(resp.result);
          self.views = self.processOrders;
          self.tabs = [];
          if (self.processes != null) {
            self.processes.forEach(function (p) {
              var tab = {
                data: p,
                num: self.processOrders == null ? 0 : self.processOrders.filter(function (o) {
                  return p.id == o.processCnfId;
                }).length,
              };
              self.tabs.push(tab);
            });
          }
        } else {
          ToastService.toast({
            type: 'error',
            body: '获取流程详情出错：' + resp.message
          });
        }
      }).catch(function (err) {
        self.loading = false;
        ToastService.errHandler(err);
      });
    };

    self.onTabSelected = function (index) {
      if (index == 100) {
        self.views = self.processOrders;
        return;
      }
      self.views = [];
      if (self.processOrders != null) {
        self.processOrders.forEach(function (data) {
          if (data.processCnfId == self.tabs[index].data.id) {
            self.views.push(data);
          }
        });
      }
    }

    // 查看工单详情
    self.viewTaskOrder = function (taskOrder) {
      ProcessOrderAction.viewTaskOrderDetail(taskOrder).catch(function (reason) {
      });
    };

    self.init();
  }

  /**
   * 工单详情CTRL
   * @param $scope
   * @param $uibModalInstance
   * @param ToastService
   * @param ProcessOrderService
   * @constructor
   */
  function TaskOrderDetailController($scope, $uibModalInstance, ToastService, ProcessOrderService, taskOrder) {
    $scope.loading = false;
    $scope.taskOrder = taskOrder;

    $scope.ok = function () {
      $uibModalInstance.close();
    };
    $scope.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };

    $scope.getTaskOrderLabels = function () {
      $scope.loading = true;
      ProcessOrderService.taskOrderLabels($scope.taskOrder.id).then(function (resp) {
        $scope.loading = false;
        if (resp.code === 0) {
          $scope.labels = resp.result;
        } else {
          ToastService.toast({
            type: 'error',
            body: '获取工单详情出错：' + resp.message
          });
        }
      }).catch(function (err) {
        $scope.loading = false;
        ToastService.errHandler(err);
      });
    };

    //
    $scope.getTaskOrderLabels();

  }

  /**
   * 定单详情CTRL
   * @param $scope
   * @param $uibModalInstance
   * @param ToastService
   * @param ProcessOrderService
   * @constructor
   */
  function ProcessOrderDetailController($scope, $uibModalInstance, ToastService, ProcessOrderService, processOrder) {
    $scope.loading = false;
    $scope.processOrder = processOrder;

    $scope.ok = function () {
      $uibModalInstance.close();
    };
    $scope.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };

    $scope.getTaskOrderLabels = function () {
      $scope.loading = true;
      ProcessOrderService.processOrderLabels($scope.processOrder.id).then(function (resp) {
        $scope.loading = false;
        if (resp.code === 0) {
          $scope.labels = resp.result;
        } else {
          ToastService.toast({
            type: 'error',
            body: '获取定单详情出错：' + resp.message
          });
        }
      }).catch(function (err) {
        $scope.loading = false;
        ToastService.errHandler(err);
      });
    };

    //
    $scope.getTaskOrderLabels();

  }

  function ProcessOrderQueryController($scope, $state, $stateParams, ModalService, ToastService, ProcessOrderService,
                                       ProcessOrderAction, ActService, INSTANCE_TYPE, PROCESS_ORDER_STATE) {
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });

    var self = this;

    self.init = function () {
      self.loading = false;
      self.instanceType = INSTANCE_TYPE.filter(function (type) {
        return type.key == $stateParams.instanceType;
      })[0];

      self.processOrderStates = PROCESS_ORDER_STATE;
      self.processOrders = [];

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

      self.getAllProcesses();
    }

    self.reset=function(){
      self.searchCondition = {};
      self.nationalEconomyField = null;
      self.currentNationalEconomyField = null;

    }

    self.processes = [];
    self.getAllProcesses = function () {
      ProcessOrderService.getAllProcesses(self.instanceType.id).then(function (resp) {
        if (resp && resp.code === 0) {
          self.processes = self.processes.concat(resp.result);
        } else {
          ToastService.toast({
            type: "error",
            body: '获取流程模版失败'
          });
        }
      }).catch(ToastService.errHandler);
    }

    self.onDatetimeRangeChanged = function (property, start, end) {
      if (property == "up") {
        self.searchCondition.fromUpdateTime = start ? start.toDate().getTime() : start;
        self.searchCondition.toUpdateTime = end ? end.toDate().getTime() : end;
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

    // 搜索
    self.search = function () {
      self.loading = true;
      self.searchCondition.pageSize = self.paginationConf.itemsPerPage;
      self.searchCondition.pageNum = self.paginationConf.currentPage;

      if (self.instanceType.key == "Patent") {
        self.searchCondition.instanceType = self.instanceType.id;
        self.searchCondition.actInstanceId = $scope.nullIfSpaceOrEmpty(self.searchCondition.actInstanceId);
        self.searchCondition.actTaskId = $scope.nullIfSpaceOrEmpty(self.searchCondition.actTaskId);
        self.searchCondition.state = self.searchCondition.state;
        self.searchCondition.an = $scope.nullIfSpaceOrEmpty(self.searchCondition.an);
        self.searchCondition.ti = $scope.nullIfSpaceOrEmpty(self.searchCondition.ti);
        self.searchCondition.pa = $scope.nullIfSpaceOrEmpty(self.searchCondition.pa);
        self.searchCondition.pin = $scope.nullIfSpaceOrEmpty(self.searchCondition.pin);
        self.searchCondition.lastLegalStatus = $scope.nullIfSpaceOrEmpty(self.searchCondition.lastLegalStatus);
        self.searchCondition.fromUpdateTime = self.searchCondition.fromUpdateTime;
        self.searchCondition.toUpdateTime = self.searchCondition.toUpdateTime;
        ProcessOrderService.searchPatent(self.searchCondition).then(function (resp) {
          self.loading = false;
          if (resp.code === 0) {
            self.paginationConf.totalItems = resp.result.total;
            self.processOrders = [];
            self.processOrders = self.processOrders.concat(resp.result.list);
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
      else if (self.instanceType.key == "EnterpriseRequire") {
        self.searchCondition.instanceType = self.instanceType.id;
        self.searchCondition.actInstanceId = $scope.nullIfSpaceOrEmpty(self.searchCondition.actInstanceId);
        self.searchCondition.actTaskId = $scope.nullIfSpaceOrEmpty(self.searchCondition.actTaskId);
        self.searchCondition.state = self.searchCondition.state;
        self.searchCondition.name = $scope.nullIfSpaceOrEmpty(self.searchCondition.name);
        self.searchCondition.requirement = $scope.nullIfSpaceOrEmpty(self.searchCondition.requirement);
        self.searchCondition.fromUpdateTime = self.searchCondition.fromUpdateTime;
        self.searchCondition.toUpdateTime = self.searchCondition.toUpdateTime;
        self.searchCondition.nationalEconomyField = self.currentNationalEconomyField == null ? null : self.currentNationalEconomyField.code;

        ProcessOrderService.searchRequirement(self.searchCondition).then(function (resp) {
          self.loading = false;
          if (resp.code === 0) {
            self.paginationConf.totalItems = resp.result.total;
            self.processOrders = [];
            self.processOrders = self.processOrders.concat(resp.result.list);
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

    self.init();
  }

})(angular, CONTEXT_PATH);
