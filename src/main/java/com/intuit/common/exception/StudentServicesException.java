package com.intuit.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by kian on 1/19/16.
 */
public class StudentServicesException extends Exception {
	private ErrorCode errorCode;

	private HttpStatus status;

	public StudentServicesException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public HttpStatus getStatus() {
		return status;
	}

}
