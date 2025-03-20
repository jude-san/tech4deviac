package org.tech4dev

class DockerUtils implements Serializable {
    void cleanupDockerResources(String imageName, List<String> tags) {
        def docker = new Docker()
        tags.each { tag ->
            docker.image("${imageName}:${tag}").remove()
        }
    }

    String formatDockerImageName(String dockerRepo, String serviceName) {
        return "${dockerRepo}/${serviceName}"
    }

}
