/* 1 建立連線 */
CREATE LOGIN admin
--DROP LOGIN admin;

WITH PASSWORD = 'a123',
	CHECK_POLICY = ON , --啟用密碼原則檢查
	DEFAULT_DATABASE = [meowdb];

GO

/* 2 移除不相關資料庫的 view */
/* 因為SQL Server 預設為所有登入帳號授予 VIEW ANY DATABASE 權限*/

--切換到 master 後執行以下
REVOKE VIEW ANY DATABASE FROM admin;
