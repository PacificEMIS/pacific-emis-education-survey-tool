package fm.doe.national.database_test;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import fm.doe.national.data.persistence.dao.CategoryDao;
import fm.doe.national.data.persistence.dao.SurveyDao;
import fm.doe.national.data.persistence.entity.RoomCategory;
import fm.doe.national.database_test.util.RoomTestData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class RoomCategoryTests extends BaseDatabaseTest {

    private CategoryDao categoryDao;
    private long testSurveyId = -1;

    @Before
    @Override
    public void before() {
        super.before();
        categoryDao = database.getCategoryDao();
        fillSurveyTable();
    }

    private void fillSurveyTable() {
        SurveyDao surveyDao = database.getSurveyDao();
        surveyDao.insert(RoomTestData.getSurveyFor_putSurveyEntityTest());
        testSurveyId = surveyDao.getAll().get(0).uid;
    }

    @Test
    public void createDeleteTest() {
        categoryDao.deleteAllForSurveyWithId(testSurveyId);

        RoomCategory testCategory = RoomTestData.getCategoryFor_createDeleteTest(testSurveyId);

        categoryDao.insert(testCategory);
        assertEquals(1, categoryDao.getAllForSurveyWithId(testSurveyId).size());

        testCategory.title = "RoomCategory Two";
        categoryDao.insert(testCategory);

        List<RoomCategory> categoriesInDb = categoryDao.getAllForSurveyWithId(testSurveyId);
        assertEquals(2, categoriesInDb.size());
        assertEquals("RoomCategory Two", categoriesInDb.get(1).title);

        categoryDao.delete(categoriesInDb.get(0));

        categoriesInDb = categoryDao.getAllForSurveyWithId(testSurveyId);
        assertEquals(1, categoriesInDb.size());
        assertEquals("RoomCategory Two", categoriesInDb.get(0).title);

        categoryDao.insert(testCategory);
        categoryDao.deleteAllForSurveyWithId(testSurveyId);

        categoriesInDb = categoryDao.getAllForSurveyWithId(testSurveyId);
        assertEquals(0, categoriesInDb.size());
    }

    @Test
    public void updateTest() {
        categoryDao.deleteAllForSurveyWithId(testSurveyId);

        categoryDao.insert(RoomTestData.getCategoryFor_createDeleteTest(testSurveyId));
        RoomCategory insertedCategory = categoryDao.getAllForSurveyWithId(testSurveyId).get(0);
        assertEquals("RoomCategory One", insertedCategory.title);

        insertedCategory.title = "RoomCategory Two";
        categoryDao.update(insertedCategory);

        insertedCategory = categoryDao.getAllForSurveyWithId(testSurveyId).get(0);
        assertEquals("RoomCategory Two", insertedCategory.title);
    }

    @Test
    public void getByIdTest() {
        categoryDao.deleteAllForSurveyWithId(testSurveyId);
        categoryDao.insert(RoomTestData.getCategoryFor_createDeleteTest(testSurveyId));

        RoomCategory insertedCategory = categoryDao.getAllForSurveyWithId(testSurveyId).get(0);
        RoomCategory categoryById = categoryDao.getById(insertedCategory.uid);

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

        List<RoomCategory> insertedCategories = categoryDao.getAllForSurveyWithId(testSurveyId);
        assertEquals(6, insertedCategories.size());

        database.getSurveyDao().deleteById(testSurveyId);

        assertEquals(0, categoryDao.getAllForSurveyWithId(testSurveyId).size());

        insertedCategories.forEach(category -> assertNull(categoryDao.getById(category.uid)));

        fillSurveyTable();
    }
}