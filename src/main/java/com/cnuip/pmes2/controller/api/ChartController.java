package com.cnuip.pmes2.controller.api;

import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.domain.statistics.PatentParam;
import com.cnuip.pmes2.service.ChartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * pmes统计分析图表与清单
 */
@RestController
@RequestMapping("/api/chart")
public class ChartController {

    private ChartService chartService;

    public ChartController(ChartService chartService) {
        this.chartService = chartService;
    }

    /**
     *省份列表
     */
    @GetMapping("/provinces")
    public ApiResponse<List<Map>> provinces() {
        ApiResponse<List<Map>> response = new ApiResponse<>();
        response.setResult(chartService.provinces());
        return response;
    }

    /**
     *省份高校列表
     */
    @GetMapping("/colleges")
    public ApiResponse<List<Map>> colleges(@RequestParam String name) {
        ApiResponse<List<Map>> response = new ApiResponse<>();
        response.setResult(chartService.colleges(name));
        return response;
    }

    /**
     * 1.1省份的高校数量
     *
     * @return
     */
    @GetMapping("/province/number")
    public ApiResponse<List<Map>> provinceNumber() {
        ApiResponse<List<Map>> response = new ApiResponse<>();
        response.setResult(chartService.provinceNumber());
        return response;
    }

    /**
     * 2.1右侧上是高校名称查询和高校列表（列表中有高校名称和专利数量，按照专利数量倒排，高校数量多的时候做成分页）
     *
     * @param collegeName
     * @param provinceName
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/college/patent")
    public ApiResponse<Map<String, Object>> collegePatentList(String collegeName, String provinceName,
                                                    @RequestParam(required = false, defaultValue = "1") int pageNum, @RequestParam(required = false, defaultValue = "10") int pageSize) {
        ApiResponse<Map<String, Object>> response = new ApiResponse<>();
        response.setResult(chartService.collegePatentList(provinceName, collegeName, pageNum, pageSize));
        return response;
    }

    /**
     * 2.2右侧下是专利查询条件（专利类型、法律状态、申请号、专利名称、申请人、发明人、申请日、公开日）和专利列表，点专利名称可看专利详情
     *
     * @param patentParam
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/province/patent")
    public ApiResponse<Map<String, Object>> provincePatentList(PatentParam patentParam,
                                                     @RequestParam(required = false, defaultValue = "1") int pageNum, @RequestParam(required = false, defaultValue = "10") int pageSize) {
        ApiResponse<Map<String, Object>> response = new ApiResponse<>();
        response.setResult(chartService.provincePatentList(patentParam, pageNum, pageSize));
        return response;
    }

    /**
     * 3/根据高校专利量进行柱状图排名，显示top10，top20。
     * 柱状图报表筛选条件：专利类型+法律状态
     *
     * @param type
     * @param legState
     * @param limit
     * @return
     */
    @GetMapping("/college/patent/sort")
    public ApiResponse<List<Map>> collegePatentSort(String type, String legState, @RequestParam(required = false, defaultValue = "10") int limit) {
        ApiResponse<List<Map>> response = new ApiResponse<>();
        response.setResult(chartService.collegePatentSort(type, legState, limit));
        return response;
    }

    /**
     * 3.1专利类型饼图
     *
     * @param name
     * @return
     */
    @GetMapping("/college/type/stat")
    public ApiResponse<List<Map>> collegeTypeStat(String name) {
        ApiResponse<List<Map>> response = new ApiResponse<>();
        response.setResult(chartService.collegeTypeStat(name));
        return response;
    }

    /**
     * 3.2法律状态饼图
     *
     * @param name
     * @return
     */
    @GetMapping("/college/leg/stat")
    public ApiResponse<List<Map>> collegeLegStat(@RequestParam String name) {
        ApiResponse<List<Map>> response = new ApiResponse<>();
        response.setResult(chartService.collegeLegStat(name));
        return response;
    }


    /**
     * 5用折线图或柱状图，展示高校的专利总量、发明、实用新型、外观随年份（近10年）的变化
     *
     * @param name need
     * @return
     */
    @GetMapping("/college/stat/lastTen")
    public ApiResponse<List<Map>> collegeStatLastTenYear(@RequestParam String name) {
        ApiResponse<List<Map>> response = new ApiResponse<>();
        response.setResult(chartService.collegeStatLastTenYear(name));
        return response;
    }

    /**
     * 7高校的技术热点（IPC的top20排序，IPC选3级还是4级需要看数据定）
     * 以矩形图展示。
     * 筛选条件：省份+高校 ，年份（近5年、近10年、全部）（页面入口？）
     *
     * @param name
     * @param collection five, ten, all
     * @param limit
     * @return
     */
    @GetMapping("/college/ipc/hot/{collection}")
    public ApiResponse<List<Map>> collegeIpc(@RequestParam String name, @PathVariable("collection") String collection, @RequestParam(required = false, defaultValue = "20") int limit) {
        ApiResponse<List<Map>> response = new ApiResponse<>();
        response.setResult(chartService.collegeIpc(name, collection, limit));
        return response;
    }

    /**
     * 6 横轴是申请日，纵轴是IPC分类，IPC分类分到五级
     * 筛选条件：省份+高校
     *
     * @param name
     * @return
     */
    @GetMapping("/college/ipc/trend")
    public ApiResponse<List<Map>> collegeIpcTen(@RequestParam String name) {
        ApiResponse<List<Map>> response = new ApiResponse<>();
        response.setResult(chartService.collegeIpcTopYear(name));
        return response;
    }

    /**
     * 8以南京理工大学为例，选出IPC（2级或3级待定）排名前10的技术领域，
     * 并在每个技术领域的外围列举5-10（待定）PMES总分从高到低排序的专利
     *
     * @param name
     * @return
     */
    @GetMapping("/college/ipc/value")
    public ApiResponse<List<Map>> collegeIpcValue(@RequestParam String name) {
        ApiResponse<List<Map>> response = new ApiResponse<>();
        response.setResult(chartService.collegeIpcTopValue(name));
        return response;
    }

    /**
     * 10方案一、天弓的协同创新主体，及某所高校的联合申请人，显示联合申请人名称及专利数量（top10）。
     *
     * @param name
     * @return
     */
    @GetMapping("/college/partner")
    public ApiResponse<List<Map>> collegePartner(@RequestParam String name) {
        ApiResponse<List<Map>> response = new ApiResponse<>();
        response.setResult(chartService.collegePartner(name));
        return response;
    }

    /**
     * 12根据高校名称，对发明人专利数量进行统计排名，展示专利量top10，top20，top50的发明人，
     *
     * @param name
     * @param limit
     * @return
     */
    @GetMapping("/college/pin")
    public ApiResponse<List<Map>> collegePin(@RequestParam String name, @RequestParam(required = false, defaultValue = "10") int limit) {
        ApiResponse<List<Map>> response = new ApiResponse<>();
        response.setResult(chartService.collegePin(name, limit));
        return response;
    }

    /**
     * 14展示某所高校总专利量排名前10的发明人，点击每个发明人展示合作发明人，及每个发明人的专利数量
     *
     * @param name
     * @param pinSplit
     * @return
     */
    @GetMapping("/college/pin/partner")
    public ApiResponse<List<Map>> collegePinPartner(@RequestParam String name, @RequestParam("pinSplit") String pinSplit) {
        ApiResponse<List<Map>> response = new ApiResponse<>();
        response.setResult(chartService.collegePinPartner(name, pinSplit));
        return response;
    }

    /**
     * 9根据专利被引用次数，展示每所高校被引用次数最多的专利，展示top5、top10、top15等，点击可展示专利清单。
     *
     * @param name
     * @param limit
     * @return
     */
    @GetMapping("/college/byQuoteTotal")
    public ApiResponse<List<Map>> byQuoteTotal(@RequestParam String name, @RequestParam(required = false, defaultValue = "5") int limit) {
        ApiResponse<List<Map>> response = new ApiResponse<>();
        response.setResult(chartService.byQuoteTotal(name, limit));
        return response;
    }

    /**
     * 11每所高校专利总量按照分数区间分布。
     * 柱状图， 0~50、后面分数段为5分一个区间。
     *
     * @param collegeName
     * @return
     */
    @GetMapping("/college/pmes/value")
    public ApiResponse<Map<String, Object>> collegePmesValue(@RequestParam String collegeName) {
        ApiResponse<Map<String, Object>> response = new ApiResponse<>();
        response.setResult(chartService.collegePmesValue(collegeName));
        return response;
    }


    /**
     * 15发明人的PMES分数区间，钻取看某个分数的专利
     * 0~50，后面每5分一个区间
     *
     * @param collegeName
     * @param name
     * @return
     */
    @GetMapping("/pin/pmes/value")
    public ApiResponse<Map<String, Object>> pinPmesValue(@RequestParam String collegeName, @RequestParam String name) {
        ApiResponse<Map<String, Object>> response = new ApiResponse<>();
        response.setResult(chartService.pinPmesValue(collegeName, name));
        return response;
    }

    /**
     * 13展示所选发明人的技术领域、专家关键词、专家研发热点、相似专家和潜在合作对象。
     * 筛选条件：高校+发明人
     *
     * @param collegeName
     * @param name
     * @return
     */
    @GetMapping("/pin/expert")
    public String getExpertRecommendation(@RequestParam String collegeName, @RequestParam String name) {
        return chartService.getExpertRecommendation(collegeName, name);
    }
}
