package com.intuit.rest.controller;

import com.intuit.core.service.StudentService;
import com.intuit.rest.domain.Student;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

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
public class StudentControllerTest {
	@InjectMocks
	private StudentController controller;

	@Mock
	private StudentService service;

	private static final String STUDENT_ID = "UNIQUE_STUDENT_ID";

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

	}

	@Test
	public void testGetStudent() throws Exception {
		Student s = new Student();
		when(service.getStudent(anyString())).thenReturn(s);
		ResponseEntity<Student> response = controller.getStudent(STUDENT_ID);
		assertEquals("GET Student status code did not match expected", HttpStatus.OK, response.getStatusCode());
		assertTrue("GET Student body did not match expected", response.getBody() == s);
		verify(service, times(1)).getStudent(eq(STUDENT_ID));
	}

	@Test
	public void testGetStudentNotFound() throws Exception {
		when(service.getStudent(anyString())).thenReturn(null);
		ResponseEntity<Student> response = controller.getStudent(STUDENT_ID);
		assertEquals("Get Student status code did not match expected for no student found", HttpStatus.NOT_FOUND,
				response.getStatusCode());
		assertNull("Get Student body did not match expected for no student found", response.getBody());
	}


	@Test
	public void testDeleteStudent() throws Exception {
		ResponseEntity response = controller.deleteStudent(STUDENT_ID);
		assertEquals("Delete student status code did not match expected", HttpStatus.NO_CONTENT,
				response.getStatusCode());
		verify(service, times(1)).removeStudent(eq(STUDENT_ID));
	}

	@Test
	public void testAddStudent() throws Exception {
		Student s = new Student();
		ResponseEntity response = controller.addStudent(s);
		assertEquals("Status code did not match expected value", HttpStatus.CREATED, response.getStatusCode());
		verify(service, times(1)).addStudent(eq(s));
	}

	@Test
	public void testUpdateStudent() throws Exception {
		Student s = new Student();
		ResponseEntity response = controller.updateStudent(STUDENT_ID, s);
		assertEquals("Status code did not match expected value", HttpStatus.NO_CONTENT, response.getStatusCode());
		verify(service, times(1)).updateStudent(eq(STUDENT_ID), eq(s));
	}
}
