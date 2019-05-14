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
import fm.doe.national.data.persistence.entity.relative.RelativeRoomAnswer;
import fm.doe.national.data.persistence.entity.relative.RelativeRoomCategory;
import fm.doe.national.data.persistence.entity.relative.RelativeRoomCriteria;
import fm.doe.national.data.persistence.entity.relative.RelativeRoomStandard;
import fm.doe.national.data.persistence.entity.relative.RelativeRoomSubCriteria;
import fm.doe.national.data.persistence.entity.relative.RelativeRoomSurvey;
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
    public void testAnswerRelations() {
        database.getSurveyDao().deleteById(testSurveyId);
        fillTable();

        RelativeRoomAnswer relativeRoomAnswer = database.getAnswerDao().getFilledById(testAnswerId);
        assertNotNull(relativeRoomAnswer.answer);
        assertEquals(testAnswerId, relativeRoomAnswer.answer.getId());
        assertNotNull(relativeRoomAnswer.photos);
        assertFalse(relativeRoomAnswer.photos.isEmpty());
        assertEquals(relativeRoomAnswer.photos.get(0).getRemotePath(), database.getPhotoDao().getById(testPhotoId).getRemotePath());
    }

    @Test
    public void setTestSubCriteriaRelations() {
        database.getSurveyDao().deleteById(testSurveyId);
        fillTable();

        RelativeRoomSubCriteria relativeRoomSubCriteria = database.getSubcriteriaDao().getFilledById(testSubCriteriaId);
        assertNotNull(relativeRoomSubCriteria.answers);
        assertFalse(relativeRoomSubCriteria.answers.isEmpty());
        assertEquals(testAnswerId, relativeRoomSubCriteria.answers.get(0).answer.getId());
        assertEquals(testPhotoId, relativeRoomSubCriteria.answers.get(0).photos.get(0).getId());
    }

    @Test
    public void setTestCriteriaRelations() {
        database.getSurveyDao().deleteById(testSurveyId);
        fillTable();

        RelativeRoomCriteria relativeRoomCriteria = database.getCriteriaDao().getFilledById(testCriteriaId);
        assertNotNull(relativeRoomCriteria.criteria);
        assertNotNull(relativeRoomCriteria.subCriterias);
        assertFalse(relativeRoomCriteria.subCriterias.isEmpty());
        assertEquals(testSubCriteriaId, relativeRoomCriteria.subCriterias.get(0).subCriteria.getId());
        assertEquals(testAnswerId, relativeRoomCriteria.subCriterias.get(0).answers.get(0).answer.getId());
        assertEquals(testPhotoId, relativeRoomCriteria.subCriterias.get(0).answers.get(0).photos.get(0).getId());
    }

    @Test
    public void setTestStandardRelations() {
        database.getSurveyDao().deleteById(testSurveyId);
        fillTable();

        RelativeRoomStandard relativeRoomStandard = database.getStandardDao().getFilledById(testStandardId);
        assertNotNull(relativeRoomStandard.standard);
        assertNotNull(relativeRoomStandard.criterias);
        assertFalse(relativeRoomStandard.criterias.isEmpty());
        assertEquals(testCriteriaId, relativeRoomStandard.criterias.get(0).criteria.getId());
        assertEquals(testSubCriteriaId, relativeRoomStandard.criterias.get(0).subCriterias.get(0).subCriteria.getId());
        assertEquals(testAnswerId, relativeRoomStandard.criterias.get(0).subCriterias.get(0).answers.get(0).answer.getId());
        assertEquals(testPhotoId, relativeRoomStandard.criterias.get(0).subCriterias.get(0).answers.get(0).photos.get(0).getId());
    }

    @Test
    public void setTestCategoryRelations() {
        database.getSurveyDao().deleteById(testSurveyId);
        fillTable();

        RelativeRoomCategory relativeRoomCategory = database.getCategoryDao().getFilledById(testCategoryId);
        assertNotNull(relativeRoomCategory.category);
        assertNotNull(relativeRoomCategory.standards);
        assertFalse(relativeRoomCategory.standards.isEmpty());
        assertEquals(testStandardId, relativeRoomCategory.standards.get(0).standard.getId());
        assertEquals(testCriteriaId, relativeRoomCategory.standards.get(0).criterias.get(0).criteria.getId());
        assertEquals(testSubCriteriaId, relativeRoomCategory.standards.get(0).criterias.get(0).subCriterias.get(0).subCriteria.getId());
        assertEquals(testAnswerId, relativeRoomCategory.standards.get(0).criterias.get(0).subCriterias.get(0).answers.get(0).answer.getId());
        assertEquals(testPhotoId, relativeRoomCategory.standards.get(0).criterias.get(0).subCriterias.get(0).answers.get(0).photos.get(0).getId());
    }

    @Test
    public void setTestSurveyRelations() {
        database.getSurveyDao().deleteById(testSurveyId);
        fillTable();

        RelativeRoomSurvey relativeRoomSurvey = database.getSurveyDao().getFilledById(testSurveyId);
        assertNotNull(relativeRoomSurvey.survey);
        assertNotNull(relativeRoomSurvey.categories);
        assertFalse(relativeRoomSurvey.categories.isEmpty());
        assertEquals(testCategoryId, relativeRoomSurvey.categories.get(0).category.getId());
        assertEquals(testStandardId, relativeRoomSurvey.categories.get(0).standards.get(0).standard.getId());
        assertEquals(testCriteriaId, relativeRoomSurvey.categories.get(0).standards.get(0).criterias.get(0).criteria.getId());
        assertEquals(testSubCriteriaId, relativeRoomSurvey.categories.get(0).standards.get(0).criterias.get(0).subCriterias.get(0).subCriteria.getId());
        assertEquals(testAnswerId, relativeRoomSurvey.categories.get(0).standards.get(0).criterias.get(0).subCriterias.get(0).answers.get(0).answer.getId());
        assertEquals(testPhotoId, relativeRoomSurvey.categories.get(0).standards.get(0).criterias.get(0).subCriterias.get(0).answers.get(0).photos.get(0).getId());
    }

}
