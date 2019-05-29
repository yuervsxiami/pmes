/**
 * Created by Ray丶X on 2017/6/2.
 */
(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('System')
    .service('UserService', ['HttpService', UserService])
    .service('RoleService', ['HttpService', RoleService])
    .service('OrganizationService', ['HttpService', OrganizationService])
    .service('AuthorityService', ['$q', 'HttpService', AuthorityService]);

  function UserService(HttpService) {
    this.getUsers = function (pageNum, pageSize) {
      return HttpService.get(CONTEXT_PATH + '/api/user/all/', {pageNum: pageNum, pageSize: pageSize});
    };
    this.addUser = function (postData) {
      return HttpService.post(CONTEXT_PATH + '/api/user/add/', postData);
    };
    this.updateUser = function (postData) {
      return HttpService.post(CONTEXT_PATH + '/api/user/update/', postData);
    };
    this.deleteUser = function (postData) {
      return HttpService.get(CONTEXT_PATH + '/api/user/delete/' + postData, {});
    };
    this.recoverUser = function (postData) {
    	return HttpService.get(CONTEXT_PATH + '/api/user/recover/' + postData, {});
    };
    this.findUser = function (postData) {
      return HttpService.get(CONTEXT_PATH + '/api/user/search/', postData);
    };
    this.findUserById = function (id) {
      return HttpService.get(CONTEXT_PATH + '/api/user/info/' + id, {});
    };
    this.modifyPassword = function (postData) {
      return HttpService.post(CONTEXT_PATH + '/api/user/modifyPassword/', postData);
    };
    this.modifyProfile = function(user) {
			return HttpService.post(CONTEXT_PATH + '/api/user/profile/update/', user);
    }

  }

  function RoleService(HttpService) {
    this.getRoles = function () {
      return HttpService.get(CONTEXT_PATH + '/api/role/all/', {_random: Math.random()});
    };
    this.getRoleById = function (id) {
      return HttpService.get(CONTEXT_PATH + '/api/role/' + id, {_random: Math.random()});
    };
    this.updateRoleAuth = function (data) {
      return HttpService.post(CONTEXT_PATH + '/api/role/bindAuthorities/', data);
    };
    this.updateRole = function (data) {
      return HttpService.post(CONTEXT_PATH + '/api/user/updateUserRole', data);
    };
  }

  function AuthorityService($q, HttpService) {
    this.getAuthorities = function () {
      return HttpService.get(CONTEXT_PATH + '/api/auth/all/', {_random: Math.random()});
    };
    this.getAuthoritiesByRoleId = function (roleId) {
      return HttpService.get(CONTEXT_PATH + '/api/auth/findByRole/' + roleId, {_random: Math.random()});
    };
    this.getAuthorityTreeDataByRoleId = function (roleId) {
      var defer = $q.defer();
      HttpService.get(CONTEXT_PATH + '/api/auth/all/', {_random: Math.random()}).then(function (resp) {
        if (resp && resp.code === 0) {
          var allAuths = resp.result;
          HttpService.get(CONTEXT_PATH + '/api/auth/findByRole/' + roleId, {_random: Math.random()}).then(function (resp) {
            if (resp && resp.code === 0) {
              var roleAuths = resp.result;
              defer.resolve({
                authorities: allAuths,
                roleAuthorities: roleAuths
              });
            } else {
              var err = new Error(resp.message);
              err.code = resp.code;
              defer.reject(err);
            }
          });
        } else {
          var err = new Error(resp.message);
          err.code = resp.code;
          defer.reject(err);
        }
      }).catch(defer.reject);
      return defer.promise;
    };
    this.getAuthorityById = function (id) {
      return HttpService.get(CONTEXT_PATH + '/api/auth/' + id, {_random: Math.random()});
    };
    this.addAuthority = function (auth) {
      return HttpService.post(CONTEXT_PATH + '/api/auth/add/', auth);
    };
    this.updateAuthority = function (auth) {
      return HttpService.post(CONTEXT_PATH + '/api/auth/update/', auth);
    };
    this.deleteAuthorityById = function (id) {
      return HttpService.get(CONTEXT_PATH + '/api/auth/delete/' + id, {_random: Math.random()});
    };
  }

  function OrganizationService(HttpService) {
    this.getOrganizations = function () {
      return HttpService.get(CONTEXT_PATH + '/api/organization/all/', {_random: Math.random()});
    };
    this.getOrganizationById = function (id) {
        return HttpService.get(CONTEXT_PATH + '/api/organization/' + id, {_random: Math.random()});
    }
    this.addOrganization = function (organization) {
        return HttpService.post(CONTEXT_PATH + '/api/organization/add/', organization);
    };
    this.updateOrganization = function (organization) {
    	return HttpService.post(CONTEXT_PATH + '/api/organization/update/', organization);
    };
    this.deleteOrganizationById = function (id) {
        return HttpService.get(CONTEXT_PATH + '/api/organization/delete/' + id, {_random: Math.random()});
    };
  }

})(angular, CONTEXT_PATH);