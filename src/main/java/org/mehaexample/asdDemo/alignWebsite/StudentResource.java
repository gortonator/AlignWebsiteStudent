package org.mehaexample.asdDemo.alignWebsite;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mehaexample.asdDemo.dao.alignprivate.*;
import org.mehaexample.asdDemo.model.alignprivate.*;

@Path("student-facing")
public class StudentResource {
    StudentsDao studentDao = new StudentsDao(true);
    ElectivesDao electivesDao = new ElectivesDao(true);
    CoursesDao coursesDao = new CoursesDao(true);
    WorkExperiencesDao workExperiencesDao = new WorkExperiencesDao(true);
    ExtraExperiencesDao extraExperiencesDao = new ExtraExperiencesDao(true);


    /**
     * uopdate student details.
     * <p>
     * http://localhost:8181/webapi/student-facing/students/{NUID}
     *
     * @param neuId
     * @param student
     * @return
     */
    @PUT
    @Path("/students/{nuId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStudentRecord(@PathParam("nuId") String neuId, Students student) {
        student = studentDao.getStudentRecord(neuId);

        if (!studentDao.ifNuidExists(neuId)) {
            return Response.status(Response.Status.NOT_FOUND).entity("No Student record exists with given ID").build();
        }
        studentDao.updateStudentRecord(student);
        return Response.status(Response.Status.OK).entity("Student record updated successfully").build();
    }


    @GET
    @Path("/students/{nuId}/courses")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudentCourses(@PathParam("nuId") String neuId, Students student) {
        ArrayList<String> courses = new ArrayList<>();
        if (!studentDao.ifNuidExists(neuId)) {
            return Response.status(Response.Status.NOT_FOUND).entity("No Student record exists with given ID").build();
        } else {

            List<Electives> electives;
            electives = electivesDao.getElectivesByNeuId(neuId);

            for (int i = 0; i < electives.size(); i++) {
                Electives elective = electivesDao.getElectiveById(electives.get(i).getElectiveId());
                Courses course = coursesDao.getCourseById(elective.getCourseId());
                courses.add(course.getCourseName());
            }
        }
        JSONArray resultArray = new JSONArray();
        for (String course : courses) {
            resultArray.put(course);
        }
        return Response.status(Response.Status.OK).entity(resultArray.toString()).build();
    }


    @GET
    @Path("/students/{nuId}/workexperiences")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudentWorkExperience(@PathParam("nuId") String neuId, Students student) {
        List<WorkExperiences> workExperiencesList;
        if (!studentDao.ifNuidExists(neuId)) {
            return Response.status(Response.Status.NOT_FOUND).entity("No Student record exists with given ID").build();
        } else {
            workExperiencesList = workExperiencesDao.getWorkExperiencesByNeuId(neuId);
        }
        JSONObject jsonObj = new JSONObject(workExperiencesList);
        jsonObj.put("student", workExperiencesList);
        return Response.status(Response.Status.OK).entity(jsonObj.toString()).build();
    }

    /**
     * @param neuId
     * @param student
     * @return
     */
    @GET
    @Path("/students/{nuId}/extraexperiences")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudentExtraExperience(@PathParam("nuId") String neuId, Students student) {

        List<ExtraExperiences> extraExperiencesList;
        if (!studentDao.ifNuidExists(neuId)) {
            return Response.status(Response.Status.NOT_FOUND).entity("No Student record exists with given ID").build();
        } else {
            extraExperiencesList = extraExperiencesDao.getExtraExperiencesByNeuId(neuId);
        }

        JSONObject jsonObj = new JSONObject(extraExperiencesList);
        jsonObj.put("student", extraExperiencesList);

        return Response.status(Response.Status.OK).entity(jsonObj.toString()).build();
    }

    /**
     * @param neuId
     * @param extraExperienceId
     * @param student
     * @return
     */

    @DELETE
    @Path("/students/{nuId}/extraexperiences/{Id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteExtraExperience(@PathParam("nuId") String neuId, @PathParam("Id") Integer extraExperienceId, Students student) {

        List<ExtraExperiences> extraExperiencesList;
        if (!studentDao.ifNuidExists(neuId)) {
            return Response.status(Response.Status.NOT_FOUND).entity("No Student record exists with given ID").build();
        } else {
            extraExperiencesList = extraExperiencesDao.getExtraExperiencesByNeuId(neuId);
            for (int i = 0; i < extraExperiencesList.size(); i++) {
                if (extraExperiencesList.get(i).getExtraExperienceId() == extraExperienceId) {
                    extraExperiencesDao.deleteExtraExperienceById(extraExperienceId);
                    return Response.status(Response.Status.OK).entity("Experience deleted successfully").build();
                } else {
                    return Response.status(Response.Status.NOT_FOUND).entity("No Experience record exists with given ID").build();
                }
            }
        }
        return Response.status(Response.Status.OK).entity("Experience deleted successfully").build();
    }

    /**
     * @param neuId
     * @param extraExperienceId
     * @param student
     * @return
     */
    @PUT
    @Path("/students/{nuId}/extraexperiences/{Id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateExtraExperience(@PathParam("nuId") String neuId, @PathParam("Id") Integer extraExperienceId, Students student) {

        List<ExtraExperiences> extraExperiencesList;
        if (!studentDao.ifNuidExists(neuId)) {
            return Response.status(Response.Status.NOT_FOUND).entity("No Student record exists with given ID").build();
        } else {
            extraExperiencesList = extraExperiencesDao.getExtraExperiencesByNeuId(neuId);
            for (int i = 0; i < extraExperiencesList.size(); i++) {
                if (extraExperiencesList.get(i).getExtraExperienceId() == extraExperienceId) {
                    extraExperiencesDao.updateExtraExperience(extraExperiencesList.get(i));
                    return Response.status(Response.Status.OK).entity("Experience updated successfully :)").build();
                } else {
                    return Response.status(Response.Status.NOT_FOUND).entity("No Experience record exists with given ID").build();
                }
            }
        }
        return Response.status(Response.Status.OK).entity("Experience updated successfully").build();
    }

//    @POST
//    @Path("/students/{nuId}/extraexperiences")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response AddExtraExperience(@PathParam("nuId") String neuId, ExtraExperiences extraExperiences) {
//        List<ExtraExperiences> extraExperiencesList;
//        System.out.println(neuId);
//        extraExperiences.setNeuId(neuId);
//        extraExperiencesDao.createExtraExperience(extraExperiences);
//
//        int id = extraExperiences.getExtraExperienceId();
//
//        extraExperiencesList = extraExperiencesDao.getExtraExperiencesByNeuId(neuId);
//
//        for (int i = 0; i < extraExperiencesList.size(); i++) {
//            if (extraExperiencesList.get(i).getExtraExperienceId() == id) {
//                return Response.status(Response.Status.OK).entity("Experience added successfully :)").build();
//            } else {
//                return Response.status(Response.Status.NOT_FOUND).entity("Experience not added successfully").build();
//            }
//        }
//        return Response.status(Response.Status.OK).entity("Experience added successfully").build();
//    }

    @POST
    @Path("/students/{nuId}/extraexperiences")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response AddExtraExperience(@PathParam("nuId") String neuId, ExtraExperiences extraExperiences) {
//        List<ExtraExperiences> extraExperiencesList;
        Date Enddate = new Date("09/09/2018");
        Date Startdate = new Date("09/12/2018");
//
//        System.out.println(neuId);
        extraExperiences.setExtraExperienceId(90);
        extraExperiences.setNeuId("080");
        extraExperiences.setCompanyName("zillow");
        extraExperiences.setEndDate(Enddate);
        extraExperiences.setStartDate(Startdate);
        extraExperiences.setTitle("SDE");
        extraExperiences.setDescription("intern");

        extraExperiencesDao.createExtraExperience(extraExperiences);
//        int id = extraExperiences.getExtraExperienceId();
//        extraExperiencesList = extraExperiencesDao.getExtraExperiencesByNeuId(neuId);
//
//        for (int i = 0; i < extraExperiencesList.size(); i++) {
//            if (extraExperiencesList.get(i).getExtraExperienceId() == id) {
//                return Response.status(Response.Status.OK).entity("Experience added successfully :)").build();
//            } else {
//                return Response.status(Response.Status.NOT_FOUND).entity("Experience not added successfully").build();
//            }
//        }
        return Response.status(Response.Status.OK).entity("Experience added successfully").build();
    }

    /**
     * retrive student details by NUID
     * <p>
     * http://localhost:8080/webapi/student-facing/students/{NUID}
     *
     * @param nuid
     * @return a student object
     */
    @GET
    @Path("students/{nuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudentProfile(@PathParam("nuid") String nuid) {
        if (!studentDao.ifNuidExists(nuid)) {
            return Response.status(Response.Status.NOT_FOUND).entity("No Student record exists with given ID").build();
        } else {
            Students studentRecord = studentDao.getStudentRecord(nuid);
            JSONObject jsonObj = new JSONObject(studentRecord);
            jsonObj.put("student", studentRecord);
            return Response.status(Response.Status.OK).entity(jsonObj.toString()).build();
        }
    }
}
