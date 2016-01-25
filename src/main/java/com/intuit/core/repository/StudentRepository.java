package com.intuit.core.repository;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteConstants;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;
import com.intuit.common.exception.ErrorCode;
import com.intuit.common.exception.StudentServicesException;
import com.intuit.core.IStudentRepository;
import com.intuit.rest.domain.Student;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kian on 1/19/16.
 */
@Repository
public class StudentRepository implements IStudentRepository, InitializingBean {

	@Value("${sqlite.db.path}")
	private String sqliteDatabasePath;

	@Value("${sqlite.db.name}")
	private String sqliteDatabaseName;

	private Path sqliteDatabase;

	private SQLiteConnection db;

	private static final String STUDENTS_TABLE = "students";

	private static final String COURSE_DELIMETER = ",";

	private static final String CREATE_STUDENTS_TABLE = "create table if not exists " + STUDENTS_TABLE + "(" +
			"id INTEGER PRIMARY KEY AUTOINCREMENT," +
			"uuid varchar(255) UNIQUE," +
			"enrolled text," +
			"classes text," +
			"name text," +
			"dob text," +
			"address1 text," +
			"address2 text," +
			"city text," +
			"state text," +
			"zip text," +
			"phoneNumber text);";

	private static final String INSERT_STUDENTS = "INSERT INTO students(" +
			"uuid," +
			"enrolled," +
			"classes," +
			"name," +
			"dob," +
			"address1," +
			"address2," +
			"city," +
			"state," +
			"zip," +
			"phoneNumber)" +
			" values(?,?,?,?,?,?,?,?,?,?,?)";

	private static final String GET_STUDENT = "SELECT " +
			"id," +
			"uuid," +
			"enrolled," +
			"classes," +
			"name," +
			"dob," +
			"address1," +
			"address2," +
			"city," +
			"state," +
			"zip," +
			"phoneNumber" +
			" from students where uuid=?";

	private static final String DELETE_STUDENT = "DELETE from students where uuid=?";

	private static final String UPDATE_STUDENT = "UPDATE " + STUDENTS_TABLE +
			" SET enrolled=?," +
			"classes=?," +
			"name=?," +
			"dob=?," +
			"address1=?," +
			"address2=?," +
			"city=?," +
			"state=?," +
			"zip=?," +
			"phoneNumber=?" +
			" WHERE uuid=?";

	DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

	@Override
	public void addStudent(Student student) throws StudentServicesException {
		this.openDatabaseConnection();
		String combinedCourseString = this.combineCourses(student.getClasses());
		String dateString = df.format(student.getDob());
		try {
			SQLiteStatement st = this.bindParameters(db.prepare(INSERT_STUDENTS), student.getId(), String.valueOf
							(student.getEnrolled()), combinedCourseString, student.getName(), dateString,
					student.getAddressLine1(), student.getAddressLine2(), student.getCity(), student.getState(),
					student.getZip(), student.getPhoneNumber());
			st.stepThrough();

		} catch (SQLiteException e) {
			if (SQLiteConstants.SQLITE_CONSTRAINT_UNIQUE == e.getErrorCode()) {
				throw new StudentServicesException(ErrorCode.DATABASE_ID_NOT_UNIQUE);
			}
			throw new StudentServicesException(ErrorCode.DATABASE_STATEMENT_ERROR);
		}
		this.closeDatabaseConnection();
	}

	@Override
	public Student getStudent(String studentUuid) throws StudentServicesException {
		List<Student> students = new ArrayList<>();
		try {
			this.openDatabaseConnection();
			SQLiteStatement st = db.prepare(GET_STUDENT).bind(1, studentUuid);
			while (st.step()) {
				Student student = new Student();
				student.setNumber(st.columnInt(0));
				student.setId(st.columnString(1));
				// Boolean evaluates to string in sqlite
				student.setEnrolled(Boolean.parseBoolean(st.columnString(2)));
				student.setClasses(this.splitCourses(st.columnString(3)));
				student.setName(st.columnString(4));
				student.setDob(df.parse(st.columnString(5)));
				student.setAddressLine1(st.columnString(6));
				student.setAddressLine2(st.columnString(7));
				student.setCity(st.columnString(8));
				student.setState(st.columnString(9));
				student.setZip(st.columnString(10));
				student.setPhoneNumber(st.columnString(11));
				students.add(student);
			}
		} catch (SQLiteException | ParseException e) {
			throw new StudentServicesException(ErrorCode.DATABASE_STATEMENT_ERROR);
		}
		this.closeDatabaseConnection();
		return students.isEmpty() ? null : students.get(0);
	}

	@Override
	public void removeStudent(String studentUuid) throws StudentServicesException {
		try {
			this.openDatabaseConnection();
			SQLiteStatement st = db.prepare(DELETE_STUDENT).bind(1, studentUuid);
			st.stepThrough();

		} catch (SQLiteException e) {
			throw new StudentServicesException(ErrorCode.DATABASE_DELETE_ERROR);
		}
		this.closeDatabaseConnection();

	}

	@Override
	public void updateStudent(String studentId, Student student) throws StudentServicesException {
		try {
			this.openDatabaseConnection();
			String combinedCourseString = this.combineCourses(student.getClasses());
			SQLiteStatement st = this.bindParameters(db.prepare(UPDATE_STUDENT), String.valueOf(student.getEnrolled
					()), combinedCourseString, student.getName(), df.format(student.getDob()), student.getAddressLine1
					(), student.getAddressLine2(), student.getCity(), student.getState(), student.getZip(), student
					.getPhoneNumber(), studentId);
			st.stepThrough();
		} catch (SQLiteException e) {
			throw new StudentServicesException(ErrorCode.DATABASE_UPDATE_ERROR);
		} finally {
			this.closeDatabaseConnection();
		}

	}

	/**
	 * After the properties are set, ensure table is created.
	 *
	 * @throws Exception - throw the base exception as this will halt startup of service.
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		sqliteDatabase = Paths.get(sqliteDatabasePath, sqliteDatabaseName);
		openDatabaseConnection();
		db.exec(CREATE_STUDENTS_TABLE);
		closeDatabaseConnection();
	}

	private void openDatabaseConnection() throws StudentServicesException {
		try {
			db = new SQLiteConnection(sqliteDatabase.toFile());
			db.open(true);
		} catch (SQLiteException e) {

			throw new StudentServicesException(ErrorCode.DATABASE_ERROR);
		}
	}


	private void closeDatabaseConnection() {
		db.dispose();
	}

	private String combineCourses(Map<String, String> courses) {
		return StringUtils.join(courses.values(), COURSE_DELIMETER);
	}

	private SQLiteStatement bindParameters(SQLiteStatement st, String... parameters) throws StudentServicesException {
		int i = 1;
		try {
			for (String parameter : parameters) {
				st = st.bind(i++, parameter);
			}
			return st;
		} catch (SQLiteException e) {
			throw new StudentServicesException(ErrorCode.DATABASE_STATEMENT_ERROR);
		}
	}

	private Map<String, String> splitCourses(String courses) {
		String[] coursesArray = null;
		if (courses != null) {
			coursesArray = courses.split(COURSE_DELIMETER);
		}
		Map<String, String> coursesMap = new HashMap<>();
		for (int i = 0; i < coursesArray.length; i++) {
			coursesMap.put("course" + (i + 1), coursesArray[i]);
		}
		return coursesMap;

	}

	public void setSqliteDatabaseName(String sqliteDatabaseName) {
		this.sqliteDatabaseName = sqliteDatabaseName;
	}

	public void setSqliteDatabasePath(String sqliteDatabasePath) {
		this.sqliteDatabasePath = sqliteDatabasePath;
	}

	public Path getSqliteDatabase() {
		return sqliteDatabase;
	}
}
