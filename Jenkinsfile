pipeline {
  agent {
    node {
      label 'rhel6'
    }
    
  }
  stages {
    stage('Prepare') {
      steps {
        git(url: 'https://github.com/galeb/galeb-router.git', branch: 'develop')
      }
    }
    stage('Build') {
      steps {
        sh 'mvn clean install -DskipTests'
      }
    }
    stage('Tests') {
      steps {
        sh 'mvn test'
      }
    }
    stage('Jacoco') {
      steps {
        sh 'mvn jacoco:prepare-agent jacoco:prepare-agent-integration'
      }
    }
    stage('Sonar') {
      steps {
        sh 'mvn sonar:sonar'
      }
    }
  }
  environment {
    JAVA_HOME = '/opt/java18/'
  }
}
