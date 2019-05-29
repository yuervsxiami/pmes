/**
 * Created by xiongwei 2018/4/8 下午3:04.
 */

(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('Patent')
    .controller('PatentModifyController', ['$scope', '$stateParams', '$uibModal', 'ToastService', 'PatentModifyService', 'PatentModifyAction', PatentModifyController])
    .controller('PatentLabelModifyController', ['$scope', '$uibModalInstance', 'label', PatentLabelModifyController])
    .controller('LabelChangeController', ['$scope', '$uibModalInstance', 'patentId', 'labelId', 'PatentModifyService', LabelChangeController])
  ;

  function PatentModifyController($scope, $stateParams, $uibModal, ToastService, PatentModifyService, PatentModifyAction) {
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });
    var self = this;
    self.loading = false;
    self.patentId = $stateParams.id;

    self.findDetail = function () {
      self.loading = true;
      PatentModifyService.detail(self.patentId).then(function (resp) {
        if (resp.code === 0) {
          self.patent = resp.result;
        } else {
          ToastService.toast({
            type: 'error',
            body: resp.message
          });
        }
      }).catch(function (err) {
        ToastService.toast({
          type: 'error',
          body: err.message
        });
      }).finally(function () {
        self.loading = false;
      });
    };
    
    self.modifyLabel = function (label, index) {
      self.loading = true;
      var tmp = angular.copy(label);
      $uibModal.open({
        animation: true,
        templateUrl: CONTEXT_PATH + '/static/tpls/patent/modify/edit.label.html',
        controller: 'PatentLabelModifyController',
        size: 'lg',
        resolve: {
          label: tmp
        }
      }).result.then(function (label) {
        return PatentModifyService.modifyLabel(self.patent.id, label);
      }).then(function (resp) {
        if (resp.code === 0) {
					self.patent.instanceLabels[index] = resp.result;
					self.findDetail();
          ToastService.toast({
            type: 'success',
            body: '修改标签值成功！'
          });
        } else {
          ToastService.toast({
            type: 'error',
            body: resp.message
          });
        }
      }).catch(function (err) {
        if (typeof err !== 'string') {
          ToastService.toast({
            type: 'error',
            body: err.message
          });
        }
      }).finally(function () {
        self.loading = false;
      });
    };

    self.showHistory = function (labelId) {
			PatentModifyAction.viewLabelChangeDetail(self.patentId,labelId).then(function (resp) {
			}).catch(function (err) {
				if (typeof err !== 'string') {
					ToastService.toast({
						type: 'error',
						body: err.message
					});
				}
			}).finally(function () {
				self.loading = false;
			});
    };

    // init
    self.findDetail();

  }
  
  function PatentLabelModifyController($scope, $uibModalInstance, label) {
    $scope.label = label;

    $scope.ok = function () {
      if (!$scope.label.strValue && !$scope.label.textValue) {
        return;
      }
      $uibModalInstance.close($scope.label);
    };
    $scope.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };
  }

  function LabelChangeController($scope, $uibModalInstance, patentId, labelId, PatentModifyService) {

    $scope.loadHistory = function () {
			PatentModifyService.modifyHistory(patentId,labelId).then(function (resp) {
			  if(resp && resp.result) {
			    $scope.labels = resp.result;
        }
			}).catch(function (err) {
				if (typeof err !== 'string') {
					ToastService.toast({
						type: 'error',
						body: err.message
					});
				}
			});
    };

    $scope.ok = function () {
      $uibModalInstance.close(0);
    };
    $scope.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };

		$scope.loadHistory();
  }

})(angular, CONTEXT_PATH);
