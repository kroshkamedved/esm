node {
    def tomcatUrl = "http://192.168.88.17:8080/manager/text"
    def username = "jenkins"
    def password = "password"
    def warFileName = "esm.war"
    def currentDir = sh(script: 'pwd', returnStdout: true).trim();
    def warFilePath = "${currentDir}/target/${warFileName}"
    def deployUrl = "${tomcatUrl}/deploy?path=/esm"

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
                    sh "echo ${deployUrl}"
                    sh "echo ${warFilePath}"
                  //sh 'curl -X PUT -u jenkins:password --data-binary @/var/jenkins_home/workspace/esm/target/esm.war "http://192.168.88.17:8080/manager/text/deploy?path=/esm"'
                 sh "curl -X PUT -u jenkins:password --data-binary @${warFilePath} ${deployUrl}"
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