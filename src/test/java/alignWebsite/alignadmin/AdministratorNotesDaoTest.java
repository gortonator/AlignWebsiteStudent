package alignWebsite.alignadmin;

import org.hibernate.HibernateException;
import org.junit.*;
import org.mehaexample.asdDemo.dao.alignadmin.AdministratorNotesDao;
import org.mehaexample.asdDemo.dao.alignadmin.AdministratorsDao;
import org.mehaexample.asdDemo.enums.Campus;
import org.mehaexample.asdDemo.enums.DegreeCandidacy;
import org.mehaexample.asdDemo.enums.EnrollmentStatus;
import org.mehaexample.asdDemo.enums.Gender;
import org.mehaexample.asdDemo.enums.Term;
import org.mehaexample.asdDemo.model.alignadmin.AdministratorNotes;
import org.mehaexample.asdDemo.model.alignadmin.Administrators;
import org.mehaexample.asdDemo.model.alignprivate.Students;
import org.mehaexample.asdDemo.dao.alignprivate.StudentsDao;

import java.util.List;

public class AdministratorNotesDaoTest {
  private static AdministratorNotesDao administratorNotesDao;
  private static StudentsDao studentsDao;
  private static AdministratorsDao adminDao;

  @BeforeClass
  public static void init() {
    studentsDao = new StudentsDao(true);
    adminDao = new AdministratorsDao(true);
    administratorNotesDao = new AdministratorNotesDao(true);
  }

  @Before
  public void addDatabasePlaceholder() {
    Students student = new Students("001234567", "tomcat@gmail.com", "Tom", "",
            "Cat", Gender.M, "F1", "1111111111",
            "401 Terry Ave", "WA", "Seattle", "98109",
            Term.FALL, 2014, Term.SPRING, 2016,
            EnrollmentStatus.FULL_TIME, Campus.SEATTLE, DegreeCandidacy.MASTERS, null, true);
    Administrators newAdministrator = new Administrators("123456789", "john.stewart@gmail.com",
            "John", "Main", "Stewart");
    adminDao.addAdministrator(newAdministrator);
    studentsDao.addStudent(student);
  }

  @After
  public void deleteDatabasePlaceholder() {
    adminDao.deleteAdministrator("123456789");
    studentsDao.deleteStudent("001234567");
  }

  @Test(expected = HibernateException.class)
  public void updateNonExistentAdministratorNoteTest() {
    AdministratorNotes note = new AdministratorNotes("111111", "000000", "TEST", "TEST");
    note.setAdministratorNoteId(-200);
    administratorNotesDao.updateAdministratorNote(note);
  }

  @Test(expected = HibernateException.class)
  public void deleteNonExistentAdministratorNoteTest() {
    AdministratorNotes note = new AdministratorNotes("111111", "000000", "TEST", "TEST");
    administratorNotesDao.deleteAdministratorNoteRecord(-20);
  }

  @Test
  public void getAdministratorNoteRecordTest() {
    Students student = studentsDao.getStudentRecord("001234567");
    Administrators admin = adminDao.getAdministratorRecord("123456789");
    AdministratorNotes note = new AdministratorNotes(student.getNeuId(), admin.getAdministratorNeuId(), "TEST", "TEST");
    note = administratorNotesDao.addAdministratorNoteRecord(note);
    Assert.assertTrue(administratorNotesDao.getAdministratorNoteById(note.getAdministratorNoteId())
            .getTitle().equals("TEST"));

    List<AdministratorNotes> notes = administratorNotesDao.getAdministratorNoteRecordByNeuId("001234567");
    for (AdministratorNotes n : notes)
      Assert.assertTrue(n.getTitle().equals("TEST"));

    notes = administratorNotesDao.getAdministratorNoteRecordByAdminNeuId("123456789");
    for (AdministratorNotes n : notes)
      Assert.assertTrue(n.getTitle().equals("TEST"));

    administratorNotesDao.deleteAdministratorNoteRecord(note.getAdministratorNoteId());
  }

  @Test
  public void addAndUpdateAdministratorNoteRecordTest() {
    Students student = studentsDao.getStudentRecord("001234567");
    Administrators admin = adminDao.getAdministratorRecord("123456789");
    AdministratorNotes note = new AdministratorNotes(student.getNeuId(), admin.getAdministratorNeuId(), "TEST", "TEST");
    administratorNotesDao.addAdministratorNoteRecord(note);

    note.setTitle("TEST2");
    int noteId = note.getAdministratorNoteId();
    administratorNotesDao.updateAdministratorNote(note);
    note = administratorNotesDao.getAdministratorNoteById(noteId);
    Assert.assertTrue(note.getTitle().equals("TEST2"));

    Assert.assertTrue(administratorNotesDao.ifNuidExists("001234567"));
    administratorNotesDao.deleteAdministratorNoteRecord(note.getAdministratorNoteId());
    Assert.assertTrue(!administratorNotesDao.ifNuidExists("001234567"));
  }

  @Test
  public void deleteAdministratorNoteRecordTest() {
    Students student = studentsDao.getStudentRecord("001234567");
    Administrators admin = adminDao.getAdministratorRecord("123456789");
    AdministratorNotes note = new AdministratorNotes(student.getNeuId(), admin.getAdministratorNeuId(), "TEST", "TEST");
    administratorNotesDao.addAdministratorNoteRecord(note);
    administratorNotesDao.deleteAdministratorNoteRecord(note.getAdministratorNoteId());
    Assert.assertTrue(!administratorNotesDao.ifNuidExists("001234567"));
  }

  @Test
  public void ifNuidExistsTest() {
    Students student = studentsDao.getStudentRecord("001234567");
    Administrators admin = adminDao.getAdministratorRecord("123456789");
    AdministratorNotes note = new AdministratorNotes(student.getNeuId(), admin.getAdministratorNeuId(), "TEST", "TEST");
    administratorNotesDao.addAdministratorNoteRecord(note);
    Assert.assertTrue(administratorNotesDao.ifNuidExists("001234567"));
    administratorNotesDao.deleteAdministratorNoteRecord(note.getAdministratorNoteId());
    Assert.assertTrue(!administratorNotesDao.ifNuidExists("001234567"));
  }
}