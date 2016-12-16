'use strict';

angular.module('mycaseApp').controller('ProcessDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Process', 'Role',
        function($scope, $stateParams, $modalInstance, entity, Process, Role) {

        $scope.process = entity;
        $scope.roles = Role.query();
        $scope.load = function(id) {
            Process.get({id : id}, function(result) {
                $scope.process = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('mycaseApp:processUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.process.id != null) {
                Process.update($scope.process, onSaveFinished);
            } else {
                Process.save($scope.process, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
