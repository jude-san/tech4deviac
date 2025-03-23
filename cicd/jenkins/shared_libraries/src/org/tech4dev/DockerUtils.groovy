package org.tech4dev

class DockerUtils implements Serializable {
    void cleanupDockerResources(String imageName, List<String> tags) {
        tags.each { tag ->
            sh "docker rmi ${imageName}:${tag} || true"
        }
    }

    String formatDockerImageName(String baseName, String component, String version) {
        return "${baseName}/${component}:${version}"
    }

}
