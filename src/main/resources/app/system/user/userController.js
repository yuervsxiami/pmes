(function (angular	) {
  'use strict';
  angular.module('System')
    .controller('UserEditController', ['$log', '$state', 'ModalService', 'UserService', 'ToastService', 'RoleService', 'AuthService', 'OrganizationService', '$scope', UserEditCtrl])
    .controller('ChangePasswordEditerCtrl', ['$scope', '$uibModalInstance', 'ToastService', 'HttpService', '$log', 'UserService', 'ModalService', 'AuthService', ChangePasswordEditerCtrl])
    .controller('ChangeProfileEditerCtrl', ['$scope', '$uibModalInstance', 'ToastService', 'HttpService', '$log', 'UserService', 'ModalService', 'AuthService', ChangeProfileEditerCtrl])
  ;

  function UserEditCtrl($log, $state, ModalService, UserService, ToastService, RoleService, AuthService, OrganizationService, $scope) {

		$scope.$on('$viewContentLoaded', function (event) {
			$.AdminLTE.layout.fix();
		});

    var self = this;
//    self.loginUser = AuthService.getLoginUser();
    self.editId = $state.params.id;
    self.findUser = function () {
      if (!self.editId) {
        return;
      }
      self.loading = true;
      UserService.findUserById(self.editId).then(function (resp) {
        self.loading = false;
        if (resp.code === 0) {
          self.user = resp.result;
          self.roleId = self.user.roleId;
          self.organizationId = self.user.organizationId;
          self.organization = self.user.organization;
          self.name = self.user.name;
          self.username = self.user.username;
          self.phone = self.user.phone;
          self.email = self.user.email;
          $log.info(self.user);
        } else {
          ToastService.toast({
            type: 'error',
            body: '查询用户出错: ' + resp.message
          });
        }
      });
    };
    self.findAllRoles = function () {
      RoleService.getRoles().then(function (resp) {
        if (resp.code === 0) {
          self.roles = resp.result;
        } else {
          ToastService.toast({
            type: "error",
            message: resp.detailMessage
          });
        }
      });
    };
    self.findAllOrganizations = function () {
    	OrganizationService.getOrganizations().then(function (resp) {
          if (resp.code === 0) {
            self.organizations = resp.result;
          } else {
            ToastService.toast({
              type: "error",
              message: resp.detailMessage
            });
          }
        });
      };
    self.findUser();
    self.findAllRoles();
    self.findAllOrganizations();
    
    self.hasSelected = function (param) {
    	if(param) {
    		return true;
    	}
    	return false;
    }; 

    self.editUser = function () {
      var data = {
        formData: {
          "id": self.editId,
          "name": self.name,
          "username": self.username,
          "phone": self.phone,
          "email": self.email,
          "roleId": self.roleId,
          "organizationId": self.organization.id,
        }
      };
      if (!data.formData.username || !data.formData.phone) {
        ToastService.toast({
          type: 'info',
          body: "输入用户信息格式不正确"
        });
        return;
      }
      if(!self.organization) {
	    ToastService.toast({
          type: 'info',
          body: "您未选择组织机构"
        });
        return;
      }
      ModalService.openAlert({
        title: '提示',
        message: '您是否要提交?'
      }).then(function (code) {
        if (code === 0) {
          UserService.updateUser(data.formData).then(function (resp) {
            if (resp.code === 0) {
              ToastService.toast({
                type: 'success',
                body: '已完成编辑用户'
              });
              $state.go("main.console.system.users");
            } else {
              ToastService.toast({
                type: 'error',
                body: resp.detailMessage
              });
            }
          });
        }
      }).catch(function (err) {
        $log.info(err);
      });
    };
    self.addUser = function () {
      var data = {
        formData: {
            "name": self.name,
            "username": self.username,
            "password": self.password,
            "phone": self.phone,
            "email": self.email,
            "roleId": self.roleId,
            "organizationId": self.organization.id,
        }
      };
      if (!data.formData.username || !data.formData.phone) {
        ToastService.toast({
          type: 'info',
          body: "输入用户信息格式不正确"
        });
        return;
      }
      if(!self.organization) {
  	    ToastService.toast({
            type: 'info',
            body: "您未选择组织机构"
          });
          return;
        }
      ModalService.openAlert({
        title: '提示',
        message: '您是否要提交?'
      }).then(function (code) {
        if (code === 0) {
          UserService.addUser(data.formData).then(function (resp) {
            if (resp.code === 0) {
              ToastService.toast({
                type: 'success',
                body: '已成功添加用户'
              });
              $state.go("main.console.system.users");
            } else {
              ToastService.toast({
                type: 'error',
                body: resp.detailMessage
              });
            }
          }).catch(function (err) {
            $log.info(err);
          });
        }
      }).catch(function (err) {
        $log.info(err);
      });
    };
    self.close = function () {
      $state.go("main.console.system.users");
    };
    
    self.openOrganTree = function () {
    	ModalService.openOrganTree().then(function(organization){
    		self.organization = organization;
    	}).catch(function (err) {
            $log.info(err);
        });
    };

  }

  function ChangeProfileEditerCtrl($scope, $uibModalInstance, ToastService, HttpService, $log, UserService, ModalService, AuthService) {
		var self = this;

		self.init = function () {
      self.title = "编辑个人信息";
      self.verifyPassword = "";
      AuthService.getLoginUser().then(function (loginUser) {
        self.user = loginUser;
      }).catch(ToastService.errHandler);
    };

		self.cancel = function () {
			$uibModalInstance.dismiss('cancel');
		};

		self.ok = function () {
			UserService.modifyProfile(self.user).then(function(resp) {
				if (resp && resp.code === 0) {
					ToastService.toast({
						type: 'success',
						body: "编辑个人信息成功"
					});
					$uibModalInstance.close(0);
				}
			}).catch(ToastService.errHandler);
		};

		self.init();

	}

  function ChangePasswordEditerCtrl($scope, $uibModalInstance, ToastService, HttpService, $log, UserService, ModalService, AuthService) {
    var self = this;

    self.init = function () {
      self.title = "修改密码";
      self.passwordParam = {userId: "", oldPassword: "", newPassword: ""};
      self.verifyPassword = "";
      AuthService.getLoginUser().then(function (loginUser) {
        self.passwordParam.userId = loginUser.id;
      }).catch(ToastService.errHandler);
    };

    self.checkOldPassword = function (form) {
      if (form.inputOldPassword.$dirty && form.inputOldPassword.$error.required) {
        return {hasError: true, msg: '原密码不能为空！'};
      }
      else {
        return {hasError: false, msg: ''};
      }
    }

    self.checkNewPassword = function (form) {
      if (form.inputNewPassword.$dirty && form.inputNewPassword.$error.required) {
        return {hasError: true, msg: '新密码不能为空！'};
      }
      else if (form.inputNewPassword.$dirty && self.passwordParam.oldPassword == self.passwordParam.newPassword) {
        return {hasError: true, msg: '新密码和原密码一样！'};
      }
      else {
        return {hasError: false, msg: ''};
      }
    }

    self.checkVerifyPassword = function (form) {
      if (form.inputVerifyPassword.$dirty && form.inputVerifyPassword.$error.required) {
        return {hasError: true, msg: '新密码校验不能为空！'};
      }
      else if (form.inputVerifyPassword.$dirty && self.verifyPassword != self.passwordParam.newPassword) {
        return {hasError: true, msg: '两次录入的新密码不一样！'};
      }
      else {
        return {hasError: false, msg: ''};
      }
    }

    self.canSave = function (form) {
      if (self.passwordParam.oldPassword == "" || self.passwordParam.newPassword == "" || self.verifyPassword == "") {
        return true;
      }
      else {
        return self.checkOldPassword(form).hasError || self.checkNewPassword(form).hasError || self.checkVerifyPassword(form).hasError;
      }
    }

    self.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };

    self.ok = function () {
      UserService.modifyPassword(self.passwordParam).then(function(resp) {
        if (resp && resp.code === 0) {
          ToastService.toast({
            type: 'success',
            body: resp.result
          });
          $uibModalInstance.close(0);
        }
      }).catch(ToastService.errHandler);
    };

    self.init();

  };

})(angular);