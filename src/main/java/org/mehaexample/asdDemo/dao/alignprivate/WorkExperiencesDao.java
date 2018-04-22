package org.mehaexample.asdDemo.dao.alignprivate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.mehaexample.asdDemo.model.alignprivate.Privacies;
import org.mehaexample.asdDemo.model.alignprivate.WorkExperiences;

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

  /**
   * Delete work experiences of a student
   * @param neuId Student neu id
   * @return true if delete succss
   */
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
}