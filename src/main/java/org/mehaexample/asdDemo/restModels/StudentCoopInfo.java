package org.mehaexample.asdDemo.restModels;

import org.mehaexample.asdDemo.enums.Campus;

public class StudentCoopInfo {
  private String NuId;
  private Campus Campuses;
  private int EnrollmentYear;
  private int GraduationYear;
  private String Coops;

  public StudentCoopInfo(String nuId, Campus campus, int enrollmentYear, int graduationYear, String coops) {
    NuId = nuId;
    Campuses = campus;
    EnrollmentYear = enrollmentYear;
    GraduationYear = graduationYear;
    Coops = coops;
  }

  public String getNuId() {
    return NuId;
  }

  public void setNuId(String nuId) {
    NuId = nuId;
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
