/**
 * Created by xiongwei on 2017/1/3.
 */

(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('Configuration')
    .controller('LabelController', ['ToastService', 'ModalService', 'LabelService', 'MetaService', '$log', '$scope', 'LABEL_TYPE', 'VALUE_TYPE',
    	'PATENT_INDEX_TYPE', 'PATENT_LABEL_SOURCE', 'ENTERPRISE_INFO_INDEX_TYPE', 'ENTERPRISE_INFO_LABEL_SOURCE', 'ENTERPRISE_REQUIREMENT_INDEX_TYPE', 'ENTERPRISE_REQUIREMENT_LABEL_SOURCE', LabelController])
	.controller('LabelEditCtrl', ['$scope', '$uibModalInstance', 'ToastService', 'HttpService', '$log', 'labelConfig', 'MetaService', 'LabelService', 'ModalService', 'LABEL_TYPE', 'VALUE_TYPE',
		'PATENT_INDEX_TYPE', 'PATENT_LABEL_SOURCE', 'ENTERPRISE_INFO_INDEX_TYPE', 'ENTERPRISE_INFO_LABEL_SOURCE', 'ENTERPRISE_REQUIREMENT_INDEX_TYPE', 'ENTERPRISE_REQUIREMENT_LABEL_SOURCE', LabelEditCtrl])
	.controller('LabelViewerCtrl', ['$scope', '$uibModalInstance', 'ToastService', '$log', 'labelConfig', LabelViewerCtrl])	
  ;

  function LabelController(ToastService, ModalService, LabelService, MetaService, $log, $scope, LABEL_TYPE, VALUE_TYPE,
		  PATENT_INDEX_TYPE, PATENT_LABEL_SOURCE,ENTERPRISE_INFO_INDEX_TYPE, ENTERPRISE_INFO_LABEL_SOURCE,ENTERPRISE_REQUIREMENT_INDEX_TYPE, ENTERPRISE_REQUIREMENT_LABEL_SOURCE) {

    var self = this;

    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });

    self.contextPath = CONTEXT_PATH;

    self.init = function () {
      self.labelType = LABEL_TYPE;
      self.valueType = VALUE_TYPE;
      self.patentIndexType = PATENT_INDEX_TYPE;
      self.patentLabelSource = PATENT_LABEL_SOURCE;
      self.enterpriseInfomationIndexType = ENTERPRISE_INFO_INDEX_TYPE;
      self.enterpriseInfomationLabelSource = ENTERPRISE_INFO_LABEL_SOURCE;
      self.enterpriseRequirementIndexType = ENTERPRISE_REQUIREMENT_INDEX_TYPE;
      self.enterpriseRequirementLabelSource = ENTERPRISE_REQUIREMENT_LABEL_SOURCE;

      //配置分页基本参数
      self.paginationConf = {
        currentPage: 1,
        itemsPerPage: 10,
      };
      
      self.searchCondition = {
    	type: 1,
	    fromTime: moment().add(-180, 'd').startOf('day').toDate().getTime(),
        toTime: moment().endOf('day').toDate().getTime()
	  };

      $('#dateTimeRange').daterangepicker({
        startDate: moment(self.searchCondition.fromTime),
        endDate: moment(self.searchCondition.toTime),
        maxDate: moment(),
        showDropdowns: false,
        showWeekNumbers: false,
        timePickerIncrement: 1,
        singleDatePicker: false,
        locale: {
          separator: '~',
          applyLabel: '确认',
          cancelLabel: '取消',
          format: 'YYYY-MM-DD',
          daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
          monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
        },
        opens: 'right',
        buttonClasses: ['btn btn-default'],
        applyClass: 'btn-small btn-primary',
        cancelClass: 'btn-small btn-default',
        format: 'YYYY-MM-DD'
      }, function (start, end, label) { // 格式化日期显示框
        $scope.$apply(function () {
          self.searchCondition.fromTime = start.toDate().getTime();
          self.searchCondition.toTime = end.toDate().getTime();
        })
      }).next().on('click', function () {
        $(this).prev().focus();
      });
      
      $scope.$watch(function () {
          return self.paginationConf.itemsPerPage + " " + self.paginationConf.currentPage;
      }, self.search);
      
      $scope.$watch(function () {
          return self.searchCondition.type;
      }, function (newValue,oldValue) {
    	self.indexTypes = [];
    	self.labelSources = [];
    	delete self.searchCondition.indexType;
    	delete self.searchCondition.source;
    	switch (newValue) {
				case 1:
						self.indexTypes = self.patentIndexType;
						self.labelSources = self.patentLabelSource;
					break;
				case 2:
						self.indexTypes = self.enterpriseInfomationIndexType;
						self.labelSources = self.enterpriseInfomationLabelSource;
					break;
				case 3:
						self.indexTypes = self.enterpriseRequirementIndexType;
						self.labelSources = self.enterpriseRequirementLabelSource;
					break;
				case 4:
					break;
			}
		});

    };

    self.changeState = function (id, state) {
    	LabelService.changeState({id: id, state: state}).then(function (resp) {
        if (resp && resp.code === 0) {
          self.search();
          ToastService.toast({
            type: "success",
            body: '修改标签状态成功'
          });
        } else {
          ToastService.toast({
            type: "error",
            body: '修改标签状态失败:' + resp.message
          });
        }
      }).catch(function (err) {
        $log.info(err);
      });
    };
    
    self.reset = function () {
      self.searchCondition = {
      	type: 1,
  	    fromTime: moment().add(-180, 'd').startOf('day').toDate().getTime(),
        toTime: moment().endOf('day').toDate().getTime()
  	  };
    }
    self.openLabelEditer = function (labelConfig) {
	    ModalService.openLabelEditer(labelConfig).then(function (resp) {
	      if (resp == 0) {
	        self.search();
	      }
	    }).catch(function (err) {
	      $log.info(err);
	    });
    };

    self.add = function () {
      self.openLabelEditer({model: "add"});
    };

    self.update = function (label) {
      self.openLabelEditer({model: "update", label: label});
    };
    
    self.view = function (label) {
      ModalService.openLabelViewer({label: label}).then(function (resp) {
      }).catch(function (err) {
        $log.info(err);
      });
    };

    self.search = function () {
      self.loading = true;
      self.searchCondition.pageSize = self.paginationConf.itemsPerPage;
      self.searchCondition.pageNum = self.paginationConf.currentPage;
      LabelService.search(self.searchCondition).then(function (resp) {
	    self.loading = false;
        if (resp && resp.code === 0) {
          self.paginationConf.totalItems = resp.result.total;
          self.list = resp.result.list;
          self.searching = false;
        } else {
          ToastService.toast({
            type: "error",
            body: '获取标签失败'
          });
        }
      }).catch(function (err) {
        self.loading = false;
        ToastService.toast({
          type: "error",
          body: err.message
        });
      });
    };

    self.init();

  };

  function LabelEditCtrl($scope, $uibModalInstance, ToastService, HttpService, $log, labelConfig, MetaService, LabelService, ModalService, LABEL_TYPE, VALUE_TYPE,
  		PATENT_INDEX_TYPE, PATENT_LABEL_SOURCE, ENTERPRISE_INFO_INDEX_TYPE, ENTERPRISE_INFO_LABEL_SOURCE, ENTERPRISE_REQUIREMENT_INDEX_TYPE, ENTERPRISE_REQUIREMENT_LABEL_SOURCE) {
	  var self = this;
	  
	  self.init = function () {
        self.labelType = LABEL_TYPE;
        self.valueType = VALUE_TYPE;
        self.patentIndexType = PATENT_INDEX_TYPE;
        self.patentLabelSource = PATENT_LABEL_SOURCE;
        self.enterpriseInfomationIndexType = ENTERPRISE_INFO_INDEX_TYPE;
        self.enterpriseInfomationLabelSource = ENTERPRISE_INFO_LABEL_SOURCE;
        self.enterpriseRequirementIndexType = ENTERPRISE_REQUIREMENT_INDEX_TYPE;
        self.enterpriseRequirementLabelSource = ENTERPRISE_REQUIREMENT_LABEL_SOURCE;

        self.loadMetas();
        
	    $scope.$watch(function () {
	    	//TODO:当类型和值类型变化时,应该对绑定的元数据列表进行筛选
	    	return self.currentLabel.type;
	    }, function (newValue,oldValue) {
	    	self.indexTypes = [];
	    	self.labelSources = [];
	    	if(oldValue !== newValue) {
	    		delete self.currentLabel.indexType;
	    		delete self.currentLabel.source;
	    	}
	    	switch (newValue) {
	    	case 1:
	    		self.indexTypes = self.patentIndexType;
	    		self.labelSources = self.patentLabelSource;
			break;
	    	case 2:
	    		self.indexTypes = self.enterpriseInfomationIndexType;
	    		self.labelSources = self.enterpriseInfomationLabelSource;
	    		break;
	    	case 3:
	    		self.indexTypes = self.enterpriseRequirementIndexType;
	    		self.labelSources = self.enterpriseRequirementLabelSource;
	    		break;
	    	case 4:
	    		break;
	    	}
	    });
        
	    $scope.$watch(function () {
	    	return self.currentLabel.type + "," + self.currentLabel.valueType;
	    }, function (newValue,oldValue) {
	    	if(newValue === oldValue) {
	    		return;
	    	}
	    	if(self.allMetas) {
		    	var type = newValue.substring(0,newValue.indexOf(","));
		    	var valueType = newValue.substring(newValue.indexOf(",")+1);
		    	self.metas = [];
	    		for(var i=0;i<self.allMetas.length;i++) {
	    			var meta = self.allMetas[i];
	    			if(meta.type == type && meta.valueType == valueType) {
	    				self.metas.push(meta);
	    			}
	    		}
	    	}
	    });

	    self.title = "添加标签";
	    self.currentLabel = {
	      type: 1,
	      showInProcess: 0,
	      isRequired: 0,
	      isMultiple: 0,
	    };
	    if(labelConfig.model=="update") {
		  self.currentLabel = labelConfig.label;
		  self.title = "编辑标签";
	    }
  	  };
	  
	  self.notEmpty = function (data) {
		if(data) {
			return true;
		}  
		return false;
	  };

	  self.cancel = function () {
		  $uibModalInstance.dismiss('cancel');
	  };

	  self.ok = function () {
		  if(!self.currentLabel){
			  self.init();
			  return;
		  }
		  var flag = false;
		  if(!self.currentLabel.type) {
		      ToastService.toast({
		        type: 'error',
		        body: '请先选择标签类型'
		      });
		      flag = true;
		  }
		  if(!self.currentLabel.valueType) {
		      ToastService.toast({
		        type: 'error',
		        body: '请先选择标签值类型'
		      });
		      flag = true;
		  }
		  if(flag) {
			  return;
		  }
		  var p;
		  delete self.currentLabel.user;
		  if(labelConfig.model=="update") {
			  p = LabelService.update(self.currentLabel);
		  } else {
			  p = LabelService.add(self.currentLabel);
		  }
		  p.then(function(resp){
		    if (resp && resp.code === 0) {
	          ToastService.toast({
		            type: 'success',
		            body: '编辑成功'
	          });
	          $uibModalInstance.close(0);
		    } else {
	          ToastService.toast({
	            type: 'error',
	            body: '编辑失败'
	          });
		    }
	      }).catch(function (err) {
	        ToastService.errHandler(err);
	      });
	  };
	  
	  self.loadMetas = function () {
	    MetaService.getAllMetas({pageSize:0,pageNum:0}).then(function(resp){
	      if (resp && resp.code === 0) {
	    	self.allMetas = resp.result;
	    	self.metas = [];
    		for(var i=0;i<self.allMetas.length;i++) {
    			var meta = self.allMetas[i];
    			if(meta.type == self.currentLabel.type && meta.valueType == self.currentLabel.valueType) {
    				self.metas.push(meta);
    			}
    		}
	      } else {
          ToastService.toast({
            type: 'error',
            body: '获取元数据列表失败'
          });
	      }
        }).catch(function (err) {
          ToastService.errHandler(err);
        });
	  };

	  
	  self.init();
  };
  
  function LabelViewerCtrl($scope, $uibModalInstance, ToastService, $log, labelConfig) {
	  var self = this;
	  
	  self.init = function () {
		  self.currentLabel = labelConfig.label;
	  };
	  
	  self.cancel = function () {
	      $uibModalInstance.dismiss('cancel');
	  };

	  self.init();
	  
  };
  
})(angular, CONTEXT_PATH);