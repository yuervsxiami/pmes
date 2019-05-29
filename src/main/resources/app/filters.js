/**
 * Created by xiongwei on 2017/1/4.
 */

(function () {
  'use strict';

  angular.module('FILTERS', [])
    .filter('changeDur', function () {
      return function (text) {
        var theTime = parseInt(text / 1000);// 秒
        var theTime1 = 0;// 分
        var theTime2 = 0;// 小时
        if (theTime > 60) {
          theTime1 = parseInt(theTime / 60);
          theTime = parseInt(theTime % 60);
          if (theTime1 > 60) {
            theTime2 = parseInt(theTime1 / 60);
            theTime1 = parseInt(theTime1 % 60);
          }
        } else if (theTime < 60) {
          var res = "00:" + parseInt(theTime);
          return res;
        }
        var result = "" + parseInt(theTime);
        if (theTime1 > 0) {
          result = "" + (parseInt(theTime1) >= 10 ? parseInt(theTime1) : ("0" + parseInt(theTime1))) + ":" + (result > 10 ? result : ("0" + result));
        }
        if (theTime2 > 0) {
          result = "" + (parseInt(theTime2) >= 10 ? parseInt(theTime2) : ("0" + parseInt(theTime2))) + ":" + result;
        }
        return result;
      };
    })
    .filter('changeDur2', function () {
      return function (text) {
        var theTime = parseInt(text);// 秒
        var theTime1 = 0;// 分
        var theTime2 = 0;// 小时
        if (theTime > 60) {
          theTime1 = parseInt(theTime / 60);
          theTime = parseInt(theTime % 60);
          if (theTime1 > 60) {
            theTime2 = parseInt(theTime1 / 60);
            theTime1 = parseInt(theTime1 % 60);
          }
        } else if (theTime < 60) {
          var res = "00:" + parseInt(theTime);
          return res;
        }
        var result = "" + parseInt(theTime);
        if (theTime1 > 0) {
          result = "" + (parseInt(theTime1) >= 10 ? parseInt(theTime1) : ("0" + parseInt(theTime1))) + ":" + (result > 10 ? result : ("0" + result));
        }
        if (theTime2 > 0) {
          result = "" + (parseInt(theTime2) >= 10 ? parseInt(theTime2) : ("0" + parseInt(theTime2))) + ":" + result;
        }
        return result;
      };
    })
    .filter('changeTarget', function () {
      return function (str) {
        var parameter;
        if (str.target === "subjectCourseDetail" || str.target === "subjectDetail") {
          parameter = JSON.parse(str.parameter);
          if (str.target === "subjectCourseDetail") {
            parameter.isSubject = 1;
            return parameter;
          } else if (str.target === "subjectDetail") {
            parameter.isSubject = 2;
            return parameter;
          }
        } else {
          return str.target;
        }
      };
    })
    .filter('changeCkEditor', function () {
      return function (data) {
        if (!data) {
          return;
        }
        var s = escape2Html(data);
        s = removeHtmlTab(s);
        return s;

        function removeHtmlTab(tab) {
          return tab.replace(/<[^<>]+?>/g, '');//删除所有HTML标签
        }

        function escape2Html(str) {
          var arrEntities = {'lt': '<', 'gt': '>', 'nbsp': '', 'amp': '&', 'quot': '"'};
          return str.replace(/&(lt|gt|nbsp|amp|quot);/ig, function (all, t) {
            return arrEntities[t];
          });
        }
      };
    })
    .filter('changeState', function () {
      return function (text) {
        if (text === 0) {
          return "待审核";
        } else if (text === 1) {
          return "待编辑";
        } else if (text === 2) {
          return "已通过";
        } else {
          return "未通过";
        }
      };
    })
    .filter('addHttp', function () {
      return function (text) {
        if (!text) {
          return;
        }
        var extStart = /.*(.jpg|.png|.gif|.JPG|.PNG|.GIF)$/;
        if (!extStart.test(text)) {
          return;
        }
        var extHttp = /(http|ftp|https):\/\/([\w.]+\/?)\S*/;
        if (extHttp.test(text)) {
          return text;
        } else {
          return "http://homecare-assets.oss-cn-hangzhou.aliyuncs.com/" + text;
        }
      };
    })
    .filter('score', function () {
      return function (num) {
        if (!num) {
          return;
        }
        var str = "", str1 = "★";
        for (var i = 0; i < num; i++) {
          str = str + str1;
        }
        return str;
      };
    })
    .filter('changeMetaType', function () {
      return function (type) {
        if (!type) {
          return;
        }
        var str = "";
        switch (type) {
          case 1:
            str = "专利标签元数据"
            break;
          case 2:
            str = "企业信息标签元数据"
            break;
          case 3:
            str = "企业需求标签元数据"
            break;
          case 4:
            str = "专家标签元数据"
            break;
          case 5:
            str = "流程元数据"
            break;
          case 6:
            str = "通用元数据"
            break;
        }
        return str;
      };
    })
    .filter('changeValueType', function () {
      return function (type) {
        if (!type) {
          return;
        }
        var str = "";
        switch (type) {
          case 1:
            str = "单行文本"
            break;
          case 2:
            str = "多行文本"
            break;
          case 3:
            str = "时间"
            break;
          case 4:
            str = "附件"
            break;
          case 5:
            str = "数字"
          case 6:
            str = "布尔"
            break;
        }
        return str;
      };
    })
    .filter('changeLabelType', function () {
      return function (type) {
        if (!type) {
          return;
        }
        var str = "";
        switch (type) {
          case 1:
            str = "专利标签"
            break;
          case 2:
            str = "企业信息标签"
            break;
          case 3:
            str = "企业需求标签"
            break;
          case 4:
            str = "专家标签"
            break;
        }
        return str;
      };
    })
    .filter('changeIndexType', function () {
      return function (type) {
        if (!type) {
          return;
        }
        var str = "";
        switch (type) {
          case 1:
            str = "自动获取"
            break;
          case 2:
            str = "自动计算"
            break;
          case 3:
            str = "自动获取人工修改"
            break;
          case 4:
            str = "自动计算人工修改"
            break;
          case 5:
            str = "人工标引"
            break;
        }
        return str;
      };
    })
    .filter('changeLabelSource', function () {
      return function (type) {
        if (!type) {
          return;
        }
        var str = "";
        switch (type) {
          case 1:
            str = "中高"
            break;
          case 2:
            str = "国家知识产权局"
            break;
          case 3:
            str = "出版社"
            break;
          case 4:
            str = "innography"
            break;
          case 5:
            str = "智慧芽"
            break;
          case 6:
            str = "发明人"
            break;
          case 7:
            str = "五星资产评估"
            break;
          case 8:
            str = "中高交易平台"
            break;
          case 9:
            str = "爬虫"
            break;
        }
        return str;
      };
    })
    .filter('labelsetType', function () {
      return function (type) {
        return ['', '专利价值快速评估体系', '基础标引标签体系', '价值评估标签体系', '价格评估标签体系',
          '深度标引标签体系', '企业信息标签体系', '企业需求标签体系', '专家标签体系'][type];
      };
    })
    .filter('changeProcessOrderState', function () {
      return function (state) {
        if (state == null) {
          return;
        }
        var str = "";
        switch (state) {
          case 0:
              str = "";
              break;
          case 1:
              str = "未完成";
              break;
          case 2:
              str = "已完成";
              break;
        }
        return str;
      };
    })
    .filter('changeTaskOrderState', function () {
      return function (state) {
        if (state == null) {
          return;
        }
        var str = "";
        switch (state) {
          case 0:
            str = "未完成"
            break;
          case 1:
            str = "已完成"
            break;
          case 2:
            str = "被退单"
            break;
        }
        return str;
      };
    })
    .filter('changePatentType', function () {
      return function (type) {
        if (!type) {
          return;
        }
        var str = "";
        switch (type) {
          case 1:
            str = "发明专利";
            break;
          case 2:
            str = "实用新型";
            break;
          case 3:
            str = "外观设计";
            break;
          case 4:
            str = "发明授权";
            break;
        }
        return str;
      };
    })
    .filter('changeTaskType', function () {
      return function (type) {
        if (!type) {
          return;
        }
        var str = "";
        switch (type) {
          case 1:
            str = "派单"
            break;
          case 2:
            str = "自动标引"
            break;
          case 3:
            str = "人工标引"
            break;
          case 4:
            str = "人工标引审核"
            break;
          case 5:
            str = "价值评估"
            break;
          case 6:
            str = "价格评估"
            break;
          case 7:
            str = "深度标引"
            break;
          case 8:
            str = "半自动标引"
            break;
          case 9:
        	str = "匹配结果人工筛选"
    		break;
          case 10:
            str = "半自动标引审核"
            break;
        }
        return str;
      };
    })
    .filter('processType', function () {
      return function (type) {
        return ['', '专利批量处理', '专利基础标引', '专利价值标引', '专利价格标引',
          '专利深度标引', '企业信息标引', '企业需求标引', '企业需求匹配', '专家标签体系'][type];
      };
    })
    .filter('labelDisplay', function () {
      // displayType: 0 - 百分制
      return function (value, displayType) {
        var transformed = value;
        switch (displayType) {
          case 0:
            transformed = (parseFloat(value) * 100).toFixed(2);
            break;
          default:
            break;
        }
        return transformed;
      };
    })
    .filter('changeRegion', function () {
      return function (ep) {
        var str = ep ? ep.province.name + (ep.city ? '-' + ep.city.name + (ep.district ? '-' + ep.district.name : '') : '') : null;
        return str || '-';
      }
    })
    .filter('changeRegionWithAddress', function () {
      return function (ep) {
        if (!ep || !ep.province) {
          return ep ? ep.address : '-';
        }
        var str = ep ? ep.province.name + (ep.city ? '-' + ep.city.name + (ep.district ? '-' + ep.district.name : '') : '') : null
        if (str == null) {
          return '-';
        }
        else if (ep.address == null) {
          return str;
        }
        else {
          return str + " - " + ep.address;
        }
      }
    })
    .filter('changeSearchRegion', function () {
      return function (region, defaultValue) {
        return region ? region.t1.name + (region.t2 ? '-' + region.t2.name + (region.t3 ? '-' + region.t3.name : '') : '') : (defaultValue || '全部');
      }
    })
    .filter('changeMessageType', function () {
      return function (type) {
        if (!type) {
          return;
        }
        var str = "";
        switch (type) {
          case 1:
            str = "预警提醒"
            break;
          case 2:
            str = "超时提醒"
            break;
          case 3:
            str = "退单通知"
            break;
          case 4:
            str = "转派通知"
            break;
        }
        return str;
      };
    })
    .filter('changeTimedTaskType', function () {
      return function (type) {
        if (!type) {
          return;
        }
        var str = "";
        switch (type) {
          case 1:
            str = "批量更新专利"
            break;
          case 2:
            str = "同步sub1"
            break;
          case 3:
            str = "同步sub3"
            break;
          case 4:
            str = "同步st_legalstatusinfo"
            break;
          case 5:
            str = "同步st_patfeeinfo"
            break;
          case 6:
            str = "同步st_patprsexploitationinfo"
            break;
          case 7:
            str = "同步st_patprspreservationinfo"
            break;
          case 8:
            str = "同步st_patprstransferinfo"
            break;
          case 9:
            str = "同步st_scoreinfo"
            break;
          case 10:
          case 11:
          case 12:
          case 13:
          case 14:
          case 15:
          case 16:
          case 17:
          case 18:
          case 19:
          case 20:
          case 21:
          case 22:
          case 23:
          case 24:
          case 25:
          case 26:
          case 27:
          case 28:
          case 29:
          case 30:
          case 31:
          case 32:
          case 33:
          case 34:
          case 35:
          case 36:
          case 37:
          case 38:
          case 39:
            str = "批量快速计算专利任务" + (type-10)
            break;
          case 100:
          case 101:
          case 102:
          case 103:
          case 104:
          case 105:
          case 106:
          case 107:
          case 108:
          case 109:
            str = "快速同步尾号为" + (type-100)+ "的专利"
            break;
          case 110:
          case 111:
          case 112:
          case 113:
          case 114:
          case 115:
          case 116:
          case 117:
          case 118:
          case 119:
          case 120:
              str = "检查an尾号为" + (type-110)+ "的专利更新"
              break;
					case 130:
					case 131:
					case 132:
					case 133:
					case 134:
					case 135:
					case 136:
					case 137:
					case 138:
					case 139:
					case 140:
						str = "检查an尾号为" + (type-130)+ "的sub3专利更新"
						break;
          case 200:
          case 201:
          case 202:
          case 203:
          case 204:
          case 205:
          case 206:
          case 207:
          case 208:
          case 209:
						str = "自动计算专利id尾号为" + (type-200)+ "的专利"
						break;
        }
        return str;
      };
    })
    .filter('changeEnterpriseRequirementType', function () {
      return function (type) {
        if (!type) {
          return;
        }
        var str = "";
        switch (type) {
          case "PROJECT_DECLARATION": return "项目申报";
          case "PATENT_APPLICATION": return "专利申请";
          case "INTELLECTUAL_PROPERTY_RIGHT": return "知识产权贯标";
          case "LEGAL_AID": return "法律援助";
          case "ANALYSIS_OF_PATENT_INFORMATION": return "专利情报分析";
          case "EVALUATION_OF_PATENT_VALUE": return "专利价值评估";
        }
        return str;
      };
    })
    .filter('changeEnterpriseType', function () {
				return function (type) {
					if (!type) {
						return;
					}
					var str = "";
					switch (type) {
						case "HIGH_NEW_TECHNOLOGY": return "高新技术企业";
						case "INNOVATIVE": return "创新型企业";
						case "SCIENCE_TECHNOLOGY": return "科技型中小企业";
						case "PRIVATE_SCIENCE_TECHNOLOGY": return "民营科技企业";
						case "LARGE_MIDDLE_SIZED": return "大中型企业";
					}
					return str;
				};
    })
  ;
})();