'use strict';

angular.module('mycaseApp')
    .controller('SecondEntityController', function ($scope, SecondEntity, ParseLinks) {
        $scope.secondEntitys = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            SecondEntity.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.secondEntitys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            SecondEntity.get({id: id}, function(result) {
                $scope.secondEntity = result;
                $('#deleteSecondEntityConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            SecondEntity.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSecondEntityConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.secondEntity = {name: null, description: null, id: null};
        };
    });
