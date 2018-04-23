package StudentServiceTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mehaexample.asdDemo.alignWebsite.StudentFacingService;
import org.mehaexample.asdDemo.dao.alignprivate.CoursesDao;
import org.mehaexample.asdDemo.dao.alignprivate.ElectivesDao;
import org.mehaexample.asdDemo.dao.alignprivate.ExtraExperiencesDao;
import org.mehaexample.asdDemo.dao.alignprivate.PhotosDao;
import org.mehaexample.asdDemo.dao.alignprivate.PrivaciesDao;
import org.mehaexample.asdDemo.dao.alignprivate.ProjectsDao;
import org.mehaexample.asdDemo.dao.alignprivate.StudentLoginsDao;
import org.mehaexample.asdDemo.dao.alignprivate.StudentsDao;
import org.mehaexample.asdDemo.dao.alignprivate.WorkExperiencesDao;
import org.mehaexample.asdDemo.dao.alignpublic.StudentsPublicDao;
import org.mehaexample.asdDemo.dao.alignpublic.UndergraduatesPublicDao;
import org.mehaexample.asdDemo.enums.Campus;
import org.mehaexample.asdDemo.enums.DegreeCandidacy;
import org.mehaexample.asdDemo.enums.EnrollmentStatus;
import org.mehaexample.asdDemo.enums.Gender;
import org.mehaexample.asdDemo.enums.Term;
import org.mehaexample.asdDemo.model.alignadmin.LoginObject;
import org.mehaexample.asdDemo.model.alignprivate.Courses;
import org.mehaexample.asdDemo.model.alignprivate.Electives;
import org.mehaexample.asdDemo.model.alignprivate.Photos;
import org.mehaexample.asdDemo.model.alignprivate.Privacies;
import org.mehaexample.asdDemo.model.alignprivate.StudentLogins;
import org.mehaexample.asdDemo.model.alignprivate.Students;
import org.mehaexample.asdDemo.model.alignprivate.WorkExperiences;
import org.mehaexample.asdDemo.model.alignpublic.StudentsPublic;
import org.mehaexample.asdDemo.restModels.EmailToRegister;
import org.mehaexample.asdDemo.restModels.ExtraExperienceObject;
import org.mehaexample.asdDemo.restModels.PasswordChangeObject;
import org.mehaexample.asdDemo.restModels.PasswordCreateObject;
import org.mehaexample.asdDemo.restModels.PasswordResetObject;
import org.mehaexample.asdDemo.restModels.ProjectObject;
import org.mehaexample.asdDemo.restModels.SearchOtherStudents;

import junit.framework.Assert;

public class StudentTest {
	private static String ECRYPTEDNEUIDTEST = "MDAxMjM0MTIz";
	private static String NEUIDTEST = "111";
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
	private static ExtraExperiencesDao extraExperiencesDao;
	private static PasswordCreateObject passwordCreateObject;
	private static PhotosDao photosDao;
	private static StudentsPublicDao studentsPublicDao;


	Students studentChangePassword;
	StudentLogins studentLogins;
	Photos photos;

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
		projectsDao = new ProjectsDao(true); 
		studentLoginsDao = new StudentLoginsDao();
		extraExperiencesDao = new ExtraExperiencesDao();
		studentsPublicDao = new StudentsPublicDao(true);
		passwordCreateObject = new PasswordCreateObject();
	}

	@Before
	public void setupAddRecords()throws Exception {
		Students newStudent = new Students("111", "tomcat@gmail.com", "Tom", "",
				"Cat", Gender.M, "F1", "1111111111",
				"401 Terry Ave", "WA", "Seattle", "98109", Term.FALL, 2015,
				Term.SPRING, 2017,
				EnrollmentStatus.FULL_TIME, Campus.SEATTLE, DegreeCandidacy.MASTERS, null, true);
		Students newStudent2 = new Students("112", "jerrymouse@gmail.com", "Jerry", "",
				"Mouse", Gender.F, "F1", "1111111111",
				"225 Terry Ave", "MA", "Seattle", "98109", Term.FALL, 2014,
				Term.SPRING, 2016,
				EnrollmentStatus.PART_TIME, Campus.BOSTON, DegreeCandidacy.MASTERS, null, true);
		Students newStudent3 = new Students("113", "tomcat3@gmail.com", "Tom", "",
				"Dog", Gender.M, "F1", "1111111111",
				"401 Terry Ave", "WA", "Seattle", "98109", Term.FALL, 2015,
				Term.FALL, 2017,
				EnrollmentStatus.DROPPED_OUT, Campus.CHARLOTTE, DegreeCandidacy.MASTERS, null, true);

		Students newStudent4 = new Students("001234123", "tomcat1111@gmail.com", "Tom", "",
				"Dog", Gender.M, "F1", "1111111111",
				"401 Terry Ave", "WA", "Seattle", "98109", Term.FALL, 2015,
				Term.FALL, 2017,
				EnrollmentStatus.DROPPED_OUT, Campus.CHARLOTTE, DegreeCandidacy.MASTERS, null, true);

		studentsDao.addStudent(newStudent4);

		newStudent.setScholarship(true);
		//		newStudent.setRace("White");
		//		newStudent2.setRace("Black");
		//		newStudent3.setRace("White");

		studentsDao.addStudent(newStudent);
		studentsDao.addStudent(newStudent2);
		studentsDao.addStudent(newStudent3);

		// Adding experience
		//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
		//		Date startdate = formatter.parse(STARTDATE);
		//		Date enddate = formatter.parse(ENDDATE);
		//		ExtraExperiences extraExperiences = new ExtraExperiences(NEUIDTEST, "companyName", startdate, 
		//				enddate, "title", "description"	);


		studentChangePassword = new Students("135", "krishnakaranam3732@gmail.com", "Tom", "",
				"Dog", Gender.M, "F1", "1111111111",
				"401 Terry Ave", "WA", "Seattle", "98109", Term.FALL, 2015,
				Term.FALL, 2017,
				EnrollmentStatus.DROPPED_OUT, Campus.CHARLOTTE, DegreeCandidacy.MASTERS, null, true);

		studentsDao.addStudent(studentChangePassword);

		studentLogins = new StudentLogins("krishnakaranam3732@gmail.com",
				"$s0$41010$cwF4TDlHcEf5+zxUKgsA3w==$vlMxt0lC641Vdavp9nclzELFgS3YgkuG9GBTgeUKfwQ=",
				"key",
				Timestamp.valueOf("2017-09-23 10:10:10.0"),
				Timestamp.valueOf("2019-09-23 10:10:10.0"),
				true);

		studentLoginsDao.createStudentLogin(studentLogins);
	}

	@After
	public void deleteForDuplicateDatabase() {
		studentsDao.deleteStudent("111");
		studentsDao.deleteStudent("112");
		studentsDao.deleteStudent("113");
		studentsDao.deleteStudent("001234123");

		studentLoginsDao.deleteStudentLogin("krishnakaranam3732@gmail.com");
		studentsDao.deleteStudent("135");
	}	   

	/*
    getStudentProile
	 */


	@Test
	public void getStudentProfileNotFoundTest() {
		Response studentProfileResponse = studentFacing.getOtherStudentProfile("MDAxMjM0MTIi");
		Assert.assertEquals(studentProfileResponse.getStatus(), 404);
	}

	@Test
	public void getmyProfileNotFoundTest() {
		Response studentProfileResponse = studentFacing.getStudentProfile("MDAxMjM0MTIi");
		Assert.assertEquals(studentProfileResponse.getStatus(), 404);
	}


	@Test
	public void getStudentProfileTest() {

		Privacies privacy = new Privacies("001234123", 1, true, true, true, true, true,
				true, true,true, true, true, true, true, true, true);
		privacy.setNeuId("001234123");
		privacy.setPublicId(studentsDao.getStudentRecord("001234123").getPublicId());
		privaciesDao.createPrivacy(privacy);

		Response studentProfileResponse = studentFacing.getOtherStudentProfile("MDAxMjM0MTIz");

		Assert.assertEquals(200, studentProfileResponse.getStatus());

		privaciesDao.deletePrivacy("001234123");
	}

	@Test
	public void getmyProfileTest() {

		Privacies privacy = new Privacies("001234123", 1, true, true, true, true, true,
				true, true,true, true, true, true, true, true, true);
		privacy.setNeuId("001234123");
		privacy.setPublicId(studentsDao.getStudentRecord("001234123").getPublicId());
		privaciesDao.createPrivacy(privacy);

		Response studentProfileResponse = studentFacing.getStudentProfile("MDAxMjM0MTIz");

		Assert.assertEquals(200, studentProfileResponse.getStatus());

		privaciesDao.deletePrivacy("001234123");
	}

	@Test
	public void getStudentProfileTest2() {

		// add privacies
		Privacies privacy = new Privacies("001234123", 1, true, true, true, true, true,
				true, true,true, true, true, true, true, true, true);
		privacy.setNeuId("001234123");
		privacy.setPublicId(studentsDao.getStudentRecord("001234123").getPublicId());
		privaciesDao.createPrivacy(privacy);

		// add extra experience
		String endDate = "01-04-2017";
		String startDate = "01-04-2018";
		ExtraExperienceObject extraExperiencesObject = 
				new ExtraExperienceObject(111, "001234123", "companyName", startDate, 
						endDate, "title", "description");
		Response respExtraExperience =studentFacing.addExtraExperience(ECRYPTEDNEUIDTEST, extraExperiencesObject);

		// add project
		ProjectObject projectObject = new ProjectObject(10, "001234123", "Student Website", "01-01-2018",
				"01-01-2019", "description");
		Response respProject = studentFacing.addProject(ECRYPTEDNEUIDTEST, projectObject);

		// add work experience
		WorkExperiences newWorkExperience = new WorkExperiences();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		try {
			newWorkExperience.setStartDate(dateFormat.parse("06-01-2017"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			newWorkExperience.setEndDate(dateFormat.parse("04-01-2018"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newWorkExperience.setCurrentJob(false);
		newWorkExperience.setCoop(true);
		newWorkExperience.setTitle("Title");
		newWorkExperience.setDescription("Description");
		newWorkExperience.setNeuId("001234123");
		newWorkExperience.setCompanyName("Amazon");
		workExperiencesDao.createWorkExperience(newWorkExperience);
		
		// adding a course
		Courses newcourse = new Courses("55555", "course1", "course description 1");
		coursesDao.createCourse(newcourse);
		Electives elective = new Electives();
	    elective.setNeuId("001234123");
	    elective.setCourseId("55555");
	    Electives electivesNew = electivesDao.addElective(elective);

		Response studentProfileResponse = studentFacing.getOtherStudentProfile("MDAxMjM0MTIz");

		Assert.assertEquals(200, studentProfileResponse.getStatus());

		privaciesDao.deletePrivacy("001234123");

		int experirnceId = Integer.parseInt(respExtraExperience.getEntity().toString());
		Response resp3 = studentFacing.deleteExtraExperience(ECRYPTEDNEUIDTEST, experirnceId);

		int projectId = Integer.parseInt(respProject.getEntity().toString());
		Response resp2 = studentFacing.deleteProject(ECRYPTEDNEUIDTEST, projectId);
		Assert.assertEquals("Project deleted successfully", resp2.getEntity().toString());

		workExperiencesDao.deleteWorkExperienceByNeuId(ECRYPTEDNEUIDTEST);
		
	    electivesDao.deleteElectiveRecord(electivesNew.getElectiveId());
		coursesDao.deleteCourseById("55555");
	}

	@Test
	public void getmyProfileTest2() {
		// add privacies
		Privacies privacy = new Privacies("001234123", 1, true, true, true, true, true,
				true, true,true, true, true, true, true, true, true);
		privacy.setNeuId("001234123");
		privacy.setPublicId(studentsDao.getStudentRecord("001234123").getPublicId());
		privaciesDao.createPrivacy(privacy);

		// add extra experience
		String endDate = "01-04-2017";
		String startDate = "01-04-2018";
		ExtraExperienceObject extraExperiencesObject = 
				new ExtraExperienceObject(111, "001234123", "companyName", startDate, 
						endDate, "title", "description");
		Response respExtraExperience =studentFacing.addExtraExperience(ECRYPTEDNEUIDTEST, extraExperiencesObject);

		// add project
		ProjectObject projectObject = new ProjectObject(10, "001234123", "Student Website", "01-01-2018",
				"01-01-2019", "description");
		Response respProject = studentFacing.addProject(ECRYPTEDNEUIDTEST, projectObject);

		// add work experience
		WorkExperiences newWorkExperience = new WorkExperiences();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		try {
			newWorkExperience.setStartDate(dateFormat.parse("06-01-2017"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			newWorkExperience.setEndDate(dateFormat.parse("04-01-2018"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newWorkExperience.setCurrentJob(false);
		newWorkExperience.setCoop(true);
		newWorkExperience.setTitle("Title");
		newWorkExperience.setDescription("Description");
		newWorkExperience.setNeuId("001234123");
		newWorkExperience.setCompanyName("Amazon");
		workExperiencesDao.createWorkExperience(newWorkExperience);
		
		// adding a course
		Courses newcourse = new Courses("55555", "course1", "course description 1");
		coursesDao.createCourse(newcourse);
		Electives elective = new Electives();
	    elective.setNeuId("001234123");
	    elective.setCourseId("55555");
	    Electives electivesNew = electivesDao.addElective(elective);

		Response studentProfileResponse = studentFacing.getStudentProfile("MDAxMjM0MTIz");

		Assert.assertEquals(200, studentProfileResponse.getStatus());

		privaciesDao.deletePrivacy("001234123");

		int experirnceId = Integer.parseInt(respExtraExperience.getEntity().toString());
		Response resp3 = studentFacing.deleteExtraExperience(ECRYPTEDNEUIDTEST, experirnceId);

		int projectId = Integer.parseInt(respProject.getEntity().toString());
		Response resp2 = studentFacing.deleteProject(ECRYPTEDNEUIDTEST, projectId);
		Assert.assertEquals("Project deleted successfully", resp2.getEntity().toString());

		workExperiencesDao.deleteWorkExperienceByNeuId(ECRYPTEDNEUIDTEST);
		
	    electivesDao.deleteElectiveRecord(electivesNew.getElectiveId());
		coursesDao.deleteCourseById("55555");
	}


	//    @Test
	//    public void getStudentProfileBadTest() {
	//        Response studentProfileResponse = studentFacing.getOtherStudentProfile("090");
	//        String studentProfile = (String) studentProfileResponse.getEntity();
	//        Assert.assertEquals("No Student record exists with given ID", studentProfile);
	//    }

	@Test
	public void search(){
		List<String> companyList = new ArrayList<>();
		List<String> courseNameList = new ArrayList<>();
		List<String> startTermList = new ArrayList<>();
		List<String> endTermList = new ArrayList<>();
		List<String> campusList = new ArrayList<>();
		List<String> genderList = new ArrayList<>();

		companyList.add("Amazon");
		courseNameList.add("CS5200");
		startTermList.add("2019");
		campusList.add("SEATTLE"); 
		endTermList.add("2019");
		genderList.add("F");

		SearchOtherStudents search = new SearchOtherStudents(companyList, courseNameList,startTermList,
				endTermList, campusList, genderList);
		Response response = studentFacing.searchStudent(search);

		Assert.assertEquals(200, response.getStatus()); 
	}

	@Test
	public void updateStudentWithPhotoNullTest(){
		Response resp = studentFacing.updateStudentWithPhoto("MDAxMjM0MTIi", "String"); 
		Assert.assertEquals("No Student record exists with given ID", resp.getEntity());
		Assert.assertEquals(404, resp.getStatus()); 
	}

	@Test
	public void updateStudentWithPhotoNullTest1(){ 
		Response resp = studentFacing.updateStudentWithPhoto(ECRYPTEDNEUIDTEST, "String"); 
		Assert.assertEquals("No photo found for the given student", resp.getEntity());
		Assert.assertEquals(404, resp.getStatus());  
	}

	//	@Test
	//	public void updateStudentWithPhotoNullTest2(){ 
	//		byte[] b = "String".getBytes(); 
	//		Photos photo = new Photos("MDAxMjM0MTIz", b);
	//		photosDao.updatePhoto(photo);
	//		Response resp = studentFacing.updateStudentWithPhoto(ECRYPTEDNEUIDTEST, "String"); 
	//		Assert.assertEquals("Congratulations! Your Photo is uploaded successfully!", resp.getEntity());
	//		Assert.assertEquals(200, resp.getStatus());  
	//	}

	@Test
	public void updateStudentRecordTest1(){
		Students student = studentsDao.getStudentRecord("001234123");
		student.setCity("BOSTON");
		studentFacing.updateStudentRecord(ECRYPTEDNEUIDTEST, student);

		Students studentUpdated = studentsDao.getStudentRecord("001234123");

		Assert.assertEquals(student.getCity(), studentUpdated.getCity());
	}

	@Test
	public void updateStudentRecordTest2(){
		Students student = studentsDao.getStudentRecord("001234123");
		Response response = studentFacing.updateStudentRecord("MDAxMjM0MTIi", student);

		Assert.assertEquals("No Student record exists with given ID", response.getEntity().toString());
	}

	@Test
	public void updateStudentRecordTest3(){
		Students student = studentsDao.getStudentRecord("001234123");
		student.setCity("BOSTON");
		Response resp = studentFacing.updateStudentRecord(ECRYPTEDNEUIDTEST, student);

		studentsDao.getStudentRecord(NEUIDTEST);
		Assert.assertEquals(200, resp.getStatus());
	}

	@Test
	public void addExtraExperienceTest1(){
		Students student = studentsDao.getStudentRecord("001234123");
		ExtraExperienceObject extraExperiencesObject = new ExtraExperienceObject();

		Response resp = studentFacing.addExtraExperience("MDAxMjM0MTIi", extraExperiencesObject);
		Assert.assertEquals("No Student record exists with given ID", resp.getEntity().toString());
	}


	@Test
	public void addExtraExperienceTest2(){
		String endDate = "2017-01-04";
		String startDate = "2018-01-04";
		ExtraExperienceObject extraExperiencesObject = 
				new ExtraExperienceObject(111, "001234123", "companyName", startDate, 
						endDate, "title", "description");

		Response resp = studentFacing.addExtraExperience(ECRYPTEDNEUIDTEST, extraExperiencesObject);

		Assert.assertEquals(200, resp.getStatus());

		int experirnceId = Integer.parseInt(resp.getEntity().toString());
		Response resp2 = studentFacing.deleteExtraExperience(ECRYPTEDNEUIDTEST, experirnceId);
		Assert.assertEquals("Experience deleted successfully", resp2.getEntity().toString());
	}


	@Test
	public void addExtraExperienceTest3(){
		String endDate = "Tue Apr 29 11:40:55 GMT+04:00 2014";
		String startDate = "Tue Apr 29 11:40:55 GMT+04:00 2014";
		ExtraExperienceObject extraExperiencesObject = 
				new ExtraExperienceObject(111, "001234123", "companyName", startDate, 
						endDate, "title", "description");

		Response resp = studentFacing.addExtraExperience(ECRYPTEDNEUIDTEST, extraExperiencesObject);

		System.out.println("e" + resp.getEntity());
		Assert.assertEquals("Start Date didn't parse", resp.getEntity());
		Assert.assertEquals(400, resp.getStatus());
	}

	@Test
	public void addExtraExperienceTest4(){
		String startDate = "01-01-2018";
		String endDate = "Tue Apr 29 11:40:55 GMT+04:00 2014";
		ExtraExperienceObject extraExperiencesObject = 
				new ExtraExperienceObject(111, "001234123", "companyName", startDate, 
						endDate, "title", "description");

		Response resp = studentFacing.addExtraExperience(ECRYPTEDNEUIDTEST, extraExperiencesObject);

		System.out.println("e" + resp.getEntity());
		Assert.assertEquals("End Date didn't parse", resp.getEntity());
		Assert.assertEquals(400, resp.getStatus());
	}

	@Test
	public void addProjectTest1(){
		Students student = studentsDao.getStudentRecord("001234123");
		//			List<Projects> projects =
		//					projectsDao.getProjectsByNeuId("001234123");
		//	
		ProjectObject projectObject = new ProjectObject();

		Response resp = studentFacing.addProject("MDAxMjM0MTIi", projectObject);
		Assert.assertEquals("No Student record exists with given ID", resp.getEntity().toString());
	}

	@Test
	public void updateExtraExperience4(){
		String endDate = "01-01-2017";
		String startDate = "01-01-2018";
		ExtraExperienceObject extraExperiencesObject = 
				new ExtraExperienceObject(111, "001234123", "companyName", startDate, 
						endDate, "title", "description");

		Response resp = studentFacing.updateExtraExperience("MDAxMjM0MTIi", 1, extraExperiencesObject);
		Assert.assertEquals("No Student record exists with given ID", resp.getEntity().toString());
	}

	@Test
	public void updateExtraExperience5(){
		String endDate = "01-01-2017";
		String startDate = "01-01-2018";
		ExtraExperienceObject extraExperiencesObject = 
				new ExtraExperienceObject(111, "001234123", "companyName", startDate, 
						endDate, "title", "description");

		Response resp = studentFacing.updateExtraExperience("MDAxMjM0MTIz", 1, extraExperiencesObject);
		Assert.assertEquals("No Extra Experience record exists for a given Id: 1", resp.getEntity().toString());
	}

	@Test
	public void updateExtraExperience1(){
		String endDate = "01-01-2017";
		String startDate = "01-01-2018";
		ExtraExperienceObject extraExperiencesObject = 
				new ExtraExperienceObject(111, "001234123", "companyName", startDate, 
						endDate, "title", "description");

		String oldDescription = extraExperiencesObject.getDescription();
		Response resp = studentFacing.addExtraExperience(ECRYPTEDNEUIDTEST, extraExperiencesObject);
		Assert.assertEquals(200, resp.getStatus());

		extraExperiencesObject.setDescription("d2");
		int projectId = Integer.parseInt(resp.getEntity().toString()); 
		Response respUpdate = studentFacing.updateExtraExperience(ECRYPTEDNEUIDTEST, projectId, extraExperiencesObject);

		Assert.assertEquals("Experience updated successfully :", respUpdate.getEntity());
		Assert.assertEquals(200, respUpdate.getStatus());
		Assert.assertEquals("d2", extraExperiencesObject.getDescription());

		Response resp2 = studentFacing.deleteExtraExperience(ECRYPTEDNEUIDTEST, projectId);
		Assert.assertEquals("Experience deleted successfully", resp2.getEntity().toString());
	}

	@Test
	public void updateExtraExperience2(){
		String startDate = "01-01-2018";
		String endDate = "Tue Apr 29 11:40:55 GMT+04:00 2014";

		ExtraExperienceObject extraExperiencesObject = 
				new ExtraExperienceObject(111, "001234123", "companyName", startDate, 
						endDate, "title", "description");


		Response resp = studentFacing.addExtraExperience(ECRYPTEDNEUIDTEST, extraExperiencesObject);

		Assert.assertEquals("End Date didn't parse", resp.getEntity());
		Assert.assertEquals(400, resp.getStatus());
	}

	@Test
	public void updateExtraExperience3(){
		String endDate = "Tue Apr 29 11:40:55 GMT+04:00 2014";
		String startDate = "Tue Apr 29 11:40:55 GMT+04:00 2014";

		ExtraExperienceObject extraExperiencesObject = 
				new ExtraExperienceObject(111, "001234123", "companyName", startDate, 
						endDate, "title", "description");

		Response resp = studentFacing.addExtraExperience(ECRYPTEDNEUIDTEST, extraExperiencesObject);

		Assert.assertEquals("Start Date didn't parse", resp.getEntity());
		Assert.assertEquals(400, resp.getStatus());
	}

	@Test
	public void updateProject4(){
		ProjectObject projectObject = new ProjectObject(10, "001234123", "Student Website", "01-01-2018",
				"01-01-2019", "description");

		String oldDescription = projectObject.getDescription();
		Response resp = studentFacing.addProject(ECRYPTEDNEUIDTEST, projectObject);

		Assert.assertEquals(200, resp.getStatus());

		//			ProjectObject projectNew = new ProjectObject();
		projectObject.setDescription("d2");

		int projectId = Integer.parseInt(resp.getEntity().toString()); 
		Response respUpdate = studentFacing.updateProject(ECRYPTEDNEUIDTEST, projectId, projectObject);

		Assert.assertEquals(200, respUpdate.getStatus());
		Assert.assertEquals("d2", projectObject.getDescription());

		Response resp2 = studentFacing.deleteProject(ECRYPTEDNEUIDTEST, projectId);
		Assert.assertEquals("Project deleted successfully", resp2.getEntity().toString());
	}

	@Test
	public void updateProject2(){
		String startDate = "01-01-2018";
		String endDate = "Tue Apr 29 11:40:55 GMT+04:00 2014";

		ProjectObject projectObject = new ProjectObject(10, "001234123", "Student Website", startDate,
				endDate, "description");

		Response resp = studentFacing.addProject(ECRYPTEDNEUIDTEST, projectObject);

		Assert.assertEquals("End Date didn't parse", resp.getEntity());
		Assert.assertEquals(400, resp.getStatus());
	}

	@Test
	public void updateProject3(){
		String endDate = "Tue Apr 29 11:40:55 GMT+04:00 2014";
		String startDate = "Tue Apr 29 11:40:55 GMT+04:00 2014";

		ProjectObject projectObject = new ProjectObject(10, "001234123", "Student Website", startDate,
				endDate, "description");

		Response resp = studentFacing.addProject(ECRYPTEDNEUIDTEST, projectObject);

		Assert.assertEquals("Start Date didn't parse", resp.getEntity());
		Assert.assertEquals(400, resp.getStatus());
	}

	@Test
	public void updatePrivaciesTest1(){
		EmailToRegister emailToRegister = new EmailToRegister("");
		Response res = studentFacing.sendRegistrationEmail(emailToRegister);

		String response = (String) res.getEntity();

		Assert.assertEquals("Email Id can't be null or empty" , response); 
	}

	@Test
	public void addProjectTest2(){
		ProjectObject projectObject = new ProjectObject(10, "001234123", "Student Website", "01-01-2018",
				"01-01-2019", "description");

		Response resp = studentFacing.addProject(ECRYPTEDNEUIDTEST, projectObject);

		Assert.assertEquals(200, resp.getStatus());

		int projectId = Integer.parseInt(resp.getEntity().toString());
		Response resp2 = studentFacing.deleteProject(ECRYPTEDNEUIDTEST, projectId);
		Assert.assertEquals("Project deleted successfully", resp2.getEntity().toString());
	}

	@Test
	public void getStudentExtraExperience1(){
		String endDate = "01-04-2017";
		String startDate = "01-04-2018";
		ExtraExperienceObject extraExperiencesObject = 
				new ExtraExperienceObject(111, "001234123", "companyName", startDate, 
						endDate, "title", "description");

		Response resp = studentFacing.addExtraExperience(ECRYPTEDNEUIDTEST, extraExperiencesObject);
		Response getResp = studentFacing.getStudentExtraExperience(ECRYPTEDNEUIDTEST);

		Assert.assertEquals(200, getResp.getStatus());

		int experirnceId = Integer.parseInt(resp.getEntity().toString());
		Response resp2 = studentFacing.deleteExtraExperience(ECRYPTEDNEUIDTEST, experirnceId);
		Assert.assertEquals("Experience deleted successfully", resp2.getEntity().toString());
	}

	@Test
	public void getStudentExtraExperience2(){
		Response getResp = studentFacing.getStudentExtraExperience("MDAxMjM0MTIi");

		Assert.assertEquals(404, getResp.getStatus());
		Assert.assertEquals("No Student record exists with given ID", getResp.getEntity());
	}

	@Test
	public void getStudentExtraExperience3(){
		Response getResp = studentFacing.getStudentExtraExperience(ECRYPTEDNEUIDTEST);
		Assert.assertEquals(404, getResp.getStatus());
		Assert.assertEquals("No Extra Experience record exists for a given NeuId: MDAxMjM0MTIz", getResp.getEntity());
	}

	@Test
	public void getStudentWorkExperiences3(){
		Response getResp = studentFacing.getStudentWorkExperiences("MDAxMjM0MTIi");
		Assert.assertEquals(404, getResp.getStatus());
		Assert.assertEquals("No Student record exists with given ID", getResp.getEntity());
	}

	@Test
	public void getStudentWorkExperiences2(){
		Response getResp = studentFacing.getStudentWorkExperiences(ECRYPTEDNEUIDTEST);
		Assert.assertEquals(404, getResp.getStatus());
		Assert.assertEquals("No Student record exists with given ID", getResp.getEntity());
	}

	@Test
	public void getStudentWorkExperiences1(){
		WorkExperiences newWorkExperience = new WorkExperiences();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		try {
			newWorkExperience.setStartDate(dateFormat.parse("06-01-2017"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			newWorkExperience.setEndDate(dateFormat.parse("04-01-2018"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newWorkExperience.setCurrentJob(false);
		newWorkExperience.setCoop(true);
		newWorkExperience.setTitle("Title");
		newWorkExperience.setDescription("Description");
		newWorkExperience.setNeuId("001234123");
		newWorkExperience.setCompanyName("Amazon");
		workExperiencesDao.createWorkExperience(newWorkExperience);

		Response response = studentFacing.getStudentWorkExperiences(ECRYPTEDNEUIDTEST);

		Assert.assertEquals(200, response.getStatus());

		workExperiencesDao.deleteWorkExperienceByNeuId(NEUIDTEST);
	}

	@Test
	public void getAllGradYears(){
		Response  response = studentFacing.getAllCampuses();
		Response  response2 = studentFacing.getAllCoopCompanies();
		Response  response3 = studentFacing.getAllCourses();
		Response  response4 = studentFacing.getAllEnrollmentYears();
		Response  response5 = studentFacing.getAllGradYears();
		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals(200, response2.getStatus());
		Assert.assertEquals(200, response3.getStatus());
		Assert.assertEquals(200, response4.getStatus());
		Assert.assertEquals(200, response5.getStatus());
	}

	@Test
	public void getStudentPrivacies1(){
		Response  response = studentFacing.getStduentPrivacies("MDAxMjM0MTIi");
		Assert.assertEquals(404, response.getStatus());
	}

	@Test
	public void getStudentPrivacies2(){
		Privacies privacy = new Privacies("001234123", 1, true, true, true, true, true,
				true, true,true, true, true, true, true, true, true);
		privacy.setNeuId("001234123");
		privacy.setPublicId(studentsDao.getStudentRecord("001234123").getPublicId());
		privaciesDao.createPrivacy(privacy);

		Response  response = studentFacing.getStduentPrivacies(ECRYPTEDNEUIDTEST);

		Assert.assertEquals(200, response.getStatus());
	}

	@Test
	public void updatePrivacies(){
		Privacies privacy = new Privacies("001234123", 1, true, true, true, true, true,
				true, true,true, true, true, true, true, true, true);
		privacy.setNeuId("001234123");
		privacy.setPublicId(studentsDao.getStudentRecord("001234123").getPublicId());
		privaciesDao.createPrivacy(privacy);

		privacy.setVisibleToPublic(false); 

		Response  response = studentFacing.updatePrivacies(ECRYPTEDNEUIDTEST, privacy);

		Assert.assertEquals(privacy.isVisibleToPublic(), false);

		Assert.assertEquals(200, response.getStatus());
	}

	@Test
	public void updatePrivaciesNullTest(){
		Privacies privacy = new Privacies("0012341231", 1, true, true, true, true, true,
				true, true,true, true, true, true, true, true, true);
		privacy.setNeuId("0012341231");
		privacy.setPublicId(2);

		Response  response = studentFacing.updatePrivacies(ECRYPTEDNEUIDTEST, privacy);

		Assert.assertEquals("No Privacies record exists for a given NUID", response.getEntity());
	}

	@Test
	public void updatePrivaciesNullTest2(){
		Privacies privacy = new Privacies("001234123", 1, true, true, true, true, true,
				true, true,true, true, true, true, true, true, true);
		privacy.setNeuId("001234123");
		privacy.setPublicId(studentsDao.getStudentRecord("001234123").getPublicId());
		privaciesDao.createPrivacy(privacy);

		privacy.setVisibleToPublic(false); 

		StudentsPublic studentsPublic = new 
				StudentsPublic(studentsDao.getStudentRecord("001234123").getPublicId(), 2016, true);
		studentsPublicDao.createStudent(studentsPublic);

		Response  response = studentFacing.updatePrivacies(ECRYPTEDNEUIDTEST, privacy);

		Assert.assertEquals(privacy.isVisibleToPublic(), false);

		Assert.assertEquals(200, response.getStatus());
	}

	@Test
	public void loginUser1(){
		LoginObject loginObject = new LoginObject("test.alignstudent123@gmail.com", "mangograpes123");
		Response  response = studentFacing.loginUser(null, loginObject);

		Assert.assertEquals(response.getEntity().toString(), "User doesn't exist: test.alignstudent123@gmail.com");
	}

	@Test
	public void logoutUser1(){
		LoginObject loginObject = new LoginObject("test.alignstudent123@gmail.com", "mangograpes123");
		Response  response = studentFacing.logoutUser(null, loginObject);

		Assert.assertEquals(response.getEntity().toString(), "User doesn't exist: test.alignstudent123@gmail.com");
	}

	@Test
	public void registerStudent1(){
		EmailToRegister emailToRegister = new EmailToRegister("test.alignstudent1231@gmail.com");
		Response res = studentFacing.sendRegistrationEmail(emailToRegister);

		String response = (String) res.getEntity();

		Assert.assertEquals("To Register should be an Align Student!" , response); 
	}

	@Test
	public void registerStudent2(){
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

	@Test
	public void createPassword1(){

		StudentLogins studentLogins = new StudentLogins("tomcat3@gmail.com",
				"password",
				"key",
				Timestamp.valueOf("2017-09-23 10:10:10.0"),
				Timestamp.valueOf("2017-09-23 10:10:10.0"),
				true);
		studentLoginsDao.createStudentLogin(studentLogins);

		PasswordCreateObject passwordCreateObject = new
				PasswordCreateObject("tomcat3@gmail.com", "password","key");

		Response res = studentFacing.createPassword(passwordCreateObject);

		String response = (String) res.getEntity();

		Assert.assertEquals(" Registration key expired!" , response); 

		studentLoginsDao.deleteStudentLogin("tomcat3@gmail.com");
	}

	@Test
	public void createPassword2(){
		StudentLogins studentLogins = new StudentLogins("tomcat3@gmail.com",
				"password",
				"key",
				Timestamp.valueOf("2017-09-23 10:10:10.0"),
				Timestamp.valueOf("2017-09-23 10:10:10.0"),
				true);
		studentLoginsDao.createStudentLogin(studentLogins);

		PasswordCreateObject passwordCreateObject = new
				PasswordCreateObject("tomcat3@gmail.com", "password","key");
		passwordCreateObject.setEmail(null);

		Response res = studentFacing.createPassword(passwordCreateObject);

		String response = (String) res.getEntity();

		Assert.assertEquals("Invalid Student details. Student does not exist" , response); 

		studentLoginsDao.deleteStudentLogin("tomcat3@gmail.com");
	}

	/*
    createPassword
	 */

	@Test
	public void CreatePasswordTest() throws SQLException, ParseException {
		Students TestStudent = new Students("19", "studentlogintest@gmail.com", "test", "test",
				"test", Gender.M, "F1", "1111111111",
				"401 Terry Ave", "WA", "Seattle", "98109", Term.FALL, 2015,
				Term.SPRING, 2017,
				EnrollmentStatus.FULL_TIME, Campus.SEATTLE, DegreeCandidacy.MASTERS, null, true);
		studentsDao.addStudent(TestStudent);

		StudentLogins studentLogins = new StudentLogins("studentlogintest@gmail.com",
				"password",
				"key",
				Timestamp.valueOf("2017-09-23 10:10:10.0"),
				Timestamp.valueOf("2017-09-23 10:10:10.0"),
				false);
		studentLoginsDao.createStudentLogin(studentLogins);

		passwordCreateObject = new PasswordCreateObject("studentlogintest@gmail.com",
				"passwordTest","key");

		Response response =  studentFacing.createPassword(passwordCreateObject);
		Assert.assertEquals(200, response.getStatus());

		studentLoginsDao.deleteStudentLogin("studentlogintest@gmail.com");
		studentsDao.deleteStudent(TestStudent.getNeuId());
	}

	@Test
	public void CreatePasswordRegitrationExpTest() throws SQLException, ParseException {
		Students TestStudent = new Students("45", "studentlogintest@gmail.com", "test", "test",
				"test", Gender.M, "F1", "1111111111",
				"401 Terry Ave", "WA", "Seattle", "98109", Term.FALL, 2015,
				Term.SPRING, 2017,
				EnrollmentStatus.FULL_TIME, Campus.SEATTLE, DegreeCandidacy.MASTERS, null, true);
		studentsDao.addStudent(TestStudent);

		StudentLogins studentLogins = new StudentLogins("studentlogintest@gmail.com",
				"password",
				"key",
				Timestamp.valueOf("2017-09-23 10:10:10.0"),
				Timestamp.valueOf("2017-09-23 10:10:10.0"),
				false);
		studentLoginsDao.createStudentLogin(studentLogins);

		passwordCreateObject = new PasswordCreateObject("studentlogintest@gmail.com",
				"passwordTest","key");

		Response TopBachelorResponse;
		TopBachelorResponse = studentFacing.createPassword(passwordCreateObject);
		Assert.assertEquals(200, TopBachelorResponse.getStatus());

		studentLoginsDao.deleteStudentLogin("studentlogintest@gmail.com");
		studentsDao.deleteStudent(TestStudent.getNeuId());
	}

	@Test
	public void CreatePasswordNoEmailExpTest() throws SQLException, ParseException {
		passwordCreateObject = new PasswordCreateObject("test@gmail.com",
				"passwordTest","key");

		Response response = studentFacing.createPassword(passwordCreateObject);
		Assert.assertEquals(400, response.getStatus());
	}

	@Test
	public void CreatePasswordErrorTest() throws SQLException, ParseException {
		Students TestStudent = new Students("45", "studentlogintest@gmail.com", "test", "test",
				"test", Gender.M, "F1", "1111111111",
				"401 Terry Ave", "WA", "Seattle", "98109", Term.FALL, 2015,
				Term.SPRING, 2017,
				EnrollmentStatus.FULL_TIME, Campus.SEATTLE, DegreeCandidacy.MASTERS, null, true);
		studentsDao.addStudent(TestStudent);

		StudentLogins studentLogins = new StudentLogins("studentlogintest@gmail.com",
				"password",
				"key",
				Timestamp.valueOf("2017-09-23 10:10:10.0"),
				Timestamp.valueOf("2017-09-23 10:10:10.0"),
				false);
		studentLoginsDao.createStudentLogin(studentLogins);

		passwordCreateObject = new PasswordCreateObject("studentlogintest@gmail.com",
				"passwordTest","key1");

		Response TopBachelorResponse;
		TopBachelorResponse = studentFacing.createPassword(passwordCreateObject);
		Assert.assertEquals(400, TopBachelorResponse.getStatus());

		studentLoginsDao.deleteStudentLogin("studentlogintest@gmail.com");
		studentsDao.deleteStudent(TestStudent.getNeuId());
	}

	@Test
	public void CreatePassworddataErrorTest() throws SQLException, ParseException {
		passwordCreateObject = new PasswordCreateObject();
		Response TopBachelorResponse;
		TopBachelorResponse = studentFacing.createPassword(passwordCreateObject);
		Assert.assertEquals(400, TopBachelorResponse.getStatus());

	}

	/*
	 * Student Login Tests
	 */

	@Test
	public void StudentLoginTest() throws SQLException, ParseException {
		HttpServletRequest request = new HttpServletRequest() {
			@Override
			public String getAuthType() {
				return null;
			}

			@Override
			public Cookie[] getCookies() {
				return new Cookie[0];
			}

			@Override
			public long getDateHeader(String s) {
				return 0;
			}

			@Override
			public String getHeader(String s) {
				return null;
			}

			@Override
			public Enumeration getHeaders(String s) {
				return null;
			}

			@Override
			public Enumeration getHeaderNames() {
				return null;
			}

			@Override
			public int getIntHeader(String s) {
				return 0;
			}

			@Override
			public String getMethod() {
				return null;
			}

			@Override
			public String getPathInfo() {
				return null;
			}

			@Override
			public String getPathTranslated() {
				return null;
			}

			@Override
			public String getContextPath() {
				return null;
			}

			@Override
			public String getQueryString() {
				return null;
			}

			@Override
			public String getRemoteUser() {
				return null;
			}

			@Override
			public boolean isUserInRole(String s) {
				return false;
			}

			@Override
			public Principal getUserPrincipal() {
				return null;
			}

			@Override
			public String getRequestedSessionId() {
				return null;
			}

			@Override
			public String getRequestURI() {
				return null;
			}

			@Override
			public StringBuffer getRequestURL() {
				return null;
			}

			@Override
			public String getServletPath() {
				return null;
			}

			@Override
			public HttpSession getSession(boolean b) {
				return null;
			}

			@Override
			public HttpSession getSession() {
				return null;
			}

			@Override
			public boolean isRequestedSessionIdValid() {
				return false;
			}

			@Override
			public boolean isRequestedSessionIdFromCookie() {
				return false;
			}

			@Override
			public boolean isRequestedSessionIdFromURL() {
				return false;
			}

			@Override
			public boolean isRequestedSessionIdFromUrl() {
				return false;
			}

			@Override
			public Object getAttribute(String s) {
				return null;
			}

			@Override
			public Enumeration getAttributeNames() {
				return null;
			}

			@Override
			public String getCharacterEncoding() {
				return null;
			}

			@Override
			public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

			}

			@Override
			public int getContentLength() {
				return 0;
			}

			@Override
			public String getContentType() {
				return null;
			}

			@Override
			public ServletInputStream getInputStream() throws IOException {
				return null;
			}

			@Override
			public String getParameter(String s) {
				return null;
			}

			@Override
			public Enumeration getParameterNames() {
				return null;
			}

			@Override
			public String[] getParameterValues(String s) {
				return new String[0];
			}

			@Override
			public Map getParameterMap() {
				return null;
			}

			@Override
			public String getProtocol() {
				return null;
			}

			@Override
			public String getScheme() {
				return null;
			}

			@Override
			public String getServerName() {
				return null;
			}

			@Override
			public int getServerPort() {
				return 0;
			}

			@Override
			public BufferedReader getReader() throws IOException {
				return null;
			}

			@Override
			public String getRemoteAddr() {
				return null;
			}

			@Override
			public String getRemoteHost() {
				return null;
			}

			@Override
			public void setAttribute(String s, Object o) {

			}

			@Override
			public void removeAttribute(String s) {

			}

			@Override
			public Locale getLocale() {
				return null;
			}

			@Override
			public Enumeration getLocales() {
				return null;
			}

			@Override
			public boolean isSecure() {
				return false;
			}

			@Override
			public RequestDispatcher getRequestDispatcher(String s) {
				return null;
			}

			@Override
			public String getRealPath(String s) {
				return null;
			}

			@Override
			public int getRemotePort() {
				return 0;
			}

			@Override
			public String getLocalName() {
				return null;
			}

			@Override
			public String getLocalAddr() {
				return null;
			}

			@Override
			public int getLocalPort() {
				return 0;
			}
		};

		LoginObject loginObject = new LoginObject("krishnakaranam3732@gmail.com","$s0$41010$cwF4TDlHcEf5+zxUKgsA3w==$vlMxt0lC641Vdavp9nclzELFgS3YgkuG9GBTgeUKfwQ=");

		Response TopBachelorResponse;
		TopBachelorResponse = studentFacing.loginUser(request ,loginObject);
		Assert.assertEquals(401, TopBachelorResponse.getStatus());

	}
	@Test
	public void StudentLoginIncorrectTest() throws SQLException, ParseException {
		HttpServletRequest request = new HttpServletRequest() {
			@Override
			public String getAuthType() {
				return null;
			}

			@Override
			public Cookie[] getCookies() {
				return new Cookie[0];
			}

			@Override
			public long getDateHeader(String s) {
				return 0;
			}

			@Override
			public String getHeader(String s) {
				return null;
			}

			@Override
			public Enumeration getHeaders(String s) {
				return null;
			}

			@Override
			public Enumeration getHeaderNames() {
				return null;
			}

			@Override
			public int getIntHeader(String s) {
				return 0;
			}

			@Override
			public String getMethod() {
				return null;
			}

			@Override
			public String getPathInfo() {
				return null;
			}

			@Override
			public String getPathTranslated() {
				return null;
			}

			@Override
			public String getContextPath() {
				return null;
			}

			@Override
			public String getQueryString() {
				return null;
			}

			@Override
			public String getRemoteUser() {
				return null;
			}

			@Override
			public boolean isUserInRole(String s) {
				return false;
			}

			@Override
			public Principal getUserPrincipal() {
				return null;
			}

			@Override
			public String getRequestedSessionId() {
				return null;
			}

			@Override
			public String getRequestURI() {
				return null;
			}

			@Override
			public StringBuffer getRequestURL() {
				return null;
			}

			@Override
			public String getServletPath() {
				return null;
			}

			@Override
			public HttpSession getSession(boolean b) {
				return null;
			}

			@Override
			public HttpSession getSession() {
				return null;
			}

			@Override
			public boolean isRequestedSessionIdValid() {
				return false;
			}

			@Override
			public boolean isRequestedSessionIdFromCookie() {
				return false;
			}

			@Override
			public boolean isRequestedSessionIdFromURL() {
				return false;
			}

			@Override
			public boolean isRequestedSessionIdFromUrl() {
				return false;
			}

			@Override
			public Object getAttribute(String s) {
				return null;
			}

			@Override
			public Enumeration getAttributeNames() {
				return null;
			}

			@Override
			public String getCharacterEncoding() {
				return null;
			}

			@Override
			public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

			}

			@Override
			public int getContentLength() {
				return 0;
			}

			@Override
			public String getContentType() {
				return null;
			}

			@Override
			public ServletInputStream getInputStream() throws IOException {
				return null;
			}

			@Override
			public String getParameter(String s) {
				return null;
			}

			@Override
			public Enumeration getParameterNames() {
				return null;
			}

			@Override
			public String[] getParameterValues(String s) {
				return new String[0];
			}

			@Override
			public Map getParameterMap() {
				return null;
			}

			@Override
			public String getProtocol() {
				return null;
			}

			@Override
			public String getScheme() {
				return null;
			}

			@Override
			public String getServerName() {
				return null;
			}

			@Override
			public int getServerPort() {
				return 0;
			}

			@Override
			public BufferedReader getReader() throws IOException {
				return null;
			}

			@Override
			public String getRemoteAddr() {
				return null;
			}

			@Override
			public String getRemoteHost() {
				return null;
			}

			@Override
			public void setAttribute(String s, Object o) {

			}

			@Override
			public void removeAttribute(String s) {

			}

			@Override
			public Locale getLocale() {
				return null;
			}

			@Override
			public Enumeration getLocales() {
				return null;
			}

			@Override
			public boolean isSecure() {
				return false;
			}

			@Override
			public RequestDispatcher getRequestDispatcher(String s) {
				return null;
			}

			@Override
			public String getRealPath(String s) {
				return null;
			}

			@Override
			public int getRemotePort() {
				return 0;
			}

			@Override
			public String getLocalName() {
				return null;
			}

			@Override
			public String getLocalAddr() {
				return null;
			}

			@Override
			public int getLocalPort() {
				return 0;
			}
		};

		LoginObject loginObject = new LoginObject("krishnakaranam3732@gmail.com","password");

		Response TopBachelorResponse;
		TopBachelorResponse = studentFacing.loginUser(request ,loginObject);
		Assert.assertEquals(200, TopBachelorResponse.getStatus());

	}

	@Test
	public void StudentLoginNullTest() throws SQLException, ParseException {
		HttpServletRequest request = new HttpServletRequest() {
			@Override
			public String getAuthType() {
				return null;
			}

			@Override
			public Cookie[] getCookies() {
				return new Cookie[0];
			}

			@Override
			public long getDateHeader(String s) {
				return 0;
			}

			@Override
			public String getHeader(String s) {
				return null;
			}

			@Override
			public Enumeration getHeaders(String s) {
				return null;
			}

			@Override
			public Enumeration getHeaderNames() {
				return null;
			}

			@Override
			public int getIntHeader(String s) {
				return 0;
			}

			@Override
			public String getMethod() {
				return null;
			}

			@Override
			public String getPathInfo() {
				return null;
			}

			@Override
			public String getPathTranslated() {
				return null;
			}

			@Override
			public String getContextPath() {
				return null;
			}

			@Override
			public String getQueryString() {
				return null;
			}

			@Override
			public String getRemoteUser() {
				return null;
			}

			@Override
			public boolean isUserInRole(String s) {
				return false;
			}

			@Override
			public Principal getUserPrincipal() {
				return null;
			}

			@Override
			public String getRequestedSessionId() {
				return null;
			}

			@Override
			public String getRequestURI() {
				return null;
			}

			@Override
			public StringBuffer getRequestURL() {
				return null;
			}

			@Override
			public String getServletPath() {
				return null;
			}

			@Override
			public HttpSession getSession(boolean b) {
				return null;
			}

			@Override
			public HttpSession getSession() {
				return null;
			}

			@Override
			public boolean isRequestedSessionIdValid() {
				return false;
			}

			@Override
			public boolean isRequestedSessionIdFromCookie() {
				return false;
			}

			@Override
			public boolean isRequestedSessionIdFromURL() {
				return false;
			}

			@Override
			public boolean isRequestedSessionIdFromUrl() {
				return false;
			}

			@Override
			public Object getAttribute(String s) {
				return null;
			}

			@Override
			public Enumeration getAttributeNames() {
				return null;
			}

			@Override
			public String getCharacterEncoding() {
				return null;
			}

			@Override
			public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

			}

			@Override
			public int getContentLength() {
				return 0;
			}

			@Override
			public String getContentType() {
				return null;
			}

			@Override
			public ServletInputStream getInputStream() throws IOException {
				return null;
			}

			@Override
			public String getParameter(String s) {
				return null;
			}

			@Override
			public Enumeration getParameterNames() {
				return null;
			}

			@Override
			public String[] getParameterValues(String s) {
				return new String[0];
			}

			@Override
			public Map getParameterMap() {
				return null;
			}

			@Override
			public String getProtocol() {
				return null;
			}

			@Override
			public String getScheme() {
				return null;
			}

			@Override
			public String getServerName() {
				return null;
			}

			@Override
			public int getServerPort() {
				return 0;
			}

			@Override
			public BufferedReader getReader() throws IOException {
				return null;
			}

			@Override
			public String getRemoteAddr() {
				return null;
			}

			@Override
			public String getRemoteHost() {
				return null;
			}

			@Override
			public void setAttribute(String s, Object o) {

			}

			@Override
			public void removeAttribute(String s) {

			}

			@Override
			public Locale getLocale() {
				return null;
			}

			@Override
			public Enumeration getLocales() {
				return null;
			}

			@Override
			public boolean isSecure() {
				return false;
			}

			@Override
			public RequestDispatcher getRequestDispatcher(String s) {
				return null;
			}

			@Override
			public String getRealPath(String s) {
				return null;
			}

			@Override
			public int getRemotePort() {
				return 0;
			}

			@Override
			public String getLocalName() {
				return null;
			}

			@Override
			public String getLocalAddr() {
				return null;
			}

			@Override
			public int getLocalPort() {
				return 0;
			}
		};

		LoginObject loginObject = new LoginObject("null@gmail.com","password");

		Response TopBachelorResponse;
		TopBachelorResponse = studentFacing.loginUser(request ,loginObject);
		Assert.assertEquals(404, TopBachelorResponse.getStatus());

	}

	@Test
	public void StudentLoginNull2Test() throws SQLException, ParseException {
		Students TestStudent = new Students("290", "null2@gmail.com", "test", "test",
				"test", Gender.M, "F1", "1111111111",
				"401 Terry Ave", "WA", "Seattle", "98109", Term.FALL, 2015,
				Term.SPRING, 2017,
				EnrollmentStatus.FULL_TIME, Campus.SEATTLE, DegreeCandidacy.MASTERS, null, true);
		studentsDao.addStudent(TestStudent);

		StudentLogins studentLogins = new StudentLogins("null2@gmail.com",
				"pass",
				"key",
				Timestamp.valueOf("2017-09-23 10:10:10.0"),
				Timestamp.valueOf("2017-09-23 10:10:10.0"),
				false);
		studentLoginsDao.createStudentLogin(studentLogins);

		HttpServletRequest request = new HttpServletRequest() {
			@Override
			public String getAuthType() {
				return null;
			}

			@Override
			public Cookie[] getCookies() {
				return new Cookie[0];
			}

			@Override
			public long getDateHeader(String s) {
				return 0;
			}

			@Override
			public String getHeader(String s) {
				return null;
			}

			@Override
			public Enumeration getHeaders(String s) {
				return null;
			}

			@Override
			public Enumeration getHeaderNames() {
				return null;
			}

			@Override
			public int getIntHeader(String s) {
				return 0;
			}

			@Override
			public String getMethod() {
				return null;
			}

			@Override
			public String getPathInfo() {
				return null;
			}

			@Override
			public String getPathTranslated() {
				return null;
			}

			@Override
			public String getContextPath() {
				return null;
			}

			@Override
			public String getQueryString() {
				return null;
			}

			@Override
			public String getRemoteUser() {
				return null;
			}

			@Override
			public boolean isUserInRole(String s) {
				return false;
			}

			@Override
			public Principal getUserPrincipal() {
				return null;
			}

			@Override
			public String getRequestedSessionId() {
				return null;
			}

			@Override
			public String getRequestURI() {
				return null;
			}

			@Override
			public StringBuffer getRequestURL() {
				return null;
			}

			@Override
			public String getServletPath() {
				return null;
			}

			@Override
			public HttpSession getSession(boolean b) {
				return null;
			}

			@Override
			public HttpSession getSession() {
				return null;
			}

			@Override
			public boolean isRequestedSessionIdValid() {
				return false;
			}

			@Override
			public boolean isRequestedSessionIdFromCookie() {
				return false;
			}

			@Override
			public boolean isRequestedSessionIdFromURL() {
				return false;
			}

			@Override
			public boolean isRequestedSessionIdFromUrl() {
				return false;
			}

			@Override
			public Object getAttribute(String s) {
				return null;
			}

			@Override
			public Enumeration getAttributeNames() {
				return null;
			}

			@Override
			public String getCharacterEncoding() {
				return null;
			}

			@Override
			public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

			}

			@Override
			public int getContentLength() {
				return 0;
			}

			@Override
			public String getContentType() {
				return null;
			}

			@Override
			public ServletInputStream getInputStream() throws IOException {
				return null;
			}

			@Override
			public String getParameter(String s) {
				return null;
			}

			@Override
			public Enumeration getParameterNames() {
				return null;
			}

			@Override
			public String[] getParameterValues(String s) {
				return new String[0];
			}

			@Override
			public Map getParameterMap() {
				return null;
			}

			@Override
			public String getProtocol() {
				return null;
			}

			@Override
			public String getScheme() {
				return null;
			}

			@Override
			public String getServerName() {
				return null;
			}

			@Override
			public int getServerPort() {
				return 0;
			}

			@Override
			public BufferedReader getReader() throws IOException {
				return null;
			}

			@Override
			public String getRemoteAddr() {
				return null;
			}

			@Override
			public String getRemoteHost() {
				return null;
			}

			@Override
			public void setAttribute(String s, Object o) {

			}

			@Override
			public void removeAttribute(String s) {

			}

			@Override
			public Locale getLocale() {
				return null;
			}

			@Override
			public Enumeration getLocales() {
				return null;
			}

			@Override
			public boolean isSecure() {
				return false;
			}

			@Override
			public RequestDispatcher getRequestDispatcher(String s) {
				return null;
			}

			@Override
			public String getRealPath(String s) {
				return null;
			}

			@Override
			public int getRemotePort() {
				return 0;
			}

			@Override
			public String getLocalName() {
				return null;
			}

			@Override
			public String getLocalAddr() {
				return null;
			}

			@Override
			public int getLocalPort() {
				return 0;
			}
		};

		LoginObject loginObject = new LoginObject("null2@gmail.com","pass");

		Response response = studentFacing.loginUser(request ,loginObject);
		Assert.assertEquals(401, response.getStatus());

		studentLoginsDao.deleteStudentLogin("null2@gmail.com");
		studentsDao.deleteStudent(TestStudent.getNeuId());
	}


	/*
    StudentLogout
	 */

	@Test
	public void studentLogoutUnAuthorizeTest() throws SQLException, ParseException {
		Students TestStudent = new Students("130", "t@gmail.com", "test", "test",
				"test", Gender.M, "F1", "1111111111",
				"401 Terry Ave", "WA", "Seattle", "98109", Term.FALL, 2015,
				Term.SPRING, 2017,
				EnrollmentStatus.FULL_TIME, Campus.SEATTLE, DegreeCandidacy.MASTERS, null, true);
		studentsDao.addStudent(TestStudent);

		StudentLogins studentLogins = new StudentLogins("t@gmail.com",
				"password",
				"key",
				Timestamp.valueOf("2017-09-23 10:10:10.0"),
				Timestamp.valueOf("2017-09-23 10:10:10.0"),
				false);
		studentLoginsDao.createStudentLogin(studentLogins);

		HttpServletRequest request = new HttpServletRequest() {
			@Override
			public String getAuthType() {
				return null;
			}

			@Override
			public Cookie[] getCookies() {
				return new Cookie[0];
			}

			@Override
			public long getDateHeader(String s) {
				return 0;
			}

			@Override
			public String getHeader(String s) {
				return null;
			}

			@Override
			public Enumeration getHeaders(String s) {
				return null;
			}

			@Override
			public Enumeration getHeaderNames() {
				return null;
			}

			@Override
			public int getIntHeader(String s) {
				return 0;
			}

			@Override
			public String getMethod() {
				return null;
			}

			@Override
			public String getPathInfo() {
				return null;
			}

			@Override
			public String getPathTranslated() {
				return null;
			}

			@Override
			public String getContextPath() {
				return null;
			}

			@Override
			public String getQueryString() {
				return null;
			}

			@Override
			public String getRemoteUser() {
				return null;
			}

			@Override
			public boolean isUserInRole(String s) {
				return false;
			}

			@Override
			public Principal getUserPrincipal() {
				return null;
			}

			@Override
			public String getRequestedSessionId() {
				return null;
			}

			@Override
			public String getRequestURI() {
				return null;
			}

			@Override
			public StringBuffer getRequestURL() {
				return null;
			}

			@Override
			public String getServletPath() {
				return null;
			}

			@Override
			public HttpSession getSession(boolean b) {
				return null;
			}

			@Override
			public HttpSession getSession() {
				return null;
			}

			@Override
			public boolean isRequestedSessionIdValid() {
				return false;
			}

			@Override
			public boolean isRequestedSessionIdFromCookie() {
				return false;
			}

			@Override
			public boolean isRequestedSessionIdFromURL() {
				return false;
			}

			@Override
			public boolean isRequestedSessionIdFromUrl() {
				return false;
			}

			@Override
			public Object getAttribute(String s) {
				return null;
			}

			@Override
			public Enumeration getAttributeNames() {
				return null;
			}

			@Override
			public String getCharacterEncoding() {
				return null;
			}

			@Override
			public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

			}

			@Override
			public int getContentLength() {
				return 0;
			}

			@Override
			public String getContentType() {
				return null;
			}

			@Override
			public ServletInputStream getInputStream() throws IOException {
				return null;
			}

			@Override
			public String getParameter(String s) {
				return null;
			}

			@Override
			public Enumeration getParameterNames() {
				return null;
			}

			@Override
			public String[] getParameterValues(String s) {
				return new String[0];
			}

			@Override
			public Map getParameterMap() {
				return null;
			}

			@Override
			public String getProtocol() {
				return null;
			}

			@Override
			public String getScheme() {
				return null;
			}

			@Override
			public String getServerName() {
				return null;
			}

			@Override
			public int getServerPort() {
				return 0;
			}

			@Override
			public BufferedReader getReader() throws IOException {
				return null;
			}

			@Override
			public String getRemoteAddr() {
				return null;
			}

			@Override
			public String getRemoteHost() {
				return null;
			}

			@Override
			public void setAttribute(String s, Object o) {

			}

			@Override
			public void removeAttribute(String s) {

			}

			@Override
			public Locale getLocale() {
				return null;
			}

			@Override
			public Enumeration getLocales() {
				return null;
			}

			@Override
			public boolean isSecure() {
				return false;
			}

			@Override
			public RequestDispatcher getRequestDispatcher(String s) {
				return null;
			}

			@Override
			public String getRealPath(String s) {
				return null;
			}

			@Override
			public int getRemotePort() {
				return 0;
			}

			@Override
			public String getLocalName() {
				return null;
			}

			@Override
			public String getLocalAddr() {
				return null;
			}

			@Override
			public int getLocalPort() {
				return 0;
			}
		};

		LoginObject loginObject = new LoginObject("t@gmail.com","IncorrectPassword");

		Response response = studentFacing.logoutUser(request ,loginObject);
		Assert.assertEquals(200, response.getStatus());

		studentLoginsDao.deleteStudentLogin("t@gmail.com");
		studentsDao.deleteStudent(TestStudent.getNeuId());
	}

	@Test
	public void StudentLogoutTest() throws SQLException, ParseException {
		Students TestStudent = new Students("130", "t@gmail.com", "test", "test",
				"test", Gender.M, "F1", "1111111111",
				"401 Terry Ave", "WA", "Seattle", "98109", Term.FALL, 2015,
				Term.SPRING, 2017,
				EnrollmentStatus.FULL_TIME, Campus.SEATTLE, DegreeCandidacy.MASTERS, null, true);
		studentsDao.addStudent(TestStudent);

		StudentLogins studentLogins = new StudentLogins("t@gmail.com",
				"password",
				"key",
				Timestamp.valueOf("2017-09-23 10:10:10.0"),
				Timestamp.valueOf("2017-09-23 10:10:10.0"),
				false);
		studentLoginsDao.createStudentLogin(studentLogins);

		HttpServletRequest request = new HttpServletRequest() {
			@Override
			public String getAuthType() {
				return null;
			}

			@Override
			public Cookie[] getCookies() {
				return new Cookie[0];
			}

			@Override
			public long getDateHeader(String s) {
				return 0;
			}

			@Override
			public String getHeader(String s) {
				return null;
			}

			@Override
			public Enumeration getHeaders(String s) {
				return null;
			}

			@Override
			public Enumeration getHeaderNames() {
				return null;
			}

			@Override
			public int getIntHeader(String s) {
				return 0;
			}

			@Override
			public String getMethod() {
				return null;
			}

			@Override
			public String getPathInfo() {
				return null;
			}

			@Override
			public String getPathTranslated() {
				return null;
			}

			@Override
			public String getContextPath() {
				return null;
			}

			@Override
			public String getQueryString() {
				return null;
			}

			@Override
			public String getRemoteUser() {
				return null;
			}

			@Override
			public boolean isUserInRole(String s) {
				return false;
			}

			@Override
			public Principal getUserPrincipal() {
				return null;
			}

			@Override
			public String getRequestedSessionId() {
				return null;
			}

			@Override
			public String getRequestURI() {
				return null;
			}

			@Override
			public StringBuffer getRequestURL() {
				return null;
			}

			@Override
			public String getServletPath() {
				return null;
			}

			@Override
			public HttpSession getSession(boolean b) {
				return null;
			}

			@Override
			public HttpSession getSession() {
				return null;
			}

			@Override
			public boolean isRequestedSessionIdValid() {
				return false;
			}

			@Override
			public boolean isRequestedSessionIdFromCookie() {
				return false;
			}

			@Override
			public boolean isRequestedSessionIdFromURL() {
				return false;
			}

			@Override
			public boolean isRequestedSessionIdFromUrl() {
				return false;
			}

			@Override
			public Object getAttribute(String s) {
				return null;
			}

			@Override
			public Enumeration getAttributeNames() {
				return null;
			}

			@Override
			public String getCharacterEncoding() {
				return null;
			}

			@Override
			public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

			}

			@Override
			public int getContentLength() {
				return 0;
			}

			@Override
			public String getContentType() {
				return null;
			}

			@Override
			public ServletInputStream getInputStream() throws IOException {
				return null;
			}

			@Override
			public String getParameter(String s) {
				return null;
			}

			@Override
			public Enumeration getParameterNames() {
				return null;
			}

			@Override
			public String[] getParameterValues(String s) {
				return new String[0];
			}

			@Override
			public Map getParameterMap() {
				return null;
			}

			@Override
			public String getProtocol() {
				return null;
			}

			@Override
			public String getScheme() {
				return null;
			}

			@Override
			public String getServerName() {
				return null;
			}

			@Override
			public int getServerPort() {
				return 0;
			}

			@Override
			public BufferedReader getReader() throws IOException {
				return null;
			}

			@Override
			public String getRemoteAddr() {
				return null;
			}

			@Override
			public String getRemoteHost() {
				return null;
			}

			@Override
			public void setAttribute(String s, Object o) {

			}

			@Override
			public void removeAttribute(String s) {

			}

			@Override
			public Locale getLocale() {
				return null;
			}

			@Override
			public Enumeration getLocales() {
				return null;
			}

			@Override
			public boolean isSecure() {
				return false;
			}

			@Override
			public RequestDispatcher getRequestDispatcher(String s) {
				return null;
			}

			@Override
			public String getRealPath(String s) {
				return null;
			}

			@Override
			public int getRemotePort() {
				return 0;
			}

			@Override
			public String getLocalName() {
				return null;
			}

			@Override
			public String getLocalAddr() {
				return null;
			}

			@Override
			public int getLocalPort() {
				return 0;
			}
		};

		LoginObject loginObject = new LoginObject("t@gmail.com","password");

		Response response = studentFacing.logoutUser(request ,loginObject);
		Assert.assertEquals(200, response.getStatus());

		studentLoginsDao.deleteStudentLogin("t@gmail.com");
		studentsDao.deleteStudent(TestStudent.getNeuId());
	}


	/*
    changePassword
	 */

	@Test
	public void ChangePasswordTest() throws SQLException, ParseException {

		PasswordChangeObject passwordChangeObject = new PasswordChangeObject("krishnakaranam3732@gmail.com",
				"password","newpassword");

		Response TopBachelorResponse;
		TopBachelorResponse = studentFacing.changeUserPassword(passwordChangeObject);
		Assert.assertEquals(200, TopBachelorResponse.getStatus());

	}

	@Test
	public void ChangePasswordBadTest() throws SQLException, ParseException {

		PasswordChangeObject passwordChangeObject = new PasswordChangeObject("krishnakaranam3732@gmail.com",
				"$s0$41010$cwF4TDlHcEf5+zxUKgsA3w==$vlMxt0lC641Vdavp9nclzELFgS3YgkuG9GBTgeUKfwQ=","newpassword");

		Response TopBachelorResponse;
		TopBachelorResponse = studentFacing.changeUserPassword(passwordChangeObject);
		Assert.assertEquals(400, TopBachelorResponse.getStatus());

	}

	/*
	 * Student Password Reset Test
	 */
	@Test
	public void emailForPasswordResetNullTest() throws SQLException, ParseException {

		PasswordResetObject passwordResetObject = new PasswordResetObject(null);

		Response TopBachelorResponse;
		TopBachelorResponse = studentFacing.sendEmailForPasswordResetStudent(passwordResetObject);
		Assert.assertEquals(400, TopBachelorResponse.getStatus());

	}

	@Test
	public void emailForPasswordResetNotExistTest() throws SQLException, ParseException {

		PasswordResetObject passwordResetObject = new PasswordResetObject("doesnotexist.com");

		Response TopBachelorResponse;
		TopBachelorResponse = studentFacing.sendEmailForPasswordResetStudent(passwordResetObject);
		Assert.assertEquals(404, TopBachelorResponse.getStatus());

	}

	@Test
	public void emailForPasswordResetFalseTest() throws SQLException, ParseException {
		Students TestStudent = new Students("130", "t@gmail.com", "test", "test",
				"test", Gender.M, "F1", "1111111111",
				"401 Terry Ave", "WA", "Seattle", "98109", Term.FALL, 2015,
				Term.SPRING, 2017,
				EnrollmentStatus.FULL_TIME, Campus.SEATTLE, DegreeCandidacy.MASTERS, null, true);
		studentsDao.addStudent(TestStudent);

		StudentLogins studentLogins = new StudentLogins("t@gmail.com",
				"123",
				"key",
				Timestamp.valueOf("2017-09-23 10:10:10.0"),
				Timestamp.valueOf("2017-09-23 10:10:10.0"),
				false);
		studentLoginsDao.createStudentLogin(studentLogins);

		PasswordResetObject passwordResetObject = new PasswordResetObject("t@gmail.com");

		Response response = studentFacing.sendEmailForPasswordResetStudent(passwordResetObject);
		Assert.assertEquals(404, response.getStatus());

		studentLoginsDao.deleteStudentLogin("t@gmail.com");
		studentsDao.deleteStudent(TestStudent.getNeuId());
	}

	/*
    AutoFill
	 */

	@Test
	public void AutoFillTest() throws SQLException, ParseException {
		Response TopBachelorResponse;
		TopBachelorResponse = studentFacing.getAutoFillSearch("Tom Cat 0012345671 tomcat@gmail.com");
		Assert.assertEquals(200, TopBachelorResponse.getStatus());
	}

	@Test
	public void AutoFillTest3() throws SQLException, ParseException {
		Response TopBachelorResponse;
		TopBachelorResponse = studentFacing.getAutoFillSearch("Tom Cat");
		Assert.assertEquals(200, TopBachelorResponse.getStatus());
	}

	@Test
	public void AutoFillTest2() throws SQLException, ParseException {

		Students TestStudent = new Students("020", "test@gmail.com", "test", "test",
				"test", Gender.M, "F1", "1111111111",
				"401 Terry Ave", "WA", "Seattle", "98109", Term.FALL, 2015,
				Term.SPRING, 2017,
				EnrollmentStatus.FULL_TIME, Campus.SEATTLE, DegreeCandidacy.MASTERS, null, true);
		studentsDao.addStudent(TestStudent);
		Response TopBachelorResponse;
		TopBachelorResponse = studentFacing.getAutoFillSearch("test test test 020 test@gmail.com");
		Assert.assertEquals(200, TopBachelorResponse.getStatus());

		studentsDao.deleteStudent(TestStudent.getNeuId());
	}
	
	/**
	 * Filter Student Test cases
	 */
	
//	@Test
//	public void filterTest1() throws SQLException, ParseException {
//		Response TopBachelorResponse;
//		TopBachelorResponse = studentFacing.getAutoFillSearch("Tom Cat 0012345671 tomcat@gmail.com");
//		Assert.assertEquals(200, TopBachelorResponse.getStatus());
//	}
}