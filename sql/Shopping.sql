-- Check if the database 'moewdb' exists
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'moewdb')
BEGIN
    CREATE DATABASE moewdb;
END

USE moewdb;

-- 1. Check and create temporary tables for missing foreign keys (if needed)

-- Check if Member table exists
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'Member')
BEGIN
    CREATE TABLE Member (
        memberId INT PRIMARY KEY,
        memberName NVARCHAR(255) NOT NULL,
        birthdayMonth CHAR(2) -- Adding this for FK relationship
    );
END

-- Check if Product table exists
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'Product')
BEGIN
    CREATE TABLE Product (
        productId INT PRIMARY KEY,
        productName NVARCHAR(255) NOT NULL,
        price FLOAT NOT NULL
    );
END

-- Check if Admin table exists
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'Admin')
BEGIN
    CREATE TABLE Admin (
        adminId INT PRIMARY KEY,
        adminName NVARCHAR(255) NOT NULL
    );
END

-- Check if Category table exists
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'Category')
BEGIN
    CREATE TABLE Category (
        categoryId INT PRIMARY KEY,
        categoryName NVARCHAR(255) NOT NULL
    );
END

-- Check if Discount table exists
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'Discount')
BEGIN
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
END

-- 2. Create the main tables and define Foreign Keys

-- Create Cart table (One-to-One with Member)
CREATE TABLE Cart (
    cartId INT PRIMARY KEY,
    memberId INT NOT NULL,
    lastUpdatedDate DATETIME2 NOT NULL,  -- Changed from TIMESTAMP to DATETIME2
    FOREIGN KEY (memberId) REFERENCES Member(memberId)
);

-- Create CartItem table (One-to-Many with Cart and Product)
CREATE TABLE CartItem (
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

-- Create Order table (One-to-Many with Member and Discount)
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
    FOREIGN KEY (discountId) REFERENCES Discount(discountId)
);

-- Create OrderItem table (One-to-Many with Order and Product)
CREATE TABLE OrderItem (
    orderItemId INT PRIMARY KEY,
    orderId INT NOT NULL,
    productId INT NOT NULL,
    orderQuantity INT NOT NULL,
    purchasedPrice FLOAT NOT NULL,
    FOREIGN KEY (orderId) REFERENCES [Order](orderId),
    FOREIGN KEY (productId) REFERENCES Product(productId)
);

-- Create DiscountScope table (One-to-Many with Discount, Category, Product, Member)
CREATE TABLE DiscountScope (
    discountScopeId INT PRIMARY KEY,
    discountScopeType NVARCHAR(20) NOT NULL,
    discountId INT NOT NULL,
    categoryId INT,
    productId INT,
    birthdayMonth CHAR(2),
    FOREIGN KEY (discountId) REFERENCES Discount(discountId),
    FOREIGN KEY (categoryId) REFERENCES Category(categoryId),
    FOREIGN KEY (productId) REFERENCES Product(productId)
);
