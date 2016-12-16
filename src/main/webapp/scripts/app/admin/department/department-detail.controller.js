'use strict';

angular.module('mycaseApp')
    .controller('DepartmentDetailController', function ($scope, $rootScope, $stateParams, entity, Department, Person) {
        $scope.department = entity;
        $scope.load = function (id) {
            Department.deleteDepartment().getDepartment({id: id}, function(result) {
                $scope.department = result;
            });
        };
        $rootScope.$on('mycaseApp:departmentUpdate', function(event, result) {
            $scope.department = result;
        });
    });
