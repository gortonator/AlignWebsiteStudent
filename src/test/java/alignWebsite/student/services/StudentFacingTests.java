package alignWebsite.student.services;

import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mehaexample.asdDemo.alignWebsite.StudentFacing;
import org.mehaexample.asdDemo.dao.alignprivate.StudentsDao;
import org.mehaexample.asdDemo.dao.alignpublic.UndergraduatesPublicDao;
import org.mehaexample.asdDemo.enums.Campus;
import org.mehaexample.asdDemo.enums.DegreeCandidacy;
import org.mehaexample.asdDemo.enums.EnrollmentStatus;
import org.mehaexample.asdDemo.enums.Gender;
import org.mehaexample.asdDemo.enums.Term;
import org.mehaexample.asdDemo.model.alignprivate.Students;
import org.mehaexample.asdDemo.restModels.EmailToRegister;

import junit.framework.Assert;

public class StudentFacingTests {

	private static StudentFacing studentFacing;
	private static StudentsDao studentsDao;
	UndergraduatesPublicDao undergraduatesPublicDao = new UndergraduatesPublicDao(true);

	@BeforeClass
	public static void init() {
		studentsDao = new StudentsDao();
		studentFacing = new StudentFacing();
	}

	@After
	public void deleteForDuplicateDatabase() {
		if (studentsDao.ifNuidExists("10101010")) {
			studentsDao.deleteStudent("10101010");
		}

		studentsDao.deleteStudent("0000000");
		studentsDao.deleteStudent("1111111");
		studentsDao.deleteStudent("2222222");
	}

	@Before
	public void setupAddRecords() {
		Students newStudent = new Students("0000000", "tomcat@gmail.com", "Tom", "",
				"Cat", Gender.M, "F1", "1111111111",
				"401 Terry Ave", "WA", "Seattle", "98109", Term.FALL, 2015,
				Term.SPRING, 2017,
				EnrollmentStatus.FULL_TIME, Campus.SEATTLE, DegreeCandidacy.MASTERS, null, true);
		Students newStudent2 = new Students("1111111", "jerrymouse@gmail.com", "Jerry", "",
				"Mouse", Gender.F, "F1", "1111111111",
				"225 Terry Ave", "MA", "Seattle", "98109", Term.FALL, 2014,
				Term.SPRING, 2016,
				EnrollmentStatus.PART_TIME, Campus.BOSTON, DegreeCandidacy.MASTERS, null, true);
		Students newStudent3 = new Students("2222222", "tomcat3@gmail.com", "Tom", "",
				"Dog", Gender.M, "F1", "1111111111",
				"401 Terry Ave", "WA", "Seattle", "98109", Term.FALL, 2015,
				Term.FALL, 2017,
				EnrollmentStatus.DROPPED_OUT, Campus.CHARLOTTE, DegreeCandidacy.MASTERS, null, true);

		newStudent.setScholarship(true);
		newStudent.setRace("White");
		newStudent2.setRace("Black");
		newStudent3.setRace("White");

		studentsDao.addStudent(newStudent);
		studentsDao.addStudent(newStudent2);
		studentsDao.addStudent(newStudent3);
	}	

	@SuppressWarnings("unchecked")
	@Test
	public void studentCRUDServiceTest(){
		// Step 1: Create a student
		String email = "abc.def31@gmail.com";		
		String nuid = "3121";

		Students newStudent = new Students(nuid, email, "Tom", "",
				"Cat", Gender.M, "F1", "1111111111",
				"401 Terry Ave", "WA", "Seattle", "98109", Term.FALL, 2015,
				Term.SPRING, 2017,
				EnrollmentStatus.FULL_TIME, Campus.SEATTLE, DegreeCandidacy.MASTERS, null, true);

		studentsDao.addStudent(newStudent);

		// Step 2:1- Get the created Student
		Response getStudent = studentFacing.getStudentRecord(nuid);
		Students x = (Students) getStudent.getEntity();
		System.out.println("x nuid=" + x.getNeuId());
		Assert.assertEquals(x.getNeuId(), nuid);

		// Step 2:2- Get the student profile
		//		getStudentProfile

		// Step 3: Update student

		// Step 4: Get the updated student

		// Step 5: now delete the student
		Response deleteStudent = studentFacing.deleteStudentByNuid(nuid);
		Assert.assertEquals("Student deleted successfully", deleteStudent.getEntity().toString());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void registerStudent3(){
		EmailToRegister emailToRegister = new EmailToRegister("test.alignstudent1231@gmail.com");
		Response res = studentFacing.sendRegistrationEmail(emailToRegister);

		String response = (String) res.getEntity();

		Assert.assertEquals("To Register should be an Align Student!" , response); 
	}

	@SuppressWarnings("unchecked")
	@Test
	public void registerStudent4(){
		EmailToRegister emailToRegister = new EmailToRegister("");
		Response res = studentFacing.sendRegistrationEmail(emailToRegister);

		String response = (String) res.getEntity();

		Assert.assertEquals("Email Id can't be null or empty" , response); 
	}

}