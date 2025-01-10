/*建立資料表*/
USE meowdb

-- 建立 productCategory 表
CREATE TABLE productCategory (
    categoryId INT PRIMARY KEY IDENTITY(1,1),
    categoryName NVARCHAR(255) UNIQUE NULL,
    categoryDescription NVARCHAR(MAX) NULL,
    defaultUnit CHAR(10) NOT NULL
);

-- 建立 Admin 表 (等Jude表格取代)
CREATE TABLE Admin (
    adminId INT PRIMARY KEY IDENTITY(1,1),
    adminName NVARCHAR(255) NOT NULL
);

-- 建立 product 表
CREATE TABLE product (
    productId INT PRIMARY KEY IDENTITY(1,1) NOT NULL,
    productName NVARCHAR(255) NOT NULL UNIQUE,
    productDescription NVARCHAR(MAX) NULL,
    productOriginalPrice DECIMAL(10, 2) NOT NULL,
    productSalePrice DECIMAL(10, 2) NOT NULL,
    productStock INT NOT NULL,
    productUnit NVARCHAR(255) NULL,
    productStatus NVARCHAR(20) NULL,
    expire DATE NOT NULL,
    createdAt DATETIME DEFAULT GETDATE() NOT NULL,
    updatedAt DATETIME DEFAULT GETDATE() NOT NULL,
    categoryId INT NOT NULL,
    adminId INT NOT NULL,
    FOREIGN KEY (categoryId) REFERENCES productCategory(categoryId),
    FOREIGN KEY (adminId) REFERENCES Admin(adminId)
);

-- 建立 productImage 表
CREATE TABLE productImage (
    imageId INT PRIMARY KEY IDENTITY(1,1),
    productId INT NOT NULL,
    imageUrl NVARCHAR(255) NOT NULL,
    isPrimary BIT NOT NULL,
    createdAt DATETIME DEFAULT GETDATE() NOT NULL,
    FOREIGN KEY (productId) REFERENCES product(productId)
);

-- 建立 tag 表
CREATE TABLE tag (
    tagId INT PRIMARY KEY IDENTITY(1,1),
    tagName NVARCHAR(255) NOT NULL,
    tagDescription NVARCHAR(MAX) NULL
);

-- 建立 productTag 表
CREATE TABLE productTag (
    productId INT NOT NULL,
    tagId INT NOT NULL,
    PRIMARY KEY (productId, tagId),
    FOREIGN KEY (productId) REFERENCES product(productId),
    FOREIGN KEY (tagId) REFERENCES tag(tagId)
);

-- 建立 inventory 表
CREATE TABLE inventory (
    inventoryId INT PRIMARY KEY IDENTITY(1,1),
    adminId INT NOT NULL,
    quantity INT NOT NULL,
    diffReason NVARCHAR(MAX) NULL,
    inventoryStatus NVARCHAR(20) NOT NULL,
    checkAt DATETIME DEFAULT GETDATE(),
    endAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (adminId) REFERENCES Admin(adminId)
);

-- 建立 stockAudit 表
CREATE TABLE stockAudit (
    auditId INT PRIMARY KEY IDENTITY(1,1),
    inventoryId INT NOT NULL,
    productId INT NOT NULL,
    auditDate DATE NOT NULL,
    quantityBefore INT NOT NULL,
    quantityAfter INT NOT NULL,
    FOREIGN KEY (inventoryId) REFERENCES inventory(inventoryId),
    FOREIGN KEY (productId) REFERENCES product(productId)
);

-- 建立 discount 表(等Imark表格取代)
CREATE TABLE discount (
    discountId INT PRIMARY KEY IDENTITY(1,1),
    discountPercentage DECIMAL(5, 2) CHECK (discountPercentage BETWEEN 0 AND 100) NOT NULL,
    startDate DATE NOT NULL,
    endDate DATE NOT NULL
);

-- 建立 banner 表
CREATE TABLE banner (
    bannerId INT PRIMARY KEY IDENTITY(1,1),
    bannerTitle NVARCHAR(255) NOT NULL,
    bannerDescription NVARCHAR(MAX) NULL,
    bannerImageUrl NVARCHAR(255) NOT NULL,
    startDate DATE NOT NULL,
    endDate DATE NOT NULL,
    createdAt DATETIME DEFAULT GETDATE(),
    discountId INT NOT NULL,
    FOREIGN KEY (discountId) REFERENCES discount(discountId)
);

-- 建立 member 表(等Jude表格取代)
CREATE TABLE member (
    memberId INT PRIMARY KEY IDENTITY(1,1),
    memberName NVARCHAR(255) NOT NULL
);

-- 建立 wishlist 表
CREATE TABLE wishlist (
    wishlistId INT PRIMARY KEY IDENTITY(1,1),
    memberId INT NOT NULL,
    productId INT NOT NULL,
    addedAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (memberId) REFERENCES member(memberId),
    FOREIGN KEY (productId) REFERENCES product(productId)
);

CREATE TABLE Discount (
        discountId INT PRIMARY KEY,
        adminId INT NOT NULL,
        discountStartTime DATETIME2 NOT NULL,  -- Changed to DATETIME2
        discountEndTime DATETIME2 NOT NULL,    -- Changed to DATETIME2
        minAmount FLOAT,
        minQuantity INT,
        priority INT,
        discountStatus NVARCHAR(20),
        discountType NVARCHAR(20),
        discountValue FLOAT,
        FOREIGN KEY (adminId) REFERENCES Admin(adminId)
    );




