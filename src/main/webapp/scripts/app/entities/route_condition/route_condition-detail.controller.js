'use strict';

angular.module('mycaseApp')
    .controller('Route_conditionDetailController', function ($scope, $rootScope, $stateParams, entity, Route_condition, Route, Role, Category) {
        $scope.route_condition = entity;
        $scope.load = function (id) {
            Route_condition.get({id: id}, function(result) {
                $scope.route_condition = result;
            });
        };
        $rootScope.$on('mycaseApp:route_conditionUpdate', function(event, result) {
            $scope.route_condition = result;
        });
    });
