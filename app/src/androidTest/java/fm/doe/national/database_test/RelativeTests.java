package fm.doe.national.database_test;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import fm.doe.national.data.persistence.AppDatabase;
import fm.doe.national.data.persistence.dao.AnswerDao;
import fm.doe.national.data.persistence.dao.CategoryDao;
import fm.doe.national.data.persistence.dao.CriteriaDao;
import fm.doe.national.data.persistence.dao.PhotoDao;
import fm.doe.national.data.persistence.dao.StandardDao;
import fm.doe.national.data.persistence.dao.SubCriteriaDao;
import fm.doe.national.data.persistence.dao.SurveyDao;
import fm.doe.national.data.persistence.entity.relative.RelativePersistenceAnswer;
import fm.doe.national.database_test.util.RoomTestData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class RelativeTests {

    private AppDatabase database;

    private long testSurveyId;
    private long testCategoryId;
    private long testStandardId;
    private long testCriteriaId;
    private long testSubCriteriaId;
    private long testAnswerId;
    private long testPhotoId;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() {
        database.close();
    }

    private void fillTable() {
        SurveyDao surveyDao = database.getSurveyDao();
        surveyDao.insert(RoomTestData.getSurveyFor_putSurveyEntityTest());
        testSurveyId = surveyDao.getAll().get(0).uid;

        CategoryDao categoryDao = database.getCategoryDao();
        categoryDao.insert(RoomTestData.getCategoryFor_createDeleteTest(testSurveyId));
        testCategoryId = categoryDao.getAllForSurveyWithId(testSurveyId).get(0).uid;

        StandardDao standardDao = database.getStandardDao();
        standardDao.insert(RoomTestData.getTestStandard(testCategoryId));
        testStandardId = standardDao.getAllForCategoryWithId(testCategoryId).get(0).uid;

        CriteriaDao criteriaDao = database.getCriteriaDao();
        criteriaDao.insert(RoomTestData.getTestCriteria(testStandardId));
        testCriteriaId = criteriaDao.getAllForStandardWithId(testStandardId).get(0).uid;

        SubCriteriaDao subCriteriaDao = database.getSubcriteriaDao();
        subCriteriaDao.insert(RoomTestData.getTestSubCriteria(testCriteriaId));
        testSubCriteriaId = subCriteriaDao.getAllForCriteriaWithId(testCriteriaId).get(0).uid;

        AnswerDao answerDao = database.getAnswerDao();
        answerDao.insert(RoomTestData.getTestAnswer(testSubCriteriaId));
        testAnswerId = answerDao.getAllForSubCriteriaWithId(testSubCriteriaId).get(0).uid;


        PhotoDao photoDao = database.getPhotoDao();
        photoDao.insert(RoomTestData.getTestPhotoEntity(testAnswerId));
        testPhotoId = photoDao.getAllForAnswerWithId(testAnswerId).get(0).uid;
    }

    @Test
    public void testCascadeDelete() {
        fillTable();

        database.getSurveyDao().deleteById(testSurveyId);

        assertNull(database.getPhotoDao().getById(testPhotoId));
        assertNull(database.getAnswerDao().getById(testAnswerId));
        assertNull(database.getSubcriteriaDao().getById(testSubCriteriaId));
        assertNull(database.getCategoryDao().getById(testCategoryId));
        assertNull(database.getCriteriaDao().getById(testCriteriaId));
        assertNull(database.getStandardDao().getById(testStandardId));
        assertNull(database.getSurveyDao().getById(testSurveyId));
    }

    @Test
    public void testAnswerPhotos() {
        database.getSurveyDao().deleteById(testSurveyId);
        fillTable();

        RelativePersistenceAnswer relativePersistenceAnswer = database.getAnswerDao().getFilledById(testAnswerId);
        assertNotNull(relativePersistenceAnswer.answer);
        assertEquals(testAnswerId, relativePersistenceAnswer.answer.getId());
        assertNotNull(relativePersistenceAnswer.photos);
        assertFalse(relativePersistenceAnswer.photos.isEmpty());
        assertEquals(relativePersistenceAnswer.photos.get(0).getRemotePath(), database.getPhotoDao().getById(testPhotoId).getRemotePath());
    }

}
