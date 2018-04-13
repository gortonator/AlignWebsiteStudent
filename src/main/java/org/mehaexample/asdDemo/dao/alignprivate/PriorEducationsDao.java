package org.mehaexample.asdDemo.dao.alignprivate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.mehaexample.asdDemo.dao.alignpublic.MultipleValueAggregatedDataDao;
import org.mehaexample.asdDemo.enums.Campus;
import org.mehaexample.asdDemo.model.alignadmin.StudentBachelorInstitution;
import org.mehaexample.asdDemo.model.alignadmin.TopBachelor;
import org.mehaexample.asdDemo.model.alignprivate.PriorEducations;
import org.mehaexample.asdDemo.model.alignpublic.MultipleValueAggregatedData;

import javax.persistence.TypedQuery;
import java.util.List;

public class PriorEducationsDao {
  private SessionFactory factory;
  private Session session;

  /**
   * Default constructor.
   * it will check the Hibernate.cfg.xml file and load it
   * next it goes to all table files in the hibernate file and loads them.
   */
  public PriorEducationsDao() {
    this.factory = StudentSessionFactory.getFactory();
  }

  public PriorEducationsDao(boolean test) {
    if (test) {
      this.factory = StudentTestSessionFactory.getFactory();
    }
  }

  /**
   * Find a Prior Education by the Work Experience Id.
   * This method searches the work experience from the private database.
   *
   * @param priorEducationId prior education Id in private database.
   * @return Prior Education if found, null if not found.
   */
  public PriorEducations getPriorEducationById(int priorEducationId) {
    try {
      session = factory.openSession();
      org.hibernate.query.Query query = session.createQuery(
              "FROM PriorEducations WHERE priorEducationId = :priorEducationId");
      query.setParameter("priorEducationId", priorEducationId);
      List listOfPriorEducation = query.list();
      if (listOfPriorEducation.isEmpty()) {
        return null;
      }
      return (PriorEducations) listOfPriorEducation.get(0);
    } finally {
      session.close();
    }
  }

  /**
   * Find prior education records of a student in private DB.
   *
   * @param neuId the neu Id of a student; not null.
   * @return List of Prior Educations if neu Id found, null if Neu Id not found.
   */
  public List<PriorEducations> getPriorEducationsByNeuId(String neuId) {
    try {
      session = factory.openSession();
      org.hibernate.query.Query query = session.createQuery(
              "FROM PriorEducations WHERE neuId = :neuId");
      query.setParameter("neuId", neuId);
      return (List<PriorEducations>) query.list();
    } finally {
      session.close();
    }
  }

  /**
   * Create a prior education in the private database.
   * This function requires the Student, institution, major
   * object inside the prior education object to be not null.
   *
   * @param priorEducation the prior education object to be created; not null.
   * @return newly created priorEducation.
   */
  public PriorEducations createPriorEducation(PriorEducations priorEducation) {
    session = factory.openSession();
    Transaction tx = null;
    try {
      tx = session.beginTransaction();
      session.save(priorEducation);
      tx.commit();
    } catch (HibernateException e) {
      if (tx != null) tx.rollback();
      throw new HibernateException(e);
    } finally {
      session.close();
    }

    return priorEducation;
  }

  // THIS IS A SCRIPT FOR MACHINE LEARNING / PUBLIC FACING
  // What bachelors majors do Align students have?
  public List<MultipleValueAggregatedData> getStudentBachelorMajors() {
    String hql = "SELECT NEW org.mehaexample.asdDemo.model.alignpublic.MultipleValueAggregatedData ( " +
            "pe.majorName, cast(Count(*) as integer) ) " +
            "FROM PriorEducations pe LEFT OUTER JOIN Students s ON pe.neuId = s.neuId " +
            "WHERE pe.degreeCandidacy = 'BACHELORS' " +
            "AND (s.enrollmentStatus = 'FULL_TIME' OR s.enrollmentStatus = 'PART_TIME') " +
            "GROUP BY pe.majorName " +
            "ORDER BY Count(*) DESC ";
    try {
      session = factory.openSession();
      TypedQuery<MultipleValueAggregatedData> query = session.createQuery(hql, MultipleValueAggregatedData.class);
      List<MultipleValueAggregatedData> list = query.getResultList();
      for (MultipleValueAggregatedData data : list) {
        data.setAnalyticTerm(MultipleValueAggregatedDataDao.LIST_OF_BACHELOR_DEGREES);
      }
      return list;
    } finally {
      session.close();
    }
  }

  // THIS IS FOR PUBLIC FACING SCRIPT
  // Degree Breakdown?
  public List<MultipleValueAggregatedData> getDegreeList() {
    String hql = "SELECT NEW org.mehaexample.asdDemo.model.alignpublic.MultipleValueAggregatedData ( " +
            "cast(pe.degreeCandidacy as string ), cast(Count(*) as integer) ) " +
            "FROM PriorEducations pe LEFT OUTER JOIN Students s ON pe.neuId = s.neuId " +
            "WHERE s.enrollmentStatus = 'FULL_TIME' OR s.enrollmentStatus = 'PART_TIME'" +
            "GROUP BY pe.degreeCandidacy " +
            "ORDER BY Count(*) DESC ";
    try {
      session = factory.openSession();
      TypedQuery<MultipleValueAggregatedData> query = session.createQuery(hql, MultipleValueAggregatedData.class);
      List<MultipleValueAggregatedData> list = query.getResultList();
      for (MultipleValueAggregatedData data : list) {
        data.setAnalyticTerm(MultipleValueAggregatedDataDao.LIST_OF_DEGREES);
      }
      return list;
    } finally {
      session.close();
    }
  }

  /**
   * Delete a prior education in the private database.
   *
   * @param priorEducationId the prior education Id to be deleted.
   * @return true if prior education is deleted, false otherwise.
   */
  public boolean deletePriorEducationById(int priorEducationId) {
    PriorEducations priorEducation = getPriorEducationById(priorEducationId);
    if (priorEducation == null) {
      throw new HibernateException("Prior Education does not exist.");
    }

    session = factory.openSession();
    Transaction tx = null;
    try {
      tx = session.beginTransaction();
      session.delete(priorEducation);
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
   * Update a prior education in the private DB.
   *
   * @param priorEducation prior education object; not null.
   * @return true if the prior education is updated, false otherwise.
   */
  public boolean updatePriorEducation(PriorEducations priorEducation) {
    if (getPriorEducationById(priorEducation.getPriorEducationId()) == null) {
      throw new HibernateException("Prior Education does not exist.");
    }
    session = factory.openSession();
    Transaction tx = null;
    try {
      tx = session.beginTransaction();
      session.saveOrUpdate(priorEducation);
      tx.commit();
    } catch (HibernateException e) {
      if (tx != null) tx.rollback();
      throw new HibernateException(e);
    } finally {
      session.close();
    }
    return true;
  }

  public List<TopBachelor> getTopTenBachelors(List<Campus> campuses, Integer year) {
    StringBuilder hql = new StringBuilder("SELECT NEW org.mehaexample.asdDemo.model.alignadmin.TopBachelor( " +
            "pe.majorName, Count(*) ) " +
            "FROM Students s INNER JOIN PriorEducations pe " +
            "ON s.neuId = pe.neuId " +
            "WHERE pe.degreeCandidacy = 'BACHELORS' ");
    if (campuses != null) {
      hql.append("AND s.campus IN (:campuses) ");
    }
    if (year != null) {
      hql.append("AND s.expectedLastYear = :year ");
    }
    hql.append("GROUP BY pe.majorName ");
    hql.append("ORDER BY Count(*) DESC ");
    try {
      session = factory.openSession();
      TypedQuery<TopBachelor> query = session.createQuery(hql.toString(), TopBachelor.class);
      query.setMaxResults(10);
      if (campuses != null) {
        query.setParameter("campuses", campuses);
      }
      if (year != null) {
        query.setParameter("year", year);
      }
      return query.getResultList();
    } finally {
      session.close();
    }
  }

  public List<StudentBachelorInstitution> getListOfBachelorInstitutions(Campus campus, Integer year) {
    StringBuilder hql = new StringBuilder("SELECT NEW org.mehaexample.asdDemo.model.alignadmin.StudentBachelorInstitution( " +
            "pe.institutionName, Count(*) ) " +
            "FROM Students s INNER JOIN PriorEducations pe " +
            "ON s.neuId = pe.neuId " +
            "WHERE pe.degreeCandidacy = 'BACHELORS' ");
    if (campus != null) {
      hql.append("AND s.campus = :campus ");
    }
    if (year != null) {
      hql.append("AND s.expectedLastYear = :year ");
    }
    hql.append("GROUP BY pe.institutionName ");
    hql.append("ORDER BY Count(*) DESC ");
    try {
      session = factory.openSession();
      TypedQuery<StudentBachelorInstitution> query = session.createQuery(hql.toString(), StudentBachelorInstitution.class);
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