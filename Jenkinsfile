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
                bat '''mvn clean test -Dtest="advanced.ApiTester"'''
                bat '''mvn test -Dtest="advanced.gherkin.GherkinTest"'''
            }
        }
    }

     post {
         always {
            bat '''for /d /r %%d in (*allure-report) do rd /s /q "%%d"'''

            bat "allure generate target/allure-results --clean -o allure-report"

            archiveArtifacts artifacts: 'logs/*.log', allowEmptyArchive: true
            archiveArtifacts artifacts: 'allure-report/**', allowEmptyArchive: true

            publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'allure-report', reportFiles: 'index.html', reportName: 'Allure Report (Cucumber included)', reportTitles: '', useWrapperFileDirectly: true])
            }
        }
   }