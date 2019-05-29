package com.cnuip.pmes2.controller.api;

import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.domain.core.InstanceLabel;
import com.cnuip.pmes2.domain.core.LabelChangeHistory;
import com.cnuip.pmes2.domain.core.Patent;
import com.cnuip.pmes2.domain.core.TaskOrderLabel;
import com.cnuip.pmes2.exception.TaskOrderLabelException;
import com.cnuip.pmes2.service.PatentService;
import com.cnuip.pmes2.service.TaskOrderLabelService;
import com.cnuip.pmes2.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PatentController
 *
 * @author: xiongwei
 * Date: 2018/4/8 下午3:15
 */
@RestController
@RequestMapping("/api/patent")
public class PatentController extends BussinessLogicExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(PatentController.class);

    @Autowired
    private PatentService patentService;
    @Autowired
    private TaskOrderLabelService taskOrderLabelService;

    @RequestMapping("/{id}")
    public ApiResponse<Patent> detailWithLabels(@PathVariable("id") Long id) {
        Patent patent = this.patentService.findPatentByIdWithManualLabels(id);
        ApiResponse<Patent> resp = new ApiResponse<>();
        resp.setResult(patent);
        return resp;
    }

    /**
     * 根据an号找专利
     * @param an
     * @return
     */
    @RequestMapping("/")
    public ApiResponse<Patent> findByAn(@RequestParam String an) {
        Patent patent = this.patentService.findByAn(an);
        ApiResponse<Patent> resp = new ApiResponse<>();
        resp.setResult(patent);
        return resp;
    }

    @RequestMapping(value = "/{id}/label", method = RequestMethod.POST)
    public ApiResponse<InstanceLabel> updatePatentLabel(Authentication authentication, @PathVariable("id") Long id, @RequestBody InstanceLabel instanceLabel) {
        ApiResponse<InstanceLabel> resp = new ApiResponse<>();
        Long userId = UserUtil.getUser(authentication).getId();
        InstanceLabel label = null;
        try {
            label = this.taskOrderLabelService.update(instanceLabel,userId,id);
            this.patentService.updateHasBatchIndexed(id);
            resp.setResult(label);
        } catch (TaskOrderLabelException e) {
            logger.error("更新专利标签失败", e);
            resp.setCode(e.getExceptionEnum().getCode());
            resp.setMessage(e.getExceptionEnum().getMsg());
            resp.setError(e);
        }
        return resp;
    }

    @RequestMapping(value = "/{patentId}/label/history/{labelId}", method = RequestMethod.GET)
    public ApiResponse<List<LabelChangeHistory>> findLabelChangeHistory(@PathVariable("labelId") Long labelId, @PathVariable("patentId") Long patentId) {
        ApiResponse<List<LabelChangeHistory>> resp = new ApiResponse<>();
        resp.setResult(taskOrderLabelService.findLabelChangeHistory(labelId,patentId));
        return resp;
    }


}
