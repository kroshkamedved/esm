node {
  stage('SCM') {
    checkout scm
  }
  stage('Run Tests') {
          // Run tests using Maven (replace with your test command)
          sh 'mvn clean test'
       }
  stage('Build') {
             // Build the Spring Boot project using Maven (replace with your build command)
          sh 'mvn clean package'
      }
      post {
              success {
                  script {
                      def tomcatUrl = "http://192.168.88.17:8080/manager/text"
                      def username = "jenkins"
                      def password = "password"
                      def warFileName = "esm.war"

                      def deployUrl = "${tomcatUrl}/deploy?path=/context-path&war=file:${warFileName}"

                      sh "curl -v -u ${username}:${password} --upload-file ${warFileName} ${deployUrl}"
                  }
              }
      }

  stage('SonarQube Analysis') {
        def mvn = tool 'mvn';
    withSonarQubeEnv() {
      sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=esm"
    }
  }
}