package com.cnuip.pmes2.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.domain.core.Authority;
import com.cnuip.pmes2.exception.AuthException;
import com.cnuip.pmes2.service.AuthorityService;

/**
* Create By Crixalis:
* 2017年12月28日 下午3:34:47
*/
@RestController
@RequestMapping("/api/auth")
public class AuthorityController extends BussinessLogicExceptionHandler{
	
	@Autowired
	private AuthorityService authorityService;
	
	/**
	 * 根据id获取权限
	 * @param authority
	 * @return
	 */
	@RequestMapping(value="/{id}",method = RequestMethod.GET)
	public ApiResponse<Authority> get(@PathVariable Long id){
		ApiResponse<Authority> resp = new ApiResponse<>();
		resp.setResult(authorityService.selectByPrimaryKey(id));
		return resp;
	}
	
	/**
	 * 添加权限
	 * @param authority
	 * @return
	 */
	@RequestMapping(value="/add/",method = RequestMethod.POST)
	public ApiResponse<Authority> add(@RequestBody Authority authority){
		ApiResponse<Authority> resp = new ApiResponse<>();
		resp.setResult(authorityService.add(authority));
		return resp;
	}
	
	/**
	 * 修改权限
	 * @param authority
	 * @return
	 */
	@RequestMapping(value="/update/",method = RequestMethod.POST)
	public ApiResponse<Authority> update(@RequestBody Authority authority){
		ApiResponse<Authority> resp = new ApiResponse<>();
		resp.setResult(authorityService.update(authority));
		return resp;
	}
	
	/**
	 * 删除权限,只能从子权限一级一级删除
	 * @param id
	 * @return
	 * @throws AuthException 
	 */
	@RequestMapping(value="/delete/{id}",method = RequestMethod.GET)
	public ApiResponse<String> delete(@PathVariable Long id) throws AuthException{
		ApiResponse<String> resp = new ApiResponse<>();
		authorityService.delete(id);
		resp.setResult("删除成功");
		return resp;
	}
	
	/**
	 * 根据角色查找权限,以树状格式返回权限
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value="/findByRole/{roleId}",method = RequestMethod.GET)
	public ApiResponse<List<Authority>> findByRoleId(@PathVariable Long roleId){
		ApiResponse<List<Authority>> resp = new ApiResponse<>();
		resp.setResult(authorityService.selectByRoleId(roleId));
		return resp;
	}
	
	/**
	 * 查找所有权限,以树状格式返回权限
	 * @return
	 */
	@RequestMapping(value="/all/",method = RequestMethod.GET)
	public ApiResponse<List<Authority>> findAll(){
		ApiResponse<List<Authority>> resp = new ApiResponse<>();
		resp.setResult(authorityService.selectAll(0, 0));
		return resp;
	}
	
}	
