package com.kasemodel.goldenraspberryaward.interfaces.rest.exception.handler;

import com.kasemodel.goldenraspberryaward.infra.persistence.exception.MovieAlreadyExistsException;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.movie.MovieNotFoundException;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.movie.MovieWithoutProducersException;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.movie.MovieWithoutStudiosException;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.movie.MovieWithoutTitleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class MovieExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler({
		MovieWithoutTitleException.class,
		MovieWithoutProducersException.class,
		MovieWithoutStudiosException.class,
		MovieAlreadyExistsException.class})
	public ResponseEntity onBadRequestError(final RuntimeException ex, final WebRequest request) {
		log.error("Movie error: {}", ex.getMessage());
		return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(MovieNotFoundException.class)
	public ResponseEntity onNotFoundException(final MovieNotFoundException ex, final WebRequest request) {
		log.error("Movie error: {}", ex.getMessage());
		return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
}
