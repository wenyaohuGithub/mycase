'use strict';

angular.module('mycaseApp')
    .controller('RouteController', function ($scope, Route, ParseLinks) {
        $scope.routes = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Route.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.routes = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Route.get({id: id}, function(result) {
                $scope.route = result;
                $('#deleteRouteConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Route.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteRouteConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.route = {name: null, description: null, id: null};
        };
    });
