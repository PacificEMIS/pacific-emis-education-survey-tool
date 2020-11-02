package org.pacific_emis.surveys.database_test;

import org.junit.After;
import org.junit.Before;

import org.pacific_emis.surveys.core.data.persistence.AppDatabase;
import org.pacific_emis.surveys.database_test.util.RoomTestData;

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
