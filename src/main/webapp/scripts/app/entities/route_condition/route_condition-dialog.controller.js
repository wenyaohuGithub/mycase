'use strict';

angular.module('mycaseApp').controller('Route_conditionDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Route_condition', 'Route', 'Role', 'Category',
        function($scope, $stateParams, $modalInstance, entity, Route_condition, Route, Role, Category) {

        $scope.route_condition = entity;
        $scope.routes = Route.query();
        $scope.roles = Role.query();
        $scope.categorys = Category.query();
        $scope.load = function(id) {
            Route_condition.get({id : id}, function(result) {
                $scope.route_condition = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('mycaseApp:route_conditionUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.route_condition.id != null) {
                Route_condition.update($scope.route_condition, onSaveFinished);
            } else {
                Route_condition.save($scope.route_condition, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
