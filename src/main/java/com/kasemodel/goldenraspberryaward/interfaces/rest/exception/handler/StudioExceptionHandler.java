package com.kasemodel.goldenraspberryaward.interfaces.rest.exception.handler;

import com.kasemodel.goldenraspberryaward.infra.persistence.exception.studio.StudioAlreadyExistsException;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.studio.StudioNameCannotBeEmptyException;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.studio.StudioNotFoundException;
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
public class StudioExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler({
		StudioAlreadyExistsException.class,
		StudioNameCannotBeEmptyException.class
	})
	public ResponseEntity onBadRequestError(final RuntimeException ex, final WebRequest request) {
		log.error("Studio error: {}", ex.getMessage());
		return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(StudioNotFoundException.class)
	public ResponseEntity onNotFoundError(final RuntimeException ex, final WebRequest request) {
		log.error("Studio error: {}", ex.getMessage());
		return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
}
