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
import fm.doe.national.data.persistence.dao.StandardDao;
import fm.doe.national.data.persistence.dao.SurveyDao;
import fm.doe.national.data.persistence.entity.RoomStandard;
import fm.doe.national.database_test.util.RoomTestData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class RoomStandardTests {

    private StandardDao standardDao;
    private AppDatabase database;

    private long testCategoryId = -1;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        standardDao = database.getStandardDao();

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
        testCategoryId = categoryDao.getAllForSurveyWithId(testSurveyId).get(0).uid;
    }

    @Test
    public void createDeleteTest() {
        standardDao.deleteAllForCategoryWithId(testCategoryId);

        RoomStandard testStandard = RoomTestData.getTestStandard(testCategoryId);

        standardDao.insert(testStandard);
        assertEquals(1, standardDao.getAllForCategoryWithId(testCategoryId).size());

        testStandard.title = "RoomStandard 2";
        standardDao.insert(testStandard);

        List<RoomStandard> standardsInDb = standardDao.getAllForCategoryWithId(testCategoryId);
        assertEquals(2, standardsInDb.size());
        assertEquals("RoomStandard 2", standardsInDb.get(1).title);

        standardDao.delete(standardsInDb.get(0));

        standardsInDb = standardDao.getAllForCategoryWithId(testCategoryId);
        assertEquals(1, standardsInDb.size());
        assertEquals("RoomStandard 2", standardsInDb.get(0).title);

        standardDao.insert(testStandard);
        standardDao.deleteAllForCategoryWithId(testCategoryId);

        standardsInDb = standardDao.getAllForCategoryWithId(testCategoryId);
        assertEquals(0, standardsInDb.size());
    }

    @Test
    public void updateTest() {
        standardDao.deleteAllForCategoryWithId(testCategoryId);

        standardDao.insert(RoomTestData.getTestStandard(testCategoryId));
        RoomStandard insertedStandard = standardDao.getAllForCategoryWithId(testCategoryId).get(0);
        assertEquals("RoomStandard One", insertedStandard.title);

        insertedStandard.title = "RoomStandard Two";
        standardDao.update(insertedStandard);

        insertedStandard = standardDao.getAllForCategoryWithId(testCategoryId).get(0);
        assertEquals("RoomStandard Two", insertedStandard.title);
    }

    @Test
    public void getByIdTest() {
        standardDao.deleteAllForCategoryWithId(testCategoryId);
        standardDao.insert(RoomTestData.getTestStandard(testCategoryId));

        RoomStandard insertedStandard = standardDao.getAllForCategoryWithId(testCategoryId).get(0);
        RoomStandard standardById = standardDao.getById(insertedStandard.uid);

        assertEquals(insertedStandard.title, standardById.title);
        assertNull(standardDao.getById(123984));
    }

    @Test
    public void cascadeDeleteTest() {
        standardDao.deleteAllForCategoryWithId(testCategoryId);

        standardDao.insert(RoomTestData.getTestStandard(testCategoryId));
        standardDao.insert(RoomTestData.getTestStandard(testCategoryId));
        standardDao.insert(RoomTestData.getTestStandard(testCategoryId));
        standardDao.insert(RoomTestData.getTestStandard(testCategoryId));
        standardDao.insert(RoomTestData.getTestStandard(testCategoryId));
        standardDao.insert(RoomTestData.getTestStandard(testCategoryId));

        List<RoomStandard> insertedStandards = standardDao.getAllForCategoryWithId(testCategoryId);
        assertEquals(6, insertedStandards.size());

        database.getCategoryDao().deleteById(testCategoryId);

        assertEquals(0, standardDao.getAllForCategoryWithId(testCategoryId).size());

        insertedStandards.forEach(standard -> assertNull(standardDao.getById(standard.uid)));

        fillTable();
    }
}
