package fm.doe.national.database_test;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import fm.doe.national.data.persistence.AppDatabase;
import fm.doe.national.data.persistence.dao.CategoryDao;
import fm.doe.national.data.persistence.dao.CriteriaDao;
import fm.doe.national.data.persistence.dao.StandardDao;
import fm.doe.national.data.persistence.dao.SurveyDao;
import fm.doe.national.data.persistence.entity.Criteria;
import fm.doe.national.database_test.util.RoomTestData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class CriteriaTests {
    private CriteriaDao criteriaDao;
    private AppDatabase database;

    private long testStandardId = -1;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        criteriaDao = database.getCriteriaDao();

        fillTable();
    }

    @After
    public void closeDb() {
        database.close();
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

        Criteria testCriteria = RoomTestData.getTestCriteria(testStandardId);

        criteriaDao.insert(testCriteria);
        assertEquals(1, criteriaDao.getAllForStandardWithId(testStandardId).size());

        testCriteria.title = "Criteria 2";
        criteriaDao.insert(testCriteria);

        List<Criteria> criteriasInDb = criteriaDao.getAllForStandardWithId(testStandardId);
        assertEquals(2, criteriasInDb.size());
        assertEquals("Criteria 2", criteriasInDb.get(1).title);

        criteriaDao.delete(criteriasInDb.get(0));

        criteriasInDb = criteriaDao.getAllForStandardWithId(testStandardId);
        assertEquals(1, criteriasInDb.size());
        assertEquals("Criteria 2", criteriasInDb.get(0).title);

        criteriaDao.insert(testCriteria);
        criteriaDao.deleteAllForStandardWithId(testStandardId);

        criteriasInDb = criteriaDao.getAllForStandardWithId(testStandardId);
        assertEquals(0, criteriasInDb.size());
    }

    @Test
    public void updateTest() {
        criteriaDao.deleteAllForStandardWithId(testStandardId);

        criteriaDao.insert(RoomTestData.getTestCriteria(testStandardId));
        Criteria insertedStandard = criteriaDao.getAllForStandardWithId(testStandardId).get(0);
        assertEquals("Criteria One", insertedStandard.title);

        insertedStandard.title = "Criteria Two";
        criteriaDao.update(insertedStandard);

        insertedStandard = criteriaDao.getAllForStandardWithId(testStandardId).get(0);
        assertEquals("Criteria Two", insertedStandard.title);
    }

    @Test
    public void getByIdTest() {
        criteriaDao.deleteAllForStandardWithId(testStandardId);
        criteriaDao.insert(RoomTestData.getTestCriteria(testStandardId));

        Criteria insertedCriteria = criteriaDao.getAllForStandardWithId(testStandardId).get(0);
        Criteria criteriaById = criteriaDao.getById(insertedCriteria.uid);

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

        List<Criteria> insertedCriterias = criteriaDao.getAllForStandardWithId(testStandardId);
        assertEquals(6, insertedCriterias.size());

        database.getCategoryDao().deleteById(testStandardId);

        assertEquals(0, criteriaDao.getAllForStandardWithId(testStandardId).size());

        insertedCriterias.forEach(standard -> assertNull(criteriaDao.getById(standard.uid)));

        fillTable();
    }
}
