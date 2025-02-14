CREATE DATABASE meowdb
-- DROP DATABASE meowdb

ON PRIMARY(
    NAME = Meowdb_Data,
    FILENAME = '/var/opt/mssql/data/Meowdb.mdf',  -- 修改為 Docker MSSQL 的路徑
    SIZE = 100MB,
    MAXSIZE = 500MB,
    FILEGROWTH = 10MB
)

LOG ON(
    NAME = Meowdb_Log,
    FILENAME = '/var/opt/mssql/data/Meowdb.ldf',  -- 修改為 Docker MSSQL 的路徑
    SIZE = 50MB,
    MAXSIZE = 500MB,
    FILEGROWTH = 10MB
)

COLLATE Chinese_Taiwan_Stroke_CI_AS;
