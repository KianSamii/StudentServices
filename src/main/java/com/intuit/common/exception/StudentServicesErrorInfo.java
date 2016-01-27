package com.intuit.common.exception;

/**
 * @author ksamii
 * @since 1/20/16.
 */
public class StudentServicesErrorInfo {
	private String id;
	private int code;
	private String details;

	public StudentServicesErrorInfo(ErrorCode error) {
		this.id = error.name();
		this.code = error.getErrorCode();
		this.details = error.getMessage();
	}

	public String getId() {
		return id;
	}

	public int getCode() {
		return code;
	}

	public String getDetails() {
		return details;
	}
}
