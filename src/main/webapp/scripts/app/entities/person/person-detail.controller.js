'use strict';

angular.module('mycaseApp')
    .controller('PersonDetailController', function ($scope, $rootScope, $stateParams, entity, Person, Address) {
        $scope.person = entity;
        $scope.load = function (id) {
            Person.get({id: id}, function(result) {
                $scope.person = result;
            });
        };
        $rootScope.$on('mycaseApp:personUpdate', function(event, result) {
            $scope.person = result;
        });
    });
