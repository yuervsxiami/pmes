/**
 * Created by wangzhibin on 2018/3/13.
 */
/**
 * Created by wangzhibin on 2018/1/29.
 */
(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('Patent')
    .controller('TimedTaskController', ['$scope', '$filter', 'ToastService', 'TimedTaskService', 'TIMED_TASK_TYPE', 'TIMED_TASK_SEARCH_TYPES', '$state', TimedTaskController])
    .controller('TimedTaskDetailController', ['$scope', '$filter', 'ToastService', 'TimedTaskService', 'TIMED_TASK_TYPE', 'TIMED_TASK_SEARCH_TYPES', '$state', 'HumenService', 'ModalService', TimedTaskDetailController])
    .controller('IndexErrorController', ['TimedTaskService', 'ToastService', '$scope', IndexErrorController])
  ;

  function IndexErrorController(TimedTaskService, ToastService, $scope) {
		$scope.$on('$viewContentLoaded', function (event) {
			$.AdminLTE.layout.fix();
		});

		var self = this;

		self.init = function () {

			self.loading = false;

			//配置分页基本参数
			self.paginationConf = {
				currentPage: 1,
				itemsPerPage: 10
			};

			// 查询条件
			$scope.$watch(function () {
				return self.paginationConf.itemsPerPage + " " + self.paginationConf.currentPage;
			}, self.search);
    };

		self.search = function () {
			self.loading = true;
			self.searching = true;
			TimedTaskService.getIndexError(self.paginationConf).then(function (resp) {
				self.loading = false;
				if (resp.code === 0) {
					self.searching = false;
					self.paginationConf.totalItems = resp.result.total;
					self.list = resp.result.list;
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
			})
		};

		self.init();

  };

  function TimedTaskController($scope, $filter, ToastService, TimedTaskService, TIMED_TASK_TYPE, TIMED_TASK_SEARCH_TYPES, $state) {
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });

    var self = this;

    self.init = function () {
      self.loading = false;
      //配置分页基本参数
      self.paginationConf = {
        currentPage: 1,
        itemsPerPage: 10
      };

      self.timeTaskSeachTypes = TIMED_TASK_SEARCH_TYPES;
      self.timedTaskTypes = TIMED_TASK_TYPE;
      self.timedTaskStates = [{id: 0, name: "未完成"}, {id: 1, name: "已完成"}];

      // 查询条件
      self.reset();
      $scope.$watch(function () {
        return self.paginationConf.itemsPerPage + " " + self.paginationConf.currentPage;
      }, self.search);
    }

    // 重置
    self.reset = function () {
      self.searchCondition = {};
    }

    self.onDatetimeRangeChanged = function (start, end) {
      self.searchCondition.fromCreateTime = start ? start.toDate().getTime() : start;
      self.searchCondition.toCreateTime = end ? end.toDate().getTime() : end;
    };

    self.getPercent = function(data) {
      if (data==null || data.finishAmount==null || data.totalAmount==null){
        return null;
      }
      return Math.floor(100*data.finishAmount/data.totalAmount) + '%';
    }

    self.goDetail = function(id) {
    	$state.go("main.console.patent.assessment.task.detail", {id:id})
		};

    // 搜索
    self.search = function () {
      self.loading = true;
      self.searchCondition.pageSize = self.paginationConf.itemsPerPage;
      self.searchCondition.pageNum = self.paginationConf.currentPage;
      if(!self.searchCondition.types) {
				self.searchCondition.types = null;
			}
      TimedTaskService.search(self.searchCondition).then(function (resp) {
        self.loading = false;
        if (resp.code === 0) {
          self.paginationConf.totalItems = resp.result.total;
          self.tasks = resp.result.list;
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
      })
    };

    self.init();
  };

	function TimedTaskDetailController($scope, $filter, ToastService, TimedTaskService, TIMED_TASK_TYPE, TIMED_TASK_SEARCH_TYPES, $state, HumenService, ModalService) {
		$scope.$on('$viewContentLoaded', function (event) {
			$.AdminLTE.layout.fix();
		});

		var self = this;

		self.init = function () {
			self.loading = false;
			//配置分页基本参数
			self.paginationConf = {
				currentPage: 1,
				itemsPerPage: 10
			};

			self.timeTaskSeachTypes = TIMED_TASK_SEARCH_TYPES;
			self.timedTaskTypes = TIMED_TASK_TYPE;

			// 查询条件
			self.reset();
			$scope.$watch(function () {
				return self.paginationConf.itemsPerPage + " " + self.paginationConf.currentPage;
			}, self.search);
		};

		// 重置
		self.reset = function () {
			self.searchCondition = {id:$state.params.id};
		};

		self.changeState = function (state) {
			switch (state) {
				case 0:
					return '计算中';
				case 1:
					return '成功';
				case 2:
					return '失败';
			}
			return '未完成';
		};

		self.start = function (patent) {
			patent.disabled = true;
			HumenService.start(patent.id).then(function (resp) {
				patent.disabled = false;
				if (resp.code === 0) {
					ToastService.toast({
						type: 'success',
						body: resp.result
					});
					patent.hasBatchIndexed = 1;
					patent.updateTime = new Date();
				}
				else {
					ToastService.toast({
						type: 'error',
						body: '重新评估失败'
					});
				}
			}).catch(function (reason) {
				patent.disabled = false;
				ToastService.errHandler(reason);
			})
		};

		self.onLegalStatusSelected = function (selected) {
			self.searchCondition.lastLegalStatus = [];
			if (selected == null) {
				return;
			}
			selected.forEach(function (m) {
				self.searchCondition.lastLegalStatus.push(m.value == null ? m.name : m.value);
			})
		};

		self.showLog = function (log) {
			ModalService.openAlert({
				type: 'info',
				title: '错误日志',
				hideCancel: true,
				message: log
			}).then(function (code) {
				$log.info('AlertModal result: ', code);
			}).catch(function (err) {
				$log.info('AlertModal dismissed at: ' + new Date(), err);
			});
		};

		// 搜索
		self.search = function () {
			self.loading = true;
			self.searchCondition.pageSize = self.paginationConf.itemsPerPage;
			self.searchCondition.pageNum = self.paginationConf.currentPage;
			TimedTaskService.searchDetail(self.searchCondition).then(function (resp) {
				self.loading = false;
				if (resp.code === 0) {
					self.paginationConf.totalItems = resp.result.total;
					self.patents = resp.result.list;
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
			})
		};

		self.init();
	};

})(angular, CONTEXT_PATH);
