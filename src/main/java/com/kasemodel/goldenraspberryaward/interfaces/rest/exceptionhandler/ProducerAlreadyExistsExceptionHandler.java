package com.kasemodel.goldenraspberryaward.interfaces.rest.exceptionhandler;

import com.kasemodel.goldenraspberryaward.infra.persistence.exception.ProducerAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ProducerAlreadyExistsExceptionHandler {
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ProducerAlreadyExistsException.class)
	@ResponseBody
	public ResponseEntity onError(final ProducerAlreadyExistsException ex) {
		if (ex == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}
}
