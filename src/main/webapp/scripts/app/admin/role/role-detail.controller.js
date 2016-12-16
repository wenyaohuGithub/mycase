'use strict';

angular.module('mycaseApp')
    .controller('RoleDetailController', function ($scope, $rootScope, $stateParams, entity, Role) {
        $scope.role = entity;
        $scope.load = function (name) {
            Role.get({name: name}, function(result) {
                $scope.role = result;
            });
        };
        $rootScope.$on('mycaseApp:roleUpdate', function(event, result) {
            $scope.role = result;
        });
    });
