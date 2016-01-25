package com.intuit.core.service;

import com.intuit.common.exception.ErrorCode;
import com.intuit.common.exception.StudentServicesException;
import com.intuit.core.IStudentRepository;
import com.intuit.core.IStudentService;
import com.intuit.rest.domain.Student;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * Created by kian on 1/20/16.
 */
@Service
public class StudentService implements IStudentService {
	@Autowired
	private IStudentRepository studentRepository;

	private static final String UUID_V4_PATTERN =
			"[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89ab][a-f0-9]{3}-[0-9a-f]{12}";


	@Override
	public void addStudent(Student student) throws StudentServicesException {
		student.setId(student.getId().toLowerCase());
		this.verifyStudent(student);
		studentRepository.addStudent(student);
	}

	@Override
	public Student getStudent(String studentId) throws StudentServicesException {
		studentId = studentId.toLowerCase();
		return studentRepository.getStudent(studentId);
	}

	@Override
	public void removeStudent(String studentId) throws StudentServicesException {
		studentId = studentId.toLowerCase();
		studentRepository.removeStudent(studentId);

	}

	@Override
	public void updateStudent(String studentId, Student student) throws StudentServicesException {
		studentId = studentId.toLowerCase();
		student.setId(studentId.toLowerCase());
		this.verifyStudent(student);
		studentRepository.updateStudent(studentId, student);
	}

	private void verifyStudent(Student s) throws StudentServicesException {
		// Validate Student class logic
		if (BooleanUtils.toBoolean(s.getEnrolled()) && (CollectionUtils.isEmpty(s.getClasses()) || s.getClasses()
				.size() != 3)) {
			throw new StudentServicesException(ErrorCode.INVALID_NUMBER_OF_COURSES);
		} else if (!BooleanUtils.toBoolean(s.getEnrolled()) && !CollectionUtils.isEmpty(s.getClasses())) {
			throw new StudentServicesException(ErrorCode.STUDENT_NOT_ENROLLED_HAS_CLASSES);
		}

		//UUID v. 4 matcher
		if (!s.getId().matches(UUID_V4_PATTERN)) {
			throw new StudentServicesException(ErrorCode.ID_NOT_MATCHING_UUID4_PATTERN);
		}


	}
}
