package org.mehaexample.asdDemo.dao.alignprivate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.mehaexample.asdDemo.model.alignprivate.Privacies;
import org.mehaexample.asdDemo.model.alignprivate.Projects;

import java.util.ArrayList;
import java.util.List;

public class ProjectsDao {
  private SessionFactory factory;
  private PrivaciesDao privaciesDao;

  /**
   * Default constructor.
   * it will check the Hibernate.cfg.xml file and load it
   * next it goes to all table files in the hibernate file and loads them.
   */
  public ProjectsDao() {
    privaciesDao = new PrivaciesDao();
    this.factory = StudentSessionFactory.getFactory();
  }

  public ProjectsDao(boolean test) {
    if (test) {
      privaciesDao = new PrivaciesDao(true);
      this.factory = StudentTestSessionFactory.getFactory();
    }
  }

  /**
   * Find a Project by the Project Id.
   * This method searches the project from the private database.
   *
   * @param projectId project Id in private database.
   * @return Project if found.
   */
  public Projects getProjectById(int projectId) {
    Session session = factory.openSession();
    try {
      org.hibernate.query.Query query = session.createQuery(
              "FROM Projects WHERE projectId = :projectId");
      query.setParameter("projectId", projectId);
      List<Projects> listOfProjects = query.list();
      if (listOfProjects.isEmpty())
        return null;
      return listOfProjects.get(0);
    } finally {
      session.close();
    }
  }

  /**
   * Find project records of a student in private DB.
   *
   * @param neuId the neu Id of a student; not null.
   * @return List of Projects.
   */
  public List<Projects> getProjectsByNeuId(String neuId) {
    Session session = factory.openSession();
    try {
      org.hibernate.query.Query query = session.createQuery(
              "FROM Projects WHERE neuId = :neuId");
      query.setParameter("neuId", neuId);
      return (List<Projects>) query.list();
    } finally {
      session.close();
    }
  }

  public List<Projects> getProjectsWithPrivacy(String neuId) {
    Privacies privacy = privaciesDao.getPrivacyByNeuId(neuId);
    if (!privacy.isProject()) {
      return new ArrayList<>();
    } else {
      return getProjectsByNeuId(neuId);
    }
  }

  /**
   * Create a project in the private database.
   *
   * @param project the project object to be created; not null.
   * @return newly created project if success. Otherwise, return null;
   */
  public synchronized Projects createProject(Projects project) {
    Session session = factory.openSession();
    Transaction tx = null;
    try {
      tx = session.beginTransaction();
      session.save(project);
      tx.commit();
    } catch (HibernateException e) {
      if (tx != null) tx.rollback();
      throw new HibernateException(e);
    } finally {
      session.close();
    }

    return project;
  }

  /**
   * Delete a project in the private database.
   *
   * @param projectId the project Id to be deleted.
   * @return true if project is deleted, false otherwise.
   */
  public synchronized boolean deleteProjectById(int projectId) {
    Projects project = getProjectById(projectId);
    if (project == null) {
      throw new HibernateException("Project Id cannot be found.");
    }
    Session session = factory.openSession();
    Transaction tx = null;
    try {
      tx = session.beginTransaction();
      session.delete(project);
      tx.commit();
    } catch (HibernateException e) {
      if (tx != null) tx.rollback();
      throw new HibernateException(e);
    } finally {
      session.close();
    }

    return true;
  }

  public synchronized boolean deleteProjectsByNeuId(String neuId) {
    Transaction tx = null;

    Session session = factory.openSession();
    try {
      tx = session.beginTransaction();
      org.hibernate.query.Query query = session.createQuery("DELETE FROM Projects " +
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
   * Update a project in the private DB.
   *
   * @param project project object; not null.
   * @return true if the project is updated, false otherwise.
   */
  public synchronized boolean updateProject(Projects project) {

    if (getProjectById(project.getProjectId()) == null) {
      throw new HibernateException("Project does not exist.");
    }
    Session session = factory.openSession();
    Transaction tx = null;
    try {
      tx = session.beginTransaction();
      session.saveOrUpdate(project);
      tx.commit();
    } catch (HibernateException e) {
      if (tx != null) tx.rollback();
      throw new HibernateException(e);
    } finally {
      session.close();
    }

    return true;
  }
}
