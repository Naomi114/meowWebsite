/* 1 �T�{���n�J�v���]�t���Ǹ�Ʈw*/
SELECT name AS DatabaseName, suser_sname(owner_sid) AS Owner
FROM sys.databases
WHERE suser_sname(owner_sid) = 'admin';
GO

/* 2 �ಾ��Ʈw�v���� sa*/
ALTER AUTHORIZATION ON DATABASE::[moewdb] TO sa;

/* 3 �R���n�J*/
DROP LOGIN admin;
