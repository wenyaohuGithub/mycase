'use strict';

angular.module('mycaseApp')
    .controller('Route_conditionController', function ($scope, Route_condition, ParseLinks) {
        $scope.route_conditions = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Route_condition.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.route_conditions = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Route_condition.get({id: id}, function(result) {
                $scope.route_condition = result;
                $('#deleteRoute_conditionConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Route_condition.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteRoute_conditionConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.route_condition = {previous_step_result: null, id: null};
        };
    });
