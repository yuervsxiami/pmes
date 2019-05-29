/**
 * Created by xiongwei on 2017/1/3.
 */

(function (angular, contextPath, echarts) {
  'use strict';

  angular.module('COMPONENTS', ['CONSTANTS', 'Configuration'])
    .component('jsTree', {
      templateUrl: contextPath + '/static/tpls/components/tree.html',
      controller: ['$scope', '$timeout', TreeController],
      bindings: {
        checkbox: '<',
        nodes: '<',
        checkedNodes: '<',
        onInitDone: '&',
        onNodeSelected: '&'
      }
    })
    .component('echartsTree', {
      template: '<div id="echarts_container" style="min-width: 420px; height: 400px;"></div>',
      controller: ['$scope', '$log', EchartsTreeCtrl],
      controllerAs: 'et',
      bindings: {
        width: '@',
        height: '@',
        tree: '=',
        onTreeSelected: '&',
        onChartCtrlAware: '&'
      }
    })
  ;

  function TreeController($scope, $timeout) {
    var treeEle = angular.element('#js_tree');
    var self = this;
    self.createTree = function () {
      var plugins = ['unique', 'search'];
      if (self.checkbox) {
        plugins.push('checkbox');
      }
      treeEle.on('select_node.jstree', function (event, node) {
        var selectedAuthId = node.selected[0];
        if (self.onNodeSelected) {
          self.onNodeSelected({ id: selectedAuthId });
        }
      }).on('ready.jstree', function (event, node) {
        if (self.onInitDone) {
          self.onInitDone({ tree: treeEle, controller: self });
        }
      }).on('refresh.jstree', function(event, node) {
        if (self.checkbox && self.checkedNodes) {
          var jstree = treeEle.jstree(true);
          angular.forEach(self.checkedNodes, function (node) {
            jstree.check_node(node);
          });
        }
      }).jstree({
        core: {
          multiple: !!self.checkbox,
          check_callback: true,
          themes : {
            stripes : false,
            dots: false,
            responsive: true,
          },
          data: self.nodes,
        },
        force_text: true,
        plugins : plugins,
      });
    };
    self.destroy = function () {
      var tree = treeEle.jstree(true);
      if (tree) {
        tree.destroy();
      }
    };
    self.$onInit = function () {
      self.createTree();
    };
    self.$onDestroy = function () {
      self.destroy();
    };
    self.refresh = function () {
      if (!treeEle.jstree(true).settings) {
        return;
      }
      treeEle.jstree(true).settings.core.data = self.nodes;
      treeEle.jstree(true).refresh();
    };
  }

  function EchartsTreeCtrl($scope, $log) {
    var self = this;
    self.setOption = function () {
      var element = document.getElementById('echarts_container');
      $(element).css('width', self.width || '420px');
      $(element).css('height', self.height || '400px');
      if (!self.chart) {
        self.chart = echarts.init(element);
      }
      var option = {
        tooltip: {
          trigger: 'item',
          triggerOn: 'mousemove'
        },
        series: [{
          type: 'tree',
          top: '1%',
          left: '7%',
          bottom: '1%',
          right: '20%',
          symbolSize: 10,
          symbol: 'circle',
          initialTreeDepth: -1, // 全部展开
          itemStyle: {
            borderColor: '#ccc',
          },

          label: {
            normal: {
              position: 'bottom',
              verticalAlign: 'middle',
              align: 'left',
              fontSize: 12
            }
          },

          leaves: {
            label: {
              normal: {
                position: 'right',
                verticalAlign: 'middle',
                align: 'left',
                fontSize: 12
              }
            }
          },
          data: [self.tree],

          expandAndCollapse: false,
          animation: true,
          animationDuration: 300,
          animationDurationUpdate: 300
        }]
      };
      self.chart.clear();
      self.chart.setOption(option, true);
      self.chart.resize();
    };
    self.showLoading = function () {
      if (self.chart) {
        self.chart.showLoading();
      }
    };
    self.hideLoading = function () {
      if (self.chart) {
        self.chart.hideLoading();
      }
    };
    self.$onInit = function () {
      self.setOption();
      if (self.onChartCtrlAware) {
        self.onChartCtrlAware({ctrl: self});
      }
      if (self.chart) {
        self.chart.on('click', function (params) {
          $log.log('params', params);
          if (self.onTreeSelected) {
            $scope.$apply(function () {
              self.onTreeSelected({label: params.data});
            });
          }
        })
      }
    };
  }

})(angular, CONTEXT_PATH, echarts);