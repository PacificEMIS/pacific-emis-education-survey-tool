package org.pacific_emis.surveys.database_test;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import org.pacific_emis.surveys.core.data.persistence.dao.SurveyDao;
import org.pacific_emis.surveys.core.data.persistence.entity.RoomSurvey;
import org.pacific_emis.surveys.database_test.util.RoomTestData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class RoomSurveyTests extends BaseDatabaseTest {

    private SurveyDao surveyDao;

    @Before
    @Override
    public void before() {
        super.before();
        surveyDao = database.getSurveyDao();
    }

    @Test
    public void putSurveyEntityTest() {
        surveyDao.deleteAll();

        RoomSurvey survey = RoomTestData.getSurveyFor_putSurveyEntityTest();
        surveyDao.insert(survey);

        List<RoomSurvey> allSurveys = surveyDao.getAll();
        assertEquals(allSurveys.size(), 1);
        assertEquals(allSurveys.get(0).getId(), 1);
        assertEquals(allSurveys.get(0).getVersion(), 1);
        assertEquals(allSurveys.get(0).getSurveyType(), SurveyType.SCHOOL_ACCREDITATION);

        survey = new RoomSurvey(RoomTestData.getSurveyFor_updateSurveyTest());
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
        RoomSurvey survey = new RoomSurvey(RoomTestData.getSurveyFor_putSurveyEntityTest());
        surveyDao.insert(survey);
        surveyDao.insert(survey); // twice

        RoomSurvey existingSurvey = surveyDao.getAll().get(0);
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

        RoomSurvey existingSurvey = surveyDao.getAll().get(0);
        assertEquals(SurveyType.SCHOOL_ACCREDITATION, existingSurvey.getSurveyType());

        RoomSurvey updatedSurvey = RoomTestData.getSurveyFor_updateSurveyTest();
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

        RoomSurvey survey = surveyDao.getAll().get(0);
        RoomSurvey surveyById = surveyDao.getById(survey.getId());

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

        List<RoomSurvey> surveys = surveyDao.getAll();
        assertEquals(2, surveys.size());
        assertEquals(Arrays.asList(secondId, thirdId), Arrays.asList(surveys.get(0).getId(), surveys.get(1).getId()));
    }
}
