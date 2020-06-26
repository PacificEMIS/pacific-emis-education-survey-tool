package org.pacific_emis.surveys.database_test;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import org.pacific_emis.surveys.core.data.persistence.dao.CategoryDao;
import org.pacific_emis.surveys.core.data.persistence.dao.CriteriaDao;
import org.pacific_emis.surveys.core.data.persistence.dao.StandardDao;
import org.pacific_emis.surveys.core.data.persistence.dao.SurveyDao;
import org.pacific_emis.surveys.core.data.persistence.entity.RoomCriteria;
import org.pacific_emis.surveys.database_test.util.RoomTestData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class RoomCriteriaTests extends BaseDatabaseTest {
    private CriteriaDao criteriaDao;

    private long testStandardId = -1;

    @Before
    @Override
    public void before() {
        super.before();
        criteriaDao = database.getCriteriaDao();
        fillTable();
    }

    private void fillTable() {
        SurveyDao surveyDao = database.getSurveyDao();
        surveyDao.insert(RoomTestData.getSurveyFor_putSurveyEntityTest());
        long testSurveyId = surveyDao.getAll().get(0).uid;

        CategoryDao categoryDao = database.getCategoryDao();
        categoryDao.insert(RoomTestData.getCategoryFor_createDeleteTest(testSurveyId));
        long testCategoryId = categoryDao.getAllForSurveyWithId(testSurveyId).get(0).uid;

        StandardDao standardDao = database.getStandardDao();
        standardDao.insert(RoomTestData.getTestStandard(testCategoryId));
        testStandardId = standardDao.getAllForCategoryWithId(testCategoryId).get(0).uid;
    }

    @Test
    public void createDeleteTest() {
        criteriaDao.deleteAllForStandardWithId(testStandardId);

        RoomCriteria testCriteria = RoomTestData.getTestCriteria(testStandardId);

        criteriaDao.insert(testCriteria);
        assertEquals(1, criteriaDao.getAllForStandardWithId(testStandardId).size());

        testCriteria.title = "RoomCriteria 2";
        criteriaDao.insert(testCriteria);

        List<RoomCriteria> criteriasInDb = criteriaDao.getAllForStandardWithId(testStandardId);
        assertEquals(2, criteriasInDb.size());
        assertEquals("RoomCriteria 2", criteriasInDb.get(1).title);

        criteriaDao.delete(criteriasInDb.get(0));

        criteriasInDb = criteriaDao.getAllForStandardWithId(testStandardId);
        assertEquals(1, criteriasInDb.size());
        assertEquals("RoomCriteria 2", criteriasInDb.get(0).title);

        criteriaDao.insert(testCriteria);
        criteriaDao.deleteAllForStandardWithId(testStandardId);

        criteriasInDb = criteriaDao.getAllForStandardWithId(testStandardId);
        assertEquals(0, criteriasInDb.size());
    }

    @Test
    public void updateTest() {
        criteriaDao.deleteAllForStandardWithId(testStandardId);

        criteriaDao.insert(RoomTestData.getTestCriteria(testStandardId));
        RoomCriteria insertedStandard = criteriaDao.getAllForStandardWithId(testStandardId).get(0);
        assertEquals("RoomCriteria One", insertedStandard.title);

        insertedStandard.title = "RoomCriteria Two";
        criteriaDao.update(insertedStandard);

        insertedStandard = criteriaDao.getAllForStandardWithId(testStandardId).get(0);
        assertEquals("RoomCriteria Two", insertedStandard.title);
    }

    @Test
    public void getByIdTest() {
        criteriaDao.deleteAllForStandardWithId(testStandardId);
        criteriaDao.insert(RoomTestData.getTestCriteria(testStandardId));

        RoomCriteria insertedCriteria = criteriaDao.getAllForStandardWithId(testStandardId).get(0);
        RoomCriteria criteriaById = criteriaDao.getById(insertedCriteria.uid);

        assertEquals(insertedCriteria.title, criteriaById.title);
        assertNull(criteriaDao.getById(123984));
    }

    @Test
    public void cascadeDeleteTest() {
        criteriaDao.deleteAllForStandardWithId(testStandardId);

        criteriaDao.insert(RoomTestData.getTestCriteria(testStandardId));
        criteriaDao.insert(RoomTestData.getTestCriteria(testStandardId));
        criteriaDao.insert(RoomTestData.getTestCriteria(testStandardId));
        criteriaDao.insert(RoomTestData.getTestCriteria(testStandardId));
        criteriaDao.insert(RoomTestData.getTestCriteria(testStandardId));
        criteriaDao.insert(RoomTestData.getTestCriteria(testStandardId));

        List<RoomCriteria> insertedCriterias = criteriaDao.getAllForStandardWithId(testStandardId);
        assertEquals(6, insertedCriterias.size());

        database.getCategoryDao().deleteById(testStandardId);

        assertEquals(0, criteriaDao.getAllForStandardWithId(testStandardId).size());

        insertedCriterias.forEach(standard -> assertNull(criteriaDao.getById(standard.uid)));

        fillTable();
    }
}
