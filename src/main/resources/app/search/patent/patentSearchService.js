/**
 * Created by xiongwei 2018/2/4 上午10:27.
 */

(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('Search')
    .service('PatentSearchService', ['HttpService', PatentSearchService])
  ;

  function PatentSearchService(HttpService) {
    /**
     * 搜索所有流程已完成的专利
     * @param searchCondition
     */
    this.searchCompletePatents = function (searchCondition) {
      return HttpService.get(CONTEXT_PATH + '/api/search/patent/', searchCondition);
    };

    /**
     * 专利详情，不包含定单信息
     * @param patentId
     */
    this.findPatentDetail = function (patentId) {
      return HttpService.get(CONTEXT_PATH + '/api/search/patent/' + patentId);
    };

    /**
     * 专利标签，根据专利id和流程类型查询
     * @param patentId
     */
    this.findPatentLabelsByProcessType = function (patentId, processType) {
      return HttpService.get(CONTEXT_PATH + '/api/search/patent/' + patentId + '/' + processType + '/labels/latest');
    };

		/**
		 * 专利标签，根据专利id和流程类型查询
		 * @param patentId
		 */
		this.findPatentInstanceLabelsByProcessType = function (patentId, processType) {
			return HttpService.get(CONTEXT_PATH + '/api/search/patent/' + patentId + '/' + processType + '/instancelabels/latest');
		};


	}

})(angular, CONTEXT_PATH);
