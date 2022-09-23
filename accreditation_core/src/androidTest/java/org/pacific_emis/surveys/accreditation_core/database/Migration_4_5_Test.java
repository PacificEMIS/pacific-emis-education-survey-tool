package org.pacific_emis.surveys.accreditation_core.database;

import static org.pacific_emis.surveys.accreditation_core.data.persistence.AccreditationDatabase.MIGRATION_4_5;

import androidx.room.testing.MigrationTestHelper;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.pacific_emis.surveys.accreditation_core.data.persistence.AccreditationDatabase;

import java.io.IOException;

public class Migration_4_5_Test {
    private static final String TEST_DB = "accreditation.database";

    @Rule
    public MigrationTestHelper helper = new MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
            AccreditationDatabase.class.getCanonicalName(),
            new FrameworkSQLiteOpenHelperFactory());

    @Test
    public void migrateAccreditation_4_5() throws IOException {
        SupportSQLiteDatabase db = helper.createDatabase(TEST_DB, 4);
        db.close();

        db = helper.runMigrationsAndValidate(TEST_DB, 5, false, MIGRATION_4_5);
        db.close();
    }
}