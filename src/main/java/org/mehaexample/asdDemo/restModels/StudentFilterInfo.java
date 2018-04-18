package org.mehaexample.asdDemo.restModels;

import java.util.List;

public class StudentFilterInfo {
	private List<String> Coops;
	private List<String> Campuses;
	private List<String> EnrollmentYear;
	private List<String> GraduationYear;
	private List<String> Courses;
	
	public StudentFilterInfo(List<String> coops, List<String> campuses, List<String> enrollmentYear,
			List<String> graduationYear, List<String> courses) {
		super();
		Coops = coops;
		Campuses = campuses;
		EnrollmentYear = enrollmentYear;
		GraduationYear = graduationYear;
		Courses = courses;
	}

	public StudentFilterInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<String> getCoops() {
		return Coops;
	}

	public void setCoops(List<String> coops) {
		Coops = coops;
	}

	public List<String> getCampuses() {
		return Campuses;
	}

	public void setCampuses(List<String> campuses) {
		Campuses = campuses;
	}

	public List<String> getEnrollmentYear() {
		return EnrollmentYear;
	}

	public void setEnrollmentYear(List<String> enrollmentYear) {
		EnrollmentYear = enrollmentYear;
	}

	public List<String> getGraduationYear() {
		return GraduationYear;
	}

	public void setGraduationYear(List<String> graduationYear) {
		GraduationYear = graduationYear;
	}

	public List<String> getCourses() {
		return Courses;
	}

	public void setCourses(List<String> courses) {
		Courses = courses;
	}

}
