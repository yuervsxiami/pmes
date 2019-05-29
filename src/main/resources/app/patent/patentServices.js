/**
 * Created by wangzhibin on 2018/1/23.
 */
(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('Patent')
    .service('PatentService', ['HttpService', PatentService])
  ;

  function PatentService(HttpService) {
    /**
     * 通用的专利搜索接口
     * @param patentSearchCondition
     */
    this.search = function (patentSearchCondition) {
      return HttpService.get(CONTEXT_PATH + '/api/patent/search', patentSearchCondition);
    };

    this.findByAn = function (an) {
			return HttpService.get(CONTEXT_PATH + '/api/patent/', {an: an});
    }
  }

})(angular, CONTEXT_PATH);