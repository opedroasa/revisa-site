-- 1) Adiciona coluna email (temporariamente opcional)
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS email VARCHAR(255);

-- 2) Preenche email com o username para contas já existentes
UPDATE usuario SET email = username WHERE email IS NULL;

-- 3) Torna email obrigatório e único
ALTER TABLE usuario ALTER COLUMN email SET NOT NULL;
ALTER TABLE usuario ADD CONSTRAINT uk_usuario_email UNIQUE (email);

-- 4) Campo de status (ativo) para permitir ativar/inativar futuramente
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS ativo BOOLEAN;

UPDATE usuario SET ativo = TRUE WHERE ativo IS NULL;
ALTER TABLE usuario ALTER COLUMN ativo SET NOT NULL;
