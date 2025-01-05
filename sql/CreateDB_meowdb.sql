/*建立DB及交易紀錄檔*/
CREATE DATABASE moewdb
-- DROP DATABASE moewdb

ON PRIMARY(
NAME = Moewdb,  --資料庫邏輯名稱
FILENAME = 'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\DATA\Moewdb.mdf', 
SIZE = 100MB,
MAXSIZE = 500MB,
FILEGROWTH = 10MB )

LOG ON(
NAME = Moewdb,  --交易紀錄檔邏輯名稱
FILENAME = 'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\DATA\Moewdb.ldf', 
SIZE = 50MB,
MAXSIZE = 500MB,
FILEGROWTH = 10MB )

COLLATE Chinese_Taiwan_Stroke_CI_AS  --定序

/*查詢DB的內容、屬性*/
-- sp_helpdb moewdb