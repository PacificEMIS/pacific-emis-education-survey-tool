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
import fm.doe.national.data.persistence.dao.SurveyDao;
import fm.doe.national.data.persistence.entity.Survey;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class SurveyTests {

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

        Survey survey = RoomTestData.getSurveyFor_putSurveyEntityTest();
        surveyDao.insert(survey);

        List<Survey> allSurveys = surveyDao.getAll();
        assertEquals(allSurveys.size(), 1);
        assertEquals(allSurveys.get(0).schoolName, survey.schoolName);
        assertEquals(allSurveys.get(0).uid, 1);

        survey.schoolName = "aa";
        surveyDao.insert(survey);

        allSurveys = surveyDao.getAll();
        assertEquals(allSurveys.size(), 2);
        assertEquals("aa", allSurveys.get(1).schoolName);
        assertEquals(allSurveys.get(1).uid, 2);
    }

    @Test
    public void deleteSurveysTest() {
        Survey survey = RoomTestData.getSurveyFor_putSurveyEntityTest();
        surveyDao.insert(survey);
        surveyDao.insert(survey); // twice

        Survey existingSurvey = surveyDao.getAll().get(0);
        surveyDao.delete(existingSurvey);
        assertEquals(1, surveyDao.getAll().size());

        surveyDao.insert(survey);
        surveyDao.deleteAll();

        assertTrue(surveyDao.getAll().isEmpty());
    }

    @Test
    public void updateSurveyTest() {
        surveyDao.deleteAll();
        surveyDao.insert(RoomTestData.getSurveyFor_updateSurveyTest());

        Survey existingSurvey = surveyDao.getAll().get(0);
        existingSurvey.schoolName = "11";
        existingSurvey.schoolId = "__";
        surveyDao.update(existingSurvey);

        Survey updatedSurvey = surveyDao.getAll().get(0);
        assertEquals("11", updatedSurvey.schoolName);
        assertEquals("__", updatedSurvey.schoolId);
    }

    @Test
    public void updateIsNotCreateTest() {
        surveyDao.deleteAll();
        surveyDao.update(RoomTestData.getSurveyFor_updateSurveyTest());
        assertTrue(surveyDao.getAll().isEmpty());
    }
}
