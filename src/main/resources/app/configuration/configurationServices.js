/**
 * Created by Ray丶X on 2017/6/2.
 */
(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('Configuration')
    .service('MetaService', ['HttpService', MetaService])
    .service('LabelService', ['HttpService', LabelService])
    ;

  function MetaService(HttpService) {
	  this.getMeta = function(key) {
		  return HttpService.get(CONTEXT_PATH + '/api/meta/get/'+ key, {});
	  };
	  this.getMetaByPrimaryKey = function(id) {
		  return HttpService.get(CONTEXT_PATH + '/api/meta/'+id, {});
	  };
	  this.searchMetas = function(searchCondition) {
		  return HttpService.get(CONTEXT_PATH + '/api/meta/search/', searchCondition);
	  };
	  this.getAllMetas = function(searchCondition) {
		  return HttpService.get(CONTEXT_PATH + '/api/meta/all/', searchCondition);
	  };
	  this.getMetaValues = function(key) {
		  return HttpService.get(CONTEXT_PATH + '/api/meta/values/'+ key, {});
	  };
	  this.addMeta = function (meta) {
		  return HttpService.post(CONTEXT_PATH + '/api/meta/add/', meta);
	  };
	  this.updateMeta = function (meta) {
		  return HttpService.post(CONTEXT_PATH + '/api/meta/update/', meta);
	  };
	  this.changeMetaState = function (meta) {
		  return HttpService.post(CONTEXT_PATH + '/api/meta/changeState/', meta);
	  };
	  this.deleteMetaValue = function (id) {
		  return HttpService.delete(CONTEXT_PATH + '/api/meta/value/delete/'+ id, {});
	  };
  }
  
  function LabelService(HttpService) {
	  this.get = function(key) {
		  return HttpService.get(CONTEXT_PATH + '/api/label/get/'+ key, {});
	  };
	  this.getByPrimaryKey = function(id) {
		  return HttpService.get(CONTEXT_PATH + '/api/label/'+id, {});
	  };
	  this.search = function(searchCondition) {
		  return HttpService.get(CONTEXT_PATH + '/api/label/search/', searchCondition);
	  };
	  this.getByType = function(type, searchCondition) {
		  return HttpService.get(CONTEXT_PATH + '/api/label/'+type, searchCondition);
	  };
	  this.add = function (label) {
		  return HttpService.post(CONTEXT_PATH + '/api/label/add/', label);
	  };
	  this.update = function (label) {
		  return HttpService.post(CONTEXT_PATH + '/api/label/update/', label);
	  };
	  this.changeState = function (label) {
		  return HttpService.post(CONTEXT_PATH + '/api/label/changeState/', label);
	  };
  }


})(angular, CONTEXT_PATH);