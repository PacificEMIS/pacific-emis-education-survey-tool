package fm.doe.national.database_test;

import org.junit.After;
import org.junit.Before;

import fm.doe.national.core.data.persistence.AppDatabase;
import fm.doe.national.database_test.util.RoomTestData;

public class BaseDatabaseTest {

    protected AppDatabase database;

    @Before
    public void before() {
        database = RoomTestData.createAppDatabase();
    }

    @After
    public void after() {
        database.close();
    }
}
