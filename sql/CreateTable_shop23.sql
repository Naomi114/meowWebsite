/*�إ߸�ƪ�*/
CREATE TABLE dbo.Categories2
( CategoryID2    int IDENTITY(1,1) NOT NULL,
  CategoryName nvarchar(15)       NOT NULL, --�Y���R�W���p�߼g���A�i�H���������Ʀ�k��/�]�p�h�ק�
  [Description]  ntext             NULL,    --Description�O�O�d�r�A�~�h�n�[�W���A���A�_�h�|��ܬ��Ŧ�
  Picture        image             NULL)

/*��X�إߪ���ƪ��e*/
SELECT * FROM Categories2  --�i���ƨϥ��˵��C�����G
SELECT * FROM Categories2 WHERE [Description] LIKE '%��%'

/*�W�R���*/
ALTER TABLE Categories2 ADD CategoryName  nvarchar(15) NULL  
ALTER TABLE Categories2 ADD [�Ƶ�]  nvarchar(15) NULL  --����r���Y�n�Τ��A���ذ_��
ALTER TABLE Categories2 DROP COLUMN CattegoryName

/*�R�������C*/
DELETE Categories2
WHERE CategoryID2 BETWEEN 1 AND 4
