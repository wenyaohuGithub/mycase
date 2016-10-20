'use strict';

angular.module('mycaseApp')
    .controller('ContractDetailController', function ($scope, $rootScope, $stateParams, entity, Contract, Person) {
        $scope.contract = entity;
        $scope.load = function (id) {
            Contract.get({id: id}, function(result) {
                $scope.contract = result;
            });
        };
        $rootScope.$on('mycaseApp:contractUpdate', function(event, result) {
            $scope.contract = result;
        });
    });
