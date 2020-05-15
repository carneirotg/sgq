package net.sgq.incidentes.utils.handler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return new ResponseEntity<>(mensagemErro(ex, "Requisição com parâmetros incompletos"),
				HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(HttpServletRequest reques, Exception ex) {
		return new ResponseEntity<>(mensagemErro(ex, "Tipo de dados incorreto na requisição"), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ IllegalStateException.class })
	protected ResponseEntity<Object> requisicaoInvalida(HttpServletRequest reques, Exception ex) {
		return new ResponseEntity<>(mensagemErro(ex, "Estado inválido"), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ EntityNotFoundException.class })
	protected ResponseEntity<Object> entidadeNaoEncontrada(HttpServletRequest reques, Exception ex) {
		return new ResponseEntity<>(mensagemErro(ex, "Não encontrado"), HttpStatus.NOT_FOUND);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		BindingResult result = ex.getBindingResult();
		int errorCount = result.getErrorCount();
		String errors = result.getFieldErrors().stream().map(e -> e.getField() + " - " + e.getDefaultMessage())
				.collect(Collectors.joining("\r\n"));

		return new ResponseEntity<>(mensagemErro(ex, "Erro(s) de validação (" + errorCount + "): " + errors),
				HttpStatus.BAD_REQUEST);
	}

	private Map<String, Object> mensagemErro(Exception ex, String erro) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("erro", erro);
		body.put("erroCompleto", ex.getMessage());
		return body;
	}

}
