/**
 * Created by Crixalis on 2018/10/15.
 * 企业需求匹配专家
 */

(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('Match')
    .controller('MatchProfessorController', ['$scope', '$state', 'ToastService', 'MatchService', MatchProfessorController])
    .controller('PKController', ['$scope', '$state', 'ToastService', 'MatchService', PKController])
    .controller('MatchProfessorDetailController', ['$scope', '$state', 'ToastService', 'MatchService', 'MetaService', MatchProfessorDetailController])
    .controller('MatchProfessorActionController', ['$scope', '$state', 'ToastService', 'MatchService', 'ModalService', MatchProfessorActionController])
		.controller('MatchProfessorResultController', ['$scope', '$state', 'ToastService', 'MatchService', 'ModalService', MatchProfessorResultController])
		.controller('CorrelationViewerCtrl', ['$scope', '$uibModalInstance', 'ToastService', '$log', 'config', 'MatchService', CorrelationViewerCtrl])

  ;

  function MatchProfessorController($scope , $state, ToastService, MatchService) {
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });

    var self = this;

    self.init = function () {

    	self.states = [
				{ id : "0", name: "未推送"},
				{ id : "1", name: "已推送"},
			];

			self.reset();
			$scope.$watch(function () {
				return self.paginationConf.itemsPerPage + " " + self.paginationConf.currentPage;
			}, self.search);
			setInterval(function () {
				$('.keywordDiv').css("max-width", window.innerWidth - 700);
			},300)
    };

    self.reset = function () {
			self.searchCondition = {pushState: "", lastLegalStatus: ""};
			self.paginationConf = {
				currentPage: 1,
				itemsPerPage: 10
			};

    };

		self.onDatetimeRangeChanged = function (start, end) {
				self.searchCondition.startTime = start ? start.toDate().getTime() : start;
				self.searchCondition.endTime = end ? end.toDate().getTime() : end;
		};

    self.search = function (){
			self.searchCondition.pageSize = self.paginationConf.itemsPerPage;
			self.searchCondition.pageNum = self.paginationConf.currentPage;
			self.loading = true;
			MatchService.searchRmp(self.searchCondition).then(function(resp) {
			  self.loading = false;
				if (resp && resp.code === 0) {
					self.requirements = resp.result.list;
					self.paginationConf.totalItems = resp.result.total;
				} else {
					ToastService.toast({
						type: 'error',
						body: '查询企业需求列表：' + resp.message
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

  function PKController($scope , $state, ToastService, MatchService) {
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });

    var self = this;

    self.init = function () {
    	self.searchCondition = {};
    };

		// allselected checkbox change
		self.changeAllSelected = function () {
			self.keywords.forEach(function (item) {
				item.selected = self.allSelected;
			});
		};

		self.changeSelected = function () {
			// 选中个数
			var num = self.keywords.filter(function (item) {
				return item.selected
			}).length;
			self.allSelected = num == self.keywords.length;
		};


		self.search = function () {
    	self.loading = true;
			MatchService.extardProfessorKeyword(self.searchCondition).then(function(resp) {
				self.loading = false;
				if (resp && resp.code === 0) {
					self.professor = angular.copy(self.searchCondition);
					self.keywords = resp.result;
				} else {
					ToastService.toast({
						type: 'error',
						body: '查询专家关键词：' + resp.message
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

		self.save = function () {
			var keywords = [];
					self.keywords.forEach(function (k) {
				if(k.selected) {
					keywords.push( k.keyword);
				}
			});
			self.professor.keyWords = keywords.join(" ");
			MatchService.saveProfessorKeyWords(self.professor).then(function(resp) {
				self.loading = false;
				if (resp && resp.code === 0) {
					ToastService.toast({
						type: 'success',
						body: '保存专家关键词成功'
					});
				} else {
					ToastService.toast({
						type: 'error',
						body: '保存专家关键词成功：' + resp.message
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

  function MatchProfessorDetailController($scope , $state, ToastService, MatchService, MetaService) {
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });

    var self = this;

    self.init = function () {
    	self.id = $state.params.id;
			self.getSourceMeta();
    	self.get();
    };

    self.get = function () {
    	self.loading = true;
			MatchService.reqDetail(self.id).then(function(resp) {
				self.loading = false;
				if (resp && resp.code === 0) {
					self.req = resp.result;
					self.getKeyword();
				} else {
					ToastService.toast({
						type: 'error',
						body: '查询企业需求详情：' + resp.message
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

    self.getKeyword = function () {
			MatchService.contentKeyword(self.req.requirement).then(function(resp) {
				if (resp && resp.code == 0) {
					self.keyword =
							{
								name: resp.result.map(function(k) {
									return k.keyword
								}).join(";") || '暂无关键词',
								weight: resp.result.map(function(k) {
									return k.rank.toFixed(2)
								}).join(";") ||"暂无权重"
							};
				} else {
					ToastService.toast({
						type: 'error',
						body: '查询企业需求关键词：' + resp.message
					});
				}
			}).catch(function (err) {
				ToastService.toast({
					type: "error",
					body: err.message
				});
			});
		};

    self.getSourceMeta = function () {

			MetaService.getMeta("enterpriseRequireSource").then(function (resp) {
				if (resp && resp.code === 0) {
					self.sourceMeta = resp.result;
				}
			}).catch(function (err) {
				$log.error($scope.metaKey, err);
			})
		};

    self.filterMeta = function (source) {
			if (!source || !self.sourceMeta) {
				return "暂无来源";
			}
			var str = "";
			self.sourceMeta.values.forEach(function (value) {
				if(value.value === source) {
					str = value.name;
					return;
				}
			});
			return str;
		};

    self.init();
  }

  function MatchProfessorActionController($scope , $state, ToastService, MatchService, ModalService) {
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });

    var self = this;

    self.init = function () {
    	self.id = $state.params.id;
    	self.get();
			self.paginationConf = {};
			self.selectedProfessors = [];
			self.hovering = false;
			setInterval(function () {
				$('.keywordDiv').css("max-width", window.innerWidth - 700);
			},300)
		};

    self.get = function () {
    	self.loading = true;
			MatchService.reqDetail(self.id).then(function(resp) {
				self.loading = false;
				if (resp && resp.code === 0) {
					self.req = resp.result;
					if(self.req.pushState == 1) {
						ToastService.toast({
							type: 'info',
							body: '该企业需求已推送过匹配结果!'
						});
						$state.go('main.console.match.rmp');
						return;
					}
					self.getKeyword();
				} else {
					ToastService.toast({
						type: 'error',
						body: '查询企业需求详情：' + resp.message
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

    self.getKeyword = function () {
			MatchService.contentKeyword(self.req.requirement).then(function(resp) {
				if (resp && resp.code == 0) {
					self.keyword =
							{
								name: resp.result.map(function(k) {
									return k.keyword
								}).join(";") || '暂无关键词',
								weight: resp.result.map(function(k) {
									return k.rank.toFixed(2)
								}).join(";") ||"暂无权重"
							};
					self.keywords = resp.result.map(function(k) {
						return k.keyword
					});
					self.reset();
					$scope.$watch(function () {
						return self.paginationConf.itemsPerPage + " " + self.paginationConf.currentPage;
					}, self.search);
				} else {
					ToastService.toast({
						type: 'error',
						body: '查询企业需求关键词：' + resp.message
					});
				}
			}).catch(function (err) {
				ToastService.toast({
					type: "error",
					body: err.message
				});
			});
		};

    self.viewCorrelation = function () {
			MatchService.openCorrelationViewer({keywords: self.keywords}).then(function (resp) {
			}).catch(function (err) {
				$log.info(err);
			});
		};

		// allselected checkbox change
		self.changeAllSelected = function () {
			self.professerors.forEach(function (item) {
				item.selected = self.allSelected;
			});
		};

		self.changeSelected = function () {
			// 选中个数
			var num = self.professerors.filter(function (item) {
				return item.selected
			}).length;
			self.allSelected = num == self.professerors.length;
		};


		// 重置关键词
    self.reset = function () {
			self.expression = self.keywords.join(" OR ") || "";
			self.paginationConf = {
				currentPage: 1,
				itemsPerPage: 10,
			};
		};

    self.search = function () {
    	if(!self.expression) {
    		return;
			}
			MatchService.match(self.expression, self.paginationConf.itemsPerPage, self.paginationConf.currentPage-1).then(function(resp) {
				if (resp && resp.code == 0) {
					self.professerors = resp.result.list;
					self.paginationConf.totalItems = resp.result.total;
				} else {
					ToastService.toast({
						type: 'error',
						body: '企业需求匹配专家：' + resp.message
					});
				}
			}).catch(function (err) {
				ToastService.toast({
					type: "error",
					body: err.message
				});
			});

		};

    self.add = function (professor) {
    	for(var i=0; i<self.selectedProfessors.length; i++) {
    		if(self.selectedProfessors[i].id === professor.id) {
    			return;
				}
			}
			self.selectedProfessors.push(professor);
		};

    self.batchAdd = function () {
			self.professerors.forEach(function (p) {
				if(p.selected) {
					self.add(p);
				}
			});
		};

    self.delete = function (professor) {
			for(var i=0; i<self.selectedProfessors.length; i++) {
				if(self.selectedProfessors[i].id === professor.id) {
					self.selectedProfessors.splice(i,1);
					return;
				}
			}
		};

    self.batchDelete = function (){
			ModalService.openAlert({
				title: '提示',
				message: '您确定清空所选专家吗?'
			}).then(function (code) {
				if (code === 0) {
					self.selectedProfessors = [];
				}
			}).catch(function (err) {
				$log.info(err);
			});
		};

    self.mouseOver = function (professor) {
			self.hovering = true;
			self.content = professor.keyWords;
			var hoverDiv = $(".hoverCotent");
			var x = window.event.x;
			var y = window.event.y;
			hoverDiv.css("top", y);
			hoverDiv.css("left", x);
		};

		self.mouseOver2 = function (professor) {
			self.hovering = true;
			self.content = "高校名称:" + professor.collegeName + "<br/>"
									+ "关键词:" + professor.keyWords;
			var hoverDiv = $(".hoverCotent");
			var x = window.event.x;
			var y = window.event.y;
			hoverDiv.css("top", y - Math.max(Math.abs(hoverDiv.height()), 150));
			hoverDiv.css("left", x + 10);
		};

		self.mouseLeave = function (professor) {
			self.hovering = false;
		};

		self.save = function () {
			ModalService.openAlert({
				title: '提示',
				message: '您确定要推送匹配结果吗(推送之后不再可以修改匹配结果)?'
			}).then(function (code) {
				var professors = [];
				self.selectedProfessors.forEach(function (p) {
					professors.push({
						requirementId: self.id,
						originId: self.req.originId,
						professorRemoteKey: p.id,
						professorName: p.name,
						professorCollegeName: p.collegeName,
						score: p._score,
						matchingDegree: p.matchingDegree,
						keywords: p.keyWords
					})
				});
				MatchService.saveProfessors(professors).then(function(resp) {
					if (resp && resp.code === 0) {
						ToastService.toast({
							type: 'success',
							body: '存储匹配结果成功'
						});
						$state.go('main.console.match.rmp.result', {id: self.id});
					} else {
						ToastService.toast({
							type: 'error',
							body: '存储匹配结果：' + resp.message
						});
					}
				}).catch(function (err) {
					ToastService.toast({
						type: "error",
						body: err.message
					});
				});
			}).catch(function (err) {
				$log.info(err);
			});

		}

    self.init();
  }

  function MatchProfessorResultController($scope , $state, ToastService, MatchService, ModalService) {
    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });

    var self = this;

    self.init = function () {
    	self.id = $state.params.id;
    	self.get();
			self.paginationConf = {
				currentPage: 1,
				itemsPerPage: 10,
			};
			$scope.$watch(function () {
				return self.paginationConf.itemsPerPage + " " + self.paginationConf.currentPage;
			}, self.getResult);
			self.hovering = false;
			setInterval(function () {
				$('.keywordDiv').css("max-width", window.innerWidth - 700);
			},300)
		};

    self.get = function () {
    	self.loading = true;
			MatchService.reqDetail(self.id).then(function(resp) {
				self.loading = false;
				if (resp && resp.code === 0) {
					self.req = resp.result;
					if(self.req.pushState != 1) {
						ToastService.toast({
							type: 'info',
							body: '该企业需求未推送过匹配结果!'
						});
						$state.go('main.console.match.rmp');
						return;
					}
					self.getKeyword();
				} else {
					ToastService.toast({
						type: 'error',
						body: '查询企业需求详情：' + resp.message
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

    self.getResult = function () {
			MatchService.getResult(self.id, self.paginationConf.itemsPerPage, self.paginationConf.currentPage).then(function(resp) {
				if (resp && resp.code == 0) {
					self.professerors = resp.result.list;
					self.paginationConf.totalItems = resp.result.total;
				} else {
					ToastService.toast({
						type: 'error',
						body: '查询企业需求关键词：' + resp.message
					});
				}
			}).catch(function (err) {
				ToastService.toast({
					type: "error",
					body: err.message
				});
			});

		};

    self.getKeyword = function () {
			MatchService.contentKeyword(self.req.requirement).then(function(resp) {
				if (resp && resp.code == 0) {
					self.keyword =
							{
								name: resp.result.map(function(k) {
									return k.keyword
								}).join(";") || '暂无关键词',
								weight: resp.result.map(function(k) {
									return k.rank.toFixed(2)
								}).join(";") ||"暂无权重"
							};
					self.keywords = resp.result.map(function(k) {
									return k.keyword
							});
				} else {
					ToastService.toast({
						type: 'error',
						body: '查询企业需求关键词：' + resp.message
					});
				}
			}).catch(function (err) {
				ToastService.toast({
					type: "error",
					body: err.message
				});
			});
		};

    self.mouseOver = function (professor) {
			self.hovering = true;
			self.content = professor.keywords;
			var hoverDiv = $(".hoverCotent");
			var x = window.event.x;
			var y = window.event.y;
			hoverDiv.css("top", y);
			hoverDiv.css("left", x);
		};

		self.mouseLeave = function (professor) {
			self.hovering = false;
		};

    self.init();
  }

	function CorrelationViewerCtrl($scope, $uibModalInstance, ToastService, $log, config, MatchService) {
		var self = this;

		self.init = function () {
			self.title = config.title;
			self.keywords = config.keywords;
			self.search();
		};

		self.search = function () {
			self.loading = true;
			MatchService.correlation(self.keywords.join("|"), 10).then(function(resp) {
				self.loading = false;
				if (resp && resp.success) {
					self.similars = resp.result.data;
				} else {
					ToastService.toast({
						type: 'error',
						body: '查询企业需求相似词：' + resp.message
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

		self.cancel = function () {
			$uibModalInstance.dismiss('cancel');
		};

		self.init();

	};


})(angular, CONTEXT_PATH);

