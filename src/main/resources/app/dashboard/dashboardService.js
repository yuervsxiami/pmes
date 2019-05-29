/**
 * Created by wangzhibin on 2018/3/10.
 */
(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('Dashboard')
    .service('DashboardPersonalService', ['HttpService', DashboardPersonalService])
  ;

  function DashboardPersonalService(HttpService) {
    this.findDashboardOrders1 = function (condition) {
      return HttpService.get(CONTEXT_PATH + '/api/instance/process/order/due', condition);
    }

    this.findDashboardOrders2 = function (condition) {
      return HttpService.get(CONTEXT_PATH + '/api/instance/process/order/alert', condition);
    }

    this.findDashboardOrders3 = function (condition) {
      return HttpService.get(CONTEXT_PATH + '/api/instance/process/order/unfinished', condition);
    }

    this.findDashboardOrders4 = function (condition) {
      return HttpService.get(CONTEXT_PATH + '/api/instance/process/order/finished', condition);
    }

    this.findDashboardOrders5 = function (condition) {
      return HttpService.get(CONTEXT_PATH + '/api/instance/process/order/back', condition);
    }
  }
})(angular, CONTEXT_PATH);
