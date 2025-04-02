@Library('shared_libraries@assignment') _


def components = [
    [
        name: 'API',
        language: 'go',
        path: 'cicd/banking-app/backend-api',
        imageName: 'banking-api',
        lint: true,
        test: true
    ],
    [
        name: 'Transaction Service',
        language: 'python',
        path: 'cicd/banking-app/transaction-service',
        imageName: 'banking-processor',
        lint: true,
        test: true
    ],
    [
        name: 'Frontend',
        language: 'javascript',
        path: 'cicd/banking-app/frontend',
        imageName: 'banking-frontend',
        lint: true,
        test: false
    ]
]

dockerPipeline(
    dockerHubCredentials: 'dockerhub-credentials',
    dockerHubRepo: 'david930',
    components: components,
    notifyOnSuccess: false,
    notifyOnFailure: true,
    notifyRecipients: ['menion@devlabs.com']
)
