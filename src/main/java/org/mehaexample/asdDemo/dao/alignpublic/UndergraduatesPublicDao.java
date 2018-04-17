package org.mehaexample.asdDemo.dao.alignpublic;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.mehaexample.asdDemo.model.alignpublic.TopUndergradDegrees;
import org.mehaexample.asdDemo.model.alignpublic.TopUndergradSchools;
import org.mehaexample.asdDemo.model.alignpublic.UndergraduatesPublic;

import javax.persistence.TypedQuery;
import java.util.List;

public class UndergraduatesPublicDao {
  private SessionFactory factory;

  public UndergraduatesPublicDao() {
      // it will check the hibernate.cfg.xml file and load it
      // next it goes to all table files in the hibernate file and loads them
    this.factory = PublicSessionFactory.getFactory();
  }

  public UndergraduatesPublicDao(boolean test) {
    if (test) {
      this.factory = PublicTestSessionFactory.getFactory();
    }
  }

  public synchronized UndergraduatesPublic createUndergraduate(UndergraduatesPublic undergraduate) {
    Transaction tx = null;
    Session session = factory.openSession();
    try {
      tx = session.beginTransaction();
      session.save(undergraduate);
      tx.commit();
    } catch (HibernateException e) {
      if (tx != null) tx.rollback();
      throw new HibernateException(e);
    } finally {
      session.close();
    }
    return undergraduate;
  }

  public UndergraduatesPublic findUndergraduateById(int undergraduateId) {
    List<UndergraduatesPublic> list;
    Session session = factory.openSession();
    try {
      org.hibernate.query.Query query = session.createQuery(
              "FROM UndergraduatesPublic WHERE undergraduateId = :undergraduateId ");
      query.setParameter("undergraduateId", undergraduateId);
      list = query.list();
    } finally {
      session.close();
    }
    if (list.isEmpty()) {
      return null;
    }
    return list.get(0);
  }

  public List<TopUndergradSchools> getTopUndergradSchools(int numberOfResultsDesired) {
    String hql = "SELECT NEW org.mehaexample.asdDemo.model.alignpublic.TopUndergradSchools(u.undergradSchool, Count(*)) " +
            "FROM UndergraduatesPublic u " +
            "GROUP BY u.undergradSchool " +
            "ORDER BY Count(*) DESC ";
    List<TopUndergradSchools> listOfTopUndergradSchools;
    Session session = factory.openSession();
    try {
      TypedQuery<TopUndergradSchools> query = session.createQuery(hql, TopUndergradSchools.class);
      query.setMaxResults(numberOfResultsDesired);
      listOfTopUndergradSchools = query.getResultList();
    } finally{
      session.close();
    }
    return listOfTopUndergradSchools;
  }

  public List<TopUndergradDegrees> getTopUndergradDegrees(int numberOfResultsDesired) {
    String hql = "SELECT NEW org.mehaexample.asdDemo.model.alignpublic.TopUndergradDegrees(u.undergradDegree, Count(*)) " +
            "FROM UndergraduatesPublic u " +
            "GROUP BY u.undergradDegree " +
            "ORDER BY Count(*) DESC ";
    List<TopUndergradDegrees> listOfTopUndergradDegrees;
    Session session = factory.openSession();
    try {
      TypedQuery<TopUndergradDegrees> query = session.createQuery(hql, TopUndergradDegrees.class);
      query.setMaxResults(numberOfResultsDesired);
      listOfTopUndergradDegrees = query.getResultList();
    } finally{
      session.close();
    }
    return listOfTopUndergradDegrees;
  }

  public List<String> getListOfAllSchools() {
    String hql = "SELECT u.undergradSchool " +
            "FROM UndergraduatesPublic u " +
            "GROUP BY u.undergradSchool " +
            "ORDER BY Count(*) DESC";
    List<String> listOfAllSchools;
    Session session = factory.openSession();
    try {
      org.hibernate.query.Query query = session.createQuery(hql);
      listOfAllSchools = query.getResultList();
    } finally{
      session.close();
    }
    return listOfAllSchools;
  }

  public List<String> getListOfAllUndergraduateDegrees() {
    String hql = "SELECT u.undergradDegree " +
            "FROM UndergraduatesPublic u " +
            "GROUP BY u.undergradDegree " +
            "ORDER BY Count(*) DESC";
    List<String> listOfAllSchools;
    Session session = factory.openSession();
    try {
      org.hibernate.query.Query query = session.createQuery(hql);
      listOfAllSchools = query.getResultList();
    } finally{
      session.close();
    }
    return listOfAllSchools;
  }

//  public boolean updateUndergraduate(UndergraduatesPublic undergraduate) {
//    if (findUndergraduateById(undergraduate.getUndergraduateId()) == null) {
//      throw new HibernateException("Cannot find Undergraduate with that undergraduate Id");
//    }
//
//    Transaction tx = null;
//
//    try {
//      session = factory.openSession();
//      tx = session.beginTransaction();
//      session.saveOrUpdate(undergraduate);
//      tx.commit();
//    } catch (HibernateException e) {
//      if (tx != null) tx.rollback();
//      throw new HibernateException(e);
//    } finally {
//      session.close();
//    }
//
//    return true;
//  }

  public synchronized boolean deleteUndergraduateById(int publicId) {
    UndergraduatesPublic undergraduate = findUndergraduateById(publicId);
    if (undergraduate != null) {
      Session session = factory.openSession();
      Transaction tx = null;
      try {
        tx = session.beginTransaction();
        session.delete(undergraduate);
        tx.commit();
      } catch (HibernateException e) {
        if (tx != null) tx.rollback();
        throw new HibernateException(e);
      } finally {
        session.close();
      }
    } else {
      throw new HibernateException("Undergraduate Id does not exist.");
    }
    return true;
  }
}
