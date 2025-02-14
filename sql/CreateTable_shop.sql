/* 建立商城資料表順序
1.  Admin
2.  Member
3.  Category
4.  Product
5.  ProductImage
6.  ProductTag
7.  ProductTagMapping
8.  Discount
9.  DiscountScope
10. Inventory
11. InventoryItem
12. Orders
13. OrderItem
14. Wishlist
15. Cart
16. CartActionLog
17. CartItem
18. Notification
*/

USE meowdb

CREATE TABLE Admin (
    adminId INT PRIMARY KEY AUTO_INCREMENT,
    adminName VARCHAR(20) UNIQUE NOT NULL,
    password VARCHAR(20) NOT NULL,
    createDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL
);

CREATE TABLE Member (
    memberId INT PRIMARY KEY AUTO_INCREMENT,
    nickName VARCHAR(20) UNIQUE NOT NULL,
    password VARCHAR(20) NOT NULL,
    name VARCHAR(70) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(10) NOT NULL,
    address VARCHAR(100) NOT NULL,
    birthday DATE NOT NULL,
    createDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    lineId VARCHAR(255),
    lineName VARCHAR(255),
    linePicture VARCHAR(255),
    followed BOOLEAN DEFAULT FALSE,
    userType BOOLEAN NOT NULL
);

CREATE TABLE Category (
    categoryId INT PRIMARY KEY AUTO_INCREMENT,
    categoryName VARCHAR(255) UNIQUE NOT NULL,
    categoryDescription TEXT,
    defaultUnit VARCHAR(10)
);

CREATE TABLE Product (
    productId INT PRIMARY KEY AUTO_INCREMENT,
    productName VARCHAR(255) NOT NULL,
    description TEXT,
    originalPrice DECIMAL(10,2) NOT NULL,
    salePrice DECIMAL(10,2) NOT NULL,
    stockQuantity INT NOT NULL,
    unit VARCHAR(50),
    status VARCHAR(50),
    expire DATE NOT NULL,
    createdAt DATETIME NOT NULL,
    updatedAt DATETIME NOT NULL,
    FK_categoryId INT,
    FK_adminId INT,
    FOREIGN KEY (FK_categoryId) REFERENCES Category(categoryId) ON DELETE SET NULL,
    FOREIGN KEY (FK_adminId) REFERENCES Admin(adminId) ON DELETE SET NULL
);

CREATE TABLE ProductImage (
    imageId INT PRIMARY KEY AUTO_INCREMENT,
    imageUrl VARCHAR(255) NOT NULL,
    isPrimary BOOLEAN NOT NULL,
    createdAt DATETIME NOT NULL,
    FK_productId INT NOT NULL,
    FOREIGN KEY (FK_productId) REFERENCES Product(productId) ON DELETE CASCADE
);

CREATE TABLE ProductTag (
    tagId INT PRIMARY KEY AUTO_INCREMENT,
    tagName VARCHAR(255) NOT NULL,
    tagDescription TEXT
);

CREATE TABLE ProductTagMapping (
    FK_productId INT NOT NULL,
    FK_tagId INT NOT NULL,
    PRIMARY KEY (FK_productId, FK_tagId),
    FOREIGN KEY (FK_productId) REFERENCES Product(productId) ON DELETE CASCADE,
    FOREIGN KEY (FK_tagId) REFERENCES ProductTag(tagId) ON DELETE CASCADE
);

CREATE TABLE Discount (
    discountId INT PRIMARY KEY AUTO_INCREMENT,
    FK_adminId INT NOT NULL,
    discountStartTime DATETIME NOT NULL,
    discountEndTime DATETIME NOT NULL,
    minAmount DECIMAL(10,2),
    minQuantity INT,
    priority INT,
    discountStatus VARCHAR(20),
    discountType VARCHAR(20),
    discountValue DECIMAL(10,2),
    FOREIGN KEY (FK_adminId) REFERENCES Admin(adminId) ON DELETE CASCADE
);

CREATE TABLE DiscountScope (
    discountScopeId INT PRIMARY KEY AUTO_INCREMENT,
    discountScopeType VARCHAR(255) NOT NULL UNIQUE,
    FK_discountId INT NOT NULL,
    FK_categoryId INT,
    FK_productId INT,
    FK_memberId INT,
    FOREIGN KEY (FK_discountId) REFERENCES Discount(discountId) ON DELETE CASCADE,
    FOREIGN KEY (FK_categoryId) REFERENCES Category(categoryId) ON DELETE SET NULL,
    FOREIGN KEY (FK_productId) REFERENCES Product(productId) ON DELETE SET NULL
    FOREIGN KEY (FK_memberId) REFERENCES Member(memberId) ON DELETE SET NULL
);

CREATE TABLE Inventory (
    inventoryId INT PRIMARY KEY AUTO_INCREMENT,
    quantity INT NOT NULL,
    diffReason VARCHAR(255),
    inventoryStatus VARCHAR(255) NOT NULL,
    checkAt DATETIME NOT NULL,
    endAt DATETIME NOT NULL,
    FK_adminId INT NOT NULL,
    FOREIGN KEY (FK_adminId) REFERENCES Admin(adminId) ON DELETE CASCADE
);

CREATE TABLE InventoryItem (
    inventoryItemId INT PRIMARY KEY AUTO_INCREMENT,
    stockQuantity INT NOT NULL,
    actualStock INT NOT NULL,
    FK_productId INT NULL,
    FK_inventoryId INT NULL,
    FOREIGN KEY (FK_productId) REFERENCES Product(productId) ON DELETE SET NULL,
    FOREIGN KEY (FK_inventoryId) REFERENCES Inventory(inventoryId) ON DELETE SET NULL
);

CREATE TABLE Orders (
    orderId INT PRIMARY KEY AUTO_INCREMENT,
    FK_memberId INT NOT NULL,
    FK_discountId INT,
    shippingAddress VARCHAR(255) NOT NULL,
    orderDate DATETIME NOT NULL,
    creditCard VARCHAR(255) NOT NULL,
    orderStatus VARCHAR(255) NOT NULL,
    feedback TEXT,
    subtotalPrice DOUBLE NOT NULL,
    finalPrice DOUBLE NOT NULL,
    transactionId VARCHAR(255),
    merchantTradeNo VARCHAR(255),
    FOREIGN KEY (FK_memberId) REFERENCES Member(memberId) ON DELETE CASCADE,
    FOREIGN KEY (FK_discountId) REFERENCES Discount(discountId) ON DELETE SET NULL
);

CREATE TABLE OrderItem (
    orderItemId INT PRIMARY KEY AUTO_INCREMENT,
    FK_orderId INT NOT NULL,
    FK_productId INT NOT NULL,
    orderQuantity INT NOT NULL,
    purchasedPrice DECIMAL(10,2) NOT NULL,
    status VARCHAR(255) NOT NULL,
    FOREIGN KEY (FK_orderId) REFERENCES Orders(orderId) ON DELETE CASCADE,
    FOREIGN KEY (FK_productId) REFERENCES Product(productId) ON DELETE CASCADE
);

CREATE TABLE WishList (
    wishlistId INT PRIMARY KEY AUTO_INCREMENT,
    FK_memberId INT NOT NULL,
    FK_productId INT NOT NULL,
    addedAt DATETIME NOT NULL,
    FOREIGN KEY (FK_memberId) REFERENCES Member(memberId) ON DELETE CASCADE,
    FOREIGN KEY (FK_productId) REFERENCES Product(productId) ON DELETE CASCADE
);

CREATE TABLE Cart (
    cartId INT PRIMARY KEY AUTO_INCREMENT,
    FK_memberId INT NOT NULL,
    lastUpdatedDate DATETIME NOT NULL,
    FOREIGN KEY (FK_memberId) REFERENCES Member(memberId) ON DELETE CASCADE
);

CREATE TABLE CartActionLog (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    FK_memberId INT NOT NULL,
    action VARCHAR(255) NOT NULL,
    details TEXT,
    timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (FK_memberId) REFERENCES Member(memberId) ON DELETE CASCADE
);

CREATE TABLE CartItem (
    cartItemId INT PRIMARY KEY AUTO_INCREMENT,
    FK_cartId INT NOT NULL,
    FK_productId INT NOT NULL,
    FK_orderId INT,
    FK_discountId INT,
    cartItemStatus VARCHAR(255),
    createDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updateDate DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    cartItemQuantity INT NOT NULL,
    FOREIGN KEY (FK_cartId) REFERENCES Cart(cartId) ON DELETE CASCADE,
    FOREIGN KEY (FK_productId) REFERENCES Product(productId) ON DELETE CASCADE,
    FOREIGN KEY (FK_orderId) REFERENCES Orders(orderId) ON DELETE SET NULL,
    FOREIGN KEY (FK_discountId) REFERENCES Discount(discountId) ON DELETE SET NULL
);

CREATE TABLE Notification (
    notificationId BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    [message] TEXT NOT NULL,
    createdAt DATETIME NOT NULL,
    FK_memberId INT,
    FK_adminId INT,
    readStatus BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (FK_memberId) REFERENCES Member(memberId) ON DELETE CASCADE,
    FOREIGN KEY (FK_adminId) REFERENCES Admin(adminId) ON DELETE CASCADE
);


