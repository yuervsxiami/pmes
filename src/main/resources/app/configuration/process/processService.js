/**
 * Created by wangzhibin on 2018/1/15.
 */

(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('Configuration')
    .service('ProcessService', ['HttpService', ProcessService])
    .service('ProcessAction', ['ProcessService', '$uibModal', ProcessAction])
  ;

  function ProcessService(HttpService, $uibModal) {
    this.search = function(searchCondition) {
      return HttpService.get(CONTEXT_PATH + '/api/process/search', searchCondition);
    };
    this.edit = function(process) {
      return HttpService.post(CONTEXT_PATH + '/api/process/edit', process);
    };
    this.changeState = function(id) {
      return HttpService.get(CONTEXT_PATH + '/api/process/changestate/'+id, {});
    };
    this.setLabelset = function(process) {
      return HttpService.post(CONTEXT_PATH + '/api/process/setlabelset', process);
    };
    this.removeLabelset = function(id) {
      return HttpService.get(CONTEXT_PATH + '/api/process/removelabelset/'+id, {});
    };
    this.changeTime = function(process) {
      return HttpService.post(CONTEXT_PATH + '/api/process/changetime', process);
    };
    this.get = function(id) {
			return HttpService.get(CONTEXT_PATH + '/api/process/' + id);
    }
  }

  function ProcessAction(ProcessService, $uibModal) {
    // 添加、修改
    this.processEditer = function (editConfig) {
      var alertModalInstance = $uibModal.open({
        animation: true,
        templateUrl: CONTEXT_PATH + '/static/tpls/configuration/process/processEditer.html',
        controller: 'ProcessEditerController',
        controllerAs: 'processEditer',
        size: 'lg',
        resolve: {
          editConfig: editConfig
        }
      });
      return alertModalInstance.result;
    }

    // 设置标签体系
    this.setLabelset = function (editConfig) {
      var alertModalInstance = $uibModal.open({
        animation: true,
        templateUrl: CONTEXT_PATH + '/static/tpls/configuration/process/setLabelset.html',
        controller: 'SetLabelsetController',
        controllerAs: 'setLabelset',
        size: 'lg',
        resolve: {
          editConfig: editConfig
        }
      });
      return alertModalInstance.result;
    }

    // 设置时间
    this.changeTime = function (editConfig) {
      var alertModalInstance = $uibModal.open({
        animation: true,
        templateUrl: CONTEXT_PATH + '/static/tpls/configuration/process/changeTime.html',
        controller: 'ChangeTimeController',
        controllerAs: 'changeTime',
        size: 'lg',
        resolve: {
          editConfig: editConfig
        }
      });
      return alertModalInstance.result;
    }
  }

})(angular, CONTEXT_PATH);