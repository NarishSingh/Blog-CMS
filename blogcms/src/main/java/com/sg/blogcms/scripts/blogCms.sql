DROP DATABASE IF EXISTS blogCms;
CREATE DATABASE blogCms;

USE blogCms;

CREATE TABLE `role` (
    roleId INT PRIMARY KEY AUTO_INCREMENT,
    `role` VARCHAR(30) NOT NULL
);

CREATE TABLE `user` (
    userId INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    isEnabled BIT NOT NULL DEFAULT 0,
    firstName VARCHAR(50),
    lastName VARCHAR(50),
    email VARCHAR(50),
    photoFilename VARCHAR(255)
);

-- bridge table
CREATE TABLE userRole (
    roleId INT NOT NULL,
    userId INT NOT NULL,
    PRIMARY KEY (roleId, userId),
    CONSTRAINT `fk_role_userRole` FOREIGN KEY (roleId)
        REFERENCES `role`(roleId),
    CONSTRAINT `fk_user_userRole` FOREIGN KEY (userId)
        REFERENCES `user`(userId)
);

CREATE TABLE post (
    postId INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    body TEXT NOT NULL,
    isApproved BIT NOT NULL DEFAULT 0,
    isStaticPage BIT NOT NULL DEFAULT 0,
    createdOn DATETIME NOT NULL DEFAULT NOW(),
    postOn DATETIME NOT NULL,
    expireOn DATETIME NOT NULL,
    photoFilename VARCHAR(255),
    userId INT NOT NULL,
    CONSTRAINT `fk_user_post` FOREIGN KEY (userId)
        REFERENCES `user` (userId)
);

CREATE TABLE category (
    categoryId INT PRIMARY KEY AUTO_INCREMENT,
    category VARCHAR(50) NOT NULL
);

-- bridge table
CREATE TABLE postCategory (
    postId INT NOT NULL,
    categoryId INT NOT NULL,
    PRIMARY KEY (postId, categoryId),
    CONSTRAINT `fk_post_postCategory` FOREIGN KEY (postId)
        REFERENCES post (postId),
    CONSTRAINT `fk_category_postCategory` FOREIGN KEY (categoryId)
        REFERENCES category (categoryId)
);