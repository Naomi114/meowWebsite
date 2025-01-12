/* 1. �إ߳s�u */
--DROP LOGIN admin;
CREATE LOGIN admin
WITH PASSWORD = 'a123',
    CHECK_POLICY = ON,
    DEFAULT_DATABASE = master; -- �Ȯɳ]�� master �קK�n�J���D

/* 2. ���� VIEW ANY DATABASE �v�� */
USE master;
REVOKE VIEW ANY DATABASE FROM admin;

/* 3. �b meowdb �����v admin */
USE meowdb;
GRANT CONNECT TO admin; -- ���v�s��
GRANT VIEW DEFINITION TO admin; -- ���v�d�ݵ��c
ALTER AUTHORIZATION ON DATABASE::meowdb TO admin;  --�ಾ�v��

SELECT name, suser_sname(owner_sid) AS owner
FROM sys.databases
WHERE name = 'meowdb';

/* 4. ���զ��\��A���s�]�m�w�]��Ʈw */
ALTER LOGIN admin WITH DEFAULT_DATABASE = meowdb;
