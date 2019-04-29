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
import fm.doe.national.data.persistence.dao.SurveyDao;
import fm.doe.national.data.persistence.entity.PersistenceCategory;
import fm.doe.national.database_test.util.RoomTestData;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class PersistenceCategoryTests {

    private CategoryDao categoryDao;
    private AppDatabase database;

    private long testSurveyId = -1;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        categoryDao = database.getCategoryDao();

        fillSurveyTable();
    }

    @After
    public void closeDb() {
        database.close();
    }

    private void fillSurveyTable() {
        SurveyDao surveyDao = database.getSurveyDao();
        surveyDao.insert(RoomTestData.getSurveyFor_putSurveyEntityTest());
        testSurveyId = surveyDao.getAll().get(0).uid;
    }

    @Test
    public void createDeleteTest() {
        categoryDao.deleteAllForSurveyWithId(testSurveyId);

        PersistenceCategory testCategory = RoomTestData.getCategoryFor_createDeleteTest(testSurveyId);

        categoryDao.insert(testCategory);
        assertEquals(1, categoryDao.getAllForSurveyWithId(testSurveyId).size());

        testCategory.title = "PersistenceCategory Two";
        categoryDao.insert(testCategory);

        List<PersistenceCategory> categoriesInDb = categoryDao.getAllForSurveyWithId(testSurveyId);
        assertEquals(2, categoriesInDb.size());
        assertEquals("PersistenceCategory Two", categoriesInDb.get(1).title);

        categoryDao.delete(categoriesInDb.get(0));

        categoriesInDb = categoryDao.getAllForSurveyWithId(testSurveyId);
        assertEquals(1, categoriesInDb.size());
        assertEquals("PersistenceCategory Two", categoriesInDb.get(0).title);

        categoryDao.insert(testCategory);
        categoryDao.deleteAllForSurveyWithId(testSurveyId);

        categoriesInDb = categoryDao.getAllForSurveyWithId(testSurveyId);
        assertEquals(0, categoriesInDb.size());
    }

    @Test
    public void updateTest() {
        categoryDao.deleteAllForSurveyWithId(testSurveyId);

        categoryDao.insert(RoomTestData.getCategoryFor_createDeleteTest(testSurveyId));
        PersistenceCategory insertedCategory = categoryDao.getAllForSurveyWithId(testSurveyId).get(0);
        assertEquals("PersistenceCategory One", insertedCategory.title);

        insertedCategory.title = "PersistenceCategory Two";
        categoryDao.update(insertedCategory);

        insertedCategory = categoryDao.getAllForSurveyWithId(testSurveyId).get(0);
        assertEquals("PersistenceCategory Two", insertedCategory.title);
    }

    @Test
    public void getByIdTest() {
        categoryDao.deleteAllForSurveyWithId(testSurveyId);
        categoryDao.insert(RoomTestData.getCategoryFor_createDeleteTest(testSurveyId));

        PersistenceCategory insertedCategory = categoryDao.getAllForSurveyWithId(testSurveyId).get(0);
        PersistenceCategory categoryById = categoryDao.getById(insertedCategory.uid);

        assertEquals(insertedCategory.title, categoryById.title);
        assertNull(categoryDao.getById(123984));
    }

    @Test
    public void cascadeDeleteTest() {
        categoryDao.deleteAllForSurveyWithId(testSurveyId);

        categoryDao.insert(RoomTestData.getCategoryFor_createDeleteTest(testSurveyId));
        categoryDao.insert(RoomTestData.getCategoryFor_createDeleteTest(testSurveyId));
        categoryDao.insert(RoomTestData.getCategoryFor_createDeleteTest(testSurveyId));
        categoryDao.insert(RoomTestData.getCategoryFor_createDeleteTest(testSurveyId));
        categoryDao.insert(RoomTestData.getCategoryFor_createDeleteTest(testSurveyId));
        categoryDao.insert(RoomTestData.getCategoryFor_createDeleteTest(testSurveyId));

        List<PersistenceCategory> insertedCategories = categoryDao.getAllForSurveyWithId(testSurveyId);
        assertEquals(6, insertedCategories.size());

        database.getSurveyDao().deleteById(testSurveyId);

        assertEquals(0, categoryDao.getAllForSurveyWithId(testSurveyId).size());

        insertedCategories.forEach(category -> assertNull(categoryDao.getById(category.uid)));

        fillSurveyTable();
    }
}