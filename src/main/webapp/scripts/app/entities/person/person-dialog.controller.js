'use strict';

angular.module('mycaseApp').controller('PersonDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Person', 'Address',
        function($scope, $stateParams, $modalInstance, entity, Person, Address) {

        $scope.person = entity;
        $scope.addresss = Address.query();
        $scope.load = function(id) {
            Person.get({id : id}, function(result) {
                $scope.person = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('mycaseApp:personUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.person.id != null) {
                Person.update($scope.person, onSaveFinished);
            } else {
                Person.save($scope.person, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
