-- USUARIOS PADRAO
INSERT INTO usuarios(username, display_name, password)
VALUES
	('gestor', 'Gestor de Qualidade', '{noop}g123'),
	('analista', 'Analista de Qualidade', '{noop}a123');
	
-- PAPEIS PADRAO
INSERT INTO papeis(authority)
VALUES
	('ROLE_GESTOR'),
	('ROLE_ANALISTA');
	
-- ATRIBUICAO DE PAPEIS
INSERT INTO usuarios_authorities(usuario_id, authorities_authority)
VALUES
	(1, 'ROLE_GESTOR'),
	(2, 'ROLE_ANALISTA');
