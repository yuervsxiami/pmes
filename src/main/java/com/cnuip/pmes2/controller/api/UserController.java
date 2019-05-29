package com.cnuip.pmes2.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.cnuip.pmes2.controller.api.request.ModifyPasswordParam;
import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.domain.core.User;
import com.cnuip.pmes2.exception.AuthException;
import com.cnuip.pmes2.exception.UserException;
import com.cnuip.pmes2.service.UserService;
import com.cnuip.pmes2.util.UserUtil;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.ApiOperation;

/**
* Create By Crixalis:
* 2017年12月29日 下午4:53:29
*/
@RestController
@RequestMapping("/api/user")
public class UserController extends BussinessLogicExceptionHandler{

	@Autowired
	private UserService userService;

	/**
	 * 登录
	 * @param user
	 * @return
	 * @throws UserException 
	 */
	@ApiOperation(value = "登录接口", notes = "传phone和password,提供手机号登录和用户名登录")
	@RequestMapping(value="/login/",method = RequestMethod.POST)
	public ApiResponse<User> login(@RequestBody User user) throws UserException{
		ApiResponse<User> resp = new ApiResponse<>();
		resp.setResult(userService.login(user));
		return resp;
	}

	/**
	 * 添加用户
	 * @param user
	 * @return
	 * @throws UserException 
	 */
	@RequestMapping(value="/add/",method = RequestMethod.POST)
	public ApiResponse<User> add(@RequestBody User user) throws UserException{
		ApiResponse<User> resp = new ApiResponse<>();
		resp.setResult(userService.add(user));
		return resp;
	}
	
	/**
	 * 修改用户
	 * @param user
	 * @return
	 * @throws UserException 
	 */
	@RequestMapping(value="/update/",method = RequestMethod.POST)
	public ApiResponse<User> update(@RequestBody User user) throws UserException{
		ApiResponse<User> resp = new ApiResponse<>();
		resp.setResult(userService.update(user));
		return resp;
	}
	
	/**
	 * 获得个人信息
	 */
	@RequestMapping(value="/profile/",method = RequestMethod.GET)
	public ApiResponse<User> getProfile(Authentication authentication) throws UserException{
		ApiResponse<User> resp = new ApiResponse<>();
		resp.setResult(UserUtil.getUser(authentication));
		return resp;
	}

	/**
	 * 修改用户
	 * @param authentication
	 * @param user
	 * @return
	 * @throws UserException
	 */
	@PostMapping("/profile/update/")
	public ApiResponse<User> updateProfile(Authentication authentication,@RequestBody User user) throws UserException {
		ApiResponse<User> resp = new ApiResponse<>();
		User u = new User();
		u.setId(UserUtil.getUser(authentication).getId());
		u.setEmail(user.getEmail());
		u.setName(user.getName());
		resp.setResult(userService.update(u));
		return resp;
	}
	
	/**
	 * 修改个人信息
	 * @param user
	 * @return
	 * @throws UserException 
	 */
	@RequestMapping(value="/profile/",method = RequestMethod.POST)
	public ApiResponse<User> profile(Authentication authentication,@RequestBody User user) throws UserException{
		ApiResponse<User> resp = new ApiResponse<>();
		user.setId(UserUtil.getUser(authentication).getId());
		resp.setResult(userService.update(user));
		return resp;
	}

	/**
	 * 修改用户密码,根据旧密码修改新密码
	 * @param param
	 * @return
	 * @throws UserException
	 */
	@RequestMapping(value="/modifyPassword/",method = RequestMethod.POST)
	public ApiResponse<String> modifyPassword(@RequestBody ModifyPasswordParam param) throws UserException{
		ApiResponse<String> resp = new ApiResponse<>();
		userService.updatePassword(param);
		resp.setResult("修改密码成功");
		return resp;
	}
	
	/**
	 * 删除用户
	 * @param id
	 * @return
	 * @throws UserException 
	 */
	@RequestMapping(value="/delete/{id}",method = RequestMethod.GET)
	public ApiResponse<String> delete(@PathVariable Long id) throws AuthException{
		ApiResponse<String> resp = new ApiResponse<>();
		userService.updateUserState(id, 0);
		resp.setResult("删除成功");
		return resp;
	}
	
	/**
	 * 恢复用户
	 * @param id
	 * @return
	 * @throws UserException 
	 */
	@RequestMapping(value="/recover/{id}",method = RequestMethod.GET)
	public ApiResponse<String> recover(@PathVariable Long id) throws AuthException{
		ApiResponse<String> resp = new ApiResponse<>();
		userService.updateUserState(id, 1);
		resp.setResult("删除成功");
		return resp;
	}

	/**
	 * 获取用户
	 * @param id
	 * @return
	 * @throws UserException 
	 */
	@RequestMapping(value="/info/{id}",method = RequestMethod.GET)
	public ApiResponse<User> info(@PathVariable Long id) throws UserException{
		ApiResponse<User> resp = new ApiResponse<>();
		resp.setResult(userService.selectUserByPrimaryKey(id));
		return resp;
	}

	/**
	 * 根据角色获取用户
	 * @param id
	 * @return
	 * @throws UserException 
	 */
	@RequestMapping(value="/findByRole/{roleId}",method = RequestMethod.GET)
	public ApiResponse<PageInfo<User>> findByRole(@PathVariable Long roleId,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = "1") int pageNum) throws UserException{
		ApiResponse<PageInfo<User>> resp = new ApiResponse<>();
		resp.setResult(userService.selectByRoleId(roleId, pageNum, pageSize));
		return resp;
	}

	/**
	 * 根据角色获取用户
	 * @param id
	 * @return
	 * @throws UserException 
	 */
	@RequestMapping(value="/all/",method = RequestMethod.GET)
	public ApiResponse<PageInfo<User>> findAll(
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = "1") int pageNum) throws UserException{
		ApiResponse<PageInfo<User>> resp = new ApiResponse<>();
		resp.setResult(userService.selectAll(pageNum, pageSize));
		return resp;
	}
	
	/**
	 * 模糊查询用户
	 * @param pageSize
	 * @param pageNum
	 * @param condition
	 * @return
	 * @throws AuthException
	 */
	@RequestMapping(value = "/search/", method = RequestMethod.GET)
	public ApiResponse<PageInfo<User>> search(
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required=false)String name, 
			@RequestParam(required=false)String email, 
			@RequestParam(required=false)String phone, 
			@RequestParam(required=false)Integer state, 
			@RequestParam(required=false)Long roleId, 
			@RequestParam(required=false)String organizationName) throws AuthException {
		ApiResponse<PageInfo<User>> resp = new ApiResponse<>();
		User user = new User(name, email, phone, state, roleId, organizationName);
		resp.setResult(userService.search(user, pageNum, pageSize));
		return resp;
	}
	
}
