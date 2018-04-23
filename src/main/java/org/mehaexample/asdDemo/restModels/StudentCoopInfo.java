package org.mehaexample.asdDemo.restModels;

import org.mehaexample.asdDemo.enums.Campus;

public class StudentCoopInfo {
	private String Nuid;
	private Campus Campuses;
	private int EnrollmentYear;
	private int GraduationYear;
	private String Coops;
	private String FirstName;
	private String LastName;

	public StudentCoopInfo(String nuId, Campus campus, int enrollmentYear, int graduationYear, String coops,
			String firstName, String lastName) {
		Nuid = nuId;
		Campuses = campus;
		EnrollmentYear = enrollmentYear;
		GraduationYear = graduationYear;
		Coops = coops;
		FirstName = firstName;
		LastName = lastName;
	}

	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}

	public String getNuId() {
		return Nuid;
	}

	public void setNuId(String nuId) {
		Nuid = nuId;
	}

	public Campus getCampus() {
		return Campuses;
	}

	public void setCampus(Campus campus) {
		Campuses = campus;
	}

	public int getEnrollmentYear() {
		return EnrollmentYear;
	}

	public void setEnrollmentYear(int enrollmentYear) {
		EnrollmentYear = enrollmentYear;
	}

	public int getGraduationYear() {
		return GraduationYear;
	}

	public void setGraduationYear(int graduationYear) {
		GraduationYear = graduationYear;
	}

	public String getCoops() {
		return Coops;
	}

	public void setCoops(String coops) {
		Coops = coops;
	}
}
