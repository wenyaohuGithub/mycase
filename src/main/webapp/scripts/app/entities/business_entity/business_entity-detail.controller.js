'use strict';

angular.module('mycaseApp')
    .controller('Business_entityDetailController', function ($scope, $rootScope, $stateParams, entity, Business_entity, Person, Address, Contract) {
        $scope.business_entity = entity;
        $scope.load = function (id) {
            Business_entity.get({id: id}, function(result) {
                $scope.business_entity = result;
            });
        };
        $rootScope.$on('mycaseApp:business_entityUpdate', function(event, result) {
            $scope.business_entity = result;
        });
    });
