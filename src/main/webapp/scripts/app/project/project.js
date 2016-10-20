'use strict';

angular.module('mycaseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('project', {
                parent: 'site',
                url: '/',
                data: {
                    roles: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/project/project.html',
                        controller: 'ProjectController'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('project');
                        return $translate.refresh();
                    }]
                }
            });
    });
