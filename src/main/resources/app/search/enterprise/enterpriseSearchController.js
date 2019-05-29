/**
 * Created by xiongwei 2018/2/4 上午10:27.
 */


(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('Search')
    .controller('EnterpriseSearchController', ['$scope', '$state', '$stateParams', 'ModalService', 'ToastService', 'UserService', 'EnterpriseSearchService', EnterpriseSearchController])
    .controller('EnterpriseDetailController', ['$scope', '$state', '$stateParams', '$log', 'ToastService', 'EnterpriseSearchService', EnterpriseDetailController])
  ;

  function EnterpriseSearchController($scope, $state, $stateParams, ModalService, ToastService, UserService, EnterpriseSearchService) {
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });

    var self = this;
    self.init=function() {
      self.loading = false;
      //配置分页基本参数
      self.paginationConf = {
        currentPage: 1,
        itemsPerPage: 10
      };
      // 查询条件
      self.reset();
      // 查询操作人
      self.getAllUsers();

      $scope.$watch(function () {
        return self.paginationConf.itemsPerPage + " " + self.paginationConf.currentPage;
      }, self.search);
    };

    self.reset=function() {
      self.query = {
        state: 1
      };
      self.nationalEconomyField = null;
      self.currentNationalEconomyField = null;
      self.region = null;
    };

    self.getAllUsers = function () {
      UserService.getUsers(1, 9999).then(function (resp) {
        if (resp && resp.code === 0) {
          self.users = resp.result.list;
        } else {
          ToastService.toast({
            type: "error",
            body: '获取用户失败'
          });
        }
      }).catch(ToastService.errHandler);
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

    // 操作时间范围回调
    self.onDatetimeRangeChanged = function (property, start, end) {
      self.query.optDateFrom = start ? start.toDate().getTime() : start;
      self.query.optDateTo = end ? end.toDate().getTime() : end;
    };

    // 搜索
    self.search = function () {
      self.loading = true;
      self.query.pageSize = self.paginationConf.itemsPerPage;
      self.query.pageNum = self.paginationConf.currentPage;
      self.query.name = $scope.nullIfSpaceOrEmpty(self.query.name);
      self.query.nationalEconomyField = self.currentNationalEconomyField == null ? null : self.currentNationalEconomyField.code;
      self.query.provinceId = self.region == null ? null : (self.region.t1 == null ? null : self.region.t1.id);
      self.query.cityId = self.region == null ? null : (self.region.t2 == null ? null : self.region.t2.id);
      self.query.districtId = self.region == null ? null : (self.region.t3 == null ? null : self.region.t3.id);

      EnterpriseSearchService.searchCompleteEnterprises(self.query).then(function (resp) {
        self.loading = false;
        if (resp.code === 0) {
          self.paginationConf.totalItems = resp.result.total;
          self.enterprises = resp.result.list;
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

    //
    self.init();

  }
  
  function EnterpriseDetailController($scope, $state, $stateParams, $log, ToastService, EnterpriseSearchService) {
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });

    var self = this;
    self.basicLoading = false;
    self.detailLoading = false;

    // 查询企业信息详情
    self.findEnterpriseDetail = function (enterpriseId) {
      EnterpriseSearchService.detail(enterpriseId).then(function (resp) {
        self.basicLoading = false;
        if (resp.code === 0) {
          self.enterprise = resp.result;
        }
        else {
          ToastService.toast({
            type: 'error',
            body: '查询企业信息出错：' + resp.message
          });
        }
      }).catch(function (reason) {
        self.basicLoading = false;
        ToastService.errHandler(reason);
      });
    };
    self.findLabels = function (enterpriseId) {
      self.detailLoading = true;
      EnterpriseSearchService.labels(enterpriseId).then(function (resp) {
        self.detailLoading = false;
        if (resp.code === 0) {
          self.labels = resp.result;
        }
        else {
          ToastService.toast({
            type: 'error',
            body: '查询企业详情出错：' + resp.message
          });
        }
      }).catch(function (reason) {
        self.detailLoading = false;
        ToastService.errHandler(reason);
      });
    };

    // init
    self.findEnterpriseDetail($stateParams.id);
    self.findLabels($stateParams.id);
  }

})(angular, CONTEXT_PATH);
