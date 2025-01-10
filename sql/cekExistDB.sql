/* 確認資料庫是否存在 */
SELECT name 
FROM sys.databases
WHERE name = 'meowdb';

/* 確認邏輯檔案是否未卸除乾淨 */
SELECT name AS LogicalName, physical_name AS PhysicalName
FROM sys.master_files
WHERE name = 'Meowdb';

--DROP DATABASE meowdb