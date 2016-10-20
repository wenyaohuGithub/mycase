'use strict';

angular.module('mycaseApp').controller('ContractDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Contract', 'Person',
        function($scope, $stateParams, $modalInstance, entity, Contract, Person) {

        $scope.contract = entity;
        $scope.persons = Person.query();
        $scope.load = function(id) {
            Contract.get({id : id}, function(result) {
                $scope.contract = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('mycaseApp:contractUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.contract.id != null) {
                Contract.update($scope.contract, onSaveFinished);
            } else {
                Contract.save($scope.contract, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
