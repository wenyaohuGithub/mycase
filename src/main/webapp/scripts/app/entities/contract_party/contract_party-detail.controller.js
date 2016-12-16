'use strict';

angular.module('mycaseApp')
    .controller('Contract_partyDetailController', function ($scope, $rootScope, $stateParams, entity, Contract_party, Address, Bank_account) {
        $scope.contract_party = entity;
        $scope.load = function (id) {
            Contract_party.get({id: id}, function(result) {
                $scope.contract_party = result;
            });
        };
        $rootScope.$on('mycaseApp:contract_partyUpdate', function(event, result) {
            $scope.contract_party = result;
        });
    });
