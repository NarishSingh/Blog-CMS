USE blogCms;

-- test
INSERT INTO `role` (`role`)
    VALUES ("ROLE_ADMIN");

INSERT INTO `role` (`role`)
    VALUES ("ROLE_CREATOR");

-- admin manual add
INSERT INTO `user` (username, password, isEnabled)
    VALUES ("Narish", "password", 1);

INSERT INTO userRole (userId, roleId)
    VALUES (1,1);

UPDATE `user` SET 
    password = "$2a$10$7jEqWdVdbs8p6kImwvpD6.AwdiQEZ.bdgl6QF/LSoqHF0AQvnVpFS"
WHERE userId = 1;

-- INSERT INTO post(title, body, isApproved, isStaticPage, createdOn, postOn, expireOn, userId)
--     values ("title", "body", 1, 1, now(), now(), null, 1);

SELECT * FROM blogCms.`user`;
SELECT * FROM `role`;
SELECT * FROM userRole;
SELECT * FROM blogCms.category;
SELECT * FROM blogCms.postCategory;

SELECT * FROM blogCms.post;
SELECT * FROM blogCms.post 
    WHERE (postOn <= NOW() AND expireOn >= NOW()) 
        AND isApproved != 0;