/* 1 �إ߳s�u */
CREATE LOGIN admin
--DROP LOGIN admin;

WITH PASSWORD = 'a123',
	CHECK_POLICY = ON , --�ҥαK�X��h�ˬd
	DEFAULT_DATABASE = [meowdb];

GO

/* 2 ������������Ʈw�� view */
/* �]��SQL Server �w�]���Ҧ��n�J�b���¤� VIEW ANY DATABASE �v��*/

--������ master �����H�U
REVOKE VIEW ANY DATABASE FROM admin;
