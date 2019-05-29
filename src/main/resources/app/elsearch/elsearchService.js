/**
 * Created by xiongwei 2018/2/8 下午2:13.
 */

(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('ElSearch')
    .service('ElPatentService', ['HttpService', ElPatentService])
    .service('ElPatentAction', ['$uibModal', ElPatentAction])
  ;

  function ElPatentService(HttpService) {
    this.search = function (keywords, pageNum, pageSize) {
      var params = {
        keywords: keywords || '',
        pageNum: pageNum || 1,
        pageSize: pageSize || 50,
      };
      return HttpService.get(CONTEXT_PATH + '/api/elsearch/patent', params);
    };

  }
  
  function ElPatentAction($uibModal) {
    
  }


})(angular, CONTEXT_PATH);
