package alignWebsite.alignprivate;

import org.hibernate.HibernateException;
import org.junit.*;
import org.mehaexample.asdDemo.enums.*;
import org.mehaexample.asdDemo.model.alignprivate.StudentLogins;
import org.mehaexample.asdDemo.model.alignprivate.Students;
import org.mehaexample.asdDemo.dao.alignprivate.StudentLoginsDao;
import org.mehaexample.asdDemo.dao.alignprivate.StudentsDao;

import java.sql.Timestamp;

public class StudentLoginsDaoTest {
  private static StudentLoginsDao studentLoginsDao;
  private static StudentsDao studentsDao;

  @BeforeClass
  public static void init() {
    studentLoginsDao = new StudentLoginsDao(true);
    studentsDao = new StudentsDao(true);

//    studentLoginsDao = new StudentLoginsDao();
//    studentsDao = new StudentsDao();

    Students newStudent = new Students("0000000", "tomcat@gmail.com", "Tom", "",
            "Cat", Gender.M, "F1", "1111111111",
            "401 Terry Ave", "WA", "Seattle", "98109", Term.FALL, 2015,
            Term.SPRING, 2017,
            EnrollmentStatus.FULL_TIME, Campus.SEATTLE, DegreeCandidacy.MASTERS, null, true);
    Students newStudent2 = new Students("1111111", "jerrymouse@gmail.com", "Jerry", "",
            "Mouse", Gender.M, "F1", "1111111111",
            "225 Terry Ave", "WA", "Seattle", "98109", Term.FALL, 2014,
            Term.SPRING, 2016,
            EnrollmentStatus.FULL_TIME, Campus.SEATTLE, DegreeCandidacy.MASTERS, null, true);
    Students newStudent3 = new Students("2222222", "tomcat3@gmail.com", "Tom", "",
            "Dog", Gender.M, "F1", "1111111111",
            "401 Terry Ave", "WA", "Seattle", "98109", Term.FALL, 2015,
            Term.SPRING, 2017,
            EnrollmentStatus.FULL_TIME, Campus.SEATTLE, DegreeCandidacy.MASTERS, null, true);
    studentsDao.addStudent(newStudent);
    studentsDao.addStudent(newStudent2);
    studentsDao.addStudent(newStudent3);

    StudentLogins studentLogins = new StudentLogins("tomcat3@gmail.com",
            "password",
            "key",
            Timestamp.valueOf("2017-09-23 10:10:10.0"),
            Timestamp.valueOf("2017-09-23 10:10:10.0"),
            false);
    studentLoginsDao.createStudentLogin(studentLogins);
  }

  @AfterClass
  public static void deletePlaceholderDB() {
    studentLoginsDao.deleteStudentLogin("tomcat3@gmail.com");
    studentsDao.deleteStudent("2222222");
    studentsDao.deleteStudent("1111111");
    studentsDao.deleteStudent("0000000");
  }

  @Test(expected = HibernateException.class)
  public void addDuplicate() {
    StudentLogins studentLogins = new StudentLogins("tomcat3@gmail.com",
            "password",
            "key",
            Timestamp.valueOf("2017-09-23 10:10:10.0"),
            Timestamp.valueOf("2017-09-23 10:10:10.0"),
            false);
    studentLoginsDao.createStudentLogin(studentLogins);
  }

  @Test(expected = HibernateException.class)
  public void updateNonExistentStudentLogin() {
    StudentLogins studentLogins = new StudentLogins("tomcattttttt@gmail.com",
            "password",
            "key",
            Timestamp.valueOf("2017-09-23 10:10:10.0"),
            Timestamp.valueOf("2017-09-23 10:10:10.0"),
            false);
    studentLoginsDao.updateStudentLogin(studentLogins);
  }

  @Test(expected = HibernateException.class)
  public void deleteNonExistentStudentLoginEmail() {
    studentLoginsDao.deleteStudentLogin("jdoe@gmail.com");
  }

  @Test(expected = IllegalArgumentException.class)
  public void deleteNullArgument() {
    studentLoginsDao.deleteStudentLogin(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void deleteEmptyArgument() {
    studentLoginsDao.deleteStudentLogin("");
  }

  @Test
  public void StudentLoginIntegrationTest() {
    StudentLogins studentLogins = new StudentLogins("tomcat@gmail.com",
            "password",
            "key",
            Timestamp.valueOf("2017-09-23 10:10:10.0"),
            Timestamp.valueOf("2017-09-23 10:10:10.0"),
            false);
    studentLoginsDao.createStudentLogin(studentLogins);

    StudentLogins temp = studentLoginsDao.findStudentLoginsByEmail("tomcat@gmail.com");
    Assert.assertTrue(temp.getStudentPassword().equals("password"));
    Assert.assertTrue(temp.getRegistrationKey().equals("key"));

    temp.setStudentPassword("password2");
    studentLoginsDao.updateStudentLogin(temp);
    Assert.assertTrue(studentLoginsDao.findStudentLoginsByEmail(
            "tomcat@gmail.com").getStudentPassword().equals("password2"));

    studentLoginsDao.deleteStudentLogin("tomcat@gmail.com");
    Assert.assertTrue(studentLoginsDao.findStudentLoginsByEmail("tomcat@gmail.com") == null);
  }
}
