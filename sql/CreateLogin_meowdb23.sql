/* 1. 建立連線 */
--DROP LOGIN admin;
CREATE LOGIN admin
WITH PASSWORD = 'a123',
    CHECK_POLICY = ON,
    DEFAULT_DATABASE = master; -- 暫時設為 master 避免登入問題

/* 2. 移除 VIEW ANY DATABASE 權限 */
USE master;
REVOKE VIEW ANY DATABASE FROM admin;

/* 3. 在 meowdb 中授權 admin */
USE meowdb;
GRANT CONNECT TO admin; -- 授權連接
GRANT VIEW DEFINITION TO admin; -- 授權查看結構
ALTER AUTHORIZATION ON DATABASE::meowdb TO admin;  --轉移權限

SELECT name, suser_sname(owner_sid) AS owner
FROM sys.databases
WHERE name = 'meowdb';

/* 4. 測試成功後，重新設置預設資料庫 */
ALTER LOGIN admin WITH DEFAULT_DATABASE = meowdb;
