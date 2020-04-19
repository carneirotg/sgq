package net.sgq.gateway.normas.utils.handler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(HttpServletRequest reques, Exception ex) {
		return new ResponseEntity<>(mensagemErro(ex, "Erro Interno GW Gestão de Normas"), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private Map<String, Object> mensagemErro(Exception ex, String erro) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("erro", erro);
		body.put("erroCompleto", ex.getMessage());
		return body;
	}

}
