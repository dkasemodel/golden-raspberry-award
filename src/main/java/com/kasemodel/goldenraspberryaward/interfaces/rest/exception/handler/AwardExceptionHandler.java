package com.kasemodel.goldenraspberryaward.interfaces.rest.exception.handler;

import com.kasemodel.goldenraspberryaward.infra.persistence.exception.AwardAlreadyExistsException;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.award.AwardNotFoundException;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.award.AwardUpdateException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class AwardExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler({
		AwardAlreadyExistsException.class,
		AwardNotFoundException.class,
		AwardUpdateException.class})
	public ResponseEntity onError(final RuntimeException ex, final WebRequest request) {
		return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

//	@ExceptionHandler({AwardNotFoundException.class})
//	public ResponseEntity onError(final AwardNotFoundException ex, final WebRequest request) {
//		return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
//	}
}
