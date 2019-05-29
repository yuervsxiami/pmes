/**
 * Created by xiongwei on 2016/12/28.
 */

(function (angular) {
  'use strict';

  angular
    .module('PMES2', [
      'CONSTANTS', 'DIRECTIVES', 'COMPONENTS', 'SERVICES', 'MODALS', 'FILTERS', 'SIDEBAR', 'Expert', 'Statistics', 'Match',
      'Dashboard', 'Message', 'System', 'Configuration', 'Patent', 'Enterprise', 'ProcessOrder', 'Search', 'ElSearch', 'Instance',
      'toaster', 'ngAnimate', 'ngRoute', 'ui.router', 'ui.router.state.events', 'ngSanitize', 'ui.select','ngFileUpload'
      ])
    .config(['$qProvider', function ($qProvider) {
      $qProvider.errorOnUnhandledRejections(false);
    }])
    .factory('AuthInterceptor', ['$rootScope', '$q', '$log', 'GLOBAL_EVENTS', function ($rootScope, $q, $log, GLOBAL_EVENTS) {
      return {
        responseError: function (response) {
          var data = response.data;
          var isJson = angular.isObject(data);
          if (isJson && (response.status === 401 || response.status === 403 || response.status === 500)) {
            $rootScope.$broadcast({
              401: GLOBAL_EVENTS.UnAuthenticated,
              403: GLOBAL_EVENTS.UnAuthorized,
              500: GLOBAL_EVENTS.ServerException
            }[response.status], response);
          }
          return $q.reject(response);
        }
      };
    }])
    .config(['$httpProvider', function ($httpProvider) {
      $httpProvider.interceptors.push('AuthInterceptor');
      // 为http请求添加统一的请求头
      $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
    }]);
})(angular);