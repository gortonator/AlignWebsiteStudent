package org.mehaexample.asdDemo.dao.alignadmin;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.mehaexample.asdDemo.model.alignadmin.ElectivesAdmin;
import org.mehaexample.asdDemo.dao.alignprivate.StudentsDao;

public class ElectivesAdminDao {
  private SessionFactory factory;
  private Session session;

  private StudentsDao studentDao;

  /**
   * Default Constructor.
   */
  public ElectivesAdminDao() {
    studentDao = new StudentsDao();
    // it will check the hibernate.cfg.xml file and load it
    // next it goes to all table files in the hibernate file and loads them
    this.factory = AdminSessionFactory.getFactory();
  }

  public ElectivesAdminDao( boolean test) {
    studentDao = new StudentsDao(true);
    if (test) {
      this.factory = AdminTestSessionFactory.getFactory();
    }
  }

  public List<ElectivesAdmin> getElectivesByNeuId(String neuId) {
    try {
      session = factory.openSession();
      org.hibernate.query.Query query = session.createQuery("from ElectivesAdmin where neuId = :neuId");
      query.setParameter("neuId", neuId);
      return (List<ElectivesAdmin>) query.list();
    } finally {
      session.close();
    }
  }

  public ElectivesAdmin getElectiveById(int electiveId) {
    try {
      session = factory.openSession();
      org.hibernate.query.Query query = session.createQuery("from ElectivesAdmin where electiveId = :electiveId");
      query.setParameter("electiveId", electiveId);
      List<ElectivesAdmin> list = query.list();
      if (list.isEmpty()) {
        return null;
      }
      return list.get(0);
    } finally {
      session.close();
    }
  }

  /**
   * This is the function to add an Elective for a given student into database.
   *
   * @param elective elective to be added; not null.
   * @return true if insert successfully. Otherwise throws exception.
   */
  public ElectivesAdmin addElective(ElectivesAdmin elective) {
    if (elective == null) {
      throw new IllegalArgumentException("Elective argument cannot be null.");
    }

    Transaction tx = null;
    session = factory.openSession();

    if (studentDao.ifNuidExists(elective.getNeuId())) {
      try {
        tx = session.beginTransaction();
        session.save(elective);
        tx.commit();
      } catch (HibernateException e) {
        if (tx != null) tx.rollback();
        throw new HibernateException(e);
      } finally {
        session.close();
      }
    } else {
      throw new HibernateException("The student with a given nuid doesn't exists");
    }
    return elective;
  }

  public boolean updateElectives(ElectivesAdmin elective) {
    if (getElectiveById(elective.getElectiveId()) == null) {
      throw new HibernateException("Elective Id cannot be null.");
    }
    Transaction tx = null;
    try {
      session = factory.openSession();
      tx = session.beginTransaction();
      session.saveOrUpdate(elective);
      tx.commit();
      return true;
    } catch (HibernateException e) {
      if (tx != null) tx.rollback();
      throw new HibernateException(e);
    } finally {
      session.close();
    }
  }

  public boolean deleteElectiveRecord(int id) {
    ElectivesAdmin electives = getElectiveById(id);
    if (electives == null) {
      throw new HibernateException("Elective Id cannot be found.");
    }
    Transaction tx = null;
    try {
      session = factory.openSession();
      tx = session.beginTransaction();
      session.delete(electives);
      tx.commit();
      return true;
    } catch (HibernateException e) {
      if (tx != null) tx.rollback();
      throw new HibernateException(e);
    } finally {
      session.close();
    }
  }
}