package fm.doe.national.database_test;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import fm.doe.national.data.persistence.AppDatabase;
import fm.doe.national.data.persistence.dao.SurveyDao;
import fm.doe.national.data.persistence.entity.PersistenceSurvey;
import fm.doe.national.data.persistence.new_model.SurveyType;
import fm.doe.national.database_test.util.RoomTestData;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class PersistenceSurveyTests {

    private SurveyDao surveyDao;
    private AppDatabase database;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        surveyDao = database.getSurveyDao();
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Test
    public void putSurveyEntityTest() {
        surveyDao.deleteAll();

        PersistenceSurvey survey = RoomTestData.getSurveyFor_putSurveyEntityTest();
        surveyDao.insert(survey);

        List<PersistenceSurvey> allSurveys = surveyDao.getAll();
        assertEquals(allSurveys.size(), 1);
        assertEquals(allSurveys.get(0).getId(), 1);
        assertEquals(allSurveys.get(0).getVersion(), 1);
        assertEquals(allSurveys.get(0).getSurveyType(), SurveyType.SCHOOL_ACCREDITATION);

        survey = new PersistenceSurvey(RoomTestData.getSurveyFor_updateSurveyTest());
        surveyDao.insert(survey);

        allSurveys = surveyDao.getAll();
        assertEquals(allSurveys.size(), 2);
        assertNotEquals(allSurveys.get(0).getId(), allSurveys.get(1).getId());
        assertEquals(allSurveys.get(1).getId(), 2);
        assertEquals(allSurveys.get(0).getVersion(), 1);
        assertEquals(allSurveys.get(1).getVersion(), 3);
        assertEquals(allSurveys.get(1).getSurveyType(), SurveyType.WASH);
    }

    @Test
    public void deleteSurveysTest() {
        PersistenceSurvey survey = new PersistenceSurvey(RoomTestData.getSurveyFor_putSurveyEntityTest());
        surveyDao.insert(survey);
        surveyDao.insert(survey); // twice

        PersistenceSurvey existingSurvey = surveyDao.getAll().get(0);
        surveyDao.delete(existingSurvey);
        assertEquals(1, surveyDao.getAll().size());

        surveyDao.insert(survey);
        surveyDao.deleteAll();

        assertTrue(surveyDao.getAll().isEmpty());
    }

    @Test
    public void updateSurveyTest() {
        surveyDao.deleteAll();
        surveyDao.insert(RoomTestData.getSurveyFor_putSurveyEntityTest());

        PersistenceSurvey existingSurvey = surveyDao.getAll().get(0);
        assertEquals(SurveyType.SCHOOL_ACCREDITATION, existingSurvey.getSurveyType());

        PersistenceSurvey updatedSurvey = RoomTestData.getSurveyFor_updateSurveyTest();
        updatedSurvey.uid = existingSurvey.getId();
        surveyDao.update(updatedSurvey);

        updatedSurvey = surveyDao.getAll().get(0);
        assertEquals(SurveyType.WASH, updatedSurvey.getSurveyType());
    }

    @Test
    public void updateIsNotCreateTest() {
        surveyDao.deleteAll();
        surveyDao.update(RoomTestData.getSurveyFor_updateSurveyTest());
        assertTrue(surveyDao.getAll().isEmpty());
    }

    @Test
    public void getByIdTest() {
        surveyDao.deleteAll();
        surveyDao.insert(RoomTestData.getSurveyFor_updateSurveyTest());

        PersistenceSurvey survey = surveyDao.getAll().get(0);
        PersistenceSurvey surveyById = surveyDao.getById(survey.getId());

        assertEquals(survey.getVersion(), surveyById.getVersion());

        assertNull(surveyDao.getById(78235235));
    }

    @Test
    public void deleteByIdTest() {
        surveyDao.deleteAll();

        surveyDao.insert(RoomTestData.getSurveyFor_updateSurveyTest());
        surveyDao.insert(RoomTestData.getSurveyFor_updateSurveyTest());
        surveyDao.insert(RoomTestData.getSurveyFor_updateSurveyTest());

        long firstId = surveyDao.getAll().get(0).getId();
        long secondId = surveyDao.getAll().get(1).getId();
        long thirdId = surveyDao.getAll().get(2).getId();

        surveyDao.deleteById(firstId);

        List<PersistenceSurvey> surveys = surveyDao.getAll();
        assertEquals(2, surveys.size());
        assertEquals(Arrays.asList(secondId, thirdId), Arrays.asList(surveys.get(0).getId(), surveys.get(1).getId()));
    }
}
