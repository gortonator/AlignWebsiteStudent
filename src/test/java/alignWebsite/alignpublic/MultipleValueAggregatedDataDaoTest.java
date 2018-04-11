package alignWebsite.alignpublic;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mehaexample.asdDemo.dao.alignpublic.MultipleValueAggregatedDataDao;
import org.mehaexample.asdDemo.model.alignpublic.DataCount;
import org.mehaexample.asdDemo.model.alignpublic.MultipleValueAggregatedData;

import java.util.ArrayList;
import java.util.List;

public class MultipleValueAggregatedDataDaoTest {
  private static MultipleValueAggregatedDataDao dataDao;

  @BeforeClass
  public static void init() {
    dataDao = new MultipleValueAggregatedDataDao(true);

//    dataDao = new MultipleValueAggregatedDataDao();
  }

  @Test
  public void updateGetDeleteEmployersTest() {
    // add data
    MultipleValueAggregatedData google = new MultipleValueAggregatedData();
    google.setAnalyticTerm(MultipleValueAggregatedDataDao.LIST_OF_EMPLOYERS);
    google.setAnalyticKey("Google");
    google.setAnalyticValue(20);

    MultipleValueAggregatedData microsoft = new MultipleValueAggregatedData();
    microsoft.setAnalyticTerm(MultipleValueAggregatedDataDao.LIST_OF_EMPLOYERS);
    microsoft.setAnalyticKey("Microsoft");
    microsoft.setAnalyticValue(21);

    // update in database
    List<MultipleValueAggregatedData> listOfEmployers = new ArrayList<>();
    listOfEmployers.add(google);
    listOfEmployers.add(microsoft);
    dataDao.saveOrUpdateList(listOfEmployers);

    // query the database
    List<String> result = dataDao.getTopFiveListOfEmployers();
    Assert.assertTrue(result.size() == 2);
    Assert.assertTrue(result.get(0).equals("Microsoft"));
    Assert.assertTrue(result.get(1).equals("Google"));

    // clear the database
    dataDao.deleteListOfEmployers();
    Assert.assertTrue(dataDao.getTopFiveListOfEmployers().isEmpty());
  }

  @Test
  public void updateGetDeleteBachelorDegreesTest() {
    // add data
    MultipleValueAggregatedData accounting = new MultipleValueAggregatedData();
    accounting.setAnalyticTerm(MultipleValueAggregatedDataDao.LIST_OF_BACHELOR_DEGREES);
    accounting.setAnalyticKey("Accounting");
    accounting.setAnalyticValue(20);

    MultipleValueAggregatedData english = new MultipleValueAggregatedData();
    english.setAnalyticTerm(MultipleValueAggregatedDataDao.LIST_OF_BACHELOR_DEGREES);
    english.setAnalyticKey("English");
    english.setAnalyticValue(21);

    // update in database
    List<MultipleValueAggregatedData> listOfBachelorDegrees = new ArrayList<>();
    listOfBachelorDegrees.add(accounting);
    listOfBachelorDegrees.add(english);
    dataDao.saveOrUpdateList(listOfBachelorDegrees);

    // query the database
    List<String> result = dataDao.getTopFiveListOfBachelorDegrees();
    Assert.assertTrue(result.size() == 2);
    Assert.assertTrue(result.get(0).equals("English"));
    Assert.assertTrue(result.get(1).equals("Accounting"));

    // clear the database
    dataDao.deleteListOfBachelorDegrees();
    Assert.assertTrue(dataDao.getTopFiveListOfBachelorDegrees().isEmpty());
  }

  @Test
  public void updateGetDeleteStudentsStatesTest() {
    // add data
    MultipleValueAggregatedData wa = new MultipleValueAggregatedData();
    wa.setAnalyticTerm(MultipleValueAggregatedDataDao.LIST_OF_STUDENTS_STATES);
    wa.setAnalyticKey("WA");
    wa.setAnalyticValue(20);

    MultipleValueAggregatedData ma = new MultipleValueAggregatedData();
    ma.setAnalyticTerm(MultipleValueAggregatedDataDao.LIST_OF_STUDENTS_STATES);
    ma.setAnalyticKey("MA");
    ma.setAnalyticValue(21);

    // update in database
    List<MultipleValueAggregatedData> listOfStudentsStates = new ArrayList<>();
    listOfStudentsStates.add(wa);
    listOfStudentsStates.add(ma);
    dataDao.saveOrUpdateList(listOfStudentsStates);

    // query the database
    List<DataCount> result = dataDao.getListOfStudentsStatesCount();
    Assert.assertTrue(result.size() == 2);
    Assert.assertTrue(result.get(0).getDataKey().equals("MA"));
    Assert.assertTrue(result.get(0).getDataValue() == 21);
    Assert.assertTrue(result.get(1).getDataKey().equals("WA"));
    Assert.assertTrue(result.get(1).getDataValue() == 20);

    // clear the database
    dataDao.deleteListOfStudentsStatesCounts();
    Assert.assertTrue(dataDao.getListOfStudentsStatesCount().isEmpty());
  }

  @Test
  public void updateGetDeleteHighestDegreesTest() {
    // add data
    MultipleValueAggregatedData masters = new MultipleValueAggregatedData();
    masters.setAnalyticTerm(MultipleValueAggregatedDataDao.LIST_OF_DEGREES);
    masters.setAnalyticKey("Masters");
    masters.setAnalyticValue(20);

    MultipleValueAggregatedData bachelors = new MultipleValueAggregatedData();
    bachelors.setAnalyticTerm(MultipleValueAggregatedDataDao.LIST_OF_DEGREES);
    bachelors.setAnalyticKey("Bachelors");
    bachelors.setAnalyticValue(21);

    // update in database
    List<MultipleValueAggregatedData> listOfHighestDegrees = new ArrayList<>();
    listOfHighestDegrees.add(masters);
    listOfHighestDegrees.add(bachelors);
    dataDao.saveOrUpdateList(listOfHighestDegrees);

    // query the database
    List<DataCount> result = dataDao.getListOfHighestDegreesCount();
    Assert.assertTrue(result.size() == 2);
    Assert.assertTrue(result.get(0).getDataKey().equals("Bachelors"));
    Assert.assertTrue(result.get(0).getDataValue() == 21);
    Assert.assertTrue(result.get(1).getDataKey().equals("Masters"));
    Assert.assertTrue(result.get(1).getDataValue() == 20);

    // clear the database
    dataDao.deleteListOfHighestDegreesCounts();
    Assert.assertTrue(dataDao.getListOfHighestDegreesCount().isEmpty());
  }
}
