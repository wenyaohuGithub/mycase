'use strict';

angular.module('mycaseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('secondEntity', {
                parent: 'entity',
                url: '/secondEntitys',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mycaseApp.secondEntity.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/secondEntity/secondEntitys.html',
                        controller: 'SecondEntityController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('secondEntity');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('secondEntity.detail', {
                parent: 'entity',
                url: '/secondEntity/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mycaseApp.secondEntity.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/secondEntity/secondEntity-detail.html',
                        controller: 'SecondEntityDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('secondEntity');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'SecondEntity', function($stateParams, SecondEntity) {
                        return SecondEntity.get({id : $stateParams.id});
                    }]
                }
            })
            .state('secondEntity.new', {
                parent: 'secondEntity',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/secondEntity/secondEntity-dialog.html',
                        controller: 'SecondEntityDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, description: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('secondEntity', null, { reload: true });
                    }, function() {
                        $state.go('secondEntity');
                    })
                }]
            })
            .state('secondEntity.edit', {
                parent: 'secondEntity',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/secondEntity/secondEntity-dialog.html',
                        controller: 'SecondEntityDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['SecondEntity', function(SecondEntity) {
                                return SecondEntity.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('secondEntity', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
