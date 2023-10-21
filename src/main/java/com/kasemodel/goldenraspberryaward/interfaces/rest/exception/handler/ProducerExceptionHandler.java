package com.kasemodel.goldenraspberryaward.interfaces.rest.exception.handler;

import com.kasemodel.goldenraspberryaward.infra.persistence.exception.ProducerAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ProducerExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(ProducerAlreadyExistsException.class)
	public ResponseEntity onError(final ProducerAlreadyExistsException ex, final WebRequest request) {
		return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
}
