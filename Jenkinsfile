pipeline {
    agent any  // 讓 Jenkins 在任意可用節點上執行

    environment {
        BACKEND_IMAGE = 'leekuanju/meowbackend:latest'
        MSSQL_IMAGE = "mcr.microsoft.com/mssql/server:2022-latest"
        REDIS_IMAGE = "redis:latest"
        DOCKER_CREDENTIALS_ID = 'petfinder'    //已於在jenkins中設定可以登入dokcer-hub的帳密和使用id
        AZURE_VM = 'KuanJu@20.2.146.70'    //username@vm公開ip
        IMAGE_VOLUME = "petfinder_images"  // VM 中 Docker Volume 名稱，拿來放冠假資料圖片
        PRODUCT_IMAGE_VOLUME = "product_images"   // VM 中 Docker Volume 名稱，拿來放商城資料圖片
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


        stage('清理舊的 Docker Image') {
            steps {
                sh "docker image prune -f"
                sh '''
                IMAGES=$(docker images -q leekuanju/meowbackend)
                if [ -n "$IMAGES" ]; then
                    echo "刪除舊的 Docker 映像檔: $IMAGES"
                    echo "$IMAGES" | xargs -r docker rmi -f
                else
                    echo "沒有舊的映像檔需要刪除"
                fi
                '''
            }
        }


        stage('建構後端 Docker 映像檔') {
            steps {
                sh "docker build -t $BACKEND_IMAGE ./projfinal-back"
            }
        }

      
         stage('登入並推送至 Docker Hub') {
            steps { 
                withDockerRegistry([credentialsId: 'petfinder', url: '']) {
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
                            sudo cp -r /tmp/images/* /var/lib/docker/volumes/$IMAGE_VOLUME/_data/final/pet/images/
                            sudo rm -rf /tmp/images
EOF"""
                    }
                }
            }
        }


        stage('上傳產品假圖片到 Azure VM') {
            steps {
                script {
                    withCredentials([sshUserPrivateKey(credentialsId: 'petFinder', keyFileVariable: 'SSH_KEY')]) {
                        sh """
                        rsync -avz -e "ssh -i /var/jenkins_home/.ssh/jenkins_azure_key -o StrictHostKeyChecking=no" --ignore-existing /var/jenkins_home/meowWebsite/images/ $AZURE_VM:/tmp/product_images/
                        
                        ssh -i /var/jenkins_home/.ssh/jenkins_azure_key -o StrictHostKeyChecking=no $AZURE_VM <<EOF
                            sudo mkdir -p /var/lib/docker/volumes/$PRODUCT_IMAGE_VOLUME/_data/final/product/images
                            sudo cp -r /tmp/product_images/* /var/lib/docker/volumes/$PRODUCT_IMAGE_VOLUME/_data/final/product/images/
                            sudo rm -rf /tmp/product_images
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

                            # 創建放假資料圖的 Docker Volume（如果尚未建立）
                            docker volume create $IMAGE_VOLUME || true
                            docker volume create $PRODUCT_IMAGE_VOLUME || true

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
                                -v $PRODUCT_IMAGE_VOLUME:/usr/local/tomcat/meowWebsite/images/ \\
                                --restart always \\
                                \$BACKEND_IMAGE
EOF"""
            }
        }
    }
}
    }
}
