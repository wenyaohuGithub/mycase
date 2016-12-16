'use strict';

angular.module('mycaseApp')
    .controller('ContractDetailController', function ($scope, $rootScope, $state, $stateParams, $timeout, entity, Contract) {
        $scope.contract = entity;

        $scope.load = function (id) {
            Contract.get(id, function(result) {
                $scope.contract = result;
                $scope.$apply();
            });
        };
        $rootScope.$on('mycaseApp:contractUpdate', function(event, result) {
            $scope.contract = result;
        });

        $scope.submitForReview = function () {
            $('#submitForReview_Confirmation').modal('show');
        };

        $scope.confirmToNextProcess = function(nextProcessId){
            Contract.moveToNextProcess('id='+nextProcessId).moveToNextProcess($scope.contract, {id:$scope.contract.id}, function (result) {
                $('#submitNextProcess_Confirmation').modal('hide');
                $scope.clear();
                $scope.contract = result;
            });
        };

        $scope.rejectContractRequest = function() {
            $('#rejectRequest_Confirmation').modal('show');
        };

        /*$scope.confirmToReject = function(){
            Contract.moveToNextProcess('id=1').moveToNextProcess($scope.contract, {id:$scope.contract.id}, function (result) {
                $scope.clear();
                $scope.load($scope.contract.id);
                $('#rejectRequest_Confirmation').modal('hide');
            });
        };*/

        $scope.confirmToReject = function(){
            Contract.reject($scope.contract.id, function (result) {
                $scope.clear();
                $scope.contract = result;
                $('#rejectRequest_Confirmation').modal('hide');
            });
        };

        $scope.clear = function () {
            //$scope.contract = {name: null, review_identifier: null, contract_identifier: null, contracting_method: null, amount: null, amount_written: null, currency: null, amount_current_year: null, submit_date: null, start_date: null, end_date: null, expire_date: null, is_multi_year: null, status: null, state: null, approve_date: null, sign_date: null, archive_date: null, id: null};
            //$('#submitNextProcess_Confirmation').modal('close');
        };
    });
