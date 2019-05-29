/**
 * Created by wangzhibin on 2018/1/31.
 */

(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('Match')
    .service('MatchService', ['HttpService', '$uibModal', MatchService])
  ;

  function MatchService(HttpService, $uibModal) {
  	// 搜索企业需求
		this.searchRmp = function (searchCondition) {
			return HttpService.get(CONTEXT_PATH + '/api/enterpriseRequire/', filterEmptyProperty(searchCondition));
		};
		// 企业需求详情
		this.reqDetail = function (erid) {
			return HttpService.get(CONTEXT_PATH + '/api/enterpriseRequire/' + erid, {_random: Math.random()});
		};
		// 企业需求关键词
		this.contentKeyword = function (text, topn) {
			return HttpService.get(CONTEXT_PATH + '/api/keyword/content/keyword', {text: text, topn:topn? topn: 10});
		};
		// 企业需求匹配
		this.match = function (expression, pageSize, pageNum) {
			if(pageNum<0) {
				pageNum = 0
			}
			return HttpService.get(CONTEXT_PATH + '/api/keyword/match', {expression: expression, pageSize:pageSize? pageSize: 10, pageNum:pageNum? pageNum:0});
		};
		// 需求相似词
		this.correlation = function (keywords, topn) {
			return HttpService.get(CONTEXT_PATH + '/api/keyword/correlation', {keywords: keywords, topn:topn? topn: 10});
		};
		// 存储匹配结果
		this.saveProfessors = function (professors) {
			return HttpService.post(CONTEXT_PATH + '/api/keyword/professor', professors);
		};
		// 查看匹配结果
		this.getResult = function (id, pageSize, pageNum) {
			return HttpService.get(CONTEXT_PATH + '/api/keyword/' + id, {pageSize: pageSize, pageNum: pageNum});
		};

		// 查看相似词
		this.openCorrelationViewer = function (config) {
			var viewerModalInstance = $uibModal.open({
				animation: true,
				templateUrl: CONTEXT_PATH + '/static/tpls/match/professor/correlationViewer.html',
				controller: 'CorrelationViewerCtrl',
				controllerAs: 'cCtrl',
				size: 'lg',
				resolve: {
					config: function () {
						return angular.merge({
							title: '需求相似词',
						}, config || {});
					}
				}
			});
			return viewerModalInstance.result;
		};

		// 提取专家关键词
		this.extardProfessorKeyword = function (condition) {
			return HttpService.get(CONTEXT_PATH + '/api/keyword/professor/keyword', filterEmptyProperty(condition));
		};

		// 索引专家
		this.saveProfessorKeyWords = function (professor) {
			return HttpService.post(CONTEXT_PATH + '/api/enterpriseRequire/saveProfessorKeyWords', filterEmptyProperty(professor));
		};

  }

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