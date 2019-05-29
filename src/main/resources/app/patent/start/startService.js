/**
 * Created by wangzhibin on 2018/1/23.
 */

(function (angular, CONTEXT_PATH) {

  'use strict';
  angular.module('Patent')
    .service('PatentStartService', ['HttpService', PatentStartService])
  ;

  function PatentStartService(HttpService) {
    this.search = function(mode, searchCondition) {
      return HttpService.get(CONTEXT_PATH + '/api/patent/start/search/'+mode, searchCondition);
    };
    this.startProcess = function(processType, patents) {
      return HttpService.post(CONTEXT_PATH + '/api/patent/start/process/'+processType, patents);
    };
  }

})(angular, CONTEXT_PATH);
