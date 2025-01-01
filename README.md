# UI 美編風格
- 網站名稱(未決定)
- 字型統一????
- 網站主色 
<img width="800" alt="100" src="https://github.com/user-attachments/assets/4511bc74-5cb9-4810-8ac6-31eb200037fb" />

| 分類         | 範例參考                          |
| ------------------------------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------ |
| 主視覺       | <img width="800" alt="image" src="https://github.com/user-attachments/assets/f400c4d8-f286-4b15-b60e-d6907786a385" /> |
| 圖表設計     | <img width="800" alt="image" src="https://github.com/user-attachments/assets/8fe0876e-5610-42e2-8688-91282e1003d7" />     |
| icon 符號    | <img width="869" alt="image" src="https://github.com/user-attachments/assets/77ea0699-19dc-4cc9-ad15-1915a10e1fb2" />   |
|

# 開發工具及版本設定

- Spring Tool Suite 4
  - Version: 4.27.0.RELEASE
  - 官網下載: https://spring.io/tools

- VS code
  - 版本: 1.93.1 (system setup)
  - 認可: 38c31bc77e0dd6ae88a4e9cc93428cc27a56ba40
  - 日期: 2024-09-11T17:20:05.685Z
  - Electron: 30.4.0
  - ElectronBuildId: 10073054
  - Chromium: 124.0.6367.243
  - Node.js: 18.20.5
  - OS: Windows_NT x64 10.0.19045

- SSMS 資料庫管理工具
  - 版本: 20.2.30.0
  - 編碼規則 encoding= UTF-8
  - database 名稱: meow(??)
  - 登入: ?????
  - 密碼: ?????

- Azure 雲端資料庫

# 安裝插件 plugin
- Chrome
  - Wappalyzer: 查看網頁使用的前後端技術

- VS code
  - Vue official
  - Vue 3 support - All in One：提示函式庫
  - Path autocomplete
  - Auto rename tag
  - indent rainbow: 顏色標記階層 (白色視窗會比較明顯)

# 專案初始設定
## 建立 Spring Boot Maven 專案
1. 編碼規則 encoding= UTF-8
2. 使用 application.yml
3. 加入相關儲存庫依賴 pom.xml (將範例檔丟上來!
   -  spring-boot-starter
   -  spring-boot-devtools
   -  spring-boot-starter-data-jpa
   -  spring-boot-starter-web
   -  spring-boot-tomcat
   -  mssql-jdbc
   -  nimbus-jose-jwt (V 9.48)

   ### 範例: ![image](https://github.com/user-attachments/assets/99c698ff-2f17-467f-a6d1-8dd25f1812c9)

## 建立 Vue 專案相關語法
1. 產生專案: `npm create vue@latest`
2. 初始專案: `npm install`
3. 安裝前端路由套件: `npm install vue-router@4`
4. 安裝bootstrap網站到覽列: `npm install bootstrap@5`
5. 測試專案: `npm run dev`


## 建立連線
- 不能設定localhost 8080, 如何連線到專題報告的教室電腦?? (restful 會教 by 馬老師 20241231)
- 使用https開頭，建立安全連線: 申請 SSL 憑證 (ref. Azure 課程) 
