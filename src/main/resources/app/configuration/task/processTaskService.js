/**
 * Created by wangzhibin on 2018/1/15.
 */

(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('Configuration')
    .service('ProcessTaskService', ['HttpService', ProcessTaskService])
    .service('ProcessTaskAction', ['ProcessTaskService', '$uibModal', ProcessTaskAction])
  ;

  function ProcessTaskService(HttpService, $uibModal) {
    this.search = function(searchCondition) {
      return HttpService.get(CONTEXT_PATH + '/api/processtask/search', searchCondition);
    };
    this.add = function(processTask) {
      return HttpService.post(CONTEXT_PATH + '/api/processtask/add', processTask);
    };
    this.changeState = function(id) {
      return HttpService.get(CONTEXT_PATH + '/api/processtask/changestate/'+id, {});
    };
    this.changeTime = function(processTask) {
      return HttpService.post(CONTEXT_PATH + '/api/processtask/changetime', processTask);
    };
    this.addTaskLabel = function(processTask) {
      return HttpService.post(CONTEXT_PATH + '/api/processtask/addtasklabels', processTask);
    };
    this.changeRole = function(processTask) {
      return HttpService.post(CONTEXT_PATH + '/api/processtask/changerole', processTask);
    };
    this.getLabels = function(id) {
			return HttpService.get(CONTEXT_PATH + '/api/processtask/labels/' + id);
    }
  }

  function ProcessTaskAction(ProcessTaskService, $uibModal) {
    // 添加
    this.addProcessTask = function () {
      var alertModalInstance = $uibModal.open({
        animation: true,
        templateUrl: CONTEXT_PATH + '/static/tpls/configuration/task/addTask.html',
        controller: 'AddProcessTaskController',
        controllerAs: 'addProcessTask',
        size: 'lg'
      });
      return alertModalInstance.result;
    }

    // 环节标签设置
    this.addTaskLabel = function (editConfig) {
      var alertModalInstance = $uibModal.open({
        animation: true,
        templateUrl: CONTEXT_PATH + '/static/tpls/configuration/task/addTaskLabel.html',
        controller: 'AddTaskLabelController',
        controllerAs: 'addTaskLabel',
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
        templateUrl: CONTEXT_PATH + '/static/tpls/configuration/task/changeTime.html',
        controller: 'ChangeTimeController',
        controllerAs: 'changeTime',
        size: 'lg',
        resolve: {
          editConfig: editConfig
        }
      });
      return alertModalInstance.result;
    }

    // 设置时间
    this.changeRole = function (editConfig) {
      var alertModalInstance = $uibModal.open({
        animation: true,
        templateUrl: CONTEXT_PATH + '/static/tpls/configuration/task/changeRole.html',
        controller: 'ChangeRoleController',
        controllerAs: 'changeRole',
        size: 'lg',
        resolve: {
          editConfig: editConfig
        }
      });
      return alertModalInstance.result;
    }
  }

})(angular, CONTEXT_PATH);