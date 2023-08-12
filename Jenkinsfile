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
  stage('SonarQube Analysis') {
        def mvn = tool 'mvn';
    withSonarQubeEnv() {
      sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=esm"
    }
  }
}