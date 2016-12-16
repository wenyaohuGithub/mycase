'use strict';

angular.module('mycaseApp').controller('RouteDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Route', 'Process',
        function($scope, $stateParams, $modalInstance, entity, Route, Process) {

        $scope.route = entity;
        $scope.processs = Process.query();
        $scope.load = function(id) {
            Route.get({id : id}, function(result) {
                $scope.route = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('mycaseApp:routeUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.route.id != null) {
                Route.update($scope.route, onSaveFinished);
            } else {
                Route.save($scope.route, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
