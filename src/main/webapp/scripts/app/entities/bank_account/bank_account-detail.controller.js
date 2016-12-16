'use strict';

angular.module('mycaseApp')
    .controller('Bank_accountDetailController', function ($scope, $rootScope, $stateParams, entity, Bank_account) {
        $scope.bank_account = entity;
        $scope.load = function (id) {
            Bank_account.get({id: id}, function(result) {
                $scope.bank_account = result;
            });
        };
        $rootScope.$on('mycaseApp:bank_accountUpdate', function(event, result) {
            $scope.bank_account = result;
        });
    });
