/**
 * Created by Ray丶X on 2017/6/2.
 */
(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('Statistics')
    .service('StatisticsService', ['HttpService', UserService])
    .service('ChartService', ['HttpService', ChartService])
	;

  function UserService(HttpService) {
    this.getPatentIndexOrderNum = function (searchParam) {
      return HttpService.get(CONTEXT_PATH + '/api/statistics/patentIndexOrderNum/', searchParam);
    };
		this.getProcessOrderNum = function (searchParam) {
      return HttpService.get(CONTEXT_PATH + '/api/statistics/processOrderNum/', searchParam);
    };
		this.getPatentValue = function (searchParam) {
			return HttpService.get(CONTEXT_PATH + '/api/statistics/patent/value', searchParam);
		};
  };

  function ChartService(HttpService) {
  	// 获取全国各省高校数量和专利数量
    this.getNationalNumber = function () {
      return HttpService.get(CONTEXT_PATH + '/api/chart/province/number');
    };
    // 根据高校专利量进行柱状图排名
		this.getCollegePatentSort = function (limit, type ,legState) {
			var searchParam = { limit : 10};
			if(limit) searchParam.limit = limit;
			if(type) searchParam.type = type;
			if(legState) searchParam.legState = legState;
			return HttpService.get(CONTEXT_PATH + '/api/chart/college/patent/sort', searchParam);
		};
		// 省份列表
		this.getProvinces = function () {
			return HttpService.get(CONTEXT_PATH + '/api/chart/provinces');
		};
		// 根据省份获取高校列表
		this.getColleges = function (name) {
			return HttpService.get(CONTEXT_PATH + '/api/chart/colleges', {name: name});
		};
		// 高校名称查询和高校列表
		this.getCollegePatent = function(param) {
			return HttpService.get(CONTEXT_PATH + '/api/chart/college/patent', filterEmptyProperty(param));
		};
		// 2.2右侧下是专利查询条件（专利类型、法律状态、申请号、专利名称、申请人、发明人、申请日、公开日）和专利列表，点专利名称可看专利详情
		this.getProvincePatent = function(param) {
			return HttpService.get(CONTEXT_PATH + '/api/chart/province/patent', filterEmptyProperty(param));
		};
		// 4.1专利类型饼图
		this.getCollegeTypeStat = function(name) {
			return HttpService.get(CONTEXT_PATH + '/api/chart/college/type/stat', {name: name});
		};
		// 4.2法律状态饼图
		this.getCollegeLegStat = function(name) {
			return HttpService.get(CONTEXT_PATH + '/api/chart/college/leg/stat', {name: name});
		};
		// 5高校专利申请趋势
		this.getCollegeStatLastTen = function(name) {
			return HttpService.get(CONTEXT_PATH + '/api/chart/college/stat/lastTen', {name: name});
		};
		// 6高校技术发展趋势分析
		this.getCollegeIpcTrend = function(name) {
			return HttpService.get(CONTEXT_PATH + '/api/chart/college/ipc/trend', {name: name});
		};
		// 7高校技术热点分布
		this.getCollegeIpcHot = function(name, collection) {
			return HttpService.get(CONTEXT_PATH + '/api/chart/college/ipc/hot/' + collection, {name: name});
		};
		// 8高校热门领域的高价值专利分布
		this.getCollegeIpcValue = function(name) {
			return HttpService.get(CONTEXT_PATH + '/api/chart/college/ipc/value' , {name: name});
		};
		// 9高校核心创新专利
		this.getCollegeByQuoteTotal = function(name, limit) {
			return HttpService.get(CONTEXT_PATH + '/api/chart/college/byQuoteTotal' , {name: name, limit: limit});
		};
		// 10高校联合创新主体分析
		this.getCollegePartner = function(name) {
			return HttpService.get(CONTEXT_PATH + '/api/chart/college/partner' , {name: name});
		};
		// 11高校专利价值分布
		this.getCollegePmesValue = function(name) {
			return HttpService.get(CONTEXT_PATH + '/api/chart/college/pmes/value' , {collegeName: name});
		};
		// 12高校发明人
		this.getCollegePin = function(name, limit) {
			return HttpService.get(CONTEXT_PATH + '/api/chart/college/pin' , {name: name, limit: limit});
		};
		// 13发明人产学研技术详情
		this.getPinExpert = function(name, collegeName) {
			return HttpService.get(CONTEXT_PATH + '/api/chart/pin/expert' , {name: name, collegeName: collegeName});
		};
		// 14发明人团队展示
		this.getCollegePinPartner = function(name, collegeName) {
			return HttpService.get(CONTEXT_PATH + '/api/chart/college/pin/partner' , {name: collegeName, pinSplit: name});
		};
		// 15发明人专利价值分布
		this.getPinPmesValue = function(name, collegeName) {
			return HttpService.get(CONTEXT_PATH + '/api/chart/pin/pmes/value' , {name: name, collegeName: collegeName});
		};

  };

	/**
	 * 把对象中空属性删除
	 * @param param
	 * @returns {*}
	 */
  function filterEmptyProperty (param) {
  	var newParam = angular.copy(param);
  	for(var key in newParam) {
  		if(!newParam[key]) {
  			delete newParam[key];
			}
		}
		return newParam;
	}

})(angular, CONTEXT_PATH);