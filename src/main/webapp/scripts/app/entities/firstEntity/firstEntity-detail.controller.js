'use strict';

angular.module('mycaseApp')
    .controller('FirstEntityDetailController', function ($scope, $rootScope, $stateParams, entity, FirstEntity) {
        $scope.firstEntity = entity;
        $scope.load = function (id) {
            FirstEntity.get({id: id}, function(result) {
                $scope.firstEntity = result;
            });
        };
        $rootScope.$on('mycaseApp:firstEntityUpdate', function(event, result) {
            $scope.firstEntity = result;
        });
    });
