package com.cnuip.pmes2.controller.api;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cnuip.pmes2.controller.api.request.LabelSearchCondition;
import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.domain.core.Label;
import com.cnuip.pmes2.domain.core.User;
import com.cnuip.pmes2.exception.LabelException;
import com.cnuip.pmes2.service.LabelService;
import com.cnuip.pmes2.util.UserUtil;
import com.github.pagehelper.PageInfo;

/**
* Create By Crixalis:
* 2018年1月4日 下午4:27:02
*/
@RestController
@RequestMapping("/api/label")
public class LabelController extends BussinessLogicExceptionHandler{

	@Autowired
	private LabelService labelService;
	
	/**
	 * 根据key获得标签
	 * @param key
	 * @return
	 */
	@RequestMapping(value="/get/{key}",method = RequestMethod.GET)
	public ApiResponse<Label> get(@PathVariable String key) {
		ApiResponse<Label> resp = new ApiResponse<>();
		resp.setResult(labelService.selectByKey(key));
		return resp;
	}
	
	/**
	 * 根据id获得标签
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/{id}",method = RequestMethod.GET)
	public ApiResponse<Label> getByPrimaryKey(@PathVariable Long id) {
		ApiResponse<Label> resp = new ApiResponse<>();
		resp.setResult(labelService.selectByPrimaryKey(id));
		return resp;
	}
	
	/**
	 * 根据type获得标签,如果type为0,则获取全部
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/findByType/{type}",method = RequestMethod.GET)
	public ApiResponse<List<Label>> getByType(@PathVariable Integer type,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = "1") int pageNum) {
		ApiResponse<List<Label>> resp = new ApiResponse<>();
		resp.setResult(labelService.selectByType(type, pageNum, pageSize));
		return resp;
	}
	
	/**
	 * 根据搜索条件搜索列表
	 * @return
	 */
	@RequestMapping(value="/search/",method = RequestMethod.GET)
	public ApiResponse<PageInfo<Label>> search(
			@RequestParam(required = false) Integer type,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) Integer state,
			@RequestParam(required = false) Integer indexType,
			@RequestParam(required = false) Long source,
			@RequestParam(required = false) String username,
			@RequestParam(required = false) Date fromTime,
			@RequestParam(required = false) Date toTime,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = "1") int pageNum) {
		ApiResponse<PageInfo<Label>> resp = new ApiResponse<>();
		LabelSearchCondition condition = new LabelSearchCondition(type, name, state, indexType, source, username, fromTime, toTime);
		resp.setResult(labelService.search(condition, pageNum, pageSize));
		return resp;
	}
	
	/**
	 * 添加标签
	 * @param id
	 * @return
	 * @throws LabelException 
	 */
	@RequestMapping(value="/add/",method = RequestMethod.POST)
	public ApiResponse<Label> add(Authentication authentication, @RequestBody Label label) throws LabelException {
		ApiResponse<Label> resp = new ApiResponse<>();
		Long userId = UserUtil.getUser(authentication).getId();
		label.setUserId(userId);
		resp.setResult(labelService.add(label));
		return resp;
	}
	
	/**
	 * 修改标签
	 * @param id
	 * @return
	 * @throws LabelException 
	 */
	@RequestMapping(value="/update/",method = RequestMethod.POST)
	public ApiResponse<Label> update(Authentication authentication, @RequestBody Label label) throws LabelException {
		ApiResponse<Label> resp = new ApiResponse<>();
		Long userId = UserUtil.getUser(authentication).getId();
		label.setUserId(userId);
		resp.setResult(labelService.update(label));
		return resp;
	}
	
	/**
	 * 改变标签状态0为禁用,1为启用
	 */
	@RequestMapping(value="/changeState/",method = RequestMethod.POST)
	public ApiResponse<String> changState(@RequestBody Label label) throws LabelException {
		ApiResponse<String> resp = new ApiResponse<>();
		labelService.changeState(label);
		resp.setResult("修改成功");
		return resp;
	}
	
}
