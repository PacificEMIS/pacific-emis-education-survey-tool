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
import fm.doe.national.data.persistence.dao.PhotoDao;
import fm.doe.national.data.persistence.dao.StandardDao;
import fm.doe.national.data.persistence.dao.SubCriteriaDao;
import fm.doe.national.data.persistence.dao.SurveyDao;
import fm.doe.national.data.persistence.entity.PhotoEntity;
import fm.doe.national.database_test.util.RoomTestData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class PhotoEntityTests {

    private PhotoDao photoDao;
    private AppDatabase database;

    private long testSubCriteriaId = -1;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        photoDao = database.getPhotoDao();

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
        long testCriteriaId = criteriaDao.getAllForStandardWithId(testStandardId).get(0).uid;


        SubCriteriaDao subCriteriaDao = database.getSubcriteriaDao();
        subCriteriaDao.insert(RoomTestData.getTestSubCriteria(testCriteriaId));
        testSubCriteriaId = subCriteriaDao.getAllForCriteriaWithId(testCriteriaId).get(0).uid;
    }

    @Test
    public void createDeleteTest() {
        photoDao.deleteAllForSubCriteriaWithId(testSubCriteriaId);

        PhotoEntity photoEntity = RoomTestData.getTestPhotoEntity(testSubCriteriaId);

        photoDao.insert(photoEntity);
        assertEquals(1, photoDao.getAllForSubCriteriaWithId(testSubCriteriaId).size());

        photoEntity.url = "http://images.unsplash.com/photo-1531804055935-76f44d7c3621?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjEyMDd9";
        photoDao.insert(photoEntity);

        List<PhotoEntity> photosInDb = photoDao.getAllForSubCriteriaWithId(testSubCriteriaId);
        assertEquals(2, photosInDb.size());
        assertEquals("http://images.unsplash.com/photo-1531804055935-76f44d7c3621?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjEyMDd9", photosInDb.get(1).url);

        photoDao.delete(photosInDb.get(0));

        photosInDb = photoDao.getAllForSubCriteriaWithId(testSubCriteriaId);
        assertEquals(1, photosInDb.size());
        assertEquals("http://images.unsplash.com/photo-1531804055935-76f44d7c3621?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjEyMDd9", photosInDb.get(0).url);

        photoDao.insert(photoEntity);
        photoDao.deleteAllForSubCriteriaWithId(testSubCriteriaId);

        photosInDb = photoDao.getAllForSubCriteriaWithId(testSubCriteriaId);
        assertEquals(0, photosInDb.size());
    }

    @Test
    public void updateTest() {
        photoDao.deleteAllForSubCriteriaWithId(testSubCriteriaId);

        photoDao.insert(RoomTestData.getTestPhotoEntity(testSubCriteriaId));
        PhotoEntity photoEntity = photoDao.getAllForSubCriteriaWithId(testSubCriteriaId).get(0);
        assertEquals("https://avatars1.githubusercontent.com/u/28600571?s=200&v=4", photoEntity.url);

        photoEntity.url = "http://images.unsplash.com/photo-1531804055935-76f44d7c3621?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjEyMDd9";
        photoDao.update(photoEntity);

        photoEntity = photoDao.getAllForSubCriteriaWithId(testSubCriteriaId).get(0);
        assertEquals("http://images.unsplash.com/photo-1531804055935-76f44d7c3621?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjEyMDd9", photoEntity.url);
    }

    @Test
    public void getByIdTest() {
        photoDao.deleteAllForSubCriteriaWithId(testSubCriteriaId);
        photoDao.insert(RoomTestData.getTestPhotoEntity(testSubCriteriaId));

        PhotoEntity photoEntity = photoDao.getAllForSubCriteriaWithId(testSubCriteriaId).get(0);
        PhotoEntity subCriteriaById = photoDao.getById(photoEntity.uid);

        assertEquals(photoEntity.url, subCriteriaById.url);
        assertNull(photoDao.getById(123984));
    }

    @Test
    public void cascadeDeleteTest() {
        photoDao.deleteAllForSubCriteriaWithId(testSubCriteriaId);

        photoDao.insert(RoomTestData.getTestPhotoEntity(testSubCriteriaId));
        photoDao.insert(RoomTestData.getTestPhotoEntity(testSubCriteriaId));
        photoDao.insert(RoomTestData.getTestPhotoEntity(testSubCriteriaId));
        photoDao.insert(RoomTestData.getTestPhotoEntity(testSubCriteriaId));
        photoDao.insert(RoomTestData.getTestPhotoEntity(testSubCriteriaId));
        photoDao.insert(RoomTestData.getTestPhotoEntity(testSubCriteriaId));

        List<PhotoEntity> insertedSubCriterias = photoDao.getAllForSubCriteriaWithId(testSubCriteriaId);
        assertEquals(6, insertedSubCriterias.size());

        database.getCategoryDao().deleteById(testSubCriteriaId);

        assertEquals(0, photoDao.getAllForSubCriteriaWithId(testSubCriteriaId).size());

        insertedSubCriterias.forEach(standard -> assertNull(photoDao.getById(standard.uid)));

        fillTable();
    }
}
