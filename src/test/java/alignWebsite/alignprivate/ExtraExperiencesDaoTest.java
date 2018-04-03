package alignWebsite.alignprivate;

import org.hibernate.HibernateException;
import org.junit.*;
import org.mehaexample.asdDemo.enums.*;
import org.mehaexample.asdDemo.model.alignprivate.ExtraExperiences;
import org.mehaexample.asdDemo.model.alignprivate.Privacies;
import org.mehaexample.asdDemo.model.alignprivate.Students;
import org.mehaexample.asdDemo.dao.alignprivate.ExtraExperiencesDao;
import org.mehaexample.asdDemo.dao.alignprivate.PrivaciesDao;
import org.mehaexample.asdDemo.dao.alignprivate.StudentsDao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.*;

public class ExtraExperiencesDaoTest {
  private static ExtraExperiencesDao extraExperiencesDao;
  private static StudentsDao studentsDao;
  private static PrivaciesDao privaciesDao;

  @BeforeClass
  public static void init() {
    extraExperiencesDao = new ExtraExperiencesDao(true);
    studentsDao = new StudentsDao(true);
    privaciesDao = new PrivaciesDao(true);

//    extraExperiencesDao = new ExtraExperiencesDao();
//    studentsDao = new StudentsDao();
//    privaciesDao = new PrivaciesDao();
  }

  @Before
  public void addDatabasePlaceholder() throws ParseException {
    Students student = new Students("001234567", "tomcat@gmail.com", "Tom", "",
            "Cat", Gender.M, "F1", "1111111111",
            "401 Terry Ave", "WA", "Seattle", "98109",
            Term.FALL, 2014, Term.SPRING, 2016,
            EnrollmentStatus.FULL_TIME, Campus.SEATTLE, DegreeCandidacy.MASTERS, null, true);
    Students student2 = new Students("111234567", "jerrymouse@gmail.com", "Jerry", "",
            "Mouse", Gender.M, "F1", "1111111111",
            "401 Terry Ave", "WA", "Seattle", "98109",
            Term.FALL, 2014, Term.SPRING, 2016,
            EnrollmentStatus.FULL_TIME, Campus.SEATTLE, DegreeCandidacy.MASTERS, null, true);
    studentsDao.addStudent(student);
    studentsDao.addStudent(student2);

    Privacies privacy = new Privacies();
    privacy.setNeuId("001234567");
    privacy.setPublicId(studentsDao.getStudentRecord("001234567").getPublicId());
    privacy.setExtraExperience(true);
    privaciesDao.createPrivacy(privacy);

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    ExtraExperiences extraExperience = new ExtraExperiences("001234567", "GE", dateFormat.parse("2018-01-01"),
            dateFormat.parse("2018-04-01"), "Electronic Engineer", "I extraed as Electronic Engineer");
    extraExperiencesDao.createExtraExperience(extraExperience);
  }

  @After
  public void deleteDatabasePlaceholder() {
    extraExperiencesDao.deleteExtraExperienceByNeuId("001234567");
    studentsDao.deleteStudent("001234567");
    studentsDao.deleteStudent("111234567");
  }

  @Test(expected = HibernateException.class)
  public void deleteNonExistentExtraExp() {
    extraExperiencesDao.deleteExtraExperienceById(-200);
  }

  @Test(expected = HibernateException.class)
  public void updateNonExistentExtraExp() {
    ExtraExperiences newExtraExperience = new ExtraExperiences();
    newExtraExperience.setExtraExperienceId(-200);
    extraExperiencesDao.updateExtraExperience(newExtraExperience);
  }

  @Test
  public void getExtraExperienceById() {
    int tempId = extraExperiencesDao.getExtraExperiencesByNeuId("001234567").get(0).getExtraExperienceId();
    ExtraExperiences extraExperience1 = extraExperiencesDao.getExtraExperienceById(tempId);
    assertTrue(extraExperience1.getNeuId().equals("001234567"));
    assertTrue(extraExperience1.getCompanyName().equals("GE"));
    ExtraExperiences notFoundExtraExperience = extraExperiencesDao.getExtraExperienceById(-10);
    assertTrue(notFoundExtraExperience == null);
  }

  @Test
  public void getExtraExperiencesByNeuId() {
    List<ExtraExperiences> listOfExtraExperiences = extraExperiencesDao.getExtraExperiencesByNeuId("001234567");
    assertTrue(listOfExtraExperiences.get(0).getCompanyName().equals("GE"));

    assertTrue(extraExperiencesDao.getExtraExperiencesByNeuId("00000000").size() == 0);
  }

  @Test
  public void createDeleteUpdateExtraExperience() throws ParseException {
    ExtraExperiences newExtraExperience = new ExtraExperiences();

    Students student = studentsDao.getStudentRecord("111234567");

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    newExtraExperience.setStartDate(dateFormat.parse("2017-06-01"));
    newExtraExperience.setEndDate(dateFormat.parse("2017-12-01"));
    newExtraExperience.setTitle("Title");
    newExtraExperience.setDescription("Description");
    newExtraExperience.setNeuId(student.getNeuId());
    newExtraExperience.setCompanyName("Intel");

    // create new extra experience
    extraExperiencesDao.createExtraExperience(newExtraExperience);
    ExtraExperiences foundExtraExperience = extraExperiencesDao.getExtraExperiencesByNeuId("111234567").get(0);
    assertTrue(foundExtraExperience.getCompanyName().equals("Intel"));

    // update found extra experience
    foundExtraExperience.setDescription("Description2");
    extraExperiencesDao.updateExtraExperience(foundExtraExperience);
    assertTrue(extraExperiencesDao.getExtraExperiencesByNeuId("111234567").get(0).getDescription().equals("Description2"));

    // delete the extra experience
    extraExperiencesDao.deleteExtraExperienceById(foundExtraExperience.getExtraExperienceId());
    assertTrue(extraExperiencesDao.getExtraExperienceById(foundExtraExperience.getExtraExperienceId()) == null);
  }

  @Test
  public void getExtraExperienceWithPrivacyTest() {
    Assert.assertTrue(extraExperiencesDao.getExtraExperiencesWithPrivacy("001234567").size() == 1);
    Privacies privacy = privaciesDao.getPrivacyByNeuId("001234567");
    privacy.setExtraExperience(false);
    privaciesDao.updatePrivacy(privacy);
    Assert.assertTrue(extraExperiencesDao.getExtraExperiencesWithPrivacy("001234567").size() == 0);
  }
}
