/**
 * Created by xiongwei 2018/2/4 上午10:27.
 */

(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('Search')
    .service('EnterpriseSearchService', ['HttpService', EnterpriseSearchService])
  ;

  function EnterpriseSearchService(HttpService) {
    /**
     * 搜索所有流程已完成的企业信息
     * @param searchCondition
     */
    this.searchCompleteEnterprises = function (searchCondition) {
      return HttpService.get(CONTEXT_PATH + '/api/enterprise/', searchCondition);
    };

    this.detail = function (id) {
      return HttpService.get(CONTEXT_PATH + '/api/enterprise/' + id, {});
    };

    this.labels = function (labelId) {
      return HttpService.get(CONTEXT_PATH + '/api/search/patent/' + labelId + '/6/labels/latest');
    };
  }

})(angular, CONTEXT_PATH);
