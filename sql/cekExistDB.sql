/* �T�{��Ʈw�O�_�s�b */
SELECT name 
FROM sys.databases
WHERE name = 'meowdb';

/* �T�{�޿��ɮ׬O�_���������b */
SELECT name AS LogicalName, physical_name AS PhysicalName
FROM sys.master_files
WHERE name = 'Meowdb';

--DROP DATABASE meowdb