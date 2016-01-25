package com.intuit.rest.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by kian on 1/19/16.
 */
public class Student {

	@NotNull
	public Boolean enrolled;

	@NotNull
	public String id;

	public int number;

	public Map<String,String> classes;

	@NotNull
	public String name;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy", timezone = "PDT")
	@NotNull
	@Past
	public Date dob;

	@NotNull
	public String addressLine1;

	public String addressLine2;

	@NotNull
	public String city;

	@NotNull
	public String state;

	@NotNull
	public String zip;

	@NotNull
	public String phoneNumber;

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Map<String, String> getClasses() {
		return classes;
	}

	public void setClasses(Map<String, String> classes) {
		this.classes = classes;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dob);
		cal.add(Calendar.HOUR, 7);
		this.dob = cal.getTime();
	}

	public Boolean getEnrolled() {
		return enrolled;
	}

	public void setEnrolled(Boolean enrolled) {
		this.enrolled = enrolled;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return String.format("%07d", number);
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	private boolean coursesMatch(Map<String,String> first, Map<String,String> second) {
		if(first.size() == second.size()) {
			for(String value : first.values()) {
				if(!second.containsValue(value)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Student student = (Student) o;

		if (enrolled != null ? !enrolled.equals(student.enrolled) : student.enrolled != null) return false;
		if (id != null ? !id.equals(student.id) : student.id != null) return false;
		if (classes != null && !student.coursesMatch(this.classes, ((Student) o).getClasses())) return false;
		if (name != null ? !name.equals(student.name) : student.name != null) return false;
		if (dob != null ? !dob.equals(student.dob) : student.dob != null) return false;
		if (addressLine1 != null ? !addressLine1.equals(student.addressLine1) : student.addressLine1 != null)
			return false;
		if (addressLine2 != null ? !addressLine2.equals(student.addressLine2) : student.addressLine2 != null)
			return false;
		if (city != null ? !city.equals(student.city) : student.city != null) return false;
		if (state != null ? !state.equals(student.state) : student.state != null) return false;
		if (zip != null ? !zip.equals(student.zip) : student.zip != null) return false;
		return phoneNumber != null ? phoneNumber.equals(student.phoneNumber) : student.phoneNumber == null;

	}

	@Override
	public int hashCode() {
		int result = enrolled != null ? enrolled.hashCode() : 0;
		result = 31 * result + (id != null ? id.hashCode() : 0);
		result = 31 * result + number;
		result = 31 * result + (classes != null ? classes.hashCode() : 0);
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (dob != null ? dob.hashCode() : 0);
		result = 31 * result + (addressLine1 != null ? addressLine1.hashCode() : 0);
		result = 31 * result + (addressLine2 != null ? addressLine2.hashCode() : 0);
		result = 31 * result + (city != null ? city.hashCode() : 0);
		result = 31 * result + (state != null ? state.hashCode() : 0);
		result = 31 * result + (zip != null ? zip.hashCode() : 0);
		result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Student{" +
				"addressLine1='" + addressLine1 + '\'' +
				", enrolled=" + enrolled +
				", id='" + id + '\'' +
				", number=" + number +
				", classes=" + classes +
				", name='" + name + '\'' +
				", dob=" + dob +
				", addressLine2='" + addressLine2 + '\'' +
				", city='" + city + '\'' +
				", state='" + state + '\'' +
				", zip='" + zip + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				'}';
	}
}
