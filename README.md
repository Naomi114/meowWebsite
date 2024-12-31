# 待辦事項
- 網站名稱未決定
- 網站主色
[網站主色]: https://coolors.co/palette/d0ccd0-c6bc77-fffd77-feba07
- SQL 資料庫初始化資料連結
- 假資料製作

# 開發工具及版本設定

- Spring Tool Suite 4
  - Version: 4.27.0.RELEASE
  - 官網下載: https://spring.io/tools

- VS code
  - 版本: 1.93.1 (system setup)
  - 認可: 38c31bc77e0dd6ae88a4e9cc93428cc27a56ba40
  -日期: 2024-09-11T17:20:05.685Z
  - Electron: 30.4.0
  - ElectronBuildId: 10073054
  - Chromium: 124.0.6367.243
  - Node.js: 20.15.1
  - OS: Windows_NT x64 10.0.19045

- SSMS 資料庫管理工具
  - 編碼規則 encoding= UTF-8

- Azure 雲端資料庫

# 安裝插件 plugin
## Chrome
- Wappalyzer: 查看網頁使用的前後端技術
![image](https://www.notion.so/Vue-JS-1602af478a2d80ee8feffb5a4d6c251b?pvs=4#1652af478a2d80278fe7c8de73f5bdaf)

## VS code
- Vue official
- Vue 3 support - All in One：提示函式庫
- Path autocomplete
- Auto rename tag
- indent rainbow: 顏色標記階層 (白色視窗會比較明顯)

# 專案初始設定
## 建立 Spring Boot Maven 專案
1. 編碼規則 encoding= UTF-8
2. 使用 application.yml
3. 加入相關儲存庫依賴 pom.xml
   -  spring-boot-starter
   -  spring-boot-devtools
   -  spring-boot-starter-data-jpa
   -  spring-boot-starter-web
   -  spring-boot-tomcat
   -  mssql-jdbc

   ### 範例: ![image](https://github.com/user-attachments/assets/99c698ff-2f17-467f-a6d1-8dd25f1812c9)

## 建立 Vue 專案相關語法
1. 產生專案: `npm create vue@latest`
2. 初始專案: `npm install`
3. 安裝前端路由套件: `npm install vue-router@4`
4. 測試專案: `npm run dev`
5. 安裝bootstrap網站到覽列: `npm install bootstrap@5`

