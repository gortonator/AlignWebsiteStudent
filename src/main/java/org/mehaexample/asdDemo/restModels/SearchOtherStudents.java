package org.mehaexample.asdDemo.restModels;

import java.util.List;

public class SearchOtherStudents {
	private List<String> companyName;
	private List<String>  courseName;
	private List<String>  startTerm;
	private List<String>  endTerm;
	private List<String>  campus;
	private List<String>  gender;
	
	public SearchOtherStudents() {
		super();
	}

	public SearchOtherStudents(List<String> companyName, List<String> courseName, List<String> startTerm,
			List<String> endTerm, List<String> campus, List<String> gender) {
		super();
		this.companyName = companyName;
		this.courseName = courseName;
		this.startTerm = startTerm;
		this.endTerm = endTerm;
		this.campus = campus;
		this.gender = gender;
	}

	public List<String> getCompanyName() {
		return companyName;
	}

	public void setCompanyName(List<String> companyName) {
		this.companyName = companyName;
	}

	public List<String> getCourseName() {
		return courseName;
	}

	public void setCourseName(List<String> courseName) {
		this.courseName = courseName;
	}

	public List<String> getStartTerm() {
		return startTerm;
	}

	public void setStartTerm(List<String> startTerm) {
		this.startTerm = startTerm;
	}

	public List<String> getEndTerm() {
		return endTerm;
	}

	public void setEndTerm(List<String> endTerm) {
		this.endTerm = endTerm;
	}

	public List<String> getCampus() {
		return campus;
	}

	public void setCampus(List<String> campus) {
		this.campus = campus;
	}

	public List<String> getGender() {
		return gender;
	}

	public void setGender(List<String> gender) {
		this.gender = gender;
	}
}
