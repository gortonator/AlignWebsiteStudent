package alignWebsite.student.services;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mehaexample.asdDemo.alignWebsite.StudentFacing;
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

	UndergraduatesPublicDao undergraduatesPublicDao = new UndergraduatesPublicDao(true);

	@BeforeClass
	public static void init() {
	}

	@Before
	public void setup() {
		studentFacing = new StudentFacing();
	}	

	@SuppressWarnings("unchecked")
	@Test
	public void registerStudent(){
		EmailToRegister emailToRegister = new EmailToRegister("meha@gmail.com");
		Response res = studentFacing.sendRegistrationEmail(emailToRegister);

		String response = (String) res.getEntity();

		Assert.assertEquals("To Register should be an Align Student!", response);

		Students newStudent = new Students("0000000", "meha@gmail.com", "Tom", "",
				"Cat", Gender.M, "F1", "1111111111",
				"401 Terry Ave", "WA", "Seattle", "98109", Term.FALL, 2015,
				Term.SPRING, 2017,
				EnrollmentStatus.FULL_TIME, Campus.SEATTLE, DegreeCandidacy.MASTERS, null, true);
		
	}

	@SuppressWarnings("unchecked")
	@Test
	public void registerStudent2(){
		EmailToRegister emailToRegister = new EmailToRegister("test.alignstudent123@gmail.com");
		Response res = studentFacing.sendRegistrationEmail(emailToRegister);

		String response = (String) res.getEntity();

		Assert.assertEquals("Registration link sent succesfully to test.alignstudent123@gmail.com" , response); 
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

	@SuppressWarnings("unchecked")
	@Test
	public void registerStudent5(){
		EmailToRegister emailToRegister = new EmailToRegister("doe.j@husky.neu.edu");
		Response res = studentFacing.sendRegistrationEmail(emailToRegister);

		String response = (String) res.getEntity();

		Assert.assertEquals("Student is Already Registered!doe.j@husky.neu.edu" , response); 
	}

}
