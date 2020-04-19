-- Carregamento de normas
INSERT INTO normas(id, nome, area_industrial, resumo, link_repositorio) 
VALUES
	(1, 'ISO 9000', 'QUALIDADE', 'resumo', 'http://repositorio-normas.net/ISO9000'),
	(2, 'ISO 9001', 'QUALIDADE', 'resumo', 'http://repositorio-normas.net/ISO9001'),
	(3, 'ISO 9004', 'QUALIDADE', 'resumo', 'http://repositorio-normas.net/ISO9004'),
	(4, 'ISO 19011', 'QUALIDADE', 'resumo', 'http://repositorio-normas.net/ISO19011'),
	(5, 'ISO/IEC 27002', 'TI', 'resumo', 'http://repositorio-normas.net/ISO27002'),
	(6, 'ISO/IEC 12207', 'TI', 'resumo', 'http://repositorio-normas.net/ISO12207'),
	(7, 'IATF 16949', 'AUTOMOVEL', 'resumo', 'http://repositorio-normas.net/IATF16494'),
	(8, 'IATF 16949', 'AUTOMOVEL', 'resumo', 'http://repositorio-normas.net/IATF16494'),
	(9, 'ISO 14000', 'AMBIENTE', 'resumo', 'http://repositorio-normas.net/ISO14000'),
	(10, 'ISO 14001', 'AMBIENTE', 'resumo', 'http://repositorio-normas.net/ISO14001'),
	(11, 'OHSAS 18001', 'SEGURANCA_TRABALHO', 'resumo', 'http://repositorio-normas.net/OHSAS18001'),
	(12, 'ISO 22000', 'SEGURANCA_ALIMENTAR', 'resumo', 'http://repositorio-normas.net/ISO22000');

-- Carregamento de critérios genéricos para checklists
INSERT INTO checklist_items(id, criterio)
VALUES
	(1, 'Critério X da norma está dentro do padrão aceitável?'),
	(2, 'Os passos A,B e C foram seguidos nesta ordem?'),
	(3, 'Todos os items passaram por revisão manual?'),
	(4, 'Foi registrada a amostra X no artefato de acordo com a norma?'),
	(5, 'Houve inspeção por pares no artefato resultante?'),
	(6, 'Registrou-se a questão Y de acordo com a norma?'),
	(7, 'Foi obtido um grau igual ou superior em X% da medida?'),
	(8, 'Foi possível reproduzir o efeito esperado após a mudança X?'),
	(9, 'As dimensões esperadas encontram-se em X mm?'),
	(10, 'Após a revisão, foi criado o artefato Y de aceite?'),
	(11, 'A temperatura de produção do artefato foi mantida em X ºC constantes?');
	
-- Associação arbitrária de critérios genéricos
INSERT INTO normas_check_list(normas_id, check_list_id)
VALUES 
	(1,2),(1,4),
	(2,7),(2,4),(2,2),(2,3),
	(3,3),(3,11),
	(4,1),(4,1),(4,1),
	(5,8),(5,9),(5,10),(5,11),(5,3),
	(6,3),(6,1),(6,7),
	(7,3),(7,4),
	(8,2),
	(9,3),(9,5),(9,7),
	(10,1),(10,2),(10,3),(10,4),(10,5),(10,11),
	(11,1),(11,3),
	(12,11);
	
	