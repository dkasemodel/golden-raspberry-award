package com.kasemodel.goldenraspberryaward.interfaces.rest.exception.handler;

import com.kasemodel.goldenraspberryaward.infra.persistence.exception.movie.MovieNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class MovieExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler({MovieNotFoundException.class})
	public ResponseEntity onError(final MovieNotFoundException ex, final WebRequest request) {
		return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
}
