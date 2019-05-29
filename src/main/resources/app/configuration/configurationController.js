/**
 * Created by xiongwei on 2017/1/3.
 */

(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('Configuration')
    .controller('MetaCtrl', ['ToastService', 'ModalService', 'MetaService', '$log', '$scope', 'META_TYPE', 'VALUE_TYPE', MetaCtrl])
    .controller('MetaEditCtrl', ['$scope', '$uibModalInstance', 'ToastService', 'HttpService', '$log', 'metaConfig', 'MetaService', 'ModalService', 'META_TYPE', 'VALUE_TYPE', MetaEditCtrl])
    .controller('MetaViewerCtrl', ['$scope', '$uibModalInstance', 'ToastService', '$log', 'metaConfig', 'MetaService', MetaViewerCtrl])
  
  ;

  function MetaCtrl(ToastService, ModalService, MetaService, $log, $scope, META_TYPE, VALUE_TYPE) {

    var self = this;

    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });

    self.contextPath = CONTEXT_PATH;

    self.init = function () {
      self.metaType = META_TYPE;
      self.valueType = VALUE_TYPE;

      //配置分页基本参数
      self.paginationConf = {
        currentPage: 1,
        itemsPerPage: 10,
      };
      self.searchCondition = {
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

    };

    self.changeState = function (key, state) {
      MetaService.changeMetaState({key: key, state: state}).then(function (resp) {
        if (resp && resp.code === 0) {
          self.search();
          ToastService.toast({
            type: "success",
            body: '修改元数据状态成功'
          });
        } else {
          ToastService.toast({
            type: "error",
            body: '修改元数据状态失败'
          });
        }
      }).catch(function (err) {
        $log.info(err);
      });
    };

    self.openMetaEditer = function (metaConfig) {
      ModalService.openMetaEditer(metaConfig).then(function (resp) {
        if (resp == 0) {
          self.search();
        }
      }).catch(function (err) {
        $log.info(err);
      });
    };

    self.addMeta = function () {
      self.openMetaEditer({model: "add"});
    };

    self.updateMeta = function (meta) {
      self.openMetaEditer({model: "update", meta: meta});
    };

    self.viewMeta = function (meta) {
      ModalService.openMetaViewer({title: "元数据展示", meta: meta}).then(function (resp) {
      }).catch(function (err) {
        $log.info(err);
      });
    };
    
    self.reset = function () {
        self.searchCondition = {
          fromTime: moment().add(-180, 'd').startOf('day').toDate().getTime(),
          toTime: moment().endOf('day').toDate().getTime()
		};	
    }

    self.search = function () {
      self.loading = true;
      self.searchCondition.pageSize = self.paginationConf.itemsPerPage;
      self.searchCondition.pageNum = self.paginationConf.currentPage;
      MetaService.searchMetas(self.searchCondition).then(function (resp) {
	    self.loading = false;
        if (resp && resp.code === 0) {
          self.paginationConf.totalItems = resp.result.total;
          self.list = resp.result.list;
          self.searching = false;
        } else {
          ToastService.toast({
            type: "error",
            body: '获取元数据失败'
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

  }
  
  function MetaEditCtrl($scope, $uibModalInstance, ToastService, HttpService, $log, metaConfig, MetaService, ModalService, META_TYPE, VALUE_TYPE) {
	  var self = this;
	  
	  self.init = function () {
		  self.metaType = META_TYPE;
		  self.valueType = VALUE_TYPE;
		  self.title = "添加元数据";
		  self.currentMeta = {values:[{},{},{}]};
		  if(metaConfig.model=="update") {
			  self.currentMeta = metaConfig.meta;
			  self.title = "编辑元数据";
		  }
	  };
	  
	  self.addOneValue = function () {
		  self.currentMeta.values.push({});
	  };
	  
	  self.deleteOneValue = function (value) {
		  ModalService.openAlert({
	        title: '提示',
	        message: '您是否要删除该元数据值?'
	      }).then(function (code) {
	    	  if (code === 0) {
	    		  if(!value.id) {
	    			  for(var i =0;i<self.currentMeta.values.length;i++) {
	    				  var v = self.currentMeta.values[i];
	    				  if(value == v) {
	    					  self.currentMeta.values.splice(i,1);
	    					  return;
	    				  }
	    			  }
	    			  return;
	    		  }
	    		  MetaService.deleteMetaValue(value.id).then(function(resp){
	    			  if (resp && resp.code === 0) {
	    		          ToastService.toast({
	    			            type: 'success',
	    			            body: '删除成功'
	    		          });
	    				  for(var i =0;i<self.currentMeta.values.length;i++) {
	    					  var v = self.currentMeta.values[i];
	    					  if(value.id == v.id) {
	    						  self.currentMeta.values.splice(i,1);
	    					  }
	    				  }
	    			  } else {
	    		          ToastService.toast({
	    		            type: 'error',
	    		            body: '删除失败'
	    		          });
	    			  }
	    	      }).catch(function (err) {
	    	        ToastService.errHandler(err);
	    	      });
	    	  }
	      }).catch(function (err) {
	          $log.info(err);
	      });	
	  };
	  
	  self.hasSelectedMetaType = function (){
		if(self.currentMeta.type) {
			return true;
		}  
		return false;
	  };
	  
	  self.hasSelectedValueType = function () {
		if(self.currentMeta.valueType) {
			return true;
		}  
		return false;
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
		  if(!self.currentMeta){
			  self.init();
			  return;
		  }
		  var flag = false;
		  if(!self.currentMeta.type) {
		      ToastService.toast({
		        type: 'error',
		        body: '请先选择元数据类型'
		      });
		      flag = true;
		  }
		  if(!self.currentMeta.valueType) {
		      ToastService.toast({
		        type: 'error',
		        body: '请先选择元数据值类型'
		      });
		      flag = true;
		  }
		  if(flag) {
			  return;
		  }
		  //把无name元数据值清空
		  for(var i=0;i<self.currentMeta.values.length;i++) {{
			  var value = self.currentMeta.values[i];
			  if(!value.name) {
				  self.currentMeta.values.splice(i,1);
				  i--;
			  }
		  }}
		  var p;
		  delete self.currentMeta.user;
		  if(metaConfig.model=="update") {
			  p = MetaService.updateMeta(self.currentMeta);
		  } else {
			  p = MetaService.addMeta(self.currentMeta);
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
	  
	  self.init();
	  
  };
  
  function MetaViewerCtrl($scope, $uibModalInstance, ToastService, $log, metaConfig, MetaService) {
	  var self = this;
	  
	  self.init = function () {
		  self.title = metaConfig.title;
		  self.currentMeta = metaConfig.meta;
	  };
	  
	  self.cancel = function () {
	      $uibModalInstance.dismiss('cancel');
	  };

	  self.init();
	  
  };


})(angular, CONTEXT_PATH);