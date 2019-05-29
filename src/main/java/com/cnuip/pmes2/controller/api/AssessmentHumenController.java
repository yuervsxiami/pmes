package com.cnuip.pmes2.controller.api;

import com.UpYun;
import com.cnuip.pmes2.controller.api.request.PatentStartSearchCondition;
import com.cnuip.pmes2.controller.api.response.ApiResponse;
import com.cnuip.pmes2.domain.core.*;
import com.cnuip.pmes2.domain.inventory.ExportData;
import com.cnuip.pmes2.domain.inventory.PatentInfoAn;
import com.cnuip.pmes2.domain.inventory.RequestExportBean;
import com.cnuip.pmes2.domain.inventory.ValuePatent;
import com.cnuip.pmes2.repository.el.ElPatentRepository;
import com.cnuip.pmes2.domain.core.HumanAssessmentPatent;
import com.cnuip.pmes2.domain.core.Patent;
import com.cnuip.pmes2.domain.core.TaskOrder;
import com.cnuip.pmes2.domain.core.TaskOrderLabel;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.Soundbank;
import java.io.*;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 专利价值快速评估相关接口
 * Created by wangzhibin on 2018/1/29.
 */
@RestController
@RequestMapping("/api/patent/assessment/humen")
public class AssessmentHumenController extends BussinessLogicExceptionHandler {

    private static final UpYun UP_YUN = new UpYun("bucket-cnuip", "cnuipadmin", "CA2203!A");

    @Autowired
    private PatentAssessmentService patentAssessmentService;
    @Autowired
    private PatentEvaluateService patentEvaluateService;
    @Autowired
    private ElPatentRepository elPatentRepository;
    @Autowired
    private BatchQuickService batchQuickService;
    @Autowired
    private PatentService patentService;
    @Autowired
    private MongoDetailService mongoDetailService;

    @GetMapping("/indexError/")
    public ApiResponse<PageInfo<IndexEr>> getIndexError(
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "1") int pageNum) {
        ApiResponse<PageInfo<IndexEr>> res = new ApiResponse<>();
        res.setResult(patentService.selectIndexError(pageSize, pageNum));
        return res;
    }

    @RequestMapping(value = "/start/{id}")
    public ApiResponse<String> startOneBatchQuick(@PathVariable Long id) throws BussinessLogicException {
        ApiResponse<String> res = new ApiResponse<>();
        Patent patent = patentService.selectSimpleByPrimaryKey(id);
        batchQuickService.startOneBatchProcess(patent);
        res.setResult(patent.getTi() + "快速计算完成");
        return res;
    }

    /**
     * 根据搜索条件搜索列表
     *
     * @return
     */
    @RequestMapping(value = "/search/{mode}")
    public ApiResponse<PageInfo<Patent>> search(
            @PathVariable("mode") Integer mode,
            @RequestParam(required = false) List<String> lastLegalStatus,
            @RequestParam(required = false) List<Long> types,
            @RequestParam(required = false) String ti,
            @RequestParam(required = false) String an,
            @RequestParam(required = false) List<String> ans,
            @RequestParam(required = false) String onm,
            @RequestParam(required = false) String pnm,
            @RequestParam(required = false) String pa,
            @RequestParam(required = false) String pin,
            @RequestParam(required = false) Date fromAd,
            @RequestParam(required = false) Date toAd,
            @RequestParam(required = false) Date fromOd,
            @RequestParam(required = false) Date toOd,
            @RequestParam(required = false) Date fromPd,
            @RequestParam(required = false) Date toPd,
            @RequestParam(required = false, defaultValue = "0") Integer searchType,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "1") int pageNum) {
        ApiResponse<PageInfo<Patent>> resp = new ApiResponse<>();
        if (mode != 2) {
            PatentStartSearchCondition condition = new PatentStartSearchCondition();
            condition.setLastLegalStatus(lastLegalStatus);
            condition.setTypes(types);
            condition.setTi(ti);
            condition.setAn(an);
            condition.setOnm(onm);
            condition.setPnm(pnm);
            condition.setPa(pa);
            condition.setPin(pin);
            condition.setFromAd(fromAd);
            condition.setToAd(toAd);
            condition.setFromAd(fromOd);
            condition.setToAd(toOd);
            condition.setFromAd(fromPd);
            condition.setToAd(toPd);
            condition.setSearchType(searchType);
            Page<Patent> page = (Page<Patent>) this.patentAssessmentService.searchHumanAssessment(condition, pageNum, pageSize);
            resp.setResult(page.toPageInfo());
        } else {
            Page<Patent> page = (Page<Patent>) this.patentAssessmentService.searchHumanAssessmentWithAns(ans, pageNum, pageSize);
            resp.setResult(page.toPageInfo());
        }

        return resp;
    }

    /**
     * 查询快速评估详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/detail/{id}")
    public ApiResponse<HumanAssessmentPatent> detail(@PathVariable("id") Long id) {
        ApiResponse<HumanAssessmentPatent> resp = new ApiResponse<>();
        Map<String, String> labels = new HashMap<>();
        HumanAssessmentPatent patent;
        ElPatent elPatent = elPatentRepository.findById(id);
        if (elPatent != null && elPatent.getLatestLabels() != null && elPatent.getLatestLabels().size() > 0) {
            patent = new HumanAssessmentPatent();
            for (TaskOrderLabel label : elPatent.getLatestLabels()) {
                String strVal = label.getStrValue();
                labels.put(label.getLabel().getKey(), Strings.isNullOrEmpty(strVal) ? label.getTextValue() : strVal);
            }
            patent.setId(elPatent.getId());
            patent.setAn(elPatent.getAn());
            patent.setTi(elPatent.getTi());
            patent.setPatentValue(elPatent.getPatentValue());
            patent.setPdfUrl(elPatent.getPdfUrl());
            patent.setUpdateTime(elPatent.getUpdateTime());
            ProcessOrder processOrder = new ProcessOrder();
            TaskOrder taskOrder = new TaskOrder();
            taskOrder.setLabels(elPatent.getLatestLabels());
            processOrder.setTaskOrders(Arrays.asList(taskOrder));
            patent.setAssessmentOrder(processOrder);
        } else {
            patent = this.patentAssessmentService.findAssessmentPatentById(id);
            if (patent != null && patent.getAssessmentOrder() != null) {
                for (TaskOrder order : patent.getAssessmentOrder().getTaskOrders()) {
                    for (TaskOrderLabel label : order.getLabels()) {
                        String strVal = label.getStrValue();
                        labels.put(label.getLabel().getKey(), Strings.isNullOrEmpty(strVal) ? label.getTextValue() : strVal);
                    }
                }
            }
        }
        if (labels.size() > 0) {
            if(labels.get("legalValue")!=null) {
                patent.setExpPatentValues(patentEvaluateService.expPatentValues(labels));
            }
        }
        resp.setResult(patent);
        return resp;
    }

    /**
     * 导出Excel
     */

    @RequestMapping(value = "/exportExcel", method = RequestMethod.POST)
    public ApiResponse<String> exportExcel(HttpServletRequest request, HttpServletResponse response,
                                           @RequestBody RequestExportBean requestExportBean
    ) {
        ApiResponse<String> resp = new ApiResponse<>();
        PatentInfoAn patentInfoAn = new PatentInfoAn();
        List<PatentInfoAn> patentInfoAnList;
        List<PatentInfoAn> patentInfoAnValueList;
        String excelName = "ZG_patent_evaluate";
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String path = "/home/temp";
        String fileName = "/dataExcel/" + excelName + "_" + fdate.format(new Date()) + ".xls";
        String filePath = path + fileName;
        if (requestExportBean.getMode() != 2) {
            patentInfoAn.setAn(requestExportBean.getAn());
            patentInfoAn.setType(requestExportBean.getSearchType());
            patentInfoAn.setLastLegalStatusList(requestExportBean.getLastLegalStatus());
            ValuePatent valuePatent = new ValuePatent();
            valuePatent.setTi(requestExportBean.getTi());
            valuePatent.setPa(requestExportBean.getPa());
            valuePatent.setPin(requestExportBean.getPin());
            patentInfoAn.setValue(valuePatent);
            patentInfoAn.setSectionNameList(changePatentType(requestExportBean.getTypes()));
            patentInfoAnList = mongoDetailService.findPatentInfoAn(patentInfoAn);
            patentInfoAnValueList = getPatentValue(patentInfoAnList,requestExportBean.getDataLists());

        } else {
            patentInfoAnList = mongoDetailService.findPatentInfoAnList(requestExportBean.getAns());
            patentInfoAnValueList = getPatentValue(patentInfoAnList,requestExportBean.getDataLists());
        }
        Workbook wb = null;
        String url="";
        try {
            wb = new HSSFWorkbook();
            // 建立新的sheet对象（excel的表单）
            Sheet sheet = wb.createSheet(excelName);
            int rownum = 0;
            Row head = sheet.createRow(rownum++);
            //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
            for (int i = 0; i < requestExportBean.getDataLists().size(); i++) {
                Cell cell = head.createCell(i);
                cell.setCellValue(requestExportBean.getDataLists().get(i).getLabel());
                sheet.setColumnWidth(i, 256 * 30);
            }
            // 专利列表
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> map = new HashMap<>();
            for (PatentInfoAn patentInfo : patentInfoAnValueList) {
                try {
                   // map = objectMapper.readValue(objectMapper.writeValueAsString(patentInfo.getValue()), Map.class);
                    map=patentInfo.getValueMap();
                } catch (Exception e) {
                    throw new RuntimeException("错误：导出信息失败。", e);
                }
                Row row = sheet.createRow(rownum++);
                for (int i = 0; i < requestExportBean.getDataLists().size(); i++) {
                    row.createCell(i).setCellValue(map.get(requestExportBean.getDataLists().get(i).getName()) == null ? "" : map.get(requestExportBean.getDataLists().get(i).getName()).toString());
                }
            }
            File f = new File(filePath);
            FileOutputStream fos = null;
            try {
                if (!f.exists()) {
                    f.createNewFile();
                }
                fos = new FileOutputStream(f);
                wb.write(fos);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            //上传云盘
            Boolean flag;
            try {
                flag = UP_YUN.writeFile(filePath,f);
            } catch (Exception e) {
                e.printStackTrace();
                flag = false;
            }
            if (!flag) {
                throw new RuntimeException("上传失败，请重新上传");
            }
            url="https://image.cnuip.com"+filePath;
        } catch (Exception e) {
            throw new RuntimeException("错误：导出信息失败。", e);
        } finally {
            try {
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        resp.setResult(url);
        return resp;
    }

    private static List<String> changePatentType(List<Long> patentTypeList) {
        List<String> list = new ArrayList<>();
        for (Long type : patentTypeList) {
            if (type == 1L) {
                list.add("FMZL");
            }
            if (type == 2L) {
                list.add("SYXX");
            }
            if (type == 3L) {
                list.add("WGZL");
            }
            if (type == 4L) {
                list.add("FMSQ");
            }
        }
        return list;
    }

    private List<PatentInfoAn> getPatentValue(List<PatentInfoAn> patentInfoAnList,List<ExportData> exportDataList) {
        List<PatentInfoAn> list=new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        if (patentInfoAnList != null && patentInfoAnList.size() > 0) {
            Map<String,Object> map=new HashMap<>();
            for (PatentInfoAn patentInfo : patentInfoAnList) {
                Patent patent = null;
                try {
                    patent = this.patentService.findPatentByAnWithFullLabels(patentInfo.getAn());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (patent == null) {
                    try {
                        map = objectMapper.readValue(objectMapper.writeValueAsString(patentInfo.getValue()), Map.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    patentInfo.setValueMap(map);
                    list.add(patentInfo);
                    continue;
                }
                Map<String, Object> labels = new HashMap<>();
                List<TaskOrderLabel> orderLabels = patent.getLatestLabels();
                if (orderLabels != null && orderLabels.size() > 0) {
                    for (TaskOrderLabel label : orderLabels) {
                        labels.put(label.getLabel().getKey(), label.getStrValue() == null ?
                                label.getTextValue() : label.getStrValue());
                    }
                }
                ValuePatent valuePatent = patentInfo.getValue();
                valuePatent.setPatentValue(patent.getPatentValue());
                try {
                    map = objectMapper.readValue(objectMapper.writeValueAsString(valuePatent), Map.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for(ExportData exportData:exportDataList){
                    if(exportData.getId() < 1000) {
                        if (labels.get(exportData.getName()) != null) {
                            map.put(exportData.getName(), labels.get(exportData.getName()).toString());
                        }
                    }
                }
                patentInfo.setValueMap(map);
                list.add(patentInfo);
            }
        }
        return list;
    }

    public static String toUtf8String(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = Character.toString(c).getBytes("utf-8");
                } catch (Exception ex) {
                    System.out.println(ex);
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0) k += 256;
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }

}
