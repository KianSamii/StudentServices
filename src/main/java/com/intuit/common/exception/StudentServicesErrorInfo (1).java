package com.intuit.common.exception;

import org.springframework.validation.FieldError;

import java.util.List;

/**
 * Created by kian on 1/22/16.
 */
public class StudentServicesErrorInfo {
	private int code;
	private String message;
	private List<FieldError> fieldErrors;

	public StudentServicesErrorInfo(ErrorCode errorCode, List<FieldError> fieldErrors) {
		this.code = errorCode.getErrorCode();
		this.message = errorCode.getMessage();
		this.fieldErrors = fieldErrors;
	}


	public StudentServicesErrorInfo(ErrorCode errorCode) {
		this(errorCode, null);
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public List<FieldError> getFieldErrors() {
		return fieldErrors;
	}

}
