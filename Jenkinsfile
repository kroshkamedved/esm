node {
  stage('SCM') {
    checkout scm
  }
  stage('SonarQube Analysis') {
    def mvn = tool 'mvn';
    withSonarQubeEnv() {
      sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=esm"
    }
  }
  stage('Build') {
    steps {
      sh "${mvn} -B -DskipTests clean package"
    }
  }
}