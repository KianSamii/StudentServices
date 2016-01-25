package com.intuit.core.repository;

import com.intuit.common.exception.StudentServicesException;
import com.intuit.rest.domain.Student;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by kian on 1/23/16.
 */
public class StudentRepositoryTest {
	private StudentRepository repository;

	private static final String DB_NAME = "test.db";

	private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");

	private Student s;

	@Before
	public void setup() throws Exception {
		repository = new StudentRepository();
		String location = Paths.get(StudentRepositoryTest.class.getResource("/sql").toURI()).toAbsolutePath().toString();
		repository.setSqliteDatabaseName(DB_NAME);
		repository.setSqliteDatabasePath(location);
		repository.afterPropertiesSet();
		Files.delete(repository.getSqliteDatabase());
		repository.afterPropertiesSet();
		s = new Student();
		s.setEnrolled(false);
		s.setAddressLine1("TEST");
		s.setId("UNIQUE_ID");
		s.setNumber(25);
		s.setDob(dateFormat.parse("07/11/1989"));
		Map<String, String> classes = new HashMap<>();
		classes.put("Key", "Value");
		s.setClasses(classes);
	}

	@Test
	public void addAndGetStudent() throws Exception {
		repository.addStudent(s);
		Student fromDb = repository.getStudent("UNIQUE_ID");
		assertTrue(s.equals(fromDb));
		assertTrue(fromDb.getNumber().equals("0000001"));
	}

	@Test(expected= StudentServicesException.class)
	public void addStudentsSameId() throws StudentServicesException {
		try {
			repository.addStudent(s);
		} catch (StudentServicesException e) {
			fail();
		}
		repository.addStudent(s);
	}

	@Test
	public void deleteStudent() throws Exception {
		repository.addStudent(s);
		repository.removeStudent(s.getId());
		Student fromDb = repository.getStudent(s.getId());
		assertNull(fromDb);
	}


//	@Test
//	public void updateStudent() throws Exception {
//		repository.addStudent(s);
//		String oldId = s.getId();
//		String newId = "New ID";
//		s.setAddressLine1("New Address");
//		s.setId(newId);
//		repository.updateStudent(oldId, s);
//		Student updatedStudent = repository.getStudent(oldId);
//		Student nonExistantStudent = repository.getStudent(newId);
//		assertEquals("New Address", updatedStudent.getAddressLine1());
//		assertEquals(oldId, updatedStudent.getId());
//		assertNull(nonExistantStudent);
//	}
}