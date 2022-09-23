package org.pacific_emis.surveys.accreditation_core.database;

import static org.pacific_emis.surveys.accreditation_core.data.persistence.AccreditationDatabase.MIGRATION_5_6;

import androidx.room.testing.MigrationTestHelper;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.pacific_emis.surveys.accreditation_core.data.persistence.AccreditationDatabase;

import java.io.IOException;

@LargeTest
public class Migration_5_6_Test {
    private static final String TEST_DB = "accreditation.database";

    @Rule
    public MigrationTestHelper helper = new MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
            AccreditationDatabase.class.getCanonicalName(),
            new FrameworkSQLiteOpenHelperFactory());

    @Test
    public void migrateAccreditation_4_5() throws IOException {
        SupportSQLiteDatabase db = helper.createDatabase(TEST_DB, 5);
        db.close();

        db = helper.runMigrationsAndValidate(TEST_DB, 6, false, MIGRATION_5_6);
        db.close();
    }
}