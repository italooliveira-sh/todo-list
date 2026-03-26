ALTER TABLE users RENAME COLUMN username TO name;
DROP INDEX IF EXISTS idx_users_username; -- Remove o índice antigo se existir
CREATE INDEX idx_users_name ON users(name); -- Cria o novo índice para o campo renomeado
