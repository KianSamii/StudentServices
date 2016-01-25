package com.intuit.common.exception;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

/*
 *
 * =======================================================================
 *
 * Copyright (c) 2009-2015 Sony Network Entertainment International, LLC. All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Sony Network Entertainment International, LLC.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with
 * Sony Network Entertainment International, LLC.
 *
 * =======================================================================
 *
 * For more information, please see http://www.sony.com/SCA/outline/corporation.shtml
 *
 */
public class StudentServicesControllerAdviceTest {

	private StudentServicesControllerAdvice advice;

	@Before
	public void setup() {
		advice = new StudentServicesControllerAdvice();
	}

	@Test
	public void testRuntimeExceptionHandling() {
		ResponseEntity<StudentServicesErrorInfo> response = advice.handleRuntimeException(new RuntimeException());
		assertEquals("Runtime exception did not match expected", HttpStatus.INTERNAL_SERVER_ERROR,
				response.getStatusCode());
		assertEquals(ErrorCode.UNEXPECTED_ERROR.getMessage(), response.getBody().getMessage());
	}


	@Test
	public void testStudentServicesExceptionHandlingBadRequest() {
		ResponseEntity<StudentServicesErrorInfo> response = advice.handleStudentServicesException(new
				StudentServicesException(ErrorCode
				.STUDENT_ID_SYNTAX_ERROR), null);
		assertEquals("Runtime exception did not match expected", HttpStatus.BAD_REQUEST,
				response.getStatusCode());
		assertEquals(ErrorCode.STUDENT_ID_SYNTAX_ERROR.getMessage(), response.getBody().getMessage());
	}

	@Test
	public void testStudentServicesExceptionHandlingInternalError() {
		ResponseEntity<StudentServicesErrorInfo> response = advice.handleStudentServicesException(new
				StudentServicesException(ErrorCode
				.DATABASE_STATEMENT_ERROR), null);
		assertEquals("Runtime exception did not match expected", HttpStatus.INTERNAL_SERVER_ERROR,
				response.getStatusCode());
		assertEquals(ErrorCode.DATABASE_STATEMENT_ERROR.getMessage(), response.getBody().getMessage());
	}
}

