package org.pacific_emis.surveys.accreditation_core.database;

import androidx.room.testing.MigrationTestHelper;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.pacific_emis.surveys.accreditation_core.data.persistence.AccreditationDatabase;

import java.io.IOException;

import static org.pacific_emis.surveys.accreditation_core.data.persistence.AccreditationDatabase.MIGRATION_2_3;

@LargeTest
public class Migration_2_3_Test {
    private static final String TEST_DB = "accreditation.database";

    @Rule
    public MigrationTestHelper helper = new MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
            AccreditationDatabase.class.getCanonicalName(),
            new FrameworkSQLiteOpenHelperFactory());

    @Test
    public void migrateAccreditation_2_3() throws IOException {
        // Create earliest version of the database.
        SupportSQLiteDatabase db = helper.createDatabase(TEST_DB, 2);
        db.close();

        // Open latest version of the database. Room will validate the schema
        // once all migrations execute.
        db = helper.runMigrationsAndValidate(TEST_DB, 3, false, MIGRATION_2_3);
        db.close();
    }
}
