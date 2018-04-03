package alignWebsite.student.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.Spring;
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
import org.mehaexample.asdDemo.model.alignprivate.ExtraExperiences;
import org.mehaexample.asdDemo.model.alignprivate.Students;
import org.mehaexample.asdDemo.restModels.EmailToRegister;
import org.mehaexample.asdDemo.restModels.StudentProfile;

import junit.framework.Assert;

public class StudentFacingTests {
	private static String NEUIDTEST = "0000000";
	private static String ENDDATE = "2017-01-04";
	private static String STARTDATE = "2018-01-04";

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
	public void getStudentProfileTest(){
		Response studentProfileResponse = studentFacing.getStudentProfile(NEUIDTEST);
		StudentProfile studentProfile = (StudentProfile) studentProfileResponse.getEntity();
		Assert.assertEquals(studentProfile.getStudentRecord().getNeuId(), NEUIDTEST);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void updateStudentRecordTest(){
		Students students = studentsDao.getStudentRecord(NEUIDTEST);
		students.setCity("BOSTON");
		
		studentFacing.updateStudentRecord(NEUIDTEST, students);
		students = studentsDao.getStudentRecord(NEUIDTEST);
		
		Assert.assertEquals(students.getCity(), "BOSTON");
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void addExtraExperienceTest() throws Exception{
		Students students = studentsDao.getStudentRecord(NEUIDTEST);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
		Date startdate = formatter.parse(STARTDATE);
		Date enddate = formatter.parse(ENDDATE);
		ExtraExperiences extraExperiences = new ExtraExperiences(NEUIDTEST, "companyName", startdate, 
				enddate, "title", "description"	);
		
		studentFacing.addExtraExperience(NEUIDTEST, extraExperiences);
		
		// now get the profile
		Response studentProfileResponse = studentFacing.getStudentProfile(NEUIDTEST);
		StudentProfile studentProfile = (StudentProfile) studentProfileResponse.getEntity();
		
		Assert.assertEquals(studentProfile.getStudentRecord().getNeuId(), NEUIDTEST);
		List<ExtraExperiences> ex = studentProfile.getExtraExperiences();
		Assert.assertEquals(ex.size(), 1);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void updateStudentRecordTestd(){
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void updateStudentRecdordTest(){
		
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