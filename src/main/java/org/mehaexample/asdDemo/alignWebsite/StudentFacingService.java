package org.mehaexample.asdDemo.alignWebsite;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import org.mehaexample.asdDemo.restModels.WorkExperienceObject;
import org.mehaexample.asdDemo.utils.MailClient;

import com.lambdaworks.crypto.SCryptUtil;

@Path("")
public class StudentFacingService {
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

	public StudentFacingService(){}

	/**
	 * This function gets the student details by NUID
	 * 
	 * http://localhost:8080/student-facing-align-website/webapi/student-facing/students/001234123
	 *
	 * @param nuid
	 * @return a student object
	 */
	@GET
	@Path("students/{nuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentProfile(@PathParam("nuid") String nuid) {
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
		Obj.put("ExtraExperiences", extraExperienceObj);
		Obj.put("Projects", projectObj);
		Obj.put("Courses", coursesObjArray);
		Obj.put("Privacies", privacyObject);

		return Response.status(Response.Status.OK).entity(Obj.toString()).build();
	}


	/**
	 * This function updates a student detail by NUID 
	 * 
	 * http://localhost:8181/webapi/student-facing/students/{NUID}
	 *
	 * @param neuId
	 * @param student
	 * @return 200 response if a student details are successfully updated 
	 */
	@PUT
	@Path("/students/{nuId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateStudentRecord(@PathParam("nuId") String neuId, Students student) {
		student.setNeuId(neuId);

		if (!studentDao.ifNuidExists(neuId)) {
			System.out.println("not found "+ neuId);
			return Response.status(Response.Status.NOT_FOUND).entity(NUIDNOTFOUND).build();
		}

		try{
			studentDao.updateStudentRecord(student);
		}catch(Exception ex){

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ex).build();
		}

		return Response.status(Response.Status.OK).entity(student).build(); 
	}

	/**
	 * This function creates an Extra Experience for a student
	 * 
	 * @param neuId
	 * @param extraExperiences
	 * @return 200 if the Extra Experience is created successfully 
	 * @throws ParseException
	 */
	@POST
	@Path("/students/{nuId}/extraexperiences")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addExtraExperience(@PathParam("nuId") String neuId, ExtraExperienceObject extraExperienceObject) {
		if (!studentDao.ifNuidExists(neuId)) {
			return Response.status(Response.Status.NOT_FOUND).entity(NUIDNOTFOUND).build();
		}

		ExtraExperiences experiences = new ExtraExperiences();  
		experiences.setCompanyName(extraExperienceObject.getCompanyName());
		experiences.setTitle(extraExperienceObject.getTitle());
		experiences.setDescription(extraExperienceObject.getDescription());
		experiences.setNeuId(neuId);

		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
		try {
			Date startDate = formatter.parse(extraExperienceObject.getStartDate());
			experiences.setStartDate(startDate);
		} catch (ParseException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Start Date didn't parse").build();
		}

		try {
			Date endDate = formatter.parse(extraExperienceObject.getEndDate());
			experiences.setEndDate(endDate);
		} catch (ParseException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("End Date didn't parse").build();
		}

		try{
			experiences = extraExperiencesDao.createExtraExperience(experiences);
		}catch(Exception ex){

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ex).build();
		}

		return Response.status(Response.Status.OK).entity(experiences.getExtraExperienceId()).build();
	}

	/**
	 * This function creates a project for a given student 
	 * 
	 * @param neuId
	 * @param project
	 * @return 200 response if the project is added successfully
	 * @throws ParseException
	 */
	@POST
	@Path("/students/{nuId}/projects")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addProject(@PathParam("nuId") String neuId, ProjectObject projectObject) {
		if (!studentDao.ifNuidExists(neuId)) {
			return Response.status(Response.Status.NOT_FOUND).entity(NUIDNOTFOUND).build();
		}

		Projects project = new Projects(); 
		project.setNeuId(neuId);
		project.setProjectName(projectObject.getProjectName()); 
		project.setDescription(projectObject.getDescription()); 

		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
		try {
			Date startDate = formatter.parse(projectObject.getStartDate());
			project.setStartDate(startDate);
		} catch (ParseException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Start Date didn't parse").build();
		}

		try {
			Date endDate = formatter.parse(projectObject.getEndDate());
			project.setEndDate(endDate);
		} catch (ParseException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("End Date didn't parse").build();
		}

		try{
			project = projectsDao.createProject(project);
		}catch(Exception ex) {

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ex).build();
		}

		return Response.status(Response.Status.OK).entity(project.getProjectId()).build();
	}

	/**
	 * This function gets all the Extra Experiences of a student 
	 * 
	 * @param neuId
	 * @param student
	 * @return 200 response if all the Extra Experiences retrieved successfully 
	 */
	@GET
	@Path("/students/{nuId}/extraexperiences") 
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentExtraExperience(@PathParam("nuId") String neuId) {
		List<ExtraExperiences> extraExperiencesList;
		if (!studentDao.ifNuidExists(neuId)) {

			return Response.status(Response.Status.NOT_FOUND).entity(NUIDNOTFOUND).build();
		} 

		try{
			extraExperiencesList = extraExperiencesDao.getExtraExperiencesByNeuId(neuId);
		}catch(Exception ex){

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ex).build();
		}

		if(extraExperiencesList == null || extraExperiencesList.isEmpty()){

			return Response.status(Response.Status.NOT_FOUND).
					entity("No Extra Experience record exists for a given NeuId: " + neuId).build();

		}

		List<ExtraExperienceObject> extraExperiencesObjectList = new ArrayList<>();

		for(ExtraExperiences experiences: extraExperiencesList){
			ExtraExperienceObject experienceObjectNew = new ExtraExperienceObject();

			experienceObjectNew.setCompanyName(experiences.getCompanyName()); 
			experienceObjectNew.setCompanyName(experiences.getCompanyName());
			experienceObjectNew.setTitle(experiences.getTitle());
			experienceObjectNew.setDescription(experiences.getDescription());
			experienceObjectNew.setNeuId(neuId);
			System.out.println("exitra id" + experiences.getExtraExperienceId());
			experienceObjectNew.setExtraExperienceId(experiences.getExtraExperienceId());  

			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");

			String startDateConverted = formatter.format(experiences.getStartDate());
			experienceObjectNew.setStartDate(startDateConverted);

			String endDateConverted = formatter.format(experiences.getEndDate());
			experienceObjectNew.setEndDate(endDateConverted);

			extraExperiencesObjectList.add(experienceObjectNew);
		}

		return Response.status(Response.Status.OK).entity(extraExperiencesObjectList).build();
	}

	/**
	 * This function gets all the Work Experiences for a student
	 * 
	 * @param neuId
	 * @param student
	 * @return 200 if all the work Experiences for a student are returned successfully
	 */
	@GET
	@Path("/students/{nuId}/workexperiences")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentWorkExperiences(@PathParam("nuId") String neuId) {
		List<WorkExperiences> workExperiencesList = null;
		if (!studentDao.ifNuidExists(neuId)) {
			return Response.status(Response.Status.NOT_FOUND).entity(NUIDNOTFOUND).build();
		} 

		try{
			workExperiencesList = workExperiencesDao.getWorkExperiencesByNeuId(neuId);
		}catch(Exception ex){

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ex).build();
		}

		if(workExperiencesList == null || workExperiencesList.isEmpty()){

			return  Response.status(Response.Status.NOT_FOUND).entity(NUIDNOTFOUND).build();
		}

		List<WorkExperienceObject> workExperiencesObjectList = new ArrayList<>();

		for(WorkExperiences experiences: workExperiencesList){
			WorkExperienceObject workExperienceObjectNew = new WorkExperienceObject();

			workExperienceObjectNew.setCompanyName(experiences.getCompanyName()); 
			workExperienceObjectNew.setCompanyName(experiences.getCompanyName());
			workExperienceObjectNew.setTitle(experiences.getTitle());
			workExperienceObjectNew.setDescription(experiences.getDescription());
			workExperienceObjectNew.setCoop(experiences.isCoop()); 
			workExperienceObjectNew.setCurrentJob(experiences.isCurrentJob());
			workExperienceObjectNew.setNeuId(neuId);
			workExperienceObjectNew.setWorkExperienceId(experiences.getWorkExperienceId()); 

			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");

			String startDateConverted = formatter.format(experiences.getStartDate());
			workExperienceObjectNew.setStartDate(startDateConverted);

			String endDateConverted = formatter.format(experiences.getEndDate());
			workExperienceObjectNew.setEndDate(endDateConverted);

			workExperiencesObjectList.add(workExperienceObjectNew);
		}

		return Response.status(Response.Status.OK).entity(workExperiencesObjectList).build();
	}

	/**
	 * This function updates an Extra Experience of student for a given ID
	 * 
	 * @param neuId
	 * @param extraExperienceId
	 * @return 200 response if the Extra Experience is updated successfully 
	 */
	@PUT
	@Path("/students/{nuId}/extraexperiences/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateExtraExperience(@PathParam("nuId") String neuId,
			@PathParam("id") int extraExperienceId, ExtraExperienceObject extraExperienceObject) {

		if (!studentDao.ifNuidExists(neuId)) {

			return Response.status(Response.Status.NOT_FOUND).entity(NUIDNOTFOUND).build();
		} 

		ExtraExperiences extraExperiences = extraExperiencesDao.getExtraExperienceById(extraExperienceId);
		if(extraExperiences == null){

			return Response.status(Response.Status.NOT_FOUND).
					entity("No Extra Experience record exists for a given Id: " + extraExperienceId).build(); 
		}

		ExtraExperiences experiences = new ExtraExperiences();  
		experiences.setCompanyName(extraExperienceObject.getCompanyName());
		experiences.setTitle(extraExperienceObject.getTitle());
		experiences.setDescription(extraExperienceObject.getDescription());
		experiences.setNeuId(neuId);
		experiences.setExtraExperienceId(extraExperienceId); 

		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
		try {
			Date startDate = formatter.parse(extraExperienceObject.getStartDate());
			experiences.setStartDate(startDate);
		} catch (ParseException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Start Date didn't parse").build();
		}

		try {
			Date endDate = formatter.parse(extraExperienceObject.getEndDate());
			experiences.setEndDate(endDate);
		} catch (ParseException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("End Date didn't parse").build();
		}

		try{
			extraExperiencesDao.updateExtraExperience(experiences);
		}catch(Exception ex){

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ex).build();
		}

		return Response.status(Response.Status.OK).entity("Experience updated successfully :").build(); 
	}

	/**
	 * This function updates a given project of a student 
	 * 
	 * @param neuId
	 * @param projectId
	 * @return 200 response if the project is updated successfully
	 */
	@PUT
	@Path("/students/{nuId}/projects/{Id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateProject(@PathParam("nuId") String neuId, 
			@PathParam("Id") Integer projectId, ProjectObject project) {
		if (!studentDao.ifNuidExists(neuId)) {

			return Response.status(Response.Status.NOT_FOUND).entity(NUIDNOTFOUND).build();
		} 

		Projects projects = projectsDao.getProjectById(projectId);
		if(projects == null){

			return Response.status(Response.Status.NOT_FOUND).entity("Project doesn't exists").build();
		}

		Projects projectUpdated = new Projects(); 
		projectUpdated.setNeuId(neuId);
		projectUpdated.setProjectName(project.getProjectName()); 
		projectUpdated.setDescription(project.getDescription()); 
		projectUpdated.setProjectId(projectId);

		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
		try {
			Date startDate = formatter.parse(project.getStartDate());
			projectUpdated.setStartDate(startDate);
		} catch (ParseException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Start Date didn't parse").build();
		}

		try {
			Date endDate = formatter.parse(project.getEndDate());
			projectUpdated.setEndDate(endDate);
		} catch (ParseException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("End Date didn't parse").build();
		}

		try{
			projectsDao.updateProject(projectUpdated);

		}catch(Exception ex) {

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ex).build();
		}

		return Response.status(Response.Status.OK).entity("Project updated successfully!").build();

	}


	/**
	 * This function deletes an Extra Experience of a student which they requested to delete
	 * 
	 * @param neuId
	 * @param extraExperienceId
	 * @return 200 response if the Extra Experience is deleted successfully 
	 */
	@DELETE
	@Path("/students/{nuId}/extraexperiences/{Id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteExtraExperience(@PathParam("nuId") String neuId, @PathParam("Id") Integer extraExperienceId) {
		ExtraExperiences extraExperiences = null;
		if (!studentDao.ifNuidExists(neuId)) {
			return Response.status(Response.Status.NOT_FOUND).entity(NUIDNOTFOUND).build();
		} 
		extraExperiences = extraExperiencesDao.getExtraExperienceById(extraExperienceId);

		if(extraExperiences == null){

			return Response.status(Response.Status.NOT_FOUND).entity("No Experience record exists with given ID").build();
		}

		try{
			extraExperiencesDao.deleteExtraExperienceById(extraExperienceId);
		}catch(Exception ex){

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ex).build();
		}

		return Response.status(Response.Status.OK).entity("Experience deleted successfully").build();
	}

	/**
	 * This function delete a given project of a student requested by them to be deleted 
	 * 
	 * @param neuId
	 * @param projectId
	 * @return 200 response if the project is deleted successfully 
	 */
	@DELETE
	@Path("/students/{nuId}/project/{Id}")
	@Produces(MediaType.APPLICATION_JSON) 
	public Response deleteProject(@PathParam("nuId") String neuId, @PathParam("Id") int projectId) {
		Projects project = null;
		if (!studentDao.ifNuidExists(neuId)) {

			return Response.status(Response.Status.NOT_FOUND).entity(NUIDNOTFOUND).build();
		}

		try{
			project = projectsDao.getProjectById(projectId);

		}catch(Exception ex){

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ex).build();
		}

		if(project == null){

			return Response.status(Response.Status.NOT_FOUND).
					entity("No project record exists with given ID: " + projectId).build();
		}
		projectsDao.deleteProjectById(projectId);

		return Response.status(Response.Status.OK).entity("Project deleted successfully").build(); 
	}

	/**
	 * This is a function to login using student email and password
	 * 
	 * http://localhost:8080/webapi/login
	 * @param passwordChangeObject
	 * @return the token if logged in successfully
	 */
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response loginUser(@Context HttpServletRequest request,LoginObject loginInput){
		StudentLogins studentLogins = studentLoginsDao.findStudentLoginsByEmail(loginInput.getUsername());
		if(studentLogins == null){

			return Response.status(Response.Status.NOT_FOUND).
					entity("User doesn't exist: " + loginInput.getUsername()).build();
		}

		boolean matched = false;
		try{
			String reqPass = loginInput.getPassword();
			String saltStr = loginInput.getUsername().substring(0, loginInput.getUsername().length()/2);
			String originalPassword = reqPass+saltStr;
			matched = SCryptUtil.check(originalPassword,studentLogins.getStudentPassword());
		} catch (Exception e){

			return Response.status(Response.Status.UNAUTHORIZED).
					entity(INCORRECTPASS).build();
		}

		if(matched){
			try {
				JSONObject jsonObj = new JSONObject();
				Timestamp keyGeneration = new Timestamp(System.currentTimeMillis());
				Timestamp keyExpiration = new Timestamp(System.currentTimeMillis()+15*60*1000);
				studentLogins.setLoginTime(keyGeneration);
				studentLogins.setKeyExpiration(keyExpiration);
				studentLoginsDao.updateStudentLogin(studentLogins);
				String ip = request.getRemoteAddr();
				JsonWebEncryption senderJwe = new JsonWebEncryption();
				senderJwe.setPlaintext(studentLogins.getEmail()+"*#*"+ip+"*#*"+keyGeneration.toString());
				senderJwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.DIRECT);
				senderJwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);

				String secretKey = ip+"sEcR3t_nsA-K3y";
				byte[] key = secretKey.getBytes();
				key = Arrays.copyOf(key, 32);
				AesKey keyMain = new AesKey(key);
				senderJwe.setKey(keyMain);
				String compactSerialization = senderJwe.getCompactSerialization();
				jsonObj.put("token", compactSerialization);
				//				Students student = studentDao.findStudentistratorByEmail(loginInput.getUsername());
				//				jsonObj.put("id", student.getStudentistratorNeuId());

				return Response.status(Response.Status.OK).
						entity(jsonObj.toString()).build();
			} catch (Exception e) {

				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
						entity(e).build();
			}
		}else{

			return Response.status(Response.Status.UNAUTHORIZED).
					entity(INCORRECTPASS).build();
		}
	}

	/**
	 * This is a function to logout for Student
	 * 
	 * http://localhost:8080/webapi/logout
	 * @param 
	 * @return 200 OK
	 */
	@POST
	@Path("/logout")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response logoutUser(@Context HttpServletRequest request,LoginObject loginInput){
		StudentLogins studentLogins = studentLoginsDao.findStudentLoginsByEmail(loginInput.getUsername());

		if(studentLogins == null){

			return Response.status(Response.Status.NOT_FOUND).
					entity("User doesn't exist: " + loginInput.getUsername()).build();
		}
		try{
			Timestamp keyExpiration = new Timestamp(System.currentTimeMillis());
			studentLogins.setKeyExpiration(keyExpiration);
			studentLoginsDao.updateStudentLogin(studentLogins);
		}
		catch (Exception e){

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(e).build();    
		}

		return Response.status(Response.Status.OK).
				entity("Logged Out Successfully").build();
	}

	/**
	 * This function sends the registration email to a student only if he/she is present in the align database
	 * 
	 * http://localhost:8080/alignWebsite/webapi/student-facing/registration
	 * test.alignstudent123@gmail.com
	 * 
	 * @param emailToRegister
	 * @return 200 if Registration link is sent successfully
	 */
	@POST
	@Path("/registration")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	// Send email to admin’s northeastern ID to reset the password.
	public Response sendRegistrationEmail(EmailToRegister emailToRegister){

		String studentEmail = emailToRegister.getEmail();

		// check if the email string is null or empty
		if (studentEmail == null || studentEmail.trim().length() == 0){  
			return Response.status(Response.Status.BAD_REQUEST).
					entity("Email Id can't be null or empty").build();
		}else{

			// check if the student is a valid Align student
			Students student = studentDao.getStudentRecordByEmailId(studentEmail);

			// check if the student record exists in the student database
			if(student == null){
				return Response.status(Response.Status.BAD_REQUEST).
						entity("To Register should be an Align Student!").build();
			}

			// check if the student is already registered
			StudentLogins studentLogin = studentLoginsDao.findStudentLoginsByEmail(studentEmail);

			// check if studenLogin either has  record for given email and "confirmed" is true
			if(studentLogin != null && (studentLogin.isConfirmed() == true)) {

				return Response.status(Response.Status.NOT_ACCEPTABLE).
						entity("Student is Already Registered!" + studentEmail).build();
			}

			StudentLogins studentLoginsNew = new StudentLogins();

			// generate registration key 
			String registrationKey = createRegistrationKey(); 

			// Create TimeStamp for Key Expiration for 15 min
			Timestamp keyExpirationTime = new Timestamp(System.currentTimeMillis()+ 15*60*1000);

			studentLoginsNew.setEmail(studentEmail); 
			studentLoginsNew.setStudentPassword("waitingForCreatePassword");
			studentLoginsNew.setConfirmed(false);
			studentLoginsNew.setRegistrationKey(registrationKey);
			studentLoginsNew.setKeyExpiration(keyExpirationTime);

			boolean success = false;
			if(studentLogin == null){

				StudentLogins studentLoginUpdatedWithoutPassword = studentLoginsDao.createStudentLogin(studentLoginsNew);
				if(studentLoginUpdatedWithoutPassword != null) {
					success = true;
				}
			}else{
				boolean studentLoginUpdatedWithoutPassword = studentLoginsDao.updateStudentLogin(studentLoginsNew);
				if(studentLoginUpdatedWithoutPassword == true) {
					success = true;
				}
			}

			// after record created without password, registration link will be sent
			if(success){
				MailClient.sendRegistrationEmail(studentEmail, registrationKey);

				return Response.status(Response.Status.OK).
						entity("Registration link sent succesfully to " + studentEmail).build();
			}

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity("Something Went Wrong" + studentEmail).build();
		}
	}

	/**
	 * This function creates the password and registers the student
	 * 
	 * @param passwordCreateObject
	 * @return 200 if password changed successfully else return 404
	 */
	@POST
	@Path("/password-create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createPassword(PasswordCreateObject passwordCreateObject){
		String email = passwordCreateObject.getEmail();
		String password = passwordCreateObject.getPassword();
		String registrationKey = passwordCreateObject.getRegistrationKey();

		// before create password, a student login should exist
		StudentLogins studentLoginsExisting = studentLoginsDao.findStudentLoginsByEmail(email);
		if(studentLoginsExisting == null) {

			return Response.status(Response.Status.BAD_REQUEST).
					entity("Invalid Student details. Student does not exist" ).build();
		}

		String databaseRegistrationKey = studentLoginsExisting.getRegistrationKey();
		Timestamp databaseTimestamp = studentLoginsExisting.getKeyExpiration();

		// check if the entered registration key matches 
		if((databaseRegistrationKey.equals(registrationKey))){
			// if registration key matches, then check if its valid or not
			Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

			String successMessage = "Congratulations Password created and Student registered successfully!";

			if(studentLoginsExisting.isConfirmed()){
				successMessage = "Password Reset successfully";
			}

			// check if the database time is after the current time
			if(databaseTimestamp.after(currentTimestamp)){
				String saltnewStr = email.substring(0, email.length()/2);
				String setPassword = password+saltnewStr;
				String hashedPassword = SCryptUtil.scrypt(setPassword, 16, 16, 16);

				studentLoginsExisting.setStudentPassword(hashedPassword);
				studentLoginsExisting.setConfirmed(true);

				boolean studentLoginUpdatedWithPassword = studentLoginsDao.updateStudentLogin(studentLoginsExisting);

				if(studentLoginUpdatedWithPassword) {

					return Response.status(Response.Status.OK).
							entity(successMessage).build();
				} else {

					return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
							entity("Database exception thrown" ).build();
				}
			} else {

				return Response.status(Response.Status.OK).
						entity(" Registration key expired!" ).build();
			}
		} else {

			return Response.status(Response.Status.BAD_REQUEST).
					entity("Invalid registration key" ).build();
		}
	}

	/**
	 * This is a function to change an existing students password
	 * 
	 * http://localhost:8080/alignWebsite/webapi/student-facing/password-change
	 * @param passwordChangeObject
	 * @return 200 if password changed successfully else return 404
	 */
	@POST
	@Path("/password-change")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeUserPassword(PasswordChangeObject passwordChangeObject){

		// check if the student login exists i.e the request is made by already registered student
		StudentLogins studentLogins = studentLoginsDao.findStudentLoginsByEmail(passwordChangeObject.getEmail());

		if(studentLogins == null){

			return Response.status(Response.Status.NOT_FOUND).
					entity("This Email doesn't exist: " + passwordChangeObject.getEmail()).build();
		}

		if(studentLogins.isConfirmed() == false){

			return Response.status(Response.Status.NOT_ACCEPTABLE).
					entity("Please create the password before reseting it! " + passwordChangeObject.getEmail()).build();
		}

		String enteredOldPassword = passwordChangeObject.getOldPassword();

		String enteredNewPassword = passwordChangeObject.getNewPassword();

		if(enteredOldPassword.equals(enteredNewPassword)){

			return Response.status(Response.Status.NOT_ACCEPTABLE).
					entity("The New Password can't be same as Old passoword ").build();
		}

		String saltnewStr = passwordChangeObject.getEmail().substring(0, passwordChangeObject.getEmail().length()/2);

		String setPassword = enteredOldPassword + saltnewStr;

		String convertOldPasswordToHash = SCryptUtil.scrypt(setPassword, 16, 16, 16);

		boolean matched = false;
		try{
			String reqPass = passwordChangeObject.getOldPassword();
			String saltStr = passwordChangeObject.getEmail().substring(0, passwordChangeObject.getEmail().length()/2);
			String originalPassword = reqPass+saltStr;
			matched = SCryptUtil.check(originalPassword,studentLogins.getStudentPassword());
		} catch (Exception e){

			return Response.status(Response.Status.UNAUTHORIZED).
					entity(INCORRECTPASS).build();
		}

		// check if the entered old password is correct
		if(matched){

			String newPass = passwordChangeObject.getNewPassword();
			String saltnewStr2 = passwordChangeObject.getEmail().substring(0, passwordChangeObject.getEmail().length()/2);
			String updatePassword = newPass+saltnewStr2;
			String generatedSecuredPasswordHash = SCryptUtil.scrypt(updatePassword, 16, 16, 16);

			//			String hashNewPassword = StringUtils.createHash(passwordChangeObject.getNewPassword());

			studentLogins.setStudentPassword(generatedSecuredPasswordHash);
			studentLoginsDao.updateStudentLogin(studentLogins);

			return Response.status(Response.Status.OK).
					entity("Password Changed Succesfully!" ).build();
		}else{
			return Response.status(Response.Status.BAD_REQUEST).
					entity("Incorrect old Password: ").build();
		}
	}

	/**
	 * This function sends email to Student's northeastern ID to reset the password.
	 * 
	 * @param adminEmail
	 * @return 200 if password changed successfully else return 404
	 */
	@POST
	@Path("/password-reset")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendEmailForPasswordResetStudent(PasswordResetObject passwordResetObject){

		String studentEmail = passwordResetObject.getEmail();

		if (studentEmail == null){

			return Response.status(Response.Status.BAD_REQUEST).
					entity("Email Id can't be null").build();
		}else{

			StudentLogins studentLogins = studentLoginsDao.findStudentLoginsByEmail(studentEmail);

			// Check if student has Registered or not
			if(studentLogins == null){

				return Response.status(Response.Status.NOT_FOUND).
						entity("Email doesn't exist, Please enter a valid Email Id " + studentLogins).build();
			}

			if(studentLogins.isConfirmed() == false){

				return Response.status(Response.Status.NOT_FOUND).
						entity("Password can't be reset....Please create password and register first: ").build();
			}

			// generate registration key 
			String registrationKey = createRegistrationKey(); 

			// Create TimeStamp for Key Expiration for 15 min
			Timestamp keyExpirationTime = new Timestamp(System.currentTimeMillis()+ 15*60*1000);

			StudentLogins studentLoginsNew = new StudentLogins();

			studentLoginsNew.setEmail(studentEmail);
			studentLoginsNew.setStudentPassword(studentLogins.getStudentPassword()); 
			studentLoginsNew.setLoginTime(studentLogins.getLoginTime()); 
			studentLoginsNew.setRegistrationKey(registrationKey);
			studentLoginsNew.setKeyExpiration(keyExpirationTime);
			studentLoginsNew.setConfirmed(true);

			boolean studentLoginUpdated = studentLoginsDao.updateStudentLogin(studentLoginsNew);

			if(studentLoginUpdated) {
				// after generation, send email
				MailClient.sendPasswordResetEmail(studentEmail, registrationKey);

				return Response.status(Response.Status.OK).
						entity("Password Reset link sent succesfully!" ).build(); 
			}

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity("Something Went Wrong" + studentEmail).build();
		}
	}

	/**
	 * This function adds privacy for a given student
	 * 
	 * @param privacy
	 * @return 200 Response if the privacy is created successfully for the given student
	 */
	@POST
	@Path("/students/{NUID}/privacies/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addPrivacy(@PathParam("NUID") String neuId, Privacies privacy){
		Privacies privacies = null;

		Students student = studentDao.getStudentRecord(neuId);

		if (!studentDao.ifNuidExists(neuId)) {

			return Response.status(Response.Status.NOT_FOUND).entity(NUIDNOTFOUND).build();
		}

		privacy.setNeuId(student.getNeuId());
		privacy.setPublicId(student.getPublicId()); 

		try{
			privacies = privaciesDao.createPrivacy(privacy);
		}catch(Exception ex) {

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ex).build();
		}

		return Response.status(Response.Status.OK).entity("Privacies added successfully!").build();
	}

	/**
	 * This function gets the privacy setting for a student 
	 * 
	 * @param neuId
	 * @return 200 response if privacy setting is retrieved successfully 
	 */
	@GET
	@Path("/students/{nuId}/privacies") 
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStduentPrivacies(@PathParam("nuId") String neuId) {
		Privacies privacy = null;
		if (!studentDao.ifNuidExists(neuId)) {

			return Response.status(Response.Status.NOT_FOUND).entity(NUIDNOTFOUND).build();
		} 

		try{
			privacy = privaciesDao.getPrivacyByNeuId(neuId);

		}catch(Exception ex){

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ex).build();
		}

		if(privacy == null){

			return Response.status(Response.Status.NOT_FOUND).
					entity("No privacy setting exists for a given NeuId: " + neuId).build();

		}

		return Response.status(Response.Status.OK).entity(privacy).build();
	}

	/**
	 * This function updates an Extra Experience of student for a given ID
	 * 
	 * @param neuId
	 * @param extraExperienceId
	 * @return 200 response if the Extra Experience is updated successfully 
	 */
	@PUT
	@Path("/students/{NUID}/privacies/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updatePrivacies(@PathParam("NUID") String neuId, Privacies privacies) { 
		if (!studentDao.ifNuidExists(neuId)) {

			return Response.status(Response.Status.NOT_FOUND).entity(NUIDNOTFOUND).build();
		} 

		Students student = studentDao.getStudentRecord(neuId);
		privacies.setNeuId(student.getNeuId());
		privacies.setPublicId(student.getPublicId()); 

		Privacies privacy =  privaciesDao.getPrivacyByNeuId(neuId);
		if(privacy == null){

			return Response.status(Response.Status.NOT_FOUND).
					entity("No Privacies record exists for a given NUID").build(); 
		}

		try{
			privaciesDao.updatePrivacy(privacies);
		}catch(Exception ex){

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ex).build();
		}

		return Response.status(Response.Status.OK).entity("Privacies Updated Successfully!").build(); 
	}

	/**
	 * This function search for the other students
	 * 
	 * @param search
	 * @return 200 Response if all the filtered students are returned successfully 
	 */
	@POST
	@Path("/students")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchStudent(SearchOtherStudents search){

		List<Students> studentRecords;
		Map<String,List<String>> map = new HashMap<String,List<String>>();

		try{
			if (search.getCompanyName()!= null && !search.getCompanyName().isEmpty()) { 
				map.put("companyName",search.getCompanyName());
			}

			if (search.getCourseName()!= null && !search.getCourseName().isEmpty()){
				map.put("courseName",search.getCourseName());
			}

			if (search.getStartTerm()!= null && !search.getStartTerm().isEmpty()){
				map.put("startTerm",search.getStartTerm());
			}

			if (search.getEndTerm()!= null && !search.getEndTerm().isEmpty()){
				map.put("endTerm",search.getEndTerm());
			}

			if (search.getCampus()!= null && !search.getCampus().isEmpty()){
				map.put("campus",search.getCampus());
			}

			if (search.getGender()!= null && !search.getGender().isEmpty()){
				map.put("gender",search.getGender());
			}

		}catch (Exception e){
			return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
		}

		try {
			studentRecords = (ArrayList<Students>) studentDao.getStudentFilteredStudents(map, 0, 20);
		} catch (Exception e) {

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
		}

		JSONObject studentsRecordObj = new JSONObject();
		studentsRecordObj.put("studentsRecord", studentRecords);

		return Response.status(Response.Status.OK).entity(studentsRecordObj.toString()).build(); 
	}

	private String createRegistrationKey() {

		return UUID.randomUUID().toString();
	}	
}
