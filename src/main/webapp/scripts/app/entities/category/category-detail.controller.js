'use strict';

angular.module('mycaseApp')
    .controller('CategoryDetailController', function ($scope, $rootScope, $stateParams, entity, Category) {
        $scope.category = entity;
        $scope.load = function (id) {
            Category.get({id: id}, function(result) {
                $scope.category = result;
            });
        };
        $rootScope.$on('mycaseApp:categoryUpdate', function(event, result) {
            $scope.category = result;
        });
    });
