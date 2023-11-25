INSERT INTO USERS (id, name, surname, username, email, password, created_at, updated_at, is_deleted)
VALUES
    (UUID(), 'Madirex', 'Land', 'Test', 'test@madirex.com', '$2a$10$/iB5BY9exKMl6Soofb7EquzGHWm3vOPHBCBb6Cg3sWcfz9kHUyIpC', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);