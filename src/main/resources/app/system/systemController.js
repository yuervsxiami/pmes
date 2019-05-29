/**
 * Created by xiongwei on 2017/2/6.
 */

(function (angular) {
	'use strict';
	angular.module('System')
			.controller('RoleCtrl', ['RoleService', 'ToastService', '$scope', RoleCtrl])
			.controller('UserCtrl', ['$log', '$scope', 'ModalService', 'UserService', 'ToastService', 'RoleService', UserCtrl])
			.controller('AuthorityCtrl', ['AuthorityService', 'ToastService', 'ModalService', '$scope', AuthorityCtrl])
			.controller('OrganizationCtrl', ['OrganizationService', 'ToastService', 'ModalService', '$scope', OrganizationCtrl])
	;

	function RoleCtrl(RoleService, ToastService, $scope) {
		var self = this;

		$scope.$on('$viewContentLoaded', function (event) {
			$.AdminLTE.layout.fix();
		});

		self.getAllRole = function () {
			self.loading = true;
			RoleService.getRoles().then(function (resp) {
				self.loading = false;
				if (resp && resp.code === 0) {
					self.list = resp.result;
				} else {
					ToastService.toast({
						type: 'error',
						body: '查询角色失败：' + resp.message
					});
				}
			}).catch(function (err) {
				self.loading = false;
				ToastService.toast({
					type: "error",
					body: err.message
				});
			});
		};
		self.getAllRole();
	}

	function UserCtrl($log, $scope, ModalService, UserService, ToastService, RoleService) {
		var self = this;

		$scope.$on('$viewContentLoaded', function (event) {
			$.AdminLTE.layout.fix();
		});

		//分页配置
		self.searching = false;

		//搜索用户
		self.findUser = function () {
			self.searchCondition.pageSize = self.paginationConf.itemsPerPage;
			self.searchCondition.pageNum = self.paginationConf.currentPage;

			self.loading = true;
			UserService.findUser(self.searchCondition).then(function (resp) {
				if (resp && resp.code === 0) {
					self.paginationConf.totalItems = resp.result.total;
					self.loading = false;
					self.list = resp.result.list;
					self.searching = false;
				} else {
					ToastService.toast({
						type: "error",
						body: '搜索用户失败'
					});
				}
			}).catch(function (err) {
				ToastService.toast({
					type: "error",
					body: err.message
				});
			});
		};

		self.getAllRole = function () {
			RoleService.getRoles().then(function (resp) {
				if (resp && resp.code === 0) {
					self.roles = resp.result;
				} else {
					ToastService.toast({
						type: 'error',
						body: '查询角色失败：' + resp.message
					});
				}
			});
		};

		//禁用
		self.remove = function (id) {
			ModalService.openAlert({
				title: '提示',
				message: '您是否要禁用该用户?'
			}).then(function (code) {
				if (code === 0) {
					UserService.deleteUser(id).then(function (resp) {
						if (resp && resp.code === 0) {
							self.findUser();
							ToastService.toast({
								type: 'success',
								body: "已成功禁用用户"
							});
						}
					}).catch(function (err) {
						ToastService.toast({
							type: 'error',
							body: err.message
						});
					});
				}
			}).catch(function (err) {
				$log.info(err);
			});
		};
		//启用
		self.recover = function (id) {
			UserService.recoverUser(id).then(function (resp) {
				if (resp && resp.code === 0) {
					self.findUser();
					ToastService.toast({
						type: 'success',
						body: "已成功启用用户"
					});
				}
			}).catch(function (err) {
				ToastService.toast({
					type: 'error',
					body: err.message
				});
			});
		};

		self.init = function () {
			//配置分页基本参数
			self.paginationConf = {
				currentPage: 1,
				itemsPerPage: 10,
			};
			self.searchCondition = {accountNonExpired: true};
			$scope.$watch(function () {
				return self.paginationConf.itemsPerPage + " " + self.paginationConf.currentPage;
			}, self.findUser);

			self.getAllRole();

		};

		self.reset = function () {
			self.searchCondition = {accountNonExpired: true};
		};

		self.init();

	}

	function AuthorityCtrl(AuthorityService, ToastService, ModalService, $scope) {
		var self = this;

		$scope.$on('$viewContentLoaded', function (event) {
			$.AdminLTE.layout.fix();
		});

		self.nodes = {
			id: '0',
			text: 'PMES',
			icon: 'fa fa-home',
			state: {
				opened: true
			},
		};

		self.isButtonList = [{key: "否", value: false}, {key: "是", value: true}];

		self.onTreeReady = function (tree, controller) {
			if (!self.treeCtrl) {
				self.treeCtrl = controller;
				self.getAuthorities();
			}
		};

		self.authChildren = function (auth) {
			if (!auth.sonAuthorities) {
				return;
			}
			return auth.sonAuthorities.map(function (subAuth) {
				return {
					id: subAuth.id,
					name: subAuth.name,
					text: subAuth.title,
					icon: subAuth.pic || 'fa  fa-circle-o',
					state: {
						opened: true
					},
					children: self.authChildren(subAuth)
				};
			});
		}

		self.onNodeSelected = function (authId) {
			self.loading = true;
			AuthorityService.getAuthorityById(authId).then(function (resp) {
				self.loading = false;
				if (resp && resp.code === 0) {
					self.selectedAuthority = resp.result;
				} else {
					ToastService.toast({
						type: 'error',
						body: '请求权限失败：' + resp.message
					});
				}
			}).catch(function (err) {
				self.loading = false;
				ToastService.errHandler(err);
			});
		};

		self.getAuthorities = function () {
			self.loading = true;
			AuthorityService.getAuthorities().then(function (resp) {
				self.loading = false;
				if (resp && resp.code === 0) {
					self.auths = resp.result;
					self.nodes.children = resp.result.map(function (auth) {
						return {
							id: auth.id,
							name: auth.name,
							text: auth.title,
							state: {
								opened: true
							},
							icon: auth.pic || 'fa fa-files-o',
							children: self.authChildren(auth)
						};
					});
					if (self.treeCtrl) {
						self.treeCtrl.refresh();
					}
				} else {
					ToastService.toast({
						type: 'error',
						body: '请求权限失败：' + resp.message
					});
				}
			}).catch(function (err) {
				self.loading = false;
				ToastService.errHandler(err);
			});
		};

		/**
		 * 添加权限
		 */
		self.addAuthority = function () {
			if (!self.selectedAuthority) {
				self.selectedAuthority = {};
			}
			if (self.selectedAuthority && self.selectedAuthority.id) {
				self.selectedAuthority.parentId = self.selectedAuthority.id;
				self.selectedAuthority.id = 0;
				delete self.selectedAuthority.id;
				delete self.selectedAuthority.name;
				delete self.selectedAuthority.title;
				delete self.selectedAuthority.url;
				delete self.selectedAuthority.pic;
				delete self.selectedAuthority.sortOrder;
				self.selectedAuthority.isMenu = false;
			}
		};

		/**
		 * 删除权限
		 * @param id
		 */
		self.deleteAuthority = function (id) {
			AuthorityService.deleteAuthorityById(id).then(function (resp) {
				self.loading = false;
				if (resp && resp.code === 0) {
					ToastService.toast({
						type: 'success',
						body: '删除权限成功！'
					});
					self.selectedAuthority = null;
					self.getAuthorities();
				} else {
					ToastService.toast({
						type: 'error',
						body: '删除权限失败：' + resp.message
					});
				}
			}).catch(function (err) {
				self.loading = false;
				ToastService.errHandler(err);
			});
		};

		/**
		 * 新增或修改权限
		 */
		self.submitAuth = function () {
			var p = null;
			self.loading = true;
			if (self.selectedAuthority.id > 0) {
				p = AuthorityService.updateAuthority(self.selectedAuthority);
			} else {
				p = AuthorityService.addAuthority(self.selectedAuthority);
			}
			p.then(function (resp) {
				self.loading = false;
				//$log.info(resp);
				if (resp && resp.code === 0) {
					ToastService.toast({
						type: 'success',
						body: self.selectedAuthority.id > 0 ? '修改权限成功！' : '添加权限成功！'
					});
					if (self.selectedAuthority.id === 0) {
						self.selectedAuthority = null;
					}
					self.getAuthorities();
				} else {
					ToastService.toast({
						type: 'error',
						body: (self.selectedAuthority.id > 0 ? '修改权限失败：' : '添加权限失败：') + resp.message
					});
				}
			}).catch(function (err) {
				self.loading = false;
				ToastService.errHandler(err);
			});
		};
		/**
		 * 删除权限
		 */
		self.deleteAuth = function () {
			ModalService.openAlert({
				title: '提示',
				message: '您是否要删除该权限?'
			}).then(function (code) {
				if (code === 0) {
					self.deleteAuthority(self.selectedAuthority.id);
				}
			}, function () {
				// none
			});
		};
	}

	function OrganizationCtrl(OrganizationService, ToastService, ModalService, $scope) {
		var self = this;

		$scope.$on('$viewContentLoaded', function (event) {
			$.AdminLTE.layout.fix();
		});

		self.loading = false;
		self.nodes = {
			id: '0',
			text: 'PMES',
			icon: 'fa fa-home',
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

		self.organizationChildren = function (organization) {
			if (!organization.sonOrganizations) {
				return;
			}
			return organization.sonOrganizations.map(function (subOrganization) {
				return {
					id: subOrganization.id,
					name: subOrganization.remark,
					text: subOrganization.name,
					icon: subOrganization.pic || 'fa fa-circle-o',
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
						body: '请求组织机构失败：' + resp.message
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
							icon: organization.pic || 'fa fa-files-o',
							children: self.organizationChildren(organization)
						};
					});
					if (self.treeCtrl) {
						self.treeCtrl.refresh();
					}
				} else {
					ToastService.toast({
						type: 'error',
						body: '请求组织机构失败：' + resp.message
					});
				}
			}).catch(function (err) {
				self.loading = false;
				ToastService.errHandler(err);
			});
		};

		/**
		 * 添加组织机构
		 */
		self.addOrganization = function () {
			if (!self.selectedOrganization) {
				self.selectedOrganization = {};
			}
			if (self.selectedOrganization && self.selectedOrganization.id) {
				self.selectedOrganization.parentId = self.selectedOrganization.id;
				self.selectedOrganization.id = 0;
				delete self.selectedOrganization.id;
				delete self.selectedOrganization.name;
				delete self.selectedOrganization.remark;
			}
		};

		/**
		 * 删除组织机构
		 * @param id
		 */
		self.deleteOrganization = function (id) {
			self.loading = true;
			OrganizationService.deleteOrganizationById(id).then(function (resp) {
				self.loading = false;
				if (resp && resp.code === 0) {
					ToastService.toast({
						type: 'success',
						body: '删除组织机构成功！'
					});
					self.selectedOrganization = null;
					self.getOrganizations();
				} else {
					ToastService.toast({
						type: 'error',
						body: '删除组织机构失败：' + resp.message
					});
				}
			}).catch(function (err) {
				self.loading = false;
				ToastService.errHandler(err);
			});
		};

		/**
		 * 新增或修改权限
		 */
		self.submitOrganization = function () {
			var p = null;
			if (self.selectedOrganization.id > 0) {
				p = OrganizationService.updateOrganization(self.selectedOrganization);
			} else {
				p = OrganizationService.addOrganization(self.selectedOrganization);
			}
			p.then(function (resp) {
				self.loading = false;
				//$log.info(resp);
				if (resp && resp.code === 0) {
					ToastService.toast({
						type: 'success',
						body: self.selectedOrganization.id > 0 ? '修改组织机构成功！' : '添加组织机构成功！'
					});
					if (self.selectedOrganization.id === 0) {
						self.selectedOrganization = null;
					}
					self.getOrganizations();
				} else {
					ToastService.toast({
						type: 'error',
						body: (self.selectedOrganization.id > 0 ? '修改组织机构失败：' : '添加组织机构失败：') + resp.message
					});
				}
			}).catch(function (err) {
				self.loading = false;
				ToastService.errHandler(err);
			});
		};

		/**
		 * 删除组织机构
		 */
		self.deleteOrgan = function () {
			ModalService.openAlert({
				title: '提示',
				message: '您是否要删除该组织机构?'
			}).then(function (code) {
				if (code === 0) {
					self.deleteOrganization(self.selectedOrganization.id);
				}
			}, function () {
				// none
			});
		};
	}

})(angular);