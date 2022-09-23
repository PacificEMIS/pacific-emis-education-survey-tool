package org.pacific_emis.surveys.wash_core.database;

import static org.pacific_emis.surveys.wash_core.data.persistence.WashDatabase.MIGRATION_4_5;

import androidx.room.testing.MigrationTestHelper;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.pacific_emis.surveys.wash_core.data.persistence.WashDatabase;

import java.io.IOException;

@LargeTest
public class Migration_4_5_Test {
    private static final String TEST_DB = "wash.database";

    @Rule
    public MigrationTestHelper helper = new MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
            WashDatabase.class.getCanonicalName(),
            new FrameworkSQLiteOpenHelperFactory());

    @Test
    public void migrateWash_3_4() throws IOException {
        SupportSQLiteDatabase db = helper.createDatabase(TEST_DB, 4);
        db.close();

        db = helper.runMigrationsAndValidate(TEST_DB, 5, false, MIGRATION_4_5);
        db.close();
    }
}