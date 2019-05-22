package fm.doe.national.database_test;


import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import fm.doe.national.core.data.persistence.dao.SchoolDao;
import fm.doe.national.core.data.persistence.entity.RoomSchool;
import fm.doe.national.database_test.util.RoomTestData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class RoomSchoolTests extends BaseDatabaseTest {

    private SchoolDao schoolDao;

    @Before
    @Override
    public void before() {
        super.before();
        schoolDao = database.getSchoolDao();
    }

    @Test
    public void putEntityTest() {
        schoolDao.deleteAll();

        RoomSchool school =  RoomTestData.getTestSchool();
        schoolDao.insert(school);

        List<RoomSchool> allSchools = schoolDao.getAll();
        assertEquals(1, allSchools.size());
        assertEquals("Test School 1", allSchools.get(0).getName());
        assertEquals("SCH001", allSchools.get(0).getId());

        school.name = "Test School 2";
        school.id = "SCH002";

        schoolDao.insert(school);

        allSchools = schoolDao.getAll();
        assertEquals(allSchools.size(), 2);
        assertNotEquals(allSchools.get(0).getId(), allSchools.get(1).getId());
        assertEquals("Test School 1", allSchools.get(0).getName());
        assertEquals("SCH001", allSchools.get(0).getId());
        assertEquals("Test School 2", allSchools.get(1).getName());
        assertEquals("SCH002", allSchools.get(1).getId());
    }

    @Test
    public void deleteSurveysTest() {
        RoomSchool school =  RoomTestData.getTestSchool();
        schoolDao.insert(school);
        school.id = "id2";
        schoolDao.insert(school);

        RoomSchool existingSchool = schoolDao.getAll().get(0);
        schoolDao.delete(existingSchool);
        assertEquals(1, schoolDao.getAll().size());

        school.id = "id3";
        schoolDao.insert(school);
        schoolDao.deleteAll();

        assertTrue(schoolDao.getAll().isEmpty());
    }

    @Test
    public void updateSurveyTest() {
        schoolDao.deleteAll();
        schoolDao.insert(RoomTestData.getTestSchool());

        RoomSchool existingSchool = schoolDao.getAll().get(0);
        assertEquals("Test School 1", existingSchool.getName());

        RoomSchool updatedSchool = RoomTestData.getTestSchool();
        updatedSchool.id = existingSchool.id;
        updatedSchool.name = "Test School 2";
        schoolDao.update(updatedSchool);

        updatedSchool = schoolDao.getAll().get(0);
        assertEquals("Test School 2", updatedSchool.getName());
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

        RoomSchool school = schoolDao.getAll().get(0);
        RoomSchool schoolById = schoolDao.getById(school.getId());

        assertEquals(school.getName(), schoolById.getName());

        assertNull(schoolDao.getById("t6bgwvefgtwy"));
    }
}
