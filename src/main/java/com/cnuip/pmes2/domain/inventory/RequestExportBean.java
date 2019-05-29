package com.cnuip.pmes2.domain.inventory;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/6/21.
 * Time: 15:01
 */
@Setter
@Getter
public class RequestExportBean {
   private String ti;
   private String pa;
   private String an;
   private String pin;
   private List<String> ans;
   private List<Long> types;
   private List<String> lastLegalStatus;
   private Integer mode;
   private List<ExportData> dataLists;
   private Integer searchType;
}
