/**
 * Created by Ray丶X on 2017/6/8.
 */
(function (angular) {
  'use strict';
  angular.module('System')
    .controller('RoleCheckController', ['$state', 'ToastService', 'AuthorityService', 'RoleService', RoleCheckCtrl])
  ;

  function RoleCheckCtrl($state, ToastService, AuthorityService, RoleService) {
    var self = this;
    self.loading = false;
    self.roleId = $state.params.id;

    self.nodes = {
      id: '0',
      text: 'PMES2.0',
      icon: 'fa fa-circle-o',
      state: {
        opened: true
      },
    };
    self.checkedNodes = [];

    self.onTreeReady = function (tree, controller) {
      self.tree = tree;
      if (!self.treeCtrl) {
        self.treeCtrl = controller;
        self.getAuthorities();
      }
    };
    
    self.authChildren = function(auth) {
    	if(!auth.sonAuthorities) {
    		return;
    	}
    	return auth.sonAuthorities.map(function (subAuth) {
            return {
              id: subAuth.id,
              name: subAuth.name,
              text: subAuth.title,
              icon: subAuth.pic || 'fa fa-file',
              state: {
                  opened: true
                },
              children: self.authChildren(subAuth)
            };
          });
    }

    self.getAuthorities = function () {
      self.loading = true;
      AuthorityService.getAuthorityTreeDataByRoleId(self.roleId).then(function (result) {
        self.loading = false;
        self.authorities = result.authorities;
        self.roleAuthorities = result.roleAuthorities;

        self.nodes.children = self.authorities.map(function (auth) {
          return {
            id: auth.id,
            name: auth.name,
            text: auth.title,
            state: {
              opened: true
            },
            icon: auth.pic || 'fa fa-folder',
            children: self.authChildren(auth)
          };
        });

        var checkAuth = function (auth) {
					if (auth.sonAuthorities.length === 0) {
						self.checkedNodes.push({id: auth.id});
					} else {
						auth.sonAuthorities.map(checkAuth);
					}
				};

        self.roleAuthorities.map(checkAuth);
        if (self.treeCtrl) {
          self.treeCtrl.refresh();
        }

      }).catch(function (err) {
        self.loading = false;
        ToastService.errHandler(err);
      });
    };

    self.submitAuths = function () {
      if (!self.tree) {
        ToastService.toast({
          type: 'error',
          body: '权限树未创建！'
        });
        return;
      }
      var tree = self.tree.jstree(true);
      var selectedAuths = tree.get_checked();
      if (selectedAuths.length === 0) {
        ToastService.toast({
          type: 'error',
          body: '请至少选择一个权限！'
        });
        return;
      }
      for(var i = 0, j = selectedAuths.length; i < j; i++) {
        var patents = tree.get_node(selectedAuths[i]).parents;
        selectedAuths = selectedAuths.concat(patents);
      }
      selectedAuths = $.vakata.array_unique(selectedAuths.filter(function (auth) {
        return auth !== '0' && auth !== '#';
      }).map(function (auth) {
        return parseInt(auth, 10);
      }));
      // console.info('selectedAuths', angular.copy(selectedAuths));
      self.loading = true;
      RoleService.updateRoleAuth({
        roleId: self.roleId,
        authIds: selectedAuths,
      }).then(function (resp) {
        self.loading = false;
        if (resp && resp.code === 0) {
          ToastService.toast({
            type: 'success',
            body: '修改角色权限成功！'
          });
        } else {
          ToastService.toast({
            type: 'error',
            body: '修改角色权限失败：' + resp.messasge
          });
        }
      }).catch(function (err) {
        self.loading = false;
        ToastService.errHandler(err);
      });
    };
  }

})(angular);