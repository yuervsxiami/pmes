/**
 * Created by xiongwei 2018/2/6 上午10:57.
 */


(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('Enterprise')
    .service('EnterpriseService', ['HttpService', EnterpriseService])
    .service('EnterpriseRequirementService', ['HttpService', EnterpriseRequirementService])
    .service('EnterpriseAction', ['$uibModal', EnterpriseAction])
    .service('EnterpriseRequirementAction', ['$uibModal', EnterpriseRequirementAction])
  ;

  function EnterpriseService(HttpService) {
    this.search = function (enterprise) {
      return HttpService.get(CONTEXT_PATH + '/api/enterprise/', enterprise);
    };

    // 自动完成的接口
    this.autoComplete = function (enterprise) {
      return HttpService.get(CONTEXT_PATH + '/api/enterprise/auto/complete', enterprise);
    };
    // 国民经济领域联想接口
    this.nationalEconomyFields = function (keyword) {
      return HttpService.get(CONTEXT_PATH + '/api/nationaleconomy/bottoms', {keyword: keyword});
    };

    this.detail = function (eid) {
      return HttpService.get(CONTEXT_PATH + '/api/enterprise/' + eid);
    };

    this.save = function (enterprise) {
      return HttpService.put(CONTEXT_PATH + '/api/enterprise/', enterprise);
    };

    this.update = function (enterprise) {
      return HttpService.post(CONTEXT_PATH + '/api/enterprise/', enterprise);
    };

    this.delete = function (enterprise) {
      return HttpService.delete(CONTEXT_PATH + '/api/enterprise/', enterprise);
    };

    this.startProcess = function(processType, enterprises) {
      return HttpService.post(CONTEXT_PATH + '/api/enterprise/start/process/'+processType, enterprises);
    }
  }
  
  function EnterpriseAction($uibModal) {
    /**
     * 编辑或新增企业
     * @param enterprise
     */
    this.addOrEditEnterprise = function (enterprise) {
      var modalInstance = $uibModal.open({
        animation: true,
        templateUrl: CONTEXT_PATH + '/static/tpls/enterprise/edit.html',
        controller: 'EnterpriseEditCtrl',
        controllerAs: 'eec',
        size: 'lg',
        resolve: {
          enterprise: enterprise
        }
      });
      return modalInstance.result;
    }
  }

  function EnterpriseRequirementService(HttpService) {
    this.search = function (requirement) {
      return HttpService.get(CONTEXT_PATH + '/api/requirement/', requirement);
    };

    this.detail = function (erid) {
      return HttpService.get(CONTEXT_PATH + '/api/requirement/' + erid);
    };

    this.save = function (requirement) {
      return HttpService.put(CONTEXT_PATH + '/api/requirement/', requirement);
    };

    this.update = function (requirement) {
      return HttpService.post(CONTEXT_PATH + '/api/requirement/', requirement);
    };

    this.delete = function (requirement) {
      return HttpService.delete(CONTEXT_PATH + '/api/requirement/', requirement);
    };

    this.startProcess = function(instanceType, processType, requirements) {
      return HttpService.post(CONTEXT_PATH + '/api/requirement/start/process/'+instanceType+'/'+processType, requirements);
    }
  }
  
  function EnterpriseRequirementAction($uibModal) {
    /**
     * 编辑或新增企业需求
     * @param enterprise
     */
    this.addOrEditRequirement = function (requirement) {
      var modalInstance = $uibModal.open({
        animation: true,
        templateUrl: CONTEXT_PATH + '/static/tpls/enterprise/edit.require.html',
        controller: 'RequirementEditCtrl',
        controllerAs: 'rec',
        size: 'lg',
        resolve: {
          requirement: requirement
        }
      });
      return modalInstance.result;
    }
  }

})(angular, CONTEXT_PATH);
