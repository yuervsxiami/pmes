package com.cnuip.pmes2.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.domain.core.Organization;
import com.cnuip.pmes2.exception.AuthException;
import com.cnuip.pmes2.exception.OrganizationException;
import com.cnuip.pmes2.service.OrganizationService;

/**
* Create By Crixalis:
* 2017年12月29日 上午10:15:31
*/
@RestController
@RequestMapping("/api/organization")
public class OrganizationController extends BussinessLogicExceptionHandler{

	@Autowired
	private OrganizationService organizationService;
	
	/**
	 * 根据id获取组织机构
	 * @param organization
	 * @return
	 */
	@RequestMapping(value="/{id}",method = RequestMethod.GET)
	public ApiResponse<Organization> get(@PathVariable Long id){
		ApiResponse<Organization> resp = new ApiResponse<>();
		resp.setResult(organizationService.selectByPrimaryKey(id));
		return resp;
	}
	
	/**
	 * 添加组织机构
	 * @param organization
	 * @return
	 */
	@RequestMapping(value="/add/",method = RequestMethod.POST)
	public ApiResponse<Organization> add(@RequestBody Organization organization){
		ApiResponse<Organization> resp = new ApiResponse<>();
		resp.setResult(organizationService.add(organization));
		return resp;
	}
	
	/**
	 * 修改组织机构
	 * @param organization
	 * @return
	 */
	@RequestMapping(value="/update/",method = RequestMethod.POST)
	public ApiResponse<Organization> update(@RequestBody Organization organization){
		ApiResponse<Organization> resp = new ApiResponse<>();
		resp.setResult(organizationService.update(organization));
		return resp;
	}
	
	/**
	 * 删除组织机构,只能从子组织机构一级一级删除
	 * @param id
	 * @return
	 * @throws AuthException 
	 * @throws OrganizationException 
	 */
	@RequestMapping(value="/delete/{id}",method = RequestMethod.GET)
	public ApiResponse<String> delete(@PathVariable Long id) throws OrganizationException{
		ApiResponse<String> resp = new ApiResponse<>();
		organizationService.delete(id);
		resp.setResult("删除成功");
		return resp;
	}
	
	/**
	 * 查找所有组织机构,以树状结构返回组织机构
	 * @return
	 */
	@RequestMapping(value="/all/",method = RequestMethod.GET)
	public ApiResponse<List<Organization>> findAll(){
		ApiResponse<List<Organization>> resp = new ApiResponse<>();
		resp.setResult(organizationService.selectAll(0, 0));
		return resp;
	}
	
}
