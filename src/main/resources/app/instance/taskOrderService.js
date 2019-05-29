/**
 * Created by wangzhibin on 2018/1/31.
 */

(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('Instance')
    .service('TaskOrderService', ['HttpService', TaskOrderService])
    .service('TaskOrderAction', ['TaskOrderService', '$uibModal', TaskOrderAction])
  ;

  function TaskOrderService(HttpService, $uibModal) {
    this.searchPatentTaskOrder = function (searchCondition) {
      return HttpService.get(CONTEXT_PATH + '/api/instance/process/order/searchPatentTaskOrder', searchCondition);
    };
    this.searchEnterpriseInfoTaskOrder = function (searchCondition) {
      return HttpService.get(CONTEXT_PATH + '/api/instance/process/order/searchEnterpriseInfoTaskOrder', searchCondition);
    };
    this.searchEnterpriseRequirementTaskOrder = function (searchCondition) {
      return HttpService.get(CONTEXT_PATH + '/api/instance/process/order/searchEnterpriseRequirementTaskOrder', searchCondition);
    };
    this.searchEnterpriseRequirementMatchPatentTaskOrder = function (searchCondition) {
      return HttpService.get(CONTEXT_PATH + '/api/instance/process/order/searchEnterpriseRequirementMatchPatentTaskOrder', searchCondition);
    };
    this.save = function (taskOrderDealParams) {
      return HttpService.post(CONTEXT_PATH + '/api/instance/process/order/save', taskOrderDealParams);
    };
    this.hold = function (taskOrderDealParam) {
      return HttpService.post(CONTEXT_PATH + '/api/instance/process/order/hold/', taskOrderDealParam);
    };
    this.deal = function (taskOrderDealParam) {
        return HttpService.post(CONTEXT_PATH + '/api/instance/process/order/deal/', taskOrderDealParam);
    };
    this.getUsers = function (taskOrderId) {
      return HttpService.get(CONTEXT_PATH + '/api/instance/process/order/users/' + taskOrderId, {});
    };
    this.getAutoIndexLabels = function (taskOrderId) {
      return HttpService.get(CONTEXT_PATH + '/api/instance/process/order/labels/auto/' + taskOrderId, {});
    };
    this.getManualIndexLabels = function (taskOrderId) {
      return HttpService.get(CONTEXT_PATH + '/api/instance/process/order/labels/manual/' + taskOrderId, {});
    };
    this.getSemiAutoIndexLabels = function (taskOrderId) {
    	return HttpService.get(CONTEXT_PATH + '/api/instance/process/order/labels/semi/' + taskOrderId, {});
    };
    this.getValueIndexLabels = function (taskOrderId) {
      return HttpService.get(CONTEXT_PATH + '/api/instance/process/order/labels/value/' + taskOrderId, {});
    };
    this.getValueIndexDetail = function(patentId, orderId) {
      var url = orderId ? (CONTEXT_PATH + '/api/instance/process/order/value/' + orderId) : (CONTEXT_PATH + '/api/instance/process/order/detail/' + patentId);
      return HttpService.get(url, {_random: Math.random()});
    };
    this.getPriceIndexLabels = function (taskOrderId) {
      return HttpService.get(CONTEXT_PATH + '/api/instance/process/order/labels/price/' + taskOrderId, {});
    };
    this.maudit = function (taskOrderId) {
    	return HttpService.get(CONTEXT_PATH + '/api/instance/process/order/labels/maudit/' + taskOrderId, {});
    };
    this.semiaudit = function (taskOrderId) {
    	return HttpService.get(CONTEXT_PATH + '/api/instance/process/order/labels/semiaudit/' + taskOrderId, {});
    };
    this.maudit = function (taskOrderId) {
    	return HttpService.get(CONTEXT_PATH + '/api/instance/process/order/labels/maudit/' + taskOrderId, {});
    };
    this.searchPatent = function (searchCondition) {
      return HttpService.get(CONTEXT_PATH + '/api/elsearch/patent', searchCondition);
    };
    this.getRedeployCandidateUser = function (taskOrderId) {
      return HttpService.get(CONTEXT_PATH + '/api/instance/process/order/redeploy/users/' + taskOrderId, {});
    };
    this.redeploy = function (taskOrderDealParams) {
      return HttpService.post(CONTEXT_PATH + '/api/instance/process/order/redeploy', taskOrderDealParams);
    };
  }

  function TaskOrderAction(TaskOrderService, $uibModal) {
    // 派单
    this.openAssignOrderEditer = function (modalParams) {
      var alertModalInstance = $uibModal.open({
        animation: true,
        templateUrl: CONTEXT_PATH + '/static/tpls/instance/assignOrder.html',
        controller: 'AssignOrderController',
        controllerAs: 'assignOrderCtrl',
        size: 'lg',
        resolve: {
          modalParams: modalParams
        }
      });
      return alertModalInstance.result;
    }

    // 转派
    this.openRedeployOrderEditer = function (modalParams) {
      var alertModalInstance = $uibModal.open({
        animation: true,
        templateUrl: CONTEXT_PATH + '/static/tpls/instance/redeployOrder.html',
        controller: 'RedeployOrderController',
        controllerAs: 'roc',
        size: 'lg',
        resolve: {
          modalParams: modalParams
        }
      });
      return alertModalInstance.result;
    }

    //编辑退单原因
    this.openReasonEditer = function (params) {
      var reasonEditerInstance = $uibModal.open({
        animation: true,
        templateUrl: CONTEXT_PATH + '/static/tpls/instance/chargebackReasonEditer.html',
        controller: 'ChargebackReasonEditerController',
        controllerAs: 'chargebackReasonEditerCtrl',
        size: 'lg',
        resolve: {
        	params: params
        }
      });
      return reasonEditerInstance.result;
    }
  }

})(angular, CONTEXT_PATH);