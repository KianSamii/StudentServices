package integration;

import integration.features.exception.StudentServicesErrorHandler;
import integration.features.exception.StudentServicesTestException;
import com.intuit.rest.domain.Student;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by kian on 1/20/16.
 */
public class StudentStepDefs {
	private static Student student;

	private static Properties properties;

	ResponseEntity lastResponse;

	HttpStatus lastStatus;

	private static Map<String, Object> storage = new HashMap<>();

	static {
		properties = new Properties();
		try {
			properties.load(StudentStepDefs.class.getClassLoader().getResourceAsStream("environment.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Given("^I create a student with parameters:$")
	public void a_student_with_parameters(DataTable studentParameters) throws Exception {
		student = new Student();
		this.reflectivelyUpdateStudent(student, studentParameters);
		if (student.getId() == null) {
			student.setId(UUID.randomUUID().toString());
		}
	}

	@When("I add the student$")
	public void I_add_the_student() {
		RestTemplate template = this.getRestTemplate();
		HttpEntity<Student> request = new HttpEntity<>(student);
		lastResponse = template.exchange(properties.getProperty("host"), HttpMethod.POST, request,
				String.class);
		lastStatus = lastResponse.getStatusCode();
	}

	@When("I add the student expecting an error")
	public void I_add_the_student_expecting_error() {
		try {
			this.I_add_the_student();
		} catch (StudentServicesTestException e) {
			lastStatus = e.getStatus();
		}
	}

	@When("I get the student")
	public void I_get_the_student() {
		RestTemplate template = this.getRestTemplate();
		String host = properties.getProperty("host");
		try {
			lastResponse = template.getForEntity(UriComponentsBuilder.fromHttpUrl(host).pathSegment(student
					.getId()).toUriString(), Student.class);
			lastStatus = lastResponse.getStatusCode();
		} catch (StudentServicesTestException e) {
			lastStatus = e.getStatus();
		}
	}

	@When("I update the student on the server")
	public void I_update_the_student() {
		RestTemplate template = this.getRestTemplate();
		HttpEntity<Student> request = new HttpEntity<>(student);
		String host = properties.getProperty("host");
		lastResponse = template.exchange(UriComponentsBuilder.fromHttpUrl(host).pathSegment(student
						.getId()).toUriString(), HttpMethod.PUT, request,
				String.class);
		lastStatus = lastResponse.getStatusCode();
	}

	@When("I delete the student")
	public void I_delete_the_student() {
		RestTemplate template = this.getRestTemplate();
		HttpEntity<Student> request = new HttpEntity<>(student);
		String host = properties.getProperty("host");
		lastResponse = template.exchange(UriComponentsBuilder.fromHttpUrl(host).pathSegment(student
						.getId()).toUriString(), HttpMethod.DELETE, request,
				String.class);
		lastStatus = lastResponse.getStatusCode();
	}

	@When("I update the student with parameters:")
	public void update_student_with_parameters(DataTable studentParameters) throws Exception {
		reflectivelyUpdateStudent(student, studentParameters);
	}


	@When("I store the last response as (.*)")
	public void I_store_the_last_response(String name) {
		storage.put(name, lastResponse);
	}


	@Then("the response matches the student")
	public void The_response_matches_the_student() {
		Student studentResponse = (Student) lastResponse.getBody();
		assertEquals(studentResponse, student);
	}

	@Then("^the response does not match the student$")
	public void the_response_does_not_match_the_student() {
		Student studentResponse = (Student) lastResponse.getBody();
		assertNotEquals(studentResponse, student);
	}

	@Then("^the status code should be (\\d+)$")
	public void the_status_code_should_be(int status) {
		assertEquals(status, lastStatus.value());
	}

	@Then("^the last response should have a number one higher than the one in (.*)")
	public void higerNumber(String name) {
		ResponseEntity oldResponse = (ResponseEntity) storage.get(name);
		Student oldStudent = (Student) oldResponse.getBody();
		Student newStudent = (Student) lastResponse.getBody();
		assertEquals(Integer.parseInt(newStudent.getNumber()) - 1, Integer.parseInt(oldStudent.getNumber()));
	}

	@Then("^the last response should have a number the same as the one in (.*)$")
	public void equalNumber(String name) {
		ResponseEntity oldResponse = (ResponseEntity) storage.get(name);
		Student oldStudent = (Student) oldResponse.getBody();
		Student newStudent = (Student) lastResponse.getBody();
		assertEquals(Integer.parseInt(newStudent.getNumber()), Integer.parseInt(oldStudent.getNumber()));
	}

	@Then("^the student number should be seven characters long")
	public void student_number_seven_characters() {
		Student s = (Student) lastResponse.getBody();
		assertEquals(s.getNumber().length(), 7);
	}

	private RestTemplate getRestTemplate() {
		RestTemplate template = new RestTemplate();
		StudentServicesErrorHandler errorHandler = new StudentServicesErrorHandler();
		template.setErrorHandler(errorHandler);
		return template;
	}


	private void reflectivelyUpdateStudent(Student s, DataTable studentParameters) throws
			NoSuchFieldException, IllegalAccessException, ParseException {
		Map<String, String> studentParameterMap = studentParameters.asMap(String.class, String.class);
		Class studentClass = s.getClass();
		for (String key : studentParameterMap.keySet()) {
			String value = studentParameterMap.get(key);
			Field field;
			field = studentClass.getField(key);
			if (field.getType().equals(Boolean.class)) {
				field.set(s, Boolean.parseBoolean(value));
			} else if (field.getType().equals(Date.class)) {
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				field.set(s, df.parse(value));
			} else if (field.getType().getName().equals("int")) {
				field.set(s, Integer.parseInt(value));
			} else if (field.getType().equals(Map.class)) {
				Map<String, String> classes = new HashMap<>();
				int i = 1;
				for (String course : Arrays.asList(value.split(","))) {
					classes.put("class" + i++, course);
				}
				field.set(s, classes);
			} else {
				field.set(s, studentParameterMap.get(key));
			}
		}
	}

}
