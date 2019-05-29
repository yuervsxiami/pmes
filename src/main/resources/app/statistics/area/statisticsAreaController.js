/**
 * Created by Crixalis on 2017/9/21.
 */

(function (angular) {
  'use strict';
  angular.module('Statistics')
      .controller('AreaStatCtrl', ['$scope', '$state', 'ToastService', 'ChartService', AreaStatCtrl])
      .controller('CollegeProvincePatentCtrl', ['$scope', '$state', 'ToastService', 'ChartService', 'PatentService', CollegeProvincePatentCtrl])
    ;

  function AreaStatCtrl($scope, $state, ToastSerrvice, ChartService) {

		$scope.$on('$viewContentLoaded', function (event) {
			$.AdminLTE.layout.fix();
		});

		var self = this;

		self.init = function () {
			self.searchTime = 0;
			self.mapName = '全国省市高校专利大数据';

			self.visualMap =  {
				min: 0,
				max: 250000,
				type: 'continuous', // 定义为连续型 viusalMap
				inRange: {
					color: ['lightskyblue','yellow', 'orangered'],
					symbolSize: [30, 200]
				},
				text: ['250000', '0'],
			};

			self.getNationalNumber();
		};

		self.clickMap = function (params) {
			$state.go("main.console.statistics.area.patent", {provinceName: params._id});
		};

		self.getNationalNumber = function () {
			self.loading = true;
			ChartService.getNationalNumber().then(function(resp) {
				self.loading = false;
				if (resp && resp.code === 0) {
					resp.result.forEach(function(p) {
						p.name = self.changeIdToName(p._id);
						p.value = p.patentCount;
					});
					self.data = resp.result;
					self.searchTime++;
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

		self.changeIdToName = function(id) {
			if (id==='黑龙江省') {
				return '黑龙江';
			}
			if (id==='内蒙古自治区') {
				return '内蒙古';
			}
			return id.substring(0,2);
		};

		self.init();

  };

  function CollegeProvincePatentCtrl($scope, $state, ToastSerrvice, ChartService, PatentService) {

		$scope.$on('$viewContentLoaded', function (event) {
			$.AdminLTE.layout.fix();
		});

		var self = this;

		self.init = function () {
			self.provinceName = $state.params.provinceName;
			self.reset();
			self.getProvinces();
		};

		// 初始化/重置第一个列表的筛选条件
		self.reset1 = function () {
			self.searchCondition1 = self.searchCondition1 || {};
			self.paginationConf1 = {
				currentPage: 1,
				itemsPerPage: 10
			};
			if(!self.loading1) {
				self.getColleges();
			}
		};

		// 初始化/重置第二个列表的筛选条件
		self.reset2 = function (collegeName) {
			self.searchCondition2 = {patType: "", lastLegalStatus: ""};
			self.searchCondition2.collegeName = collegeName;
			self.paginationConf2 = {
				currentPage: 1,
				itemsPerPage: 10
			};
			if(!self.loading2) {
				self.getPatents();
			}
		};

		self.reset = function () {
			self.reset1();
			$scope.$watch(function () {
				return self.paginationConf1.itemsPerPage + " " + self.paginationConf1.currentPage;
			}, self.getColleges);
			self.reset2();
			$scope.$watch(function () {
				return self.paginationConf2.itemsPerPage + " " + self.paginationConf2.currentPage;
			}, self.getPatents);
		};

		/**
		 * 获取所有省份
		 */
		self.getProvinces = function () {
			ChartService.getProvinces().then(function(resp) {
				if (resp && resp.code === 0) {
					self.provinces = resp.result;
					if(self.provinceName && !self.isFirstInit) {
						self.isFirstInit = false;
						self.searchCondition1.provinceName = self.provinceName;
					} else {
						self.searchCondition1.provinceName = resp.result[0]._id;
					}
					if(!self.colleges || self.colleges.length === 0) {
						self.getColleges();
					}
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
			if(!self.searchCondition1.provinceName ||self.loading1) {
				return;
			}
			self.searchCondition1.pageSize = self.paginationConf1.itemsPerPage;
			self.searchCondition1.pageNum = self.paginationConf1.currentPage;
			self.loading1 = true;
			ChartService.getCollegePatent(self.searchCondition1).then(function(resp) {
				self.loading1 = false;
				if (resp && resp.code === 0) {
					self.colleges = resp.result.data;
					self.cs = self.colleges.map(function (college) {
						return {name : college._id};
					});
					self.paginationConf1.totalItems = resp.result.count;
					if(self.colleges && self.colleges.length>0) {
						self.drill(self.colleges[0]);
					}
				} else {
					ToastService.toast({
						type: 'error',
						body: '查询高校列表：' + resp.message
					});
				}
			}).catch(function (err) {
				self.loading1 = false;
				ToastService.toast({
					type: "error",
					body: err.message
				});
			});
		};

		/**
		 * 钻取高校下专利
		 */
		self.drill = function (college) {
			self.reset2(college._id);
		};

		/**
		 * 获取所有指定高校下的专利
		 */
		self.getPatents = function () {
			if(!self.searchCondition2.collegeName || self.loading2) {
				return;
			}
			self.searchCondition2.pageSize = self.paginationConf2.itemsPerPage;
			self.searchCondition2.pageNum = self.paginationConf2.currentPage;
			self.loading2 = true;
			ChartService.getProvincePatent(self.searchCondition2).then(function(resp) {
				self.loading2 = false;
				if (resp && resp.code === 0) {
					self.patents = resp.result.data;
					self.paginationConf2.totalItems = resp.result.count;
				} else {
					ToastService.toast({
						type: 'error',
						body: '查询专利列表：' + resp.message
					});
				}
			}).catch(function (err) {
				self.loading2 = false;
				ToastService.toast({
					type: "error",
					body: err.message
				});
			});
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