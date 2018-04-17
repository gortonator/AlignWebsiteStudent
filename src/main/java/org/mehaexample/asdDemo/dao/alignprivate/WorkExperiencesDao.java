package org.mehaexample.asdDemo.dao.alignprivate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.mehaexample.asdDemo.dao.alignpublic.MultipleValueAggregatedDataDao;
import org.mehaexample.asdDemo.enums.Campus;
import org.mehaexample.asdDemo.model.alignadmin.TopBachelor;
import org.mehaexample.asdDemo.model.alignadmin.TopEmployer;
import org.mehaexample.asdDemo.model.alignprivate.Privacies;
import org.mehaexample.asdDemo.model.alignprivate.StudentBasicInfo;
import org.mehaexample.asdDemo.model.alignprivate.StudentCoopList;
import org.mehaexample.asdDemo.model.alignprivate.WorkExperiences;
import org.mehaexample.asdDemo.model.alignpublic.MultipleValueAggregatedData;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class WorkExperiencesDao {
  private SessionFactory factory;
  private PrivaciesDao privaciesDao;

  /**
   * Default constructor.
   * it will check the Hibernate.cfg.xml file and load it
   * next it goes to all table files in the hibernate file and loads them.
   */
  public WorkExperiencesDao() {
    privaciesDao = new PrivaciesDao();
    this.factory = StudentSessionFactory.getFactory();
  }

  public WorkExperiencesDao(boolean test) {
    if (test) {
      privaciesDao = new PrivaciesDao(true);
      this.factory = StudentTestSessionFactory.getFactory();
    }
  }

  /**
   * Find a Work Experience by the Work Experience Id.
   * This method searches the work experience from the private database.
   *
   * @param workExperienceId work experience Id in private database.
   * @return Work Experience if found.
   */
  public WorkExperiences getWorkExperienceById(int workExperienceId) {
    Session session = factory.openSession();
    try {
      org.hibernate.query.Query query = session.createQuery(
              "FROM WorkExperiences WHERE workExperienceId = :workExperienceId");
      query.setParameter("workExperienceId", workExperienceId);
      List<WorkExperiences> listOfWorkExperience = query.list();
      if (listOfWorkExperience.isEmpty())
        return null;
      return listOfWorkExperience.get(0);
    } finally {
      session.close();
    }
  }

  /**
   * Find work experience records of a student in private DB.
   *
   * @param neuId the neu Id of a student; not null.
   * @return List of Work Experiences.
   */
  public List<WorkExperiences> getWorkExperiencesByNeuId(String neuId) {
    Session session = factory.openSession();
    try {
      org.hibernate.query.Query query = session.createQuery(
              "FROM WorkExperiences WHERE neuId = :neuId");
      query.setParameter("neuId", neuId);
      return (List<WorkExperiences>) query.list();
    } finally {
      session.close();
    }
  }

  public List<WorkExperiences> getWorkExperiencesWithPrivacy(String neuId) {
    Privacies privacy = privaciesDao.getPrivacyByNeuId(neuId);
    if (!privacy.isCoop()) {
      return new ArrayList<>();
    } else {
      return getWorkExperiencesByNeuId(neuId);
    }
  }

  /**
   * Create a work experience in the private database.
   * This function requires the StudentsPublic object and the Companies
   * object inside the work experience object to be not null.
   *
   * @param workExperience the work experience object to be created; not null.
   * @return newly created WorkExperience if success. Otherwise, return null;
   */
  public synchronized WorkExperiences createWorkExperience(WorkExperiences workExperience) {
    Transaction tx = null;
    Session session = factory.openSession();
    try {
      tx = session.beginTransaction();
      session.save(workExperience);
      tx.commit();
    } catch (HibernateException e) {
      if (tx != null) tx.rollback();
      throw new HibernateException(e);
    } finally {
      session.close();
    }

    return workExperience;
  }

  // THIS IS THE SCRIPT FOR MACHINE LEARNING
  // How many Align students get jobs?
  public int getTotalStudentsGotJob() {
    Session session = factory.openSession();
    try {
      org.hibernate.query.Query query = session.createQuery(
              "SELECT COUNT(DISTINCT we.neuId) FROM WorkExperiences we WHERE we.coop = false ");
      return ((Long) query.list().get(0)).intValue();
    } finally {
      session.close();
    }
  }

  /**
   * Delete a work experience in the private database.
   *
   * @param workExperienceId the work experience Id to be deleted.
   * @return true if work experience is deleted, false otherwise.
   */
  public synchronized boolean deleteWorkExperienceById(int workExperienceId) {
    WorkExperiences workExperiences = getWorkExperienceById(workExperienceId);
    if (workExperiences != null) {
      Session session = factory.openSession();
      Transaction tx = null;
      try {
        tx = session.beginTransaction();
        session.delete(workExperiences);
        tx.commit();
      } catch (HibernateException e) {
        if (tx != null) tx.rollback();
        throw new HibernateException(e);
      } finally {
        session.close();
      }
    } else {
      throw new HibernateException("work experience id does not exist");
    }

    return true;
  }

  public synchronized boolean deleteWorkExperienceByNeuId(String neuId) {
    Transaction tx = null;

    Session session = factory.openSession();
    try {
      tx = session.beginTransaction();
      org.hibernate.query.Query query = session.createQuery("DELETE FROM WorkExperiences " +
              "WHERE neuId = :neuId ");
      query.setParameter("neuId", neuId);
      query.executeUpdate();
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
   * Update a work experience in the private DB.
   *
   * @param workExperience work experience object; not null.
   * @return true if the work experience is updated, false otherwise.
   */
  public synchronized boolean updateWorkExperience(WorkExperiences workExperience) {
    if (getWorkExperienceById(workExperience.getWorkExperienceId()) != null) {
      Session session = factory.openSession();
      Transaction tx = null;
      try {
        tx = session.beginTransaction();
        session.saveOrUpdate(workExperience);
        tx.commit();
      } catch (HibernateException e) {
        if (tx != null) tx.rollback();
        throw new HibernateException(e);
      } finally {
        session.close();
      }
    } else {
      throw new HibernateException("Work Experience ID does not exist");
    }
    return true;
  }

  public List<TopEmployer> getTopTenEmployers(Campus campus, Integer year) {
    StringBuilder hql = new StringBuilder("SELECT NEW org.mehaexample.asdDemo.model.alignadmin.TopEmployer( " +
            "we.companyName, Count(*) ) " +
            "FROM Students s INNER JOIN WorkExperiences we " +
            "ON s.neuId = we.neuId " +
            "WHERE we.coop = false ");
    if (campus != null) {
      hql.append("AND s.campus = :campus ");
    }
    if (year != null) {
      hql.append("AND s.expectedLastYear = :year ");
    }
    hql.append("GROUP BY s.neuId ");
    hql.append("ORDER BY Count(*) DESC ");
    Session session = factory.openSession();
    try {
      TypedQuery<TopEmployer> query = session.createQuery(hql.toString(), TopEmployer.class);
      query.setMaxResults(10);
      if (campus != null) {
        query.setParameter("campus", campus);
      }
      if (year != null) {
        query.setParameter("year", year);
      }
      return query.getResultList();
    } finally {
      session.close();
    }
  }

  // THIS IS A SCRIPT FOR MACHINE LEARNING / PUBLIC FACING
  // Who are the largest Align student employers?
  public List<MultipleValueAggregatedData> getStudentEmployers() {
    String hql = "SELECT NEW org.mehaexample.asdDemo.model.alignpublic.MultipleValueAggregatedData ( " +
            "we.companyName, cast(Count(*) as integer) ) " +
            "FROM WorkExperiences we " +
            "WHERE we.coop = false " +
            "GROUP BY we.companyName " +
            "ORDER BY Count(*) DESC ";
    Session session = factory.openSession();
    try {
      TypedQuery<MultipleValueAggregatedData> query = session.createQuery(hql, MultipleValueAggregatedData.class);
      List<MultipleValueAggregatedData> list = query.getResultList();
      for (MultipleValueAggregatedData data : list) {
        data.setAnalyticTerm(MultipleValueAggregatedDataDao.LIST_OF_EMPLOYERS);
      }
      return list;
    } finally {
      session.close();
    }
  }

  public List<StudentCoopList> getStudentCoopCompanies(Campus campus, Integer year) {
    StringBuilder hql = new StringBuilder("SELECT DISTINCT NEW org.mehaexample.asdDemo.model.alignprivate.StudentCoopList( " +
            "s.neuId, s.firstName, s.lastName ) " +
            "FROM Students s INNER JOIN WorkExperiences we " +
            "ON s.neuId = we.neuId " +
            "WHERE we.coop = true ");
    if (campus != null) {
      hql.append("AND s.campus = :campus ");
    }
    if (year != null) {
      hql.append("AND s.expectedLastYear = :year ");
    }
    Session session = factory.openSession();
    try {
      TypedQuery<StudentCoopList> query = session.createQuery(hql.toString(), StudentCoopList.class);
      if (campus != null) {
        query.setParameter("campus", campus);
      }
      if (year != null) {
        query.setParameter("year", year);
      }
      List<StudentCoopList> studentCoopLists = query.getResultList();

      for (StudentCoopList student : studentCoopLists) {
        student.setCompanies(getCompaniesByNeuId(student.getNeuId()));
      }
      return studentCoopLists;
    } finally {
      session.close();
    }
  }

  private List<String> getCompaniesByNeuId(String neuId) {
    Session session = factory.openSession();
    try {
      org.hibernate.query.Query query = session.createQuery(
              "SELECT we.companyName FROM WorkExperiences we WHERE we.neuId = :neuId");
      query.setParameter("neuId", neuId);
      return (List<String>) query.list();
    } finally {
      session.close();
    }
  }

  public List<StudentBasicInfo> getStudentsWorkingInACompany(Campus campus, Integer year, String companyName) {
    StringBuilder hql = new StringBuilder("SELECT DISTINCT NEW org.mehaexample.asdDemo.model.alignprivate.StudentBasicInfo( " +
            "s.firstName, s.lastName, s.neuId ) " +
            "FROM Students s INNER JOIN WorkExperiences we " +
            "ON s.neuId = we.neuId " +
            "WHERE we.companyName = :companyName " +
            "AND we.coop = false ");
    if (campus != null) {
      hql.append("AND s.campus = :campus ");
    }
    if (year != null) {
      hql.append("AND s.expectedLastYear = :year ");
    }
    Session session = factory.openSession();
    try {
      TypedQuery<StudentBasicInfo> query = session.createQuery(hql.toString(), StudentBasicInfo.class);
      query.setParameter("companyName", companyName);
      if (campus != null) {
        query.setParameter("campus", campus);
      }
      if (year != null) {
        query.setParameter("year", year);
      }
      return query.getResultList();
    } finally {
      session.close();
    }
  }

  public List<StudentCoopList> getStudentCurrentCompanies(Campus campus, Integer year) {
    StringBuilder hql = new StringBuilder("SELECT DISTINCT NEW org.mehaexample.asdDemo.model.alignprivate.StudentCoopList( " +
            "s.neuId, s.firstName, s.lastName ) " +
            "FROM Students s INNER JOIN WorkExperiences we " +
            "ON s.neuId = we.neuId ");
    hql.append("WHERE we.currentJob = true AND we.coop = false ");
    if (campus != null) {
      hql.append("AND s.campus = :campus ");
    }
    if (year != null) {
      hql.append("AND ");
      hql.append("s.expectedLastYear = :year ");
    }
    Session session = factory.openSession();
    try {
      TypedQuery<StudentCoopList> query = session.createQuery(hql.toString(), StudentCoopList.class);
      if (campus != null) {
        query.setParameter("campus", campus);
      }
      if (year != null) {
        query.setParameter("year", year);
      }
      List<StudentCoopList> studentCoopLists = query.getResultList();

      for (StudentCoopList student : studentCoopLists) {
        student.setCompanies(getCurrentCompaniesByNeuId(student.getNeuId()));
      }
      return studentCoopLists;
    } finally {
      session.close();
    }
  }

  private List<String> getCurrentCompaniesByNeuId(String neuId) {
    Session session = factory.openSession();
    try {
      org.hibernate.query.Query query = session.createQuery(
              "SELECT we.companyName FROM WorkExperiences we WHERE we.currentJob = true AND we.neuId = :neuId");
      query.setParameter("neuId", neuId);
      return (List<String>) query.list();
    } finally {
      session.close();
    }
  }
}