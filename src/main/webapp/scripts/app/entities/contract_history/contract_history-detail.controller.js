'use strict';

angular.module('mycaseApp')
    .controller('Contract_historyDetailController', function ($scope, $rootScope, $stateParams, entity, Contract_history, Contract, User, Process) {
        $scope.contract_history = entity;
        $scope.load = function (id) {
            Contract_history.get({id: id}, function(result) {
                $scope.contract_history = result;
            });
        };
        $rootScope.$on('mycaseApp:contract_historyUpdate', function(event, result) {
            $scope.contract_history = result;
        });
    });
