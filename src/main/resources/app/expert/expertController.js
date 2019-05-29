/**
 * Created by crixalis on 2018/3/22.
 */

(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('Expert')
    .controller('ExpertController', ['$scope', '$filter', '$location', 'ModalService','ToastService', 'ExpertService', 'UserService', ExpertController])
    .controller('ExpertEditController', ['$scope', '$filter', '$location', 'ModalService','ToastService', 'ExpertService', '$state', ExpertEditController])
    .controller('ExpertLibraryController', ['$scope', '$filter', '$location', 'ModalService','ToastService', 'ExpertService', 'UserService', ExpertLibraryController])
    .controller('ExpertLibraryEditController', ['$scope','$timeout','ToastService', 'ExpertLibraryService', 'Upload', ExpertLibraryEditController])
  ;

  function ExpertEditController($scope, $filter, $location, ModalService, ToastService, ExpertService, $state) {
    var self = this;
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });

    self.init = function () {
      self.selectSpec = false;
      self.selectIpc = false;
      self.editId = $state.params.id;
      if(self.editId) {
        self.isUpdate = true;
        self.get();
      };
    };

    self.get = function () {
      ExpertService.get(self.editId).then(function(resp) {
        if (resp.code === 0) {
            self.expert = resp.result;
        } else {
            ToastService.toast({
                type: 'error',
                body: resp.detailMessage
            });
        }
      });
    };

    self.changeSpec = function () {
        self.selectSpec = !self.selectSpec;
    };

    self.changeIpc = function () {
        self.selectIpc = !self.selectIpc;
    };

    self.edit = function () {
      if(self.isUpdate) {
        self.update();
      } else {
        self.add();
      }
    };

    self.close = function () {
        $state.go("main.console.expert.list");
    };

    self.add = function () {
      ExpertService.add(self.expert).then(function (resp) {
        if (resp.code === 0) {
          ToastService.toast({
            type: 'success',
            body: '添加专家成功'
          });
          $state.go("main.console.expert.list");
        } else {
          ToastService.toast({
            type: 'error',
            body: resp.detailMessage
          });
        }
      });
    };

    self.update = function () {
      ExpertService.update(self.expert).then(function (resp) {
        if (resp.code === 0) {
          ToastService.toast({
              type: 'success',
              body: '修改专家成功'
          });
          $state.go("main.console.expert.list");
        } else {
          ToastService.toast({
              type: 'error',
              body: resp.detailMessage
          });
        }
      });
  };


    self.init();

  };

  function ExpertController($scope, $filter, $location, ModalService, ToastService, ExpertService, UserService) {
    var self = this;
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });
    self.init = function () {

      // 查询条件
      self.reset();

      self.getAllUsers();
      //配置分页基本参数
      self.paginationConf = {
          currentPage: 1,
          itemsPerPage: 10
      };
      $scope.$watch(function () {
        return self.paginationConf.itemsPerPage + " " + self.paginationConf.currentPage;
      }, self.search);
    };

    // 重置
    self.reset = function(){
      self.searchCondition = {
      };
    }

    self.getAllUsers = function () {
        UserService.getUsers(1, 9999).then(function (resp) {
            self.users = [];
            if (resp && resp.code === 0) {
                self.users = self.users.concat(resp.result.list);
            } else {
                ToastService.toast({
                    type: "error",
                    body: '获取用户失败'
                });
            }
        }).catch(ToastService.errHandler);
    }

    self.openIpcSelector = function () {
      ModalService.openIpcSelector(self.ipcField).then(function(data){
          self.ipcField = data;
          self.searchCondition.specialties = null;
          if(data.t1) {
              self.searchCondition.specialties = data.t2.code;
          }
      }).catch(function (err) {
          $log.info(err);
      });
    }

    self.onDatetimeRangeChanged = function (start, end) {
      self.searchCondition.optDateFrom = start ? start.toDate().getTime() : start;
      self.searchCondition.optDateTo = end ? end.toDate().getTime() : end;
    };

    // allselected checkbox change
    self.changeAllSelected = function () {
      self.experts.forEach(function (item) {
        item.selected = self.allSelected;
      });
    }

    self.changeSelected = function () {
      // 选中个数
      var num = self.patents.filter(function (item) {
        return item.selected
      }).length;
      self.allSelected = num == self.patents.length;
    }

    // 搜索
    self.search = function () {
      self.loading = true;
      self.searchCondition.pageSize = self.paginationConf.itemsPerPage;
      self.searchCondition.pageNum = self.paginationConf.currentPage;
      self.searchCondition.institutions = $scope.nullIfSpaceOrEmpty(self.searchCondition.institutions);
      self.searchCondition.name = $scope.nullIfSpaceOrEmpty(self.searchCondition.name);
      self.searchCondition.ipcField = $scope.nullIfSpaceOrEmpty(self.searchCondition.ipcField);
      self.searchCondition.keyword = $scope.nullIfSpaceOrEmpty(self.searchCondition.keyword);
      self.searchCondition.userId = $scope.nullIfSpaceOrEmpty(self.searchCondition.userId);
      self.searchCondition.specialties = $scope.nullIfSpaceOrEmpty(self.searchCondition.specialties);
      self.searchCondition.optDateFrom = $scope.nullIfSpaceOrEmpty(self.searchCondition.optDateFrom);
      self.searchCondition.optDateTo = $scope.nullIfSpaceOrEmpty(self.searchCondition.optDateTo);
      ExpertService.search(self.searchCondition).then(function (resp) {
        self.loading = false;
        if (resp.code === 0) {
          self.paginationConf.totalItems = resp.result.total;
          self.experts = [];
          resp.result.list.forEach(function (data) {
            data.selected = false;
            self.experts.push(data);
          });
          self.searching = false;
        }
        else {
          ToastService.toast({
            type: 'error',
            body: resp.message
          });
        }
      }).catch(function (reason) {
        self.loading = false;
        ToastService.errHandler(reason);
      })
    };

    self.openSpecialtiesDlg = function () {
        ModalService.openSpecDlg(self.nationalEconomyField).then(function(data){
          self.nationalEconomyField = data;
          self.currentNationalEconomyField = $scope.getNationalEconomy(data);
          self.searchCondition.specialties = null;
          if(self.nationalEconomyField.t1) {
              self.searchCondition.specialties = self.currentNationalEconomyField.code;
          }
        }).catch(function (err) {
            $log.info(err);
        });
    };

    self.getAllIpc = function () {
      self.loading = true;
      ExpertService.getAllIPC().then(function (resp) {
          self.loading = false;
          if (resp && resp.code === 0) {
              self.ipcs = resp.result;
              if(defaultValue) {
                  for(var i=0; i<self.ipcs.length; i++) {
                      if(self.ipcs[i].id === defaultValue.t1.id) {
                          self.t1 = self.ipcs[i];
                      }
                  }
              }
              if(self.t1) {
                  for(var i=0; i<self.t1.sonIPCFields.length; i++) {
                      if (self.t1.sonIPCFields[i].id === defaultValue.t2.id) {
                          self.t2 = self.t1.sonIPCFields[i];
                      }
                  }
              }
          } else {
              ToastService.toast({
                  type: 'error',
                  body: '请求IPC领域失败:' + resp.message
              });
          }
      }).catch(function (err) {
          self.loading = false;
          ToastService.errHandler(err);
      });
  };

    self.init();
  }

  function ExpertLibraryController($scope, $filter, $location, ModalService, ToastService, ExpertService, UserService) {
    var self = this;
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });
    self.init = function () {

      // 查询条件
      self.reset();

      self.getAllUsers();
      //配置分页基本参数
      self.paginationConf = {
        currentPage: 1,
        itemsPerPage: 10
      };
      $scope.$watch(function () {
        return self.paginationConf.itemsPerPage + " " + self.paginationConf.currentPage;
      }, self.search);
    };

    // 重置
    self.reset = function(){
      self.searchCondition = {
      };
    }

    self.getAllUsers = function () {
      UserService.getUsers(1, 9999).then(function (resp) {
        self.users = [];
        if (resp && resp.code === 0) {
          self.users = self.users.concat(resp.result.list);
        } else {
          ToastService.toast({
            type: "error",
            body: '获取用户失败'
          });
        }
      }).catch(ToastService.errHandler);
    }

    self.openIpcDlg = function () {
      ModalService.openIpcDialog(self.ipcField).then(function(data){
        self.ipcField = data;
        self.searchCondition.specialties = null;
        if(data.t1) {
          self.searchCondition.specialties = data.t2.code;
        }
      }).catch(function (err) {
        $log.info(err);
      });
    }
    self.openExpertNationalDlg = function () {
      ModalService.openExpertNationalDlg(self.ipcField).then(function(data){
        self.ipcField = data;
        self.searchCondition.specialties = null;
        if(data.t1) {
          self.searchCondition.specialties = data.t2.code;
        }
      }).catch(function (err) {
        $log.info(err);
      });
    }
    self.openIndustryDlg = function () {
      ModalService.openIndustryDlg(self.ipcField).then(function(data){
        self.ipcField = data;
        self.searchCondition.specialties = null;
        if(data.t1) {
          self.searchCondition.specialties = data.t2.code;
        }
      }).catch(function (err) {
        $log.info(err);
      });
    }

    self.onDatetimeRangeChanged = function (start, end) {
      self.searchCondition.optDateFrom = start ? start.toDate().getTime() : start;
      self.searchCondition.optDateTo = end ? end.toDate().getTime() : end;
    };

    // allselected checkbox change
    self.changeAllSelected = function () {
      self.experts.forEach(function (item) {
        item.selected = self.allSelected;
      });
    }

    self.changeSelected = function () {
      // 选中个数
      var num = self.patents.filter(function (item) {
        return item.selected
      }).length;
      self.allSelected = num == self.patents.length;
    }

    // 搜索
    self.search = function () {
      self.loading = true;
      self.searchCondition.pageSize = self.paginationConf.itemsPerPage;
      self.searchCondition.pageNum = self.paginationConf.currentPage;
      self.searchCondition.institutions = $scope.nullIfSpaceOrEmpty(self.searchCondition.institutions);
      self.searchCondition.name = $scope.nullIfSpaceOrEmpty(self.searchCondition.name);
      self.searchCondition.ipcField = $scope.nullIfSpaceOrEmpty(self.searchCondition.ipcField);
      self.searchCondition.keyword = $scope.nullIfSpaceOrEmpty(self.searchCondition.keyword);
      self.searchCondition.userId = $scope.nullIfSpaceOrEmpty(self.searchCondition.userId);
      self.searchCondition.specialties = $scope.nullIfSpaceOrEmpty(self.searchCondition.specialties);
      self.searchCondition.optDateFrom = $scope.nullIfSpaceOrEmpty(self.searchCondition.optDateFrom);
      self.searchCondition.optDateTo = $scope.nullIfSpaceOrEmpty(self.searchCondition.optDateTo);
      ExpertService.search(self.searchCondition).then(function (resp) {
        self.loading = false;
        if (resp.code === 0) {
          self.paginationConf.totalItems = resp.result.total;
          self.experts = [];
          resp.result.list.forEach(function (data) {
            data.selected = false;
            self.experts.push(data);
          });
          self.searching = false;
        }
        else {
          ToastService.toast({
            type: 'error',
            body: resp.message
          });
        }
      }).catch(function (reason) {
        self.loading = false;
        ToastService.errHandler(reason);
      })
    };

    self.openSpecialtiesDlg = function () {
      ModalService.openSpecDlg(self.nationalEconomyField).then(function(data){
        self.nationalEconomyField = data;
        self.currentNationalEconomyField = $scope.getNationalEconomy(data);
        self.searchCondition.specialties = null;
        if(self.nationalEconomyField.t1) {
          self.searchCondition.specialties = self.currentNationalEconomyField.code;
        }
      }).catch(function (err) {
        $log.info(err);
      });
    };

    self.getAllIpc = function () {
      self.loading = true;
      ExpertService.getAllIPC().then(function (resp) {
        self.loading = false;
        if (resp && resp.code === 0) {
          self.ipcs = resp.result;
          if(defaultValue) {
            for(var i=0; i<self.ipcs.length; i++) {
              if(self.ipcs[i].id === defaultValue.t1.id) {
                self.t1 = self.ipcs[i];
              }
            }
          }
          if(self.t1) {
            for(var i=0; i<self.t1.sonIPCFields.length; i++) {
              if (self.t1.sonIPCFields[i].id === defaultValue.t2.id) {
                self.t2 = self.t1.sonIPCFields[i];
              }
            }
          }
        } else {
          ToastService.toast({
            type: 'error',
            body: '请求IPC领域失败:' + resp.message
          });
        }
      }).catch(function (err) {
        self.loading = false;
        ToastService.errHandler(err);
      });
    };

    self.init();
  }

  function ExpertLibraryEditController($scope,$timeout, ToastService, ExpertLibraryService,Upload) {
    var self = this;
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });

    self.init = function () {
      $scope.preview = document.querySelector('.preImg');
      $scope.preview.src = '';
      $scope.isImgUrl = '';
      self.province = '';
      self.allData = {};
      self.allColleges = [];
      self.allList = [];
      self.expertPost = [];
      self.getAllProvince();
      self.getAllPost();
      self.getAllIpc();
    };
    // 上传头像
    $scope.upload = function (dataUrl, name) {

      $scope.reader = new FileReader();
      $scope.reader.addEventListener("load", function () {

        $scope.preview.src = $scope.reader.result;
      }, false);

      if (dataUrl) {
        $scope.reader.readAsDataURL(dataUrl);
      }

      Upload.upload({
        url: CONTEXT_PATH+'/api/expert/uploadImage',
        data: {file: dataUrl}
      }).then(function (resp) {
        console.log('Success ' + resp + 'uploaded. Response: ' + resp.data);
        $scope.isImgUrl = resp.result;
        angular.element(".uploadBtn").remove()
      }, function (resp) {
        console.log('Error status: ' + resp.status);
      });

    }

    // 获取省份高校数据
    self.getAllProvince = function () {
      ExpertLibraryService.getAllProvince().then(function (resp) {
        if (resp && resp.code === 0) {

          self.provinceList = Object.keys(resp.result);
          self.allData = resp.result
        } else {
          ToastService.toast({
            type: "error",
            body: '获取省份高校数据出错'
          });
        }
      }).catch(ToastService.errHandler);

    }
    self.changeProvince = function () {
      self.currentProvince = self.province;
      if(self.currentProvince){
        for(var currentProvince in self.allData){
          self.allColleges.push(self.allData[currentProvince])
        }
        for(var i=0;i<self.allColleges.length;i++){
          for(var j=0;j<self.allColleges[i].length;j++){
            self.allList.push(self.allColleges[i][j])
          }
        }
          self.collegesList = self.allList.filter(function (ele) {
            return self.currentProvince === ele.provinceName
          })
      }else{
        self.collegesList = null;
      }


    }

    // 获取职称
    self.getAllPost = function () {
      ExpertLibraryService.getAllPost().then(function (resp) {
        if (resp && resp.code === 0) {
          self.expertPost = resp.result
        } else {
          ToastService.toast({
            type: "error",
            body: '获取职称数据出错！'
          });
        }
      }).catch(ToastService.errHandler);
    }
    // 获取ipc分类
    self.getAllIpc = function () {
      ExpertLibraryService.getAllIpc().then(function (resp) {
        if (resp && resp.code === 0) {
           self.ipcs = resp.result;

        } else {
          ToastService.toast({
            type: "error",
            body: '获取ipc数据出错！'
          });
        }
      }).catch(ToastService.errHandler);
    }

    self.changeIpc = function () {
      if(self.selected){
          self.ipcOne = self.selected.children;
          self.ipcOneName = self.selected.description;
      }else {
        self.ipcOne = null
      }
    }

    self.hasSelected = [];
    self.selectedTags = [];
    self.newSelected = [];
    self.arr = [];
    self.updateSelected = function(action,id,name){
       // console.log(action,id,name)
      if(action == 'add' && self.hasSelected.indexOf(id) == -1){
        self.hasSelected.push(id);
        self.selectedTags.push(name);
        self.newSelected.push(self.ipcOneName+'>'+name)
        console.log(self.newSelected)
      }
      if(action == 'remove' && self.hasSelected.indexOf(id)!=-1){
        var idx = self.hasSelected.indexOf(id);
        self.hasSelected.splice(idx,1);
        self.selectedTags.splice(idx,1);
        self.newSelected.splice(idx,1)
      }
    }

    self.updateSelection = function($event, id){
      console.log(id)
      var checkbox = $event.target;
      var action = (checkbox.checked?'add':'remove');
      self.updateSelected(action,id,checkbox.name);
    }

    self.isSelected = function(id){
      return self.hasSelected.indexOf(id)>=0;
    }


    self.init();
  };
})(angular, CONTEXT_PATH);