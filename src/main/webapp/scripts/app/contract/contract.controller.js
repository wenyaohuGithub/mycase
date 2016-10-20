'use strict';

angular.module('mycaseApp')
    .controller('ContractController', function ($scope, Contract, ParseLinks) {
        $scope.contracts = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Contract.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.contracts = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Contract.get({id: id}, function(result) {
                $scope.contract = result;
                $('#deleteContractConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Contract.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteContractConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.contract = {name: null, description: null, annual_spend_amount: null, owner: null, supplier: null, id: null};
        };
    });
