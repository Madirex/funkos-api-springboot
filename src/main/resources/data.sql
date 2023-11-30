-- Category
INSERT INTO Category (type, created_at, updated_at, active)
SELECT 'DISNEY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true
    WHERE NOT EXISTS (SELECT 1 FROM Category WHERE type = 'DISNEY')
UNION ALL
SELECT 'MOVIE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true
    WHERE NOT EXISTS (SELECT 1 FROM Category WHERE type = 'MOVIE')
UNION ALL
SELECT 'SERIES', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true
    WHERE NOT EXISTS (SELECT 1 FROM Category WHERE type = 'SERIES')
UNION ALL
SELECT 'SUPERHEROS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true
    WHERE NOT EXISTS (SELECT 1 FROM Category WHERE type = 'SUPERHEROS')
UNION ALL
SELECT 'OTHER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true
    WHERE NOT EXISTS (SELECT 1 FROM Category WHERE type = 'OTHER');

-- Funko
MERGE INTO Funko USING DUAL ON id = '57b77805-c1ce-490f-a96f-ec15505d5fae'
    WHEN NOT MATCHED THEN
        INSERT (id, name, price, quantity, image, category_id, created_at, updated_at)
            VALUES ('57b77805-c1ce-490f-a96f-ec15505d5fae', 'Mickey Mouse', 19.99, 10, 'https://www.madirex.com/funko/mickeymouse', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


INSERT INTO Funko (id, name, price, quantity, image, category_id, created_at, updated_at)
SELECT '874f872f-c30e-4089-ab33-16fd1c4d4344', 'Harry Potter', 24.99, 8, 'https://www.madirex.com/funko/harrypotter', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM Funko WHERE id = '874f872f-c30e-4089-ab33-16fd1c4d4344')
UNION ALL
SELECT '1783830d-1832-466e-a444-9510cecd06c2', 'Spider-Man', 14.99, 12, 'https://www.madirex.com/funko/spiderman', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM Funko WHERE id = '1783830d-1832-466e-a444-9510cecd06c2')
UNION ALL
SELECT '9c688032-c485-43ae-8706-e03e1d2a19d0', 'Monkey D. Luffy', 34.99, 7, 'https://www.madirex.com/funko/monkeydluffy', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM Funko WHERE id = '9c688032-c485-43ae-8706-e03e1d2a19d0')
UNION ALL
SELECT 'b5e349a5-7650-4bff-8596-5cc89853179c', 'Doctor Who', 39.99, 3, 'https://www.madirex.com/funko/doctorwho', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM Funko WHERE id = 'b5e349a5-7650-4bff-8596-5cc89853179c')
UNION ALL
SELECT '7a47f728-e959-4c81-a45d-921f39f343bb', 'Random Funko', 34.99, 7, 'https://www.madirex.com/funko/random', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM Funko WHERE id = '7a47f728-e959-4c81-a45d-921f39f343bb');

INSERT INTO USERS (id, name, surname, username, email, password, created_at, updated_at, is_deleted)
SELECT 'a2bcb7b3-4c36-4073-9ab8-9d8f4a57301b', 'Madirex', 'Land', 'Madirex', 'contact@madirex.com', '$2a$10$/iB5BY9exKMl6Soofb7EquzGHWm3vOPHBCBb6Cg3sWcfz9kHUyIpC', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false
    WHERE NOT EXISTS (SELECT 1 FROM USERS WHERE id = 'a2bcb7b3-4c36-4073-9ab8-9d8f4a57301b');

INSERT INTO USERS (id, name, surname, username, email, password, created_at, updated_at, is_deleted)
SELECT 'd4515840-8fe2-4107-8bde-0f1c545ef4c5', 'User', 'User', 'User', 'user@madirex.com', '$2a$10$/iB5BY9exKMl6Soofb7EquzGHWm3vOPHBCBb6Cg3sWcfz9kHUyIpC', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false
    WHERE NOT EXISTS (SELECT 1 FROM USERS WHERE id = 'd4515840-8fe2-4107-8bde-0f1c545ef4c5');

-- User Roles
INSERT INTO USER_ROLES (user_id, roles)
SELECT 'a2bcb7b3-4c36-4073-9ab8-9d8f4a57301b', 'USER'
    WHERE NOT EXISTS (SELECT 1 FROM USER_ROLES WHERE user_id = 'a2bcb7b3-4c36-4073-9ab8-9d8f4a57301b' AND roles = 'USER');

INSERT INTO USER_ROLES (user_id, roles)
SELECT 'a2bcb7b3-4c36-4073-9ab8-9d8f4a57301b', 'ADMIN'
    WHERE NOT EXISTS (SELECT 1 FROM USER_ROLES WHERE user_id = 'a2bcb7b3-4c36-4073-9ab8-9d8f4a57301b' AND roles = 'ADMIN');

INSERT INTO USER_ROLES (user_id, roles)
SELECT 'd4515840-8fe2-4107-8bde-0f1c545ef4c5', 'USER'
    WHERE NOT EXISTS (SELECT 1 FROM USER_ROLES WHERE user_id = 'd4515840-8fe2-4107-8bde-0f1c545ef4c5' AND roles = 'USER');
