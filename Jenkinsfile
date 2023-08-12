node {
    def tomcatUrl = "http://192.168.88.17:8080/manager/text"
    def username = "jenkins"
    def password = "password"
    def warFileName = "esm.war"
    def deployUrl = "${tomcatUrl}/deploy?path=/context-path&war=file:${warFileName}"

    try {
        stage('SCM') {
            checkout scm
        }

        stage('Run Tests') {
            sh 'mvn clean test'
        }

        stage('Build') {
            sh 'mvn clean package'
        }

        stage('Deploy to Tomcat') {
            sh "curl -v -u ${username}:${password} --upload-file ${warFileName} ${deployUrl}"
        }

        stage('SonarQube Analysis') {
            def mvn = tool 'mvn'
            withSonarQubeEnv() {
                sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=esm"
            }
        }
    } catch (Exception e) {
        currentBuild.result = 'FAILURE'
        error("Pipeline failed: ${e.getMessage()}")
    }
}