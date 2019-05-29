package com.cnuip.pmes2.controller.api;

import com.cnuip.pmes2.controller.api.request.MetaSearchCondition;
import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.domain.core.Meta;
import com.cnuip.pmes2.domain.core.MetaValue;
import com.cnuip.pmes2.exception.MetaException;
import com.cnuip.pmes2.service.MetaService;
import com.cnuip.pmes2.util.UserUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Create By Crixalis:
 * 2018年1月4日 下午4:27:02
 */
@RestController
@RequestMapping("/api/meta")
public class MetaController extends BussinessLogicExceptionHandler {

    @Autowired
    private MetaService metaService;

    /**
     * 根据key获得元数据
     *
     * @param key
     * @return
     */
    @RequestMapping(value = "/get/{key}", method = RequestMethod.GET)
    public ApiResponse<Meta> get(@PathVariable String key) {
        ApiResponse<Meta> resp = new ApiResponse<>();
        resp.setResult(metaService.selectByKey(key));
        return resp;
    }

    /**
     * 根据id获得元数据
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApiResponse<Meta> getByPrimaryKey(@PathVariable Long id) {
        ApiResponse<Meta> resp = new ApiResponse<>();
        resp.setResult(metaService.selectByPrimaryKey(id));
        return resp;
    }

    /**
     * 则获取全部元数据
     *
     * @param pageSize
     * @param pageNum
     * @return
     */
    @RequestMapping(value = "/all/", method = RequestMethod.GET)
    public ApiResponse<List<Meta>> getAll() {
        ApiResponse<List<Meta>> resp = new ApiResponse<>();
        resp.setResult(metaService.selectAll());
        return resp;
    }


    /**
     * 根据type获得元数据,如果type为0,则获取全部
     *
     * @param type
     * @param pageSize
     * @param pageNum
     * @return
     */
    @RequestMapping(value = "/findByType/{type}", method = RequestMethod.GET)
    public ApiResponse<PageInfo<Meta>> getByType(@PathVariable Integer type,
                                                 @RequestParam(required = false, defaultValue = "10") int pageSize,
                                                 @RequestParam(required = false, defaultValue = "1") int pageNum) {
        ApiResponse<PageInfo<Meta>> resp = new ApiResponse<>();
        resp.setResult(metaService.selectByType(type, pageNum, pageSize));
        return resp;
    }

    /**
     * 根据搜索条件搜索列表
     *
     * @return
     */
    @RequestMapping(value = "/search/", method = RequestMethod.GET)
    public ApiResponse<PageInfo<Meta>> search(
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer state,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Date fromTime,
            @RequestParam(required = false) Date toTime,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "1") int pageNum) {
        ApiResponse<PageInfo<Meta>> resp = new ApiResponse<>();
        MetaSearchCondition condition = new MetaSearchCondition(type, name, state, username, fromTime, toTime);
        resp.setResult(metaService.search(condition, pageNum, pageSize));
        return resp;
    }

    /**
     * 根据元数据key获取key下的所有元数据值
     *
     * @param key
     * @return
     */
    @RequestMapping(value = "/values/{key}", method = RequestMethod.GET)
    public ApiResponse<List<MetaValue>> getMetaValues(@PathVariable String key) {
        ApiResponse<List<MetaValue>> resp = new ApiResponse<>();
        resp.setResult(metaService.selectMetaValues(key));
        return resp;
    }

    /**
     * 根据id获取元数据值
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/value/{id}", method = RequestMethod.GET)
    public ApiResponse<MetaValue> getMetaValue(@PathVariable Long id) {
        ApiResponse<MetaValue> resp = new ApiResponse<>();
        resp.setResult(metaService.selectMetaValue(id));
        return resp;
    }

    /**
     * 添加元数据,如果元数据下有值会把元数据值一起添加
     *
     * @param id
     * @return
     * @throws MetaException
     */
    @RequestMapping(value = "/add/", method = RequestMethod.POST)
    public ApiResponse<Meta> add(Authentication authentication, @RequestBody Meta meta) throws MetaException {
        ApiResponse<Meta> resp = new ApiResponse<>();
        Long userId = UserUtil.getUser(authentication).getId();
        resp.setResult(metaService.addMeta(meta, userId));
        return resp;
    }

    /**
     * 修改元数据,把新增后的元数据值添加,修改的元数据值对应修改
     *
     * @param id
     * @return
     * @throws MetaException
     */
    @RequestMapping(value = "/update/", method = RequestMethod.POST)
    public ApiResponse<Meta> update(Authentication authentication, @RequestBody Meta meta) throws MetaException {
        ApiResponse<Meta> resp = new ApiResponse<>();
        Long userId = UserUtil.getUser(authentication).getId();
        resp.setResult(metaService.updateMeta(meta, userId));
        return resp;
    }

    /**
     * 删除元数据值
     *
     * @param id
     * @return
     * @throws MetaException
     */
    @RequestMapping(value = "/value/delete/{id}", method = RequestMethod.DELETE)
    public ApiResponse<String> deleteMetaValue(@PathVariable Long id) throws MetaException {
        ApiResponse<String> resp = new ApiResponse<>();
        metaService.deleteMetaValue(id);
        resp.setResult("删除成功");
        return resp;
    }

    /**
     * 改变元数据状态0为禁用,1为启用
     */
    @RequestMapping(value = "/changeState/", method = RequestMethod.POST)
    public ApiResponse<String> changState(@RequestBody Meta meta) {
        ApiResponse<String> resp = new ApiResponse<>();
        metaService.changeMetaState(meta.getKey(), meta.getState());
        resp.setResult("修改成功");
        return resp;
    }

}
