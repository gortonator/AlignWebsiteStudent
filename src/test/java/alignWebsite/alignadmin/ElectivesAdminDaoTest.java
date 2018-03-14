package alignWebsite.alignadmin;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mehaexample.asdDemo.dao.alignprivate.CoursesDao;
import org.mehaexample.asdDemo.dao.alignprivate.StudentsDao;
import org.mehaexample.asdDemo.dao.alignadmin.ElectivesAdminDao;
import org.mehaexample.asdDemo.enums.*;
import org.mehaexample.asdDemo.model.alignprivate.Courses;
import org.mehaexample.asdDemo.model.alignprivate.Students;
import org.mehaexample.asdDemo.model.alignadmin.ElectivesAdmin;

import java.util.List;

public class ElectivesAdminDaoTest {
  private static ElectivesAdminDao electivesAdminDao;
  private static StudentsDao studentsDao;
  private static CoursesDao coursesDao;

  @BeforeClass
  public static void init() {
    electivesAdminDao = new ElectivesAdminDao();
    studentsDao = new StudentsDao();
    coursesDao = new CoursesDao();
  }

  @Test
  public void addNullElectivesTest() {
    ElectivesAdmin Electives = electivesAdminDao.addElective(null);
    Assert.assertNull(Electives);
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

    ElectivesAdmin elective = new ElectivesAdmin();
    elective.setNeuId(newStudent.getNeuId());
    elective.setCourseId(newCourse.getCourseId());
//    elective.setTerms(newTerm);
    elective.setRetake(false);
    elective.setGpa((float) 3.2);
    elective.setPlagiarism(false);

    ElectivesAdmin electivesNew = electivesAdminDao.addElective(elective);

    electivesAdminDao.deleteElectiveRecord(electivesNew.getElectiveId());
    coursesDao.deleteCourseById(tempId + "");
//    termsDao.deleteTerm(term.getTermId());
    studentsDao.deleteStudent(tempId + "");
  }

  @Test
  public void deleteElectivesTest() {
    String tempId = "289";

    List<ElectivesAdmin> experiencesOld = electivesAdminDao.getElectivesByNeuId(tempId);
    int oldSize = experiencesOld.size();

    Students newStudent = new Students(tempId, "tomcat2e1kk3@gmail.com", "Tom3", "",
            "Cat", Gender.M, "F1", "1111111111",
            "401 Terry Ave", "WA", "Seattle", "98109",
            Term.FALL, 2014, Term.SPRING, 2016,
            EnrollmentStatus.FULL_TIME, Campus.SEATTLE, DegreeCandidacy.MASTERS, null, true);

    studentsDao.addStudent(newStudent);

    Courses newCourse = new Courses(tempId + "", "course2", "course description 2");
    Courses courses = coursesDao.createCourse(newCourse);

    ElectivesAdmin elective = new ElectivesAdmin();
    elective.setNeuId(newStudent.getNeuId());
    elective.setCourseId(newCourse.getCourseId());
    elective.setRetake(false);
    elective.setGpa((float) 3.2);
    elective.setPlagiarism(false);

    ElectivesAdmin electivesNew = electivesAdminDao.addElective(elective);
    electivesAdminDao.deleteElectiveRecord(electivesNew.getElectiveId());

    List<ElectivesAdmin> electivessNew = electivesAdminDao.getElectivesByNeuId(tempId);
    int newSize = electivessNew.size();
    Assert.assertEquals(oldSize, newSize);

    coursesDao.deleteCourseById(tempId + "");
//    termsDao.deleteTerm(term.getTermId());
    studentsDao.deleteStudent(tempId + "");
  }

  //
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

    ElectivesAdmin elective = new ElectivesAdmin();
    elective.setNeuId(newStudent.getNeuId());
    elective.setCourseId(newCourse.getCourseId());
    elective.setRetake(false);
    elective.setGpa((float) 3.2);
    elective.setPlagiarism(false);

    ElectivesAdmin electivesNew = electivesAdminDao.addElective(elective);

    electivesNew.setGpa((float) 4.2332);
    electivesAdminDao.updateElectives(electivesNew);
    Assert.assertEquals(electivesNew.getGpa(), ((float) 4.2332), 0.1);

    electivesAdminDao.deleteElectiveRecord(electivesNew.getElectiveId());
    coursesDao.deleteCourseById(tempId + "");
    studentsDao.deleteStudent(tempId + "");
  }
}
