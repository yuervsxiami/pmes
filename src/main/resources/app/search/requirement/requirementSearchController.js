/**
 * Created by xiongwei 2018/2/4 上午10:27.
 */


(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('Search')
    .controller('RequirementSearchController', ['$scope', '$state', '$stateParams', 'ModalService', 'ToastService', 'RequirementSearchService', RequirementSearchController])
    .controller('RequirementDetailController', ['$scope', '$state', '$stateParams', '$log', 'ToastService', 'RequirementSearchService', RequirementDetailController])
  ;
  function RequirementSearchController($scope, $state, $stateParams, ModalService, ToastService, RequirementSearchService) {
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });

    var self = this;

    self.init = function () {
      self.loading = false;

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
    };

    self.reset = function () {
      self.query = {
        enterprise: {},
        state: 1,
      };
      self.nationalEconomyField = null;
      self.currentNationalEconomyField = null;
      self.region = null;
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

      self.query.requirement = $scope.nullIfSpaceOrEmpty(self.query.requirement);
      self.query.enterprise.name = $scope.nullIfSpaceOrEmpty(self.query.enterprise.name);
      self.query.enterprise.nationalEconomyField = self.currentNationalEconomyField == null ? null : self.currentNationalEconomyField.code;
      self.query.enterprise.provinceId = self.region == null ? null : (self.region.t1 == null ? null : self.region.t1.id);
      self.query.enterprise.cityId = self.region == null ? null : (self.region.t2 == null ? null : self.region.t2.id);
      self.query.enterprise.districtId = self.region == null ? null : (self.region.t3 == null ? null : self.region.t3.id);
      var query = angular.copy(self.query);
      // enterprise对象需要特殊处理
      var enterprise = query.enterprise;
      if (enterprise) {
        delete query.enterprise;
        angular.forEach(enterprise, function (value, key) {
          query['enterprise.' + key] = value;
        })
      }
      RequirementSearchService.searchCompleteRequirements(query).then(function (resp) {
        self.loading = false;
        if (resp.code === 0) {
          self.paginationConf.totalItems = resp.result.total;
          self.requirements = resp.result.list;
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

    self.init();

  }
  
  function RequirementDetailController($scope, $state, $stateParams, $log, ToastService, RequirementSearchService) {
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });

    var self = this;
    self.basicLoading = false;
    self.detailLoading = false;

    // 查询企业需求详情
    self.findRequirementDetail = function (requirementId) {
      RequirementSearchService.detail(requirementId).then(function (resp) {
        self.basicLoading = false;
        if (resp.code === 0) {
          self.requirement = resp.result;
        }
        else {
          ToastService.toast({
            type: 'error',
            body: '查询企业需求出错：' + resp.message
          });
        }
      }).catch(function (reason) {
        self.basicLoading = false;
        ToastService.errHandler(reason);
      });
    };
    self.findLabels = function (requirementId) {
      self.detailLoading = true;
      RequirementSearchService.labels(requirementId).then(function (resp) {
        self.detailLoading = false;
        if (resp.code === 0) {
          self.labels = resp.result;
        }
        else {
          ToastService.toast({
            type: 'error',
            body: '查询企业需求出错：' + resp.message
          });
        }
      }).catch(function (reason) {
        self.detailLoading = false;
        ToastService.errHandler(reason);
      });
    };

    // init
    self.findRequirementDetail($stateParams.id);
    self.findLabels($stateParams.id);
  }

})(angular, CONTEXT_PATH);
