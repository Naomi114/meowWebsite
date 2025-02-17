pipeline {
    agent any  // 讓 Jenkins 在任意可用節點上執行

    environment {
        FRONTEND_IMAGE = 'leekuanju/teamfrontend:latest'
        BACKEND_IMAGE = 'leekuanju/teambackend:latest'
        MSSQL_IMAGE = "mcr.microsoft.com/mssql/server:2022-latest"
        REDIS_IMAGE = "redis:latest"
        DOCKER_CREDENTIALS_ID = 'petfinder'    //已於在jenkins中設定可以登入dokcer-hub的帳密和使用id
        AZURE_VM = 'KuanJu@20.2.146.70'    //username@vm公開ip
        IMAGE_VOLUME = "petfinder_images"  // VM 中 Docker Volume 名稱
    }

    stages {
        stage('拉取程式碼') {
            steps {
                script {
                    // Clone 後端專案
                    dir('backend') {
                        checkout([
                            $class: 'GitSCM',
                            branches: [[name: '*/main']],
                            userRemoteConfigs: [[
                                url: 'https://github.com/KuanJuLee/finalproj_backend_BACKUP.git',
                                credentialsId: 'e4813fab-9926-4981-8396-c634f8c15fdd'
                            ]],
                            extensions: [[$class: 'CloneOption',shallow: true, depth: 1]]
                        ])
                    }

                    // Clone 前端專案
                    dir('frontend') {
                        checkout([
                            $class: 'GitSCM',
                            branches: [[name: '*/main']],
                            userRemoteConfigs: [[
                                url: 'https://github.com/KuanJuLee/finalproj_frontend_BACKUP.git',
                                credentialsId: 'e4813fab-9926-4981-8396-c634f8c15fdd'  
                                 // 你的前端 GitHub 憑證 ID(和後端同屬於某帳戶)
                            ]],
                            extensions: [[$class: 'CloneOption', shallow: true, depth: 1]]
                        ])
                    }
                }
                sh "ls -lah" // 確保代碼拉取成功
            }
        }

        stage('清理舊的 Docker Image') {
            steps {
                sh "docker image prune -f"
                sh "docker rmi -f \$(docker images -q leekuanju/frontend | tail -n +2) || true"
                sh "docker rmi -f \$(docker images -q leekuanju/backend | tail -n +2) || true"
            }
        }

        stage('建構前端 Docker 映像檔') {
            steps {
                 script {
                    // 下載 `defalut.conf` 到 `frontend/vue-project/`
                    sh "cp backend/default.conf frontend/vue-project/default.conf"
                }
                sh "docker build -t $FRONTEND_IMAGE ./frontend/vue-project"   
                // 從jenkins容器中讀取dockerfile，並啟動一個暫時的 Build 容器（這個容器會在 docker build 過程中運行）
            }
        }

        stage('建構後端 Docker 映像檔') {
            steps {
                sh "docker build -t $BACKEND_IMAGE ./backend/projfinal-back"
            }
        }

      
         stage('登入並推送至 Docker Hub') {
            steps { 
                withDockerRegistry([credentialsId: 'petfinder', url: '']) {
                    sh "docker push $FRONTEND_IMAGE"
                    sh "docker push $BACKEND_IMAGE"
                }
            }
        }

        stage('上傳假資料圖片到 Azure VM') {
            steps {
                script {
                    withCredentials([sshUserPrivateKey(credentialsId: 'petFinder', keyFileVariable: 'SSH_KEY')]) {
                        sh """
                        # 上傳本機圖片到 Azure VM 的暫存目錄（只傳輸變更的檔案）
                        # 要在本機 Jenkins 容器內先安裝 rsync
                        # -a：保持檔案權限 -v：顯示傳輸進度 -z：壓縮資料，加速上傳 --ignore-existing：只傳輸新檔案不覆蓋舊檔案
                        rsync -avz -e "ssh -i /var/jenkins_home/.ssh/jenkins_azure_key -o StrictHostKeyChecking=no" --ignore-existing /var/jenkins_home/upload/images/ $AZURE_VM:/tmp/images/
                        
                        # SSH 進入 Azure VM，確保只移動新圖片，避免重複上傳
                        # -r：遞歸複製（確保目錄結構不變）-n：不覆蓋已存在的檔案（確保不會重複塞入相同圖片）
                        ssh -i /var/jenkins_home/.ssh/jenkins_azure_key -o StrictHostKeyChecking=no $AZURE_VM <<EOF
                            sudo mkdir -p /var/lib/docker/volumes/$IMAGE_VOLUME/_data/final/pet/images
                            sudo cp -rn /tmp/images/* /var/lib/docker/volumes/$IMAGE_VOLUME/_data/final/pet/images/
                            sudo rm -rf /tmp/images
EOF"""
                    }
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
                            docker pull $FRONTEND_IMAGE
                            docker pull $BACKEND_IMAGE
                            docker pull $MSSQL_IMAGE
                            docker pull $REDIS_IMAGE

                             #  停止並刪除舊容器
                            docker stop frontend || true
                            docker stop backend || true
                            docker stop mssql || true
                            docker stop redis || true
                            docker rm frontend || true
                            docker rm backend || true
                            docker rm mssql || true
                            docker rm redis || true

                             # 創建放假資料圖的 Docker Volume（如果尚未建立）
                            docker volume create $IMAGE_VOLUME || true

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

                             #  啟動前端 (Nginx)
                            docker run -d -p 80:80 -p 443:443 --name frontend --network petfinder_network --restart always -v /etc/letsencrypt/live/petfinder.duckdns.org/fullchain.pem:/etc/nginx/certs/fullchain.pem -v /etc/letsencrypt/live/petfinder.duckdns.org/privkey.pem:/etc/nginx/certs/privkey.pem $FRONTEND_IMAGE

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
