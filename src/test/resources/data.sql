INSERT INTO app_user (nickname, email, password, role, status)
VALUES ('name1', 'email1', 'password', 'user', 'normal');
INSERT INTO app_user (nickname, email, password, role, status)
VALUES ('name2', 'email2', 'password', 'user', 'normal');
INSERT INTO app_user (nickname, email, password, role, status)
VALUES ('name3', 'email3', 'password', 'admin', 'normal');

INSERT INTO item (name, description, owner_id, manager_id, status)
VALUES ('name1', 'description1', 1, 1, 'pending');
INSERT INTO item (name, description, owner_id, manager_id, status)
VALUES ('name2', 'description2', 2, 2, 'pending');

INSERT INTO reservation (item_id, user_id, start_at, end_at, status)
VALUES (1, 2, CURRENT_DATE, CURRENT_DATE, 'pending');
INSERT INTO reservation (item_id, user_id, start_at, end_at, status)
VALUES (2, 2, CURRENT_DATE, CURRENT_DATE, 'approved');
INSERT INTO reservation (item_id, user_id, start_at, end_at, status)
VALUES (1, 1, NOW(), NOW(), 'expired');
INSERT INTO reservation (item_id, user_id, start_at, end_at, status)
VALUES (1, 1, NOW(), NOW(), 'canceled');