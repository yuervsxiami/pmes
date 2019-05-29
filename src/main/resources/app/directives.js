/**
 * Created by xiongwei on 2017/1/3.
 */

(function (angular, contextPath) {
	'use strict';

	angular.module('DIRECTIVES', ['SERVICES', 'ui.bootstrap'])
			.directive('tmPagination', [function () {
				return {
					restrict: 'EA',
					template: '<div class="page-list pull-right" style="">' +
					'<ul class="pagination pull-left" ng-show="conf.totalItems > 0">' +
					'<li ng-class="{disabled: conf.currentPage == 1}" ng-click="prevPage()"><span>&laquo;</span></li>' +
					'<li ng-repeat="item in pageList track by $index" ng-class="{active: item == conf.currentPage, separate: item == \'...\'}" ' +
					'ng-click="changeCurrentPage(item)">' +
					'<span style="cursor: pointer">{{ item }}</span>' +
					'</li>' +
					'<li ng-class="{disabled: conf.currentPage == conf.numberOfPages}" ng-click="nextPage()"><span>&raquo;</span></li>' +
					'</ul>' +
					'<div class="page-total pull-left" style="margin: 25px 0 0 10px" ng-show="conf.totalItems > 0">' +
					'第<input type="text" ng-model="jumpPageNum" style="width:40px;text-align:center" ng-keyup="jumpToPage($event)"/>页 ' +
					'每页<select ng-model="conf.itemsPerPage" ng-options="option for option in conf.perPageOptions " ng-change="changeItemsPerPage()"></select>' +
					'/共<strong>{{ conf.totalItems }}</strong>条' +
					'</div>' +
					'<div class="no-items" ng-show="conf.totalItems <= 0"></div>' +
					'</div>',
					replace: true,
					scope: {
						conf: '='
					},
					link: function (scope, element, attrs) {

						// 变更当前页
						scope.changeCurrentPage = function (item) {
							if (item == '...') {
								return;
							} else {
								scope.conf.currentPage = item;
							}
						};

						// if (!scope.conf.rememberPerPage) {
						//   scope.conf.rememberPerPage = 'perPageOption';
						// }

						// 定义分页的长度必须为奇数 (default:9)
						scope.conf.pagesLength = parseInt(scope.conf.pagesLength) ? parseInt(scope.conf.pagesLength) : 9;
						if (scope.conf.pagesLength % 2 === 0) {
							// 如果不是奇数的时候处理一下
							scope.conf.pagesLength = scope.conf.pagesLength - 1;
						}

						// conf.erPageOptions
						if (!scope.conf.perPageOptions) {
							scope.conf.perPageOptions = [10, 20, 30, 40, 50, 60];
						}

						// pageList数组
						function getPagination() {
							// conf.currentPage
							scope.conf.currentPage = parseInt(scope.conf.currentPage) ? parseInt(scope.conf.currentPage) : 1;
							// conf.totalItems
							scope.conf.totalItems = parseInt(scope.conf.totalItems);

							// conf.itemsPerPage (default:15)
							// 先判断一下本地存储中有没有这个值
							if (scope.conf.rememberPerPage) {
								if (!parseInt(localStorage[scope.conf.rememberPerPage])) {
									localStorage[scope.conf.rememberPerPage] = parseInt(scope.conf.itemsPerPage) ? parseInt(scope.conf.itemsPerPage) : 15;
								}
								scope.conf.itemsPerPage = parseInt(localStorage[scope.conf.rememberPerPage]);

							} else {
								scope.conf.itemsPerPage = parseInt(scope.conf.itemsPerPage) ? parseInt(scope.conf.itemsPerPage) : 10;
							}

							// numberOfPages
							scope.conf.numberOfPages = Math.ceil(scope.conf.totalItems / scope.conf.itemsPerPage);

							// judge currentPage > scope.numberOfPages
							if (scope.conf.currentPage < 1) {
								scope.conf.currentPage = 1;
							}

							if (scope.conf.currentPage > scope.conf.numberOfPages) {
								scope.conf.currentPage = scope.conf.numberOfPages;
							}

							// jumpPageNum
							scope.jumpPageNum = scope.conf.currentPage;

							// 如果itemsPerPage在不在perPageOptions数组中，就把itemsPerPage加入这个数组中
							if (!scope.conf.perPageOptions) {
								scope.conf.perPageOptions = [10, 20, 30, 40, 50, 60];
							}
							var perPageOptionsLength = scope.conf.perPageOptions.length;
							// 定义状态
							var perPageOptionsStatus;
							for (var i = 0; i < perPageOptionsLength; i++) {
								if (scope.conf.perPageOptions[i] == scope.conf.itemsPerPage) {
									perPageOptionsStatus = true;
								}
							}
							// 如果itemsPerPage在不在perPageOptions数组中，就把itemsPerPage加入这个数组中
							if (!perPageOptionsStatus) {
								scope.conf.perPageOptions.push(scope.conf.itemsPerPage);
							}

							// 对选项进行sort
							scope.conf.perPageOptions.sort(function (a, b) {
								return a - b;
							});

							scope.pageList = [];
							scope.conf.pagesLength = parseInt(scope.conf.pagesLength) ? parseInt(scope.conf.pagesLength) : 9
							if (scope.conf.numberOfPages <= scope.conf.pagesLength) {
								// 判断总页数如果小于等于分页的长度，若小于则直接显示
								for (i = 1; i <= scope.conf.numberOfPages; i++) {
									scope.pageList.push(i);
								}
							} else {
								// 总页数大于分页长度（此时分为三种情况：1.左边没有...2.右边没有...3.左右都有...）
								// 计算中心偏移量
								var offset = (scope.conf.pagesLength - 1) / 2;
								if (scope.conf.currentPage <= offset) {
									// 左边没有...
									for (i = 1; i <= offset + 1; i++) {
										scope.pageList.push(i);
									}
									scope.pageList.push('...');
									scope.pageList.push(scope.conf.numberOfPages);
								} else if (scope.conf.currentPage > scope.conf.numberOfPages - offset) {
									scope.pageList.push(1);
									scope.pageList.push('...');
									for (i = offset + 1; i >= 1; i--) {
										scope.pageList.push(scope.conf.numberOfPages - i);
									}
									scope.pageList.push(scope.conf.numberOfPages);
								} else {
									// 最后一种情况，两边都有...
									scope.pageList.push(1);
									scope.pageList.push('...');

									for (i = Math.ceil(offset / 2); i >= 1; i--) {
										scope.pageList.push(scope.conf.currentPage - i);
									}
									scope.pageList.push(scope.conf.currentPage);
									for (i = 1; i <= offset / 2; i++) {
										scope.pageList.push(scope.conf.currentPage + i);
									}

									scope.pageList.push('...');
									scope.pageList.push(scope.conf.numberOfPages);
								}
							}

							if (scope.conf.onChange) {
								scope.conf.onChange();
							}
							scope.$parent.conf = scope.conf;
						}

						// prevPage
						scope.prevPage = function () {
							if (scope.conf.currentPage > 1) {
								scope.conf.currentPage -= 1;
							}
						};
						// nextPage
						scope.nextPage = function () {
							if (scope.conf.currentPage < scope.conf.numberOfPages) {
								scope.conf.currentPage += 1;
							}
						};

						// 跳转页
						scope.jumpToPage = function () {
							scope.jumpPageNum = scope.jumpPageNum.replace(/[^0-9]/g, '');
							if (scope.jumpPageNum !== '') {
								scope.conf.currentPage = scope.jumpPageNum;
							}
						};

						// 修改每页显示的条数
						scope.changeItemsPerPage = function () {
							// 清除本地存储的值方便重新设置
							if (scope.conf.rememberPerPage) {
								localStorage.removeItem(scope.conf.rememberPerPage);
							}
						};

						scope.$watch(function () {
							var newValue = scope.conf.currentPage + ' ' + scope.conf.totalItems + ' ';
							// 如果直接watch perPage变化的时候，因为记住功能的原因，所以一开始可能调用两次。
							//所以用了如下方式处理
							if (scope.conf.rememberPerPage) {
								// 由于记住的时候需要特别处理一下，不然可能造成反复请求
								// 之所以不监控localStorage[scope.conf.rememberPerPage]是因为在删除的时候会undefind
								// 然后又一次请求
								if (localStorage[scope.conf.rememberPerPage]) {
									newValue += localStorage[scope.conf.rememberPerPage];
								} else {
									newValue += scope.conf.itemsPerPage;
								}
							} else {
								newValue += scope.conf.itemsPerPage;
							}
							return newValue;

						}, getPagination);

					}
				};
			}])
			.directive('ckeditor', function () {
				return {
					require: '?ngModel',
					link: function (scope, element, attrs, ngModel) {
						var ckeditor = CKEDITOR.replace(element[0], {});
						if (!ngModel) {
							return;
						}
						ckeditor.on('instanceReady', function () {
							ckeditor.setData(ngModel.$viewValue);
						});
						ckeditor.on('pasteState', function () {
							scope.$apply(function () {
								ngModel.$setViewValue(ckeditor.getData());
							});
						});
						ngModel.$render = function (value) {
							ckeditor.setData(ngModel.$viewValue);
						};
					}
				};
			})
			/**
			 * The ng-thumb directive
			 * @author: nerv
			 * @version: 0.1.2, 2014-01-09
			 */
			.directive('ngThumb', ['$window', function ($window) {
				var helper = {
					support: !!($window.FileReader && $window.CanvasRenderingContext2D),
					isFile: function (item) {
						return angular.isObject(item) && item instanceof $window.File;
					},
					isImage: function (file) {
						var type = '|' + file.type.slice(file.type.lastIndexOf('/') + 1) + '|';
						return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
					}
				};

				return {
					restrict: 'A',
					template: '<canvas/>',
					link: function (scope, element, attributes) {
						if (!helper.support) return;

						var params = scope.$eval(attributes.ngThumb);

						if (!helper.isFile(params.file)) return;
						if (!helper.isImage(params.file)) return;

						var canvas = element.find('canvas');
						var reader = new FileReader();

						reader.onload = onLoadFile;
						reader.readAsDataURL(params.file);

						function onLoadFile(event) {
							var img = new Image();
							img.onload = onLoadImage;
							img.src = event.target.result;
						}

						function onLoadImage() {
							var width = params.width || this.width / this.height * params.height;
							var height = params.height || this.height / this.width * params.width;
							canvas.attr({width: width, height: height});
							canvas[0].getContext('2d').drawImage(this, 0, 0, width, height);
						}
					}
				};
			}])
			//调整box最小高度
			.directive('resize', ['$window', function ($window) {
				return function (scope, element) {
					var w = angular.element($window);
					scope.getWindowDimensions = function () {
						return {'h': w.height(), 'w': w.width(), 't': w.scrollTop()};
					};
					scope.$watch(scope.getWindowDimensions, function (newValue, oldValue) {
						scope.windowHeight = newValue.h;
						scope.windowWidth = newValue.w;
						scope.scrollTop = newValue.t;
						scope.style = function () {
							return {
								'height': (newValue.h - 100) + 'px',
								'width': (newValue.w - 100) + 'px'
							};
						};
						var headerHeight = 106;
						if (scope.windowWidth < 1000) {
							headerHeight = 137;
						}
						if (scope.windowWidth < 770) {
							headerHeight = 187;
						}
						var footerHeight = 95;
						if (scope.windowWidth < 482) {
							headerHeight = 115;
						}
						element.css('min-height',
								(scope.windowHeight - headerHeight - footerHeight) + 'px');
					}, true);
					// function resize() {
					//   var h = $(".box.box-primary").height();
					//   if (h >= 750 || h <= 600) {
					//     $(".page-list").css({"position": "static"});
					//   } else {
					//     $(".page-list").css({"position": "absolute"});
					//   }
					// }
					w.bind('resize', function () {
						// resize();
						scope.$apply();
					});
				};
			}])
			// 元数据select单选
			.directive('metaSelector', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/components/meta-selector.html',
					controller: ['$scope', '$log', 'MetaService', MetaSelectorCtrl],
					scope: {
						classes: '@',
						metaKey: '@',
						disabled: '=',
						model: '=',
						emptyTitle: '@',
						emptyValue: '@',
						changeFunction: '@'
					}
				};
			})
			// 元数据checkbox多选
			.directive('metaCheckbox', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/components/meta-checkbox.html',
					controller: ['$scope', '$log', 'MetaService', MetaCheckboxCtrl],
					scope: {
						metaKey: '@',
						checkAllTitle: '@',
						labelDirection: '@',
						allChecked: '@',
						onSelectionChanged: '&'
					}
				};
			})
			// 元数据checkbox多选2
			.directive('metaCheck', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/components/meta-check.html',
					controller: ['$scope', '$log', 'MetaService', MetaCheckCtrl],
					scope: {
						model: '=',
						metaKey: '@',
						checkAllTitle: '@',
						labelDirection: '@',
						allChecked: '@',
						onSelectionChanged: '&'
					}
				};
			})
			// Order 元数据td
			.directive('orderMetaSpan', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/components/order-meta-span.html',
					controller: ['$scope', '$log', 'MetaService', OrderMetaSpanCtrl],
					scope: {
						label: '='
					}
				};
			})
			// Order 元数据select单选
			.directive('orderMetaSelector', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/components/order-meta-selector.html',
					controller: ['$scope', '$log', 'MetaService', OrderMetaSelectorCtrl],
					scope: {
						classes: '@',
						metaKey: '@',
						isDisabled: '@',
						model: '=',
						emptyTitle: '@',
						emptyValue: '@',
						itemName: '@',
					}
				};
			})
			.directive('datetimeRange', function () {
				return {
					restrict: 'EA',
					replace: true,
					template: '<input type="text" class="form-control" />',
					scope: {
						onDatetimeRangeChanged: '&',
						startDate: '=',
						endDate: '=',
						maxDate: '=',
					},
					link: function (scope, element, attrs) {
						$(element).daterangepicker({
							startDate: moment(scope.startDate),
							endDate: moment(scope.endDate),
							maxDate: scope.maxDate ? moment(scope.maxDate) : false,
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
							if (scope.onDatetimeRangeChanged)
								scope.onDatetimeRangeChanged({start: start, end: end});
						}).next().on('click', function () {
							$(this).prev().focus();
						});
					}
				};
			})
			.directive('myDatetimeRange', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/directives/myDatetimeRangePicker.html',
					scope: {
						onDatetimeRangeChanged: '&',
						startDate: '=',
						endDate: '=',
					},
					link: function (scope, element) {
						var input1 = element.find('#d11');
						var input2 = element.find('#d12');

						function onChanged() {
							var start = input1.val() ? moment(input1.val()).startOf('day') : undefined;
							var end = input2.val() ? moment(input2.val()).endOf('day') : undefined;
							if (scope.onDatetimeRangeChanged) {
								scope.onDatetimeRangeChanged({start: start, end: end});
							}
						}

						input1.on('click', function () {
							WdatePicker({
								el: input1[0],
								firstDayOfWeek: 1,
								maxDate: '#F{$dp.$D(\'d12\')}',
								onpicked: onChanged,
								oncleared: onChanged,
							});
						});
						input2.on('click', function () {
							WdatePicker({
								el: input2[0],
								firstDayOfWeek: 1,
								minDate: '#F{$dp.$D(\'d11\')}',
								onpicked: onChanged,
								oncleared: onChanged,
							});
						});
					}
				};
			})
			.directive('myDatetime', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/directives/myDatetimePicker.html',
					scope: {
						date: '=',
						placeholder: "@",
					},
					link: function (scope, element) {
						var input1 = element.find('#date1');

						input1.attr('placeholder', scope.placeholder);

						function onChanged() {
							var start = input1.val() ? moment(input1.val()).startOf('day') : undefined;
							scope.date = moment(input1.val()).format('YYYY.MM.DD');
						}

						input1.on('click', function () {
							WdatePicker({
								el: input1[0],
								firstDayOfWeek: 1,
								onpicked: onChanged,
								oncleared: onChanged,
							});
						});

						scope.$watch(function() {
							return scope.date;
						}, function() {
							if(!scope.date) {
								input1.val("");
							}
						})
					}
				};
			})
			.directive('sliderValue', function () {
				return {
					restrict: 'EA',
					replace: true,
					template: '<input type="text" class="form-control slider" />',
					scope: {
						model: '=',
						min: '@',
						max: '@',
						onChange: '&'
					},
					link: function (scope, element) {
						scope.slider = $(element).slider({
							min: scope.min || 0,
							max: scope.max || 100,
							value: scope.model.weight
						});
						scope.$watch('model.weight', function () {
							scope.slider.slider('setValue', scope.model.weight);
						});
						scope.slider.on('change', function (e) {
							var newValue = e.value.newValue;
							var changed = e.value.newValue - e.value.oldValue;
							scope.$apply(function () {
								scope.model.weight = newValue;
							});
							if (scope.onChange) {
								scope.onChange({label: scope.model, change: changed});
							}
						});
					}
				};
			})
			.directive('echart', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/directives/echart.html',
					scope: {
						width: '@',
						height: '@',
						options: '=',
						onChartAware: '&',
					},
					link: function (scope, element) {
						var echart_container = element[0];
						$(echart_container).css('width', scope.width || '400px');
						$(echart_container).css('min-height', scope.height || '300px');
						if (!scope.chart) {
							scope.chart = echarts.init(echart_container);
						}
						scope.chart.setOption(scope.options, true);

						scope.$watch('options', function (newVal) {
							scope.chart.setOption(scope.options);
						});
						if (scope.onChartAware) {
							scope.onChartAware({chart: scope.chart});
						}
					}
				};
			})
			.directive('twoLevelCheck', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/directives/twoLevelCheck.html',
					controller: ['$scope', '$log', '$uibPosition', 'ModalService', 'ToastService', 'ExpertService', '$timeout', twoLevelCheckCtrl],
					scope: {
						mode: '=',
						defaultValue: '=',
						selectedValue: '=',
						showValue: '='
					},
					link: function (scope, element) {
					}
				}
			})
	   	.directive('tree', function () {
        return {
      require: '?ngModel',
      restrict: 'A',
      link: function ($scope, element, attrs, ngModel) {
        var setting = {
          async: {
            enable: true,
            // url:"../asyncData/getNodes.php",
            // autoParam:["id", "name=n", "level=lv"],
            // otherParam:{"otherParam":"zTreeAsyncTest"},
            dataFilter: filter
          },
          view: {expandSpeed:"",
            addHoverDom: addHoverDom,
            removeHoverDom: removeHoverDom,
            selectedMulti: false,
            showIcon: false,
            // dblClickExpand: false
          },
          edit: {
            enable: true
          },
          data: {
            simpleData: {
              enable: true
            }
          },
          callback: {
            beforeRemove: beforeRemove,
            onClick: function (event, treeId, treeNode, clickFlag,id) {
              $scope.$apply(function () {
              	//获取数据的id 调用数据接口
                // console.log(treeNode);
              });
            }
          }
        };
        function filter(treeId, parentNode, childNodes) {
          if (!childNodes) return null;
          for (var i=0, l=childNodes.length; i<l; i++) {
            childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
          }
          return childNodes;
        }
        function beforeRemove(treeId, treeNode) {
          var zTree = $.fn.zTree.getZTreeObj("treeDemo");
          zTree.selectNode(treeNode);
          if (confirm("确认删除 节点 -- " + treeNode.name + " 吗？")) {
            var treeInfo = treeNode.id;
            $.ajax({
              url: "Ajax.aspx?_tid=" + treeInfo + "&action=Remove",
              type: "POST",
              async: false,
              success: function (res) {
                if (res = "success") {
                  alert('删除成功!');
                } else {
                  alert('删除失败!');
                }
              }
            });
          }
        }
        var newCount = 1;
        function addHoverDom(treeId, treeNode) {
          var sObj = $("#" + treeNode.tId + "_span");
          if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
          var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
            + "' title='add node' onfocus='this.blur();'></span>";
          sObj.after(addStr);
          var btn = $("#addBtn_"+treeNode.tId);
          if (btn) btn.bind("click", function(id){
            window.location.href="#!/search/group/childGroup/"+id;
            return false;
          });
        };
        function removeHoverDom(treeId, treeNode) {
          $("#addBtn_"+treeNode.tId).unbind().remove();
        };
        var zNodes = [
          { "name" : "总公司", "age" : "29", "id" : "0","children" : [
            { "name" : "市场销售部", "age" : "21", "children" : [
              { "name" : "销售部", "age" : "42", "children" : [] },
              { "name" : "市场部", "age" : "21", "children" : [
                { "name" : "国内市场部", "age" : "23", "children" : [
                  { "name" : "华东市场", "age" : "32", "children" : [] },
                  { "name" : "中部市场", "age" : "34", "children" : [] }
                ]}
              ]}
            ]},
            { "name" : "财务部", "age" : "33", "children" : [
              { "name" : "会计部", "age" : "25", "children" : [] },
              { "name" : "出纳部", "age" : "28", "children" : [] }
            ]},
            { "name" : "法务部", "age" : "29", "children" : [] }
          ] }]

          $.fn.zTree.init($("#treeDemo"), setting,zNodes);
      }
    };
	   	})
			.directive('nodeTree', function () {
				return {
					require: '?ngModel',
					restrict: 'A',
					link: function ($scope, element, attrs, ngModel) {
            var setting = {
              check: {
                enable: true
              },
              data: {
                simpleData: {
                  enable: true
                }
              },
              callback: {
                onCheck: zTreeOnCheck
              }
            };
            setting.check.chkboxType = { "Y" : "p", "N" : "ps" };
            var zNodes = [
              { "name" : "总公司", "age" : "29", "id" : "0","children" : [
                { "name" : "市场销售部", "age" : "21", "children" : [
                  { "name" : "销售部", "age" : "42", "children" : [] },
                  { "name" : "市场部", "age" : "21", "children" : [
                    { "name" : "国内市场部", "age" : "23", "children" : [
                      { "name" : "华东市场", "age" : "32", "children" : [] },
                      { "name" : "中部市场", "age" : "34", "children" : [] }
                    ]}
                  ]}
                ]},
                { "name" : "财务部", "age" : "33", "children" : [
                  { "name" : "会计部", "age" : "25", "children" : [] },
                  { "name" : "出纳部", "age" : "28", "children" : [] }
                ]},
                { "name" : "法务部", "age" : "29", "children" : [] }
              ] }]
            function zTreeOnCheck(event, treeId, treeNode) {
              console.log(treeNode.tId + ", " + treeNode.name + "," + treeNode.checked);
            };
						$.fn.zTree.init($("#treeDemo"), setting, zNodes);


					}
				};
			})
			.directive('filterSelect', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/directives/filterSelect.html',
					controller: ['$scope', '$log', '$uibPosition', 'ModalService', 'ToastService', 'ExpertService', '$timeout', filterSelectCtrl],
					scope: {
						data: '=',
						list: '=',
					},
					link: function (scope, element) {
						var ul = element.find('.filterSelect ul').css('width', element.find('input').css('width'));
					}
				};
			})
	;


	function MetaCheckCtrl($scope, $log, MetaService) {
		$scope.checkAll = Boolean($scope.allChecked);
		if($scope.model) {
			$scope.defaultValueList = $scope.model.split(",");
		}

		MetaService.getMeta($scope.metaKey).then(function (resp) {
			if (resp && resp.code === 0) {
				$scope.meta = resp.result;
				switch ($scope.meta.valueType) {
					case 5:
						angular.forEach($scope.meta.values, function (value) {
							value.value = parseInt(value);
						});
						break;
					case 6:
						angular.forEach($scope.meta.values, function (value) {
							value.value = Boolean(value);
						});
						break;
					default:
						break;
				}
				angular.forEach($scope.meta.values, function (value) {
					value.selected = $scope.checkAll;
					if($scope.defaultValueList) {
						for(var i=0; i < $scope.defaultValueList.length; i++) {
							if($scope.defaultValueList[i] == value.name) {
								value.selected = true;
							}
						}
					}
				});
			}
		}).catch(function (err) {
			$log.error($scope.metaKey, err);
		});
		$scope.$watch(function () {
			// 王志斌 1.27
			if (!$scope.meta || !$scope.meta.values) {
				return '';
			}
			return $scope.meta.values.map(function (value) {
				return value.selected;
			}).join(',');
		}, function () {
			// 王志斌 1.27
			if (!$scope.meta || !$scope.meta.values) {
				return false;
			}
			var list = [];
			$scope.meta.values.map(function (value) {
				if(value.selected) {
					list.push(value.name);
				}
			});
			$scope.model = list.join(',') || "";
		});
	}

	function MetaSelectorCtrl($scope, $log, MetaService) {
		MetaService.getMeta($scope.metaKey).then(function (resp) {
			if (resp && resp.code === 0) {
				$scope.meta = resp.result;
				switch ($scope.meta.valueType) {
					case 5:
						angular.forEach($scope.meta.values, function (value) {
							value.value = parseInt(value);
						});
						break;
					case 6:
						angular.forEach($scope.meta.values, function (value) {
							value.value = Boolean(value);
						});
						break;
					default:
						break;
				}
			}
		}).catch(function (err) {
			$log.error($scope.metaKey, err);
		})
	}

	function MetaCheckboxCtrl($scope, $log, MetaService) {
		$scope.checkAll = Boolean($scope.allChecked);
		if($scope.model) {
			$scope.defaultValueList = $scope.model.split(",");
		}

		MetaService.getMeta($scope.metaKey).then(function (resp) {
			if (resp && resp.code === 0) {
				$scope.meta = resp.result;
				switch ($scope.meta.valueType) {
					case 5:
						angular.forEach($scope.meta.values, function (value) {
							value.value = parseInt(value);
						});
						break;
					case 6:
						angular.forEach($scope.meta.values, function (value) {
							value.value = Boolean(value);
						});
						break;
					default:
						break;
				}
				angular.forEach($scope.meta.values, function (value) {
					value.selected = $scope.checkAll;
				});
			}
		}).catch(function (err) {
			$log.error($scope.metaKey, err);
		});
		$scope.$watch(function () {
			// 王志斌 1.27
			if (!$scope.meta || !$scope.meta.values) {
				return '';
			}
			return $scope.meta.values.map(function (value) {
				return value.selected;
			}).join(',');
		}, function () {
			// 王志斌 1.27
			if (!$scope.meta || !$scope.meta.values) {
				return false;
			}
			var selected = $scope.meta.values.filter(function (value) {
				return value.selected;
			});
			if ($scope.onSelectionChanged) {
				$scope.onSelectionChanged({selected: selected});
			}
		});
		$scope.toggleCheckAll = function () {
			var all = $scope.shouldAllChecked();
			angular.forEach($scope.meta.values, function (value) {
				value.selected = !all;
			});
		};
		$scope.shouldAllChecked = function () {
			// 王志斌 1.27
			if (!$scope.meta || !$scope.meta.values) {
				return false;
			}
			var selected = $scope.meta.values.filter(function (value) {
				return value.selected;
			});
			return selected.length === $scope.meta.values.length;
		};
	}

	function OrderMetaSpanCtrl($scope, $log, MetaService) {
		$scope.value = '-';
		if(!$scope.label.label.metaKey) {
			return;
		}
		MetaService.getMeta($scope.label.label.metaKey).then(function (resp) {
			if (resp && resp.code === 0) {
				$scope.meta = resp.result;
				$scope.label.meta = resp.result;
				angular.forEach($scope.meta.values, function (value) {
					var v = $scope.label.strValue || $scope.label.textValue;
					if(v == value.value || v == value.name) {
						$scope.value = value.name;
					}
				});
			}
		}).catch(function (err) {
			$log.error($scope.metaKey, err);
		})
	}

	function OrderMetaSelectorCtrl($scope, $log, MetaService) {
		MetaService.getMeta($scope.metaKey).then(function (resp) {
			if (resp && resp.code === 0) {
				$scope.meta = resp.result;
				switch ($scope.meta.valueType) {
					case 5:
						angular.forEach($scope.meta.values, function (value) {
							value.value = parseInt(value);
						});
						break;
					case 6:
						angular.forEach($scope.meta.values, function (value) {
							value.value = Boolean(value);
						});
						break;
					default:
						break;
				}
			}
		}).catch(function (err) {
			$log.error($scope.metaKey, err);
		})
	}

	function twoLevelCheckCtrl($scope, $log, $uibPosition, ModalService, ToastService, ExpertService, $timeout) {
		var self = this;

		self.init = function () {
			self.mode = $scope.mode;
			$timeout(function () {
				$scope.selectedList = $scope.defaultValue || [];

				if(self.mode == 'spec') {//特长领域
					self.getAllNationals();
				} else if(self.mode == 'ipc') {//ipc领域
					self.getAllIpc();
				}

				$scope.$watch(function(){
					return $scope.selectedList.length;
				},function () {
					var codeList = [];
					var nameList = [];
					for(var i=0; i<$scope.selectedList.length; i++) {
						codeList.push($scope.selectedList[i].code)
						nameList.push($scope.selectedList[i].name)
					}
					$scope.selectedValue = codeList.join(',');
					$scope.showValue = nameList.join(',');
				});
			}, 500);
		};

		$scope.changeSon = function (item) {
			if($scope.mode == 'spec') {//特长领域
				$scope.sonList = item.sons;
			}
			if($scope.mode == 'ipc') {//ipc领域
				$scope.sonList = item.sonIPCFields;
			}
		};

		$scope.selectOne = function (item) {
			for(var i=0; i<$scope.selectedList.length; i++) {
				if(item.id == $scope.selectedList[i].id) {
					ToastService.toast({
						type: 'info',
						body: '该领域已被选择,无需重复选择'
					});
					return;
				}
			}
			$scope.selectedList.push(item);
		};

		$scope.deleteOne = function (item) {
			for(var i=0; i<$scope.selectedList.length; i++) {
				if(item.id == $scope.selectedList[i].id) {
					$scope.selectedList.splice(i,1);
					return;
				}
			}
		};

		self.getAllNationals = function () {
			$scope.loading = true;
			ModalService.getAllNationals().then(function (resp) {
				$scope.loading = false;
				if (resp && resp.code === 0) {
					self.nationals = resp.result;
					self.t1s = [];
					self.nationals.forEach(function (data) {
						if (data.parentId == 0 || data.parentId == null) {
							data.sons = [];
							for(var i=0; i<self.nationals.length; i++) {
								if(self.nationals[i].parentId == data.id) {
									data.sons.push(self.nationals[i]);
								}
							}
							self.t1s.push(data)
						}
					});
					$scope.list = self.t1s;
				} else {
					ToastService.toast({
						type: 'error',
						body: '请求国名经济代码失败:' + resp.message
					});
				}
			}).catch(function (err) {
				$scope.loading = false;
				ToastService.errHandler(err);
			});
		};

		self.getAllIpc = function () {
			$scope.loading = true;
			ExpertService.getAllIPC().then(function (resp) {
				$scope.loading = false;
				if (resp && resp.code === 0) {
					self.ipcs = resp.result;
					$scope.list = self.ipcs;
				} else {
					ToastService.toast({
						type: 'error',
						body: '请求IPC领域失败:' + resp.message
					});
				}
			}).catch(function (err) {
				$scope.loading = false;
				ToastService.errHandler(err);
			});
		};

		self.init();
	}

	function filterSelectCtrl($scope, $log, $uibPosition, ModalService, ToastService, ExpertService, $timeout) {

		$scope.init = function (){
			$scope.selecting = false;
		};

		$scope.start = function () {
			$scope.selecting = true;
		};

		$scope.end = function () {
			$timeout(function () {
				$scope.selecting = false;
			}, 150);
		};

		$scope.select = function (college) {
			$scope.data = college.name;
			$scope.selecting = false;
		};

		$scope.init();

	}


})(angular, CONTEXT_PATH);