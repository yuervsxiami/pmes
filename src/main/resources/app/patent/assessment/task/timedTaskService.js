/**
 * Created by wangzhibin on 2018/3/13.
 */

(function (angular, CONTEXT_PATH) {

	'use strict';
	angular.module('Patent')
			.service('TimedTaskService', ['HttpService', TimedTaskService])
	;

	function TimedTaskService(HttpService) {

		this.search = function (searchCondition) {
			return HttpService.get(CONTEXT_PATH + '/api/patent/assessment/task/search', searchCondition);
		};

		this.searchDetail = function (searchCondition) {
			return HttpService.get(CONTEXT_PATH + '/api/patent/assessment/task/detail/search', searchCondition);
		};

		this.getIndexError = function (searchCondition) {
			return HttpService.get(CONTEXT_PATH + '/api/patent/assessment/humen/indexError/', searchCondition);
		}
	}

})(angular, CONTEXT_PATH);

