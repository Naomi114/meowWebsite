pipeline {
    agent any  // 讓 Jenkins 在任意可用節點上執行

    environment {
        BACKEND_IMAGE = 'leekuanju/meowbackend:latest'
        MSSQL_IMAGE = "mcr.microsoft.com/mssql/server:2022-latest"
        REDIS_IMAGE = "redis:latest"
        DOCKER_CREDENTIALS_ID = 'petfinder'    //已於在jenkins中設定可以登入dokcer-hub的帳密和使用id
        AZURE_VM = 'KuanJu@20.2.146.70'    //username@vm公開ip
    }

    stages {
        stage('拉取程式碼') {
            steps {
                script {
                    // Clone 後端專案
                    dir('backend') {
                        checkout([
                            $class: 'GitSCM',
                            branches: [[name: '*/dev']],
                            userRemoteConfigs: [[
                                url: 'https://github.com/Naomi114/meowWebsite.git',
                                credentialsId: 'MeowBackend'
                            ]],
                            extensions: [[$class: 'CloneOption',shallow: true, depth: 1]]
                        ])
                    }
                }
                sh "ls -lah" // 確保代碼拉取成功
            }
        }


        stage('建構後端 Docker 映像檔') {
            steps {
                sh "docker build -t $BACKEND_IMAGE ./Meowbackend/projfinal-back"
            }
        }

      
         stage('登入並推送至 Docker Hub') {
            steps { 
                withDockerRegistry([credentialsId: 'petfinder', url: '']) {
                    sh "docker push $BACKEND_IMAGE"
                }
            }
        }
        
        stage('部署到 Azure VM') { 
            steps {
                script {
                    withCredentials([sshUserPrivateKey(credentialsId: 'petFinder', keyFileVariable: 'SSH_KEY')]) {
                        sh """
                        ssh -i /var/jenkins_home/.ssh/jenkins_azure_key -o StrictHostKeyChecking=no $AZURE_VM <<EOF

                            # 創建 Docker 網路 (確保 MSSQL、Redis、後端在同一個網路，可互相連通，其他不在 petfinder_network 內的容器無法存取 MSSQL & Redis)
                            docker network create petfinder_network || true
                            
                            # 拉取最新的 Docker 映像檔
                            docker pull $BACKEND_IMAGE
                            docker pull $MSSQL_IMAGE
                            docker pull $REDIS_IMAGE

                             #  停止並刪除舊容器
                            docker stop backend || true
                            docker stop mssql || true
                            docker stop redis || true
                            docker rm backend || true
                            docker rm mssql || true
                            docker rm redis || true


                             #  啟動 MSSQL 資料庫 (掛載 volume 以保存資料庫內資料) (restart always可以讓VM每次重新啟動，此container也重啟)
                            docker run -d --name mssql \\
                                --network petfinder_network \\
                                -e 'ACCEPT_EULA=Y' \\
                                -e 'SA_PASSWORD=YourStrong!Passw0rd' \\
                                -p 1433:1433 \\
                                -v mssql_data:/var/opt/mssql \\
                                --restart always \\
                                \$MSSQL_IMAGE

                             #  等待 MSSQL 初始化，MSSQL 需要一點時間啟動，先等待 10 秒再啟動後端才能連線
                            sleep 10


                             #  啟動 Redis (掛載 volume)
                            docker run -d --name redis \\
                                --network petfinder_network \\
                                -p 6379:6379 \\
                                -v redis_data:/data \\
                                --restart always \\
                                \$REDIS_IMAGE


                             #  啟動後端 (Tomcat，連結到 MSSQL & Redis) link讓後端可以透過 mssql 和 redis 這兩個名稱存取資料庫，並掛載圖片 Volume
                            docker run -d -p 8080:8080 --name backend \\
                                --network petfinder_network \\
                                -e "SPRING_PROFILES_ACTIVE=production" \\
                                -v $IMAGE_VOLUME:/usr/local/tomcat/upload \\
                                --restart always \\
                                \$BACKEND_IMAGE
EOF"""
            }
        }
    }
}
    }
}
