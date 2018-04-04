package org.mehaexample.asdDemo.restModels;

import java.util.List;

import org.mehaexample.asdDemo.model.alignprivate.Courses;
import org.mehaexample.asdDemo.model.alignprivate.ExtraExperiences;
import org.mehaexample.asdDemo.model.alignprivate.Privacies;
import org.mehaexample.asdDemo.model.alignprivate.Projects;
import org.mehaexample.asdDemo.model.alignprivate.Students;
import org.mehaexample.asdDemo.model.alignprivate.WorkExperiences;

public class StudentProfile {
	private List<WorkExperiences> workExperiencesRecord;
	private List<Projects> projects;
	private List<ExtraExperiences> extraExperiences;
	private List<Courses> courses;
	private Students studentRecord;
	private Privacies privacies;
	
	public StudentProfile() {
		super();
	}

	public StudentProfile(List<WorkExperiences> workExperiencesRecord, List<Projects> projects,
			List<ExtraExperiences> extraExperiences, List<Courses> courses, Students studentRecord, Privacies privacies) {
		super();
		this.workExperiencesRecord = workExperiencesRecord;
		this.projects = projects;
		this.extraExperiences = extraExperiences;
		this.courses = courses;
		this.studentRecord = studentRecord;
		this.privacies = privacies;
	}

	public Privacies getPrivacy() {
		return privacies;
	}

	public void setPrivacy(Privacies privacies) {
		this.privacies = privacies;
	}

	public List<Courses> getCourses() {
		return courses;
	}

	public void setCourses(List<Courses> courses) {
		this.courses = courses;
	}

	public List<WorkExperiences> getWorkExperiencesRecord() {
		return workExperiencesRecord;
	}

	public void setWorkExperiencesRecord(List<WorkExperiences> workExperiencesRecord) {
		this.workExperiencesRecord = workExperiencesRecord;
	}

	public List<Projects> getProjects() {
		return projects;
	}

	public void setProjects(List<Projects> projects) {
		this.projects = projects;
	}

	public List<ExtraExperiences> getExtraExperiences() {
		return extraExperiences;
	}

	public void setExtraExperiences(List<ExtraExperiences> extraExperiences) {
		this.extraExperiences = extraExperiences;
	}

	public Students getStudentRecord() {
		return studentRecord;
	}

	public void setStudentRecord(Students studentRecord) {
		this.studentRecord = studentRecord;
	}

}
