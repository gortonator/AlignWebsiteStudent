package alignWebsite.alignprivate;

import java.util.List;

import org.hibernate.HibernateException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mehaexample.asdDemo.enums.Campus;
import org.mehaexample.asdDemo.enums.DegreeCandidacy;
import org.mehaexample.asdDemo.enums.EnrollmentStatus;
import org.mehaexample.asdDemo.enums.Gender;
import org.mehaexample.asdDemo.enums.Term;
import org.mehaexample.asdDemo.model.alignprivate.Courses;
import org.mehaexample.asdDemo.model.alignprivate.Electives;
import org.mehaexample.asdDemo.model.alignprivate.Privacies;
import org.mehaexample.asdDemo.model.alignprivate.Students;
import org.mehaexample.asdDemo.dao.alignprivate.CoursesDao;
import org.mehaexample.asdDemo.dao.alignprivate.ElectivesDao;
import org.mehaexample.asdDemo.dao.alignprivate.PrivaciesDao;
import org.mehaexample.asdDemo.dao.alignprivate.StudentsDao;

public class ElectiveDaoTest {
  private static ElectivesDao electivesDao;
  private static StudentsDao studentsDao;
  private static CoursesDao coursesDao;
  private static PrivaciesDao privaciesDao;

  @BeforeClass
  public static void init() {
    electivesDao = new ElectivesDao(true);
    studentsDao = new StudentsDao(true);
    coursesDao = new CoursesDao(true);
    privaciesDao = new PrivaciesDao(true);
  }

  /**
   * This is test for adding null record into Electives
   */
  @Test
  public void addNullElectivesTest() {
    Electives Electives = electivesDao.addElective(null);
    Assert.assertNull(Electives);
  }

  /**
   * This is test for updating non existent record
   */
  @Test(expected = HibernateException.class)
  public void updateNonExistentElective() {
    Electives elective = new Electives();
    elective.setElectiveId(-200);
    electivesDao.updateElectives(elective);
  }

  /**
   * This is test for adding elective for non existent student
   */
  @Test(expected = HibernateException.class)
  public void addElectiveWithNonExistentNeuId() {
    Electives elective = new Electives();
    elective.setNeuId("55555");
    electivesDao.addElective(elective);
  }

  /**
   * This is test for deleting non existent record
   */
  @Test(expected = HibernateException.class)
  public void deleteNonExistentElective() {
    electivesDao.deleteElectiveRecord(-200);
  }

  /**
   * This is test for adding new elective record
   */
  @Test
  public void addElectivesTest() {
    String tempId = "1221";

    Students newStudent = new Students(tempId, "tomcat78@gmail.com", "Tom3", "",
            "Cat", Gender.M, "F1", "1111111111",
            "401 Terry Ave", "WA", "Seattle", "98109",
            Term.FALL, 2014, Term.SPRING, 2016,
            EnrollmentStatus.FULL_TIME, Campus.SEATTLE, DegreeCandidacy.MASTERS, null, true);

    studentsDao.addStudent(newStudent);

    Courses newCourse = new Courses(tempId + "", "course2", "course description 2");
    Courses courses = coursesDao.createCourse(newCourse);

    Electives elective = new Electives();
    elective.setNeuId(newStudent.getNeuId());
    elective.setCourseId(newCourse.getCourseId());

    Electives electivesNew = electivesDao.addElective(elective);

    electivesDao.deleteElectiveRecord(electivesNew.getElectiveId());
    coursesDao.deleteCourseById(tempId + "");
    studentsDao.deleteStudent(tempId + "");
  }

  /**
   * This is test for deleting elective record
   */
  @Test
  public void deleteElectivesTest() {
    String tempId = "289";

    List<Electives> experiencesOld = electivesDao.getElectivesByNeuId(tempId);
    int oldSize = experiencesOld.size();

    Students newStudent = new Students(tempId, "tomcat2e1kk3@gmail.com", "Tom3", "",
            "Cat", Gender.M, "F1", "1111111111",
            "401 Terry Ave", "WA", "Seattle", "98109",
            Term.FALL, 2014, Term.SPRING, 2016,
            EnrollmentStatus.FULL_TIME, Campus.SEATTLE, DegreeCandidacy.MASTERS, null, true);

    studentsDao.addStudent(newStudent);

    Courses newCourse = new Courses(tempId + "", "course2", "course description 2");
    Courses courses = coursesDao.createCourse(newCourse);

    Electives elective = new Electives();
    elective.setNeuId(newStudent.getNeuId());
    elective.setCourseId(newCourse.getCourseId());

    Electives electivesNew = electivesDao.addElective(elective);
    electivesDao.deleteElectiveRecord(electivesNew.getElectiveId());

    List<Electives> electivessNew = electivesDao.getElectivesByNeuId(tempId);
    int newSize = electivessNew.size();
    Assert.assertEquals(oldSize, newSize);

    coursesDao.deleteCourseById(tempId + "");
    studentsDao.deleteStudent(tempId + "");
  }

  /**
   * This is test for updating elective record
   */
  @Test
  public void updateElectivesTest() {
    String tempId = "9187";

    Students newStudent = new Students(tempId, "tommcautty@gmail.com", "Tom3", "",
            "Cat", Gender.M, "F1", "1111111111",
            "401 Terry Ave", "WA", "Seattle", "98109",
            Term.FALL, 2014, Term.SPRING, 2016,
            EnrollmentStatus.FULL_TIME, Campus.SEATTLE, DegreeCandidacy.MASTERS, null, true);

    studentsDao.addStudent(newStudent);

    Courses newCourse = new Courses(tempId + "", "course2", "course description 2");
    Courses courses = coursesDao.createCourse(newCourse);

    Electives elective = new Electives();
    elective.setNeuId(newStudent.getNeuId());
    elective.setCourseId(newCourse.getCourseId());

    Electives electivesNew = electivesDao.addElective(elective);

    electivesNew.setCourseYear(2018);
    electivesDao.updateElectives(electivesNew);
    Assert.assertEquals(2018, electivesNew.getCourseYear());

    electivesDao.deleteElectiveRecord(electivesNew.getElectiveId());
    coursesDao.deleteCourseById(tempId + "");
    studentsDao.deleteStudent(tempId + "");
  }

  /**
   * This is test for retrieving elective with privacy control
   */
  @Test
  public void getElectivesWithPrivacyTest() {
    Students newStudent = new Students("11111111", "tomcat2e1kk3@gmail.com", "Tom3", "",
            "Cat", Gender.M, "F1", "1111111111",
            "401 Terry Ave", "WA", "Seattle", "98109",
            Term.FALL, 2014, Term.SPRING, 2016,
            EnrollmentStatus.FULL_TIME, Campus.SEATTLE, DegreeCandidacy.MASTERS, null, true);

    studentsDao.addStudent(newStudent);

    Courses newCourse = new Courses("cs5000", "course2", "course description 2");
    Courses courses = coursesDao.createCourse(newCourse);

    Privacies privacy = new Privacies();
    privacy.setNeuId("11111111");
    privacy.setPublicId(studentsDao.getStudentRecord("11111111").getPublicId());
    privacy.setCourse(true);
    privaciesDao.createPrivacy(privacy);

    Electives elective = new Electives();
    elective.setNeuId(newStudent.getNeuId());
    elective.setCourseName(newCourse.getCourseName());
    elective.setCourseId(newCourse.getCourseId());
    elective.setCourseTerm(Term.SPRING);
    elective.setCourseYear(2017);
    electivesDao.addElective(elective);

    Assert.assertTrue(electivesDao.getElectivesWithPrivacy("11111111").size()==1);

    privacy.setCourse(false);
    privaciesDao.updatePrivacy(privacy);
    Assert.assertTrue(electivesDao.getElectivesWithPrivacy("11111111").size()==0);

    studentsDao.deleteStudent("11111111");
    coursesDao.deleteCourseById("cs5000");
  }
}