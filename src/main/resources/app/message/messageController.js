/**
 * Created by xiongwei on 2017/1/3.
 */
(function (angular) {
  'use strict';
  angular.module('Message')
    .controller('MessageController', ['$scope', '$state', '$window', '$log', 'AuthService', 'ToastService', 'ModalService', 'MessageService', MessageController])
  ;
  
  function MessageController($scope, $state, $window, $log, AuthService, ToastService, ModalService, MessageService) {
    var self = this;
    
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });

    self.init = function () {

			//配置分页基本参数
			self.paginationConf = {
				currentPage: 1,
				itemsPerPage: 10,
			};

			$scope.$watch(function () {
				return self.paginationConf.itemsPerPage + " " + self.paginationConf.currentPage;
			}, self.getAllMessage);

    };


    
    self.getAllMessage = function () {
      self.loading = true;
      MessageService.getMessages(self.paginationConf.currentPage,self.paginationConf.itemsPerPage).then(function (resp) {
        self.loading = false;
        if (resp.code === 0) {
          self.paginationConf.totalItems = resp.result.total;
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
      }).catch(function (err) {
        self.loading = false;
        ToastService.errHandler(err);
      });
    };
    
    self.dealMessage = function (message) {
      if(message.url) {
    	  $state.go(message.url);
      }
    };
    
    self.init();
	    
  }

})(angular);