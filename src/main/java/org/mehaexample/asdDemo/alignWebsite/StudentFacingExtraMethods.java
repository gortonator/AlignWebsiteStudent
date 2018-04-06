package org.mehaexample.asdDemo.alignWebsite;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response; 

import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.keys.AesKey;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mehaexample.asdDemo.dao.alignprivate.CoursesDao;
import org.mehaexample.asdDemo.dao.alignprivate.ElectivesDao;
import org.mehaexample.asdDemo.dao.alignprivate.ExtraExperiencesDao;
import org.mehaexample.asdDemo.dao.alignprivate.PrivaciesDao;
import org.mehaexample.asdDemo.dao.alignprivate.ProjectsDao;
import org.mehaexample.asdDemo.dao.alignprivate.StudentLoginsDao;
import org.mehaexample.asdDemo.dao.alignprivate.StudentsDao;
import org.mehaexample.asdDemo.dao.alignprivate.WorkExperiencesDao;
import org.mehaexample.asdDemo.model.alignadmin.LoginObject;
import org.mehaexample.asdDemo.model.alignprivate.Courses;
import org.mehaexample.asdDemo.model.alignprivate.Electives;
import org.mehaexample.asdDemo.model.alignprivate.ExtraExperiences;
import org.mehaexample.asdDemo.model.alignprivate.Privacies;
import org.mehaexample.asdDemo.model.alignprivate.Projects;
import org.mehaexample.asdDemo.model.alignprivate.StudentLogins;
import org.mehaexample.asdDemo.model.alignprivate.Students;
import org.mehaexample.asdDemo.model.alignprivate.WorkExperiences;
import org.mehaexample.asdDemo.restModels.EmailToRegister;
import org.mehaexample.asdDemo.restModels.ExtraExperienceObject;
import org.mehaexample.asdDemo.restModels.PasswordChangeObject;
import org.mehaexample.asdDemo.restModels.PasswordCreateObject;
import org.mehaexample.asdDemo.restModels.PasswordResetObject;
import org.mehaexample.asdDemo.restModels.ProjectObject;
import org.mehaexample.asdDemo.restModels.SearchOtherStudents;
import org.mehaexample.asdDemo.restModels.StudentProfile;
import org.mehaexample.asdDemo.utils.MailClient;

import com.lambdaworks.crypto.SCryptUtil;

public class StudentFacingExtraMethods {
	StudentsDao studentDao = new StudentsDao();
	ElectivesDao electivesDao = new ElectivesDao();
	CoursesDao coursesDao = new CoursesDao();
	WorkExperiencesDao workExperiencesDao = new WorkExperiencesDao();
	ExtraExperiencesDao extraExperiencesDao = new ExtraExperiencesDao();
	ProjectsDao projectsDao = new ProjectsDao();
	StudentLoginsDao studentLoginsDao = new StudentLoginsDao(); 
	PrivaciesDao privaciesDao = new PrivaciesDao();
	private static String NUIDNOTFOUND = "No Student record exists with given ID"; 
	private static String INCORRECTPASS = "Incorrect Password";
	
	/**
	 * This function creates a new student record
	 * 
	 * @param student
	 * @return 200 Response if the student is created successfully
	 */
	@POST
	@Path("students/{nuid}/add")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createStudent(Students student){
		boolean exists = studentDao.ifNuidExists(student.getNeuId());
		if(!exists){
			try{
				studentDao.addStudent(student);
			}catch(Exception ex){

				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
						entity(ex.toString()).build();
			}

			return Response.status(Response.Status.OK).entity("Student created successfully").build(); 
		}else{

			return Response.status(Response.Status.BAD_REQUEST).entity("The entered NUID exists already").build(); 
		}
	}

	/**
	 * This function deletes a student record for a given NeuId
	 * 
	 * @param nuid
	 * @return 200 Response if the student record is deleted successfully
	 */
	@DELETE
	@Path("deletestudent/{nuid}")
	@Produces({ MediaType.APPLICATION_JSON})
	public Response deleteStudentByNuid(@PathParam("nuid") String nuid)
	{
		boolean exists = studentDao.ifNuidExists(nuid);

		if(!exists){

			return Response.status(Response.Status.BAD_REQUEST).entity("This nuid doesn't exist").build(); 	
		}

		try{
			studentDao.deleteStudent(nuid);
			return Response.status(Response.Status.OK).entity("Student deleted successfully").build(); 
		}catch(Exception ex){

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ex).build();
		}		
	} 
	
	/**
	 * This function get the courses taken by a student 
	 * 
	 * @param neuId
	 * @return 200 if all the student courses are returned successfully 
	 */
	@GET
	@Path("/students/{nuId}/courses")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentCourses(@PathParam("nuId") String neuId) {
		ArrayList<String> courses = new ArrayList<>();
		List<Electives> electives;
		if (!studentDao.ifNuidExists(neuId)) {
			return Response.status(Response.Status.NOT_FOUND).entity(NUIDNOTFOUND).build();
		} 

		try{
			electives = electivesDao.getElectivesByNeuId(neuId);
		}catch(Exception ex){

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ex).build();
		}

		if(electives == null || electives.isEmpty()){

			return Response.status(Response.Status.NOT_FOUND).
					entity("No electives were found for the given NeuId").build();
		}

		for (int i = 0; i < electives.size(); i++) {
			Electives elective = electivesDao.getElectiveById(electives.get(i).getElectiveId());
			Courses course = coursesDao.getCourseById(elective.getCourseId());
			courses.add(course.getCourseName());
		}

		return Response.status(Response.Status.OK).entity(courses).build();
	}
	
	/**
	 * This function gets the student details by NUID
	 * 
	 * http://localhost:8080/student-facing-align-website/webapi/student-facing/students/001234123
	 *
	 * @param nuid
	 * @return a student object
	 */
	@GET
	@Path("students2/{nuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentProfile2(@PathParam("nuid") String nuid) {
		Students studentRecord = null;
		Privacies privacy = null;
		if (!studentDao.ifNuidExists(nuid)) {

			return Response.status(Response.Status.NOT_FOUND).entity(NUIDNOTFOUND + ":" + nuid).build();
		} 

		try{
			studentRecord = studentDao.getStudentRecord(nuid);
		}catch(Exception ex){

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ex).build();
		}

		if(studentRecord == null){

			return Response.status(Response.Status.NOT_FOUND).entity(NUIDNOTFOUND).build();
		}

		try{
			privacy = privaciesDao.getPrivacyByNeuId(nuid);
		}catch(Exception ex) {

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ex).build();
		}

		if(privacy == null){

			return Response.status(Response.Status.NOT_FOUND).
					entity("Privacy setting not found for the given student").build();
		}

		List<WorkExperiences> workExperiencesRecord = workExperiencesDao.getWorkExperiencesByNeuId(nuid);
		List<Projects> projects = projectsDao.getProjectsByNeuId(nuid);
		List<ExtraExperiences> extraExperiences = extraExperiencesDao.getExtraExperiencesByNeuId(nuid);
		List<Courses> courses = new ArrayList<>(); 
		List<Electives> electives = electivesDao.getElectivesByNeuId(nuid);

		JSONArray coursesObjArray = new JSONArray();
		for (int i = 0; i < electives.size(); i++) {
			JSONObject jsonObj = new JSONObject();
			Electives elective = electivesDao.getElectiveById(electives.get(i).getElectiveId());
			Courses course = coursesDao.getCourseById(elective.getCourseId());
			jsonObj.put("courseName", course.getCourseName());
			jsonObj.put("courseId", course.getCourseId());
			jsonObj.put("description", course.getDescription());

			coursesObjArray.put(jsonObj);
		}

		JSONObject Obj = new JSONObject();

		JSONArray workExperienceObj = new JSONArray();

		for(WorkExperiences workExp : workExperiencesRecord){
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("workExperienceId", workExp.getWorkExperienceId());
			jsonObj.put("neuId", workExp.getNeuId());
			jsonObj.put("companyName", workExp.getCompanyName());
			jsonObj.put("startDate", workExp.getStartDate());
			jsonObj.put("endDate", workExp.getEndDate());
			jsonObj.put("currentJob", workExp.isCurrentJob());
			jsonObj.put("coop", workExp.isCoop());
			jsonObj.put("title", workExp.getTitle());
			jsonObj.put("description", workExp.getDescription());
			workExperienceObj.put(jsonObj); 
		}

		JSONArray extraExperienceObj = new JSONArray();

		for(ExtraExperiences extraExperience : extraExperiences){
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("companyName", extraExperience.getCompanyName());
			jsonObj.put("description", extraExperience.getDescription());
			jsonObj.put("endDate", extraExperience.getEndDate());
			jsonObj.put("extraExperienceId", extraExperience.getExtraExperienceId());
			jsonObj.put("startDate", extraExperience.getStartDate());
			jsonObj.put("title", extraExperience.getTitle());
			jsonObj.put("neuId", extraExperience.getNeuId());
			extraExperienceObj.put(jsonObj); 
		}

		JSONArray projectObj = new JSONArray();

		for(Projects project: projects){
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("projectName", project.getProjectName());
			jsonObj.put("description", project.getDescription());
			jsonObj.put("endDate", project.getEndDate());
			jsonObj.put("projectId", project.getProjectId());
			jsonObj.put("startDate", project.getStartDate());
			jsonObj.put("neuId", project.getNeuId());
			projectObj.put(jsonObj); 
		}

		// add privacy
		JSONObject privacyObject = new JSONObject();
		privacyObject.put("neuId", privacy.getNeuId());
		privacyObject.put("publicId", privacy.getPublicId());
		privacyObject.put("visibleToPublic", privacy.isVisibleToPublic());
		privacyObject.put("photo", privacy.isPhoto());
		privacyObject.put("email", privacy.isEmail());
		privacyObject.put("address", privacy.isAddress());
		privacyObject.put("linkedin", privacy.isLinkedin());
		privacyObject.put("github", privacy.isGithub());
		privacyObject.put("facebook", privacy.isFacebook());
		privacyObject.put("website", privacy.isWebsite());
		privacyObject.put("course", privacy.isCourse());
		privacyObject.put("extraExperience", privacy.isExtraExperience());
		privacyObject.put("project", privacy.isProject());
		privacyObject.put("skill", privacy.isSkill());

		JSONObject studentObj = new JSONObject();
		studentObj.put("neuId", studentRecord.getNeuId());
		studentObj.put("publicId", studentRecord.getPublicId());
		studentObj.put("email", studentRecord.getEmail());
		studentObj.put("firstName", studentRecord.getFirstName());
		studentObj.put("middleName", studentRecord.getMiddleName());
		studentObj.put("lastName", studentRecord.getLastName());
		studentObj.put("gender", studentRecord.getGender());
		studentObj.put("race", studentRecord.getRace());
		studentObj.put("scholarship", studentRecord.isScholarship());
		studentObj.put("visa", studentRecord.getVisa());
		studentObj.put("phoneNum", studentRecord.getPhoneNum());
		studentObj.put("address", studentRecord.getAddress());
		studentObj.put("state", studentRecord.getState());
		studentObj.put("city", studentRecord.getCity());
		studentObj.put("zip", studentRecord.getZip());
		studentObj.put("entryTerm", studentRecord.getEntryTerm());
		studentObj.put("entryYear", studentRecord.getEntryYear());
		studentObj.put("expectedLastTerm", studentRecord.getExpectedLastTerm());
		studentObj.put("expectedLastYear", studentRecord.getExpectedLastYear());
		studentObj.put("enrollmentStatus", studentRecord.getEnrollmentStatus());
		studentObj.put("campus", studentRecord.getCampus());
		studentObj.put("degree", studentRecord.getDegree());
		studentObj.put("photo", studentRecord.getPhoto());
		studentObj.put("visible", studentRecord.isVisible());
		studentObj.put("linkedin", studentRecord.getLinkedin());
		studentObj.put("facebook", studentRecord.getFacebook());
		studentObj.put("github", studentRecord.getGithub());
		studentObj.put("website", studentRecord.getWebsite());
		studentObj.put("skills", studentRecord.getSkills());
		studentObj.put("summary", studentRecord.getSummary());

		Obj.put("studentRecord", studentObj);
		Obj.put("WorkExperiences", workExperienceObj);
		Obj.put("extraExperienceObj", extraExperienceObj);
		Obj.put("Projects", projectObj);
		Obj.put("Courses", coursesObjArray);
		Obj.put("Privacies", privacyObject);

		return Response.status(Response.Status.OK).entity(Obj.toString()).build();
	}
}
