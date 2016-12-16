'use strict';

angular.module('mycaseApp')
    .controller('WorkflowController', function ($scope, Workflow) {
        $scope.workflows = [];
        $scope.loadAll = function() {
            Workflow.query(function(result) {
               $scope.workflows = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Workflow.get({id: id}, function(result) {
                $scope.workflow = result;
                $('#deleteWorkflowConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Workflow.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteWorkflowConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.workflow = {name: null, description: null, id: null};
        };
    });
