/**
 * Created by wangzhibin on 2018/1/23.
 */

(function (angular, CONTEXT_PATH) {

  'use strict';
  angular.module('Patent')
    .service('PatentModifyService', ['HttpService', PatentModifyService])
    .service('PatentModifyAction', ['$uibModal', PatentModifyAction])
  ;

  function PatentModifyService(HttpService) {
    this.detail = function(id) {
      return HttpService.get(CONTEXT_PATH + '/api/patent/' + id, {});
    };

    this.modifyLabel = function (id, taskLabel) {
      return HttpService.post(CONTEXT_PATH + '/api/patent/' + id + '/label', taskLabel);
    }

    this.modifyHistory = function (patentId, labelId) {
			return HttpService.get(CONTEXT_PATH + '/api/patent/' + patentId + '/label/history/' + labelId);
    }
  }

	function PatentModifyAction($uibModal) {
		/**
		 * 查看标签变更历史
		 */
		this.viewLabelChangeDetail = function (patentId, labelId) {
			var labelChanegModalInstance = $uibModal.open({
				animation: true,
				templateUrl: CONTEXT_PATH + '/static/tpls/patent/modify/labelChangeHistory.html',
				controller: 'LabelChangeController',
				size: 'lg',
				resolve: {
					patentId: function () {
					  return patentId || '';
          },
          labelId: function () {
						return labelId || '';
					}
				}
			});
			return labelChanegModalInstance.result;
		};
	}

})(angular, CONTEXT_PATH);
