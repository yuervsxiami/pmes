/**
 * Created by crixalis on 2018/3/22.
 */
(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('Expert')
    .service('ExpertService', ['HttpService', ExpertService])
    .service('ExpertLibraryService', ['HttpService', ExpertLibraryService])
  ;

  function ExpertService(HttpService) {
    /**
     * 通用的专利搜索接口
     * @param patentSearchCondition
     */
    this.search = function (searchCondition) {
      return HttpService.get(CONTEXT_PATH + '/api/expert/search/', searchCondition);
    };

    this.getAllIPC = function () {
        return HttpService.get(CONTEXT_PATH + '/api/expert/ipc/all/');
    };

    this.add = function (expert) {
        return HttpService.post(CONTEXT_PATH + '/api/expert/add/', expert);
    };

    this.update = function (expert) {
        return HttpService.post(CONTEXT_PATH + '/api/expert/update/', expert);
    };

    this.get = function (id) {
        return HttpService.get(CONTEXT_PATH + '/api/expert/' + id);
    };

  }

  function ExpertLibraryService(HttpService) {
    /**
     * 查询省份高校
     * @param
     */
    this.getAllProvince = function () {
      return HttpService.get(CONTEXT_PATH + '/api/specialist/colleges');
    };
    /**
     * 查询专家职称
     * @param
     */
    this.getAllPost = function () {
      return HttpService.get(CONTEXT_PATH + '/api/specialist/titles');
    };
    /**
     * 查询ipc分类
     * @param
     */
    this.getAllIpc = function () {
      return HttpService.get(CONTEXT_PATH + '/api/result/ipc');
    };
    /**
     * 查询国民经济分类
     * @param
     */
    this.getAllNational = function () {
      return HttpService.get(CONTEXT_PATH + '/api/result/nic');
    };
    /**
     * 查询八大产业分类
     * @param
     */
    this.getAllIndustry = function () {
      return HttpService.get(CONTEXT_PATH + '/api/result/ntcc');
    };
  }
})(angular, CONTEXT_PATH);