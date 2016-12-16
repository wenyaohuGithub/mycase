'use strict';

angular.module('mycaseApp')
    .controller('ProjectDetailController', function ($scope, $rootScope, $stateParams, entity, Project) {
        $scope.project = entity;
        $scope.load = function (id) {
            Project.get({id: id}, function(result) {
                $scope.project = result;
            });
        };
        $rootScope.$on('mycaseApp:projectUpdate', function(event, result) {
            $scope.project = result;
        });
    });
