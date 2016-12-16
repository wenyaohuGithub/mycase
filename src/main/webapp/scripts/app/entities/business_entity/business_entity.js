'use strict';

angular.module('mycaseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('business_entity', {
                parent: 'entity',
                url: '/business_entitys',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mycaseApp.business_entity.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/business_entity/business_entitys.html',
                        controller: 'Business_entityController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('business_entity');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('business_entity.detail', {
                parent: 'entity',
                url: '/business_entity/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mycaseApp.business_entity.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/business_entity/business_entity-detail.html',
                        controller: 'Business_entityDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('business_entity');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Business_entity', function($stateParams, Business_entity) {
                        return Business_entity.get({id : $stateParams.id});
                    }]
                }
            })
            .state('business_entity.new', {
                parent: 'business_entity',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/business_entity/business_entity-dialog.html',
                        controller: 'Business_entityDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, description: null, registration_id: null, registered_capital: null, registration_inspection_record: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('business_entity', null, { reload: true });
                    }, function() {
                        $state.go('business_entity');
                    })
                }]
            })
            .state('business_entity.edit', {
                parent: 'business_entity',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/business_entity/business_entity-dialog.html',
                        controller: 'Business_entityDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Business_entity', function(Business_entity) {
                                return Business_entity.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('business_entity', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
