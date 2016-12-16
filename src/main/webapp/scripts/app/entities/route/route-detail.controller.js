'use strict';

angular.module('mycaseApp')
    .controller('RouteDetailController', function ($scope, $rootScope, $stateParams, entity, Route, Process) {
        $scope.route = entity;
        $scope.load = function (id) {
            Route.get({id: id}, function(result) {
                $scope.route = result;
            });
        };
        $rootScope.$on('mycaseApp:routeUpdate', function(event, result) {
            $scope.route = result;
        });
    });
