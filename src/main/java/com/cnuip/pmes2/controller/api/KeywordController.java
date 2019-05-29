package com.cnuip.pmes2.controller.api;

import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.controller.api.response.KeywordAndRank;
import com.cnuip.pmes2.domain.core.RequirementProfessor;
import com.cnuip.pmes2.domain.el.ElProfessor;
import com.cnuip.pmes2.service.KeywordService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/keyword")
public class KeywordController {

    private KeywordService keywordService;

    public KeywordController(KeywordService keywordService) {
        this.keywordService = keywordService;
    }

    /**
     * 企业需求关键词接口
     *
     * @param text
     * @param topn
     * @return
     */
    @GetMapping("/content/keyword")
    public ApiResponse<List<KeywordAndRank>> keyword(@RequestParam String text, @RequestParam(required = false, defaultValue = "10") int topn) {
        ApiResponse<List<KeywordAndRank>> response = new ApiResponse<>();
        response.setResult(keywordService.content(text, topn));
        return response;
    }

    /**
     * 提取专家关键词接口
     *
     * @return
     */
    @GetMapping("/professor/keyword")
    public ApiResponse<List<KeywordAndRank>> extardProfessorKeyword(@RequestParam String collegeName,@RequestParam String name) {
        ApiResponse<List<KeywordAndRank>> response = new ApiResponse<>();
        response.setResult(keywordService.extardProfessorKeyword(collegeName, name));
        return response;
    }

    /**
     * 表达式需求匹配
     *
     * @param expression
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/match")
    public ApiResponse<PageInfo<ElProfessor>> match(@RequestParam String expression, @RequestParam(required = false, defaultValue = "1") int pageNum, @RequestParam(required = false, defaultValue = "10") int pageSize) {
        ApiResponse<PageInfo<ElProfessor>> response = new ApiResponse<>();
        response.setResult(keywordService.match(expression, pageNum, pageSize));
        return response;
    }

    /**
     * 相似词
     *
     * @param keywords
     * @param topn
     * @return
     */
    @GetMapping("/correlation")
    public String correlation(@RequestParam String keywords, @RequestParam(required = false, defaultValue = "10") int topn) {
        return keywordService.correlation(keywords, topn);
    }

    /**
     * 存储匹配结果
     *
     * @param professors
     * @return
     */
    @PostMapping("/professor")
    public ApiResponse<Object> saveProfessor(@RequestBody List<RequirementProfessor> professors) {
        keywordService.saveProfessor(professors);
        return new ApiResponse<>();
    }

    @GetMapping("/{rqId}")
    public ApiResponse<PageInfo<RequirementProfessor>> findByRqId(@PathVariable("rqId") long rqId, @RequestParam(required = false, defaultValue = "1") int pageNum, @RequestParam(required = false, defaultValue = "10") int pageSize) {
        ApiResponse<PageInfo<RequirementProfessor>> response = new ApiResponse<>();
        response.setResult(keywordService.findByRqId(rqId, pageNum, pageSize));
        return response;
    }
}
