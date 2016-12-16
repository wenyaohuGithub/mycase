'use strict';

angular.module('mycaseApp')
    .controller('ProcessDetailController', function ($scope, $rootScope, $stateParams, entity, Process, Role) {
        $scope.process = entity;
        $scope.load = function (id) {
            Process.get({id: id}, function(result) {
                $scope.process = result;
            });
        };
        $rootScope.$on('mycaseApp:processUpdate', function(event, result) {
            $scope.process = result;
        });
    });
