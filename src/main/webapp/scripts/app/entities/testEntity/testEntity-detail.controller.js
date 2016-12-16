'use strict';

angular.module('mycaseApp')
    .controller('TestEntityDetailController', function ($scope, $rootScope, $stateParams, entity, TestEntity) {
        $scope.testEntity = entity;
        $scope.load = function (id) {
            TestEntity.get({id: id}, function(result) {
                $scope.testEntity = result;
            });
        };
        $rootScope.$on('mycaseApp:testEntityUpdate', function(event, result) {
            $scope.testEntity = result;
        });
    });
