package alignWebsite.alignadmin;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mehaexample.asdDemo.alignWebsite.StudentFacingService;
import org.mehaexample.asdDemo.dao.alignprivate.CoursesDao;
import org.mehaexample.asdDemo.dao.alignprivate.ElectivesDao;
import org.mehaexample.asdDemo.dao.alignprivate.PrivaciesDao;
import org.mehaexample.asdDemo.dao.alignprivate.ProjectsDao;
import org.mehaexample.asdDemo.dao.alignprivate.StudentLoginsDao;
import org.mehaexample.asdDemo.dao.alignprivate.StudentsDao;
import org.mehaexample.asdDemo.dao.alignprivate.WorkExperiencesDao;
import org.mehaexample.asdDemo.dao.alignpublic.UndergraduatesPublicDao;
import org.mehaexample.asdDemo.enums.Campus;
import org.mehaexample.asdDemo.enums.DegreeCandidacy;
import org.mehaexample.asdDemo.enums.EnrollmentStatus;
import org.mehaexample.asdDemo.enums.Gender;
import org.mehaexample.asdDemo.enums.Term;
import org.mehaexample.asdDemo.model.alignprivate.ExtraExperiences;
import org.mehaexample.asdDemo.model.alignprivate.Privacies;
import org.mehaexample.asdDemo.model.alignprivate.Projects;
import org.mehaexample.asdDemo.model.alignprivate.StudentLogins;
import org.mehaexample.asdDemo.model.alignprivate.Students;
import org.mehaexample.asdDemo.model.alignprivate.WorkExperiences;
import org.mehaexample.asdDemo.restModels.EmailToRegister;
import org.mehaexample.asdDemo.restModels.PasswordChangeObject;
import org.mehaexample.asdDemo.restModels.PasswordResetObject;
import org.mehaexample.asdDemo.restModels.StudentProfile;

import junit.framework.Assert;

public class StudentFacingTests {
	private static String NEUIDTEST = "0000000";
	private static String ENDDATE = "2017-01-04";
	private static String STARTDATE = "2018-01-04";

	private static StudentFacingService studentFacing;
	private static StudentsDao studentsDao;
	private static ElectivesDao electivesDao;
	private static CoursesDao coursesDao;
	private static WorkExperiencesDao workExperiencesDao;
	private static PrivaciesDao privaciesDao;
	private static ProjectsDao projectsDao;
	private static StudentLoginsDao studentLoginsDao;

	UndergraduatesPublicDao undergraduatesPublicDao = new UndergraduatesPublicDao(true);

	@BeforeClass
	public static void init() {
		studentsDao = new StudentsDao();
		studentFacing = new StudentFacingService();
		electivesDao = new ElectivesDao();
		coursesDao = new CoursesDao();
		workExperiencesDao = new WorkExperiencesDao();
		studentsDao = new StudentsDao();
		privaciesDao = new PrivaciesDao();
		projectsDao = new ProjectsDao();
		studentLoginsDao = new StudentLoginsDao();
	}

	@After
	public void deleteForDuplicateDatabase() {
		if (studentsDao.ifNuidExists("10101010")) {
			studentsDao.deleteStudent("10101010");
		}

		Response res = studentFacing.getStudentProfile(NEUIDTEST);
		StudentProfile profile = (StudentProfile) res.getEntity();
		List<WorkExperiences> workExperiences = profile.getWorkExperiencesRecord();
		List<Projects> projects= profile.getProjects();

		workExperiencesDao.deleteWorkExperienceByNeuId(NEUIDTEST);
		projectsDao.deleteProjectById(projects.get(0).getProjectId());

		// delete course and experience
		//		Response response = studentFacing.getStudentExtraExperience(NEUIDTEST);
		//		List<ExtraExperiences> extraExperiencesList = (List<ExtraExperiences>) response.getEntity(); 
		//		studentFacing.deleteExtraExperience(NEUIDTEST, extraExperiencesList.get(0).getExtraExperienceId());
		//
		//		electivesDao.deleteElectiveRecord(100);
		//		coursesDao.deleteCourseById("100");

		studentsDao.deleteStudent("0000000");
		studentsDao.deleteStudent("1111111");
		studentsDao.deleteStudent("2222222");
		//		studentLoginsDao.deleteStudentLogin("tomcat@gmail.com");
	}

	@Before
	public void setupAddRecords()throws Exception {
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

		// Adding experience
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
		Date startdate = formatter.parse(STARTDATE);
		Date enddate = formatter.parse(ENDDATE);
		ExtraExperiences extraExperiences = new ExtraExperiences(NEUIDTEST, "companyName", startdate, 
				enddate, "title", "description"	);

		studentFacing.addExtraExperience(NEUIDTEST, extraExperiences);

		// Add courses
		//		Courses newCourse = new Courses("100", "course2", "course description 2");
		//		Electives elective = new Electives();
		//		elective.setNeuId(NEUIDTEST);
		//		elective.setElectiveId(100);
		//		elective.setCourseId(newCourse.getCourseId());
		//
		//		coursesDao.createCourse(newCourse);
		//		electivesDao.addElective(elective);

		Privacies privacy = new Privacies();
		privacy.setNeuId(NEUIDTEST);
		privacy.setPublicId(studentsDao.getStudentRecord(NEUIDTEST).getPublicId());
		privacy.setCoop(true);
		privaciesDao.createPrivacy(privacy);

		WorkExperiences newWorkExperience = new WorkExperiences();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		newWorkExperience.setStartDate(dateFormat.parse("2017-06-01"));
		newWorkExperience.setEndDate(dateFormat.parse("2017-12-01"));
		newWorkExperience.setCurrentJob(false);
		newWorkExperience.setCoop(true);
		newWorkExperience.setTitle("Title");
		newWorkExperience.setDescription("Description");
		newWorkExperience.setNeuId(NEUIDTEST);
		newWorkExperience.setCompanyName("Amazon");
		workExperiencesDao.createWorkExperience(newWorkExperience);

		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		Projects project = new Projects(NEUIDTEST, "Student Website", dateFormat2.parse("2018-01-01"),
				dateFormat2.parse("2018-04-01"), "My Project");
		projectsDao.createProject(project);

	}	

	public void updateStudentRecordTest(){
		Students students = studentsDao.getStudentRecord(NEUIDTEST);
		students.setCity("BOSTON");

		studentFacing.updateStudentRecord(NEUIDTEST, students);
		students = studentsDao.getStudentRecord(NEUIDTEST);

		Assert.assertEquals(students.getCity(), "BOSTON");
	}

	@Test
	public void getStudentExtraExperienceTest(){
		Response response = studentFacing.getStudentExtraExperience(NEUIDTEST);
		List<ExtraExperiences> extraExperiencesList = (List<ExtraExperiences>) response.getEntity();

		Assert.assertEquals(extraExperiencesList.size(), 1);	
	}

	//	@SuppressWarnings("unchecked")
	//	@Test
	//	public void updateExtraExperienceTest(){
	//		Response response = studentFacing.getStudentExtraExperience(NEUIDTEST);
	//		List<ExtraExperiences> extraExperiencesList = (List<ExtraExperiences>) response.getEntity();
	//
	//		Assert.assertEquals(extraExperiencesList.size(), 1);	
	//
	//		extraExperiencesList.get(0).setCompanyName("New Company");
	//		System.out.println(	"com:"+	extraExperiencesList.get(0).getExtraExperienceId());
	//		studentFacing.updateExtraExperience(NEUIDTEST, extraExperiencesList.get(0).getExtraExperienceId());
	//
	//		response = studentFacing.getStudentExtraExperience(NEUIDTEST);
	//		extraExperiencesList = (List<ExtraExperiences>) response.getEntity();
	//		Assert.assertEquals(extraExperiencesList.get(0).getCompanyName(), "New Company");
	//	}

	@Test
	public void deleteExtraExperienceTest(){
		Response response = studentFacing.getStudentExtraExperience(NEUIDTEST);
		List<ExtraExperiences> extraExperiencesList = (List<ExtraExperiences>) response.getEntity();

		Assert.assertEquals(extraExperiencesList.size(), 1);

		response = studentFacing.deleteExtraExperience
				(NEUIDTEST, extraExperiencesList.get(0).getExtraExperienceId());

		Assert.assertEquals(response.getEntity().toString(), "Experience deleted successfully");
	}

	//	@SuppressWarnings("unchecked")
	//	@Test
	//	public void getStudentCoursesTest(){
	//		Response response = studentFacing.getStudentCourses(NEUIDTEST);
	//		ArrayList<String> courses = (ArrayList<String>) response.getEntity();
	//
	//		Assert.assertEquals(courses.size(), 1);
	//
	//	}

	@Test
	public void getStudentWorkExperiencesTest(){
		Response response =  studentFacing.getStudentWorkExperiences(NEUIDTEST);
		List<WorkExperiences> workExperiencesList = (List<WorkExperiences>) response.getEntity();
		Assert.assertEquals(workExperiencesList.size(), 1);
	}

	@Test
	public void updateProjectTest(){
		Response res = studentFacing.getStudentProfile(NEUIDTEST);
		StudentProfile profile = (StudentProfile) res.getEntity();
		Projects project = profile.getProjects().get(0);

		project.setProjectName("NewProject"); 
		Response response =  studentFacing.updateProject(NEUIDTEST, project.getProjectId(), project);

		// get the project again
		res = studentFacing.getStudentProfile(NEUIDTEST);
		profile = (StudentProfile) res.getEntity();
		project = profile.getProjects().get(0);

		Assert.assertEquals(project.getProjectName(), "NewProject");
	}

	@Test
	public void deleteExtsadraExperienceTestadsf(){

	}

	@Test
	public void sendRegistrationEmailTest1(){
		EmailToRegister emailToRegister = new EmailToRegister("test.alignstudent1231@gmail.com");
		Response res = studentFacing.sendRegistrationEmail(emailToRegister);

		String response = (String) res.getEntity();

		Assert.assertEquals("To Register should be an Align Student!" , response); 
	}

	@Test
	public void sendRegistrationEmailTest2(){
		EmailToRegister emailToRegister = new EmailToRegister("");
		Response res = studentFacing.sendRegistrationEmail(emailToRegister);

		String response = (String) res.getEntity();

		Assert.assertEquals("Email Id can't be null or empty" , response); 
	}

	@Test
	public void sendRegistrationEmailTest3(){
		EmailToRegister emailToRegister = new EmailToRegister("tomcat@gmail.com");
		Response res = studentFacing.sendRegistrationEmail(emailToRegister);

		String response = (String) res.getEntity();

		Assert.assertEquals("Registration link sent succesfully to tomcat@gmail.com" , response); 
	}

	@Test
	public void sendEmailForPasswordResetStudentTest1(){
		
		StudentLogins studentLogins = new StudentLogins("tomcat3@gmail.com",
				"password",
				"key",
				Timestamp.valueOf("2017-09-23 10:10:10.0"),
				Timestamp.valueOf("2017-09-23 10:10:10.0"),
				true);
		studentLoginsDao.createStudentLogin(studentLogins);

		PasswordResetObject passwordResetObject = new PasswordResetObject("tomcat3@gmail.com");
		
		Response res = studentFacing.sendEmailForPasswordResetStudent(passwordResetObject);

		String response = (String) res.getEntity();

		Assert.assertEquals("Password Reset link sent succesfully!" , response); 
		
		studentLoginsDao.deleteStudentLogin("tomcat3@gmail.com");
	}

	@Test
	public void sendEmailForPasswordResetStudentTest2(){
		
		StudentLogins studentLogins = new StudentLogins("tomcat3@gmail.com",
				"password",
				"key",
				Timestamp.valueOf("2017-09-23 10:10:10.0"),
				Timestamp.valueOf("2017-09-23 10:10:10.0"),
				false);
		studentLoginsDao.createStudentLogin(studentLogins);

		PasswordResetObject passwordResetObject = new PasswordResetObject("tomcat3@gmail.com");
		
		Response res = studentFacing.sendEmailForPasswordResetStudent(passwordResetObject);

		String response = (String) res.getEntity();

		Assert.assertEquals("Password can't be reset....Please create password and register first: " , response); 
		
		studentLoginsDao.deleteStudentLogin("tomcat3@gmail.com");
	}
	
	@Test
	public void sendEmailForPasswordResetStudentTest3(){
		
		PasswordResetObject passwordResetObject = new PasswordResetObject(null);
		
		Response res = studentFacing.sendEmailForPasswordResetStudent(passwordResetObject);

		String response = (String) res.getEntity();

		Assert.assertEquals("Email Id can't be null" , response); 
	}
	
	@Test
	public void sendEmailForPasswordResetStudentTest4(){
		
		PasswordResetObject passwordResetObject = new PasswordResetObject("meha@gmail.com");
		
		Response res = studentFacing.sendEmailForPasswordResetStudent(passwordResetObject);

		String response = (String) res.getEntity();

		Assert.assertEquals("Email doesn't exist, Please enter a valid Email Id null" , response); 
	}
	
	@Test
	public void changeUserPassword1(){
		
		StudentLogins studentLogins = new StudentLogins("tomcat3@gmail.com",
				"password",
				"key",
				Timestamp.valueOf("2017-09-23 10:10:10.0"),
				Timestamp.valueOf("2017-09-23 10:10:10.0"),
				true);
		studentLoginsDao.createStudentLogin(studentLogins);
		
		System.out.println("password " + studentLogins.getStudentPassword()); 
		
		PasswordChangeObject passwordChangeObject = new PasswordChangeObject("tomcat3@gmail.com","password", "password1");
		
		Response res = studentFacing.changeUserPassword(passwordChangeObject);

		String response = (String) res.getEntity();

		Assert.assertEquals("Incorrect Password" , response); 
		
		studentLoginsDao.deleteStudentLogin("tomcat3@gmail.com");
	}
}