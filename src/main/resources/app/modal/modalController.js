/**
 * Created by xiongwei on 2017/1/3.
 */
(function (angular) {
  'use strict';
  angular.module('MODALS')
    .controller('AlertCtrl', ['$uibModalInstance', 'alertInfo', AlertCtrl])
    .controller('OrganizationTreeCtrl', ['$uibModalInstance', 'OrganizationService','ToastService', OrganizationTreeCtrl])
    .controller('UploadCtrl', ['$uibModalInstance', 'ToastService', '$timeout', '$log', 'FileUploader', 'uploadConfig', UploadCtrl])
    .controller('VodCtrl', ['$scope', '$uibModalInstance', 'ToastService', 'HttpService', '$log', 'vodConfig', VodCtrl])
    .controller('RegionCtrl', ['$uibModalInstance', 'ModalService','ToastService', 'defaultValue', RegionCtrl])
    .controller('NationalCtrl', ['$uibModalInstance', 'ModalService','ToastService', 'defaultValue', NationalCtrl])
    .controller('IpcCtrl',['$uibModalInstance', 'ModalService','ToastService', 'defaultValue', 'ExpertService', IpcCtrl])
    .controller('ExpertIpcCtrl',['$uibModalInstance', 'ModalService','ToastService', 'defaultValue', 'ExpertService', ExpertIpcCtrl])
    .controller('ExpertNatCtrl',['$uibModalInstance', 'ModalService','ToastService', 'defaultValue', 'ExpertService', ExpertNatCtrl])
    .controller('ExpertIduCtrl',['$uibModalInstance', 'ModalService','ToastService', 'defaultValue', 'ExpertService', ExpertIduCtrl])
  ;

  /**
   * 警告Ctrl
   * @param $uibModalInstance
   * @param alertInfo
   * @constructor
   */
  function AlertCtrl($uibModalInstance, alertInfo) {
    var self = this;
    self.alertInfo = alertInfo;

    self.alertClass = function () {
      var type = self.alertInfo.type || 'default';
      return {
        'bg-danger': type === 'danger',
        'bg-warning': type === 'warning',
        'bg-error': type === 'error',
        'bg-primary': type === 'primary',
        'bg-info': type === 'info'
      };
    };

    self.ok = function () {
      $uibModalInstance.close(0);
    };
    self.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };
  }

  function RegionCtrl($uibModalInstance, ModalService, ToastService, defaultValue) {
    var self = this;

    self.init = function() {
      self.loading = false;
      self.region = {
        t1: defaultValue != null && defaultValue.t1!=null ? angular.toJson(defaultValue.t1) : null,
        t2: defaultValue != null && defaultValue.t2!=null ? angular.toJson(defaultValue.t2) : null,
        t3: defaultValue != null && defaultValue.t3!=null ? angular.toJson(defaultValue.t3) : null,

      };
      self.getAllRegions();
    }

    self.getAllRegions = function () {
      self.loading = true;
      ModalService.getAllRegions().then(function (resp) {
        self.loading = false;
        if (resp && resp.code === 0) {
          self.regions = resp.result;
          self.t1s = [];
          self.t2s = [];
          self.t3s = [];

          self.regions.forEach(function (data) {
            if (data.parentId == 0 || data.parentId == null) {
              self.t1s.push(data)
            }
          });
          if (self.region != null && self.region.t1 != null) {
            self.regionChange(1, self.region.t1);
          }
          if (self.region != null && self.region.t2 != null) {
            self.regionChange(2, self.region.t2);
          }
        } else {
          ToastService.toast({
            type: 'error',
            body: '请求行政区域失败:' + resp.message
          });
        }
      }).catch(function (err) {
        self.loading = false;
        ToastService.errHandler(err);
      });
    };

    self.regionChange = function(type, item) {
      var region = item == "" ? null : angular.fromJson(item);

      // 省份改变
      if (type == 1) {
        self.t2s = [];
        self.t3s = [];
        self.regions.forEach(function (data) {
          if (data.parentId == region.id) {
            self.t2s.push(data)
          }
        });
      }
      // 城市改变
      else if (type == 2) {
        self.t3s = [];
        self.regions.forEach(function (data) {
          if (data.parentId == region.id) {
            self.t3s.push(data)
          }
        });
      }
    }

    self.ok = function () {
      var result = {
        t1: null,
        t2: null,
        t3: null,
      };

      if (self.region == null) {
        result = null;
      }
      else {
        result.t1 = self.region.t1 == null || self.region.t1 == '' ? null : angular.fromJson(self.region.t1);
        result.t2 = self.region.t2 == null || self.region.t2 == '' ? null : angular.fromJson(self.region.t2);
        result.t3 = self.region.t3 == null || self.region.t3 == '' ? null : angular.fromJson(self.region.t3);
      }
      if (result.t1 == null && result.t2 == null && result.t3 == null) {
        result = null;
      }
      $uibModalInstance.close(result);
    };

    self.cancel = function () {
      $uibModalInstance.close(null);
    };

    self.init();
  }

  function IpcCtrl($uibModalInstance, ModalService, ToastService, defaultValue, ExpertService) {
      var self = this;
      self.init = function () {
          self.loading = false;
          self.getAllIpc();
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

      self.ok = function () {
          var result = {
              t1: null,
              t2: null
          };

          if (self.ipcs == null) {
              result = null;
          }
          else {
              result.t1 = self.t1 == null || self.t1 == '' ? null : angular.fromJson(self.t1);
              result.t2 = self.t2 == null || self.t2 == '' ? null : angular.fromJson(self.t2);
          }
          if (result.t1 == null && result.t2 == null) {
              result = null;
          }
          $uibModalInstance.close(result);
      };

      self.cancel = function () {
          $uibModalInstance.close(null);
      };

      self.init();

  }

  function NationalCtrl($uibModalInstance, ModalService, ToastService, defaultValue) {
    var self = this;

    self.init = function() {
      self.loading = false;
      self.national = {
        t1: defaultValue != null && defaultValue.t1!=null ? angular.toJson(defaultValue.t1) : null,
        t2: defaultValue != null && defaultValue.t2!=null ? angular.toJson(defaultValue.t2) : null,
        t3: defaultValue != null && defaultValue.t3!=null ? angular.toJson(defaultValue.t3) : null,
        t4: defaultValue != null && defaultValue.t4!=null ? angular.toJson(defaultValue.t4) : null,
      };
      self.getAllNationals();
    }

    self.getAllNationals = function () {
      self.loading = true;
      ModalService.getAllNationals().then(function (resp) {
        self.loading = false;
        if (resp && resp.code === 0) {
          self.nationals = resp.result;
          self.t1s = [];
          self.t2s = [];
          self.t3s = [];
          self.t4s = [];
          self.nationals.forEach(function (data) {
            if (data.parentId == 0 || data.parentId == null) {
              self.t1s.push(data)
            }
          });
          if (self.national!=null && self.national.t1!=null) {
            self.nationalChange(1, self.national.t1);
          }
          if (self.national!=null && self.national.t2!=null) {
            self.nationalChange(2, self.national.t2);
          }
          if (self.national!=null && self.national.t3!=null) {
            self.nationalChange(3, self.national.t3);
          }
        } else {
          ToastService.toast({
            type: 'error',
            body: '请求国名经济代码失败:' + resp.message
          });
        }
      }).catch(function (err) {
        self.loading = false;
        ToastService.errHandler(err);
      });
    };

    self.nationalChange = function(type, item) {
      var national = item == "" ? null : angular.fromJson(item);
      // 门类改变
      if (type == 1) {
        self.t2s = [];
        self.t3s = [];
        self.t4s = [];
        if (national != null) {
          self.nationals.forEach(function (data) {
            if (data.parentId == national.id) {
              self.t2s.push(data)
            }
          });
        }
      }
      // 大类改变
      else if (type == 2) {
        self.t3s = [];
        self.t4s = [];
        if (national != null) {
          self.nationals.forEach(function (data) {
            if (data.parentId == national.id) {
              self.t3s.push(data)
            }
          });
        }
      }
      // 中类改变
      else if (type == 3) {
        self.t4s = [];
        if (national != null) {
          self.nationals.forEach(function (data) {
            if (data.parentId == national.id) {
              self.t4s.push(data)
            }
          });
        }
      }
    }

    self.ok = function () {
      var result = {
        t1: null,
        t2: null,
        t3: null,
        t4: null,
      };

      if (self.national == null) {
        result = null;
      }
      else {
        result.t1 = self.national.t1 == null || self.national.t1 == '' ? null : angular.fromJson(self.national.t1);
        result.t2 = self.national.t2 == null || self.national.t2 == '' ? null : angular.fromJson(self.national.t2);
        result.t3 = self.national.t3 == null || self.national.t3 == '' ? null : angular.fromJson(self.national.t3);
        result.t4 = self.national.t4 == null || self.national.t4 == '' ? null : angular.fromJson(self.national.t4);
      }
      if (result.t1 == null && result.t2 == null && result.t3 == null && result.t4 == null) {
        result = null;
      }
      $uibModalInstance.close(result);
    };

    self.cancel = function () {
      $uibModalInstance.close(null);
    };

    self.init();
  }

  function ExpertIpcCtrl($uibModalInstance, ModalService, ToastService, defaultValue, ExpertService) {
    var self = this;
    self.init = function () {
      self.loading = false;

    };



    self.ok = function () {

      $uibModalInstance.close(null);
    };

    self.cancel = function () {
      $uibModalInstance.close(null);
    };

    self.init();

  }
  function ExpertNatCtrl($uibModalInstance, ModalService, ToastService, defaultValue, ExpertService) {
    var self = this;
    self.init = function () {
      self.loading = false;

    };



    self.ok = function () {

      $uibModalInstance.close(null);
    };

    self.cancel = function () {
      $uibModalInstance.close(null);
    };

    self.init();

  }
  function ExpertIduCtrl($uibModalInstance, ModalService, ToastService, defaultValue, ExpertService) {
    var self = this;
    self.init = function () {
      self.loading = false;

    };



    self.ok = function () {

      $uibModalInstance.close(null);
    };

    self.cancel = function () {
      $uibModalInstance.close(null);
    };

    self.init();

  }
  /**
   * 组织树
   * @param $uibModalInstance
   * @constructor
   */
  function OrganizationTreeCtrl($uibModalInstance, OrganizationService , ToastService) {
    var self = this;
    self.loading = false;
    self.nodes = {
      id: '0',
      text: 'PMES',
      icon: 'fa fa-circle-o',
      state: {
        opened: true
      },
    };

    self.onTreeReady = function (tree, controller) {
      if (!self.treeCtrl) {
        self.treeCtrl = controller;
        self.getOrganizations();
      }
    };

    self.organizationChildren = function(organization) {
      if(!organization.sonOrganizations) {
        return;
      }
      return organization.sonOrganizations.map(function (subOrganization) {
        return {
          id: subOrganization.id,
          name: subOrganization.remark,
          text: subOrganization.name,
          icon: subOrganization.pic || 'fa fa-file',
          state: {
            opened: true
          },
          children: self.organizationChildren(subOrganization)
        };
      });
    }

    self.onNodeSelected = function (organizationId) {
      self.loading = true;
      OrganizationService.getOrganizationById(organizationId).then(function (resp) {
        self.loading = false;
        if (resp && resp.code === 0) {
          self.selectedOrganization = resp.result;
        } else {
          ToastService.toast({
            type: 'error',
            body: '请求组织机构失败:' + resp.message
          });
        }
      }).catch(function (err) {
        self.loading = false;
        ToastService.errHandler(err);
      });
    };

    self.getOrganizations = function () {
      self.loading = true;
      OrganizationService.getOrganizations().then(function (resp) {
        self.loading = false;
        if (resp && resp.code === 0) {
          self.organizations = resp.result;
          self.nodes.children = resp.result.map(function (organization) {
            return {
              id: organization.id,
              name: organization.remark,
              text: organization.name,
              state: {
                opened: true
              },
              icon: organization.pic || 'fa fa-folder',
              children: self.organizationChildren(organization)
            };
          });
          if (self.treeCtrl) {
            self.treeCtrl.refresh();
          }
        } else {
          ToastService.toast({
            type: 'error',
            body: '请求组织机构失败:' + resp.message
          });
        }
      }).catch(function (err) {
        self.loading = false;
        ToastService.errHandler(err);
      });
    };

    self.alertClass = function () {
      var type = self.alertInfo.type || 'default';
      return {
        'bg-danger': type === 'danger',
        'bg-warning': type === 'warning',
        'bg-error': type === 'error',
        'bg-primary': type === 'primary',
        'bg-info': type === 'info'
      };
    };

    self.ok = function () {
      if(!self.selectedOrganization) {
        ToastService.toast({
          type: 'info',
          body: '未获取到您选择的组织机构信息'
        });
        return;
      }
      $uibModalInstance.close(self.selectedOrganization);
    };
    self.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };
  }

  /**
   * 上传弹出对话框
   * @param $uibModalInstance
   * @param FileUploader
   * @param uploadConfig
   * @constructor
   */
  function UploadCtrl($uibModalInstance, ToastService, $timeout, $log, FileUploader, uploadConfig) {
    var self = this;
    var url = '', name = '';
    self.uploadConfig = uploadConfig;
    self.uploadResult = [];
    $log.info('self.uploadConfig', self.uploadConfig);
    if (self.uploadConfig.filter === "mp4") {
      name = 'videoFilter';
      url = '/upload/video/';
    } else if (self.uploadConfig.filter.split(',').some(function (extName) {
        return extName === 'xlsx' || extName === 'xls';
      })) {
      name = 'excelFilter';
      url = '/upload/excel/' + self.uploadConfig.subjectId;
    } else {
      name = 'imageFilter';
      url = '/upload';
    }
    // var usename = AuthService.getLoginUser().phone,
    //   password = $.base64.atob(AuthService.getLoginUser().password);
    var uploader = self.uploader = new FileUploader({
      url: url
    });
    // uploader.headers.authorization = "Basic " + $.base64.btoa(usename + ":" + password);
    // FILTERS
    uploader.filters.push({
      name: name,
      fn: function (item /*{File|FileLikeObject}*/, options) {
        var type = item.name.slice(item.name.lastIndexOf('.') + 1);
        return self.uploadConfig.filter.indexOf(type) !== -1 && uploader.queue.length < self.uploadConfig.maxFiles;
      }
    });
    // CALLBACKS
    uploader.onAfterAddingFile = function (fileItem) {
      $log.info('onAfterAddingFile', fileItem);
      if (self.uploadConfig.maxFiles === 1) {
        fileItem.upload();
      }
    };
    uploader.onSuccessItem = function (fileItem, response, status, headers) {
      $log.info('onSuccessItem', fileItem, response, status, headers);
      if (response.code === 0) {
        self.uploadResult.push(response.result);
      } else {
        fileItem.remove();
        angular.element('#file').val(null);
        ToastService.toast({
          type: 'error',
          body: '上传失败:' + (response.message || '未知原因')
        });
      }
    };
    uploader.onErrorItem = function (fileItem, response, status, headers) {
      $log.info('onErrorItem', fileItem, response, status, headers);
      fileItem.remove();
      angular.element('#file').val(null);
      ToastService.toast({
        type: 'error',
        body: '上传失败:' + (response.message || '未知原因')
      });
    };

    self.ok = function () {
      $log.info('上传结果', self.uploadResult);
      $uibModalInstance.close(self.uploadResult);
    };
    self.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };

    if (self.uploadConfig.openFileDialogImmediately) {
      $timeout(function () {
        angular.element('#file').click();
      }, 1500);
    }
  }

  /**
   * 视频点播上传
   * @param $uibModalInstance
   * @param ToastService
   * @param $log
   * @constructor
   */
  function VodCtrl($scope, $uibModalInstance, ToastService, HttpService, $log, vodConfig) {
    $scope.vodConfig = vodConfig;
    $scope.uploadStatus = 0; // 0-未开始,1-上传中,2-完成
    $scope.uplaodInfo = undefined;
    $scope.progress = 0;

    var uploader = $scope.uploader = new VODUpload({
      // 文件上传失败
      'onUploadFailed': function (uploadInfo, code, message) {
        $log.log("onUploadFailed: file:" + uploadInfo.file.name + ",code:" + code + ", message:" + message);
      },
      // 文件上传完成
      'onUploadSucceed': function (uploadInfo) {
        $log.log('uploadInfo', uploadInfo);
        $scope.$apply(function () {
          $scope.uploadStatus = 2;
        });
        $log.log("onUploadSucceed: " + uploadInfo.file.name + ", endpoint:" + uploadInfo.endpoint + ", bucket:" + uploadInfo.bucket + ", object:" + uploadInfo.object);
      },
      // 文件上传进度
      'onUploadProgress': function (uploadInfo, totalSize, uploadedSize) {
        var progress = Math.ceil(uploadedSize * 100 / totalSize);
        $scope.$apply(function () {
          $scope.progress = progress;
        });

        $log.log("onUploadProgress:file:" + uploadInfo.file.name + ", fileSize:" + totalSize + ", percent:" + progress + "%");
      },
      // 开始上传
      'onUploadstarted': function (uploadInfo) {
        uploader.setUploadAuthAndAddress(uploadInfo, $scope.uploadInfo.uploadAuth, $scope.uploadInfo.uploadAddress);
        $log.log("onUploadStarted:" + uploadInfo.file.name + ", endpoint:" + uploadInfo.endpoint + ", bucket:" + uploadInfo.bucket + ", object:" + uploadInfo.object);
      }
    });
    uploader.init();

    // 监听文件选择
    $scope.fileChanged = function (element) {
      $scope.$apply(function () {
        $scope.uploadFile = element.files[0];
        $scope.uplaodInfo = undefined;
      });
    };

    $scope.$watch('uploadFile', function (newValue) {
      $log.log('newValue', newValue);
      if (newValue) {
        $scope.getUploadInfo(newValue.name);
      }
    });

    // 获取上传token和address
    $scope.getUploadInfo = function (fileName, cb) {
      HttpService.get('/api/video/auth/', {fileName: fileName})
        .then(function (resp) {
          if (resp && resp.code === 0) {
            $scope.uploadInfo = resp.result;
            cb && cb(null);
          } else {
            cb && cb(new Error(resp.message));
            ToastService.toast({
              type: 'error',
              body: '请求上传路径出错:' + resp.message
            });
          }
        })
        .catch(function (err) {
          cb && cb(err);
          ToastService.toast({
            type: 'error',
            body: '请求上传路径出错:' + err.message
          });
        });
    };

    $scope.ok = function () {
      if ($scope.uploadStatus === 0) {
        if (!$scope.uploadFile) {
          return ToastService.toast({
            type: 'error',
            body: '请先选择视频文件！'
          });
        }
        if ($scope.uploadInfo) {
          uploader.addFile($scope.uploadFile, null, null, null, '{"Vod":{"UserData":"{"IsShowWaterMark":"true","Priority":"7"}"}}');
          uploader.startUpload();
          $scope.uploadStatus = 1;
        } else {
          $scope.getUploadInfo($scope.uploadFile.name, function (err) {
            if (err) {
              ToastService.toast({
                type: 'error',
                body: '获取上传地址失败,请稍候重试！'
              });
            } else {
              $scope.ok();
            }
          });
        }
      } else if ($scope.uploadStatus === 2) {
        $uibModalInstance.close($scope.uploadInfo.videoId);
      } else {
        $uibModalInstance.dismiss('cancel');
      }
    };
    $scope.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };
  }
})(angular);