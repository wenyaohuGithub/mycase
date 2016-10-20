'use strict';

angular.module('mycaseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('logout', {
                parent: 'account',
                url: '/logout',
                data: {
                    roles: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/account/login/loing.html',
                        controller: 'LogoutController'
                    }
                }
            });
    });
