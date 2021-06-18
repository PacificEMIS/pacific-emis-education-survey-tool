package org.pacific_emis.surveys.core.data.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.pacific_emis.surveys.core.data.persistence.dao.SchoolDao;
import org.pacific_emis.surveys.core.data.persistence.dao.SubjectDao;
import org.pacific_emis.surveys.core.data.persistence.dao.TeacherDao;
import org.pacific_emis.surveys.core.data.persistence.model.RoomSchool;
import org.pacific_emis.surveys.core.data.persistence.model.RoomSubject;
import org.pacific_emis.surveys.core.data.persistence.model.RoomTeacher;

@Database(
        entities = {
                RoomSchool.class,
                RoomTeacher.class,
                RoomSubject.class
        },
        version = 3,
        exportSchema = false)
@TypeConverters({
        BaseConverters.class
})
public abstract class SchoolInfoDatabase extends RoomDatabase {

    public abstract SchoolDao getSchoolDao();
    public abstract TeacherDao getTeacherDao();
    public abstract SubjectDao getSubjectDao();

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "CREATE TABLE IF NOT EXISTS RoomTeacher (id INTEGER NOT NULL, name TEXT, appRegion TEXT, PRIMARY KEY(id));"
            );
            database.execSQL("CREATE INDEX IF NOT EXISTS index_RoomTeacher_id ON RoomTeacher (id);");
        }
    };

    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "CREATE TABLE IF NOT EXISTS RoomSubject (id TEXT NOT NULL, name TEXT, appRegion TEXT, PRIMARY KEY(id));"
            );
            database.execSQL("CREATE INDEX IF NOT EXISTS index_RoomSubject_id ON RoomSubject (id);");
        }
    };
}

