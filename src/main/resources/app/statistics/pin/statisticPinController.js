/**
 * Created by Crixalis on 2017/9/21.
 */

(function (angular) {
  'use strict';
  angular.module('Statistics')
      .controller('PinStatCtrl', ['$scope', '$state', 'ToastService', 'ChartService', 'DEFAULT_PROVINCE_COLLEGE_PIN', PinStatCtrl])
			.controller('PinPmesValuePatentCtrl', ['$scope', '$state', 'ToastService', 'ChartService', 'PatentService', PinPmesValuePatentCtrl])
    ;

  function PinStatCtrl($scope, $state, ToastService, ChartService, DEFAULT_PROVINCE_COLLEGE_PIN) {

		$scope.$on('$viewContentLoaded', function (event) {
			$.AdminLTE.layout.fix();
		});

		var self = this;

		self.init = function () {
			self.isFirstInit = true;
			self.isFirstInitCollege = true;
			// 初始化的省份名称
			self.initParam = DEFAULT_PROVINCE_COLLEGE_PIN;
			self.initParam.provinceName = $state.params.provinceName || self.initParam.provinceName;
			self.initParam.collegeName = $state.params.collegeName || self.initParam.collegeName;
			self.initParam.pinName = $state.params.pinName || self.initParam.pinName;
			self.reset();
			self.getProvinces();

			// $scope.$watch(function () {
			// 	return window.innerHeight;
			// },function() {
			// 	$('.statDiv').css('height', window.innerHeight - 235);
			// });

		};

		self.clearPinName = function () {
			self.searchCondition.pinName = "";
		};

		self.reset = function () {
			self.searchCondition = {};
		};

		/**
		 * 获取所有省份
		 */
		self.getProvinces = function () {
			ChartService.getProvinces().then(function(resp) {
				if (resp && resp.code === 0) {
					self.provinces = resp.result;
					if(self.initParam.provinceName && self.isFirstInit) {
						self.isFirstInit = false;
						self.searchCondition.provinceName = self.initParam.provinceName;
					} else {
						self.searchCondition.provinceName = resp.result[0]._id;
					}
					$scope.$watch(function() {
						return self.searchCondition.provinceName;
					}, self.getColleges);
				} else {
					ToastService.toast({
						type: 'error',
						body: '查询省份列表：' + resp.message
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

		/**
		 * 获取所有指定省下的高校
		 */
		self.getColleges = function() {
			if(!self.searchCondition.provinceName) {
				return;
			}
			ChartService.getColleges(self.searchCondition.provinceName).then(function(resp) {
				if (resp && resp.code === 0) {
					self.colleges = resp.result;
					if(self.initParam.collegeName && self.isFirstInitCollege) {
						self.isFirstInitCollege = false;
						self.searchCondition.collegeName = self.initParam.collegeName;
						self.searchCondition.pinName = self.initParam.pinName;
						self.search();
					} else {
						self.searchCondition.collegeName = self.colleges[0].name;
						self.searchCondition.pinName = "";
					}
				} else {
					ToastService.toast({
						type: 'error',
						body: '查询高校列表：' + resp.message
					});
				}
			}).catch(function (err) {
				ToastService.toast({
					type: "error",
					body: err.message
				});
			});
		};

		self.search = function () {
			if(!self.searchCondition.pinName) {
				ToastService.toast({
					type: 'info',
					body: '请先输入专家名称再搜索'
				});
				return;
			}
			self.collegeName = self.searchCondition.collegeName;
			self.pinName = self.searchCondition.pinName;
		};

		self.init();

  };

	function PinPmesValuePatentCtrl($scope, $state, ToastService, ChartService, PatentService) {

		$scope.$on('$viewContentLoaded', function (event) {
			$.AdminLTE.layout.fix();
		});

		var self = this;

		self.init = function () {
			self.provinceName = $state.params.provinceName;
			self.collegeName = $state.params.collegeName;
			self.startValue = $state.params.startValue;
			self.endValue = $state.params.endValue;
			self.pin = $state.params.pin;
			self.reset();
			self.searchCondition.pin = self.pin;
			self.cache.startValue = self.startValue;
			self.cache.endValue = self.endValue;
			self.getProvinces();
		};

		self.reset = function () {
			self.searchCondition = {};
			self.cache = {};
			self.paginationConf = {
				currentPage: 1,
				itemsPerPage: 10
			};
			if(!self.loading) {
				self.getPatents();
			}
			$scope.$watch(function () {
				return self.paginationConf.itemsPerPage + " " + self.paginationConf.currentPage;
			}, self.getPatents);
		};

		/**
		 * 获取所有省份
		 */
		self.getProvinces = function () {
			ChartService.getProvinces().then(function(resp) {
				if (resp && resp.code === 0) {
					self.provinces = resp.result;
					if(self.provinceName && !self.hasFirstInit) {
						self.hasFirstInit = true;
						self.searchCondition.provinceName = self.provinceName;
					} else {
						self.searchCondition.provinceName = resp.result[0]._id;
					}
					$scope.$watch(function() {
						return self.searchCondition.provinceName;
					}, self.getColleges);
				} else {
					ToastService.toast({
						type: 'error',
						body: '查询省份列表：' + resp.message
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

		/**
		 * 获取所有指定省下的高校
		 */
		self.getColleges = function() {
			if(!self.searchCondition.provinceName) {
				return;
			}
			ChartService.getColleges(self.searchCondition.provinceName).then(function(resp) {
				if (resp && resp.code === 0) {
					self.colleges = resp.result;
					if(self.collegeName && !self.hasFirstInitCollege) {
						self.hasFirstInitCollege = true;
						self.searchCondition.collegeName = self.collegeName;
						self.getPatents();
					} else {
						self.searchCondition.collegeName = self.colleges[0].name;
					}
				} else {
					ToastService.toast({
						type: 'error',
						body: '查询高校列表：' + resp.message
					});
				}
			}).catch(function (err) {
				ToastService.toast({
					type: "error",
					body: err.message
				});
			});
		};

		/**
		 * 获取所有指定高校下的专利
		 */
		self.getPatents = function () {
			var flag = true;
			if(!self.searchCondition.collegeName || self.loading) {
				return;
			}
			if(!self.cache.startValue) {
				ToastService.toast({
					type: 'info',
					body: '请输入开始分数'
				});
				flag = false;
			}
			if(!self.cache.endValue) {
				ToastService.toast({
					type: 'info',
					body: '请输入截止分数'
				});
				flag = false;
			}
			if (!flag) {
				return;
			}
			self.searchCondition.pageSize = self.paginationConf.itemsPerPage;
			self.searchCondition.pageNum = self.paginationConf.currentPage;
			self.searchCondition.startValue = self.cache.startValue/100;
			self.searchCondition.endValue = self.cache.endValue/100;
			self.loading = true;
			ChartService.getProvincePatent(self.searchCondition).then(function(resp) {
				self.loading = false;
				if (resp && resp.code === 0) {
					self.patents = resp.result.data;
					self.paginationConf.totalItems = resp.result.count;
				} else {
					ToastService.toast({
						type: 'error',
						body: '查询专利列表：' + resp.message
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

		self.back = function () {
			$state.go("main.console.statistics.college", {provinceName: self.provinceName, collegeName: self.collegeName});
		};

		self.goDetail = function (patent) {
			PatentService.findByAn(patent.an).then(function(resp) {
				if (resp && resp.code === 0) {
					var p = resp.result;
					$state.go("main.console.search.patent.detail", {id: p.id});
				} else {
					ToastService.toast({
						type: 'error',
						body: '查询专利失败：' + resp.message
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

		self.init();

	};

})(angular);