/**
 * Created by xiongwei on 2017/2/6.
 */

(function (angular) {
  'use strict';
  angular.module('Statistics')
      .controller('WorkloadCtrl', ['$scope', '$state', 'ToastService', 'StatisticsService', WorkloadCtrl])
      .controller('patentIndexOrderController', ['$scope', '$state', 'ToastService', 'StatisticsService', patentIndexOrderController])
			.controller('indexOrderController', ['$scope', '$state', 'ToastService', 'StatisticsService', indexOrderController])
			.controller('patentPatentValueController', ['$scope', '$state', 'ToastService', 'StatisticsService', patentPatentValueController])
    ;

  function WorkloadCtrl($scope, $state, ToastSerrvice, StatisticsService) {

		$scope.$on('$viewContentLoaded', function (event) {
			$.AdminLTE.layout.fix();
		});

		var self = this;

		self.currentStateStyle = function (state) {
			return {
				active: state === self.selectedState
			};
		};

  };

	function patentIndexOrderController($scope, $state, ToastSerrvice, StatisticsService) {

		$scope.$on('$viewContentLoaded', function (event) {
			$.AdminLTE.layout.fix();
		});

		var self = this;

		self.init = function () {
			self.reset();
			self.title = "专利流程定单完成数";
			self.yName = '数量(个)'
			self.searchTime = 0;
			self.xs = [];
			self.numList = [];
			self.search();
		};

		self.reset = function () {
			self.searchCondtion = {};
		};

		self.search = function () {
			self.loading = true;
			StatisticsService.getPatentIndexOrderNum(self.searchCondtion).then(function(resp) {
				self.loading = false;
				if (resp && resp.code === 0) {
					self.xs = [];
					self.numList = [];
					for(var i=0; i<resp.result.length; i++) {
						self.xs.push(resp.result[i].name);
						self.numList.push(resp.result[i].orderNum);
					}
					self.searchTime = self.searchTime + 1;
				} else {
					ToastService.toast({
						type: 'error',
						body: '查询统计信息失败：' + resp.message
					});
				}
			}).catch(function (err) {
				self.loading = false;
				ToastService.toast({
					type: "error",
					body: err.message
				});
			});
		};

		// 操作时间范围回调
		self.onDatetimeRangeChanged = function ( start, end) {
			self.searchCondtion.startTime = start ? start.toDate().getTime() : start;
			self.searchCondtion.endTime = end ? end.toDate().getTime() : end;
		};

		self.init();
	};

	function indexOrderController($scope, $state, ToastSerrvice, StatisticsService) {

		$scope.$on('$viewContentLoaded', function (event) {
			$.AdminLTE.layout.fix();
		});

		var self = this;

		self.init = function () {
			self.reset();
			self.title = "各流程定单完成数";
			self.yName = '数量(个)'
			self.searchTime = 0;
			self.xs = [];
			self.numList = [];
			self.search();
		};

		self.reset = function () {
			self.searchCondtion = {};
		};

		self.search = function () {
			self.loading = true;
			StatisticsService.getProcessOrderNum(self.searchCondtion).then(function(resp) {
				self.loading = false;
				if (resp && resp.code === 0) {
					self.xs = [];
					self.numList = [];
					for(var i=0; i<resp.result.length; i++) {
						self.xs.push(resp.result[i].name);
						self.numList.push(resp.result[i].orderNum);
					}
					self.searchTime = self.searchTime + 1;
				} else {
					ToastService.toast({
						type: 'error',
						body: '查询统计信息失败：' + resp.message
					});
				}
			}).catch(function (err) {
				self.loading = false;
				ToastService.toast({
					type: "error",
					body: err.message
				});
			});
		};

		// 操作时间范围回调
		self.onDatetimeRangeChanged = function ( start, end) {
			self.searchCondtion.startTime = start ? start.toDate().getTime() : start;
			self.searchCondtion.endTime = end ? end.toDate().getTime() : end;
		};

		self.init();
	};

	function patentPatentValueController($scope, $state, ToastSerrvice, StatisticsService) {

		$scope.$on('$viewContentLoaded', function (event) {
			$.AdminLTE.layout.fix();
		});

		var self = this;

		self.init = function () {
			self.reset();
			self.title = "专利价值分布和平均分";
			self.yName = '数量(个)'
			self.searchTime = 0;
			self.xs = [];
			self.numList = [];
			self.search();
		};

		self.reset = function () {
			self.searchCondtion = {};
		};

		self.search = function () {
			self.loading = true;
			if(!self.searchCondtion.patentType) {
				self.searchCondtion.patentType = null;
			}
			StatisticsService.getPatentValue(self.searchCondtion).then(function(resp) {
				self.loading = false;
				if (resp && resp.code === 0) {
					self.xs = [];
					self.numList = [];
					for(var i=0; i<resp.result.ranges.length; i++) {
						self.xs.push(resp.result.ranges[i].name);
						self.numList.push(resp.result.ranges[i].value);
					}
					self.title = "专利价值分布(平均分:" + Math.round((isNaN(resp.result.avg)?0:resp.result.avg) * 10000)/100 + "分)";
					self.searchTime = self.searchTime + 1;
				} else {
					ToastService.toast({
						type: 'error',
						body: '查询统计信息失败：' + resp.message
					});
				}
			}).catch(function (err) {
				self.loading = false;
				ToastService.toast({
					type: "error",
					body: err.message
				});
			});
		};

		// 操作时间范围回调
		self.onDatetimeRangeChanged = function (mode, start, end) {
			if(mode == 'A') {
				self.searchCondtion.fromADate = start ? start.toDate().getTime() : start;
				self.searchCondtion.toADate = end ? end.toDate().getTime() : end;
			}
			if(mode == 'O') {
				self.searchCondtion.fromODate = start ? start.toDate().getTime() : start;
				self.searchCondtion.toODate = end ? end.toDate().getTime() : end;
			}
		};

		self.init();
	};

})(angular);