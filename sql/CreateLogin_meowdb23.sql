/*用語法建立SQL Server Login*/
CREATE LOGIN admin

WITH PASSWORD = 'a123',
	CHECK_POLICY = ON , --啟用密碼原則檢查
	DEFAULT_DATABASE = [moewdb];

GO