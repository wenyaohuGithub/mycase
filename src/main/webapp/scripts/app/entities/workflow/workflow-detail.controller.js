'use strict';

angular.module('mycaseApp')
    .controller('WorkflowDetailController', function ($scope, $rootScope, $stateParams, entity, Workflow, Process) {
        $scope.workflow = entity;

        $scope.load = function (id) {
            Workflow.get({id: id}, function(result) {
                $scope.workflow = result;
            });
        };
        $rootScope.$on('mycaseApp:workflowUpdate', function(event, result) {
            $scope.workflow = result;
        });
    });
