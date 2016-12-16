'use strict';

angular.module('mycaseApp').controller('SecondEntityDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'SecondEntity', 'FirstEntity',
        function($scope, $stateParams, $modalInstance, entity, SecondEntity, FirstEntity) {

        $scope.secondEntity = entity;
        $scope.firstentitys = FirstEntity.query();
        $scope.load = function(id) {
            SecondEntity.get({id : id}, function(result) {
                $scope.secondEntity = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('mycaseApp:secondEntityUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.secondEntity.id != null) {
                SecondEntity.update($scope.secondEntity, onSaveFinished);
            } else {
                SecondEntity.save($scope.secondEntity, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
