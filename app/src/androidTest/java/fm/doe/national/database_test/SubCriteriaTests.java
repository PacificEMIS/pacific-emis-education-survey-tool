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
import fm.doe.national.data.persistence.dao.SubCriteriaDao;
import fm.doe.national.data.persistence.dao.SurveyDao;
import fm.doe.national.data.persistence.entity.SubCriteria;
import fm.doe.national.database_test.util.RoomTestData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class SubCriteriaTests {
    private SubCriteriaDao subCriteriaDao;
    private AppDatabase database;

    private long testCriteriaId = -1;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        subCriteriaDao = database.getSubcriteriaDao();

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
        long testStandardId = standardDao.getAllForCategoryWithId(testCategoryId).get(0).uid;


        CriteriaDao criteriaDao = database.getCriteriaDao();
        criteriaDao.insert(RoomTestData.getTestCriteria(testStandardId));
        testCriteriaId = criteriaDao.getAllForStandardWithId(testStandardId).get(0).uid;
    }

    @Test
    public void createDeleteTest() {
        subCriteriaDao.deleteAllForCriteriaWithId(testCriteriaId);

        SubCriteria subCriteria = RoomTestData.getTestSubCriteria(testCriteriaId);

        subCriteriaDao.insert(subCriteria);
        assertEquals(1, subCriteriaDao.getAllForCriteriaWithId(testCriteriaId).size());

        subCriteria.title = "SubCriteria 2";
        subCriteriaDao.insert(subCriteria);

        List<SubCriteria> subCriteriasInDb = subCriteriaDao.getAllForCriteriaWithId(testCriteriaId);
        assertEquals(2, subCriteriasInDb.size());
        assertEquals("SubCriteria 2", subCriteriasInDb.get(1).title);

        subCriteriaDao.delete(subCriteriasInDb.get(0));

        subCriteriasInDb = subCriteriaDao.getAllForCriteriaWithId(testCriteriaId);
        assertEquals(1, subCriteriasInDb.size());
        assertEquals("SubCriteria 2", subCriteriasInDb.get(0).title);

        subCriteriaDao.insert(subCriteria);
        subCriteriaDao.deleteAllForCriteriaWithId(testCriteriaId);

        subCriteriasInDb = subCriteriaDao.getAllForCriteriaWithId(testCriteriaId);
        assertEquals(0, subCriteriasInDb.size());
    }

    @Test
    public void updateTest() {
        subCriteriaDao.deleteAllForCriteriaWithId(testCriteriaId);

        subCriteriaDao.insert(RoomTestData.getTestSubCriteria(testCriteriaId));
        SubCriteria subCriteria = subCriteriaDao.getAllForCriteriaWithId(testCriteriaId).get(0);
        assertEquals("SubCriteria One", subCriteria.title);

        subCriteria.title = "SubCriteria Two";
        subCriteriaDao.update(subCriteria);

        subCriteria = subCriteriaDao.getAllForCriteriaWithId(testCriteriaId).get(0);
        assertEquals("SubCriteria Two", subCriteria.title);
    }

    @Test
    public void getByIdTest() {
        subCriteriaDao.deleteAllForCriteriaWithId(testCriteriaId);
        subCriteriaDao.insert(RoomTestData.getTestSubCriteria(testCriteriaId));

        SubCriteria subCriteria = subCriteriaDao.getAllForCriteriaWithId(testCriteriaId).get(0);
        SubCriteria subCriteriaById = subCriteriaDao.getById(subCriteria.uid);

        assertEquals(subCriteria.title, subCriteriaById.title);
        assertNull(subCriteriaDao.getById(123984));
    }

    @Test
    public void cascadeDeleteTest() {
        subCriteriaDao.deleteAllForCriteriaWithId(testCriteriaId);

        subCriteriaDao.insert(RoomTestData.getTestSubCriteria(testCriteriaId));
        subCriteriaDao.insert(RoomTestData.getTestSubCriteria(testCriteriaId));
        subCriteriaDao.insert(RoomTestData.getTestSubCriteria(testCriteriaId));
        subCriteriaDao.insert(RoomTestData.getTestSubCriteria(testCriteriaId));
        subCriteriaDao.insert(RoomTestData.getTestSubCriteria(testCriteriaId));
        subCriteriaDao.insert(RoomTestData.getTestSubCriteria(testCriteriaId));

        List<SubCriteria> insertedSubCriterias = subCriteriaDao.getAllForCriteriaWithId(testCriteriaId);
        assertEquals(6, insertedSubCriterias.size());

        database.getCategoryDao().deleteById(testCriteriaId);

        assertEquals(0, subCriteriaDao.getAllForCriteriaWithId(testCriteriaId).size());

        insertedSubCriterias.forEach(standard -> assertNull(subCriteriaDao.getById(standard.uid)));

        fillTable();
    }
}