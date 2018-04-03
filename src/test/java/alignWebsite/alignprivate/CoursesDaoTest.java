package alignWebsite.alignprivate;

import java.util.List;

import org.hibernate.HibernateException;
import org.junit.*;
import org.mehaexample.asdDemo.model.alignprivate.Courses;
import org.mehaexample.asdDemo.dao.alignprivate.CoursesDao;

public class CoursesDaoTest {

	private static CoursesDao coursesDao;

	@BeforeClass
	public static void init() {
		coursesDao = new CoursesDao(true);

//		coursesDao = new CoursesDao();
	}

	@Before
	public void addPlaceholder() {
		Courses newcourse = new Courses("55555", "course1", "course description 1");
		coursesDao.createCourse(newcourse);
	}

	@After
	public void removePlaceholder() {
		coursesDao.deleteCourseById("55555");
	}

	@Test(expected = HibernateException.class)
	public void addDuplicate() {
		Courses newcourse = new Courses("55555", "course1", "course description 1");
		coursesDao.createCourse(newcourse);
	}

	@Test(expected = HibernateException.class)
	public void updateNonExistentCourse() {
		Courses newcourse = new Courses();
		newcourse.setCourseId("000000");
		coursesDao.updateCourse(newcourse);
	}

	@Test(expected = HibernateException.class)
	public void deleteNonExistentCourse() {
		coursesDao.deleteCourseById("12323");
	}

	@Test(expected = IllegalArgumentException.class)
	public void deleteNullArgument() {
		coursesDao.deleteCourseById(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void deleteEmptyArgument() {
		coursesDao.deleteCourseById("");
	}

	@Test
	public void addNullcourseTest() {
		Courses Courses = coursesDao.createCourse(null);
		Assert.assertNull(Courses);
	}

	@Test
	public void addAndUpdateCourseTest() {
		Courses newcourse = new Courses("222", "course1", "course description 1");
		Courses courses = coursesDao.createCourse(newcourse);
		Assert.assertTrue(courses.toString().equals(newcourse.toString()));

		newcourse.setCourseName("course2");
		coursesDao.updateCourse(newcourse);
		Assert.assertTrue(coursesDao.getCourseById("222").getCourseName().equals("course2"));
		coursesDao.deleteCourseById("222");
	}

	@Test
	public void deleteCourseTest() {
		List<Courses> Courses = coursesDao.getAllCourses();
		int oldSize = Courses.size();		
		Courses newcourse = new Courses("111", "course name", "desc");
		coursesDao.createCourse(newcourse);

		Assert.assertTrue(coursesDao.getCourseById("111").getCourseName().equals("course name"));

		coursesDao.deleteCourseById("111");
		int newSize = Courses.size();	
		Assert.assertEquals(oldSize, newSize); 
	}

	@Test
	public void getAllCoursesTest() {
		List<Courses> courses = coursesDao.getAllCourses();

		Courses newcourse = new Courses("111", "course2", "course desc");	
		coursesDao.createCourse(newcourse);
		List<Courses> newCourses = coursesDao.getAllCourses();
		Assert.assertTrue(newCourses.size() == courses.size() + 1);
		coursesDao.deleteCourseById("111");
	}
}