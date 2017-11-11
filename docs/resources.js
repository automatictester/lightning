'use strict';

var resourceApp = angular.module('resourceApp', []);

resourceApp.controller('ResourceListController', function ResourceListController($scope) {
  $scope.resources = [
    {
      title: 'jmeter_configuration',
      url: 'Configure JMeter to save relevant data'
    }, {
      title: 'ci_cd_server_integration',
      url: 'CI/CD server integration'
    }, {
      title: 'enhanced_jenkins_integration',
      url: 'Enhanced Jenkins integration'
    }, {
      title: 'enhanced_teamcity_integration',
      url: 'Enhanced TeamCity integration'
    }, {
      title: 'test_types',
      url: 'Test types'
    }, {
      title: 'server_side_tests',
      url: 'Server-side tests'
    }, {
      title: 'report_mode',
      url: 'Report mode'
    }, {
      title: 'performance',
      url: 'Performance'
    }
  ];
});