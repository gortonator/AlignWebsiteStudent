package org.mehaexample.asdDemo.dao.alignprivate;

import java.util.*;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.mehaexample.asdDemo.enums.Campus;
import org.mehaexample.asdDemo.enums.DegreeCandidacy;
import org.mehaexample.asdDemo.enums.Gender;
import org.mehaexample.asdDemo.model.alignprivate.Privacies;
import org.mehaexample.asdDemo.model.alignprivate.Students;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.mehaexample.asdDemo.restModels.StudentCoopInfo;

public class StudentsDao {
	private SessionFactory factory;
	private PrivaciesDao privaciesDao;

	/**
	 * Default Constructor.
	 */
	public StudentsDao() {
		// it will check the hibernate.cfg.xml file and load it
		// next it goes to all table files in the hibernate file and loads them
		this.factory = StudentSessionFactory.getFactory();
		privaciesDao = new PrivaciesDao();
	}

	public StudentsDao(boolean test) {
		if (test) {
			privaciesDao = new PrivaciesDao(true);
			this.factory = StudentTestSessionFactory.getFactory();
		}
	}

	/**
	 * This is the function to add a student into database.
	 *
	 * @param student student to be inserted
	 * @return inserted student if successful. Otherwise null.
	 */
	public synchronized Students addStudent(Students student) {
		Transaction tx = null;

		if (ifNuidExists(student.getNeuId())) {
			throw new HibernateException("student already exists.");
		}

		Session session = factory.openSession();
		try {
			tx = session.beginTransaction();
			session.save(student);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) tx.rollback();
			throw new HibernateException(e);
		} finally {
			session.close();
		}

		return student;
	}

	// THIS IS FOR PUBLIC SCRIPTS
	public int getTotalStudents() {
		Session session = factory.openSession();
		try {
			org.hibernate.query.Query query = session.createQuery(
					"SELECT COUNT(*) FROM Students");
			return ((Long) query.list().get(0)).intValue();
		} finally {
			session.close();
		}
	}

	private List populateAdminFilterHql(StringBuilder hql, Map<String, List<String>> filters, Integer begin, Integer end) {
		Set<String> filterKeys = filters.keySet();
		if (!filters.isEmpty()) {
			hql.append(" WHERE ");
		}
		boolean coop = false;
		boolean firstWhereArgument = true;
		for (String filter : filterKeys) {
			if (!firstWhereArgument) {
				hql.append("AND ");
			}
			hql.append("(");
			boolean first = true;
			List<String> filterElements = filters.get(filter);
			for (int i = 0; i < filterElements.size(); i++) {
				if (!first) {
					hql.append(" OR ");
				}
				if (first) {
					first = false;
				}
				if (filter.equalsIgnoreCase("companyName")) {
					hql.append("we.").append(filter).append(" = :").append(filter).append(i);
					if (!coop) {
						coop = true;
					}
				} else if (filter.equalsIgnoreCase("majorName")
						|| filter.equalsIgnoreCase("institutionName")
						|| filter.equalsIgnoreCase("degreeCandidacy")) {
					hql.append("pe.").append(filter).append(" = :").append(filter).append(i);
				} else {
					hql.append("s.").append(filter).append(" = :").append(filter).append(i);
				}
			}
			hql.append(") ");
			if (firstWhereArgument) {
				firstWhereArgument = false;
			}
		}
		if (coop) {
			hql.append("AND we.coop = true ");
		}
		hql.append(" ORDER BY s.lastName DESC ");
		Session session = factory.openSession();
		try {
			org.hibernate.query.Query query = session.createQuery(hql.toString());
			if (begin != null && end != null) {
				query.setFirstResult(begin - 1);
				query.setMaxResults(end - begin + 1);
			}

			for (String filter : filterKeys) {
				List<String> filterElements = filters.get(filter);
				for (int i = 0; i < filterElements.size(); i++) {
					if (filter.equals("expectedLastYear")) {
						query.setParameter(filter + i, Integer.parseInt(filterElements.get(i)));
					} else {
						if (filter.trim().equalsIgnoreCase("CAMPUS")) {
							query.setParameter(filter + i, Campus.valueOf(filterElements.get(i).trim().toUpperCase()));
						} else if (filter.trim().equalsIgnoreCase("GENDER")) {
							query.setParameter(filter + i, Gender.valueOf(filterElements.get(i).trim().toUpperCase()));
						} else if (filter.trim().equalsIgnoreCase("DegreeCandidacy")) {
							query.setParameter(filter + i, DegreeCandidacy.valueOf(filterElements.get(i).trim().toUpperCase()));
						} else {
							query.setParameter(filter + i, filterElements.get(i));
						}
					}
				}
			}
			return query.list();
		} finally {
			session.close();
		}
	}

	/**
	 * Search for students by multiple properties. Each property have one or multiple values.
	 *
	 * @param filters The key of filter map is the property, like firstName.
	 *                The value of map is a list of detail values.
	 *                The format of filters is
	 *                {
	 *											"companyName"   :["Amazong", "Google", "Facebook"],
	 *											"courseName"    :["Intensive Foundations of CS"],
	 *											"gender"	      :["F","M"],
	 *											"campus"	      :["SEATTLE", "BOSTON"],
	 *											"startTerm"     :["FALL2014", "SPRING2015"],
	 *											"endTerm"       :["SPRING2018", "SUMMER2018"]
	 *								}
	 * @return a list of students filtered by specified map.
	 */
	public List<Students> getStudentFilteredStudents(Map<String, List<String>> filters, int begin, int end) {
		StringBuilder hql = new StringBuilder("SELECT Distinct(s) FROM Students s ");

		List<Students> result = (List<Students>) populateStudentFilterHql(hql, filters, begin, end);

		for (Students student : result) {
			Privacies privacy = privaciesDao.getPrivacyByNeuId(student.getNeuId());
			if (privacy == null) {
			  privacy = new Privacies(student.getNeuId(), student.getPublicId(), true, false, false, false, false, false,
                false, false, false, false, false, false, false, false);
			  privaciesDao.createPrivacy(privacy);
      }
			setPrivacy(student, privacy);
		}
		return result;
	}

	/**
	 * Search for students by multiple properties. Each property have one or multiple values.
	 * @param filters The format of filters is
	 *                 {
	 *                 	"companyName"				: ["Amazon", "Google"],
	 *                 	"campus"						:	["SEATTLE", "BOSTON"],
	 *                 	"entryYear"					:	[2015, 2016],
	 *               	 	"expectedLastYear"	: [2018, 2019],
	 *               	 	"courseId"					: ["CS 5610"]
	 *               	 }
	 * @param begin
	 * @param end
	 * @return a list of students filtered by specified map.
	 */
	public List<StudentCoopInfo> getStudentFilteredStudents2(Map<String, List<String>> filters, int begin, int end) {
		StringBuilder hql =  new StringBuilder();
		if (!filters.containsKey("companyName")) {
			hql = hql.append("SELECT NEW org.mehaexample.asdDemo.restModels.StudentCoopInfo(s.firstName, s.lastName, s.neuId, " +
							"s.campus, s.entryYear, s.expectedLastYear, '') FROM Students s ");
		} else {
			hql = hql.append("SELECT NEW org.mehaexample.asdDemo.restModels.StudentCoopInfo(s.firstName, s.lastName, s.neuId, " +
							"s.campus, s.entryYear, s.expectedLastYear, we.companyName) FROM Students s ");
		}

		List<StudentCoopInfo> result = (List<StudentCoopInfo>) populateStudentFilterHql2(hql, filters, begin, end);

		return result;
	}

	private List populateStudentFilterHql(StringBuilder hql, Map<String, List<String>> filters, Integer begin, Integer end) {
		if (filters.containsKey("companyName")) {
			hql.append("INNER JOIN WorkExperiences we ON s.neuId = we.neuId ");
		}

		if (filters.containsKey("courseName")) {
			hql.append("INNER JOIN Electives el ON s.neuId = el.neuId ")
			.append("INNER JOIN Courses co ON el.courseId = co.courseId ");
		}

		Set<String> filterKeys = filters.keySet();
		if (!filters.isEmpty()) {
			hql.append("WHERE ");
		}

		boolean firstWhereArgument = true;
		for (String filter : filterKeys) {
			if (!firstWhereArgument) {
				hql.append("AND ");
			}

			if (filter.equals("companyName")) {
				hql.append("we.").append(filter).append(" IN ").append("(").append(":")
				.append(filter).append(") ");
			} else if (filter.equals("courseName")) {
				hql.append("co.").append(filter).append(" IN ").append("(").append(":")
				.append(filter).append(") ");
			} else if (filter.equals("startTerm")) {
				String startTerm = "CONCAT(s.entryTerm, s.entryYear) ";
				hql.append(startTerm).append(" IN ").append("(").append(":")
				.append(filter).append(") ");
			} else if (filter.equals("endTerm")) {
				String endTerm = "CONCAT(s.expectedLastTerm, s.expectedLastYear) ";
				hql.append(endTerm).append(" IN ").append("(").append(":")
				.append(filter).append(") ");
			} else {
				hql.append("s.").append(filter).append(" IN ").append("(").append(":")
				.append(filter).append(") ");
			}

			if (firstWhereArgument) {
				firstWhereArgument = false;
			}
		}

		hql.append(" ORDER BY s.expectedLastYear DESC ");

		Session session = factory.openSession();
		try {
			org.hibernate.query.Query query = session.createQuery(hql.toString());
			if (begin != null || end != null) {
				query.setFirstResult(begin - 1);
				query.setMaxResults(end - begin + 1);
			}
			for (String filter : filterKeys) {
				List<String> filterElements = filters.get(filter);
				if (filter.equals("campus")) {
					List<Campus> campuses = new ArrayList<>();
					for (String campus : filterElements) {
						campuses.add(Campus.valueOf(campus.toUpperCase()));
					}
					query.setParameterList(filter, campuses);
				} else if (filter.equals("gender")) {
					List<Gender> genders = new ArrayList<>();
					for (String gender : filterElements) {
						genders.add(Gender.valueOf(gender));
					}
					query.setParameterList(filter, genders);
				} else {
					query.setParameterList(filter, filterElements);
				}
			}

			return query.list();
		} finally {
			session.close();
		}
	}

	private List populateStudentFilterHql2(StringBuilder hql, Map<String, List<String>> filters, Integer begin, Integer end) {
		if (filters.containsKey("companyName")) {
			hql.append("INNER JOIN WorkExperiences we ON s.neuId = we.neuId ");
		}

		if (filters.containsKey("courseId")) {
			hql.append("INNER JOIN Electives el ON s.neuId = el.neuId ");
		}

		Set<String> filterKeys = filters.keySet();
		if (!filters.isEmpty()) {
			hql.append("WHERE ");
		}

		boolean firstWhereArgument = true;
		for (String filter : filterKeys) {
			if (!firstWhereArgument) {
				hql.append("AND ");
			}

			if (filter.equals("companyName")) {
				hql.append("we.").append(filter).append(" IN ").append("(").append(":")
								.append(filter).append(") ");
			} else if (filter.equals("courseId")) {
				hql.append("el.").append(filter).append(" IN ").append("(").append(":")
								.append(filter).append(") ");
			} else {
				hql.append("s.").append(filter).append(" IN ").append("(").append(":")
								.append(filter).append(") ");
			}

			if (firstWhereArgument) {
				firstWhereArgument = false;
			}
		}

		hql.append(" ORDER BY s.expectedLastYear DESC ");

		Session session = factory.openSession();
		try {
			org.hibernate.query.Query query = session.createQuery(hql.toString());
			if (begin != null || end != null) {
				query.setFirstResult(begin - 1);
				query.setMaxResults(end - begin + 1);
			}
			for (String filter : filterKeys) {
				List<String> filterElements = filters.get(filter);
				if (filter.equals("campus")) {
					List<Campus> campuses = new ArrayList<>();
					for (String campus : filterElements) {
						campuses.add(Campus.valueOf(campus.toUpperCase()));
					}
					query.setParameterList(filter, campuses);
				} else if (filter.equals("entryYear") || filter.equals("expectedLastYear")) {
					List<Integer> years = new ArrayList<>();
					for (String year : filterElements) {
						years.add(Integer.parseInt(year));
					}
					query.setParameterList(filter, years);
				}	else {
					query.setParameterList(filter, filterElements);
				}
			}

			return query.list();
		} finally {
			session.close();
		}
	}

	private void setPrivacy(Students student, Privacies privacy) {
		if (!privacy.isAddress()) {
			student.setAddress("");
		}

		if (!privacy.isEmail()) {
			student.setEmail("");
		}

		if (!privacy.isPhone()) {
			student.setPhoneNum("");
		}

		if (!privacy.isPhoto()) {
			student.setPhoto(null);
		}

		if (!privacy.isFacebook()) {
			student.setFacebook("");
		}

		if (!privacy.isGithub()) {
			student.setGithub("");
		}

		if (!privacy.isWebsite()) {
			student.setWebsite("");
		}

		if (!privacy.isSkill()) {
			student.setSkills("");
		}

		if (!privacy.isLinkedin()) {
			student.setLinkedin("");
		}
	}

	/**
	 * Search a list of name alike students.
	 *
	 * @param firstName First name of student
	 * @param middleName Middle name of student
	 * @param lastName Last name of student
	 * @return A list of students with similar name
	 */
	public List<Students> getStudentAutoFillSearch(String firstName, String middleName, String lastName) {
		Session session = factory.openSession();
		try {
			org.hibernate.query.Query query;
			if (firstName.equalsIgnoreCase(lastName)) {
				query = session.createQuery(
								"SELECT s FROM Students s " +
												"WHERE s.firstName like :firstName " +
												"OR s.middleName like :middleName " +
												"OR s.lastName like :lastName ");
			} else {
				query = session.createQuery(
								"SELECT s FROM Students s " +
												"WHERE ( s.firstName like :firstName " +
												"AND s.lastName like :lastName ) " +
												"OR s.middleName like :middleName ");
			}
			query.setParameter("firstName", "%" + firstName + "%");
			query.setParameter("middleName", "%" + middleName + "%");
			query.setParameter("lastName", "%" + lastName + "%");
			return (List<Students>) query.list();
		} finally {
			session.close();
		}
	}

	/**
	 * Search a single student record using neu id.
	 *
	 * @param neuId Student Neu Id
	 * @return a student object
	 */
	public Students getStudentRecord(String neuId) {
		Session session = factory.openSession();
		try {
			org.hibernate.query.Query query = session.createQuery("FROM Students WHERE neuId = :studentNuid ");
			query.setParameter("studentNuid", neuId);
			List list = query.list();
			if (list.isEmpty()) {
				return null;
			}
			return (Students) list.get(0);
		} finally {
			session.close();
		}
	}

	/**
	 * Search a single student record using neu id and hide some private information.
	 *
	 * @param neuId
	 * @return a student object with related information hidden.
	 */
	public Students getStudentRecordWithPrivacy(String neuId) {
		Session session = factory.openSession();
		try {
			org.hibernate.query.Query query = session.createQuery("FROM Students WHERE neuId = :studentNuid ");
			query.setParameter("studentNuid", neuId);
			List list = query.list();
			if (list.isEmpty()) {
				return null;
			}
			Privacies privacy = privaciesDao.getPrivacyByNeuId(neuId);
			Students student = (Students) list.get(0);
			setPrivacy(student, privacy);
			return student;
		} finally {
			session.close();
		}
	}

	/**
	 * Update a student record with most recent details.
	 *
	 * @param student which contains the new student details.
	 * @return true if successful. Otherwise, false.
	 */
	public synchronized boolean updateStudentRecord(Students student) {
		Transaction tx = null;
		String neuId = student.getNeuId();

		if (ifNuidExists(neuId)) {
			Session session = factory.openSession();
			try {
				session = factory.openSession();
				tx = session.beginTransaction();
				session.saveOrUpdate(student);
				tx.commit();
			} catch (HibernateException e) {
				if (tx != null) tx.rollback();
				throw new HibernateException(e);
			} finally {
				session.close();
			}
		} else {
			throw new HibernateException("Student cannot be found.");
		}

		return true;
	}


	/**
	 * Delete a student record from database.
	 *
	 * @param neuId Student Neu Id
	 * @return true if delete succesfully. Otherwise, false.
	 */
	public synchronized boolean deleteStudent(String neuId) {
		if (neuId == null || neuId.isEmpty()) {
			throw new IllegalArgumentException("Neu ID argument cannot be null or empty.");
		}

		Students student = getStudentRecord(neuId);
		if (student == null) {
			throw new HibernateException("Student cannot be found.");
		}
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.delete(student);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) tx.rollback();
			throw new HibernateException(e);
		} finally {
			session.close();
		}

		return true;
	}

	/**
	 * Get all the students records from database.
	 *
	 * @return A list of students
	 */
	public List<Students> getAllStudents() {
		Session session = factory.openSession();
		try {
			org.hibernate.query.Query query = session.createQuery("FROM Students");
			return (List<Students>) query.list();
		} finally {
			session.close();
		}
	}

	/**
	 * Check if a specific student existed in database based on neu id.
	 *
	 * @param neuId Student Neu Id
	 * @return true if existed, false if not.
	 */
	public boolean ifNuidExists(String neuId) {
		boolean find = false;

		Session session = factory.openSession();
		try {
			org.hibernate.query.Query query = session.createQuery("FROM Students WHERE neuId = :studentNeuId");
			query.setParameter("studentNeuId", neuId);
			List list = query.list();
			if (!list.isEmpty()) {
				find = true;
			}
		} finally {
			session.close();
		}

		return find;
	}

	/**
	 * Get a single student record using emailId.
	 *
	 * @param email
	 * @return a student object
	 */
	public Students getStudentRecordByEmailId(String email) {
		Session session = factory.openSession();
		try {
			org.hibernate.query.Query query = session.createQuery("FROM Students WHERE email = :email ");
			query.setParameter("email", email);
			List list = query.list();
			if (list.isEmpty()) {
				return null;
			}
			return (Students) list.get(0);
		} finally {
			session.close();
		}
	}

	/**
	 *
	 * @return list of all campuses in database
	 */
	public List<String> getAllCampuses() {
		Session session = factory.openSession();
		try {
			org.hibernate.query.Query query = session.createQuery("FROM Students");

			Set<String> campusSet = new HashSet<>();
			for (Object o : query.list()) {
				campusSet.add(((Students) o).getCampus().name());
			}

			return new ArrayList<>(campusSet);
		} finally {
			session.close();
		}
	}

	/**
	 *
	 * @return List of all Enrollment years in database
	 */
	public List<Integer> getAllEntryYears() {
		Session session = factory.openSession();
		try {
			org.hibernate.query.Query query = session.createQuery("FROM Students");

			Set<Integer> entrySet = new HashSet<>();
			for (Object o : query.list()) {
				entrySet.add(((Students) o).getEntryYear());
			}

			return new ArrayList<>(entrySet);
		} finally {
			session.close();
		}
	}
}
