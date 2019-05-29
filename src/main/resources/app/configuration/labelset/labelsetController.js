/**
 * Created by xiongwei 2018/1/13 下午2:11.
 */

(function (angular, CONTEXT_PATH) {
  'use strict';
  angular.module('Configuration')
    .controller('LabelsetEditerController', ['$uibModalInstance', 'ToastService', 'FormService', 'LabelsetService', 'labelset', LabelsetEditerController])
    .controller('LabelsetController', ['$scope', 'ToastService', 'LabelsetService', 'LabelsetAction', LabelsetController])
    .controller('AddLabelsetController', ['$uibModalInstance', 'ToastService', 'FormService', 'LabelsetService', '$scope', AddLabelsetController])
    .controller('LabelsetLabelsController', ['$uibModalInstance', '$filter', 'ToastService', 'FormService', 'LabelService', 'LabelsetService', 'labelset', LabelsetLabelsController])
    .controller('LabelsetRelationController', ['$uibModalInstance', '$log', 'ToastService', 'FormService', 'LabelService', 'LabelsetService', 'TREE_NODE_TYPES', 'labelset', LabelsetRelationController])
  ;

	function LabelsetEditerController($uibModalInstance, ToastService, FormService, LabelsetService, labelset) {
		var self = this;
		angular.merge(self, FormService);

		self.init = function() {
			self.loading = false;
			self.labelset = {name: labelset.name, id:labelset.id};
			self.title = "修改流程模版";
		}

		self.cancel = function () {
			$uibModalInstance.dismiss('cancel');
		};

		self.ok = function () {
			self.loading = true;
			LabelsetService.edit(self.labelset).then(function (resp) {
				self.loading = false;
				if (resp && resp.code === 0) {
					$uibModalInstance.close(resp.result);
				} else {
					ToastService.toast({
						type: 'error',
						body: resp.message
					});
				}
			}).catch(function (err) {
				self.loading = false;
				ToastService.toast({
					type: 'error',
					body: err.message
				});
			});
		};

		self.init();
	}
  
  function LabelsetRelationController($uibModalInstance, $log, ToastService, FormService, LabelService, LabelsetService, TREE_NODE_TYPES, labelset) {
    var self = this;
    angular.merge(self, FormService);
    self.nodeTypes = TREE_NODE_TYPES;
    self.selectedNodeType = self.nodeTypes[0];
    self.showsLabelForm = false;
    self.labelset = labelset;

    self.init = function () {
			LabelsetService.getLabelsetLabels(self.labelset.id).then(function (resp) {
				if (resp && resp.code === 0) {
					self.labelset.labelsetLabels = resp.result;
					self.labels = self.labelset.labelsetLabels.filter(function (label) {
						return label.parentId !== 0 && !label.parentId;
					});

					self.tree = {
						id: 0,
						name: self.labelset.name,
						type: 1,
						children: self.labelset.labelsetLabels.filter(function (label) {
							return label.parentId === 0;
						})
					};
				} else {
					ToastService.toast({
						type: "error",
						body: '获取标签体系标签失败'
					});
				}
			}).catch(ToastService.errHandler);
    };

    self.findTreeNode = function (tree, id) {
      if (tree.id === id) {
        return tree;
      }
      for (var i in tree.children) {
        var c = self.findTreeNode(tree.children[i], id);
        if (c) {
          return c;
        }
      }
      return null;
    };
    self.findParentTreeNode = function (tree, id) {
      for (var i in tree.children) {
        var node = tree.children[i];
        if (node.id === id) {
          return tree;
        }
        var c = self.findParentTreeNode(node, id);
        if (c) {
          return c;
        }
      }
      return null;
    };

    // 得到echarts的echart对象
    self.onChartCtrlAware = function (ctrl) {
      self.treeCtrl = ctrl;
    };
    self.onTreeSelected = function (label) {
      self.selectedLabel = self.labelset.labelsetLabels.filter(function (l) {
        return label.id === l.id;
      })[0];
      if (!self.selectedLabel) {
        self.selectedLabel = self.tree;
      }
    };
    // 刷新树
    self.refreshTree = function () {
      if (self.treeCtrl) {
        self.treeCtrl.setOption();
      }
    };
    
    self.showLabelForm = function () {
      self.showsLabelForm = true;
    };
    self.hideLabelForm = function () {
      self.showsLabelForm = false;
    };

    // 添加标签到树
    self.addLabelsToTree = function () {
      var selected = self.labels.filter(function (l) {
        return l.selected;
      });
      if (selected.length === 0) {
        ToastService.toast({
          type: 'error',
          body: '请至少选择一个标签！'
        });
        return;
      }
      var i = self.labels.length;
      while (i--) {
        var label = self.labels[i];
        if (label.selected) {
          label.type = self.selectedNodeType.id;
          var original = self.labelset.labelsetLabels.filter(function (l) {
            return label.id === l.id;
          })[0];
          original.parentId = self.selectedLabel.id;

          var selectedTree = self.findTreeNode(self.tree, self.selectedLabel.id);
          if (!selectedTree.children) {
            selectedTree.children = [];
          }
          selectedTree.children.push(label);
          self.labels.splice(i, 1);
        }
      }
      self.refreshTree();
    };

    self.getChildrenLabels = function () {
      $log.log('self.selectedLabel', self.selectedLabel);
      if (!self.selectedLabel || self.selectedLabel.type === 0) {
        return [];
      }
      var childLabelIds = self.selectedLabel.children.map(function (l) {
        return l.id;
      });
      $log.log('childLabelIds', childLabelIds);
      var labels = self.labelset.labelsetLabels.filter(function (l) {
        return childLabelIds.indexOf(l.id) >= 0;
      });
      $log.log('labels', labels);
      return labels;
    };

    // 从树上移除选择的标签
    self.removeNode = function () {
      if (!self.selectedLabel) {
        return;
      }
      if (self.selectedLabel.id === 0) {
        ToastService.toast({
          type: 'error',
          body: '请不能移除根节点！'
        });
        return;
      }
      if (self.selectedLabel.children && self.selectedLabel.children.length > 0) {
        ToastService.toast({
          type: 'error',
          body: '请先移除叶子节点的子节点！'
        });
        return;
      }
      delete self.selectedLabel.parentId;
      self.selectedLabel.selected = false;
      var parent = self.findParentTreeNode(self.tree, self.selectedLabel.id);
      for (var i = 0; i < parent.children.length; i++) {
        var l = parent.children[i];
        if (l.id === self.selectedLabel.id) {
          parent.children.splice(i, 1);
          break;
        }
      }
      var selectedParent = self.labelset.labelsetLabels.filter(function (l) {
        return parent.id === l.id;
      })[0];
      for (var i = 0; i < selectedParent.children.length; i++) {
        var l = selectedParent.children[i];
        if (l.id === self.selectedLabel.id) {
          selectedParent.children.splice(i, 1);
          break;
        }
      }
      self.labels.push(self.selectedLabel);
      delete self.selectedLabel;
      self.refreshTree();
    };
    // 监听所选节点的子节点的权重之和
    self.onChange = function (label, changed) {
      /**
      var labels = self.getChildrenLabels();
      if (labels.length <= 1) {
        return;
      }
      var sum = labels.reduce(function (sum, label) {
        return sum + label.weight;
      }, 0);
      var adjust = sum - 100;
      $log.log('adjust', adjust);
      var flag = false;
      var adjusted = false;
      for (var index in labels) {
        var l = labels[index];
        if (l.id === label.id) {
          flag = true;
          continue;
        }
        if (flag && !adjusted) {
          l.weight -= adjust;
          adjusted = true;
        }
      }
       */
    };

    self.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };
    self.ok = function () {
      LabelsetService.update(self.labelset).then(function (resp) {
        if (resp && resp.code === 0) {
          $uibModalInstance.close(resp.result);
        } else {
          ToastService.toast({
            type: 'error',
            body: resp.message
          });
        }
      }).catch(function (err) {
        ToastService.toast({
          type: 'error',
          body: err.message
        });
      })
    };

    self.init();

  }

  function LabelsetLabelsController($uibModalInstance, $filter, ToastService, FormService, LabelService, LabelsetService, labelset) {
    var self = this;
    angular.merge(self, FormService);
    self.labelset = labelset;
    self.labels = [];
    self.loading = false;
    self.labelKeyword = ''; // 待添加标签的关键词
    self.labelsetKeyword = ''; // 已添加标签的关键词
    self.allSelected = false; // 是否全选所有待添加的标签
    self.setLabelAllSelected = false; // 是否全选所有已添加的标签

    self.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };

    // 根据不通标签体系获取对应的标签类型
    self.getLabelType = function () {
      switch (self.labelset.type) {
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
          return 1; // 专利标签
        case 6:
          return 2; // 企业信息标签
        case 7:
          return 3; // 企业需求标签
        case 8:
          return 4; // 专家标签
      }
    };
    
    // 
    self.isAllSelected = function () {
      return self.labels && self.labels.length > 0 && self.labels.every(function (value) {
        return value.selected;
      });
    };

    // 选中或取消选中
    self.selectAllLabels = function () {
      if (self.labels && self.labels.length > 0) {
        var filter = $filter('filter');
        angular.forEach(filter(self.labels, self.labelKeyword), function (label) {
          label.selected = self.allSelected;
        });
      }
    };

    //
    self.isAllSetLabelSelected = function () {
      return self.labelset && self.labelset.labelsetLabels && self.labelset.labelsetLabels.length > 0 && self.labelset.labelsetLabels.every(function (value) {
        return value.selected || value.parentId === 0;
      });
    };

    // 选中或取消选中
    self.selectAllSetLabels = function () {
      if (self.labelset && self.labelset.labelsetLabels.length > 0) {
        var filter = $filter('filter');
        angular.forEach(filter(self.labelset.labelsetLabels, self.labelsetKeyword), function (label) {
          if (label.parentId !== 0 && !label.parentId) {
            label.selected = self.setLabelAllSelected;
          }
        });
      }
    };
    
    self.getAddedLabelIds = function () {
      return self.labelset.labelsetLabels.map(function (label) {
        return label.labelId;
      });
    };
    
    // addLabels
    self.addLabels = function () {
      var selected = self.labels.filter(function (value) { return value.selected; });
      if (selected.length === 0) {
        ToastService.toast({
          type: 'warn',
          body: '请至少选择一个标签！'
        });
        return;
      }
      var i = self.labels.length;
      while (i--) {
        var label = self.labels[i];
        if (label.selected) {
          self.labelset.labelsetLabels.unshift({
            labelsetId: self.labelset.id,
            labelId: label.id,
            label: label,
            type: 0,
            name: label.name,
            weight: 0,
          });
          self.labels.splice(i, 1);
        }
      }
    };

    // removeLabels
    self.removeLabels = function () {
      var selected = self.labelset.labelsetLabels.filter(function (value) { return value.selected; });
      if (selected.length === 0) {
        ToastService.toast({
          type: 'warn',
          body: '请至少选择一个标签！'
        });
        return;
      }
      var i = self.labelset.labelsetLabels.length;
      while (i--) {
        var label = self.labelset.labelsetLabels[i];
        if (label.selected) {
          label.label.selected = false;
          self.labels.unshift(label.label);
          self.labelset.labelsetLabels.splice(i, 1);
        }
      }
    };

    // 查询待添加的标签
    self.findLabels = function () {
      var searchCondition = {
        pageSize: 9999,
        pageNum: 1,
        name: self.labelKeyword,
        type: self.getLabelType()
      };
      LabelService.search(searchCondition).then(function (resp) {
        if (resp && resp.code === 0) {
          self.labels.splice(0, self.labels.length);
          var addedLabelIds = self.getAddedLabelIds();
          angular.forEach(resp.result.list, function (label) {
            if (addedLabelIds.indexOf(label.id) < 0) {
              self.labels.push(label);
            }
          });
        } else {
          ToastService.toast({
            type: "error",
            body: '获取标签失败'
          });
        }
      }).catch(ToastService.errHandler);
    };

    self.ok = function () {
      LabelsetService.update(self.labelset).then(function (resp) {
        if (resp && resp.code === 0) {
          $uibModalInstance.close(resp.result);
        } else {
          ToastService.toast({
            type: 'error',
            body: resp.message
          });
        }
      }).catch(function (err) {
        ToastService.toast({
          type: 'error',
          body: err.message
        });
      })
    };

    self.init = function () {
      LabelsetService.getLabelsetLabels(self.labelset.id).then(function (resp) {
				if (resp && resp.code === 0) {
					self.labelset.labelsetLabels = resp.result;
					self.findLabels();
				} else {
					ToastService.toast({
						type: "error",
						body: '获取标签体系标签失败'
					});
				}
			}).catch(ToastService.errHandler);
    };

    self.init();

  }

  function AddLabelsetController($uibModalInstance, ToastService, FormService, LabelsetService, $scope) {
    var self = this;
    angular.merge(self, FormService);

    self.typeLabelsets = {};

    self.loading = false;
    self.labelset = {
      version: '1', // V1
      type: '1', // 专利价值快速评估体系
      name: ''
    };

    $scope.$watch(function() {
      return self.labelset.type;
    }, function () {
			self.labelset.id = null;
			if(self.typeLabelsets[self.labelset.type] != null) {
        self.currentLabelsets = self.typeLabelsets[self.labelset.type];
        return;
      }
      self.loading = true;
			LabelsetService.findByType(self.labelset.type).then(function (resp) {
				self.loading = false;
				if (resp && resp.code === 0) {
					self.currentLabelsets = resp.result;
					self.typeLabelsets[self.labelset.type] = resp.result;
				} else {
					ToastService.toast({
						type: 'error',
						body: resp.message
					});
				}
			}).catch(function (err) {
				self.loading = false;
				ToastService.toast({
					type: 'error',
					body: err.message
				});
			});
    });

    self.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };

    self.ok = function () {
      self.loading = true;
      if(!self.labelset.id) {
        self.labelset.id = null;
      }
      LabelsetService.add(self.labelset).then(function (resp) {
        self.loading = false;
        if (resp && resp.code === 0) {
          $uibModalInstance.close(resp.result);
        } else {
          ToastService.toast({
            type: 'error',
            body: resp.message
          });
        }
      }).catch(function (err) {
        self.loading = false;
        ToastService.toast({
          type: 'error',
          body: err.message
        });
      });
    };
  }

  function LabelsetController($scope, ToastService, LabelsetService, LabelsetAction) {

    var self = this;

    $scope.$on('$viewContentLoaded', function (event) {
      $.AdminLTE.layout.fix();
    });

		// 修改流程模版
		self.edit = function (labelset) {
			LabelsetAction.updateLabelset(labelset).then(function (process) {
				ToastService.toast({
					type: 'success',
					body: '修改标签体系成功！'
				});
				self.search();
			}).catch(function (reason) {
				ToastService.errHandler(reason);
			});
		};

    self.loading = false;
    self.contextPath = CONTEXT_PATH;
    self.maxDate = Date.now();
    self.searchCondition = {
      type: '',
    };
    //配置分页基本参数
    self.paginationConf = {
      currentPage: 1,
      itemsPerPage: 10
    };

    self.onDatetimeRangeChanged = function (start, end) {
      self.searchCondition.fromTime = start ? start.toDate().getTime(): start;
      self.searchCondition.toTime = end ? end.toDate().getTime() : end;
    };

    // 搜索
    self.search = function () {
      self.loading = true;
			self.searchCondition.pageSize = self.paginationConf.itemsPerPage;
			self.searchCondition.pageNum = self.paginationConf.currentPage;
      LabelsetService.search(self.searchCondition).then(function (resp) {
        self.loading = false;
        if (resp.code === 0) {
          self.paginationConf.totalItems = resp.result.total;
          self.labelsets = resp.result.list;
        } else {
          ToastService.toast({
            type: 'error',
            body: resp.message
          });
        }
      }).catch(function (reason) {
        self.loading = false;
        ToastService.errHandler(reason);
      })
    };

    // 添加标签体系
    self.add = function () {
      LabelsetAction.addLabelset().then(function (labelset) {
        ToastService.toast({
          type: 'success',
          body: '添加标签体系成功！'
        });
        self.search();
      }).catch(function (reason) {
        ToastService.errHandler(reason);
      });
    };

    // 切换体系状态
    self.toggleState = function (labelset) {
      self.loading = true;
      var update = {
        id: labelset.id,
        state: labelset.state === 0 ? 1 : 0
      };
      LabelsetService.toggleState(update).then(function (resp) {
        self.loading = false;
        if (resp.code === 0) {
          labelset.state = labelset.state === 0 ? 1 : 0;
          ToastService.toast({
            type: 'success',
            body: '标签体系' + (labelset.state === 0 ? '禁用' : '启用') + '成功！'
          });
        } else {
          ToastService.toast({
            type: 'error',
            body: resp.message
          });
        }
      }).catch(function (reason) {
        self.loading = false;
        ToastService.errHandler(reason);
      })
    };

    // 添加标签体系标签
    self.addLabels = function (labelset) {
      LabelsetAction.addLabelsetLabels(labelset).then(function (labelset) {
        ToastService.toast({
          type: 'success',
          body: '添加标签体系标签成功！'
        });
        self.search();
      }).catch(function (reason) {
        ToastService.errHandler(reason);
      });
    };

    // 添加标签关系
    self.addLabelsetRelation = function (labelset) {
      LabelsetAction.addLabelsetRelation(labelset).then(function (labelset) {
        ToastService.toast({
          type: 'success',
          body: '设置标签体系标签关系成功！'
        });
        self.search();
      }).catch(function (reason) {
        ToastService.errHandler(reason);
      });
    };

		$scope.$watch(function () {
			return self.paginationConf.itemsPerPage + " " + self.paginationConf.currentPage;
		}, self.search);
		//
    // // 初始化搜索
    // self.search();

  }

})(angular, CONTEXT_PATH);