package alignWebsite.alignprivate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.hibernate.HibernateException;
import org.junit.*;
import org.mehaexample.asdDemo.dao.alignprivate.*;
import org.mehaexample.asdDemo.enums.Campus;
import org.mehaexample.asdDemo.enums.DegreeCandidacy;
import org.mehaexample.asdDemo.enums.EnrollmentStatus;
import org.mehaexample.asdDemo.enums.Gender;
import org.mehaexample.asdDemo.enums.Term;
import org.mehaexample.asdDemo.model.alignprivate.*;

import javax.validation.constraints.AssertTrue;

public class StudentsDaoTest {
  private static StudentsDao studentdao;
  private static WorkExperiencesDao workExperiencesDao;
  private static ElectivesDao electivesDao;
  private static CoursesDao coursesDao;
  private static PriorEducationsDao priorEducationsDao;
  private static PrivaciesDao privaciesDao;

  @BeforeClass
  public static void init() {
    studentdao = new StudentsDao(true);
    workExperiencesDao = new WorkExperiencesDao(true);
    electivesDao = new ElectivesDao(true);
    coursesDao = new CoursesDao(true);
    priorEducationsDao = new PriorEducationsDao(true);
    privaciesDao = new PrivaciesDao(true);
  }

  @Before
  public void addRecords() {
    Students newStudent = new Students("0000000", "tomcat@gmail.com", "Tom", "",
            "Cat", Gender.M, "F1", "1111111111",
            "401 Terry Ave", "WA", "Seattle", "98109", Term.FALL, 2015,
            Term.SPRING, 2017,
            EnrollmentStatus.FULL_TIME, Campus.SEATTLE, DegreeCandidacy.MASTERS, null, true);
    Students newStudent2 = new Students("1111111", "jerrymouse@gmail.com", "Jerry", "",
            "Mouse", Gender.M, "F1", "1111111111",
            "225 Terry Ave", "WA", "Seattle", "98109", Term.FALL, 2014,
            Term.SPRING, 2016,
            EnrollmentStatus.FULL_TIME, Campus.BOSTON, DegreeCandidacy.MASTERS, null, true);
    Students newStudent3 = new Students("2222222", "tomcat3@gmail.com", "Tom", "",
            "Dog", Gender.M, "F1", "1111111111",
            "401 Terry Ave", "WA", "Seattle", "98109", Term.FALL, 2015,
            Term.FALL, 2017,
            EnrollmentStatus.DROPPED_OUT, Campus.CHARLOTTE, DegreeCandidacy.MASTERS, null, true);
    studentdao.addStudent(newStudent);
    studentdao.addStudent(newStudent2);
    studentdao.addStudent(newStudent3);
  }

  @After
  public void deleteForDuplicateDatabase() {
    if (studentdao.ifNuidExists("10101010")) {
      studentdao.deleteStudent("10101010");
    }

    studentdao.deleteStudent("0000000");
    studentdao.deleteStudent("1111111");
    studentdao.deleteStudent("2222222");
  }

  // need VPN for this
//  @Test
//  public void deploymentDatabaseConnectionTest() {
//    new StudentsDao();
//  }

  @Test(expected = HibernateException.class)
  public void deleteNonExistentNeuId() {
    studentdao.deleteStudent("0101010101");
  }

  @Test(expected = HibernateException.class)
  public void updateNonExistentStudent() {
    Students student = new Students();
    student.setNeuId("1010101010");
    studentdao.updateStudentRecord(student);
  }

  @Test(expected = IllegalArgumentException.class)
  public void deleteWithNullArgument() {
    studentdao.deleteStudent(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void deleteWithEmptyArgument() {
    studentdao.deleteStudent("");
  }

  @Test(expected = HibernateException.class)
  public void addDuplicateStudent() {
    Students newStudent = new Students("10101010", "tomcat10@gmail.com", "Tom", "",
            "Cat", Gender.M, "F1", "1111111111",
            "401 Terry Ave", "WA", "Seattle", "98109", Term.FALL, 2015,
            Term.SPRING, 2017,
            EnrollmentStatus.FULL_TIME, Campus.SEATTLE, DegreeCandidacy.MASTERS, null, true);
    studentdao.addStudent(newStudent);
    studentdao.addStudent(newStudent);
  }

  @Test
  public void getTotalDropoutStudentsTest() {
    Assert.assertTrue(studentdao.getTotalDropOutStudents() == 1);
  }

  @Test
  public void getTotalStudentInACampusTest() {
    Assert.assertTrue(studentdao.getTotalStudentsInACampus(Campus.SEATTLE) == 1);
    Assert.assertTrue(studentdao.getTotalStudentsInACampus(Campus.BOSTON) == 1);
    Assert.assertTrue(studentdao.getTotalStudentsInACampus(Campus.CHARLOTTE) == 1);
    Assert.assertTrue(studentdao.getTotalStudentsInACampus(Campus.SILICON_VALLEY) == 0);
    Assert.assertTrue(studentdao.getTotalStudents() == 3);
  }

  @Test
  public void getAdminFilteredStudentsTest() throws ParseException {
    Students student1 = studentdao.getStudentRecord("0000000");
    student1.setCampus(Campus.SEATTLE);
    Students student2 = studentdao.getStudentRecord("1111111");
    student2.setCampus(Campus.SEATTLE);
    Students student3 = studentdao.getStudentRecord("2222222");
    student3.setCampus(Campus.SEATTLE);

    studentdao.updateStudentRecord(student1);
    studentdao.updateStudentRecord(student2);
    studentdao.updateStudentRecord(student3);

    // add prior education
    PriorEducations newPriorEducation = new PriorEducations();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    newPriorEducation.setGraduationDate(dateFormat.parse("2015-01-01"));
    newPriorEducation.setGpa(3.50f);
    newPriorEducation.setDegreeCandidacy(DegreeCandidacy.BACHELORS);
    newPriorEducation.setNeuId("0000000");
    newPriorEducation.setMajorName("Computer Science");
    newPriorEducation.setInstitutionName("University of Washington");

    priorEducationsDao.createPriorEducation(newPriorEducation);

    WorkExperiences newWorkExperience = new WorkExperiences();
    dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    newWorkExperience.setStartDate(dateFormat.parse("2017-06-01"));
    newWorkExperience.setEndDate(dateFormat.parse("2017-12-01"));
    newWorkExperience.setCurrentJob(false);
    newWorkExperience.setTitle("Title");
    newWorkExperience.setDescription("Description");
    newWorkExperience.setNeuId("1111111");
    newWorkExperience.setCompanyName("Amazon");
    workExperiencesDao.createWorkExperience(newWorkExperience);

    // no filter case
    Assert.assertTrue(studentdao.getAdminFilteredStudents(new HashMap<String, List<String>>(), 1, 3).size() == 3);

    // first name = Tom
    List<String> firstName = new ArrayList<>();
    firstName.add("Tom");
    List<String> campus = new ArrayList<>();
    campus.add("SEATTLE");
    Map<String, List<String>> filters = new HashMap<>();
    filters.put("firstName", firstName);
    filters.put("campus", campus);
    List<Students> students = studentdao.getAdminFilteredStudents(filters, 1 , 2);
    Assert.assertTrue(students.size() == 2);
    Assert.assertTrue(students.get(0).getNeuId().equals("0000000"));
    Assert.assertTrue(students.get(1).getNeuId().equals("2222222"));

    students = studentdao.getAdminFilteredStudents(filters, 1 , 1);
    Assert.assertTrue(students.size() == 1);
    Assert.assertTrue(students.get(0).getNeuId().equals("0000000"));

    students = studentdao.getAdminFilteredStudents(filters, 2 , 2);
    Assert.assertTrue(students.size() == 1);
    Assert.assertTrue(students.get(0).getNeuId().equals("2222222"));

    students = studentdao.getAdminFilteredStudents(filters, 1 , 10);
    Assert.assertTrue(students.size() == 2);

    List<String> majorName = new ArrayList<>();
    majorName.add("computer science");
    List<String> institutionName = new ArrayList<>();
    institutionName.add("university of washington");
    List<String> gender = new ArrayList<>();
    gender.add("M");
    List<String> degreeCandidacy = new ArrayList<>();
    degreeCandidacy.add("BACHELORS");
    filters.put("majorName", majorName);
    filters.put("institutionName", institutionName);
    filters.put("gender", gender);
    filters.put("degreeCandidacy", degreeCandidacy);
    students = studentdao.getAdminFilteredStudents(filters, 1 , 10);
    Assert.assertTrue(students.size() == 1);
    Assert.assertTrue(students.get(0).getLastName().equals("Cat"));

    // first name = Tom or Jerry, and company = Amazon
    Map<String, List<String>> filters2 = new HashMap<>();
    firstName.add("Jerry");
    List<String> companyName = new ArrayList<>();
    companyName.add("Amazon");
    filters2.put("firstName", firstName);
    filters2.put("companyName", companyName);
    List<Students> students2 = studentdao.getAdminFilteredStudents(filters2, 1, 1);
    Assert.assertTrue(students2.size() == 1);
    Assert.assertTrue(students2.get(0).getNeuId().equals("1111111"));

    // company = Apple
    List<String> companyName2 = new ArrayList<>();
    companyName2.add("Apple");
    Map<String, List<String>> filters3 = new HashMap<>();
    filters3.put("companyName", companyName2);
    Assert.assertTrue(studentdao.getAdminFilteredStudents(filters3, 0, 2).isEmpty());
  }

  @Test
  public void findStudentByEmailTest() {
    Assert.assertTrue(studentdao.getStudentRecordByEmailId("tomcat@gmail.com").getNeuId().equals("0000000"));
    Assert.assertTrue(studentdao.getStudentRecordByEmailId("tomcat4@gmail.com") == null);
  }

  @Test
  public void deleteStudentRecord() {
    Students newStudent = new Students("3333333", "tomcat4@gmail.com", "Tom", "",
            "Cat", Gender.M, "F1", "1111111111",
            "401 Terry Ave", "WA", "Seattle", "98109", Term.FALL, 2015,
            Term.SPRING, 2017,
            EnrollmentStatus.FULL_TIME, Campus.SEATTLE, DegreeCandidacy.MASTERS, null, true);

    studentdao.addStudent(newStudent);
    Assert.assertTrue(studentdao.deleteStudent("3333333"));
    Assert.assertTrue(!studentdao.ifNuidExists("3333333"));
  }

  @Test
  public void getAllStudents() {
    List<Students> students = studentdao.getAllStudents();
    Assert.assertTrue(students.size() == 3);
  }

  @Test
  public void getStudentRecord() {
    Students student = studentdao.getStudentRecord("0000000");
    Assert.assertTrue(studentdao.searchStudentRecord("Tom").size() == 2);
    Assert.assertTrue(studentdao.searchStudentRecord("Tom").get(0).getNeuId().equals("0000000"));
  }

  @Test
  public void countMaleStudents() {
    int males = studentdao.countMaleStudents();
    int females = studentdao.countFemaleStudents();
    List<Students> students = studentdao.getAllStudents();
    Assert.assertTrue(students.size() == males + females);
  }

  @Test
  public void searchSimilarStudents() {
    List<Students> students = studentdao.searchSimilarStudents(DegreeCandidacy.MASTERS);

    for (Students s : students) {
      Assert.assertTrue(s.getDegree().name().equals("MASTERS"));
    }
  }

  @Test
  public void updateStudentRecord() {
    Students student = studentdao.getStudentRecord("0000000");
    Assert.assertTrue(student.getAddress().equals("401 Terry Ave"));

    student.setAddress("225 Terry Ave");
    Assert.assertTrue(studentdao.updateStudentRecord(student));
    student = studentdao.getStudentRecord("0000000");
    Assert.assertTrue(student.getAddress().equals("225 Terry Ave"));
  }


  @Test
  public void getStudentFilteredStudents() throws Exception {
    Privacies privacy1 = new Privacies();
    privacy1.setNeuId("0000000");
    Privacies privacy2 = new Privacies();
    privacy2.setNeuId("1111111");
    Privacies privacy3 = new Privacies();
    privacy3.setNeuId("2222222");
    privacy3.setEmail(true);

    privaciesDao.createPrivacy(privacy1);
    privaciesDao.createPrivacy(privacy2);
    privaciesDao.createPrivacy(privacy3);

    // no filter case
    Assert.assertTrue(studentdao.getStudentFilteredStudents(new HashMap<String, List<String>>(), 1, 20).size() == 3);

    Map<String, List<String>> map = new HashMap<>();

    // filter by start term
    List<String> startTerms = new ArrayList<>();
    startTerms.addAll(Arrays.asList(new String[]{"FALL2015"}));
    List<String> endTerms = new ArrayList<>();
    endTerms.addAll(Arrays.asList(new String[]{"FALL2017"}));
    map.put("startTerm", startTerms);
    map.put("endTerm", endTerms);
    Assert.assertTrue(studentdao.getStudentFilteredStudents(map, 1, 20).size() == 1);
    map.remove("startTerm");
    map.remove("endTerm");

    // filter by campus
    List<String> campuses = new ArrayList<>();
    campuses.addAll(Arrays.asList(new String[]{"SEATTLE", "BOSTON"}));
    map.put("campus", campuses);
    Assert.assertTrue(studentdao.getStudentFilteredStudents(map, 1, 20).size() == 2);
    map.remove("campus");

    // filter by work experience - company name
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    WorkExperiences newWorkExperience = new WorkExperiences("1111111", "Amazon",
            dateFormat.parse("2017-06-01"), dateFormat.parse("2017-12-01"),
            false, "Title", "Description");
    workExperiencesDao.createWorkExperience(newWorkExperience);

    newWorkExperience.setNeuId("2222222");
    newWorkExperience.setCompanyName("Facebook");
    workExperiencesDao.createWorkExperience(newWorkExperience);

    List<String> companies = new ArrayList<>();
    companies.addAll(Arrays.asList(new String[]{"Amazon", "Facebook", "Google"}));
    map.put("companyName", companies);
    Assert.assertTrue(studentdao.getStudentFilteredStudents(map, 1, 20).size() == 2);
    map.remove("companyName");

    // filter by course name
    Courses newCourse1 = new Courses("5800", "Algorithm", "Algorithm");
    Courses newCourse2 = new Courses("5100", "AI", "AI");
    coursesDao.createCourse(newCourse1);
    coursesDao.createCourse(newCourse2);

    Electives newElective1 = new Electives("1111111", "5800", "Course Name", Term.SPRING,
            2018);
    Electives newElective2 = new Electives("2222222", "5100", "Course Name2", Term.SPRING,
            2018);
    electivesDao.addElective(newElective1);
    electivesDao.addElective(newElective2);

    List<String> courses = new ArrayList<>();
    courses.addAll(Arrays.asList(new String[]{"Algorithm", "AI", "Database"}));
    map.put("courseName", courses);
    Assert.assertTrue(studentdao.getStudentFilteredStudents(map, 1, 20).size() == 2);
    map.remove("courseName");

    // filter by company name, course name, campus
    map.put("campus", campuses);
    map.put("companyName", companies);
    map.put("courseName", courses);
    Assert.assertTrue(studentdao.getStudentFilteredStudents(map, 1, 20).size() == 1);

    //check privacy
    for (Students student : studentdao.getStudentFilteredStudents(new HashMap<String, List<String>>(), 1, 10)) {
      if (student.getNeuId().equals("2222222")) {
        Assert.assertTrue(student.getEmail().equals("tomcat3@gmail.com"));
      } else {
        Assert.assertTrue(student.getEmail().isEmpty());
      }
    }

    coursesDao.deleteCourseById("5800");
    coursesDao.deleteCourseById("5100");
  }
}