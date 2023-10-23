package com.kasemodel.goldenraspberryaward.interfaces.rest.exception.handler;

import com.kasemodel.goldenraspberryaward.infra.persistence.exception.producer.ProducerAlreadyExistsException;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.producer.ProducerNameCannotBeEmptyException;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.producer.ProducerNotFoundException;
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
	@ExceptionHandler({
		ProducerAlreadyExistsException.class,
		ProducerNameCannotBeEmptyException.class
	})
	public ResponseEntity onBadRequestError(final RuntimeException ex, final WebRequest request) {
		log.error("Producer error: {}", ex.getMessage());
		return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(ProducerNotFoundException.class)
	public ResponseEntity onNotFoundError(final RuntimeException ex, final WebRequest request) {
		log.error("Producer error: {}", ex.getMessage());
		return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

}
