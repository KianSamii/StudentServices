package com.intuit.core;

import com.intuit.common.exception.StudentServicesException;
import com.intuit.rest.domain.Student;

/**
 * Created by kian on 1/19/16.
 */
public interface IStudentRepository {
	public void addStudent(Student student) throws StudentServicesException;

	public Student getStudent(String studentId) throws StudentServicesException;

	public void removeStudent(String studentId) throws StudentServicesException;

	public void updateStudent(String studentId, Student student) throws StudentServicesException;
}
