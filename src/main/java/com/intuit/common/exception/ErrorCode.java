package com.intuit.common.exception;

/**
 * Created by kian on 1/19/16.
 */
public enum ErrorCode {

	//Database Errors
	DATABASE_ERROR(1000, "Unexpected Database Error"),
	DATABASE_STATEMENT_ERROR(1001, "Error writing statement to database"),
	DATABASE_DELETE_ERROR(1002, "Error deleting row from database"),
	DATABASE_UPDATE_ERROR(1003, "Error updating row in database"),
	DATABASE_ID_NOT_UNIQUE(1004, "The id entered is already in use"),

	// Syntax Errors
	SYNTAX_ERROR (2002, "Syntax errors"),
	STUDENT_ID_SYNTAX_ERROR (2001, "Student ID must be seven numeric characters"),
	PHONE_NUMBER_SYNTAX_ERROR (2002, "Student ID must only include a space, plus or dash character"),
	INVALID_NUMBER_OF_COURSES (2003, "Student is enrolled in invalid number of courses"),
	STUDENT_NOT_ENROLLED_HAS_CLASSES(2004, "Student is not enrolled yet has classes"),
	ID_NOT_MATCHING_UUID4_PATTERN(2005, "The UUID does not match the v4 UUID string"),



	UNEXPECTED_ERROR(-1, "Unexpected error.");

	int errorCode;
	String message;

	ErrorCode(int errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public int getErrorCode() {
		return errorCode;
	}
}
