package alignWebsite.alignprivate;

import org.hibernate.HibernateException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mehaexample.asdDemo.enums.*;
import org.mehaexample.asdDemo.model.alignprivate.Privacies;
import org.mehaexample.asdDemo.model.alignprivate.Students;
import org.mehaexample.asdDemo.dao.alignprivate.PrivaciesDao;
import org.mehaexample.asdDemo.dao.alignprivate.StudentsDao;

import java.text.ParseException;

import static org.junit.Assert.*;

public class PrivaciesDaoTest {
  private static StudentsDao studentsDao;
  private static PrivaciesDao privaciesDao;

  @BeforeClass
  public static void init() {
    studentsDao = new StudentsDao(true);
    privaciesDao = new PrivaciesDao(true);
  }

  @Before
  public void addDatabasePlaceholder() throws ParseException {
    Students student1 = new Students("001234567", "tomcat@gmail.com", "Tom", "",
            "Cat", Gender.M, "F1", "1111111111",
            "401 Terry Ave", "WA", "Seattle", "98109",
            Term.FALL, 2014, Term.SPRING, 2016,
            EnrollmentStatus.FULL_TIME, Campus.SEATTLE, DegreeCandidacy.MASTERS, null, true);
    Students student2 = new Students("111234567", "jerrymouse@gmail.com", "Jerry", "",
            "Mouse", Gender.M, "F1", "1111111111",
            "401 Terry Ave", "WA", "Seattle", "98109",
            Term.FALL, 2014, Term.SPRING, 2016,
            EnrollmentStatus.FULL_TIME, Campus.SEATTLE, DegreeCandidacy.MASTERS, null, true);
    studentsDao.addStudent(student1);
    studentsDao.addStudent(student2);

    Privacies privacy1 = new Privacies();
    privacy1.setNeuId("001234567");
    privacy1.setPublicId(studentsDao.getStudentRecord("001234567").getPublicId());
    Privacies privacy2 = new Privacies();
    privacy2.setNeuId("111234567");
    privacy2.setPublicId(studentsDao.getStudentRecord("111234567").getPublicId());
    privacy2.setAddress(true);

    privaciesDao.createPrivacy(privacy1);
    privaciesDao.createPrivacy(privacy2);
  }

  @After
  public void deletePlaceholder() {
    studentsDao.deleteStudent("001234567");
    studentsDao.deleteStudent("111234567");
  }

  @Test(expected = HibernateException.class)
  public void deleteNonExistentPrivacy() {
    privaciesDao.deletePrivacy("00000000");
  }

  @Test(expected = HibernateException.class)
  public void updateNonExistentPrivacy() {
    Privacies privacy = new Privacies();
    privacy.setNeuId("00000000");
    privaciesDao.updatePrivacy(privacy);
  }

  @Test
  public void getPrivacyByNeuId() {
    Privacies privacy = privaciesDao.getPrivacyByNeuId("001234567");
    assertTrue(privacy.getNeuId().equals("001234567"));
    assertTrue(!privacy.isAddress());

    privacy = privaciesDao.getPrivacyByNeuId("111234567");
    assertTrue(privacy.getNeuId().equals("111234567"));
    assertTrue(privacy.isAddress());
  }

  @Test
  public void updatePrivacy() {
    Privacies privacy = privaciesDao.getPrivacyByNeuId("001234567");
    assertTrue(!privacy.isAddress());

    privacy.setAddress(true);
    privaciesDao.updatePrivacy(privacy);

    Privacies privacy_updated = privaciesDao.getPrivacyByNeuId("001234567");
    assertTrue(privacy.isAddress());
  }

  @Test
  public void createDeletePrivacy() {
    Students student1 = new Students("11111111", "mouse@gmail.com", "Tom", "",
            "Cat", Gender.M, "F1", "1111111111",
            "401 Terry Ave", "WA", "Seattle", "98109",
            Term.FALL, 2014, Term.SPRING, 2016,
            EnrollmentStatus.FULL_TIME, Campus.SEATTLE, DegreeCandidacy.MASTERS, null, true);
    studentsDao.addStudent(student1);

    Privacies privacy = new Privacies();
    privacy.setPublicId(studentsDao.getStudentRecord("11111111").getPublicId());
    privacy.setNeuId("11111111");

    privaciesDao.createPrivacy(privacy);
    assertTrue(privaciesDao.ifNuidExists("11111111"));

    privaciesDao.deletePrivacy("11111111");
    assertTrue(!privaciesDao.ifNuidExists("11111111"));

    studentsDao.deleteStudent("11111111");
  }

  @Test
  public void ifNuidExists() {
    assertTrue(privaciesDao.ifNuidExists("001234567"));
    assertTrue(!privaciesDao.ifNuidExists("011234567"));
  }
}
