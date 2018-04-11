package org.mehaexample.asdDemo.model.alignadmin;

import org.mehaexample.asdDemo.enums.Term;

public class ElectivesAdmin {
  private int electiveId;

  private String neuId;
  private String courseId;
  private String courseName;
  private Term courseTerm;
  private int courseYear;
  private boolean retake;
  private String gpa;
  private boolean plagiarism;

  public ElectivesAdmin(String neuId, String courseId, String courseName, Term courseTerm,
                        int courseYear, boolean retake, String gpa, boolean plagiarism) {
    this.neuId = neuId;
    this.courseId = courseId;
    this.courseName = courseName;
    this.courseTerm = courseTerm;
    this.courseYear = courseYear;
    this.retake = retake;
    this.gpa = gpa;
    this.plagiarism = plagiarism;
  }

  public ElectivesAdmin() { }

  public int getElectiveId() {
    return electiveId;
  }

  public void setElectiveId(int electiveId) {
    this.electiveId = electiveId;
  }

  public String getNeuId() {
    return neuId;
  }

  public void setNeuId(String neuId) {
    this.neuId = neuId;
  }

  public String getCourseId() {
    return courseId;
  }

  public void setCourseId(String courseId) {
    this.courseId = courseId;
  }


  public String getCourseName() {
    return courseName;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }

  public Term getCourseTerm() {
    return courseTerm;
  }

  public void setCourseTerm(Term courseTerm) {
    this.courseTerm = courseTerm;
  }

  public int getCourseYear() {
    return courseYear;
  }

  public void setCourseYear(int courseYear) {
    this.courseYear = courseYear;
  }

  public boolean isRetake() {
    return retake;
  }

  public void setRetake(boolean retake) {
    this.retake = retake;
  }

  public String getGpa() {
    return gpa;
  }

  public void setGpa(String gpa) {
    this.gpa = gpa;
  }

  public boolean isPlagiarism() {
    return plagiarism;
  }

  public void setPlagiarism(boolean plagiarism) {
    this.plagiarism = plagiarism;
  }
}
