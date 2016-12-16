'use strict';

angular.module('mycaseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('contract_party', {
                parent: 'entity',
                url: '/contract_partys',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mycaseApp.contract_party.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/contract_party/contract_partys.html',
                        controller: 'Contract_partyController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('contract_party');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('contract_party.detail', {
                parent: 'entity',
                url: '/contract_party/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mycaseApp.contract_party.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/contract_party/contract_party-detail.html',
                        controller: 'Contract_partyDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('contract_party');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Contract_party', function($stateParams, Contract_party) {
                        return Contract_party.get({id : $stateParams.id});
                    }]
                }
            })
            .state('contract_party.new', {
                parent: 'contract_party',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/contract_party/contract_party-dialog.html',
                        controller: 'Contract_partyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, description: null, registration_id: null, registered_capital: null, legal_representative: null, registration_inspection_record: null, professional_certificate: null, business_certificate: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('contract_party', null, { reload: true });
                    }, function() {
                        $state.go('contract_party');
                    })
                }]
            })
            .state('contract_party.edit', {
                parent: 'contract_party',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/contract_party/contract_party-dialog.html',
                        controller: 'Contract_partyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Contract_party', function(Contract_party) {
                                return Contract_party.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('contract_party', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
