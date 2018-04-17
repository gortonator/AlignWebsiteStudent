package org.mehaexample.asdDemo.dao.alignpublic;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.mehaexample.asdDemo.model.alignpublic.SingleValueAggregatedData;

import java.util.List;

public class SingleValueAggregatedDataDao {
  private static final String TOTAL_GRADUATED_STUDENTS = "TotalGraduatedStudents";
  private static final String TOTAL_CURRENT_STUDENTS = "TotalCurrentStudents";
  private static final String TOTAL_STUDENTS = "TotalStudents";
  private static final String TOTAL_STUDENTS_DROPPED_OUT = "TotalStudentsDroppedOut";
  private static final String TOTAL_STUDENTS_GOT_JOB = "TotalStudentsGotJob";
  private static final String TOTAL_STUDENTS_IN_BOSTON = "TotalStudentsInBoston";
  private static final String TOTAL_STUDENTS_IN_SEATTLE = "TotalStudentsInSeattle";
  private static final String TOTAL_STUDENTS_IN_SILICON_VALLEY = "TotalStudentsInSiliconValley";
  private static final String TOTAL_STUDENTS_IN_CHARLOTTE = "TotalStudentsInCharlotte";
  private static final String TOTAL_MALE_STUDENTS = "TotalMaleStudents";
  private static final String TOTAL_FEMALE_STUDENTS = "TotalFemaleStudents";
  private static final String TOTAL_FULL_TIME_STUDENTS = "TotalFullTimeStudents";
  private static final String TOTAL_PART_TIME_STUDENTS = "TotalPartTimeStudents";
  private static final String TOTAL_STUDENTS_WITH_SCHOLARSHIP = "TotalStudentsWithScholarship";

  private SessionFactory factory;

  public SingleValueAggregatedDataDao() {
    this.factory = PublicSessionFactory.getFactory();
  }

  public SingleValueAggregatedDataDao(boolean test) {
    if (test) {
      this.factory = PublicTestSessionFactory.getFactory();
    }
  }

  public int getTotalGraduatedStudents() {
    return findValueByKey(TOTAL_GRADUATED_STUDENTS);
  }

  public int getTotalStudents() {
    return findValueByKey(TOTAL_STUDENTS);
  }

  public int getTotalCurrentStudents() {
    return findValueByKey(TOTAL_CURRENT_STUDENTS);
  }

  public int getTotalStudentsWithScholarship() {
    return findValueByKey(TOTAL_STUDENTS_WITH_SCHOLARSHIP);
  }

  public int getTotalMaleStudents() {
    return findValueByKey(TOTAL_MALE_STUDENTS);
  }

  public int getTotalFemaleStudents() {
    return findValueByKey(TOTAL_FEMALE_STUDENTS);
  }

  public int getTotalFullTimeStudents() {
    return findValueByKey(TOTAL_FULL_TIME_STUDENTS);
  }

  public int getTotalPartTimeStudents() {
    return findValueByKey(TOTAL_PART_TIME_STUDENTS);
  }

  public int getTotalDroppedOutStudents() {
    return findValueByKey(TOTAL_STUDENTS_DROPPED_OUT);
  }

  public int getTotalStudentsGotJob() {
    return findValueByKey(TOTAL_STUDENTS_GOT_JOB);
  }

  public int getTotalStudentsInBoston() {
    return findValueByKey(TOTAL_STUDENTS_IN_BOSTON);
  }

  public int getTotalStudentsInSeattle() {
    return findValueByKey(TOTAL_STUDENTS_IN_SEATTLE);
  }

  public int getTotalStudentsInCharlotte() {
    return findValueByKey(TOTAL_STUDENTS_IN_CHARLOTTE);
  }

  public int getTotalStudentsInSiliconValley() {
    return findValueByKey(TOTAL_STUDENTS_IN_SILICON_VALLEY);
  }

  private int findValueByKey(String analyticKey) {
    Session session = factory.openSession();
    try {
      org.hibernate.query.Query query = session.createQuery(
              "SELECT analyticValue FROM SingleValueAggregatedData WHERE analyticKey = :analyticKey ");
      query.setParameter("analyticKey", analyticKey);
      return (int) query.list().get(0);
    } finally {
      session.close();
    }
  }

  public SingleValueAggregatedData findTotalGraduatedStudentsData() {
    return findDataByKey(TOTAL_GRADUATED_STUDENTS);
  }

  public SingleValueAggregatedData findTotalStudentsData() {
    return findDataByKey(TOTAL_STUDENTS);
  }

  public SingleValueAggregatedData findTotalCurrentStudentsData() {
    return findDataByKey(TOTAL_CURRENT_STUDENTS);
  }

  public SingleValueAggregatedData findTotalStudentsWithScholarshipData() {
    return findDataByKey(TOTAL_STUDENTS_WITH_SCHOLARSHIP);
  }

  public SingleValueAggregatedData findTotalMaleStudentsData() {
    return findDataByKey(TOTAL_MALE_STUDENTS);
  }

  public SingleValueAggregatedData findTotalFemaleStudentsData() {
    return findDataByKey(TOTAL_FEMALE_STUDENTS);
  }

  public SingleValueAggregatedData findTotalFullTimeStudentsData() {
    return findDataByKey(TOTAL_FULL_TIME_STUDENTS);
  }

  public SingleValueAggregatedData findTotalPartTimeStudentsData() {
    return findDataByKey(TOTAL_PART_TIME_STUDENTS);
  }

  public SingleValueAggregatedData findTotalDroppedOutStudentsData() {
    return findDataByKey(TOTAL_STUDENTS_DROPPED_OUT);
  }

  public SingleValueAggregatedData findTotalStudentsGotJobData() {
    return findDataByKey(TOTAL_STUDENTS_GOT_JOB);
  }

  public SingleValueAggregatedData findTotalStudentsInBostonData() {
    return findDataByKey(TOTAL_STUDENTS_IN_BOSTON);
  }

  public SingleValueAggregatedData findTotalStudentsInSeattleData() {
    return findDataByKey(TOTAL_STUDENTS_IN_SEATTLE);
  }

  public SingleValueAggregatedData findTotalStudentsInCharlotteData() {
    return findDataByKey(TOTAL_STUDENTS_IN_CHARLOTTE);
  }

  public SingleValueAggregatedData findTotalStudentsInSiliconValleyData() {
    return findDataByKey(TOTAL_STUDENTS_IN_SILICON_VALLEY);
  }

  private SingleValueAggregatedData findDataByKey(String analyticKey) {
    Session session = factory.openSession();
    try {
      org.hibernate.query.Query query = session.createQuery(
              "FROM SingleValueAggregatedData WHERE analyticKey = :analyticKey ");
      query.setParameter("analyticKey", analyticKey);
      List<SingleValueAggregatedData> list = query.list();
      if (list.isEmpty()) {
        return null;
      }
      return list.get(0);
    } finally {
      session.close();
    }
  }

  public synchronized boolean saveOrUpdateData(SingleValueAggregatedData updatedData) {
    Transaction tx = null;
    SingleValueAggregatedData data = findDataByKey(updatedData.getAnalyticKey());
    if (data == null) {
      throw new HibernateException("Data cannot be found.");
    }
    Session session = factory.openSession();
    try {
      tx = session.beginTransaction();
      session.saveOrUpdate(updatedData);
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
