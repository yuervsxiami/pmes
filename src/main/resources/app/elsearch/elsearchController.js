/**
 * Created by xiongwei 2018/2/8 下午2:15.
 */

(function (angular) {
  'use strict';
  angular.module('ElSearch')
    .controller('ElPatentCtrl', ['$scope', '$state', '$stateParams', 'ToastService', 'ModalService', 'ElPatentService', 'ElPatentAction', ElPatentCtrl])
  ;

  function ElPatentCtrl($scope, $state, $stateParams, ToastService, ModalService, ElPatentService, ElPatentAction) {
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });
    var self = this;
    self.loading = false;
    self.keywords = $stateParams.keywords || '';
    //配置分页基本参数
    self.paginationConf = {
      currentPage: 1,
      itemsPerPage: 50
    };

    $scope.$watch(function () {
      return self.paginationConf.itemsPerPage + " " + self.paginationConf.currentPage;
    }, self.search);

    // 搜索
    self.onSearch = function (event) {
      if (event.keyCode === 13) {
        self.search();
      }
    };
    // 搜索
    self.onSearch = function (event) {
      if (event.keyCode === 13) {
        self.search();
      }
    };

    // 专利搜索
    self.search = function () {
      self.loading = true;
      ElPatentService.search(self.keywords, self.paginationConf.currentPage, self.paginationConf.itemsPerPage).then(function (resp) {
        self.loading = false;
        if (resp.code === 0) {
          self.paginationConf.totalItems = resp.result.total;
          self.patents = resp.result.list;
          self.searching = false;
        } else {
          ToastService.toast({
            type: 'error',
            body: '查询企业信息出错：' + resp.message
          });
        }
      }).catch(function (reason) {
        self.loading = false;
        ToastService.errHandler(reason);
      })
    };

    // 搜索
    self.search();

  }

})(angular);
