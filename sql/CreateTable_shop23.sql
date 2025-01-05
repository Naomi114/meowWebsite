/*建立資料表*/
CREATE TABLE dbo.Categories2
( CategoryID2    int IDENTITY(1,1) NOT NULL,
  CategoryName nvarchar(15)       NOT NULL, --若欄位命名不小心寫錯，可以到對應的資料行右鍵/設計去修改
  [Description]  ntext             NULL,    --Description是保留字，外層要加上中括號，否則會顯示為藍色
  Picture        image             NULL)

/*輸出建立的資料表內容*/
SELECT * FROM Categories2  --可重複使用檢視每次結果
SELECT * FROM Categories2 WHERE [Description] LIKE '%茶%'

/*增刪欄位*/
ALTER TABLE Categories2 ADD CategoryName  nvarchar(15) NULL  
ALTER TABLE Categories2 ADD [備註]  nvarchar(15) NULL  --中文字表頭要用中括號框起來
ALTER TABLE Categories2 DROP COLUMN CattegoryName

/*刪除部分列*/
DELETE Categories2
WHERE CategoryID2 BETWEEN 1 AND 4
