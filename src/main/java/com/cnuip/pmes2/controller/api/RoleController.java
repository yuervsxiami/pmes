package com.cnuip.pmes2.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cnuip.pmes2.controller.api.request.RoleAuthority;
import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.domain.core.Authority;
import com.cnuip.pmes2.domain.core.Role;
import com.cnuip.pmes2.exception.AuthException;import com.cnuip.pmes2.repository.core.RoleMapper;
import com.cnuip.pmes2.service.RoleService;

/**
* Create By Crixalis:
* 2017年12月29日 上午10:49:15
*/
@RestController
@RequestMapping("/api/role")
public class RoleController extends BussinessLogicExceptionHandler {

	@Autowired
	private RoleService roleService;
	
	/**
	 * 获取指定角色
	 */
	@RequestMapping(value="/{id}",method = RequestMethod.GET)
	public ApiResponse<Role> get(@PathVariable Long id){
		ApiResponse<Role> resp = new ApiResponse<>();
		resp.setResult(roleService.selectByPrimaryKey(id));
		return resp;
	}
	
	/**
	 * 添加角色
	 * @param role
	 * @return
	 */
	@RequestMapping(value="/add/",method = RequestMethod.POST)
	public ApiResponse<Role> add(@RequestBody Role role){
		ApiResponse<Role> resp = new ApiResponse<>();
		resp.setResult(roleService.add(role));
		return resp;
	}
	
	/**
	 * 修改角色
	 * @param role
	 * @return
	 */
	@RequestMapping(value="/update/",method = RequestMethod.POST)
	public ApiResponse<Role> update(@RequestBody Role role){
		ApiResponse<Role> resp = new ApiResponse<>();
		resp.setResult(roleService.update(role));
		return resp;
	}
	
	/**
	 * 删除角色
	 * @param id
	 * @return
	 */
//	@RequestMapping(value="/delete/{id}",method = RequestMethod.GET)
	public ApiResponse<String> delete(@PathVariable Long id){
		ApiResponse<String> resp = new ApiResponse<>();
		roleService.delete(id);
		resp.setResult("删除成功");
		return resp;
	}
	
	/**
	 * 把角色与权限进行绑定,传角色id和权限id的集合
	 * @param roleAuthority
	 * @return
	 * @throws AuthException
	 */
	@RequestMapping(value="/bindAuthorities/",method = RequestMethod.POST)
	public ApiResponse<List<Authority>> bindAuthorities(@RequestBody RoleAuthority roleAuthority) throws AuthException {
		ApiResponse<List<Authority>> resp = new ApiResponse<>();
		resp.setResult(roleService.bindRoleAuthorities(roleAuthority));
		return resp;
	}
	
	/**
	 * 获得所有角色
	 * @param roleAuthority
	 * @return
	 * @throws AuthException
	 */
	@RequestMapping(value="/all/",method = RequestMethod.GET)
	public ApiResponse<List<Role>> getAll() throws AuthException {
		ApiResponse<List<Role>> resp = new ApiResponse<>();
		resp.setResult(roleService.selectAll(0, 0));
		return resp;
	}
	
}
