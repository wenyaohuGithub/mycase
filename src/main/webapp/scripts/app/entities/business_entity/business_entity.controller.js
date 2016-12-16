'use strict';

angular.module('mycaseApp')
    .controller('Business_entityController', function ($scope, Business_entity, ParseLinks) {
        $scope.business_entitys = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Business_entity.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.business_entitys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Business_entity.get({id: id}, function(result) {
                $scope.business_entity = result;
                $('#deleteBusiness_entityConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Business_entity.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteBusiness_entityConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.business_entity = {name: null, description: null, registration_id: null, registered_capital: null, registration_inspection_record: null, id: null};
        };
    });
