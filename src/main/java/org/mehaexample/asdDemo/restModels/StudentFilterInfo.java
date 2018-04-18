package org.mehaexample.asdDemo.restModels;

import java.util.List;

public class StudentFilterInfo {
	private List<String> coops;
	private List<String> campuses;
	private List<String> enrollmentYear;
	private List<String> graduationYear;
	private List<String> courses;
	
	public StudentFilterInfo(List<String> coops, List<String> campuses, List<String> enrollmentYear,
			List<String> graduationYear, List<String> courses) {
		super();
		this.coops = coops;
		this.campuses = campuses;
		this.enrollmentYear = enrollmentYear;
		this.graduationYear = graduationYear;
		this.courses = courses;
	}

	public StudentFilterInfo() {
		super();
	}

	public List<String> getCoops() {
		return coops;
	}

	public void setCoops(List<String> coops) {
		this.coops = coops;
	}

	public List<String> getCampuses() {
		return campuses;
	}

	public void setCampuses(List<String> campuses) {
		this.campuses = campuses;
	}

	public List<String> getEnrollmentYear() {
		return enrollmentYear;
	}

	public void setEnrollmentYear(List<String> enrollmentYear) {
		this.enrollmentYear = enrollmentYear;
	}

	public List<String> getGraduationYear() {
		return graduationYear;
	}

	public void setGraduationYear(List<String> graduationYear) {
		this.graduationYear = graduationYear;
	}

	public List<String> getCourses() {
		return courses;
	}

	public void setCourses(List<String> courses) {
		this.courses = courses;
	}
}
