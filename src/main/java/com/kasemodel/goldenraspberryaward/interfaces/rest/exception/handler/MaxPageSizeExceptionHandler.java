package com.kasemodel.goldenraspberryaward.interfaces.rest.exception.handler;

import com.kasemodel.goldenraspberryaward.interfaces.rest.exception.MaxPageSizeException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class MaxPageSizeExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler({MaxPageSizeException.class})
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity onError(final MaxPageSizeException ex, final WebRequest request) {
		return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
}
