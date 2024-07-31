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
                bat '''mvn clean test -Dtest="advanced.ApiTester"'''
            }
        }

//         stage('Generate Allure Report') {
//             steps {
//                 bat "allure generate target/allure-results --clean -o allure-report"
//             }
//         }
    }

     post {
         always {
            bat '''for /d /r %%d in (*allure-report) do rd /s /q "%%d"'''

            bat "allure generate target/allure-results --clean -o %date:~10,4%-%date:~7,2%-%date:~4,2%-%time:~1,1%-%time:~3,2%-%time:~6,2%-allure-report"

            archiveArtifacts artifacts: 'logs/*.log', allowEmptyArchive: true
            archiveArtifacts artifacts: '*-allure-report/**', allowEmptyArchive: true

//             dir('allure-report') {
            script {

                bat '''
                @echo off
                for /d /r %%d in (*allure-report) do (
                    cd /d "%%d"
                    echo Now in directory: %%d
                    python ../jenkins-http-server.py
                )
                '''
//                 bat 'python ../jenkins-http-server.py'
            }
//                 echo "Http server to represent test reports - default port: 8080"
//                 echo "After analyze report, you can cancel the build operation"
//                 bat "python -m http.server 8000"
//             }
        }
   }
}