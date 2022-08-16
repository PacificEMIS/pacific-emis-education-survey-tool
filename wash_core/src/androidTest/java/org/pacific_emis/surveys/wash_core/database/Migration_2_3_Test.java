package org.pacific_emis.surveys.wash_core.database;

import static org.pacific_emis.surveys.wash_core.data.persistence.WashDatabase.MIGRATION_1_2;
import static org.pacific_emis.surveys.wash_core.data.persistence.WashDatabase.MIGRATION_2_3;

import androidx.room.testing.MigrationTestHelper;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.pacific_emis.surveys.wash_core.data.persistence.WashDatabase;

import java.io.IOException;

public class Migration_2_3_Test {
    private static final String TEST_DB = "wash.database";

    @Rule
    public MigrationTestHelper helper = new MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
            WashDatabase.class.getCanonicalName(),
            new FrameworkSQLiteOpenHelperFactory());

    @Test
    public void migrateWash_2_3() throws IOException {
        // Create earliest version of the database.
        SupportSQLiteDatabase db = helper.createDatabase(TEST_DB, 2);
        db.close();

        // Open latest version of the database. Room will validate the schema
        // once all migrations execute.
        db = helper.runMigrationsAndValidate(TEST_DB, 3, false, MIGRATION_2_3);
        db.close();
    }
}
