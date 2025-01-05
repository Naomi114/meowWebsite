/* 1 確認此登入權限包含哪些資料庫*/
SELECT name AS DatabaseName, suser_sname(owner_sid) AS Owner
FROM sys.databases
WHERE suser_sname(owner_sid) = 'admin';
GO

/* 2 轉移資料庫權限給 sa*/
ALTER AUTHORIZATION ON DATABASE::[moewdb] TO sa;

/* 3 刪除登入*/
DROP LOGIN admin;
