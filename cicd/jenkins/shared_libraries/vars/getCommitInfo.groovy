def call() {
    def commitHash = sh(script: 'git rev-parse HEAD', returnStdout: true).trim()
    def commitMessage = sh(script: 'git log -1 --pretty=%B', returnStdout: true).trim()
    def commitAuthor = sh(script: 'git log -1 --pretty=%an', returnStdout: true).trim()
    def commitDate = sh(script: 'git log -1 --pretty=%cd', returnStdout: true).trim()
    return [commitHash: commitHash, commitMessage: commitMessage, commitAuthor: commitAuthor, commitDate: commitDate]
}
