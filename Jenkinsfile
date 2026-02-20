pipeline {
    agent any

    environment {
        // Docker Hub credentials
        DOCKER_HUB_REPO = 'sveggalam/app'
        DOCKER_REGISTRY = 'docker.io'
        GIT_REPO = 'https://github.com/sveggalam/App.git'
        GIT_BRANCH = 'main'
        MAVEN_HOME = '/usr/share/maven'
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64'
        PATH = "${JAVA_HOME}/bin:${MAVEN_HOME}/bin:${PATH}"
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timestamps()
        timeout(time: 1, unit: 'HOURS')
    }

    stages {
        stage('Fetch Code from GitHub') {
            steps {
                script {
                    echo '========== STAGE 1: Fetching Code from GitHub =========='
                    deleteDir() // Clean workspace
                }
                checkout(
                    [
                        $class: 'GitSCM',
                        branches: [[name: '*/main']],
                        userRemoteConfigs: [[
                            url: 'https://github.com/sveggalam/App.git',
                            credentialsId: 'github-creds' // Configure in Jenkins
                        ]]
                    ]
                )
                script {
                    echo "Code fetched successfully from GitHub"
                    echo "Git Commit: ${GIT_COMMIT}"
                    echo "Git Branch: ${GIT_BRANCH}"
                }
            }
        }

        stage('Build All Microservices') {
            parallel {
                stage('Build Auth Service') {
                    steps {
                        script {
                            echo '========== Building Auth Service =========='
                            dir('auth-service') {
                                sh '''
                                    /usr/share/maven/bin/mvn clean package -DskipTests
                                    echo "Auth Service built successfully"
                                '''
                            }
                        }
                    }
                }
                stage('Build Student Service') {
                    steps {
                        script {
                            echo '========== Building Student Service =========='
                            dir('studentMicroservice') {
                                sh '''
                                    /usr/share/maven/bin/mvn clean package -DskipTests
                                    echo "✓ Student Service built successfully"
                                '''
                            }
                        }
                    }
                }
                stage('Build Library Service') {
                    steps {
                        script {
                            echo '========== Building Library Service =========='
                            dir('libraryMicroservice') {
                                sh '''
                                    /usr/share/maven/bin/mvn clean package -DskipTests
                                    echo "✓ Library Service built successfully"
                                '''
                            }
                        }
                    }
                }
                stage('Build Mess Service') {
                    steps {
                        script {
                            echo '========== Building Mess Service =========='
                            dir('messMicroservice') {
                                sh '''
                                    /usr/share/maven/bin/mvn clean package -DskipTests
                                    echo "✓ Mess Service built successfully"
                                '''
                            }
                        }
                    }
                }
                stage('Build API Gateway') {
                    steps {
                        script {
                            echo '========== Building API Gateway =========='
                            dir('apiGateway') {
                                sh '''
                                    /usr/share/maven/bin/mvn clean package -DskipTests
                                    echo "✓ API Gateway built successfully"
                                '''
                            }
                        }
                    }
                }
            }
        }

        stage('Create Docker Images') {
            steps {
                script {
                    echo '========== STAGE 3: Creating Docker Images =========='
                    
                    def services = ['auth-service', 'studentMicroservice', 'libraryMicroservice', 'messMicroservice', 'apiGateway']
                    def imageTag = "${BUILD_NUMBER}-${GIT_COMMIT.take(7)}"
                    
                    services.each { service ->
                        dir(service) {
                            sh '''
                                echo "Building Docker image for: ''' + service + '''"
                                docker build -t ${DOCKER_HUB_REPO}/${service}:${imageTag} \
                                           -t ${DOCKER_HUB_REPO}/${service}:latest .
                                echo " Docker image created: ${DOCKER_HUB_REPO}/${service}:${imageTag}"
                            '''
                        }
                    }
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    echo '========== STAGE 4: Pushing Images to Docker Hub =========='
                    
                    withCredentials([usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )]) {
                        sh '''
                            echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                            
                            def services = ['auth-service', 'studentMicroservice', 'libraryMicroservice', 'messMicroservice', 'apiGateway']
                            def imageTag = "${BUILD_NUMBER}-${GIT_COMMIT.take(7)}"
                            
                            services.each { service ->
                                docker push ${DOCKER_HUB_REPO}/${service}:${imageTag}
                                docker push ${DOCKER_HUB_REPO}/${service}:latest
                                echo " Pushed ${DOCKER_HUB_REPO}/${service}"
                            }
                            
                            docker logout
                        '''
                    }
                }
            }
        }

        stage('Update Kubernetes Manifests') {
            when {
                branch 'main'
            }
            steps {
                script {
                    echo '========== Updating Kubernetes Manifests =========='
                    
                    def imageTag = "${BUILD_NUMBER}-${GIT_COMMIT.take(7)}"
                    
                    sh '''
                        # Update image tags in deployment manifests
                        sed -i 's|auth-service:.*|auth-service:''' + imageTag + '''|g' k8s/deployments/auth-deployment.yml
                        sed -i 's|studentMicroservice:.*|studentMicroservice:''' + imageTag + '''|g' k8s/deployments/student-deployment.yml
                        sed -i 's|libraryMicroservice:.*|libraryMicroservice:''' + imageTag + '''|g' k8s/deployments/library-deployment.yml
                        sed -i 's|messMicroservice:.*|messMicroservice:''' + imageTag + '''|g' k8s/deployments/mess-deployment.yml
                        sed -i 's|apiGateway:.*|apiGateway:''' + imageTag + '''|g' k8s/deployments/gateway-deployment.yml
                    '''
                }
            }
        }

        // stage('Deploy to Kubernetes (Manual Trigger)') {
        //     when {
        //         branch 'main'
        //     }
        //     steps {
        //         script {
        //             echo '========== Ready for Kubernetes Deployment =========='
        //             echo "Run the following to deploy via kubectl or ArgoCD:"
        //             echo "kubectl apply -f k8s/"
        //             echo "Or push changes to git for ArgoCD auto-sync"
        //         }
        //     }
        // }
    }

    post {
        always {
            script {
                echo '========== Pipeline Completed =========='
                // Archive test results
                junit(
                    testResults: '**/target/surefire-reports/*.xml',
                    allowEmptyResults: true
                )
            }
        }
        success {
            script {
                echo "✓ Pipeline completed successfully"
                // Send success notification
                echo "Images ready for deployment:"
                sh '''
                    echo "- ${DOCKER_HUB_REPO}/auth-service:${BUILD_NUMBER}"
                    echo "- ${DOCKER_HUB_REPO}/studentMicroservice:${BUILD_NUMBER}"
                    echo "- ${DOCKER_HUB_REPO}/libraryMicroservice:${BUILD_NUMBER}"
                    echo "- ${DOCKER_HUB_REPO}/messMicroservice:${BUILD_NUMBER}"
                    echo "- ${DOCKER_HUB_REPO}/apiGateway:${BUILD_NUMBER}"
                '''
            }
        }
        failure {
            script {
                echo "✗ Pipeline failed"
                // Send failure notification
            }
        }
    }
}
