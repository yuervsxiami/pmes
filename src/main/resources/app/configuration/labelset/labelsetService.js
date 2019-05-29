/**
 * Created by xiongwei 2018/1/13 下午3:42.
 */


(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('Configuration')
    .service('LabelsetService', ['HttpService', LabelsetService])
    .service('LabelsetAction', ['LabelsetService', '$uibModal', LabelsetAction])
  ;

  function LabelsetService(HttpService) {
    this.search = function(searchCondition) {
      return HttpService.get(CONTEXT_PATH + '/api/labelset/search', searchCondition);
    };
    this.findByType = function(type) {
			return HttpService.get(CONTEXT_PATH + '/api/labelset/type/' + type);
		};
    this.add = function (labelset) {
      return HttpService.put(CONTEXT_PATH + '/api/labelset/', labelset);
    };
    this.update = function (labelset) {
      return HttpService.post(CONTEXT_PATH + '/api/labelset/', labelset);
    };
    this.toggleState = function (labelset) {
      return HttpService.post(CONTEXT_PATH + '/api/labelset/state', labelset);
    };
    this.getLabelsetLabels = function (labelsetId) {
      return HttpService.get(CONTEXT_PATH + '/api/labelset/labels/'+labelsetId);
    }
    this.edit = function (labelset) {
			return HttpService.post(CONTEXT_PATH + '/api/labelset/edit/', labelset);
		};
  }
  
  function LabelsetAction(LabelsetService, $uibModal) {

		// 修改
		this.updateLabelset = function (labelset) {
			var alertModalInstance = $uibModal.open({
				animation: true,
				templateUrl: CONTEXT_PATH + '/static/tpls/configuration/labelset/labelsetEditer.html',
				controller: 'LabelsetEditerController',
				controllerAs: 'labelsetEditer',
				size: 'lg',
				resolve: {
					labelset: labelset
				}
			});
			return alertModalInstance.result;
		}

    this.addLabelset = function () {
      var instance = $uibModal.open({
        animation: true,
        templateUrl: CONTEXT_PATH + '/static/tpls/configuration/labelset/addLabelset.html',
        controller: 'AddLabelsetController',
        controllerAs: 'addLabelset',
        size: 'lg'
      });
      return instance.result;
    };

    this.addLabelsetLabels = function (labelset) {
      var instance = $uibModal.open({
        animation: true,
        templateUrl: CONTEXT_PATH + '/static/tpls/configuration/labelset/addLabelsetLabel.html',
        controller: 'LabelsetLabelsController',
        controllerAs: 'labelsetLabel',
        size: 'lg',
        resolve: {
          labelset: function () {
            return angular.copy(labelset);
          }
        }
      });
      return instance.result;
    };

    this.addLabelsetRelation = function (labelset) {
      var instance = $uibModal.open({
        animation: true,
        templateUrl: CONTEXT_PATH + '/static/tpls/configuration/labelset/addLabelsetLabelRelation.html',
        controller: 'LabelsetRelationController',
        controllerAs: 'labelsetRelation',
        size: 'lg',
        resolve: {
          labelset: function () {
            return angular.copy(labelset);
          }
        }
      });
      return instance.result;
    };
  }
  
})(angular, CONTEXT_PATH);