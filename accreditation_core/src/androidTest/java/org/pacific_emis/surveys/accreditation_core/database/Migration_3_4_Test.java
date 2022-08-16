package org.pacific_emis.surveys.accreditation_core.database;

import static org.pacific_emis.surveys.accreditation_core.data.persistence.AccreditationDatabase.MIGRATION_2_3;
import static org.pacific_emis.surveys.accreditation_core.data.persistence.AccreditationDatabase.MIGRATION_3_4;

import androidx.room.testing.MigrationTestHelper;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.pacific_emis.surveys.accreditation_core.data.persistence.AccreditationDatabase;

import java.io.IOException;

public class Migration_3_4_Test {
    private static final String TEST_DB = "accreditation.database";

    @Rule
    public MigrationTestHelper helper = new MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
            AccreditationDatabase.class.getCanonicalName(),
            new FrameworkSQLiteOpenHelperFactory());

    @Test
    public void migrateAccreditation_3_4() throws IOException {
        // Create earliest version of the database.
        SupportSQLiteDatabase db = helper.createDatabase(TEST_DB, 3);
        db.close();

        // Open latest version of the database. Room will validate the schema
        // once all migrations execute.
        db = helper.runMigrationsAndValidate(TEST_DB, 4, false, MIGRATION_3_4);
        db.close();
    }
}
