-------------------------------------------------------------------------------
-- Destinatários Padrão
-------------------------------------------------------------------------------
INSERT INTO destinatarios(descricao, endpoint, tipo_destinatario, assinante_recall, assinante_eventos)
VALUES
	('Detran RJ','http://www.detran.rj.gov.br/webservices/notifications','WEBSERVICE',true,false),
	('Detran SP','https://www.detran.sp.gov.br/servicos/comunicados','WEBSERVICE',true,false),
	('ANFAVEA','comunicacao@anfavea.com.br','EMAIL',true,false),
	('Autoesporte','contato@autoesporte.com.br','EMAIL',true,false),
	('XYZ Estudos de qualidade','http://estudos-qualidade.com.br/','WEBSERVICE',false,true),
	('Parceiros da Matriz','http://api.parceiro.net/eventos','WEBSERVICE',false,true);
