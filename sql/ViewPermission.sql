/* 登入權限檢查 */
SELECT name, principal_id, type_desc
FROM sys.server_principals
WHERE name = 'admin';

/* 資料庫狀態檢查 */
SELECT name, state_desc
FROM sys.databases
WHERE name = 'meowdb';

/* SQL Server 預設為所有登入帳號授予 VIEW ANY DATABASE 權限，
使用者可以看到伺服器上所有資料庫的名稱（即使無法進入那些資料庫）。*/
SELECT * 
FROM sys.server_permissions
WHERE permission_name = 'VIEW ANY DATABASE';

/* 登入資料庫的使用權限 */
USE meowdb;
SELECT dp.name AS UserName, dp.type_desc AS UserType, pe.permission_name, pe.state_desc
FROM sys.database_principals dp
LEFT JOIN sys.database_permissions pe
ON dp.principal_id = pe.grantee_principal_id
WHERE dp.name = 'admin';

