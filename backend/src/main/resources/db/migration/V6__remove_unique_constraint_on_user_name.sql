-- Remove a restrição UNIQUE da coluna name (antiga username)
-- No PostgreSQL, ao criar uma coluna com UNIQUE, uma constraint é criada automaticamente.
-- Como a coluna foi renomeada na V4, a constraint original persiste.

ALTER TABLE users DROP CONSTRAINT IF EXISTS users_username_key;
