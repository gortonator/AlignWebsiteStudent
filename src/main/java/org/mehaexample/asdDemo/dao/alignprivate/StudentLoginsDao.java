package org.mehaexample.asdDemo.dao.alignprivate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.mehaexample.asdDemo.model.alignprivate.StudentLogins;

import java.util.List;

public class StudentLoginsDao {
  private SessionFactory factory;
  private Session session;

  /**
   * Default Constructor.
   */
  public StudentLoginsDao() {
    // it will check the hibernate.cfg.xml file and load it
    // next it goes to all table files in the hibernate file and loads them
    this.factory = StudentSessionFactory.getFactory();
  }

  public StudentLoginsDao(boolean test) {
    if (test) {
      this.factory = StudentTestSessionFactory.getFactory();
    }
  }

  public StudentLogins findStudentLoginsByEmail(String email) {
    try {
      session = factory.openSession();
      org.hibernate.query.Query query = session.createQuery("FROM StudentLogins WHERE email = :email ");
      query.setParameter("email", email);
      List list = query.list();
      if (list.isEmpty()) {
        return null;
      }
      return (StudentLogins) list.get(0);
    } finally {
      if (session != null) { session.close(); }
    }
  }

  public StudentLogins createStudentLogin(StudentLogins studentLogin) {
    Transaction tx = null;
    if (findStudentLoginsByEmail(studentLogin.getEmail()) != null) {
      throw new HibernateException("Student Login already exists.");
    }
    try {
      session = factory.openSession();
      tx = session.beginTransaction();
      session.save(studentLogin);
      tx.commit();
    } catch (HibernateException e) {
      if (tx != null) tx.rollback();
      throw new HibernateException(e);
    } finally {
      session.close();
    }

    return studentLogin;
  }

  public boolean updateStudentLogin(StudentLogins studentLogin) {
    Transaction tx = null;
    if (findStudentLoginsByEmail(studentLogin.getEmail()) != null) {
      try {
        session = factory.openSession();
        tx = session.beginTransaction();
        session.saveOrUpdate(studentLogin);
        tx.commit();
      } catch (HibernateException e) {
        if (tx != null) tx.rollback();
        throw new HibernateException(e);
      } finally {
        session.close();
      }
    } else {
      throw new HibernateException("Student Login with email: " + studentLogin.getEmail() +
              " not found.");
    }
    return true;
  }

  public boolean deleteStudentLogin(String email) {
    if (email == null || email.trim().isEmpty()) {
      throw new IllegalArgumentException("Email argument cannot be null or empty.");
    }

    StudentLogins studentLogin = findStudentLoginsByEmail(email);
    if (studentLogin != null) {
      session = factory.openSession();
      Transaction tx = null;
      try {
        tx = session.beginTransaction();
        session.delete(studentLogin);
        tx.commit();
      } catch (HibernateException e) {
        if (tx != null) tx.rollback();
        throw new HibernateException(e);
      } finally {
        session.close();
      }
    } else {
      throw new HibernateException("Student Login with email: " + email +
              " not found.");
    }
    return true;
  }
}