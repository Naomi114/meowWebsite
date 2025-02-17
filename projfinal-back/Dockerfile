# 1. 使用 Maven 建構應用
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests


# 第二步：使用 Tomcat 運行 .war 檔案，並且掛載圖片目錄至vm docker volume
FROM tomcat:10.1.16-jdk17
WORKDIR /usr/local/tomcat/webapps/

# 複製 .war 到 Tomcat 的 webapps 目錄
# ROOT.war即部署在Tomcat webapps/目錄下，且Tomcat 會將 ROOT.war 當成根應用程式，不會加上額外的路徑。(其他檔名會需要多增加路徑)
COPY --from=build /app/target/*.war ROOT.war

# 讓 Tomcat 監聽 8080 端口
EXPOSE 8080

# 啟動 Tomcat
CMD ["catalina.sh", "run"]


# 使用 OpenJDK 來運行應用(如果輸出.jar)
# FROM openjdk:17
# WORKDIR /app
# COPY --from=build /app/target/*.jar app.jar   從第一階段拷貝 target/*.jar 到 /app/app.jar
# EXPOSE 8080
# CMD ["java", "-jar", "app.jar"]   運行 Spring Boot 應用 java -jar app.jar

# 第一階段（建構 Maven 專案）
# 使用 Maven 官方映像檔（maven:3.8.5-openjdk-17）
# 將 pom.xml 和 src/ 複製進容器
# 執行 mvn package 來打包 Spring Boot 應用程式
# 產生 JAR 檔案到 target/ 目錄
# 第二階段（運行 Spring Boot 應用）
