pipeline {
    agent any

//     environment {
//             PYTHON_HOME = 'C:/Users/GYYMM/AppData/Local/Programs/Python/Python312'
//             PATH = "${PYTHON_HOME}/bin:${env.PATH}"
//         }

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
    }

     post {
         always {
            bat '''for /d /r %%d in (*allure-report) do rd /s /q "%%d"'''

//             bat "allure generate target/allure-results --clean -o %date:~10,4%-%date:~7,2%-%date:~4,2%-%time:~1,1%-%time:~3,2%-%time:~6,2%-allure-report"
            bat "allure generate target/allure-results --clean -o allure-report"

            archiveArtifacts artifacts: 'logs/*.log', allowEmptyArchive: true
            archiveArtifacts artifacts: 'allure-report/**', allowEmptyArchive: true

            publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'allure-report', reportFiles: 'index.html', reportName: 'Allure Report', reportTitles: '', useWrapperFileDirectly: true])
            }
        }
   }