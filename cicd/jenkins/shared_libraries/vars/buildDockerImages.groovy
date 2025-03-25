def call(Map config = [:]) {
    def directory = config.directory ?: '.'
    def imageName = config.imageName ?: error("Missing required parameter 'imageName'")
    def dockerfile = config.dockerfile ?: 'Dockerfile'
    def buildArgs = config.buildArgs ?: []
    def push = config.push ?: false
    def version = config.version ?: 'latest'
    def credentialsId = config.credentialsId ?: 'dockerhub-credentials'
    def additionalTags = config.additionalTags ?: []

    def buildCmd = "docker build -t ${imageName}:${version}"

    additionalTags.each { tag ->
        buildCmd += " -t ${imageName}:${tag}"
    }

    buildArgs.each { arg ->
        buildCmd += " --build-arg ${arg}"
    }

    buildCmd += " -f ${dockerfile} ${directory}"

    dir(directory) {
        sh buildCmd
    }

    if (push) {
        if (!credentialsId) {
            error "Missing required parameter 'credentialsId' for pushing Docker image"
        }

        withCredentials([usernamePassword(credentialsId: credentialsId, 
                                            usernameVariable: 'DOCKER_HUB_CREDS_USR', 
                                            passwordVariable: 'DOCKER_HUB_CREDS_PSW')]) {
            sh "echo ${DOCKER_HUB_CREDS_PSW} | docker login -u ${DOCKER_HUB_CREDS_USR} --password-stdin"
            sh "docker push ${imageName}:${version}"

            additionalTags.each { tag ->
                sh "docker push ${imageName}:${tag}"
            }

            echo "Image ${imageName}:${version} pushed to DockerHub!"

            sh "docker logout"
        }
    }
}
