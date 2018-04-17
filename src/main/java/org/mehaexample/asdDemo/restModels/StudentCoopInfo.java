package org.mehaexample.asdDemo.restModels;

import org.mehaexample.asdDemo.enums.Campus;

public class StudentCoopInfo {
  private String neuId;
  private Campus campus;
  private int entryYear;
  private int expectedLastYear;
  private String companyName;

  public StudentCoopInfo(String neuId, Campus campus, int entryYear, int expectedLastYear, String companyName) {
    this.neuId = neuId;
    this.campus = campus;
    this.entryYear = entryYear;
    this.expectedLastYear = expectedLastYear;
    this.companyName = companyName;
  }

  public String getNeuId() {
    return neuId;
  }

  public void setNeuId(String neuId) {
    this.neuId = neuId;
  }

  public Campus getCampus() {
    return campus;
  }

  public void setCampus(Campus campus) {
    this.campus = campus;
  }

  public int getEntryYear() {
    return entryYear;
  }

  public void setEntryYear(int entryYear) {
    this.entryYear = entryYear;
  }

  public int getExpectedLastYear() {
    return expectedLastYear;
  }

  public void setExpectedLastYear(int expectedLastYear) {
    this.expectedLastYear = expectedLastYear;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }
}
