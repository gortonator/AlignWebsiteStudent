package alignWebsite.alignprivate;

import org.hibernate.HibernateException;
import org.junit.*;
import org.mehaexample.asdDemo.dao.alignpublic.MultipleValueAggregatedDataDao;
import org.mehaexample.asdDemo.enums.Campus;
import org.mehaexample.asdDemo.enums.DegreeCandidacy;
import org.mehaexample.asdDemo.enums.EnrollmentStatus;
import org.mehaexample.asdDemo.enums.Gender;
import org.mehaexample.asdDemo.enums.Term;
import org.mehaexample.asdDemo.model.alignadmin.StudentBachelorInstitution;
import org.mehaexample.asdDemo.model.alignadmin.TopBachelor;
import org.mehaexample.asdDemo.model.alignprivate.PriorEducations;
import org.mehaexample.asdDemo.model.alignprivate.Students;
import org.mehaexample.asdDemo.model.alignpublic.MultipleValueAggregatedData;
import org.mehaexample.asdDemo.dao.alignprivate.PriorEducationsDao;
import org.mehaexample.asdDemo.dao.alignprivate.StudentsDao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.*;

public class PriorEducationsDaoTest {
  private static PriorEducationsDao priorEducationsDao;
  private static StudentsDao studentsDao;

  @BeforeClass
  public static void init() {
    priorEducationsDao = new PriorEducationsDao(true);
    studentsDao = new StudentsDao(true);

//    priorEducationsDao = new PriorEducationsDao();
//    studentsDao = new StudentsDao();
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
            "401 Terry Ave", "WA", "Boston", "98109",
            Term.FALL, 2014, Term.SPRING, 2016,
            EnrollmentStatus.FULL_TIME, Campus.BOSTON, DegreeCandidacy.MASTERS, null, true);
    studentsDao.addStudent(student);
    studentsDao.addStudent(student2);

    PriorEducations newPriorEducation = new PriorEducations();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    newPriorEducation.setGraduationDate(dateFormat.parse("2015-01-01"));
    newPriorEducation.setGpa(3.50f);
    newPriorEducation.setDegreeCandidacy(DegreeCandidacy.BACHELORS);
    newPriorEducation.setNeuId(student.getNeuId());
    newPriorEducation.setMajorName("Computer Science");
    newPriorEducation.setInstitutionName("University of Washington");

    priorEducationsDao.createPriorEducation(newPriorEducation);
  }

  @After
  public void deleteDatabasePlaceholder() {
    priorEducationsDao.deletePriorEducationById(
            priorEducationsDao.getPriorEducationsByNeuId("001234567").get(0).getPriorEducationId());
    studentsDao.deleteStudent("001234567");
    studentsDao.deleteStudent("111234567");
  }

  @Test
  public void getStudentBachelorDegreesTest() {
    List<MultipleValueAggregatedData> list = priorEducationsDao.getStudentBachelorMajors();
    assertTrue(list.size() == 1);
    assertTrue(list.get(0).getAnalyticTerm().equals(MultipleValueAggregatedDataDao.LIST_OF_BACHELOR_DEGREES));
    assertTrue(list.get(0).getAnalyticKey().equals("Computer Science"));
    assertTrue(list.get(0).getAnalyticValue() == 1);
  }

  @Test
  public void getDegreeListTest() {
    List<MultipleValueAggregatedData> list = priorEducationsDao.getDegreeList();
    assertTrue(list.size() == 1);
    assertTrue(list.get(0).getAnalyticTerm().equals(MultipleValueAggregatedDataDao.LIST_OF_DEGREES));
    assertTrue(list.get(0).getAnalyticKey().equals("BACHELORS"));
    assertTrue(list.get(0).getAnalyticValue() == 1);
  }

  @Test
  public void getTopTenBachelors() throws ParseException {
    List<TopBachelor> temp = priorEducationsDao.getTopTenBachelors(null, null);
    assertTrue(temp.size() == 1);
    assertTrue(temp.get(0).getDegree().equals("Computer Science"));

    // add new prior education
    PriorEducations newPriorEducation = new PriorEducations();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    newPriorEducation.setGraduationDate(dateFormat.parse("2015-01-01"));
    newPriorEducation.setGpa(3.50f);
    newPriorEducation.setDegreeCandidacy(DegreeCandidacy.BACHELORS);
    newPriorEducation.setNeuId("111234567");
    newPriorEducation.setMajorName("farming");
    newPriorEducation.setInstitutionName("Harvard University");
    priorEducationsDao.createPriorEducation(newPriorEducation);

    // test using the newly created prior education
    temp = priorEducationsDao.getTopTenBachelors(null, null);
    assertTrue(temp.size() == 2);
    assertTrue(temp.get(0).getDegree().equals("Computer Science"));
    assertTrue(temp.get(0).getTotalStudents() == 1);
    assertTrue(temp.get(1).getDegree().equals("farming"));
    assertTrue(temp.get(1).getTotalStudents() == 1);

    temp = priorEducationsDao.getTopTenBachelors(Campus.BOSTON, 2016);
    assertTrue(temp.size() == 1);
    assertTrue(temp.get(0).getDegree().equals("farming"));

    temp = priorEducationsDao.getTopTenBachelors(Campus.SEATTLE, 2016);
    assertTrue(temp.size() == 1);
    assertTrue(temp.get(0).getDegree().equals("Computer Science"));

    temp = priorEducationsDao.getTopTenBachelors(Campus.SEATTLE, 2000);
    assertTrue(temp.size() == 0);

    temp = priorEducationsDao.getTopTenBachelors(Campus.CHARLOTTE, 2016);
    assertTrue(temp.isEmpty());

    // delete new prior education
    priorEducationsDao.deletePriorEducationById(
            priorEducationsDao.getPriorEducationsByNeuId("111234567").get(0).getPriorEducationId());
  }

  @Test
  public void getListOfBachelorInstitutionsTest() throws ParseException {
    List<StudentBachelorInstitution> temp = priorEducationsDao.getListOfBachelorInstitutions(null, null);
    assertTrue(temp.size() == 1);

    // add new prior education
    PriorEducations newPriorEducation = new PriorEducations();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    newPriorEducation.setGraduationDate(dateFormat.parse("2015-01-01"));
    newPriorEducation.setGpa(3.50f);
    newPriorEducation.setDegreeCandidacy(DegreeCandidacy.BACHELORS);
    newPriorEducation.setNeuId("111234567");
    newPriorEducation.setMajorName("farming");
    newPriorEducation.setInstitutionName("Harvard University");
    priorEducationsDao.createPriorEducation(newPriorEducation);

    // test using the newly created prior educationgetListOfBachelorInstitutions
    temp = priorEducationsDao.getListOfBachelorInstitutions(null, null);
    assertTrue(temp.size() == 2);
    assertTrue(temp.get(0).getTotalStudents() == 1);
    assertTrue(temp.get(1).getTotalStudents() == 1);

    temp = priorEducationsDao.getListOfBachelorInstitutions(Campus.BOSTON, 2016);
    assertTrue(temp.size() == 1);

    temp = priorEducationsDao.getListOfBachelorInstitutions(Campus.SEATTLE, 2016);
    assertTrue(temp.size() == 1);

    temp = priorEducationsDao.getListOfBachelorInstitutions(Campus.SEATTLE, 2017);
    assertTrue(temp.isEmpty());

    temp = priorEducationsDao.getListOfBachelorInstitutions(Campus.SEATTLE, 2000);
    assertTrue(temp.size() == 0);

    // delete new prior education
    priorEducationsDao.deletePriorEducationById(
            priorEducationsDao.getPriorEducationsByNeuId("111234567").get(0).getPriorEducationId());
  }

  @Test
  public void getPriorEducationByIdTest() {
    int tempId = priorEducationsDao.getPriorEducationsByNeuId("001234567").get(0).getPriorEducationId();
    PriorEducations tempPriorEducation = priorEducationsDao.getPriorEducationById(tempId);
    assertTrue(tempPriorEducation.getNeuId().equals("001234567"));
    assertTrue(tempPriorEducation.getInstitutionName().equals("University of Washington"));
    assertTrue(tempPriorEducation.getGpa() == 3.50f);
    assertTrue(priorEducationsDao.getPriorEducationById(-20) == null);
  }

  @Test
  public void getPriorEducationsByNeuIdTest() {
    List<PriorEducations> listOfPriorEducation = priorEducationsDao.getPriorEducationsByNeuId("001234567");
    assertTrue(listOfPriorEducation.get(0).getInstitutionName().equals("University of Washington"));
    assertTrue(listOfPriorEducation.get(0).getMajorName().equals("Computer Science"));
    assertTrue(priorEducationsDao.getPriorEducationsByNeuId("000000000").isEmpty());
  }

  @Test(expected = HibernateException.class)
  public void deleteNonExistentPriorEducation() {
    priorEducationsDao.deletePriorEducationById(-200);
  }

  @Test(expected = HibernateException.class)
  public void updateNonExistentPriorEducation() {
    PriorEducations newPriorEducation = new PriorEducations();
    newPriorEducation.setPriorEducationId(-300);
    priorEducationsDao.updatePriorEducation(newPriorEducation);
  }

  @Test
  public void createUpdateDeletePriorEducation() throws ParseException {
    PriorEducations newPriorEducation = new PriorEducations();

    Students student = studentsDao.getStudentRecord("111234567");

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    newPriorEducation.setGraduationDate(dateFormat.parse("2015-01-01"));
    newPriorEducation.setGpa(4.00f);
    newPriorEducation.setDegreeCandidacy(DegreeCandidacy.BACHELORS);
    newPriorEducation.setNeuId(student.getNeuId());
    newPriorEducation.setMajorName("Accounting");
    newPriorEducation.setInstitutionName("Stanford University");

    // create new work experience
    priorEducationsDao.createPriorEducation(newPriorEducation);
    PriorEducations foundPriorEducation = priorEducationsDao.getPriorEducationsByNeuId("111234567").get(0);
    assertTrue(foundPriorEducation.getGpa() == 4.00f);
    assertTrue(foundPriorEducation.getInstitutionName().equals("Stanford University"));

    // update found work experience
    foundPriorEducation.setGpa(3.99f);
    priorEducationsDao.updatePriorEducation(foundPriorEducation);
    assertTrue(priorEducationsDao.getPriorEducationsByNeuId("111234567").get(0).getGpa() == 3.99f);

    // delete the work experience
    priorEducationsDao.deletePriorEducationById(foundPriorEducation.getPriorEducationId());
    assertTrue(priorEducationsDao.getPriorEducationById(foundPriorEducation.getPriorEducationId()) == null);
  }
}