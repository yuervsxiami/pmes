/**
 * Created by xiongwei on 2017/1/3.
 */

(function (angular) {
  'use strict';
  angular.module('Dashboard')
    .controller('DashboardPersonalController', ['$scope', 'ToastService', 'DashboardPersonalService', DashboardPersonalController])
    .controller('PatentDashboardCtrl', ['$scope', 'ToastService', 'ModalService', 'StatService', PatentDashboardCtrl])
    .controller('EnterpriseDashboardCtrl', ['$scope', 'ToastService', 'ModalService', 'StatService', EnterpriseDashboardCtrl])
  ;

  /**
   * EnterpriseDashboardCtrl
   * @param ModalService
   * @constructor
   */
  function EnterpriseDashboardCtrl($scope, ToastService, ModalService, StatService) {
  }
  
  /**
   * DashboardCtrl
   * @param ModalService
   * @constructor
   */
  function PatentDashboardCtrl($scope, ToastService, ModalService, StatService) {
    var self = this;
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });
    // 专利定单数量统计结果
    self.processOrderStat = {};
    self.currentProcessType = 2;
    self.specialProcessOrderStat = {};
    
    self.changeCurrentProcessType = function (type) {
      self.currentProcessType = type;
    };

    // 专利定单数量统计
    self.countPatentProcessOrders = function () {
      StatService.countPatentProcessOrders().then(function (resp) {
        if (resp && resp.code === 0) {
          angular.merge(self.processOrderStat, resp.result);
        }
      });
    };
    // init
    self.countPatentProcessOrders();
    
  }

  function DashboardPersonalController($scope, ToastService, DashboardPersonalService) {
    var self = this;
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });

    self.init = function () {
      self.loading = {
        o1: false,
        o2: false,
        o3: false,
        o4: false,
        o5: false,
      };

      self.searching = {
        o1: false,
        o2: false,
        o3: false,
        o4: false,
        o5: false,
      };

      self.orders = {
        o1: null,
        o2: null,
        o3: null,
        o4: null,
        o5: null,
      }

      //配置分页基本参数
      self.paginationConf = {
        o1: {currentPage: 1, itemsPerPage: 10},
        o2: {currentPage: 1, itemsPerPage: 10},
        o3: {currentPage: 1, itemsPerPage: 10},
        o4: {currentPage: 1, itemsPerPage: 10},
        o5: {currentPage: 1, itemsPerPage: 10},
      }

      $scope.$watch(function () {
        return self.paginationConf.o1.itemsPerPage + " " + self.paginationConf.o1.currentPage;
      }, self.findDashboardOrders1);
      $scope.$watch(function () {
        return self.paginationConf.o2.itemsPerPage + " " + self.paginationConf.o2.currentPage;
      }, self.findDashboardOrders2);
      $scope.$watch(function () {
        return self.paginationConf.o3.itemsPerPage + " " + self.paginationConf.o3.currentPage;
      }, self.findDashboardOrders3);
      $scope.$watch(function () {
        return self.paginationConf.o4.itemsPerPage + " " + self.paginationConf.o4.currentPage;
      }, self.findDashboardOrders4);
      $scope.$watch(function () {
        return self.paginationConf.o5.itemsPerPage + " " + self.paginationConf.o5.currentPage;
      }, self.findDashboardOrders5);

    }

    // 超时
    self.findDashboardOrders1 = function (mode) {

      self.loading.o1 = true;
      var condition = {
        pageSize: self.paginationConf.o1.itemsPerPage,
        pageNum: self.paginationConf.o1.currentPage
      };
      DashboardPersonalService.findDashboardOrders1(condition).then(function (resp) {
        self.loading.o1 = false;
        if (resp.code === 0) {
          self.paginationConf.o1.totalItems = resp.result.total;
          self.orders.o1 = resp.result.list;
          self.searching.o1 = false;
        }
        else {
          ToastService.toast({
            type: 'error',
            body: resp.message
          });
        }
      }).catch(function (reason) {
        self.loading.o1 = false;
        ToastService.errHandler(reason);
      })
    };

    // 预警
    self.findDashboardOrders2 = function (mode) {

      self.loading.o2 = true;
      var condition = {
        pageSize: self.paginationConf.o2.itemsPerPage,
        pageNum: self.paginationConf.o2.currentPage
      };
      DashboardPersonalService.findDashboardOrders2(condition).then(function (resp) {
        self.loading.o2 = false;
        if (resp.code === 0) {
          self.paginationConf.o2.totalItems = resp.result.total;
          self.orders.o2 = resp.result.list;
          self.searching.o2 = false;
        }
        else {
          ToastService.toast({
            type: 'error',
            body: resp.message
          });
        }
      }).catch(function (reason) {
        self.loading.o2 = false;
        ToastService.errHandler(reason);
      })
    };

    // 未完成
    self.findDashboardOrders3 = function (mode) {

      self.loading.o3 = true;
      var condition = {
        pageSize: self.paginationConf.o3.itemsPerPage,
        pageNum: self.paginationConf.o3.currentPage
      };
      DashboardPersonalService.findDashboardOrders3(condition).then(function (resp) {
        self.loading.o3 = false;
        if (resp.code === 0) {
          self.paginationConf.o3.totalItems = resp.result.total;
          self.orders.o3 = resp.result.list;
          self.searching.o3 = false;
        }
        else {
          ToastService.toast({
            type: 'error',
            body: resp.message
          });
        }
      }).catch(function (reason) {
        self.loading.o3 = false;
        ToastService.errHandler(reason);
      })
    };

    // 已完成
    self.findDashboardOrders4 = function (mode) {

      self.loading.o4 = true;
      var condition = {
        pageSize: self.paginationConf.o4.itemsPerPage,
        pageNum: self.paginationConf.o4.currentPage
      };
      DashboardPersonalService.findDashboardOrders4(condition).then(function (resp) {
        self.loading.o4 = false;
        if (resp.code === 0) {
          self.paginationConf.o4.totalItems = resp.result.total;
          self.orders.o4 = resp.result.list;
          self.searching.o4 = false;
        }
        else {
          ToastService.toast({
            type: 'error',
            body: resp.message
          });
        }
      }).catch(function (reason) {
        self.loading.o4 = false;
        ToastService.errHandler(reason);
      })
    };

    // 退单
    self.findDashboardOrders5 = function (mode) {

      self.loading.o5 = true;
      var condition = {
        pageSize: self.paginationConf.o5.itemsPerPage,
        pageNum: self.paginationConf.o5.currentPage
      };
      DashboardPersonalService.findDashboardOrders5(condition).then(function (resp) {
        self.loading.o5 = false;
        if (resp.code === 0) {
          self.paginationConf.o5.totalItems = resp.result.total;
          self.orders.o5 = resp.result.list;
          self.searching.o5 = false;
        }
        else {
          ToastService.toast({
            type: 'error',
            body: resp.message
          });
        }
      }).catch(function (reason) {
        self.loading.o5 = false;
        ToastService.errHandler(reason);
      })
    };


    self.init();
  }
})(angular);