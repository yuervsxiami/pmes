/**
 * Created by xiongwei on 2017/1/3.
 */

(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('MODALS')
    .service('ModalService', ['$uibModal', '$log', 'HttpService', ModalService])
  ;

  /**
   * ModalService
   * @param $uibModalInstance
   * @param alertInfo
   * @constructor
   */
  function ModalService($uibModal, $log, HttpService) {

    var getAllRegions = function () {
      return HttpService.get(CONTEXT_PATH + '/api/region/all', {});
    };

    var getAllNationals = function () {
      return HttpService.get(CONTEXT_PATH + '/api/nationaleconomy/all', {});
    };
    /**
     * 打开警告对话框
     * @param alertInfo
     */
    var openAlert = function (alertInfo) {
      var alertModalInstance = $uibModal.open({
        animation: true,
        templateUrl: CONTEXT_PATH + '/static/tpls/modal/alert.html',
        controller: 'AlertCtrl',
        controllerAs: 'alert',
        size: 'lg',
        resolve: {
          alertInfo: function () {
            return angular.merge({
              type: 'info',
              hideCancel: false,
            }, alertInfo || {});
          }
        }
      });
      return alertModalInstance.result;
    };

    /**
     * 打开组织树对话框
     * @param alertInfo
     */
    var openOrganTree = function (alertInfo) {
      var organTreeModalInstance = $uibModal.open({
        animation: true,
        templateUrl: CONTEXT_PATH + '/static/tpls/modal/organizationTree.html',
        controller: 'OrganizationTreeCtrl',
        controllerAs: 'organTree',
        size: 'lg',
      });
      return organTreeModalInstance.result;
    };

    /**
     * 打开行政区域对话框
     */
    var openRegionDlg = function (defaultValue) {
      var regionModalInstance = $uibModal.open({
        animation: true,
        templateUrl: CONTEXT_PATH + '/static/tpls/modal/region.html',
        controller: 'RegionCtrl',
        controllerAs: 'regionCtrl',
        size: 'lg',
        resolve: {
          defaultValue: defaultValue
        }
      });
      return regionModalInstance.result;
    };

    /**
     * 打开国民经济代码对话框
     */
    var openNationalDlg = function (defaultValue) {
      var nationalModalInstance = $uibModal.open({
        animation: true,
        templateUrl: CONTEXT_PATH + '/static/tpls/modal/national.html',
        controller: 'NationalCtrl',
        controllerAs: 'nationalCtrl',
        size: 'lg',
        resolve: {
          defaultValue: defaultValue
        }
      });
      return nationalModalInstance.result;
    };

    /**
     * 打开专家特长对话框
     */
    var openSpecDlg = function (defaultValue) {
        var nationalModalInstance = $uibModal.open({
            animation: true,
            templateUrl: CONTEXT_PATH + '/static/tpls/modal/specialties.html',
            controller: 'NationalCtrl',
            controllerAs: 'nationalCtrl',
            size: 'lg',
            resolve: {
                defaultValue: defaultValue
            }
        });
        return nationalModalInstance.result;
    };

    /**
     * 打开上传对话框
     * @param uploadConfig
     */
    var openUploader = function (uploadConfig) {
      var uploaderModalInstance = $uibModal.open({
        animation: true,
        templateUrl: CONTEXT_PATH + '/static/tpls/modal/upload.html',
        controller: 'UploadCtrl',
        controllerAs: 'upload',
        size: 'lg',
        resolve: {
          uploadConfig: function () {
            return angular.merge({
              title: '上传',
              maxFiles: 5,
              openFileDialogImmediately: true,
              filter: 'jpg,jpeg,png,pdf,mp4'
            }, uploadConfig || {});
          }
        }
      });
      return uploaderModalInstance.result;
    };

    var openVodUploader = function (vodConfig) {
      var vodUploaderModalInstance = $uibModal.open({
        animation: true,
        templateUrl: CONTEXT_PATH + '/static/tpls/modal/vod.html',
        controller: 'VodCtrl',
        controllerAs: 'vod',
        size: 'lg',
        resolve: {
          vodConfig: function () {
            return angular.merge({
              title: '上传',
            }, vodConfig || {});
          }
        }
      });
      return vodUploaderModalInstance.result;
    };

    var openChangePasswordEditer = function () {
      var changePasswordEditerModalInstance = $uibModal.open({
        animation: true,
        templateUrl: CONTEXT_PATH + '/static/tpls/system/user/changePassword.html',
        controller: 'ChangePasswordEditerCtrl',
        controllerAs: 'cp',
        size: 'lg',
        resolve: {
          cpConfig: function () {
            return angular.merge({
              title: '修改密码',
            }, {});
          }
        }
      });
      return changePasswordEditerModalInstance.result;
    };

		var openChangeProfileEditer = function () {
			var changeProfileEditerModalInstance = $uibModal.open({
				animation: true,
				templateUrl: CONTEXT_PATH + '/static/tpls/system/user/changeProfile.html',
				controller: 'ChangeProfileEditerCtrl',
				controllerAs: 'cp',
				size: 'lg',
				resolve: {
					cpConfig: function () {
						return angular.merge({
							title: '编辑个人信息',
						}, {});
					}
				}
			});
			return changeProfileEditerModalInstance.result;
		};

    var openMetaEditer = function (metaConfig) {
      var metaEditerModalInstance = $uibModal.open({
        animation: true,
        templateUrl: CONTEXT_PATH + '/static/tpls/configuration/meta/metaEditer.html',
        controller: 'MetaEditCtrl',
        controllerAs: 'me',
        size: 'lg',
        resolve: {
          metaConfig: function () {
            return angular.merge({
              title: '元数据编辑',
            }, metaConfig || {});
          }
        }
      });
      return metaEditerModalInstance.result;
    };

    var openMetaViewer = function (metaConfig) {
      var metaViewerModalInstance = $uibModal.open({
        animation: true,
        templateUrl: CONTEXT_PATH + '/static/tpls/configuration/meta/metaViewer.html',
        controller: 'MetaViewerCtrl',
        controllerAs: 'me',
        size: 'lg',
        resolve: {
          metaConfig: function () {
            return angular.merge({
              title: '元数据展示',
            }, metaConfig || {});
          }
        }
      });
      return metaViewerModalInstance.result;
    };

    var openLabelEditer = function (labelConfig) {
      var labelEditerModalInstance = $uibModal.open({
        animation: true,
        templateUrl: CONTEXT_PATH + '/static/tpls/configuration/label/labelEditer.html',
        controller: 'LabelEditCtrl',
        controllerAs: 'labEdit',
        size: 'lg',
        resolve: {
          labelConfig: function () {
            return angular.merge({
              title: '标签编辑',
            }, labelConfig || {});
          }
        }
      });
      return labelEditerModalInstance.result;
    };

    var openLabelViewer = function (labelConfig) {
      var labelViewerModalInstance = $uibModal.open({
        animation: true,
        templateUrl: CONTEXT_PATH + '/static/tpls/configuration/label/labelViewer.html',
        controller: 'LabelViewerCtrl',
        controllerAs: 'labView',
        size: 'lg',
        resolve: {
          labelConfig: function () {
            return angular.merge({
              title: '标签展示',
            }, labelConfig || {});
          }
        }
      });
      return labelViewerModalInstance.result;
    };

    var openIpcSelector = function (defaultValue) {
        var ipcSelectorModalInstance = $uibModal.open({
            animation: true,
            templateUrl: CONTEXT_PATH + '/static/tpls/modal/ipcSelector.html',
            controller: 'IpcCtrl',
            controllerAs: 'ipcCtrl',
            size: 'lg',
            resolve: {
                defaultValue: defaultValue
            }
        });
        return ipcSelectorModalInstance.result;
    };

    // 打开专家库 IPC对话框
    var openIpcDialog = function (defaultValue) {
      var ipcDialogModalInstance = $uibModal.open({
        animation: true,
        templateUrl: CONTEXT_PATH + '/static/tpls/modal/ipcDialog.html',
        controller: 'ExpertIpcCtrl',
        controllerAs: 'expertIpcCtrl',
        size: 'lg',
        resolve: {
          defaultValue: defaultValue
        }
      });
      return ipcDialogModalInstance.result;
    };

    // 打开专家库 国民经济对话框
    var openExpertNationalDlg = function (defaultValue) {
      var natDialogModalInstance = $uibModal.open({
        animation: true,
        templateUrl: CONTEXT_PATH + '/static/tpls/modal/expertNational.html',
        controller: 'ExpertNatCtrl',
        controllerAs: 'expertNatCtrl',
        size: 'lg',
        resolve: {
          defaultValue: defaultValue
        }
      });
      return natDialogModalInstance.result;
    };
    // 打开专家库 八大产业对话框
    var openIndustryDlg = function (defaultValue) {
      var iduDialogModalInstance = $uibModal.open({
        animation: true,
        templateUrl: CONTEXT_PATH + '/static/tpls/modal/industry.html',
        controller: 'ExpertIduCtrl',
        controllerAs: 'expertIduCtrl',
        size: 'lg',
        resolve: {
          defaultValue: defaultValue
        }
      });
      return iduDialogModalInstance.result;
    };

    return {
      openAlert: openAlert,
      openUploader: openUploader,
      openVodUploader: openVodUploader,
      openOrganTree: openOrganTree,
      openChangePasswordEditer: openChangePasswordEditer,
      openMetaEditer: openMetaEditer,
      openMetaViewer: openMetaViewer,
      openLabelEditer: openLabelEditer,
      openLabelViewer: openLabelViewer,
      openRegionDlg: openRegionDlg,
      openNationalDlg: openNationalDlg,
      getAllRegions: getAllRegions,
      getAllNationals: getAllNationals,
      openSpecDlg:openSpecDlg,
      openIpcSelector:openIpcSelector,
			openChangeProfileEditer: openChangeProfileEditer,
      openIpcDialog:openIpcDialog,
      openIndustryDlg:openIndustryDlg,
      openExpertNationalDlg:openExpertNationalDlg
    };
  }

})(angular, CONTEXT_PATH);