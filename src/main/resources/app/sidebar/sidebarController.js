/**
 * Created by xiongwei on 2017/2/6.
 */

(function (angular) {
  'use strict';
  angular.module('SIDEBAR')
    .controller('SidebarCtrl', ['AuthService','$rootScope', '$scope', '$state', '$stateParams', 'ToastService', 'AuthorityService', SidebarCtrl])
  ;

  function SidebarCtrl(AuthService,$rootScope, $scope, $state, $stateParams, ToastService, AuthorityService) {
    var self = this;
    self.selectedState = $state.current.name;
    // 监听state状态变化,切换菜单选中状态
    $rootScope.$on('$stateChangeSuccess', function (event, next, nextParams, fromParams) {
      self.selectedState = next.name;
    });

		AuthService.getLoginUser().then(function (loginUser) {
			self.user = loginUser;
			self.getAuthorities();
		}).catch(ToastService.errHandler);

    self.keywords = $stateParams.keywords || '';
    // 搜索
    self.onSearch = function (event) {
      if (event.keyCode === 13) {
        self.search();
      }
    };

    // 专利搜索
    self.search = function () {
      $state.go('main.console.elsearch.patent', {keywords: self.keywords})
    };

    self.getAuthorities = function () {
			AuthorityService.getAuthoritiesByRoleId(self.user.roleId).then(function (resp) {
       //  AuthorityService.getAuthorities().then(function (resp) {
          if (resp && resp.code === 0) {
            self.authorities = resp.result;
          } else {
            ToastService.toast({
              type: 'error',
              body: '请求权限失败：' + resp.message
            });
          }
        }).catch(function (err) {
          ToastService.errHandler(err);
        });
      };
    
    // 是否激活选中的菜单
    self.isActive = function (auth) {
//        if (typeof paths === 'string') {
//            paths = [paths];
//        }
//        return paths.some(function (p) {
//            return self.selectedState.indexOf(p) >= 0;
//        });
      if (!auth) {
        return false;
      }
      var paths = auth.sonAuthorities.length > 0 ? auth.sonAuthorities.map(function (subAuth) {
        return subAuth.url;
      }) : [auth.url];
      return paths.some(function (p) {
        return p && self.selectedState.indexOf(p) >= 0;
      });
    };

  }

})(angular);