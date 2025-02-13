/* �n�J�v���ˬd */
SELECT name, principal_id, type_desc
FROM sys.server_principals
WHERE name = 'admin';

/* ��Ʈw���A�ˬd */
SELECT name, state_desc
FROM sys.databases
WHERE name = 'meowdb';

/* SQL Server �w�]���Ҧ��n�J�b���¤� VIEW ANY DATABASE �v���A
�ϥΪ̥i�H�ݨ���A���W�Ҧ���Ʈw���W�١]�Y�ϵL�k�i�J���Ǹ�Ʈw�^�C*/
SELECT * 
FROM sys.server_permissions
WHERE permission_name = 'VIEW ANY DATABASE';

/* �n�J��Ʈw���ϥ��v�� */
USE meowdb;
SELECT dp.name AS UserName, dp.type_desc AS UserType, pe.permission_name, pe.state_desc
FROM sys.database_principals dp
LEFT JOIN sys.database_permissions pe
ON dp.principal_id = pe.grantee_principal_id
WHERE dp.name = 'admin';

