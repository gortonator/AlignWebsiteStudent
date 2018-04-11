package org.mehaexample.asdDemo.dao.alignprivate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.mehaexample.asdDemo.dao.alignpublic.MultipleValueAggregatedDataDao;
import org.mehaexample.asdDemo.enums.Campus;
import org.mehaexample.asdDemo.enums.DegreeCandidacy;
import org.mehaexample.asdDemo.enums.EnrollmentStatus;
import org.mehaexample.asdDemo.enums.Gender;
import org.mehaexample.asdDemo.model.alignprivate.Privacies;
import org.mehaexample.asdDemo.model.alignprivate.Students;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.mehaexample.asdDemo.model.alignpublic.MultipleValueAggregatedData;

import javax.persistence.TypedQuery;

public class StudentsDao {
	private SessionFactory factory;
	private Session session;
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
	public Students addStudent(Students student) {
		Transaction tx = null;

		if (ifNuidExists(student.getNeuId())) {
			throw new HibernateException("student already exists.");
		}

		try {
			session = factory.openSession();
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

	// THIS IS FOR MACHINE LEARNING AND PUBLIC SCRIPTS
	// How many students are at the {Seattle|Boston|Silicon Valley|Charlotte) campus?
	public int getTotalCurrentStudentsInACampus(Campus campus) {
		try {
			session = factory.openSession();
			org.hibernate.query.Query query = session.createQuery(
					"SELECT COUNT(*) FROM Students WHERE campus = :campus AND " +
					"(enrollmentStatus = :enrollmentStatus1 OR enrollmentStatus = :enrollmentStatus2) ");
			query.setParameter("campus", campus);
			query.setParameter("enrollmentStatus1", EnrollmentStatus.FULL_TIME);
			query.setParameter("enrollmentStatus2", EnrollmentStatus.PART_TIME);
			return ((Long) query.list().get(0)).intValue();
		} finally {
			session.close();
		}
	}

	// THIS IS FOR MACHINE LEARNING SCRIPTS
	// How many students are in the ALIGN program?
	public int getTotalCurrentStudents() {
		try {
			session = factory.openSession();
			org.hibernate.query.Query query = session.createQuery(
					"SELECT COUNT(*) FROM Students WHERE " +
					"enrollmentStatus = :enrollmentStatus1 OR enrollmentStatus = :enrollmentStatus2");
			query.setParameter("enrollmentStatus1", EnrollmentStatus.FULL_TIME);
			query.setParameter("enrollmentStatus2", EnrollmentStatus.PART_TIME);
			return ((Long) query.list().get(0)).intValue();
		} finally {
			session.close();
		}
	}

	// THIS IS FOR PUBLIC SCRIPTS
	public int getTotalStudents() {
		try {
			session = factory.openSession();
			org.hibernate.query.Query query = session.createQuery(
					"SELECT COUNT(*) FROM Students");
			return ((Long) query.list().get(0)).intValue();
		} finally {
			session.close();
		}
	}

	// THIS IS FOR MACHINE LEARNING SCRIPTS
	// HOW MANY STUDENTS GRADUATED FROM THE ALIGN PROGRAM?
	public int getTotalGraduatedStudents() {
		try {
			session = factory.openSession();
			org.hibernate.query.Query query = session.createQuery(
					"SELECT COUNT(*) FROM Students WHERE enrollmentStatus = :enrollmentStatus ");
			query.setParameter("enrollmentStatus", EnrollmentStatus.GRADUATED);
			return ((Long) query.list().get(0)).intValue();
		} finally {
			session.close();
		}
	}

	public List<Students> getAdminFilteredStudents(Map<String, List<String>> filters, int begin, int end) {
		StringBuilder hql = new StringBuilder("SELECT Distinct s " +
				"FROM Students s " +
				"LEFT OUTER JOIN WorkExperiences we ON s.neuId = we.neuId " +
				"LEFT OUTER JOIN PriorEducations pe ON s.neuId = pe.neuId ");
		return (List<Students>) populateAdminFilterHql(hql, filters, begin, end);
	}

	public int getAdminFilteredStudentsCount(Map<String, List<String>> filters) {
		StringBuilder hql = new StringBuilder("SELECT Count( Distinct s ) " +
				"FROM Students s " +
				"LEFT OUTER JOIN WorkExperiences we ON s.neuId = we.neuId " +
				"LEFT OUTER JOIN PriorEducations pe ON s.neuId = pe.neuId ");
		List<Long> count = (List<Long>) populateAdminFilterHql(hql, filters, null, null);
		return count.get(0).intValue();
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
		try {
			session = factory.openSession();
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

	// THIS IS FOR MACHINE LEARNING SCRIPT
	// What is the drop out rate for Align?
	public int getTotalDropOutStudents() {
		try {
			session = factory.openSession();
			org.hibernate.query.Query query = session.createQuery(
					"SELECT COUNT(*) FROM Students WHERE enrollmentStatus = :enrollmentStatus");
			query.setParameter("enrollmentStatus", EnrollmentStatus.DROPPED_OUT);
			return ((Long) query.list().get(0)).intValue();
		} finally {
			session.close();
		}
	}

	// THIS IS FOR PUBLIC FACING SCRIPT
	// Total Full Time Students?
	public int getTotalFullTimeStudents() {
		try {
			session = factory.openSession();
			org.hibernate.query.Query query = session.createQuery(
					"SELECT COUNT(*) FROM Students WHERE enrollmentStatus = :enrollmentStatus");
			query.setParameter("enrollmentStatus", EnrollmentStatus.FULL_TIME);
			return ((Long) query.list().get(0)).intValue();
		} finally {
			session.close();
		}
	}

	// THIS IS FOR PUBLIC FACING SCRIPT
	// Total Part Time Students?
	public int getTotalPartTimeStudents() {
		try {
			session = factory.openSession();
			org.hibernate.query.Query query = session.createQuery(
					"SELECT COUNT(*) FROM Students WHERE enrollmentStatus = :enrollmentStatus");
			query.setParameter("enrollmentStatus", EnrollmentStatus.PART_TIME);
			return ((Long) query.list().get(0)).intValue();
		} finally {
			session.close();
		}
	}

	// THIS IS FOR PUBLIC FACING SCRIPT
	// Total Students With Scholarship?
	public int getTotalStudentsWithScholarship() {
		try {
			session = factory.openSession();
			org.hibernate.query.Query query = session.createQuery(
					"SELECT COUNT(*) FROM Students " +
							"WHERE scholarship = true AND " +
					"(enrollmentStatus = 'FULL_TIME' OR enrollmentStatus = 'PART_TIME') ");
			return ((Long) query.list().get(0)).intValue();
		} finally {
			session.close();
		}
	}

//	// THIS IS FOR PUBLIC FACING SCRIPT
//	// Race Breakdown?
//	public List<MultipleValueAggregatedData> getRaceList() {
//		String hql = "SELECT NEW org.mehaexample.asdDemo.model.alignpublic.MultipleValueAggregatedData ( " +
//				"s.race, cast(Count(*) as integer) ) " +
//				"FROM Students s " +
//				"WHERE s.enrollmentStatus = 'FULL_TIME' OR s.enrollmentStatus = 'PART_TIME' " +
//				"GROUP BY s.race " +
//				"ORDER BY Count(*) DESC ";
//		try {
//			session = factory.openSession();
//			TypedQuery<MultipleValueAggregatedData> query = session.createQuery(hql, MultipleValueAggregatedData.class);
//			List<MultipleValueAggregatedData> list = query.getResultList();
//			for (MultipleValueAggregatedData data : list) {
//				data.setAnalyticTerm(MultipleValueAggregatedDataDao.LIST_OF_RACES);
//			}
//			return list;
//		} finally {
//			session.close();
//		}
//	}

	// THIS IS FOR PUBLIC FACING SCRIPT
	// State Breakdown?
	public List<MultipleValueAggregatedData> getStateList() {
		String hql = "SELECT NEW org.mehaexample.asdDemo.model.alignpublic.MultipleValueAggregatedData ( " +
				"s.state, cast(Count(*) as integer) ) " +
				"FROM Students s " +
				"WHERE s.enrollmentStatus = 'FULL_TIME' OR s.enrollmentStatus = 'PART_TIME' " +
				"GROUP BY s.state " +
				"ORDER BY Count(*) DESC ";
		try {
			session = factory.openSession();
			TypedQuery<MultipleValueAggregatedData> query = session.createQuery(hql, MultipleValueAggregatedData.class);
			List<MultipleValueAggregatedData> list = query.getResultList();
			for (MultipleValueAggregatedData data : list) {
				data.setAnalyticTerm(MultipleValueAggregatedDataDao.LIST_OF_STUDENTS_STATES);
			}
			return list;
		} finally {
			session.close();
		}
	}

	/**
	 * Search for students by multiple properties. Each property have one or multiple values.
	 *
	 * @param filters The key of filter map is the property, like firstName.
	 *                The value of map is a list of detail values.
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

	public int getStudentFilteredStudentsCount(Map<String, List<String>> filters) {
		StringBuilder hql = new StringBuilder("SELECT Count( Distinct s ) FROM Students s ");
		List<Long> count = populateStudentFilterHql(hql, filters, null, null);
		return count.get(0).intValue();
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

		try {
			session = factory.openSession();
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
						campuses.add(Campus.valueOf(campus));
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

	public List<Students> getStudentAutoFillSearch(String firstName, String middleName, String lastName) {
		try {
			session = factory.openSession();
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
		try {
			session = factory.openSession();
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
		try {
			session = factory.openSession();
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
	public boolean updateStudentRecord(Students student) {
		Transaction tx = null;
		String neuId = student.getNeuId();

		if (ifNuidExists(neuId)) {
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
	public boolean deleteStudent(String neuId) {
		if (neuId == null || neuId.isEmpty()) {
			throw new IllegalArgumentException("Neu ID argument cannot be null or empty.");
		}

		Students student = getStudentRecord(neuId);
		if (student == null) {
			throw new HibernateException("Student cannot be found.");
		}
		session = factory.openSession();
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
	 * Get a list of students who have the same first name.
	 *
	 * @param firstName Student first name
	 * @return A list of students
	 */
	public List<Students> searchStudentRecord(String firstName) {
		try {
			session = factory.openSession();
			org.hibernate.query.Query query = session.createQuery("FROM Students WHERE firstName = :studentfirstName ");
			query.setParameter("studentfirstName", firstName);
			return (List<Students>) query.list();
		} finally {
			session.close();
		}
	}

	/**
	 * Get all the students records from database.
	 *
	 * @return A list of students
	 */
	public List<Students> getAllStudents() {
		try {
			session = factory.openSession();
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

		try {
			session = factory.openSession();
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

	// THIS IS A SCRIPT FOR MACHINE LEARNING / PUBLIC
	// GET TOTAL CURRENT MALE STUDENTS

	/**
	 * Get the total number of male students in database.
	 *
	 * @return the number of male students.
	 */
	public int countMaleStudents() {
		session = factory.openSession();
		org.hibernate.query.Query query = session.createQuery("FROM Students WHERE gender = 'M' AND " +
				"(enrollmentStatus = 'FULL_TIME' OR enrollmentStatus = 'PART_TIME') ");
		List<Students> list = query.list();
		session.close();
		return list.size();
	}

	// THIS IS A SCRIPT FOR MACHINE LEARNING / PUBLIC
	// GET TOTAL CURRENT FEMALE STUDENTS

	/**
	 * Get the total number of female students in database.
	 *
	 * @return the number of female students.
	 */
	public int countFemaleStudents() {
		session = factory.openSession();
		org.hibernate.query.Query query = session.createQuery("FROM Students WHERE gender = 'F' AND " +
				"(enrollmentStatus = 'FULL_TIME' OR enrollmentStatus = 'PART_TIME') ");
		List<Students> list = query.list();
		session.close();
		return list.size();
	}

	/**
	 * Get a list of similar students.
	 *
	 * @param degree The degree candidacy
	 * @return a list of students with the same degree.
	 */
	public List<Students> searchSimilarStudents(DegreeCandidacy degree) {
		session = factory.openSession();
		org.hibernate.query.Query query = session.createQuery("FROM Students WHERE degree = :degree");
		query.setParameter("degree", degree);
		List<Students> list = query.list();
		session.close();
		return list;
	}

	/**
	 * Get a single student record using emailId.
	 *
	 * @param email
	 * @return a student object
	 */
	public Students getStudentRecordByEmailId(String email) {
		try {
			session = factory.openSession();
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

}
