/*�λy�k�إ�SQL Server Login*/
CREATE LOGIN admin

WITH PASSWORD = 'a123',
	CHECK_POLICY = ON , --�ҥαK�X��h�ˬd
	DEFAULT_DATABASE = [moewdb];

GO