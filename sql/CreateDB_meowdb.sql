/*�إ�DB�Υ��������*/
CREATE DATABASE meowdb
-- DROP DATABASE meowdb

ON PRIMARY(
NAME = Meowdb_Data,  --��Ʈw�޿�W��
FILENAME = 'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\DATA\Meowdb.mdf', 
SIZE = 100MB,
MAXSIZE = 500MB,
FILEGROWTH = 10MB )

LOG ON(
NAME = Meowdb_Log,  --����������޿�W��
FILENAME = 'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\DATA\Meowdb.ldf', 
SIZE = 50MB,
MAXSIZE = 500MB,
FILEGROWTH = 10MB )

COLLATE Chinese_Taiwan_Stroke_CI_AS  --�w��

/*�d��DB�����e�B�ݩ�*/
-- sp_helpdb moewdb