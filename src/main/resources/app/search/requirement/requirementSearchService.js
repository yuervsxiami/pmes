/**
 * Created by xiongwei 2018/2/4 上午10:27.
 */

(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('Search')
    .service('RequirementSearchService', ['HttpService', RequirementSearchService])
  ;

  function RequirementSearchService(HttpService) {
    /**
     * 搜索所有流程已完成的企业需求
     * @param searchCondition
     */
    this.searchCompleteRequirements = function (searchCondition) {
      return HttpService.get(CONTEXT_PATH + '/api/requirement/', searchCondition);
    };

    this.detail = function (id) {
      return HttpService.get(CONTEXT_PATH + '/api/requirement/' + id, {});
    };

    this.labels = function (labelId) {
      return HttpService.get(CONTEXT_PATH + '/api/search/patent/' + labelId + '/7/labels/latest');
    };
  }

})(angular, CONTEXT_PATH);
