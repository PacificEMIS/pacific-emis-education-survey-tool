package fm.doe.national.database_test;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import fm.doe.national.data.persistence.dao.AnswerDao;
import fm.doe.national.data.persistence.dao.CategoryDao;
import fm.doe.national.data.persistence.dao.CriteriaDao;
import fm.doe.national.data.persistence.dao.PhotoDao;
import fm.doe.national.data.persistence.dao.StandardDao;
import fm.doe.national.data.persistence.dao.SubCriteriaDao;
import fm.doe.national.data.persistence.dao.SurveyDao;
import fm.doe.national.data.persistence.entity.RoomPhoto;
import fm.doe.national.database_test.util.RoomTestData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class RoomPhotoTests extends BaseDatabaseTest {

    private PhotoDao photoDao;

    private long testAnswerId = -1;

    @Before
    @Override
    public void before() {
        super.before();
        photoDao = database.getPhotoDao();
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
        long testStandardId = standardDao.getAllForCategoryWithId(testCategoryId).get(0).uid;

        CriteriaDao criteriaDao = database.getCriteriaDao();
        criteriaDao.insert(RoomTestData.getTestCriteria(testStandardId));
        long testCriteriaId = criteriaDao.getAllForStandardWithId(testStandardId).get(0).uid;

        SubCriteriaDao subCriteriaDao = database.getSubcriteriaDao();
        subCriteriaDao.insert(RoomTestData.getTestSubCriteria(testCriteriaId));
        long testSubCriteriaId = subCriteriaDao.getAllForCriteriaWithId(testCriteriaId).get(0).uid;

        AnswerDao answerDao = database.getAnswerDao();
        answerDao.insert(RoomTestData.getTestAnswer(testSubCriteriaId));
        testAnswerId = answerDao.getAllForSubCriteriaWithId(testSubCriteriaId).get(0).uid;

    }

    @Test
    public void createDeleteTest() {
        photoDao.deleteAllForAnswerWithId(testAnswerId);

        RoomPhoto photoEntity = RoomTestData.getTestPhotoEntity(testAnswerId);

        photoDao.insert(photoEntity);
        assertEquals(1, photoDao.getAllForAnswerWithId(testAnswerId).size());

        photoEntity.remoteUrl = "http://images.unsplash.com/photo-1531804055935-76f44d7c3621?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjEyMDd9";
        photoDao.insert(photoEntity);

        List<RoomPhoto> photosInDb = photoDao.getAllForAnswerWithId(testAnswerId);
        assertEquals(2, photosInDb.size());
        assertEquals("http://images.unsplash.com/photo-1531804055935-76f44d7c3621?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjEyMDd9", photosInDb.get(1).remoteUrl);

        photoDao.delete(photosInDb.get(0));

        photosInDb = photoDao.getAllForAnswerWithId(testAnswerId);
        assertEquals(1, photosInDb.size());
        assertEquals("http://images.unsplash.com/photo-1531804055935-76f44d7c3621?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjEyMDd9", photosInDb.get(0).remoteUrl);

        photoDao.insert(photoEntity);
        photoDao.deleteAllForAnswerWithId(testAnswerId);

        photosInDb = photoDao.getAllForAnswerWithId(testAnswerId);
        assertEquals(0, photosInDb.size());
    }

    @Test
    public void updateTest() {
        photoDao.deleteAllForAnswerWithId(testAnswerId);

        photoDao.insert(RoomTestData.getTestPhotoEntity(testAnswerId));
        RoomPhoto photoEntity = photoDao.getAllForAnswerWithId(testAnswerId).get(0);
        assertEquals("https://avatars1.githubusercontent.com/u/28600571?s=200&v=4", photoEntity.remoteUrl);

        photoEntity.remoteUrl = "http://images.unsplash.com/photo-1531804055935-76f44d7c3621?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjEyMDd9";
        photoDao.update(photoEntity);

        photoEntity = photoDao.getAllForAnswerWithId(testAnswerId).get(0);
        assertEquals("http://images.unsplash.com/photo-1531804055935-76f44d7c3621?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjEyMDd9", photoEntity.remoteUrl);
    }

    @Test
    public void getByIdTest() {
        photoDao.getAllForAnswerWithId(testAnswerId);
        photoDao.insert(RoomTestData.getTestPhotoEntity(testAnswerId));

        RoomPhoto photoEntity = photoDao.getAllForAnswerWithId(testAnswerId).get(0);
        RoomPhoto photoById = photoDao.getById(photoEntity.uid);

        assertEquals(photoEntity.remoteUrl, photoById.remoteUrl);
        assertNull(photoDao.getById(123984));
    }

    @Test
    public void cascadeDeleteTest() {
        photoDao.deleteAllForAnswerWithId(testAnswerId);

        photoDao.insert(RoomTestData.getTestPhotoEntity(testAnswerId));
        photoDao.insert(RoomTestData.getTestPhotoEntity(testAnswerId));
        photoDao.insert(RoomTestData.getTestPhotoEntity(testAnswerId));
        photoDao.insert(RoomTestData.getTestPhotoEntity(testAnswerId));
        photoDao.insert(RoomTestData.getTestPhotoEntity(testAnswerId));
        photoDao.insert(RoomTestData.getTestPhotoEntity(testAnswerId));

        List<RoomPhoto> insertedSubCriterias = photoDao.getAllForAnswerWithId(testAnswerId);
        assertEquals(6, insertedSubCriterias.size());

        database.getCategoryDao().deleteById(testAnswerId);

        assertEquals(0, photoDao.getAllForAnswerWithId(testAnswerId).size());

        insertedSubCriterias.forEach(standard -> assertNull(photoDao.getById(standard.uid)));

        fillTable();
    }
}
