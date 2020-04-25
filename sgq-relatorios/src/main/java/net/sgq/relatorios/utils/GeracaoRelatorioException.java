package net.sgq.relatorios.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Não foi possível gerar o relatório")
public class GeracaoRelatorioException extends RuntimeException {

	private static final long serialVersionUID = -6123380188873058894L;

}
