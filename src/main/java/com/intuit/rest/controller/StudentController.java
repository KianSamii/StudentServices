package com.intuit.rest.controller;

import com.intuit.core.service.StudentService;
import com.intuit.rest.domain.Student;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by kian on 1/19/16.
 */
@Controller
@RequestMapping(path="api/v1/students")
public class StudentController {
	private static final Logger log = LogManager.getLogger(StudentController.class);

	@Autowired
	private StudentService studentService;

	@RequestMapping(path="/{id}", method= RequestMethod.GET )
	public ResponseEntity<Student> getStudent(@PathVariable String id) throws Exception {
		log.debug("Entering GET Student");
		Student s = studentService.getStudent(id);
		if(s == null) {
			return new ResponseEntity<>(s, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(s, HttpStatus.OK);
	}

	@RequestMapping(path="/{id}", method = RequestMethod.DELETE)
	public ResponseEntity deleteStudent(@PathVariable String id) throws Exception {
		studentService.removeStudent(id);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity addStudent(@Valid @RequestBody Student student) throws Exception {
		log.debug("Entering POST Student");
		studentService.addStudent(student);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", "api/v1/students" + student.getId());

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(path="/{id}", method = RequestMethod.PUT)
	public ResponseEntity updateStudent(@PathVariable String id, @RequestBody @Valid Student student) throws Exception {
		studentService.updateStudent(id, student);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
}
