package com.intuit.core.service;

import com.intuit.common.exception.StudentServicesException;
import com.intuit.core.repository.StudentRepository;
import com.intuit.rest.domain.Student;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class StudentServiceTest {
	@InjectMocks
	private StudentService service;

	@Mock
	private StudentRepository repository;

	private final static String VALID_STUDENT_ID = UUID.randomUUID().toString();

	private final static String INVALID_STUDENT_ID = "1";

	Map<String, String> courses = new HashMap<>();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		courses.put("1", "math");
		courses.put("2", "science");
		courses.put("3", "history");

	}

	@Test
	public void testAddStudent() throws Exception {
		Student s = new Student();
		s.setId(VALID_STUDENT_ID);
		service.addStudent(s);
		verify(repository, times(1)).addStudent(eq(s));
	}

	@Test
	public void testAddStudentValidNumberOfCourses() throws Exception {
		Student s = new Student();
		s.setEnrolled(true);
		s.setClasses(courses);
		s.setId(VALID_STUDENT_ID);
		service.addStudent(s);
		verify(repository, times(1)).addStudent(eq(s));
	}

	@Test(expected= StudentServicesException.class)
	public void testAddStudentInvalidNumberOfCourses() throws Exception {
		Student s = new Student();
		s.setEnrolled(true);
		s.setId(VALID_STUDENT_ID);
		service.addStudent(s);
	}

	@Test(expected=StudentServicesException.class)
	public void testAddUnenrolledStudentWithCourses() throws Exception {
		Student s = new Student();
		s.setId(VALID_STUDENT_ID);
		s.setEnrolled(false);
		s.setClasses(courses);
		service.addStudent(s);
	}

	@Test
	public void testGetStudent() throws Exception {
		service.getStudent(VALID_STUDENT_ID);
		verify(repository, times(1)).getStudent(VALID_STUDENT_ID);
	}

	@Test
	public void testRemoveStudent() throws Exception {
		service.removeStudent(VALID_STUDENT_ID);
		verify(repository, times(1)).removeStudent(VALID_STUDENT_ID);
	}

	@Test
	public void testUpdateStudent() throws Exception {
		Student s = new Student();
		s.setId(VALID_STUDENT_ID);
		service.updateStudent(VALID_STUDENT_ID, s);
		verify(repository, times(1)).updateStudent(eq(VALID_STUDENT_ID),eq(s));
	}

	@Test
	public void testUpdateStudentValidNumberOfCourses() throws Exception {
		Student s = new Student();
		s.setEnrolled(true);
		s.setClasses(courses);
		service.updateStudent(VALID_STUDENT_ID, s);
		verify(repository, times(1)).updateStudent(eq(VALID_STUDENT_ID),eq(s));
	}

	@Test(expected= StudentServicesException.class)
	public void testUpdateStudentInvalidNumberOfCourses() throws Exception {
		Student s = new Student();
		s.setEnrolled(true);
		service.updateStudent(VALID_STUDENT_ID, s);
	}

	@Test(expected= StudentServicesException.class)
	public void testInvalidUuid() throws StudentServicesException {
		Student s = new Student();
		s.setId("INVALID_UUID");
		service.addStudent(s);
	}
}
