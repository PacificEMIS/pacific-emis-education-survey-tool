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
public class CategoryTests {

    private SurveyDao surveyDao;
    private AppDatabase database;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        surveyDao = database.getSurveyDao();
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Test
    public void putSurveyEntityTest() {
//        Survey survey = RoomTestData.getSurveyFor_putSurveyEntityTest();
//
//        surveyDao.i(survey);
//
//        List<Survey> allSurveys = surveyDao.getAllSurveys();
//
//        assertEquals(allSurveys.size(), 1);
//        assertEquals(allSurveys.get(0).schoolName, survey.schoolName);
//
//        survey.schoolName = "aa";
//        surveyDao.putSurvey(survey);
//
//        allSurveys = surveyDao.getAllSurveys();
//
//        assertEquals(allSurveys.size(), 2);
//        assertEquals(allSurveys.get(1).schoolName, "aa");
    }
}