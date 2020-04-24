package net.sgq.relatorios.utils.handler;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Component;

import feign.Response;
import feign.codec.ErrorDecoder;

@Component
public class FeignErrorHandler implements ErrorDecoder {

	@Override
	public Exception decode(String methodKey, Response response) {

		if (response.status() == 404 && methodKey.contains("GestaoIncidentesClient#consultaIncidentesConcluidos")) {
			return new EntityNotFoundException(methodKey);
		}

		return new Exception(response.status() + " : " + methodKey + " : " + response.reason());

	}

}
