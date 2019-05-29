/**
 * Created by xiongwei 2018/2/6 上午11:00.
 */

(function (angular) {
  'use strict';
  angular.module('Enterprise')
    .controller('EnterpriseListCtrl', ['$scope', '$state', 'ToastService', 'ModalService', 'EnterpriseService', 'EnterpriseAction', EnterpriseListCtrl])
    .controller('EnterpriseRequirementListCtrl', ['$scope', 'ToastService', 'ModalService', 'EnterpriseRequirementService', 'EnterpriseRequirementAction', 'INSTANCE_TYPE', 'PATENT_PROCESS_TYPE', EnterpriseRequirementListCtrl])
    .controller('EnterpriseEditCtrl', ['$scope', 'FormService', 'ToastService', 'ModalService', 'EnterpriseService', '$state', '$stateParams', 'isEdit', EnterpriseEditCtrl])
    .controller('RequirementEditCtrl', ['$scope', 'FormService', 'ToastService', 'ModalService', 'EnterpriseService', 'EnterpriseRequirementService', '$uibModalInstance', 'requirement', RequirementEditCtrl])
  ;

  /**
   * EnterpriseService
   * @param $scope
   * @param ToastService
   * @param ModalService
   * @param EnterpriseService
   * @constructor
   */
  function EnterpriseListCtrl($scope, $state, ToastService, ModalService, EnterpriseService, EnterpriseAction) {
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });
    var self = this;
    self.init = function() {
      self.loading = false;
      self.allSelected = false;
      self.enterprises = [];

      //配置分页基本参数
      self.paginationConf = {
        currentPage: 1,
        itemsPerPage: 10
      };
      // 查询条件
      self.reset();

      $scope.$watch(function () {
        return self.paginationConf.itemsPerPage + " " + self.paginationConf.currentPage;
      }, self.search);
    };

    self.reset = function() {
      self.query = {};
      self.nationalEconomyField = null;
      self.currentNationalEconomyField = null;
      self.region = null;
    };

    self.openNationalDlg = function () {
      ModalService.openNationalDlg(self.nationalEconomyField).then(function(data){
        self.nationalEconomyField = data;
        self.currentNationalEconomyField = $scope.getNationalEconomy(data);
      }).catch(function (err) {
        $log.info(err);
      });
    };

    self.openRegionDlg = function () {
      ModalService.openRegionDlg(self.region).then(function (data) {
        self.region = data;
      }).catch(function (err) {
        $log.info(err);
      });
    };

    // 操作时间范围回调
    self.onDatetimeRangeChanged = function (property, start, end) {
      self.query.optDateFrom = start ? start.toDate().getTime() : start;
      self.query.optDateTo = end ? end.toDate().getTime() :end;
    };

    // allselected checkbox change
    self.changeAllSelected = function () {
      self.enterprises.forEach(function (item) {
        item.selected = self.allSelected;
      });
    };

    self.changeSelected = function () {
      // 选中个数
      var num = self.enterprises.filter(function (item) {
        return item.selected
      }).length;
      self.allSelected = num == self.enterprises.length;
    };

    // 查询
    self.search = function () {
      self.loading = true;
      self.query.pageSize = self.paginationConf.itemsPerPage;
      self.query.pageNum = self.paginationConf.currentPage;

      self.query.name = $scope.nullIfSpaceOrEmpty(self.query.name);
      self.query.nationalEconomyField = self.currentNationalEconomyField == null ? null : self.currentNationalEconomyField.code;
      self.query.provinceId = self.region == null ? null : (self.region.t1 == null ? null : self.region.t1.id);
      self.query.cityId = self.region == null ? null : (self.region.t2 == null ? null : self.region.t2.id);
      self.query.districtId = self.region == null ? null : (self.region.t3 == null ? null : self.region.t3.id);

      EnterpriseService.search(self.query).then(function (resp) {
        self.loading = false;
        if (resp.code === 0) {
          self.paginationConf.totalItems = resp.result.total;
          self.enterprises = [];
          resp.result.list.forEach(function (data) {
            self.enterprises.push({selected: false, data: data});
          });
          self.searching = false;
        } else {
          ToastService.toast({
            type: 'error',
            body: '查询企业信息出错：' + resp.message
          });
        }
      }).catch(function (reason) {
        self.loading = false;
        ToastService.errHandler(reason);
      })
    };

    // 启动流程
    self.startProcess = function (processType, enterprise) {
      var enterprises = [];
      // 生成需启动企业信息列表
      if (enterprise == null) {
        self.enterprises.forEach(function (p) {
          if (p.selected) {
            enterprises.push(p.data);
          }
        })
      }
      else {
        enterprises.push(enterprise);
      }

      if (enterprises.length == 0) {
        ToastService.toast({
          type: 'warn',
          body: '请至少选择一个企业信息！'
        });
        return;
      }
      self.request(processType, enterprises);
    }
    self.request=function(processType, enterprises) {
      self.loading = true;
      EnterpriseService.startProcess(processType, enterprises).then(function (resp) {
        if (resp && resp.code === 0) {
          self.search();
          ToastService.toast({
            type: 'success',
            body: resp.result
          });
        }
        else{
          ToastService.toast({
            type: 'info',
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
    }


    // 添加或编辑企业信息
    self.editEnterprise = function (enterprise, index) {
      /**
      EnterpriseAction.addOrEditEnterprise(angular.copy(enterprise)).then(function (enterprise, isEdit) {
        if (isEdit) {
          self.enterprises.splice(index, 1, enterprise);
        } else {
          self.search();
        }
      }).catch(function (reason) {
        //
      });
       */
      if (index < 0) {
        $state.go('main.console.enterprise.add');
      } else {
        $state.go('main.console.enterprise.edit', {id: enterprise.id});
      }
    };

    self.init();

  }

  /**
   * 编辑或新增
   * @param $scope
   * @param ToastService
   * @param ModalService
   * @param EnterpriseService
   * @constructor
   */
  function EnterpriseEditCtrl($scope, FormService, ToastService, ModalService, EnterpriseService, $state, $stateParams, isEdit) {
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });

    var self = this;
    self.loading = false;
    self.isEdit = isEdit;
    angular.merge(this, FormService);

    self.enterprise = isEdit ? {
      id: $stateParams.id
    } : {};

    self.openRegionDlg = function () {
      ModalService.openRegionDlg(self.region).then(function (data) {
        self.region = data;
        if (self.region && self.region.t1) {
          self.enterprise.provinceId = self.region.t1.id;
        } else {
          self.enterprise.provinceId = null;
        }
        if (self.region && self.region.t1) {
          self.enterprise.cityId = self.region.t2.id;
        } else {
          self.enterprise.cityId = null;
        }
        if (self.region && self.region.t3) {
          self.enterprise.districtId = self.region.t3.id;
        } else {
          self.enterprise.districtId = null;
        }
        self.regionChanged = true;
      }).catch(function (err) {
        $log.info(err);
      });
    };

    // 查询国民经济领域
    self.getBottomNationalFields = function (keyword, shouldInit) {
      return EnterpriseService.nationalEconomyFields(keyword).then(function (resp) {
        if (resp && resp.code === 0) {
          if (shouldInit) {
            self.enterprise.nationalEconomyField = resp.result.filter(function (field) {
              return field.code === self.enterprise.nationalEconomyField;
            })[0];
          }
          return resp.result;
        }
        return [];
      });
    };
    // onSubmit
    self.onSubmit = function () {
      var data = angular.copy(self.enterprise);
      if (data.nationalEconomyField && typeof data.nationalEconomyField === 'string') {
        ToastService.toast({
          type: 'warn',
          body: '请输入国民经济领域关键词或代码，并从下拉列表中选择！'
        });
        return;
      }
      if (typeof data.nationalEconomyField !== 'string') {
        data.nationalEconomyField = data.nationalEconomyField.code;
      }
      if (self.isEdit) {
        EnterpriseService.update(data).then(function (resp) {
          if (resp && resp.code === 0) {
            ToastService.toast({
              type: 'success',
              body: '编辑企业信息成功！'
            });
            $state.go('^.start');
          } else {
            ToastService.toast({
              type: 'error',
              body: resp.message
            });
          }
        }).catch(function (reason) {
          ToastService.errHandler(reason);
        });
      } else {
        EnterpriseService.save(data).then(function (resp) {
          if (resp && resp.code === 0) {
            ToastService.toast({
              type: 'success',
              body: '添加企业信息成功！'
            });
            $state.go('^.start');
          } else {
            ToastService.toast({
              type: 'error',
              body: resp.message
            });
          }
        }).catch(function (reason) {
          ToastService.errHandler(reason);
        });
      }
    };

    // cancel
    self.cancel = function () {
      $state.go('^.start');
    };

    // 查询企业详情
    self.findDetail = function (id) {
      self.loading = true;
      EnterpriseService.detail(id).then(function (resp) {
        self.loading = false;
        if (resp.code === 0) {
          angular.merge(self.enterprise, resp.result);
          if (self.enterprise.province) {
            if (!self.region) {
              self.region = {};
            }
            self.region.t1 = self.enterprise.province;
          }
          if (self.enterprise.city) {
            if (!self.region) {
              self.region = {};
            }
            self.region.t2 = self.enterprise.city;
          }
          if (self.enterprise.district) {
            if (!self.region) {
              self.region = {};
            }
            self.region.t3 = self.enterprise.district;
          }
          self.getBottomNationalFields(self.enterprise.nationalEconomyField, true);
        } else {
          ToastService.toast({
            type: 'error',
            body: resp.message
          });
        }
      }).catch(function (reason) {
        ToastService.errHandler(reason);
      });
    };

    //
    if (self.isEdit) {
      self.findDetail($stateParams.id);
    }
  }

  function EnterpriseRequirementListCtrl($scope, ToastService, ModalService, EnterpriseRequirementService, EnterpriseRequirementAction, INSTANCE_TYPE, PATENT_PROCESS_TYPE) {
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });
    var self = this;

    self.init=function() {
      self.loading = false;
      self.allSelected = false;
      self.requirements = [];

      //配置分页基本参数
      self.paginationConf = {
        currentPage: 1,
        itemsPerPage: 10
      };
      // 查询条件
      self.reset();

      $scope.$watch(function () {
        return self.paginationConf.itemsPerPage + " " + self.paginationConf.currentPage;
      }, self.search);
    }

    self.reset=function(){
      self.query = {
        enterprise: {},
      };
      self.nationalEconomyField = null;
      self.currentNationalEconomyField = null;
      self.region = null;
    }

    // 操作时间范围回调
    self.onDatetimeRangeChanged = function (property, start, end) {
      self.query.optDateFrom = start ? start.toDate().getTime() : start;
      self.query.optDateTo = end ? end.toDate().getTime() : end;
    };

    // allselected checkbox change
    self.changeAllSelected = function () {
      self.requirements.forEach(function (item) {
        item.selected = self.allSelected;
      });
    }

    self.changeSelected = function () {
      // 选中个数
      var num = self.requirements.filter(function (item) {
        return item.selected
      }).length;
      self.allSelected = num == self.requirements.length;
    }

    self.openNationalDlg = function () {
      ModalService.openNationalDlg(self.nationalEconomyField).then(function(data){
        self.nationalEconomyField = data;
        self.currentNationalEconomyField = $scope.getNationalEconomy(data);
      }).catch(function (err) {
        $log.info(err);
      });
    };

    self.openRegionDlg = function () {
      ModalService.openRegionDlg(self.region).then(function (data) {
        self.region = data;
      }).catch(function (err) {
        $log.info(err);
      });
    };

    // 查询
    self.search = function () {
      self.loading = true;
      self.query.pageSize = self.paginationConf.itemsPerPage;
      self.query.pageNum = self.paginationConf.currentPage;

      self.query.requirement = $scope.nullIfSpaceOrEmpty(self.query.requirement);
      self.query.enterprise.name = $scope.nullIfSpaceOrEmpty(self.query.enterprise.name);
      self.query.enterprise.nationalEconomyField = self.currentNationalEconomyField==null ? null : self.currentNationalEconomyField.code;
      self.query.enterprise.provinceId = self.region == null ? null : (self.region.t1 == null ? null : self.region.t1.id);
      self.query.enterprise.cityId = self.region == null ? null : (self.region.t2 == null ? null : self.region.t2.id);
      self.query.enterprise.districtId = self.region == null ? null : (self.region.t3 == null ? null : self.region.t3.id);

      var query = angular.copy(self.query);
      // enterprise对象需要特殊处理
      var enterprise = query.enterprise;
      if (enterprise) {
        delete query.enterprise;
        angular.forEach(enterprise,function (value, key) {
          query['enterprise.' + key] = value;
        })
      }
      EnterpriseRequirementService.search(query).then(function (resp) {
        self.loading = false;
        if (resp.code === 0) {
          self.paginationConf.totalItems = resp.result.total;


          //self.requirements = resp.result.list;
          self.requirements = [];
          resp.result.list.forEach(function (data) {
            self.requirements.push({selected: false, data: data});
          });

          self.searching = false;
        } else {
          ToastService.toast({
            type: 'error',
            body: '查询企业需求出错：' + resp.message
          });
        }
      }).catch(function (reason) {
        self.loading = false;
        ToastService.errHandler(reason);
      })
    };

    // 启动流程
    self.startProcess = function (instanceType, processType, requirement) {
      var requirements = [];
      // 生成需启动企业需求列表
      if (requirement == null) {
        self.requirements.forEach(function (p) {
          if (p.selected) {
            requirements.push(p.data);
          }
        })
      }
      else {
        requirements.push(requirement);
      }

      if (requirements.length == 0) {
        ToastService.toast({
          type: 'warn',
          body: '请至少选择一个企业需求！'
        });
        return;
      }
      self.request(instanceType, processType, requirements);
    }
    self.request=function(instanceType, processType, requirements) {
      self.loading = true;

      var instanceTypeId = INSTANCE_TYPE.filter(function (type) {
        return type.key == instanceType;
      })[0].id;

      var processTypeId = PATENT_PROCESS_TYPE.filter(function (type) {
        return type.key == processType;
      })[0].id;

      EnterpriseRequirementService.startProcess(instanceTypeId, processTypeId, requirements).then(function (resp) {
        if (resp && resp.code === 0) {
          self.search();
          ToastService.toast({
            type: 'success',
            body: resp.result
          });
        }
        else {
          ToastService.toast({
            type: 'info',
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
    }

    // 添加或编辑企业需求
    self.editRequirement = function (requirement, index) {
      EnterpriseRequirementAction.addOrEditRequirement(angular.copy(requirement)).then(function (requirement, isEdit) {
        if (isEdit) {
          self.requirements.splice(index, 1, requirement);
        } else {
          self.search();
        }
      }).catch(function (reason) {
        //
      });
    };

    self.init();
  }

  /**
   * 编辑或新增企业需求
   * @param $scope
   * @param FormService
   * @param ToastService
   * @param ModalService
   * @param EnterpriseService
   * @param EnterpriseRequirementService
   * @param $uibModalInstance
   * @param requirement
   * @constructor
   */
  function RequirementEditCtrl($scope, FormService, ToastService, ModalService, EnterpriseService, EnterpriseRequirementService,
                               $uibModalInstance, requirement) {
    var self = this;
    angular.merge(this, FormService);
    self.isEdit = typeof requirement.id !== 'undefined';

    self.requirement = requirement;

    self.getEnterprises = function (val) {
      return EnterpriseService.autoComplete({name: val}).then(function (resp) {
        if (resp.code === 0) {
          return resp.result;
        }
        return [];
      });
    };

    // cancel
    self.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };

    // ok
    self.ok = function () {
      if (self.requirement.enterprise && typeof self.requirement.enterprise === 'string') {
        ToastService.toast({
          type: 'warn',
          body: '请输入企业名称关键词，并从下拉列表中选择！'
        });
        return;
      }
      self.requirement.enterpriseId = self.requirement.enterprise.id; // 更新企业id
      if (self.isEdit) {
        EnterpriseRequirementService.update(self.requirement).then(function (resp) {
          if (resp && resp.code === 0) {
            $uibModalInstance.close(resp.result, self.isEdit);
          } else {
            ToastService.toast({
              type: 'error',
              body: resp.message
            });
          }
        }).catch(function (reason) {
          ToastService.errHandler(reason);
        });
      } else {
        EnterpriseRequirementService.save(self.requirement).then(function (resp) {
          if (resp && resp.code === 0) {
            $uibModalInstance.close(resp.result, self.isEdit);
          } else {
            ToastService.toast({
              type: 'error',
              body: resp.message
            });
          }
        }).catch(function (reason) {
          ToastService.errHandler(reason);
        });
      }
    };
  }

})(angular);
