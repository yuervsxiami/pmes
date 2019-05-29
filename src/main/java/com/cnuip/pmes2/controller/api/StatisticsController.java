package com.cnuip.pmes2.controller.api;

import com.cnuip.pmes2.controller.api.request.PatentValueStatCondition;
import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.controller.api.response.PatentValueSpread;
import com.cnuip.pmes2.domain.statistics.IndexOrder;
import com.cnuip.pmes2.service.ElPatentService;
import com.cnuip.pmes2.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @auhor Crixalis
 * @date 2018/3/28 17:01
 */
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController extends BussinessLogicExceptionHandler {

	@Autowired
	private StatisticsService statisticsService;
	@Autowired
	private ElPatentService elPatentService;

	@GetMapping("/patent/value")
	public ApiResponse<PatentValueSpread> statPatentValueSpread(PatentValueStatCondition condition) {
		PatentValueSpread patentValueSpread = elPatentService.statPatentValue(condition == null ? new PatentValueStatCondition() : condition);
		ApiResponse<PatentValueSpread> response = new ApiResponse<>();
		response.setResult(patentValueSpread);
		return response;
	}

	/**
	 * 专利各流程下的完成订单数
	 * @param pa 申请人
	 * @param pin 发明人
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	@GetMapping("/patentIndexOrderNum/")
	public ApiResponse<List<IndexOrder>> getPatentIndexOrderNum(
			@RequestParam(required = false) String pa,
			@RequestParam(required = false) String pin,
			@RequestParam(required = false) Date startTime,
			@RequestParam(required = false) Date endTime) {
		ApiResponse<List<IndexOrder>> res = new ApiResponse<>();
		res.setResult(statisticsService.getPatentIndexOrderNum(pa,pin,startTime,endTime));
		return res;
	}

	@GetMapping("/processOrderNum/")
	public ApiResponse<List<IndexOrder>> getProcessOrderNum(
			@RequestParam(required = false) Date startTime,
			@RequestParam(required = false) Date endTime) {
		ApiResponse<List<IndexOrder>> res = new ApiResponse<>();
		res.setResult(statisticsService.getProcessOrderNum(startTime,endTime));
		return res;
	}

}