/*�إ�DB�Υ��������*/
CREATE DATABASE moewdb
-- DROP DATABASE moewdb

ON PRIMARY(
NAME = Moewdb,  --��Ʈw�޿�W��
FILENAME = 'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\DATA\Moewdb.mdf', 
SIZE = 100MB,
MAXSIZE = 500MB,
FILEGROWTH = 10MB )

LOG ON(
NAME = Moewdb,  --����������޿�W��
FILENAME = 'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\DATA\Moewdb.ldf', 
SIZE = 50MB,
MAXSIZE = 500MB,
FILEGROWTH = 10MB )

COLLATE Chinese_Taiwan_Stroke_CI_AS  --�w��

/*�d��DB�����e�B�ݩ�*/
-- sp_helpdb moewdb