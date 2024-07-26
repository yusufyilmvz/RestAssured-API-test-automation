pipeline {
    agent any

    environment {
            PYTHON_HOME = 'C:/Users/GYYMM/AppData/Local/Programs/Python/Python312'
            PATH = "${PYTHON_HOME}/bin:${env.PATH}"
        }

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
                bat "allure generate target/allure-results --clean -o allure-report"
            }
        }
    }

     post {
         success {
            archiveArtifacts artifacts: 'logs/*.log', allowEmptyArchive: true
            archiveArtifacts artifacts: 'allure-report/**', allowEmptyArchive: true
            cd allure-report
            echo "Http server to represent test reports - default port: 8080"
            echo "After analyze report, you can cancel the build operation"
            bat "python -m http.server 8000"
        }
   }
}
