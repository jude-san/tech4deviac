def call(Map config = [:]) {

    Map pipelineConfig = [
        repoUrl: config.repoUrl ?: '',
        branch: config.branch ?: 'main',
        dockerHubCredentials: config.dockerHubCredentials ?: 'docker-hub-credentials',
        dockerHubRepo: config.dockerHubRepo ?: 'david930',
        components: config.components ?: [],
        notifyOnSuccess: config.notifyOnSuccess ?: false,
        notifyOnFailure: config.notifyOnFailure ?: true,
        notifyRecipients: config.notifyRecipients ?: []
    ]

    def dockerUtils = new org.tech4dev.DockerUtils()
    def notificationUtils = new org.tech4dev.NotificationUtils()

    pipeline {
        agent any
        environment {
            DOCKER_HUB_CREDS = credentials("${pipelineConfig.dockerHubCredentials}")
            DOCKER_HUB_REPO = "${pipelineConfig.dockerHubRepo}"
            VERSION = "v1.0.${BUILD_NUMBER}"
        }
        stages {
            stage('Checkout') {
                steps {
                    checkout scm
                }
            }

            stage('Lint and Format Check') {
                steps {
                    script {
                        def lintStages = [:]
                        pipelineConfig.components.each { component ->
                            if (component.lint) {
                                lintStages["Lint ${component.name}"] = {
                                    runLinter(
                                        language: component.language,
                                        directory: component.path,
                                        failOnError: component.lintFailsBuilds ?: false
                                    )
                                }
                            }}
                        parallel lintStages
                    }
                }
            }

            stage('Test') {
                steps {
                    script {
                        def testStages = [:]
                        
                        pipelineConfig.components.each { component ->
                            if (component.test) {
                                testStages["Test ${component.name}"] = {
                                    runTests(
                                        language: component.language,
                                        directory: component.path
                                    )
                                }
                            }
                        }
                        
                        parallel testStages
                    }
                }
            }

            stage('Push Images to DockerHub') {
                steps {
                    script {
                        // Login to DockerHub
                        sh 'echo ${DOCKER_HUB_CREDS_PSW} | docker login -u ${DOCKER_HUB_CREDS_USR} --password-stdin'
                        
                        // Push all images
                        pipelineConfig.components.each { component ->
                            sh """
                            docker push ${DOCKER_HUB_REPO}/${component.imageName}:${VERSION}
                            docker push ${DOCKER_HUB_REPO}/${component.imageName}:latest
                            """
                        }
                        
                        echo 'Images pushed to DockerHub!'
                    }
                }
            }
        }

        post {
            always {
                script {
                    pipelineConfig.components.each { component ->
                        sh """
                        docker rmi ${DOCKER_HUB_REPO}/${component.imageName}:${VERSION} || true
                        docker rmi ${DOCKER_HUB_REPO}/${component.imageName}:latest || true
                        """
                }

                sh 'docker logout'
            }
            }

            success {
                script {
                    if (pipelineConfig.notifyOnSuccess) {
                        notificationUtils.sendSuccessNotification(
                            recipients: pipelineConfig.notifyRecipients,
                            buildUrl: env.BUILD_URL
                        )
                    }
                }
            }

            failure {
                script {
                    if (pipelineConfig.notifyOnFailure) {
                        notificationUtils.sendFailureNotification(
                            recipients: pipelineConfig.notifyRecipients,
                            buildUrl: env.BUILD_URL
                        )
                    }
                }
            }
        }
    }
}
