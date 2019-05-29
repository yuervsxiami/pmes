/**
 * Created by Crixalis on 2018/9/27.
 * 统计相关的指令
 */

(function (angular, contextPath) {
	'use strict';

	angular.module('DIRECTIVES')
			.directive('statDetail', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/directives/stat.detail.html',
					controller: ['$scope', '$log', 'StatService', StatDetailCtrl],
					scope: {
						orderType: '@', // process, task
						statType: '@' // due,alert,processing,done
					},
					link: function (scope, element) {
					}
				};
			})
			.directive('statProcess', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/directives/stat.process.html',
					controller: ['$scope', '$log', 'StatService', StatProcessCtrl],
					scope: {
						processId: '=' //true false
					},
					link: function (scope, element) {
					}
				};
			})
			.directive('statUseTime', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/directives/stat.useTime.html',
					controller: ['$scope', '$log', '$uibPosition', '$timeout', 'StatService', 'changeTaskTypeFilter', StatUseTimeCtrl],
					scope: {
						processId: '=', //true false
						cpId: '='
					},
					link: function (scope, element) {
					}
				};
			})
			.directive('statRemindNum', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/directives/stat.remindNum.html',
					controller: ['$scope', '$log', '$uibPosition', '$timeout', 'StatService', 'changeTaskTypeFilter', 'processTypeFilter', StatAlertCtrl],
					scope: {
						processId: '=', // 2~9
						isAlert: '=', //true false
						cpId: '='
					},
					link: function (scope, element) {
					}
				};
			})
			.directive('statRemindUser', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/directives/stat.remindUser.html',
					controller: ['$scope', '$log', '$uibPosition', '$timeout', 'StatService', 'changeTaskTypeFilter', 'processTypeFilter', StatAlertUserCtrl],
					scope: {
						processId: '=', // 2~9
						isAlert: '=', //true false
						cpId: '='
					},
					link: function (scope, element) {
					}
				};
			})
			// 根据高校专利量进行柱状图排名
			.directive('collegePatentSort', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/statistics/area/collegePatentSort.html',
					controller: ['$scope', '$log', '$uibPosition', '$timeout', 'ChartService', '$state', CollegePatentSortCtrl],
					scope: {
						height: '=',
						ti: '@',
						hasPowerSelect: '@',
					},
				};
			})
			// 通用柱状图指令
			.directive('barGraph', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/directives/barGraph.html',
					controller: ['$scope', '$log', '$uibPosition', 'ModalService', 'ToastService', '$timeout', BarGraphCtrl],
					scope: {
						eTitle: '=',
						xs: '=',
						numList: '=',
						searchTime: '=',
						yName: '='
					},
					link: function (scope, element) {
					}
				}
			})
			// 通用地图指令
			.directive('mapGraph', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/directives/mapGraph.html',
					controller: ['$scope', '$log', '$uibPosition', 'ModalService', 'ToastService', '$timeout', MapGraphCtrl],
					scope: {
						mapName: '=',
						visualMap: '=',
						data: '=',
						searchTime: '=',
						onClickMap: "&",
					},
					link: function (scope, element) {
					}
				}
			})
			// 4.1专利类型饼图
			.directive('collegeTypeStat', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/statistics/college/halfStat.html',
					controller: ['$scope', '$log', '$uibPosition', '$timeout', 'ChartService', 'PATENT_TYPE', CollegeTypeStatCtrl],
					scope: {
						height: '=',
						collegeName: '=',
						ti: '@',
					},
				};
			})
			// 4.2法律状态饼图
			.directive('collegeLegStat', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/statistics/college/halfStat.html',
					controller: ['$scope', '$log', '$uibPosition', '$timeout', 'ChartService', 'PATENT_TYPE', CollegeLegStatCtrl],
					scope: {
						height: '=',
						collegeName: '=',
						ti: '@',
					},
				};
			})
			// 5高校专利申请趋势
			.directive('collegeStatLastTen', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/statistics/college/stat.html',
					controller: ['$scope', '$log', '$uibPosition', '$timeout', 'ChartService', 'PATENT_TYPE', CollegeStatLastTenCtrl],
					scope: {
						height: '=',
						collegeName: '=',
						ti: '@',
					},
				};
			})
			// 6高校技术发展趋势分析
			.directive('collegeIpcTrend', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/statistics/college/stat.html',
					controller: ['$scope', '$log', '$uibPosition', '$timeout', 'ChartService', 'PATENT_TYPE', CollegeIpcTrendCtrl],
					scope: {
						height: '=',
						collegeName: '=',
						ti: '@',
					},
				};
			})
			// 7高校技术热点分布
			.directive('collegeIpcHot', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/statistics/college/statWithCollection.html',
					controller: ['$scope', '$log', '$uibPosition', '$timeout', 'ChartService', 'PATENT_TYPE', CollegeIpcHotCtrl],
					scope: {
						height: '=',
						collegeName: '=',
						ti: '@',
					},
				};
			})
			// 8高校热门领域的高价值专利分布
			.directive('collegeIpcValue', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/statistics/college/stat.html',
					controller: ['$scope', '$log', '$uibPosition', '$timeout', 'ChartService', 'PATENT_TYPE', CollegeIpcValueCtrl],
					scope: {
						height: '=',
						collegeName: '=',
						ti: '@',
					},
				};
			})
			// 9高校核心创新专利
			.directive('collegeByQuoteTotal', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/statistics/college/statWithLimit.html',
					controller: ['$scope', '$log', '$uibPosition', '$timeout', 'ChartService', 'PATENT_TYPE', CollegeByQuoteTotalCtrl],
					scope: {
						height: '=',
						collegeName: '=',
						ti: '@',
					},
				};
			})
			// 10高校联合创新主体分析
			.directive('collegePartner', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/statistics/college/stat.html',
					controller: ['$scope', '$log', '$uibPosition', '$timeout', 'ChartService', 'PATENT_TYPE', CollegePartnerCtrl],
					scope: {
						height: '=',
						collegeName: '=',
						ti: '@',
					},
				};
			})
			// 11高校专利价值分布
			.directive('collegePmesValue', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/statistics/college/stat.html',
					controller: ['$scope', '$log', '$uibPosition', '$timeout', 'ChartService', 'PATENT_TYPE', '$state', CollegePmesValueCtrl],
					scope: {
						height: '=',
						collegeName: '=',
						ti: '@',
						provinceName: '=',
					},
				};
			})
			// 12高校发明人
			.directive('collegePin', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/statistics/college/statWithLimit.html',
					controller: ['$scope', '$log', '$uibPosition', '$timeout', 'ChartService', 'PATENT_TYPE', '$state', CollegePinCtrl],
					scope: {
						height: '=',
						collegeName: '=',
						ti: '@',
						provinceName: '=',
					},
				};
			})
			// 13发明人产学研技术详情
			.directive('pinExpert', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/statistics/pin/pinExpert.html',
					controller: ['$scope', '$log', '$uibPosition', '$timeout', 'ChartService', 'PATENT_TYPE', PinExpertCtrl],
					scope: {
						height: '=',
						collegeName: '=',
						pinName: '=',
						ti: '@',
					},
				};
			})
			// 14发明人团队展示
			.directive('collegePinPartner', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/statistics/pin/stat.html',
					controller: ['$scope', '$log', '$uibPosition', '$timeout', 'ChartService', 'PATENT_TYPE', CollegePinPartnerCtrl],
					scope: {
						height: '=',
						collegeName: '=',
						pinName: '=',
						ti: '@',
					},
				};
			})
			// 15发明人专利价值分布
			.directive('pinPmesValue', function () {
				return {
					restrict: 'EA',
					replace: true,
					templateUrl: contextPath + '/static/tpls/statistics/pin/stat.html',
					controller: ['$scope', '$log', '$uibPosition', '$timeout', 'ChartService', 'PATENT_TYPE', '$state', PinPmesValueCtrl],
					scope: {
						height: '=',
						collegeName: '=',
						pinName: '=',
						ti: '@',
						provinceName: '=',
					},
				};
			})
	;

	function StatAlertUserCtrl($scope, $log, $uibPosition, $timeout, StatService, changeTaskTypeFilter, processTypeFilter) {
		$scope.loading = false;
		$scope.title = processTypeFilter($scope.processId) + ($scope.isAlert ? "预警监测（按人员）" : "超时监测（按人员）");
		$scope.options = {
			title: {
				text: $scope.title,
				left: 'center'
			},
			tooltip: {
				trigger: 'axis'
			},
			grid: {
				top: 50,
				width: '100%',
				bottom: '0%',
				left: 10,
				containLabel: true
			},
			xAxis: {
				type: 'value',
				max: 10887,
				splitLine: {
					show: false
				}
			},
			yAxis: {
				type: 'category',
				data: [],
				axisLabel: {
					interval: 0,
					rotate: 30
				},
				splitLine: {
					show: false
				}
			},
			series: [{
				type: 'bar',
				stack: 'chart',
				z: 3,
				barWidth: 5,
				data: []
			}]
		};
		$scope.onChartAware = function (chart) {
			$scope.chart = chart;
			if ($scope.processId) {
				$scope.loading = true;
				var result = null;
				if ($scope.isAlert) {
					result = StatService.countMaxUserAlert($scope.processId);
				} else {
					result = StatService.countMaxUserDue($scope.processId);
				}
				result.then(function (resp) {
					$scope.loading = false;
					if (resp && resp.code === 0) {
						var max = 10;
						for (var i = 0; i < resp.result.length; i++) {
							var rm = resp.result[i];
							$scope.options.yAxis.data.push(rm.name);
							$scope.options.series[0].data.push(rm.count);
							max = Math.max(max, rm.count);
						}
						$scope.options.xAxis.max = max;
						$scope.chart.clear();
						$scope.chart.setOption($scope.options, true);
						$scope.chart.resize();
					}
				}).catch(function (reason) {
					$scope.loading = false;
				});
			}
		};

		$scope.resize = function (isCurrent) {
			if (isCurrent && $scope.chart) {
				$timeout(function () {
					$scope.chart.resize($uibPosition.position($scope.chart.getDom()));
				}, 100);
			}
		};

		$scope.$watch(function () {
			return $scope.cpId === $scope.processId;
		}, $scope.resize);
	}

	function StatAlertCtrl($scope, $log, $uibPosition, $timeout, StatService, changeTaskTypeFilter, processTypeFilter) {
		$scope.loading = false;
		$scope.title = processTypeFilter($scope.processId) + ($scope.isAlert ? "预警监测（按环节）" : "超时监测（按环节）");
		$scope.options = {
			title: {
				text: $scope.title,
				left: 'center'
			},
			tooltip: {
				trigger: 'axis',
				axisPointer: {            // 坐标轴指示器，坐标轴触发有效
					type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
				}
			},
			xAxis: {
				data: [],
				axisTick: {show: false}
			},
			yAxis: {
				splitLine: {show: false},
				name: '人数',
			},
			animationDurationUpdate: 1200,
			series: [{
				type: 'bar',
				itemStyle: {
					normal: {
						color: '#277EAB'
					}
				},
				silent: true,
				barWidth: 40,
				barGap: '-100%', // Make series be overlap
				data: []
			}]
		};
		$scope.onChartAware = function (chart) {
			$scope.chart = chart;
			if ($scope.processId) {
				$scope.loading = true;
				var result = null;
				if ($scope.isAlert) {
					result = StatService.countAlertNumByProcess($scope.processId);
				} else {
					result = StatService.countDueNumByProcess($scope.processId);
				}
				result.then(function (resp) {
					$scope.loading = false;
					if (resp && resp.code === 0) {
						for (var i = 0; i < resp.result.length; i++) {
							var rm = resp.result[i];
							$scope.options.xAxis.data.push(changeTaskTypeFilter(rm.taskType));
							$scope.options.series[0].data.push(rm.count);
						}
						$scope.chart.clear();
						$scope.chart.setOption($scope.options, true);
						$scope.chart.resize();
					}
				}).catch(function (reason) {
					$scope.loading = false;
				});
			}
		};

		$scope.resize = function (isCurrent) {
			if (isCurrent && $scope.chart) {
				$timeout(function () {
					$scope.chart.resize($uibPosition.position($scope.chart.getDom()));
				}, 100);
			}
		};

		$scope.$watch(function () {
			return $scope.cpId === $scope.processId;
		}, $scope.resize);
	}

	function StatUseTimeCtrl($scope, $log, $uibPosition, $timeout, StatService, changeTaskTypeFilter) {
		$scope.loading = false;
		$scope.title = "完成时间";
		$scope.options = {
			title: {
				text: $scope.title,
				left: 'center'
			},
			tooltip: {
				trigger: 'axis',
				axisPointer: {            // 坐标轴指示器，坐标轴触发有效
					type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
				}
			},
			legend: {
				data: ['最大时间', '平均时间', '最小时间'],
				top: 30
			},
			xAxis: {
				data: [],
				axisTick: {show: false}
			},
			yAxis: {
				splitLine: {show: false},
				name: '时间(小时)',
			},
			animationDurationUpdate: 1200,
			series: [{
				name: '最大时间',
				type: 'bar',
				itemStyle: {
					normal: {
						color: '#277EAB'
					}
				},
				silent: true,
				barWidth: 40,
				barGap: '-100%', // Make series be overlap
				data: []
			}, {
				name: '平均时间',
				type: 'bar',
				barWidth: 40,
				z: 10,
				data: []
			}, {
				name: '最小时间',
				type: 'bar',
				barWidth: 40,
				z: 10,
				data: []
			}]
		};
		$scope.onChartAware = function (chart) {
			$scope.chart = chart;
			if ($scope.processId) {
				$scope.loading = true;
				StatService.countUseTimeByProcess($scope.processId).then(function (resp) {
					$scope.loading = false;
					if (resp && resp.code === 0) {
						for (var i = 0; i < resp.result.length; i++) {
							var ut = resp.result[i];
							$scope.options.xAxis.data.push(changeTaskTypeFilter(ut.taskType));
							$scope.options.series[0].data.push(parseFloat((ut.max || 0) / 3600).toFixed(3));
							$scope.options.series[1].data.push(parseFloat((ut.avg || 0) / 3600).toFixed(3));
							$scope.options.series[2].data.push(parseFloat((ut.min || 0) / 3600).toFixed(3));
						}
						$scope.chart.clear();
						$scope.chart.setOption($scope.options, true);
						$scope.chart.resize();
					}
				}).catch(function (reason) {
					$scope.loading = false;
				});
			}
		};

		$scope.resize = function (isCurrent) {
			if (isCurrent && $scope.chart) {
				$timeout(function () {
					$scope.chart.resize($uibPosition.position($scope.chart.getDom()));
				}, 100);
			}
		};

		$scope.$watch(function () {
			return $scope.cpId === $scope.processId;
		}, $scope.resize);
	}

	function StatDetailCtrl($scope, $log, StatService) {
		$scope.loading = false;
		var titles = {
			due: '超时定单分类',
			alert: '预警定单分类',
			processing: '进行中定单分类',
			done: '已完成定单分类',
		};
		var processes = {
			basic: '专利基础标引',
			deep: '专利深度标引',
			value: '专利价值标引',
			price: '专利价格标引',
		};
		$scope.title = titles[$scope.statType || 'due'];
		$scope.options = {
			tooltip: {
				trigger: 'item',
				formatter: "{b} : {c} ({d}%)"
			},
			series: [
				{
					name: $scope.title,
					type: 'pie',
					radius: '55%',
					center: ['50%', '60%'],
					itemStyle: {
						emphasis: {
							shadowBlur: 10,
							shadowOffsetX: 0,
							shadowColor: 'rgba(0, 0, 0, 0.5)'
						}
					},
					label: {
						position: 'outside'
					},
					labelLine: {
						show: true
					}
				}
			]
		};
		$scope.loading = true;
		$scope.onChartAware = function (chart) {
			$scope.chart = chart;
			if ($scope.orderType == 'process') {
				StatService.countDetailForProcessOrders($scope.statType).then(function (resp) {
					$scope.loading = false;
					if (resp && resp.code === 0) {
						$scope.options.series[0].data = ['basic', 'deep', 'value', 'price'].map(function (key) {
							return {
								value: resp.result[key],
								name: processes[key],
							}
						});
						$scope.chart.clear();
						$scope.chart.setOption($scope.options, true);
						$scope.chart.resize();
					}
				}).catch(function (reason) {
					$scope.loading = false;
				});
			}
		};
	}

	function StatProcessCtrl($scope, $log, StatService) {
		$scope.loading = false;

		$scope.processOrderState = {}

		// 专利定单数量统计
		$scope.countProcessOrders = function () {
			$scope.loading = true;
			StatService.countProcessOrders($scope.processId).then(function (resp) {
				$scope.loading = false;
				if (resp && resp.code === 0) {
					angular.merge($scope.processOrderState, resp.result);
				}
			}).catch(function (reason) {
				$scope.loading = false;
			});
		};

		$scope.countProcessOrders();
	}

	// 根据高校专利量进行柱状图排名
	function CollegePatentSortCtrl($scope, $log, $uibPosition, $timeout, ChartService, $state) {

		$scope.loading = false;
		$scope.type = "";
		$scope.legState = "";
		$scope.options = {
		};
		$scope.numTop = '10';
		$scope.onChartAware = function (chart) {
			$scope.chart = chart;
			ChartService.getCollegePatentSort($scope.numTop, $scope.type, $scope.legState).then(function (resp) {
				$scope.loading = false;
				if (resp && resp.code === 0) {
					$scope.chart.clear();
					var scaleData = resp.result;
					resp.result.forEach(function (element) {
						element.name = element._id.collegeName;
						element.number = element.count;
					});
					$scope.options = {
						color: ['#3398DB'],
						tooltip : {
							trigger: 'axis',
							axisPointer : {            // 坐标轴指示器，坐标轴触发有效
								type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
							}
						},
						grid: {
							left: '3%',
							right: '4%',
							bottom: '3%',
							containLabel: true
						},
						xAxis : [
							{
								type : 'category',
								data : resp.result.map(function(e) {
									return e.name;
								}),
								axisLabel:{
									// rotate:30,
									color:'#666666',
									fontSize:'10',
									textStyle:{
										fontSize:20
									}
								},
								axisTick: {
									alignWithLabel: true
								},
							}
						],
						yAxis : [
							{
								type : 'value',
								name:'专利数量(件)'
							}
						],
						series : [
							{
								type:'bar',
								barWidth: '40%',
								data: resp.result.map(function(e) {
									return [e.name, e.number, e._id.province];
								}),
								itemStyle: {
									normal: {
										//好，这里就是重头戏了，定义一个list，然后根据所以取得不同的值，这样就实现了，
										color: new echarts.graphic.LinearGradient(
												0, 0, 0, 1,
												[
													{offset: 0, color: '#83bff6'},
													{offset: 0.5, color: '#188df0'},
													{offset: 1, color: '#188df0'}
												]
										),
										//以下为是否显示，显示位置和显示格式的设置了
										label: {
											show: true,
											position: 'top',
											formatter: '{c}'

										}
									}
								},
								label: {
									normal: {
										show: false
									},
								}
							}
						]
					};

                    // 编辑excel名称 TODO:需要修改
                    var title = "根据高校专利量排名.xls";
                    if(scaleData && scaleData.length > 0) {
                        // 创建table
                        var table = document.createElement('table');
                        var innerHtml = "<tr>";
                        // 编辑抬头 TODO:需要修改
                        var nameData = new Array('高校省份','高校名称', '专利数量');
                        nameData.forEach(function (data) {
                            innerHtml += "<th>" + data + "</th>"
                        });
                        innerHtml += "</tr>";
                        // 填充内容 TODO:需要修改
                        scaleData.forEach(function(data) {
                            innerHtml +=
                                "<tr>" +
                                "<td>" + data._id.province + "</td>" +
                                "<td>" + data.name + "</td>" +
                                "<td>" + data.number + "</td>" +
                                "</tr>";
                        });
                        table.innerHTML = innerHtml;
                        fixExport($scope.options, table, title);
                    }

					if($scope.numTop > 10) {
						$scope.options.xAxis[0].axisLabel.rotate = 30;
					}
					$scope.chart.setOption($scope.options, true);
					$scope.chart.resize();
					$scope.chart.on('click', function(params) {
						$state.go("main.console.statistics.college", {provinceName: params.data[2], collegeName: params.data[0]});
					});
				}
			}).catch(function (reason) {
				$scope.loading = false;
			});
		};

	}

	function BarGraphCtrl($scope, $log, $uibPosition, ModalService, ToastService, $timeout) {
		$scope.options = {
			title: {
				text: $scope.eTitle,
				left: 'center'
			},
			tooltip: {
				trigger: 'axis',
				axisPointer: {            // 坐标轴指示器，坐标轴触发有效
					type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
				}
			},
			xAxis: {
				data: [],
				axisTick: {show: false},
				axisLabel: {rotate: 30}
			},
			yAxis: {
				splitLine: {show: false},
				name: '',
			},
			animationDurationUpdate: 1200,
			series: [{
				type: 'bar',
				itemStyle: {
					normal: {
						color: '#277EAB'
					}
				},
				silent: true,
				barWidth: 40,
				barGap: '-100%', // Make series be overlap
				data: []
			}]
		};

		$scope.onChartAware = function (chart) {
			$scope.chart = chart;
			var max = 0;
			$scope.options.series[0].data = $scope.numList;
			for(var i=0; i<$scope.numList.length; i++) {
				max = Math.max(max, $scope.numList[i]);
			}
			$scope.options.xAxis.data = $scope.xs;
			$scope.options.yAxis.max = max;
			$scope.options.yAxis.name = $scope.yName;
			$scope.options.title.text = $scope.eTitle;
			$scope.chart.clear();
			$scope.chart.setOption($scope.options, true);
			$scope.chart.resize();
		};

		$scope.resize = function (isCurrent) {
			if (isCurrent && $scope.chart) {
				$timeout(function () {
					$scope.onChartAware($scope.chart);
					$scope.chart.resize($uibPosition.position($scope.chart.getDom()));
				}, 100);
			}
		};

		$scope.$watch(function () {
			return $scope.searchTime;
		}, $scope.resize);

	}

	function MapGraphCtrl($scope, $log, $uibPosition, ModalService, ToastService, $timeout) {
		$scope.ti = '全国地图大数据';

		$scope.options = {
			backgroundColor: '#FFFFFF',
			title: {
				text: '全国地图大数据',
				subtext: '',
				x:'center',
				textStyle: {
					fontWeight: 400,
					fontSize : 28
				},
				show: false,
			},
			tooltip : {
				trigger: 'item',
				formatter: function(params) {
					return params.data._id + '<br />高校数量:' + params.data.collegeCount + '<br />专利数量:' + params.data.patentCount;
				}
			},

			//左侧小导航图标
			visualMap: {
				show : true,
				x: 'left',
				y: 'center',
				splitList: [
					{start: 500, end:600},{start: 400, end: 500},
					{start: 300, end: 400},{start: 200, end: 300},
					{start: 100, end: 200},{start: 0, end: 100},
				],
				color: ['#5475f5', '#9feaa5', '#85daef','#74e2ca', '#e6ac53', '#9fb5ea']
			},

			//配置属性
			series: [{
				type: 'map',
				mapType: 'china',
				roam: true,
				label: {
					normal: {
						show: true,
						formatter: function(params) {
							return params.name;
						}
					},
				},
			}]
		};

		$scope.onChartAware = function (chart) {
			$scope.chart = chart;
			if($scope.visualMap) {
				$scope.options.visualMap = $scope.visualMap;
			}
			$scope.options.title.text = $scope.mapName || "全国地图";
			$scope.options.series[0].data = $scope.data;
			$scope.options.toolbox = {
				right: 70,
				top: 0,
				feature: {
					saveAsImage: {
						name: $scope.ti,
					},
				}
			};
			// 编辑excel名称 TODO:需要修改
			var title = "全国地图大数据.xls";
			if($scope.data && $scope.data.length > 0) {
				// 创建table
				var table = document.createElement('table');
				var innerHtml = "<tr>";
				// 编辑抬头 TODO:需要修改
				var nameData = new Array('省份', '专利数量', '高校数量');
				nameData.forEach(function (data) {
					innerHtml += "<th>" + data + "</th>"
				});
				innerHtml += "</tr>";
				// 填充内容 TODO:需要修改
				$scope.data.forEach(function(data) {
					innerHtml +=
							"<tr>" +
							"<td>" + data._id + "</td>" +
							"<td>" + data.patentCount + "</td>" +
							"<td>" + data.collegeCount + "</td>" +
							"</tr>";
				});
				table.innerHTML = innerHtml;
				fixExport($scope.options, table, title);
			}
			if($scope.searchTime>0) {
				$scope.chart.setOption($scope.options, true);
			}
			$scope.chart.on('click', function (params) {
				if($scope.onClickMap) {
					$scope.onClickMap(params);
				}
			});
		};

		$scope.resize = function (isCurrent) {
			if (isCurrent && $scope.chart) {
				$timeout(function () {
					$scope.onChartAware($scope.chart);
					$scope.chart.resize($uibPosition.position($scope.chart.getDom()));
				}, 100);
			}
		};

		$scope.$watch(function () {
			return $scope.searchTime;
		}, $scope.resize);

	}

	// 4.1专利类型饼图
	function CollegeTypeStatCtrl($scope, $log, $uibPosition, $timeout, ChartService, PATENT_TYPE) {

		$scope.loading = false;
		$scope.options = {
			tooltip: {
				trigger: 'item',
				formatter: "{a} <br/>{b}: {c} ({d}%)"
			},
			// color:['#56adf0','#ff7878','#d29cff','#ff8e47','#ff87b9','#60e6df','ffeb3f','9bf692','8997fc'],
			color:[ '#0bb7a5', '#6243c2', '#ff7300',
				'#9ad6d6','#4266bc', '#67c2c3', '#99eb99',
				'#c69edd','#ce0275', '#e266ac', '#ab6bcc',
				'#ffd403', '#ff3234', '#ff7300', '#ffc69a',
			],
			legend: {
				top: 40,
				orient: 'vertical',
				x: 'right',
				data:['发明专利','实用新型','外观专利',
					'有效期届满发明专利','有效发明专利','在审发明专利','无效发明专利',
					'有效期届满实用新型','有效实用新型','在审实用新型','无效实用新型',
					'有效期届满外观专利','有效外观专利','在审外观专利','无效外观专利'],
			},
			series: [
				{
					name:'专利类型',
					type:'pie',
					selectedMode: 'single',
					radius: [0, '30%'],

					label: {
						normal: {
							position: 'inner'
						}
					},
					labelLine: {
						normal: {
							show: false
						}
					},
				},
				{
					name:'法律状态',
					type:'pie',
					radius: ['40%', '55%'],
					label: {
						normal: {
							show:false,
							// formatter: '{a|{a}}{abg|}\n{hr|}\n  {b|{b}：}{c}  {per|{d}%}  ',
							formatter: function (params) {
								if(params.percent == 0) {
									return '';
								}
								return '{b|'+ params.name +'：} ' + params.data.value + '  {per|'+ params.percent +'%}  ';
							},
							backgroundColor: '#eee',
							borderColor: '#aaa',
							borderWidth: 1,
							borderRadius: 4,
							rich: {
								a: {
									color: '#999',
									lineHeight: 22,
									align: 'center'
								},
								hr: {
									borderColor: '#aaa',
									width: '100%',
									borderWidth: 0.5,
									height: 0
								},
								b: {
									fontSize: 16,
									lineHeight: 33
								},
								per: {
									color: '#eee',
									backgroundColor: '#334455',
									padding: [2, 4],
									borderRadius: 2
								}
							}
						}
					},
				}
			]
		};
		$scope.onChartAware = function (chart) {
			$scope.chart = chart;
			if(!$scope.collegeName) {
				return;
			}
			self.loading = true;
			ChartService.getCollegeTypeStat($scope.collegeName).then(function (resp) {
				$scope.loading = false;
				if (resp && resp.code === 0) {
					var innerData = [];
					var outerData = [];
					resp.result.forEach(function(e) {
						var name = PATENT_TYPE[e._id.patType];
						innerData.push({name : name, value: e.count});
						e.legStat.forEach(function(el) {
							outerData.push({name : el.lastLegalStatus + name , value: el.count});
							if(el.lastLegalStatus === '有效期届满') {
								e.yxqjm = true;
							}
							if(el.lastLegalStatus === '在审') {
								e.zs = true;
							}
							if(el.lastLegalStatus === '有效') {
								e.yx = true;
							}
							if(el.lastLegalStatus === '无效') {
								e.wx = true;
							}
						})
						if(!e.yxqjm) {
							outerData.push({name : "有效期届满" + name , value: 0});
						}
						if(!e.zs) {
							outerData.push({name : "在审" + name , value: 0});
						}
						if(!e.yx) {
							outerData.push({name : "有效" + name , value: 0});
						}
						if(!e.wx) {
							outerData.push({name : "无效" + name , value: 0});
						}
					});

                    $scope.options.toolbox = {
                        right: 4,
                        top: 0,
                        feature: {
                            saveAsImage: {
                                name: $scope.collegeName + $scope.ti,
                            },
                        }
                    };
                    // 编辑excel名称 TODO:需要修改
                    var title = "高校专利类型统计.xls";
                    if(resp.result && resp.result.length > 0) {
                        // 创建table
                        var table = document.createElement('table');
                        var innerHtml = "<tr>";
                        // 编辑抬头 TODO:需要修改
                        var nameData = new Array('省份', '高校名称', '专利类型','有效专利数','无效专利数','有效期届满专利数','在审专利数');
                        nameData.forEach(function (data) {
                            innerHtml += "<th>" + data + "</th>"
                        });
                        innerHtml += "</tr>";
                        // 填充内容 TODO:需要修改
                        resp.result.forEach(function(data) {
                        	var yxcount=0;
                        	var wxcount=0;
                        	var zscount=0;
                        	var yxqjmcount=0;
                            data.legStat.forEach(function (item) {
								if(item.lastLegalStatus=="有效"){
									yxcount=item.count;
									data.yxcount=yxcount;
                                    data.wxcount=wxcount;
                                    data.zscount=zscount;
                                    data.yxqjmcount=yxqjmcount;
								}else if(item.lastLegalStatus=="无效"){
                                    wxcount=item.count;
                                    data.yxcount=yxcount;
                                    data.wxcount=wxcount;
                                    data.zscount=zscount;
                                    data.yxqjmcount=yxqjmcount;
								}else if(item.lastLegalStatus=="有效期届满"){
                                    yxqjmcount=item.count;
                                    data.yxcount=yxcount;
                                    data.wxcount=wxcount;
                                    data.zscount=zscount;
                                    data.yxqjmcount=yxqjmcount;
								}else {
                                    zscount=item.count;
                                    data.yxcount=yxcount;
                                    data.wxcount=wxcount;
                                    data.zscount=zscount;
                                    data.yxqjmcount=yxqjmcount;
								}
                            })
                            innerHtml +=
                                "<tr>" +
                                "<td>" + data._id.province + "</td>" +
                                "<td>" + data._id.collegeName + "</td>" +
                                "<td>" + data._id.patType + "</td>" +
                                "<td>" + data.yxcount + "</td>" +
                                "<td>" + data.wxcount + "</td>" +
                                "<td>" + data.yxqjmcount + "</td>" +
                                "<td>" + data.zscount + "</td>" +
                                "</tr>";
                        });
                        table.innerHTML = innerHtml;
                        fixExport($scope.options, table, title);
                    }
					$scope.options.series[0].data = innerData;
					$scope.options.series[1].data = outerData;
					$scope.chart.clear();
					$scope.chart.setOption($scope.options, true);
					$scope.chart.resize();
				}
			}).catch(function (reason) {
				$scope.loading = false;
			});
		};

		$scope.resize = function (isCurrent) {
			if (isCurrent && $scope.chart) {
				$timeout(function () {
					$scope.onChartAware($scope.chart);
					$scope.chart.resize($uibPosition.position($scope.chart.getDom()));
				}, 100);
			}
		};

		$scope.$watch(function() {
			return $scope.collegeName;
		}, $scope.resize)

	}

	// 4.2法律状态饼图
	function CollegeLegStatCtrl($scope, $log, $uibPosition, $timeout, ChartService, PATENT_TYPE) {

		$scope.loading = false;

		$scope.options = {
			tooltip: {
				trigger: 'item',
				formatter: "{a} <br/>{b}: {c} ({d}%)"
			},
			// color:['#56adf0','#ff7878','#d29cff','#ff8e47','#ff87b9','#60e6df','ffeb3f','9bf692','8997fc'],
			color:['#1da0af', '#f9253e', '#ffb43d', '#2ea2f9',
				'#c69edd', '#ff7300', '#9ad6d6',
				'#ab6bcc', '#ffc69a', '#99eb99',
				'#e266ac', '#ff9034', '#67c2c3',
				'#ce0275', '#ff3234', '#4266bc',
			],
			legend: {
				top: 40,
				orient: 'vertical',
				x: 'right',
				data:['有效专利','有效期届满专利','在审专利', '无效专利',
					'有效发明专利','有效实用新型','有效外观专利',
					'有效期届满发明专利','有效期届满实用新型','有效期届满外观专利',
					'在审发明专利','在审实用新型','在审外观专利',
					'无效发明专利','无效实用新型', '无效外观专利'
				],
			},
			series: [
				{
					name:'法律状态',
					type:'pie',
					selectedMode: 'single',
					radius: [0, '30%'],

					label: {
						normal: {
							position: 'inner'
						}
					},
					labelLine: {
						normal: {
							show: false
						}
					},
				},
				{
					name:'专利类型',
					type:'pie',
					radius: ['40%', '55%'],
					label: {
						normal: {
							show: false,
							// formatter: '{a|{a}}{abg|}\n{hr|}\n  {b|{b}：}{c}  {per|{d}%}  ',
							formatter: function (params) {
								if(params.percent == 0) {
									return '';
								}
								return '{a|'+ params.seriesName +'}{abg|}\n{hr|}\n  {b|'+ params.name +'：} ' + params.data.value + '  {per|'+ params.percent +'%}  ';
							},
							backgroundColor: '#eee',
							borderColor: '#aaa',
							borderWidth: 1,
							borderRadius: 4,
							rich: {
								a: {
									color: '#999',
									lineHeight: 22,
									align: 'center'
								},
								hr: {
									borderColor: '#aaa',
									width: '100%',
									borderWidth: 0.5,
									height: 0
								},
								b: {
									fontSize: 16,
									lineHeight: 33
								},
								per: {
									color: '#eee',
									backgroundColor: '#334455',
									padding: [2, 4],
									borderRadius: 2
								}
							}
						}
					},
				}
			]
		};
		$scope.onChartAware = function (chart) {
			$scope.chart = chart;
			if(!$scope.collegeName) {
				return;
			}
			self.loading = true;
			ChartService.getCollegeLegStat($scope.collegeName).then(function (resp) {
				$scope.loading = false;
				if (resp && resp.code === 0) {
					var innerData = [];
					var outerData = [];
					resp.result.forEach(function(e) {
						var lastLegalStatus = e._id.lastLegalStatus;
						innerData.push({name : lastLegalStatus + '专利', value: e.count});
						e.patType.forEach(function(el) {
							outerData.push({name : lastLegalStatus + PATENT_TYPE[el.patType] , value: el.count});
							if(el.patType === 'FMZL') {
								e.fmzl = true;
							}
							if(el.patType === 'WGZL') {
								e.wgzl = true;
							}
							if(el.lastLegalStatus === 'SYXX') {
								e.syxx = true;
							}
						})
						if(!e.fmzl) {
							outerData.push({name : lastLegalStatus + "发明专利" , value: 0});
						}
						if(!e.wgzl) {
							outerData.push({name : lastLegalStatus + "外观专利" , value: 0});
						}
						if(!e.syxx) {
							outerData.push({name : lastLegalStatus + "实用新型" , value: 0});
						}
					});
					$scope.options.series[0].data = innerData;
					$scope.options.series[1].data = outerData;
					$scope.options.toolbox = {
						right: 4,
						top: 0,
						feature: {
							saveAsImage: {
								name: $scope.collegeName + $scope.ti,
							},
						}
					};


                    // 编辑excel名称 TODO:需要修改
                    var title = "高校专利法律状态统计.xls";
                    if(resp.result && resp.result.length > 0) {
                        // 创建table
                        var table = document.createElement('table');
                        var innerHtml = "<tr>";
                        resp.result.forEach(function(data){
                            var fmzl=0;
                            var syzl=0;
                            var wgzl=0;
                            data.patType.forEach(function (item) {
                                if(item.patType === 'FMZL') {
                                    fmzl=item.count;
                                    data.fmzlcount=fmzl;
                                    data.syzlcount=syzl;
                                    data.wgzlcount=wgzl;
                                }
                                if(item.patType === 'WGZL') {
                                    wgzl=item.count;
                                    data.fmzlcount=fmzl;
                                    data.syzlcount=syzl;
                                    data.wgzlcount=wgzl;
                                }
                                if(item.patType === 'SYXX') {
                                    syzl=item.count;
                                    data.fmzlcount=fmzl;
                                    data.syzlcount=syzl;
                                    data.wgzlcount=wgzl;
                                }
                            })
                        })

                        // 编辑抬头 TODO:需要修改
                        var nameData = new Array('省份', '高校名称', '法律状态','专利总数','发明专利数','实用新型专利数','外观设计专利数');
                        nameData.forEach(function (data) {
                            innerHtml += "<th>" + data + "</th>"
                        });
                        innerHtml += "</tr>";
                        // 填充内容 TODO:需要修改
                        resp.result.forEach(function(data) {


                            innerHtml +=
                                "<tr>" +
                                "<td>" + data._id.province + "</td>" +
                                "<td>" + data._id.collegeName + "</td>" +
                                "<td>" + data._id.lastLegalStatus + "</td>" +
                                "<td>" + data.count + "</td>" +
                                "<td>" + data.fmzlcount + "</td>" +
                                "<td>" + data.wgzlcount + "</td>" +
                                "<td>" + data.syzlcount + "</td>" +
                                "</tr>";
                        });
                        table.innerHTML = innerHtml;
                        fixExport($scope.options, table, title);
                    }

					$scope.chart.clear();
					$scope.chart.setOption($scope.options, true);
					$scope.chart.resize();
				}
			}).catch(function (reason) {
				$scope.loading = false;
			});
		};

		$scope.resize = function (isCurrent) {
			if (isCurrent && $scope.chart) {
				$timeout(function () {
					$scope.onChartAware($scope.chart);
					$scope.chart.resize($uibPosition.position($scope.chart.getDom()));
				}, 100);
			}
		};

		$scope.$watch(function() {
			return $scope.collegeName;
		}, $scope.resize)

	}

	// 5高校专利申请趋势
	function CollegeStatLastTenCtrl($scope, $log, $uibPosition, $timeout, ChartService, PATENT_TYPE) {

		$scope.loading = false;
		$scope.options = {
			tooltip : {
				trigger: 'axis',
				axisPointer : {            // 坐标轴指示器，坐标轴触发有效
					type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
				}
			},
			color: ['#99c868', '#01b3f9', '#ffc21f'],
			legend: {
				orient: 'vertical',
				right: 40,
				y: 'center',
				data: ['发明专利', '实用新型','外观专利']
			},
			// grid: {
			// 	left: '3%',
			// 	right: '4%',
			// 	bottom: '3%',
			// 	containLabel: true
			// },
			xAxis:  {
				type: 'category',
			},
			yAxis: {
				type: 'value',
			},
			series: [
				{
					name: '发明专利',
					barWidth: '40%',
					type: 'bar',
					stack: '总量',
					label: {
						normal: {
							show: true,
							position: 'insideRight'
						}
					},
				},
				{
					name: '实用新型',
					barWidth: '40%',
					type: 'bar',
					stack: '总量',
					label: {
						normal: {
							show: true,
							position: 'insideRight'
						}
					},
				},
				{
					name: '外观专利',
					barWidth: '40%',
					type: 'bar',
					stack: '总量',
					label: {
						normal: {
							show: true,
							position: 'insideRight'
						}
					},
				},
			]
		};
		$scope.onChartAware = function (chart) {
			$scope.chart = chart;
			if(!$scope.collegeName) {
				return;
			}
			self.loading = true;
			ChartService.getCollegeStatLastTen($scope.collegeName).then(function (resp) {
				$scope.loading = false;
				if (resp && resp.code === 0) {
					var years = [];
					var fmzls = [];
					var syxxs = [];
					var wgzls = [];
					resp.result.forEach(function (e) {
						years.push(e.value.year);
						fmzls.push(e.value.fmzlCount);
						syxxs.push(e.value.syxxCount);
						wgzls.push(e.value.wgsjCount);
					});
					$scope.options.xAxis.data = years;
					$scope.options.series[0].data = fmzls;
					$scope.options.series[1].data = syxxs;
					$scope.options.series[2].data = wgzls;

					$scope.options.toolbox = {
						right: 70,
						top: 0,
						feature: {
							saveAsImage: {
								name: $scope.collegeName + $scope.ti,
							},
						}
					};

                    // 编辑excel名称 TODO:需要修改
                    var title = "高校专利申请趋势.xls";
                    if(resp.result && resp.result.length > 0) {
                        // 创建table
                        var table = document.createElement('table');
                        var innerHtml = "<tr>";
                        // 编辑抬头 TODO:需要修改
                        var nameData = new Array('省份', '高校名称', '年份','专利总申请数','发明专利申请数','实用新型申请数','外观专利申请数');
                        nameData.forEach(function (data) {
                            innerHtml += "<th>" + data + "</th>"
                        });
                        innerHtml += "</tr>";
                        // 填充内容 TODO:需要修改
                        resp.result.forEach(function(data) {
                            innerHtml +=
                                "<tr>" +
                                "<td>" + data.value.province + "</td>" +
                                "<td>" + data.value.collegeName + "</td>" +
                                "<td>" + data.value.year + "</td>" +
                                "<td>" + data.value.count + "</td>" +
                                "<td>" + data.value.fmzlCount + "</td>" +
                                "<td>" + data.value.syxxCount + "</td>" +
                                "<td>" + data.value.wgsjCount + "</td>" +
                                "</tr>";
                        });
                        table.innerHTML = innerHtml;
                        fixExport($scope.options, table, title);
                    }

					$scope.chart.clear();
					$scope.chart.setOption($scope.options, true);
					$scope.chart.resize();
				}
			}).catch(function (reason) {
				$scope.loading = false;
			});
		};

		$scope.resize = function (isCurrent) {
			if (isCurrent && $scope.chart) {
				$timeout(function () {
					$scope.onChartAware($scope.chart);
					$scope.chart.resize($uibPosition.position($scope.chart.getDom()));
				}, 100);
			}
		};

		$scope.$watch(function() {
			return $scope.collegeName;
		}, $scope.resize)

	}

	// 6高校技术发展趋势分析
	function CollegeIpcTrendCtrl($scope, $log, $uibPosition, $timeout, ChartService, PATENT_TYPE) {

		$scope.loading = false;
		$scope.options = {
			color: ['#99c868', '#01b3f9', '#00d1ef'],
			tooltip: {
				trigger: 'item',
			},
			title: [],
			singleAxis: [],
			series: []
		};
		$scope.onChartAware = function (chart) {
			$scope.chart = chart;
			if(!$scope.collegeName) {
				return;
			}
			self.loading = true;
			ChartService.getCollegeIpcTrend($scope.collegeName).then(function (resp) {
				$scope.loading = false;
				if (resp && resp.code === 0) {
					var years = new Set();
					var nameThrees = new Set();
					var data = [];
					// 遍历,拿到所有的年份和ipc,同时把所有点变为3维点
					resp.result.forEach(function(e) {
						years.add(e._id.year);
						nameThrees.add(e._id.nameThree);
						data.push([e._id.year, e._id.nameThree, e.count]);
					});
					years = Array.from(years);
					nameThrees = Array.from(nameThrees);

					$scope.options = {
						color: ['#99c868', '#01b3f9', '#00d1ef'],
						title: [],
						tooltip: {
							trigger: 'item',
						},
						singleAxis: [],
						series: []
					};
					nameThrees.forEach(function (nameThree, i) {
						$scope.options.title.push({
							textBaseline: 'middle',
							top: (i + 0.7) * 10 + '%',
							text: $scope.titleLineFeed(nameThree,8),
							textStyle : {
								fontSize: 14,
							},
						});
						$scope.options.singleAxis.push({
							left: 150,
							type: 'category',
							boundaryGap: false,
							data: years,
							top: (i * 10 + 3) + '%',
							height:  '5%',
						});
						$scope.options.series.push({
							singleAxisIndex: i,
							coordinateSystem: 'singleAxis',
							type: 'scatter',
							data: [],
							tooltip: {
								position: 'top',
								formatter: function(params) {
									return params.data[0] + "年 " + params.data[1] + "件";
								}
							},
							symbolSize: function (dataItem) {
								if(dataItem[1] < 8) {
									return 4;
								}
								return dataItem[1]/2;
							},
						});
					});
					data.forEach(function (dataItem) {
						var index = nameThrees.indexOf(dataItem[1]);
						$scope.options.series[index].data.push([dataItem[0], dataItem[2]]);
					});
					$scope.options.color = ['#ff4200', '#b721f6', '#ff8400', '#1c81e9', '#7249f6', '#ce0439', '#1896bc', '#21cb19', '#ffb10a', '#4e9108'];

					$scope.options.toolbox = {
						right: 70,
						top: 0,
						feature: {
							saveAsImage: {
								name: $scope.collegeName + $scope.ti,
							},
						}
					};
                    // 编辑excel名称 TODO:需要修改
                    var title = "高校技术发展趋势分析.xls";
                    if(resp.result && resp.result.length > 0) {
                        // 创建table
                        var table = document.createElement('table');
                        var innerHtml = "<tr>";
                        // 编辑抬头 TODO:需要修改
                        var nameData = new Array('省份', '高校名称', '年份','IPC',"IPC名称","专利总数");
                        nameData.forEach(function (data) {
                            innerHtml += "<th>" + data + "</th>"
                        });
                        innerHtml += "</tr>";
                        // 填充内容 TODO:需要修改
                        resp.result.forEach(function(data) {
                            innerHtml +=
                                "<tr>" +
                                "<td>" + data._id.province + "</td>" +
                                "<td>" + data._id.collegeName + "</td>" +
                                "<td>" + data._id.year + "</td>" +
                                "<td>" + data._id.codeThree + "</td>" +
                                "<td>" + data._id.nameThree + "</td>" +
                                "<td>" + data.count + "</td>" +
                                "</tr>";
                        });
                        table.innerHTML = innerHtml;
                        fixExport($scope.options, table, title);
                    }

					$scope.chart.clear();
					$scope.chart.setOption($scope.options, true);
					$scope.chart.resize();
				}
			}).catch(function (reason) {
				$scope.loading = false;
			});
		};

		$scope.resize = function (isCurrent) {
			if (isCurrent && $scope.chart) {
				$timeout(function () {
					$scope.onChartAware($scope.chart);
					$scope.chart.resize($uibPosition.position($scope.chart.getDom()));
				}, 100);
			}
		};

		// 标题换行
		$scope.titleLineFeed = function(title, num) {
			var time = Math.ceil(title.length/num);
			var result = "";
			for(var i=0; i< time ; i++) {
				if(i == time-1) {
					result += title.substr(i*num) + "\n";
					break;
				}
				result += title.substr(i*num, num) + "\n";
			}
			return result;
		};

		$scope.$watch(function() {
			return $scope.collegeName;
		}, $scope.resize)

	}

	// 7高校技术热点分布
	function CollegeIpcHotCtrl($scope, $log, $uibPosition, $timeout, ChartService, PATENT_TYPE) {

		$scope.loading = false;
		$scope.collection = "five";
		$scope.options = {
		};
		$scope.onChartAware = function (chart) {
			$scope.chart = chart;
			if(!$scope.collegeName) {
				return;
			}
			self.loading = true;
			ChartService.getCollegeIpcHot($scope.collegeName, $scope.collection).then(function (resp) {
				$scope.loading = false;
				if (resp && resp.code === 0) {
					var data = [];
					var total = 0;
					resp.result.forEach(function(e) {
						data.push({name: e._id.nameThree, ipc: e._id.codeThree, value: e.count});
						total += e.count;
					});
					$scope.options = {
						tooltip: {
							formatter: function(params) {
								return params.data.name + "<br/>" + params.value + "(" + Math.round(params.value / total * 100) + ")%";
							}
						},
						color: ['#34a37c', '#16ba80', '#16c285', '#4a8e7', '#7157f4', '#8b57f4', '#98c161', '#bbe062', '#b9e856', '#ff9243',
							'#fdaf62', '#fec070', '#5fc8dc', '#65d9ff', '#8ce0fb', '#fcc744', '#fed376', '#ffe50e', '#fd63a1', '#f95e56'],
						series: [{
							type: 'treemap',
							data: data,
							roam:false,
							nodeClick : false,
							label: {
								position: [0, '10%'],
								fontSize: 14,
								formatter: function(params) {
									return params.data.name + "\n" + params.value + "(" + Math.round(params.value / total * 100) + ")%";
								},
							},
							breadcrumb: {
								show: false
							},
						}]
					};
					$scope.options.toolbox = {
						right: 70,
						top: 0,
						feature: {
							saveAsImage: {
								name: $scope.collegeName + $scope.ti,
							},
						}
					};

                    // 编辑excel名称 TODO:需要修改
                    var title = "高校技术热点分布.xls";
                    if(resp.result && resp.result.length > 0) {
                        // 创建table
                        var table = document.createElement('table');
                        var innerHtml = "<tr>";
                        // 编辑抬头 TODO:需要修改
                        var nameData = new Array('省份', '高校名称', 'ipc','ipc名称','专利数');
                        nameData.forEach(function (data) {
                            innerHtml += "<th>" + data + "</th>"
                        });
                        innerHtml += "</tr>";
                        // 填充内容 TODO:需要修改
                        resp.result.forEach(function(data) {
                            innerHtml +=
                                "<tr>" +
                                "<td>" + data._id.province + "</td>" +
                                "<td>" + data._id.collegeName + "</td>" +
                                "<td>" + data._id.codeThree + "</td>" +
                                "<td>" + data._id.nameThree + "</td>" +
                                "<td>" + data.count + "</td>" +
                                "</tr>";
                        });
                        table.innerHTML = innerHtml;
                        fixExport($scope.options, table, title);
                    }

					$scope.chart.clear();
					$scope.chart.setOption($scope.options, true);
					$scope.chart.resize();
				}
			}).catch(function (reason) {
				$scope.loading = false;
			});
		};

		$scope.resize = function (isCurrent) {
			if (isCurrent && $scope.chart) {
				$timeout(function () {
					$scope.onChartAware($scope.chart);
					$scope.chart.resize($uibPosition.position($scope.chart.getDom()));
				}, 100);
			}
		};

		// 标题换行
		$scope.titleLineFeed = function(title, num) {
			var time = Math.ceil(title.length/num);
			var result = "";
			for(var i=0; i< time ; i++) {
				if(i == time-1) {
					result += title.substr(i*num) + "\n";
					break;
				}
				result += title.substr(i*num, num) + "\n";
			}
			return result;
		};

		$scope.$watch(function() {
			return $scope.collegeName;
		}, $scope.resize)

	}

	// 8高校热门领域的高价值专利分布
	function CollegeIpcValueCtrl($scope, $log, $uibPosition, $timeout, ChartService, PATENT_TYPE) {

		$scope.loading = false;
		$scope.options = {
			tooltip: {
				trigger: 'item',
				triggerOn: 'mousemove',
				formatter: function(params) {
					if(params.data.name == $scope.collegeName) {
						return params.data.name;
					}
					if(params.data.nameThree) {
						return $scope.collegeName + '<br/>'
								+ params.data.nameThree + '<br/>'
								+ params.name + '<br/>'
								+ '共 ' + params.data.value + '件'
					}
					return $scope.collegeName + '<br/>'
							+ params.name + '<br/>'
							+ params.data.an + '<br/>'
							+ '专利价值： ' + params.data.value + '分';
				}
			},
		};
		$scope.onChartAware = function (chart) {
			$scope.chart = chart;
			if(!$scope.collegeName) {
				return;
			}
			self.loading = true;
			ChartService.getCollegeIpcValue($scope.collegeName).then(function (resp) {
				$scope.loading = false;
				if (resp && resp.code === 0) {
					console.log(resp.result,"0000")
					var data = [];
					resp.result.forEach(function (e) {
						e.patent.forEach(function(p) {
							p.name = p.ti;
							p.value = (p.patentValue * 100).toFixed(2);
						});
						data.push({name: e._id.codeThree, nameThree: e._id.nameThree, code: e._id.codeThree, value: e.count, children: e.patent})
					});
					$scope.options.series= [{
							type: 'tree',
							data: [{name: $scope.collegeName, children: data}],
							top: '18%',
							bottom: '14%',
							layout: 'radial',
							symbol: 'emptyCircle',
							symbolSize: 7,
							initialTreeDepth: 3,
							animationDurationUpdate: 750,
					}];
					$scope.options.toolbox = {
						right: 70,
						top: 0,
						feature: {
							saveAsImage: {
								name: $scope.collegeName + $scope.ti,
							},
						}
					};

                    // 编辑excel名称 TODO:需要修改
                    var title = "高校热门领域的高价值专利分布.xls";
                    if(resp.result && resp.result.length > 0) {
                        // 创建table
                        var table = document.createElement('table');
                        var innerHtml = "<tr>";
                        // 编辑抬头 TODO:需要修改
                        var nameData = new Array('省份', '高校名称', 'ipc','ipc名称','专利数','子领域专利','子领域专利','子领域专利','子领域专利','子领域专利');
                        nameData.forEach(function (data) {
                            innerHtml += "<th>" + data + "</th>"
                        });
                        innerHtml += "</tr>";
                        // 填充内容 TODO:需要修改
                        resp.result.forEach(function(data) {
                            innerHtml +=
                                "<tr>" +
                                "<td>" + data._id.province + "</td>" +
                                "<td>" + data._id.collegeName + "</td>" +
                                "<td>" + data._id.codeThree + "</td>" +
                                "<td>" + data._id.nameThree + "</td>" +
                                "<td>" + data.count + "</td>" +
                                "<td>" + data.patent[0].ti+","+data.patent[0].an+",价值:"+data.patent[0].patentValue+"</td>" +
                                "<td>" + data.patent[1].ti+","+data.patent[1].an+",价值:"+data.patent[1].patentValue+"</td>" +
                                "<td>" + data.patent[2].ti+","+data.patent[2].an+",价值:"+data.patent[2].patentValue+ "</td>" +
                                "<td>" + data.patent[3].ti+","+data.patent[3].an+",价值:"+data.patent[3].patentValue+ "</td>" +
                                "<td>" + data.patent[4].ti+","+data.patent[4].an+",价值:"+data.patent[4].patentValue + "</td>" +
                                "</tr>";
                        });
                        table.innerHTML = innerHtml;
                        fixExport($scope.options, table, title);
                    }

					$scope.chart.clear();
					$scope.chart.setOption($scope.options, true);
					$scope.chart.resize();

					$scope.chart.on('click', function(params) {
						if(params.data.an) {
							copyToBoard(params.data.an);
						}
					});
				}
			}).catch(function (reason) {
				$scope.loading = false;
			});
		};

		$scope.resize = function (isCurrent) {
			if (isCurrent && $scope.chart) {
				$timeout(function () {
					$scope.onChartAware($scope.chart);
					$scope.chart.resize($uibPosition.position($scope.chart.getDom()));
				}, 100);
			}
		};

		// 标题换行
		$scope.titleLineFeed = function(title, num) {
			var time = Math.ceil(title.length/num);
			var result = "";
			for(var i=0; i< time ; i++) {
				if(i == time-1) {
					result += title.substr(i*num) + "\n";
					break;
				}
				result += title.substr(i*num, num) + "\n";
			}
			return result;
		};

		$scope.$watch(function() {
			return $scope.collegeName;
		}, $scope.resize)

	}

	// 9高校核心创新专利
	function CollegeByQuoteTotalCtrl($scope, $log, $uibPosition, $timeout, ChartService, PATENT_TYPE) {

		$scope.loading = false;
		$scope.options = {
		};
		$scope.limit = "10";
		$scope.onChartAware = function (chart) {
			$scope.chart = chart;
			if(!$scope.collegeName) {
				return;
			}
			self.loading = true;
			ChartService.getCollegeByQuoteTotal($scope.collegeName, $scope.limit).then(function (resp) {
				$scope.loading = false;
				if (resp && resp.code === 0) {
					$scope.options = {
						color: ['#99c868'],
						tooltip : {
							trigger: 'axis',
							axisPointer : {            // 坐标轴指示器，坐标轴触发有效
								type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
							},
							formatter: function(params) {
								return '专利名称: ' + params[0].data[2] + '<br/>'
										+  '申请号:   ' + params[0].data[1] + '<br/>'
										+  '被引用次数:' + params[0].data[0];
							}
						},
						xAxis : [
							{
								type : 'value',
								name:'被引用次数(件)',
								axisLabel:{
									// rotate:30,
									color:'#666666',
									fontSize:'10',
									textStyle:{
										fontSize:20
									}
								},
								axisTick: {
									alignWithLabel: true
								},
								position: 'top'
							}
						],
						yAxis : [
							{
								type : 'category',
								data : resp.result.map(function(e) {
									return e.an;
								}),
							}
						],
						series : [
							{
								type:'bar',
								barWidth: 22,
								data: resp.result.map(function(e, index) {
									// TODO: 这里被引用次数没有值,因此先设置假数据,之后需要改
									return [1000 * index, e.an, e.ti];
								}),
							}
						]
					};
					$scope.options.toolbox = {
						right: 70,
						top: 0,
						feature: {
							saveAsImage: {
								name: $scope.collegeName + $scope.ti,
							},
						}
					};

					$scope.chart.clear();
					$scope.chart.setOption($scope.options, true);
					$scope.chart.resize();
				}
			}).catch(function (reason) {
				$scope.loading = false;
			});
		};

		$scope.resize = function (isCurrent) {
			if (isCurrent && $scope.chart) {
				$timeout(function () {
					$scope.onChartAware($scope.chart);
					$scope.chart.resize($uibPosition.position($scope.chart.getDom()));
				}, 100);
			}
		};

		// 标题换行
		$scope.titleLineFeed = function(title, num) {
			var time = Math.ceil(title.length/num);
			var result = "";
			for(var i=0; i< time ; i++) {
				if(i == time-1) {
					result += title.substr(i*num) + "\n";
					break;
				}
				result += title.substr(i*num, num) + "\n";
			}
			return result;
		};

		$scope.$watch(function() {
			return $scope.collegeName;
		}, $scope.resize)

	}

	// 10高校联合创新主体分析
	function CollegePartnerCtrl($scope, $log, $uibPosition, $timeout, ChartService, PATENT_TYPE) {

		$scope.loading = false;
		$scope.options = {
		};
		$scope.limit = "10";
		$scope.onChartAware = function (chart) {
			$scope.chart = chart;
			if(!$scope.collegeName) {
				return;
			}
			self.loading = true;
			ChartService.getCollegePartner($scope.collegeName, $scope.limit).then(function (resp) {
				$scope.loading = false;
				if (resp && resp.code === 0) {
					var colors = ['#34a37c', '#f5893c', '#5fc8dc'];
					$scope.options = {
						tooltip: {                  // 提示框的配置
							formatter: function(param) {
								if (param.dataType === 'edge') {
									return param.data.category;
								}
								return param.data.name.replace(/\n/g, "") + (param.data.category? ("<br/>" + param.data.category): '');
							}
						},
						series: [{
							type: "graph",          // 系列类型:关系图
							top: '10%',             // 图表距离容器顶部的距离
							roam: false,             // 是否开启鼠标缩放和平移漫游。默认不开启。如果只想要开启缩放或者平移，可以设置成 'scale' 或者 'move'。设置成 true 为都开启
							focusNodeAdjacency: false,   // 是否在鼠标移到节点上的时候突出显示节点以及节点的边和邻接节点。[ default: false ]
							force: {                // 力引导布局相关的配置项，力引导布局是模拟弹簧电荷模型在每两个节点之间添加一个斥力，每条边的两个节点之间添加一个引力，每次迭代节点会在各个斥力和引力的作用下移动位置，多次迭代后节点会静止在一个受力平衡的位置，达到整个模型的能量最小化。	                                // 力引导布局的结果有良好的对称性和局部聚合性，也比较美观。
								repulsion: 1000,            // [ default: 50 ]节点之间的斥力因子(关系对象之间的距离)。支持设置成数组表达斥力的范围，此时不同大小的值会线性映射到不同的斥力。值越大则斥力越大
								// edgeLength: [150, 100]      // [ default: 30 ]边的两个节点之间的距离(关系对象连接线两端对象的距离,会根据关系对象值得大小来判断距离的大小)，
								// 这个距离也会受 repulsion。支持设置成数组表达边长的范围，此时不同大小的值会线性映射到不同的长度。值越小则长度越长。如下示例:
								// 值最大的边长度会趋向于 10，值最小的边长度会趋向于 50
								edgeLength: [200, 200]
							},
							layout: "force",            // 图的布局。[ default: 'none' ]
							// 'none' 不采用任何布局，使用节点中提供的 x， y 作为节点的位置。
							// 'circular' 采用环形布局;'force' 采用力引导布局.
							// 标记的图形
							// symbol: "path://M19.300,3.300 L253.300,3.300 C262.136,3.300 269.300,10.463 269.300,19.300 L269.300,21.300 C269.300,30.137 262.136,37.300 253.300,37.300 L19.300,37.300 C10.463,37.300 3.300,30.137 3.300,21.300 L3.300,19.300 C3.300,10.463 10.463,3.300 19.300,3.300 Z",
							symbol: 'circle',
							lineStyle: {            // 关系边的公用线条样式。其中 lineStyle.color 支持设置为'source'或者'target'特殊值，此时边会自动取源节点或目标节点的颜色作为自己的颜色。
								normal: {
									color: '#000',          // 线的颜色[ default: '#aaa' ]
									width: 1,               // 线宽[ default: 1 ]
									type: 'solid',          // 线的类型[ default: solid ]   'dashed'    'dotted'
									opacity: 0.5,           // 图形透明度。支持从 0 到 1 的数字，为 0 时不绘制该图形。[ default: 0.5 ]
									curveness: 0            // 边的曲度，支持从 0 到 1 的值，值越大曲度越大。[ default: 0 ]
								}
							},
							label: {                // 关系对象上的标签
								normal: {
									show: true,                 // 是否显示标签
									position: "inside",         // 标签位置:'top''left''right''bottom''inside''insideLeft''insideRight''insideTop''insideBottom''insideTopLeft''insideBottomLeft''insideTopRight''insideBottomRight'
									textStyle: {                // 文本样式
										fontSize: 14,
									}
								}
							},
							edgeLabel: {                // 连接两个关系对象的线上的标签
								normal: {
									show: true,
									textStyle: {
										fontSize: 14
									},
									formatter: function (param) {        // 标签内容
										return param.data.category;
									}
								}
							},
						}]
					};

					var data = [{
						name: $scope.collegeName,
						symbolSize: [100, 100], // 节点标记大小
						itemStyle: {
							color: '#e93c3c'
						},
					}];
					var links = [];
					var categories = [];
					resp.result.forEach(function (e, i) {
						data.push({
							name: $scope.titleLineFeed(e._id.paPartner, 4),
							symbolSize: [80, 80], // 节点标记大小
							itemStyle: {
								color: colors[i%3]
							},
							value: e.count,
							category: "合作" + e.count + "次",
						});
						links.push({
							target: $scope.titleLineFeed(e._id.paPartner, 4),
							source: $scope.collegeName,
							category: "合作" + e.count + "次",
						});
						categories.push({
							name: "合作" + e.count + "次"
						});
					});
					$scope.options.series[0].data = data;
					$scope.options.series[0].links = links;
					$scope.options.series[0].categories = categories;
					$scope.options.toolbox = {
						right: 70,
						top: 0,
						feature: {
							saveAsImage: {
								name: $scope.collegeName + $scope.ti,
							},
						}
					};

                    // 编辑excel名称 TODO:需要修改
                    var title = "高校联合创新主体分析.xls";
                    if(resp.result && resp.result.length > 0) {
                        // 创建table
                        var table = document.createElement('table');
                        var innerHtml = "<tr>";
                        // 编辑抬头 TODO:需要修改
                        var nameData = new Array('省份', '高校名称', '合作单位','合作次数');
                        nameData.forEach(function (data) {
                            innerHtml += "<th>" + data + "</th>"
                        });
                        innerHtml += "</tr>";
                        // 填充内容 TODO:需要修改
                        resp.result.forEach(function(data) {
                            innerHtml +=
                                "<tr>" +
                                "<td>" + data._id.province + "</td>" +
                                "<td>" + data._id.collegeName + "</td>" +
                                "<td>" + data._id.paPartner + "</td>" +
                                "<td>" + data.count + "</td>" +
                                "</tr>";
                        });
                        table.innerHTML = innerHtml;
                        fixExport($scope.options, table, title);
                    }

					$scope.chart.clear();
					$scope.chart.setOption($scope.options, true);
					$scope.chart.resize();
				}
			}).catch(function (reason) {
				$scope.loading = false;
			});
		};

		$scope.resize = function (isCurrent) {
			if (isCurrent && $scope.chart) {
				$timeout(function () {
					$scope.onChartAware($scope.chart);
					$scope.chart.resize($uibPosition.position($scope.chart.getDom()));
				}, 100);
			}
		};

		// 标题换行
		$scope.titleLineFeed = function(title, num) {
			var time = Math.ceil(title.length/num);
			var result = "";
			for(var i=0; i< time ; i++) {
				if(i == time-1) {
					result += title.substr(i*num) + "\n";
					break;
				}
				result += title.substr(i*num, num) + "\n";
			}
			return result;
		};

		$scope.$watch(function() {
			return $scope.collegeName;
		}, $scope.resize)

	}

	// 11高校专利价值分布
	function CollegePmesValueCtrl($scope, $log, $uibPosition, $timeout, ChartService, PATENT_TYPE, $state) {

		$scope.loading = false;
		$scope.options = {
		};
		$scope.onChartAware = function (chart) {
			$scope.chart = chart;
			if(!$scope.collegeName) {
				return;
			}
			self.loading = true;
			ChartService.getCollegePmesValue($scope.collegeName, $scope.limit).then(function (resp) {
				$scope.loading = false;
				if (resp && resp.code === 0) {
					$scope.ti = "高校专利价值分布(平均分:" + (100* resp.result.avg).toFixed(2) + ")";
					$scope.options = {
						color: ['#277EAB'],
						tooltip : {
							trigger: 'axis',
							axisPointer : {            // 坐标轴指示器，坐标轴触发有效
								type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
							},
							formatter: function(params) {
								return $scope.collegeName + '<br/>'
										+  '分段:   ' + params[0].data[0] + '<br/>'
										+  '专利数量:' + params[0].data[1];
							}
						},
						yAxis : [
							{
								type : 'value',
								name:'专利数量',
								axisLabel:{
									// rotate:30,
									color:'#666666',
									fontSize:'10',
									textStyle:{
										fontSize:20
									}
								},
								axisTick: {
									alignWithLabel: true
								},
								position: 'top'
							}
						],
						xAxis : [
							{
								type : 'category',
								data : resp.result.distri.map(function(e) {
									return e._id.title;
								}),
							}
						],
						series : [
							{
								type:'bar',
								barWidth: 40,
								data: resp.result.distri.map(function(e, index) {
									var _index = e._id.title.indexOf("_");
									var startValue = e._id.title.substr(0,_index);
									var endValue = e._id.title.substr(_index + 1);
									return [e._id.title, e.count, startValue, endValue];
								}),
								// markLine: {
								// 	/*以下设置一行后，平均线就没有开始和结束标记了（即看不见箭头了）*/
								// 	symbol: "none",
								// 	data: [
								// 		{
								// 			name: '平均线', // 支持 'average', 'min', 'max'
								// 			type: 'average',
								// 			lineStyle: {
								// 				normal: {
								// 					color: "green",
								// 					width: 2,
								// 					type: "solid",
								// 				}
								// 			},
								// 			label: {
								// 				formatter: (100* resp.result.avg).toFixed(2) + '',
								// 			}
								// 		},]
								// }
							}
						],
					};
					$scope.options.toolbox = {
						right: 60,
						top: 0,
						feature: {
							saveAsImage: {
								name: $scope.collegeName + $scope.ti,
							},
						}
					};

                    // 编辑excel名称 TODO:需要修改
                    var title = "高校专利价值分布.xls";
                    if(resp.result.distri && resp.result.distri.length > 0) {
                        // 创建table
                        var table = document.createElement('table');
                        var innerHtml = "<tr>";
                        // 编辑抬头 TODO:需要修改
                        var nameData = new Array('省份', '高校名称', '分数范围','专利总数');
                        nameData.forEach(function (data) {
                            innerHtml += "<th>" + data + "</th>"
                        });
                        innerHtml += "</tr>";
                        // 填充内容 TODO:需要修改
                        resp.result.distri.forEach(function(data) {
                            innerHtml +=
                                "<tr>" +
                                "<td>" + data._id.province + "</td>" +
                                "<td>" + data._id.collegeName + "</td>" +
                                "<td>" + data._id.title + "</td>" +
                                "<td>" + data.count + "</td>" +
                                "</tr>";
                        });
                        table.innerHTML = innerHtml;
                        fixExport($scope.options, table, title);
                    }

					$scope.chart.clear();
					$scope.chart.setOption($scope.options, true);
					$scope.chart.resize();
					$scope.chart.on('click', function(params) {
						$state.go("main.console.statistics.college.patent", { collegeName: $scope.collegeName, provinceName: $scope.provinceName, startValue: params.data[2], endValue: params.data[3]});
					});
				}
			}).catch(function (reason) {
				$scope.loading = false;
			});
		};

		$scope.resize = function (isCurrent) {
			if (isCurrent && $scope.chart) {
				$timeout(function () {
					$scope.onChartAware($scope.chart);
					$scope.chart.resize($uibPosition.position($scope.chart.getDom()));
				}, 100);
			}
		};

		// 标题换行
		$scope.titleLineFeed = function(title, num) {
			var time = Math.ceil(title.length/num);
			var result = "";
			for(var i=0; i< time ; i++) {
				if(i == time-1) {
					result += title.substr(i*num) + "\n";
					break;
				}
				result += title.substr(i*num, num) + "\n";
			}
			return result;
		};

		$scope.$watch(function() {
			return $scope.collegeName;
		}, $scope.resize)

	}

	// 12高校发明人
	function CollegePinCtrl($scope, $log, $uibPosition, $timeout, ChartService, PATENT_TYPE, $state) {

		$scope.loading = false;
		$scope.options = {
		};
		$scope.limit = "10";
		$scope.onChartAware = function (chart) {
			$scope.chart = chart;
			if(!$scope.collegeName) {
				return;
			}
			self.loading = true;
			ChartService.getCollegePin($scope.collegeName, $scope.limit).then(function (resp) {
				$scope.loading = false;
				if (resp && resp.code === 0) {
					$scope.options = {
						color: ['#ff927f'],
						tooltip : {
							trigger: 'axis',
							axisPointer : {            // 坐标轴指示器，坐标轴触发有效
								type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
							},
							formatter: function(params) {
								return $scope.collegeName + '<br/>'
										+  '发明人:   ' + params[0].data[1] + '<br/>'
										+  '专利数量: ' + params[0].data[0];
							}
						},
						xAxis : [
							{
								type : 'value',
								name:'专利数量(件)',
								axisLabel:{
									// rotate:30,
									color:'#666666',
									fontSize:'10',
									textStyle:{
										fontSize:20
									}
								},
								axisTick: {
									alignWithLabel: true
								},
								position: 'top'
							}
						],
						yAxis : [
							{
								type : 'category',
								data : resp.result.reverse().map(function(e) {
									return e.value.pinSplit;
								}),
							}
						],
						series : [
							{
								type:'bar',
								barWidth: 22,
								data: resp.result.map(function(e, index) {
									return [e.value.count , e.value.pinSplit];
								}),
							}
						]
					};
					$scope.options.toolbox = {
						right: 70,
						top: 0,
						feature: {
							saveAsImage: {
								name: $scope.title,
							},
						}
					};

                    // 编辑excel名称 TODO:需要修改
                    var title = "发明人技术拥有量.xls";
                    if(resp.result && resp.result.length > 0) {
                        // 创建table
                        var table = document.createElement('table');
                        var innerHtml = "<tr>";
                        // 编辑抬头 TODO:需要修改
                        var nameData = new Array('省份', '高校名称', '发明人','专利数量');
                        nameData.forEach(function (data) {
                            innerHtml += "<th>" + data + "</th>"
                        });
                        innerHtml += "</tr>";
                        // 填充内容 TODO:需要修改
                        resp.result.forEach(function(data) {
                            innerHtml +=
                                "<tr>" +
                                "<td>" + data.value.province + "</td>" +
                                "<td>" + data.value.collegeName + "</td>" +
                                "<td>" + data.value.pinSplit + "</td>" +
                                "<td>" + data.value.count + "</td>" +
                                "</tr>";
                        });
                        table.innerHTML = innerHtml;
                        fixExport($scope.options, table, title);
                    }

					$scope.chart.clear();
					$scope.chart.setOption($scope.options, true);
					$scope.chart.resize();
					$scope.chart.on('click', function(params) {
						$state.go("main.console.statistics.pin", {pinName: params.data[1], collegeName: $scope.collegeName, provinceName: $scope.provinceName});
					});
				}
			}).catch(function (reason) {
				$scope.loading = false;
			});
		};

		$scope.resize = function (isCurrent) {
			if (isCurrent && $scope.chart) {
				$timeout(function () {
					$scope.onChartAware($scope.chart);
					$scope.chart.resize($uibPosition.position($scope.chart.getDom()));
				}, 100);
			}
		};

		// 标题换行
		$scope.titleLineFeed = function(title, num) {
			var time = Math.ceil(title.length/num);
			var result = "";
			for(var i=0; i< time ; i++) {
				if(i == time-1) {
					result += title.substr(i*num) + "\n";
					break;
				}
				result += title.substr(i*num, num) + "\n";
			}
			return result;
		};

		$scope.$watch(function() {
			return $scope.collegeName;
		}, $scope.resize)

	}

	// 13发明人产学研技术详情
	function PinExpertCtrl($scope, $log, $uibPosition, $timeout, ChartService, PATENT_TYPE) {

		$scope.loading = false;

		$scope.init = function (){

			$scope.$watch(function() {
				return $scope.pinName + " " + $scope.collegeName;
			}, $scope.search);

		};

		$scope.search = function () {
			if(!$scope.pinName || !$scope.collegeName) {
				return;
			}
			$scope.loading = true;
			ChartService.getPinExpert($scope.pinName, $scope.collegeName)
					.then(function (resp) {
				$scope.loading = false;
				if (resp && resp.success) {
					var subNum = 10;// 截取数量
					$scope.result = resp.result;
					$scope.keywords = "";// 专家关键词
					$scope.companies = "";// 潜在合作对象
					$scope.experts = "";// 相似专家
					if(resp.result.keywords.keywords) {
						$scope.keywords = resp.result.keywords.keywords.slice(0, Math.min(resp.result.keywords.keywords.length, subNum)).join(";");
					}
					if(resp.result.companies.companies_namelist) {
						$scope.companies = resp.result.companies.companies_namelist.slice(0, Math.min(resp.result.companies.companies_namelist.length, subNum)).join(";");
					}
					if(resp.result.experts.experts_namelist) {
						$scope.experts = resp.result.experts.experts_namelist.slice(0, Math.min(resp.result.experts.experts_namelist.length, subNum)).join(";");
					}
				}
			})
			.catch(function (reason) {
				$scope.loading = false;
			});
		};

		// 标题换行
		$scope.titleLineFeed = function(title, num) {
			var time = Math.ceil(title.length/num);
			var result = "";
			for(var i=0; i< time ; i++) {
				if(i == time-1) {
					result += title.substr(i*num) + "\n";
					break;
				}
				result += title.substr(i*num, num) + "\n";
			}
			return result;
		};

		$scope.init();

	}

	// 14发明人团队展示
	function CollegePinPartnerCtrl($scope, $log, $uibPosition, $timeout, ChartService, PATENT_TYPE) {

		$scope.loading = false;
		$scope.options = {
		};
		$scope.onChartAware = function (chart) {
			$scope.chart = chart;
			if(!$scope.collegeName) {
				return;
			}
			self.loading = true;
			$scope.title = $scope.collegeName +  $scope.pinName + $scope.ti;
			ChartService.getCollegePinPartner($scope.pinName, $scope.collegeName).then(function (resp) {
				$scope.loading = false;
				if (resp && resp.code === 0) {
					var colors = ['#34a37c', '#f5893c', '#5fc8dc'];
					$scope.options = {
						tooltip: {                  // 提示框的配置
							formatter: function(param) {
								if (param.dataType === 'edge') {
									return param.data.category;
								}
								return param.data.name + (param.data.category? ("<br/>" + param.data.category): '');
							}
						},
						series: [{
							type: "graph",          // 系列类型:关系图
							top: '10%',             // 图表距离容器顶部的距离
							roam: false,             // 是否开启鼠标缩放和平移漫游。默认不开启。如果只想要开启缩放或者平移，可以设置成 'scale' 或者 'move'。设置成 true 为都开启
							focusNodeAdjacency: false,   // 是否在鼠标移到节点上的时候突出显示节点以及节点的边和邻接节点。[ default: false ]
							force: {                // 力引导布局相关的配置项，力引导布局是模拟弹簧电荷模型在每两个节点之间添加一个斥力，每条边的两个节点之间添加一个引力，每次迭代节点会在各个斥力和引力的作用下移动位置，多次迭代后节点会静止在一个受力平衡的位置，达到整个模型的能量最小化。	                                // 力引导布局的结果有良好的对称性和局部聚合性，也比较美观。
								repulsion: 1000,            // [ default: 50 ]节点之间的斥力因子(关系对象之间的距离)。支持设置成数组表达斥力的范围，此时不同大小的值会线性映射到不同的斥力。值越大则斥力越大
								// edgeLength: [150, 100]      // [ default: 30 ]边的两个节点之间的距离(关系对象连接线两端对象的距离,会根据关系对象值得大小来判断距离的大小)，
								// 这个距离也会受 repulsion。支持设置成数组表达边长的范围，此时不同大小的值会线性映射到不同的长度。值越小则长度越长。如下示例:
								// 值最大的边长度会趋向于 10，值最小的边长度会趋向于 50
								edgeLength: [200, 200]
							},
							layout: "force",            // 图的布局。[ default: 'none' ]
							// 'none' 不采用任何布局，使用节点中提供的 x， y 作为节点的位置。
							// 'circular' 采用环形布局;'force' 采用力引导布局.
							// 标记的图形
							// symbol: "path://M19.300,3.300 L253.300,3.300 C262.136,3.300 269.300,10.463 269.300,19.300 L269.300,21.300 C269.300,30.137 262.136,37.300 253.300,37.300 L19.300,37.300 C10.463,37.300 3.300,30.137 3.300,21.300 L3.300,19.300 C3.300,10.463 10.463,3.300 19.300,3.300 Z",
							symbol: 'circle',
							lineStyle: {            // 关系边的公用线条样式。其中 lineStyle.color 支持设置为'source'或者'target'特殊值，此时边会自动取源节点或目标节点的颜色作为自己的颜色。
								normal: {
									color: '#000',          // 线的颜色[ default: '#aaa' ]
									width: 1,               // 线宽[ default: 1 ]
									type: 'solid',          // 线的类型[ default: solid ]   'dashed'    'dotted'
									opacity: 0.5,           // 图形透明度。支持从 0 到 1 的数字，为 0 时不绘制该图形。[ default: 0.5 ]
									curveness: 0            // 边的曲度，支持从 0 到 1 的值，值越大曲度越大。[ default: 0 ]
								}
							},
							label: {                // 关系对象上的标签
								normal: {
									show: true,                 // 是否显示标签
									position: "inside",         // 标签位置:'top''left''right''bottom''inside''insideLeft''insideRight''insideTop''insideBottom''insideTopLeft''insideBottomLeft''insideTopRight''insideBottomRight'
									textStyle: {                // 文本样式
										fontSize: 14,
									}
								}
							},
							edgeLabel: {                // 连接两个关系对象的线上的标签
								normal: {
									show: true,
									textStyle: {
										fontSize: 14
									},
									formatter: function (param) {        // 标签内容
										return param.data.category;
									}
								}
							},
						}]
					};

					var data = [{
						name: $scope.pinName,
						symbolSize: [100, 100], // 节点标记大小
						itemStyle: {
							color: '#e93c3c'
						},
					}];
					var links = [];
					var categories = [];
					resp.result.forEach(function (e, i) {
						if(i>10) {
							return;
						}
						data.push({
							name: e.value.partner,
							symbolSize: [80, 80], // 节点标记大小
							itemStyle: {
								color: colors[i%3]
							},
							value: e.value.count,
							category: "合作" + e.value.count + "次",
						});
						links.push({
							target: e.value.partner,
							source: $scope.pinName,
							category: "合作" + e.value.count + "次",
						});
						categories.push({
							name: "合作" + e.value.count + "次"
						});
					});
					$scope.options.series[0].data = data;
					$scope.options.series[0].links = links;
					$scope.options.series[0].categories = categories;
					$scope.options.toolbox = {
						right: 70,
						top: 0,
						feature: {
							saveAsImage: {
								name: $scope.title,
							},
						}
					};

                    // 编辑excel名称 TODO:需要修改
                    var title = "发明人团队统计.xls";
                    if(resp.result && resp.result.length > 0) {
                        // 创建table
                        var table = document.createElement('table');
                        var innerHtml = "<tr>";
                        // 编辑抬头 TODO:需要修改
                        var nameData = new Array('省份', '高校名称', '发明人','合作伙伴','合作次数');
                        nameData.forEach(function (data) {
                            innerHtml += "<th>" + data + "</th>"
                        });
                        innerHtml += "</tr>";
                        // 填充内容 TODO:需要修改
                        resp.result.forEach(function(data) {
                            innerHtml +=
                                "<tr>" +
                                "<td>" + data.value.province + "</td>" +
                                "<td>" + data.value.collegeName + "</td>" +
                                "<td>" + data.value.pinSplit + "</td>" +
                                "<td>" + data.value.partner + "</td>" +
                                "<td>" + data.value.count + "</td>" +
                                "</tr>";
                        });
                        table.innerHTML = innerHtml;
                        fixExport($scope.options, table, title);
                    }

					$scope.chart.clear();
					$scope.chart.setOption($scope.options, true);
					$scope.chart.resize();
				}
			}).catch(function (reason) {
				$scope.loading = false;
			});
		};

		$scope.resize = function (isCurrent) {
			if (isCurrent && $scope.chart) {
				$timeout(function () {
					$scope.onChartAware($scope.chart);
					$scope.chart.resize($uibPosition.position($scope.chart.getDom()));
				}, 100);
			}
		};

		// 标题换行
		$scope.titleLineFeed = function(title, num) {
			var time = Math.ceil(title.length/num);
			var result = "";
			for(var i=0; i< time ; i++) {
				if(i == time-1) {
					result += title.substr(i*num) + "\n";
					break;
				}
				result += title.substr(i*num, num) + "\n";
			}
			return result;
		};

		$scope.$watch(function() {
			return $scope.pinName + " " + $scope.collegeName;
		}, $scope.resize)

	}

	// 15发明人专利价值分布
	function PinPmesValueCtrl($scope, $log, $uibPosition, $timeout, ChartService, PATENT_TYPE, $state) {

		$scope.loading = false;
		$scope.options = {
		};
		$scope.onChartAware = function (chart) {
			$scope.chart = chart;
			if(!$scope.collegeName) {
				return;
			}
			self.loading = true;
			ChartService.getPinPmesValue($scope.pinName, $scope.collegeName).then(function (resp) {
				$scope.loading = false;
				if (resp && resp.code === 0) {
					$scope.title = $scope.collegeName +  $scope.pinName + $scope.ti + "(平均分:" + (100* resp.result.avg).toFixed(2) + ")";
					$scope.ti = "发明人专利价值分布";
					$scope.options = {
						color: ['#277EAB'],
						tooltip : {
							trigger: 'axis',
							axisPointer : {            // 坐标轴指示器，坐标轴触发有效
								type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
							},
							formatter: function(params) {
								return $scope.pinName + '<br/>'
										+  '分段:   ' + params[0].data[0] + '<br/>'
										+  '专利数量:' + params[0].data[1];
							}
						},
						yAxis : [
							{
								type : 'value',
								name:'专利数量',
								axisLabel:{
									// rotate:30,
									color:'#666666',
									fontSize:'10',
									textStyle:{
										fontSize:20
									}
								},
								axisTick: {
									alignWithLabel: true
								},
								position: 'top'
							}
						],
						xAxis : [
							{
								type : 'category',
								data : resp.result.distri.map(function(e) {
									return e._id.title;
								}),
							}
						],
						series : [
							{
								type:'bar',
								barWidth: 40,
								data: resp.result.distri.map(function(e, index) {
									var _index = e._id.title.indexOf("_");
									var startValue = e._id.title.substr(0,_index);
									var endValue = e._id.title.substr(_index + 1);
									return [e._id.title, e.count, startValue, endValue];
								}),
								// markLine: {
								// 	/*以下设置一行后，平均线就没有开始和结束标记了（即看不见箭头了）*/
								// 	symbol: "none",
								// 	data: [
								// 		{
								// 			name: '平均线', // 支持 'average', 'min', 'max'
								// 			type: 'average',
								// 			lineStyle: {
								// 				normal: {
								// 					color: "green",
								// 					width: 2,
								// 					type: "solid",
								// 				}
								// 			},
								// 			label: {
								// 				formatter: (100* resp.result.avg).toFixed(2) + '',
								// 			}
								// 		},]
								// }
							}
						]
					};
					$scope.options.toolbox = {
						right: 70,
						top: 0,
						feature: {
							saveAsImage: {
								name: $scope.title,
							},
						}
					};

                    // 编辑excel名称 TODO:需要修改
                    var title = "发明人价值分布.xls";
                    if(resp.result.distri && resp.result.distri.length > 0) {
                        // 创建table
                        var table = document.createElement('table');
                        var innerHtml = "<tr>";
                        // 编辑抬头 TODO:需要修改
                        var nameData = new Array('省份', '高校名称', '发明人','价值范围','专利数');
                        nameData.forEach(function (data) {
                            innerHtml += "<th>" + data + "</th>"
                        });
                        innerHtml += "</tr>";
                        // 填充内容 TODO:需要修改
                        resp.result.distri.forEach(function(data) {
                            innerHtml +=
                                "<tr>" +
                                "<td>" + data._id.province + "</td>" +
                                "<td>" + data._id.collegeName + "</td>" +
                                "<td>" + data._id.pin + "</td>" +
                                "<td>" + data._id.title + "</td>" +
                                "<td>" + data.count + "</td>" +
                                "</tr>";
                        });
                        table.innerHTML = innerHtml;
                        fixExport($scope.options, table, title);
                    }

					$scope.chart.clear();
					$scope.chart.setOption($scope.options, true);
					$scope.chart.resize();
					$scope.chart.on('click', function(params) {
						$state.go("main.console.statistics.pin.patent", { collegeName: $scope.collegeName, provinceName: $scope.provinceName, pin:$scope.pinName , startValue: params.data[2], endValue: params.data[3]});
					});
				}
			}).catch(function (reason) {
				$scope.loading = false;
			});
		};

		$scope.resize = function (isCurrent) {
			if (isCurrent && $scope.chart) {
				$timeout(function () {
					$scope.onChartAware($scope.chart);
					$scope.chart.resize($uibPosition.position($scope.chart.getDom()));
				}, 100);
			}
		};

		// 标题换行
		$scope.titleLineFeed = function(title, num) {
			var time = Math.ceil(title.length/num);
			var result = "";
			for(var i=0; i< time ; i++) {
				if(i == time-1) {
					result += title.substr(i*num) + "\n";
					break;
				}
				result += title.substr(i*num, num) + "\n";
			}
			return result;
		};

		$scope.$watch(function() {
			return $scope.pinName + " " + $scope.collegeName;
		}, $scope.resize);

	}

	function copyToBoard(content) {
		var oInput = document.createElement('input');
		oInput.value = content;
		document.body.appendChild(oInput);
		oInput.select(); // 选择对象
		document.execCommand("Copy"); // 执行浏览器复制命令
		oInput.className = 'oInput';
		oInput.style.display='none';
		alert('复制成功');
		oInput.remove();
		console.log(oInput)
	}

	function fixExport(options, table, title) {
		if(options==null) {
			return;
		}
		if(!options.toolbox) {
			options.toolbox = {};
		}
		if(!options.toolbox.feature) {
			options.toolbox.feature = {};
		}
		options.toolbox.feature.myExcelExport = {
			show: true,
			title: '导出excel',
			icon: "image://static/images/excel.png",
			onclick: function (){
				$(table).table2excel({
					exclude: ".noExl", //过滤位置的 css 类名
					filename: title, //文件名称
					name: title,
					exclude_img: true,
					exclude_links: true,
					exclude_inputs: true
				});
			}
		}
	}

})(angular, CONTEXT_PATH);