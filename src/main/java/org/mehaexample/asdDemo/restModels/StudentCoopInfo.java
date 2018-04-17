package org.mehaexample.asdDemo.restModels;

import org.mehaexample.asdDemo.enums.Campus;

public class StudentCoopInfo {
  private String Nuid;
  private Campus Campuses;
  private int EnrollmentYear;
  private int GraduationYear;
  private String Coops;

  public StudentCoopInfo(String nuId, Campus campus, int enrollmentYear, int graduationYear, String coops) {
    Nuid = nuId;
    Campuses = campus;
    EnrollmentYear = enrollmentYear;
    GraduationYear = graduationYear;
    Coops = coops;
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
