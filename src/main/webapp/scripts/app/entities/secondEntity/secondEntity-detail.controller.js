'use strict';

angular.module('mycaseApp')
    .controller('SecondEntityDetailController', function ($scope, $rootScope, $stateParams, entity, SecondEntity, FirstEntity) {
        $scope.secondEntity = entity;
        $scope.load = function (id) {
            SecondEntity.get({id: id}, function(result) {
                $scope.secondEntity = result;
            });
        };
        $rootScope.$on('mycaseApp:secondEntityUpdate', function(event, result) {
            $scope.secondEntity = result;
        });
    });
