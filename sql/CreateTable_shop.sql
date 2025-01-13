/*建立資料表*/
USE meowdb

CREATE TABLE category (
    categoryId INT PRIMARY KEY IDENTITY(1,1),
    categoryName NVARCHAR(255) UNIQUE NULL,
    categoryDescription NVARCHAR(MAX) NULL,
    defaultUnit CHAR(10) NOT NULL
);

-- 建立 Admin 表 (等Jude表格取代)
CREATE TABLE admin (
    adminId INT PRIMARY KEY IDENTITY(1,1),
    adminName NVARCHAR(255) NOT NULL
);

-- 建立 member 表(等Jude表格取代)
CREATE TABLE member (
    memberId INT PRIMARY KEY IDENTITY(1,1),
    memberName NVARCHAR(255) NOT NULL
);

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
    FOREIGN KEY (categoryId) REFERENCES category(categoryId),
    FOREIGN KEY (adminId) REFERENCES Admin(adminId)
);

CREATE TABLE productImage (
    imageId INT PRIMARY KEY IDENTITY(1,1),
    productId INT NOT NULL,
    imageUrl NVARCHAR(255) NOT NULL,
    isPrimary BIT NOT NULL,
    createdAt DATETIME DEFAULT GETDATE() NOT NULL,
    FOREIGN KEY (productId) REFERENCES product(productId)
);

CREATE TABLE tag (
    tagId INT PRIMARY KEY IDENTITY(1,1),
    tagName NVARCHAR(255) NOT NULL,
    tagDescription NVARCHAR(MAX) NULL
);

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

CREATE TABLE discount (
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

CREATE TABLE wishlist (
    wishlistId INT PRIMARY KEY IDENTITY(1,1),
    memberId INT NOT NULL,
    productId INT NOT NULL,
    addedAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (memberId) REFERENCES member(memberId),
    FOREIGN KEY (productId) REFERENCES product(productId)
);


-- Create Cart table (One-to-One with Member)
CREATE TABLE cart (
    cartId INT PRIMARY KEY,
    memberId INT NOT NULL,
    lastUpdatedDate DATETIME2 NOT NULL,  -- Changed from TIMESTAMP to DATETIME2
    FOREIGN KEY (memberId) REFERENCES Member(memberId)
);

-- Create CartItem table (One-to-Many with Cart and Product)
CREATE TABLE cartItem (
    cartItemId INT PRIMARY KEY,
    cartId INT NOT NULL,
    productId INT NOT NULL,
    cartItemStatus NVARCHAR(20),
    createDate DATETIME2 NOT NULL,  -- Changed from TIMESTAMP to DATETIME2
    updateDate DATETIME2,  -- Changed from TIMESTAMP to DATETIME2
    cartItemQuantity INT NOT NULL,
    FOREIGN KEY (cartId) REFERENCES Cart(cartId),
    FOREIGN KEY (productId) REFERENCES Product(productId)
);

-- Create Order table (One-to-Many with Member and discount)
CREATE TABLE [Order] (
    orderId INT PRIMARY KEY,
    memberId INT NOT NULL,
    discountId INT NOT NULL,
    shippingAddress NVARCHAR(255) NOT NULL,
    orderDate DATETIME2,
    creditCard CHAR(16) NOT NULL,
    orderStatus NVARCHAR(20) NOT NULL,
    feedback NVARCHAR(MAX),
    subtotalPrice FLOAT NOT NULL,
    finalPrice FLOAT NOT NULL,
    FOREIGN KEY (memberId) REFERENCES Member(memberId),
    FOREIGN KEY (discountId) REFERENCES discount(discountId)
);

-- Create OrderItem table (One-to-Many with Order and Product)
CREATE TABLE orderItem (
    orderItemId INT PRIMARY KEY,
    orderId INT NOT NULL,
    productId INT NOT NULL,
    orderQuantity INT NOT NULL,
    purchasedPrice FLOAT NOT NULL,
    FOREIGN KEY (orderId) REFERENCES [Order](orderId),
    FOREIGN KEY (productId) REFERENCES Product(productId)
);

-- Create discountScope table (One-to-Many with discount, Category, Product, Member)
CREATE TABLE discountScope (
    discountScopeId INT PRIMARY KEY,
    discountScopeType NVARCHAR(20) NOT NULL,
    discountId INT NOT NULL,
    categoryId INT,
    productId INT,
    birthdayMonth CHAR(2),
    FOREIGN KEY (discountId) REFERENCES discount(discountId),
    FOREIGN KEY (categoryId) REFERENCES category(categoryId),
    FOREIGN KEY (productId) REFERENCES product(productId)
);


