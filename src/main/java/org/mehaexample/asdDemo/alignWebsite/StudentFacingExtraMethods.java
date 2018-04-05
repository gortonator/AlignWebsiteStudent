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
}
