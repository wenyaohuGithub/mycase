'use strict';

angular.module('mycaseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('system', {
                abstract: true,
                parent: 'site'
            });
    });
