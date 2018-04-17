package org.mehaexample.asdDemo.dao.alignprivate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.mehaexample.asdDemo.enums.Campus;
import org.mehaexample.asdDemo.model.alignadmin.TopElective;
import org.mehaexample.asdDemo.model.alignadmin.TopEmployer;
import org.mehaexample.asdDemo.model.alignprivate.Electives;
import org.mehaexample.asdDemo.model.alignprivate.Privacies;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class ElectivesDao {
  private SessionFactory factory;

  private StudentsDao studentDao;
  private PrivaciesDao privaciesDao;

  /**
   * Default Constructor.
   */
  public ElectivesDao() {
    studentDao = new StudentsDao();
    privaciesDao = new PrivaciesDao();
    // it will check the hibernate.cfg.xml file and load it
    // next it goes to all table files in the hibernate file and loads them
    this.factory = StudentSessionFactory.getFactory();
  }

  public ElectivesDao(boolean test) {
    if (test) {
      studentDao = new StudentsDao(true);
      privaciesDao = new PrivaciesDao(true);
      this.factory = StudentTestSessionFactory.getFactory();
    }
  }

  public List<Electives> getElectivesByNeuId(String neuId) {
    Session session = factory.openSession();
    try {
      org.hibernate.query.Query query = session.createQuery("from Electives where neuId = :neuId");
      query.setParameter("neuId", neuId);
      return (List<Electives>) query.list();
    } finally {
      session.close();
    }
  }

  public List<Electives> getElectivesWithPrivacy(String neuId) {
    Privacies privacy = privaciesDao.getPrivacyByNeuId(neuId);
    if (!privacy.isCourse()) {
      return new ArrayList<>();
    } else {
      return getElectivesByNeuId(neuId);
    }
  }

  public Electives getElectiveById(int electiveId) {
    Session session = factory.openSession();
    try {
      org.hibernate.query.Query query = session.createQuery("from Electives where electiveId = :electiveId");
      query.setParameter("electiveId", electiveId);
      List<Electives> list = query.list();
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
  public synchronized Electives addElective(Electives elective) {
    if (elective == null) {
      return null;
    }

    Transaction tx = null;
    Session session = factory.openSession();

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

  public synchronized boolean updateElectives(Electives elective) {
    if (getElectiveById(elective.getElectiveId()) == null) {
      throw new HibernateException("Elective does not exist.");
    }

    Session session = factory.openSession();
    Transaction tx = null;
    try {
      tx = session.beginTransaction();
      session.saveOrUpdate(elective);
      tx.commit();
    } catch (HibernateException e) {
      if (tx != null) tx.rollback();
      throw new HibernateException(e);
    } finally {
      session.close();
    }
    return true;
  }

  public synchronized boolean deleteElectiveRecord(int id) {
    if (getElectiveById(id) == null) {
      throw new HibernateException("Elective does not exist.");
    }

    Transaction tx = null;

    Session session = factory.openSession();
    try {
      tx = session.beginTransaction();
      Electives electives = session.get(Electives.class, id);
      session.delete(electives);
      tx.commit();
    } catch (HibernateException e) {
      if (tx != null) tx.rollback();
      throw new HibernateException(e);
    } finally {
      session.close();
    }

    return true;
  }

  public synchronized boolean deleteElectivesByNeuId(String neuId) {
    Transaction tx = null;

    Session session = factory.openSession();
    try {
      tx = session.beginTransaction();
      org.hibernate.query.Query query = session.createQuery("DELETE FROM Electives " +
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

  public List<TopElective> getTopTenElectives(Campus campus, Integer year) {
    StringBuilder hql = new StringBuilder("SELECT NEW org.mehaexample.asdDemo.model.alignadmin.TopElective( " +
            "e.courseName, Count(*) ) " +
            "FROM Students s INNER JOIN Electives e " +
            "ON s.neuId = e.neuId ");
    boolean first = true;
    if (campus != null) {
      hql.append("WHERE s.campus = :campus ");
      first = false;
    }
    if (year != null) {
      if (first) {
        hql.append("WHERE ");
      } else {
        hql.append("AND ");
      }
      hql.append("s.expectedLastYear = :year ");
    }
    hql.append("GROUP BY e.courseName ");
    hql.append("ORDER BY Count(*) DESC ");
    Session session = factory.openSession();
    try {
      TypedQuery<TopElective> query = session.createQuery(hql.toString(), TopElective.class);
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
}
