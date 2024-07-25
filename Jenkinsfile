pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                script {
                    echo "Checking out code from repository..."
                    echo "Branch: main"
                    echo "Repository URL: https://github.com/yusufyilmvz/RestAssured-API-test-automation.git"

                    git credentialsId: 'Q', url: 'https://github.com/yusufyilmvz/RestAssured-API-test-automation.git', branch: 'main'
                }
            }
        }

        stage('Build and Test') {
            steps {
                bat '''mvn clean test -Dtest="advanced.Deneme"'''
            }
        }

        stage('Generate Allure Report') {
            steps {
                bat "allure generate target/allure-results --clean -o target/allure-report"
            }
        }
    }

     post {
         success {
            archiveArtifacts artifacts: 'logs/*.log', allowEmptyArchive: true
            archiveArtifacts artifacts: 'target/allure-report/**', allowEmptyArchive: true
        }
   }
}
