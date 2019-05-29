/**
 * Created by xiongwei 2018/2/4 上午10:27.
 */

(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('ProcessOrder')
    .service('ActService', ['HttpService', ActService])
    .service('ProcessOrderService', ['HttpService', ProcessOrderService])
    .service('ProcessOrderAction', ['$uibModal', ProcessOrderAction])
  ;

  function ActService(HttpService) {
    /**
     * 查询流程执行状态坐标信息
     * @param actTaskId
     */
    this.getCurrentProcessLocation = function (processKey, actTaskId) {
      return HttpService.get(CONTEXT_PATH + '/api/act/image/' + processKey + '/' + actTaskId);
    }

  }

  function ProcessOrderService(HttpService) {

    this.processOrderDetail = function (processOrderId) {
      return HttpService.get(CONTEXT_PATH + '/api/process/order/' + processOrderId, {_random: Math.random()});
    };

    this.searchPatent = function (condition) {
      return HttpService.get(CONTEXT_PATH + '/api/process/order/searchPatent', condition);
    }

    this.searchRequirement = function (condition) {
      return HttpService.get(CONTEXT_PATH + '/api/process/order/searchRequirement', condition);
    }

    this.getAllProcessOrders = function (qry) {
      return HttpService.get(CONTEXT_PATH + '/api/process/order/all', qry);
    };

    this.taskOrderLabels = function (id) {
      return HttpService.get(CONTEXT_PATH + '/api/process/order/task/' + id + '/labels', {_random: Math.random()});
    }

    this.getAllProcesses = function (instanceType) {
      return HttpService.get(CONTEXT_PATH + '/api/process/all/' + instanceType, {_random: Math.random()});
    }

		this.processOrderLabels = function (id) {
			return HttpService.get(CONTEXT_PATH + '/api/process/labels/' + id, {_random: Math.random()});
		}
  }
  
  function ProcessOrderAction($uibModal) {
    /**
     * 查看工单详情
     * @param taskOrder
     */
    this.viewTaskOrderDetail = function (taskOrder) {
      var taskOrderModalInstance = $uibModal.open({
        animation: true,
        templateUrl: CONTEXT_PATH + '/static/tpls/processorder/taskOrderDetail.html',
        controller: 'TaskOrderDetailController',
        size: 'lg',
        resolve: {
          taskOrder: taskOrder
        }
      });
      return taskOrderModalInstance.result;
    };

		/**
		 * 查看定单详情
		 * @param taskOrder
		 */
		this.viewProcessOrderDetail = function (processOrder) {
			var processOrderModalInstance = $uibModal.open({
				animation: true,
				templateUrl: CONTEXT_PATH + '/static/tpls/processorder/processOrderDetail.html',
				controller: 'ProcessOrderDetailController',
				size: 'lg',
				resolve: {
					processOrder: processOrder
				}
			});
			return processOrderModalInstance.result;
		}
  }

})(angular, CONTEXT_PATH);
