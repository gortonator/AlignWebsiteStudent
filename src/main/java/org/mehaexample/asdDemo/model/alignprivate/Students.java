package org.mehaexample.asdDemo.model.alignprivate;

import java.sql.Blob;

import org.mehaexample.asdDemo.enums.Campus;
import org.mehaexample.asdDemo.enums.DegreeCandidacy;
import org.mehaexample.asdDemo.enums.EnrollmentStatus;
import org.mehaexample.asdDemo.enums.Gender;
import org.mehaexample.asdDemo.enums.Term;

public class Students {
  private String neuId;
  private int publicId;
  private String email;
  private String firstName;
  private String middleName;
  private String lastName;
  private Gender gender;
//  private String race;
  private boolean scholarship;
  private String visa;
  private String phoneNum;
  private String address;
  private String state;
  private String city;
  private String zip;
  private Term entryTerm;
  private int entryYear;
  private Term expectedLastTerm;
  private int expectedLastYear;
  private EnrollmentStatus enrollmentStatus;
  private Campus campus;
  private DegreeCandidacy degree;
  private Blob photo;
  private boolean visible;
  private String linkedin;
  private String facebook;
  private String github;
  private String website;
  private String skills;
  private String summary;

  public Students(String neuId, String email, String firstName, String middleName, String lastName, Gender gender,
                  String visa, String phoneNum, String address, String state, String city, String zip,
                  Term entryTerm, int entryYear, Term expectedLastTerm, int expectedLastYear,
                  EnrollmentStatus status, Campus campus, DegreeCandidacy degree, Blob photo, boolean visible) {
    this.neuId = neuId;
    this.email = email;
    this.firstName = firstName;
    this.middleName = middleName;
    this.lastName = lastName;
    this.gender = gender;
    this.scholarship = false;
    this.visa = visa;
    this.phoneNum = phoneNum;
    this.address = address;
    this.state = state;
    this.city = city;
    this.zip = zip;
    this.entryTerm = entryTerm;
    this.entryYear = entryYear;
    this.expectedLastTerm = expectedLastTerm;
    this.expectedLastYear = expectedLastYear;
    this.enrollmentStatus = status;
    this.campus = campus;
    this.degree = degree;
    this.photo = photo;
    this.visible = visible;
  }

//  public Students(String neuId, String email, String firstName, String middleName, String lastName, Gender gender,
//                  String visa, String phoneNum, String address, String state, String city, String zip,
//                  Term entryTerm, int entryYear, Term expectedLastTerm, int expectedLastYear,
//                  EnrollmentStatus status, Campus campus, DegreeCandidacy degree, Blob photo, boolean visible) {
//    this.neuId = neuId;
//    this.email = email;
//    this.firstName = firstName;
//    this.middleName = middleName;
//    this.lastName = lastName;
//    this.gender = gender;
////    this.race = race;
//    this.scholarship = false;
//    this.visa = visa;
//    this.phoneNum = phoneNum;
//    this.address = address;
//    this.state = state;
//    this.city = city;
//    this.zip = zip;
//    this.entryTerm = entryTerm;
//    this.entryYear = entryYear;
//    this.expectedLastTerm = expectedLastTerm;
//    this.expectedLastYear = expectedLastYear;
//    this.enrollmentStatus = status;
//    this.campus = campus;
//    this.degree = degree;
//    this.photo = photo;
//    this.visible = visible;
//  }

  public Students(String neuId, String email, String firstName, String middleName, String lastName, Gender gender,
                  boolean scholarship, String visa, String phoneNum, String address, String state,
                  String city, String zip, Term entryTerm, int entryYear, Term expectedLastTerm, int expectedLastYear,
                  EnrollmentStatus enrollmentStatus, Campus campus, DegreeCandidacy degree, Blob photo, boolean visible,
                  String linkedin, String facebook, String github, String website, String skills, String summary) {
    this.neuId = neuId;
    this.email = email;
    this.firstName = firstName;
    this.middleName = middleName;
    this.lastName = lastName;
    this.gender = gender;
//    this.race = race;
    this.scholarship = scholarship;
    this.visa = visa;
    this.phoneNum = phoneNum;
    this.address = address;
    this.state = state;
    this.city = city;
    this.zip = zip;
    this.entryTerm = entryTerm;
    this.entryYear = entryYear;
    this.expectedLastTerm = expectedLastTerm;
    this.expectedLastYear = expectedLastYear;
    this.enrollmentStatus = enrollmentStatus;
    this.campus = campus;
    this.degree = degree;
    this.photo = photo;
    this.visible = visible;
    this.linkedin = linkedin;
    this.facebook = facebook;
    this.github = github;
    this.website = website;
    this.skills = skills;
    this.summary = summary;
  }

  public Students() {
    super();
  }

  public String getNeuId() {
    return neuId;
  }

  public void setNeuId(String neuId) {
    this.neuId = neuId;
  }

  public int getPublicId() {
    return publicId;
  }

  public void setPublicId(int publicId) {
    this.publicId = publicId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

//  public String getRace() {
//    return race;
//  }
//
//  public void setRace(String race) {
//    this.race = race;
//  }

  public boolean isScholarship() {
    return scholarship;
  }

  public void setScholarship(boolean scholarship) {
    this.scholarship = scholarship;
  }

  public String getVisa() {
    return visa;
  }

  public void setVisa(String visa) {
    this.visa = visa;
  }

  public String getPhoneNum() {
    return phoneNum;
  }

  public void setPhoneNum(String phoneNum) {
    this.phoneNum = phoneNum;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  public Term getEntryTerm() {
    return entryTerm;
  }

  public void setEntryTerm(Term entryTerm) {
    this.entryTerm = entryTerm;
  }

  public int getEntryYear() {
    return entryYear;
  }

  public void setEntryYear(int entryYear) {
    this.entryYear = entryYear;
  }

  public Term getExpectedLastTerm() {
    return expectedLastTerm;
  }

  public void setExpectedLastTerm(Term expectedLastTerm) {
    this.expectedLastTerm = expectedLastTerm;
  }

  public int getExpectedLastYear() {
    return expectedLastYear;
  }

  public void setExpectedLastYear(int expectedLastYear) {
    this.expectedLastYear = expectedLastYear;
  }

  public EnrollmentStatus getEnrollmentStatus() {
    return enrollmentStatus;
  }

  public void setEnrollmentStatus(EnrollmentStatus enrollmentStatus) {
    this.enrollmentStatus = enrollmentStatus;
  }

  public Campus getCampus() {
    return campus;
  }

  public void setCampus(Campus campus) {
    this.campus = campus;
  }

  public DegreeCandidacy getDegree() {
    return degree;
  }

  public void setDegree(DegreeCandidacy degree) {
    this.degree = degree;
  }

  public Blob getPhoto() {
    return photo;
  }

  public void setPhoto(Blob photo) {
    this.photo = photo;
  }

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  public String getLinkedin() {
    return linkedin;
  }

  public void setLinkedin(String linkedin) {
    this.linkedin = linkedin;
  }

  public String getFacebook() {
    return facebook;
  }

  public void setFacebook(String facebook) {
    this.facebook = facebook;
  }

  public String getGithub() {
    return github;
  }

  public void setGithub(String github) {
    this.github = github;
  }

  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public String getSkills() {
    return skills;
  }

  public void setSkills(String skills) {
    this.skills = skills;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  @Override
  public String toString() {
    return "StudentsPublic{" +
            "neuId='" + neuId + '\'' +
            ", email='" + email + '\'' +
            ", firstName='" + firstName + '\'' +
            ", middleName='" + middleName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", gender=" + gender +
            ", scholarship=" + scholarship +
            ", visa=" + visa +
            ", phoneNum='" + phoneNum + '\'' +
            ", address='" + address + '\'' +
            ", state='" + state + '\'' +
            ", city='" + city + '\'' +
            ", zip='" + zip + '\'' +
            ", enrollmentStatus=" + enrollmentStatus +
            ", campus=" + campus +
            ", degree=" + degree +
            ", photo=" + photo +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Students students = (Students) o;

    if (publicId != students.publicId) return false;
    if (scholarship != students.scholarship) return false;
    if (entryYear != students.entryYear) return false;
    if (expectedLastYear != students.expectedLastYear) return false;
    if (visible != students.visible) return false;
    if (neuId != null ? !neuId.equals(students.neuId) : students.neuId != null) return false;
    if (email != null ? !email.equals(students.email) : students.email != null) return false;
    if (firstName != null ? !firstName.equals(students.firstName) : students.firstName != null) return false;
    if (middleName != null ? !middleName.equals(students.middleName) : students.middleName != null) return false;
    if (lastName != null ? !lastName.equals(students.lastName) : students.lastName != null) return false;
    if (gender != students.gender) return false;
    if (visa != null ? !visa.equals(students.visa) : students.visa != null) return false;
    if (phoneNum != null ? !phoneNum.equals(students.phoneNum) : students.phoneNum != null) return false;
    if (address != null ? !address.equals(students.address) : students.address != null) return false;
    if (state != null ? !state.equals(students.state) : students.state != null) return false;
    if (city != null ? !city.equals(students.city) : students.city != null) return false;
    if (zip != null ? !zip.equals(students.zip) : students.zip != null) return false;
    if (entryTerm != students.entryTerm) return false;
    if (expectedLastTerm != students.expectedLastTerm) return false;
    if (enrollmentStatus != students.enrollmentStatus) return false;
    if (campus != students.campus) return false;
    if (degree != students.degree) return false;
    if (photo != null ? !photo.equals(students.photo) : students.photo != null) return false;
    if (linkedin != null ? !linkedin.equals(students.linkedin) : students.linkedin != null) return false;
    if (facebook != null ? !facebook.equals(students.facebook) : students.facebook != null) return false;
    if (github != null ? !github.equals(students.github) : students.github != null) return false;
    if (website != null ? !website.equals(students.website) : students.website != null) return false;
    if (skills != null ? !skills.equals(students.skills) : students.skills != null) return false;
    return summary != null ? summary.equals(students.summary) : students.summary == null;
  }

  @Override
  public int hashCode() {
    int result = neuId != null ? neuId.hashCode() : 0;
    result = 31 * result + publicId;
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
    result = 31 * result + (middleName != null ? middleName.hashCode() : 0);
    result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
    result = 31 * result + (gender != null ? gender.hashCode() : 0);
    result = 31 * result + (scholarship ? 1 : 0);
    result = 31 * result + (visa != null ? visa.hashCode() : 0);
    result = 31 * result + (phoneNum != null ? phoneNum.hashCode() : 0);
    result = 31 * result + (address != null ? address.hashCode() : 0);
    result = 31 * result + (state != null ? state.hashCode() : 0);
    result = 31 * result + (city != null ? city.hashCode() : 0);
    result = 31 * result + (zip != null ? zip.hashCode() : 0);
    result = 31 * result + (entryTerm != null ? entryTerm.hashCode() : 0);
    result = 31 * result + entryYear;
    result = 31 * result + (expectedLastTerm != null ? expectedLastTerm.hashCode() : 0);
    result = 31 * result + expectedLastYear;
    result = 31 * result + (enrollmentStatus != null ? enrollmentStatus.hashCode() : 0);
    result = 31 * result + (campus != null ? campus.hashCode() : 0);
    result = 31 * result + (degree != null ? degree.hashCode() : 0);
    result = 31 * result + (photo != null ? photo.hashCode() : 0);
    result = 31 * result + (visible ? 1 : 0);
    result = 31 * result + (linkedin != null ? linkedin.hashCode() : 0);
    result = 31 * result + (facebook != null ? facebook.hashCode() : 0);
    result = 31 * result + (github != null ? github.hashCode() : 0);
    result = 31 * result + (website != null ? website.hashCode() : 0);
    result = 31 * result + (skills != null ? skills.hashCode() : 0);
    result = 31 * result + (summary != null ? summary.hashCode() : 0);
    return result;
  }
}