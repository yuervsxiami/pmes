/**
 * Created by xiongwei on 2016/12/29.
 */

(function (angular, contextPath) {
  'use strict';
  angular.module('SERVICES', [])
    .service('HttpService', ['$q', '$http', HttpService])
    .service('AuthService', ['$q', 'HttpService', AuthService])
    .service('ToastService', ['$rootScope', '$log', 'GLOBAL_EVENTS', ToastService])
    .service('FormService', FormService)
    .service('MessageService', ['HttpService', MessageService])
    .service('StatService', ['HttpService', StatService])
  ;

  function HttpService($q, $http) {
    var httpService = function (config) {
      var defer = $q.defer();
      if (config) {
        config.timeout = 1800000;
      }
      $http(config)
        .then(function (resp) {
          defer.resolve(resp.data);
        }, function (err) {
          defer.reject(err);
        });
      return defer.promise;
    };
    return {
      get: function (url, params) {
        if (params && typeof params === 'object') {
          params._random = Math.random();
        }
        return httpService({
          method: 'GET',
          url: url,
          params: params
        });
      },
      post: function (url, data) {
        return httpService({
          method: 'POST',
          url: url,
          data: data,
        });
      },
      form: function (url, data) {
        return httpService({
          method: 'POST',
          url: url,
          data: data,
          headers: {'Content-Type': 'application/x-www-form-urlencoded'},
          transformRequest: function (obj) {
            var str = [];
            for (var p in obj) {
              str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
            }
            return str.join("&");
          }
        });
      },
      put: function (url, data) {
        return httpService({
          method: 'PUT',
          url: url,
          data: data
        });
      },
      patch: function (url, data) {
        return httpService({
          method: 'PATCH',
          url: url,
          data: data
        });
      },
      delete: function (url, params) {
        return httpService({
          method: 'DELETE',
          url: url,
          params: params
        });
      },
      head: function (url, params) {
        return httpService({
          method: 'HEAD',
          url: url,
          params: params
        });
      }
    };
  }

  function AuthService($q, HttpService) {
    var self = this;

    // 获取已登陆的用户信息
    var getLoginUser = function () {
      if (self.loginUser) {
        return $q.when(self.loginUser);
      }
      var query = {
        random: Math.random()
      };
      return HttpService.get(contextPath + '/api/user', query).then(function (resp) {
        self.loginUser = resp.result;
        return self.loginUser;
      });
    };
    // 保存登陆的用户信息
    var updateLoginUser = function (user) {
      self.loginUser = user;
    };

    return {
      getLoginUser: getLoginUser,
      updateLoginUser: updateLoginUser,
    };
  }

  function ToastService($rootScope, $log, GLOBAL_EVENTS) {

    var toast = function (toastInfo) {
      // toast
      $rootScope.$broadcast(GLOBAL_EVENTS.PendingToast, toastInfo);
    };

    var errHandler = function (err) {
      $log.error('error', err);
      if (typeof err !== 'string' && err.status !== 401 && err.status !== 403 && err.status !== 500) {
        toast({
          type: 'error',
          body: err.message || '出错了'
        });
      }
    };
    return {
      toast: toast,
      errHandler: errHandler
    };
  }

  function FormService() {
    // 表单验证函数
    this.getCssClasses = function (ngModelController) {
      return {
        'has-error': ngModelController && ngModelController.$dirty && ngModelController.$invalid
      };
    };
    this.showError = function (ngModelController, error) {
      return ngModelController && ngModelController.$dirty && ngModelController.$error[error];
    };
    this.formCanSave = function (ngFromController) {
      return ngFromController && ngFromController.$dirty && ngFromController.$valid;
    };
  }

  function MessageService(HttpService) {
    this.getMessages = function (pageNum, pageSize) {
      return HttpService.get(CONTEXT_PATH + '/api/message/', {pageNum: pageNum, pageSize: pageSize});
    };
  }

  function StatService(HttpService) {
    // 专利定单数统计
    this.countPatentProcessOrders = function () {
      return HttpService.get(CONTEXT_PATH + '/api/stat/patent/process/orders', {});
    };
    // 指定流程定单数统计
    this.countProcessOrders = function (processType) {
      return HttpService.get(CONTEXT_PATH + '/api/stat/process/orders/' + processType, {});
    };
    // 统计指定订单下各种工单的处理时间
    this.countUseTimeByProcess = function (processId) {
      return HttpService.get(CONTEXT_PATH + '/api/stat/process/taskUseTime/' + processId, {});
    };
    // 统计指定订单下各种工单的预警数量
    this.countAlertNumByProcess = function (processId) {
      return HttpService.get(CONTEXT_PATH + '/api/stat/process/taskAlertNum/' + processId, {});
    };
    // 统计指定订单下各种工单的超时数量
    this.countDueNumByProcess = function (processId) {
      return HttpService.get(CONTEXT_PATH + '/api/stat/process/taskDueNum/' + processId, {});
    };
    // 统计指定订单下预警工单数量最多的人
    this.countMaxUserAlert = function (processId) {
      return HttpService.get(CONTEXT_PATH + '/api/stat/process/alertUser/' + processId, {});
    };
    // 统计指定订单下超时工单数量最多的人
    this.countMaxUserDue = function (processId) {
      return HttpService.get(CONTEXT_PATH + '/api/stat/process/dueUser/' + processId, {});
    };
    /**
     * 按流程查询不同类型的定单统计详情
     * @param type due,alert,processing,done
     */
    this.countDetailForProcessOrders = function (type) {
      return HttpService.get(CONTEXT_PATH + '/api/stat/patent/process/orders/detail/' + type, {});
    }
  }

})(angular, CONTEXT_PATH);