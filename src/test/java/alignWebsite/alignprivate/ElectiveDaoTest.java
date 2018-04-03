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
import org.mehaexample.asdDemo.model.alignadmin.TopElective;
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

//    electivesDao = new ElectivesDao();
//    studentsDao = new StudentsDao();
//    coursesDao = new CoursesDao();
//    privaciesDao = new PrivaciesDao();
  }

  @Test
  public void addNullElectivesTest() {
    Electives Electives = electivesDao.addElective(null);
    Assert.assertNull(Electives);
  }

  @Test(expected = HibernateException.class)
  public void updateNonExistentElective() {
    Electives elective = new Electives();
    elective.setElectiveId(-200);
    electivesDao.updateElectives(elective);
  }

  @Test(expected = HibernateException.class)
  public void addElectiveWithNonExistentNeuId() {
    Electives elective = new Electives();
    elective.setNeuId("55555");
    electivesDao.addElective(elective);
  }

  @Test(expected = HibernateException.class)
  public void deleteNonExistentElective() {
    electivesDao.deleteElectiveRecord(-200);
  }

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

//    Terms newTerm = new Terms(Term.FALL, tempId, TermType.QUARTER);
//    Terms term = termsDao.addTerm(newTerm);

    Electives elective = new Electives();
    elective.setNeuId(newStudent.getNeuId());
    elective.setCourseId(newCourse.getCourseId());
//    elective.setTerms(newTerm);

    Electives electivesNew = electivesDao.addElective(elective);

    electivesDao.deleteElectiveRecord(electivesNew.getElectiveId());
    coursesDao.deleteCourseById(tempId + "");
//    termsDao.deleteTerm(term.getTermId());
    studentsDao.deleteStudent(tempId + "");
  }

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

  @Test
  public void getTopTenElectivesTest() {
    String tempId = "289";

    Students newStudent = new Students(tempId, "tomcat2e1kk3@gmail.com", "Tom3", "",
            "Cat", Gender.M, "F1", "1111111111",
            "401 Terry Ave", "WA", "Seattle", "98109",
            Term.FALL, 2014, Term.SPRING, 2016,
            EnrollmentStatus.FULL_TIME, Campus.SEATTLE, DegreeCandidacy.MASTERS, null, true);

    studentsDao.addStudent(newStudent);

    Courses newCourse = new Courses(tempId, "course2", "course description 2");
    Courses courses = coursesDao.createCourse(newCourse);
    Courses newCourse2 = new Courses("789", "course1", "course description 1");
    Courses courses2 = coursesDao.createCourse(newCourse2);

    Electives elective = new Electives();
    elective.setNeuId(newStudent.getNeuId());
    elective.setCourseName(newCourse.getCourseName());
    elective.setCourseId(newCourse.getCourseId());
    elective.setCourseTerm(Term.SPRING);
    elective.setCourseYear(2017);

    Electives electivesNew = electivesDao.addElective(elective);

    List<TopElective> temp = electivesDao.getTopTenElectives(Campus.SEATTLE, 2016);
    Assert.assertTrue(temp.size() == 1);
    temp = electivesDao.getTopTenElectives(null, 2016);
    Assert.assertTrue(temp.size() == 1);
    temp = electivesDao.getTopTenElectives(Campus.SEATTLE, null);
    Assert.assertTrue(temp.size() == 1);
    temp = electivesDao.getTopTenElectives(Campus.SEATTLE, 2017);
    Assert.assertTrue(temp.size() == 0);
    temp = electivesDao.getTopTenElectives(Campus.BOSTON, 2017);
    Assert.assertTrue(temp.size() == 0);
    temp = electivesDao.getTopTenElectives(Campus.SEATTLE, 1994);
    Assert.assertTrue(temp.size() == 0);
    temp = electivesDao.getTopTenElectives(null, 1994);
    Assert.assertTrue(temp.size() == 0);

    Electives elective2 = new Electives();
    elective2.setNeuId(newStudent.getNeuId());
    elective.setCourseName(newCourse2.getCourseName());
    elective2.setCourseId(newCourse2.getCourseId());
    elective2.setCourseTerm(Term.SPRING);
    elective2.setCourseYear(2017);
    Electives electivesNew2 = electivesDao.addElective(elective2);

    temp = electivesDao.getTopTenElectives(Campus.SEATTLE, 2016);
    Assert.assertTrue(temp.size() == 2);
    temp = electivesDao.getTopTenElectives(Campus.SEATTLE, 2017);
    Assert.assertTrue(temp.isEmpty());
    temp = electivesDao.getTopTenElectives(Campus.BOSTON, 2017);
    Assert.assertTrue(temp.isEmpty());

    electivesDao.deleteElectiveRecord(electivesNew2.getElectiveId());
    electivesDao.deleteElectiveRecord(electivesNew.getElectiveId());
    coursesDao.deleteCourseById("789");
    coursesDao.deleteCourseById(tempId);
    studentsDao.deleteStudent(tempId);
  }

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