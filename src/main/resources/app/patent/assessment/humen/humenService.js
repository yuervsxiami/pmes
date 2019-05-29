/**
 * Created by wangzhibin on 2018/1/29.
 */

(function (angular, CONTEXT_PATH) {

  'use strict';
  angular.module('Patent')
    .service('HumenService', ['HttpService', HumenService])
  ;

  function HumenService(HttpService) {
    /**
     * 专利价值快速评估结果查询
     * @param searchCondition
     */
    this.search = function(mode, searchCondition) {
      return HttpService.get(CONTEXT_PATH + '/api/patent/assessment/humen/search/'+mode, searchCondition);
    };
    /**
     * 专利价值快速评估结果导出
     * @param searchCondition
     */
    this.exportExcel = function(obj) {
      return HttpService.post(CONTEXT_PATH + '/api/patent/assessment/humen/exportExcel',obj);
    };
    /**
     * 专利价值快速评估详情
     * @param id
     */
    this.detail = function(id) {
      return HttpService.get(CONTEXT_PATH + '/api/patent/assessment/humen/detail/' + id, {_random: Math.random()});
    };

    /**
     * 开启一条快速流程
     * @param id
     */
    this.start = function(id) {
        return HttpService.get(CONTEXT_PATH + '/api/patent/assessment/humen/start/' + id, {_random: Math.random()});
    };
  }

})(angular, CONTEXT_PATH);

