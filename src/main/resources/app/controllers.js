/**
 * Created by xiongwei on 2016/12/29.
 */

(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('PMES2')
    .controller('AppCtrl', ['$rootScope', '$scope', '$window', '$state', 'toaster', '$log',
      'AuthService', 'ModalService', 'GLOBAL_EVENTS', AppCtrl])
    .controller('NavCtrl', ['$scope', '$state', '$window', '$log', 'AuthService', 'ToastService', 'ModalService', 'MessageService', NavCtrl])
  ;

  function AppCtrl($rootScope, $scope, $window, $state, toaster, $log, AuthService, ModalService, GLOBAL_EVENTS) {
    var self = this;

    self.selectedState = $state.current.name;
    // 监听state状态变化,切换菜单选中状态
    $rootScope.$on('$stateChangeSuccess', function (event, next, nextParams, fromParams) {
      self.selectedState = next.name;
    });

    // 没有权限
    $rootScope.$on(GLOBAL_EVENTS.UnAuthorized, function (event) {
      ModalService.openAlert({
        type: 'error',
        title: '提示',
        hideCancel: true,
        message: '您没有访问权限!'
      }).then(function (code) {
        $log.info('AlertModal result: ', code);
      }).catch(function (err) {
        $log.info('AlertModal dismissed at: ' + new Date(), err);
      });
    });
    // 没有登录
    $rootScope.$on(GLOBAL_EVENTS.UnAuthenticated, function (event) {
      self.login();
    });
    // 其他服务器异常
    $rootScope.$on(GLOBAL_EVENTS.ServerException, function (event) {
      // ModalService.openAlert({
      //   type: 'error',
      //   title: '提示',
      //   hideCancel: true,
      //   message: '系统出错!'
      // }).then(function (code) {
      //   $log.info('AlertModal result: ', code);
      // }).catch(function (err) {
      //   $log.info('AlertModal dismissed at: ' + new Date(), err);
      // });
    });
    // toast
    $rootScope.$on(GLOBAL_EVENTS.PendingToast, function (event, toast) {
      toaster.pop(toast);
    });

    $scope.nullIfSpaceOrEmpty = function (str) {
      if (typeof str === 'string') {
        var trim = str.trim();
        return trim.length === 0 ? null : trim;
      }
      return str;
    };

    $scope.getNationalEconomy = function (national) {
      if (national == null) {
        return null;
      }
      else if (national.t4 != null) {
        return national.t4;
      }
      else if (national.t3 != null) {
        return national.t3;
      }
      else if (national.t2 != null) {
        return national.t2;
      }
      else if (national.t1 != null) {
        return national.t1;
      }
      return null;
    }

    // 表单验证函数
    $scope.getCssClasses = function (ngModelController) {
      return {
        'has-error': ngModelController && ngModelController.$dirty && ngModelController.$invalid
      };
    };
    $scope.showError = function (ngModelController, error) {
      return ngModelController && ngModelController.$dirty && ngModelController.$error[error];
    };
    $scope.formCanSave = function (ngFromController) {
      return ngFromController && ngFromController.$dirty && ngFromController.$valid;
    };

    // 登陆
    self.login = function () {
      $window.location.href = CONTEXT_PATH + '/login';
    };
  }
  
  function NavCtrl($scope, $state, $window, $log, AuthService, ToastService, ModalService, MessageService) {
    var self = this;
    self.contextPath = CONTEXT_PATH;
    // 获取当前登陆用户信息
    AuthService.getLoginUser().then(function (loginUser) {
      self.loginUser = loginUser;
      self.getAllMessage();
    }).catch(ToastService.errHandler);
    // 登出
    self.logout = function () {
      $window.location.href = CONTEXT_PATH + '/logout';
    };

    self.openChangePassword = function(){
      ModalService.openChangePasswordEditer({}).then(function (resp) {
        if (resp == 0) {
          //self.search();
        }
      }).catch(function (err) {
        $log.info(err);
      });
    };

		self.openChangeProfile = function(){
			ModalService.openChangeProfileEditer({}).then(function (resp) {
				if (resp == 0) {
					//self.search();
				}
			}).catch(function (err) {
				$log.info(err);
			});
		};
    
    self.getAllMessage = function () {
      MessageService.getMessages(1,10).then(function (resp) {
        if (resp.code === 0) {
          self.messages = resp.result.list;
          self.unreadNum = 0;
          for(var i = 0; i<self.messages.length; i++) {
        	var message = self.messages[i];
          }
          // 查询流程进度信息
        } else {
          ToastService.toast({
            type: 'error',
            body: '获取消息列表出错：' + resp.message
          });
        }
      }).catch(ToastService.errHandler);
    }
    
    self.dealMessage = function (message) {
      if(message.url) {
    	  $state.go(message.url);
      }
    }
    
  }

})(angular, CONTEXT_PATH);