'use strict';

angular.module('mycaseApp').controller('Business_entityDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Business_entity', 'Person', 'Address', 'Contract',
        function($scope, $stateParams, $modalInstance, entity, Business_entity, Person, Address, Contract) {

        $scope.business_entity = entity;
        $scope.persons = Person.query();
        $scope.addresss = Address.query();
        $scope.contracts = Contract.query();
        $scope.load = function(id) {
            Business_entity.get({id : id}, function(result) {
                $scope.business_entity = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('mycaseApp:business_entityUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.business_entity.id != null) {
                Business_entity.update($scope.business_entity, onSaveFinished);
            } else {
                Business_entity.save($scope.business_entity, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
