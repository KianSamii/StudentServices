package com.intuit.core;

import com.intuit.common.exception.StudentServicesException;
import com.intuit.rest.domain.Student;

/**
 * Created by kian on 1/20/16.
 */
public interface IStudentService {

	public void addStudent(Student student) throws StudentServicesException;

	public Student getStudent(String studentId) throws StudentServicesException;

	public void removeStudent(String studentId) throws StudentServicesException;

	public void updateStudent(String studentId, Student student) throws StudentServicesException;
}
