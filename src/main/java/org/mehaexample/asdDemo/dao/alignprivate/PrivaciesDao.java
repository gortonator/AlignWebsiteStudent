package org.mehaexample.asdDemo.dao.alignprivate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.mehaexample.asdDemo.model.alignprivate.Privacies;

import java.util.List;

public class PrivaciesDao {
  private SessionFactory factory;

  public PrivaciesDao() {
    this.factory = StudentSessionFactory.getFactory();
  }

  public PrivaciesDao(boolean test) {
    if (test) {
      this.factory = StudentTestSessionFactory.getFactory();
    }
  }

  /**
   * Get privacy record by neu id
   *
   * @param neuId Student neu id
   * @return a privacy for a specific student
   */
  public Privacies getPrivacyByNeuId(String neuId) {
    Session session = factory.openSession();
    try {
      org.hibernate.query.Query query = session.createQuery("FROM Privacies WHERE neuId = :neuId ");
      query.setParameter("neuId", neuId);
      List list = query.list();
      if (list.isEmpty()) {
        return null;
      }
      return (Privacies) list.get(0);
    } finally {
      session.close();
    }
  }

  /**
   * Create privacy record for a specific student.
   *
   * @param privacy
   * @return newly created privacy
   */
  public synchronized Privacies createPrivacy(Privacies privacy) {
    Transaction tx = null;

    if (ifNuidExists(privacy.getNeuId())) {
      throw new HibernateException("Privacy already exists.");
    }

    Session session = factory.openSession();
    try {
      tx = session.beginTransaction();
      session.save(privacy);
      tx.commit();
    } catch (HibernateException e) {
      if (tx != null) tx.rollback();
      throw new HibernateException(e);
    } finally {
      session.close();
    }

    return privacy;
  }

  /**
   * Update privacy record for a student.
   *
   * @param privacy
   * @return true if updated successfully.
   */
  public synchronized boolean updatePrivacy(Privacies privacy) {
    Transaction tx = null;

    if (!ifNuidExists(privacy.getNeuId())) {
      throw new HibernateException("Privacy doesn't exist.");
    }

    Session session = factory.openSession();
    try {
      tx = session.beginTransaction();
      session.saveOrUpdate(privacy);
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
   * Delete the privacy for student.
   *
   * @param neuId
   * @return true if deleted successfully.
   */
  public synchronized boolean deletePrivacy(String neuId) {
    Transaction tx = null;

    if (neuId == null || neuId.isEmpty()) {
      throw new IllegalArgumentException("Neu ID argument cannot be null or empty.");
    }

    if (!ifNuidExists(neuId)) {
      throw new HibernateException("Privacy doesn't exist.");
    }

    Privacies privacy = getPrivacyByNeuId(neuId);
    Session session = factory.openSession();
    try {
      tx = session.beginTransaction();
      session.delete(privacy);
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
   * Check if a privacy already exists in database.
   *
   * @param neuId
   * @return true if privacy already exists.
   */
  public boolean ifNuidExists(String neuId) {
    boolean find = false;

    Session session = factory.openSession();
    try {
      org.hibernate.query.Query query = session.createQuery("FROM Privacies WHERE neuId = :neuId");
      query.setParameter("neuId", neuId);
      List list = query.list();
      if (!list.isEmpty()) {
        find = true;
      }
    } finally {
      session.close();
    }

    return find;
  }
}
