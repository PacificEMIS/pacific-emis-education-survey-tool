package fm.doe.national.database_test;


import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import fm.doe.national.data.persistence.AppDatabase;
import fm.doe.national.data.persistence.dao.SchoolDao;
import fm.doe.national.data.persistence.entity.PersistenceSchool;
import fm.doe.national.data.persistence.entity.PersistenceSurvey;
import fm.doe.national.database_test.util.RoomTestData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PersistenceSchoolTests {

    private SchoolDao schoolDao;
    private AppDatabase database;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        schoolDao = database.getSchoolDao();
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Test
    public void putEntityTest() {
        schoolDao.deleteAll();

        PersistenceSchool school =  RoomTestData.getTestSchool();
        schoolDao.insert(school);

        List<PersistenceSchool> allSchools = schoolDao.getAll();
        assertEquals(1, allSchools.size());
        assertEquals(1, allSchools.get(0).getId());
        assertEquals("Test School 1", allSchools.get(0).getName());
        assertEquals("SCH001", allSchools.get(0).getSuffix());

        school.name = "Test School 2";
        school.suffix = "SCH002";

        schoolDao.insert(school);

        allSchools = schoolDao.getAll();
        assertEquals(allSchools.size(), 2);
        assertNotEquals(allSchools.get(0).getId(), allSchools.get(1).getId());
        assertEquals(allSchools.get(1).getId(), 2);
        assertEquals("Test School 1", allSchools.get(0).getName());
        assertEquals("SCH001", allSchools.get(0).getSuffix());
        assertEquals("Test School 2", allSchools.get(1).getName());
        assertEquals("SCH002", allSchools.get(1).getSuffix());
    }

    @Test
    public void deleteSurveysTest() {
        PersistenceSchool school =  RoomTestData.getTestSchool();
        schoolDao.insert(school);
        schoolDao.insert(school);

        PersistenceSchool existingSchool = schoolDao.getAll().get(0);
        schoolDao.delete(existingSchool);
        assertEquals(1, schoolDao.getAll().size());

        schoolDao.insert(school);
        schoolDao.deleteAll();

        assertTrue(schoolDao.getAll().isEmpty());
    }

    @Test
    public void updateSurveyTest() {
        schoolDao.deleteAll();
        schoolDao.insert(RoomTestData.getTestSchool());

        PersistenceSchool existingSchool = schoolDao.getAll().get(0);
        assertEquals("Test School 1", existingSchool.getName());

        PersistenceSchool updatedSchool = RoomTestData.getTestSchool();
        updatedSchool.uid = existingSchool.uid;
        updatedSchool.name = "Test School 2";
        updatedSchool.suffix = "SCH002";
        schoolDao.update(updatedSchool);

        updatedSchool = schoolDao.getAll().get(0);
        assertEquals("Test School 2", updatedSchool.getName());
        assertEquals("SCH002", updatedSchool.getSuffix());
    }

    @Test
    public void updateIsNotCreateTest() {
        schoolDao.deleteAll();
        schoolDao.update(RoomTestData.getTestSchool());
        assertTrue(schoolDao.getAll().isEmpty());
    }

    @Test
    public void getByIdTest() {
        schoolDao.deleteAll();
        schoolDao.insert(RoomTestData.getTestSchool());

        PersistenceSchool school = schoolDao.getAll().get(0);
        PersistenceSchool schoolById = schoolDao.getById(school.getId());

        assertEquals(school.getName(), schoolById.getName());

        assertNull(schoolDao.getById(78235235));
    }
}
