package com.intuit.common.exception;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ksamii
 * @since 1/20/16.
 */
@ControllerAdvice
public class StudentServicesControllerAdvice {

	final static Logger logger = LogManager.getLogger(StudentServicesControllerAdvice.class);

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<StudentServicesErrorInfo> handleRuntimeException(RuntimeException e) {
		logger.error(e.getLocalizedMessage(), e);
		return new ResponseEntity<>(new StudentServicesErrorInfo(ErrorCode.UNEXPECTED_ERROR),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StudentServicesErrorInfo> handleValidationException(MethodArgumentNotValidException e) {
		StudentServicesErrorInfo errorInfo = new StudentServicesErrorInfo(ErrorCode.SYNTAX_ERROR,
				e.getBindingResult().getFieldErrors());
		return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(StudentServicesException.class)
	public ResponseEntity<StudentServicesErrorInfo> handleStudentServicesException(StudentServicesException e,
	                                                                               HttpServletRequest request) {
		switch (e.getErrorCode()) {
			case STUDENT_ID_SYNTAX_ERROR:
			case DATABASE_DELETE_ERROR:
			case DATABASE_UPDATE_ERROR:
			case PHONE_NUMBER_SYNTAX_ERROR:
			case INVALID_NUMBER_OF_COURSES:
			case STUDENT_NOT_ENROLLED_HAS_CLASSES:
			case DATABASE_ID_NOT_UNIQUE:
			case SYNTAX_ERROR:
			case ID_NOT_MATCHING_UUID4_PATTERN:
				logger.info(e.getErrorCode().getMessage(), e);
				return new ResponseEntity<>(new StudentServicesErrorInfo(e.getErrorCode()), HttpStatus.BAD_REQUEST);

			case DATABASE_ERROR:
			case DATABASE_STATEMENT_ERROR:
			case UNEXPECTED_ERROR:
			default:
				logger.error(e.getLocalizedMessage(), e);
				return new ResponseEntity<>(new StudentServicesErrorInfo(e.getErrorCode()),
						HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
