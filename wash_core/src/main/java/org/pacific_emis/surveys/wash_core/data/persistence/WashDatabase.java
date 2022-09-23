package org.pacific_emis.surveys.wash_core.data.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.pacific_emis.surveys.core.data.persistence.BaseConverters;
import org.pacific_emis.surveys.wash_core.BuildConfig;
import org.pacific_emis.surveys.wash_core.data.persistence.dao.AnswerDao;
import org.pacific_emis.surveys.wash_core.data.persistence.dao.GroupDao;
import org.pacific_emis.surveys.wash_core.data.persistence.dao.PhotoDao;
import org.pacific_emis.surveys.wash_core.data.persistence.dao.QuestionDao;
import org.pacific_emis.surveys.wash_core.data.persistence.dao.SubGroupDao;
import org.pacific_emis.surveys.wash_core.data.persistence.dao.SurveyDao;
import org.pacific_emis.surveys.wash_core.data.persistence.entity.RoomAnswer;
import org.pacific_emis.surveys.wash_core.data.persistence.entity.RoomGroup;
import org.pacific_emis.surveys.wash_core.data.persistence.entity.RoomPhoto;
import org.pacific_emis.surveys.wash_core.data.persistence.entity.RoomQuestion;
import org.pacific_emis.surveys.wash_core.data.persistence.entity.RoomSubGroup;
import org.pacific_emis.surveys.wash_core.data.persistence.entity.RoomWashSurvey;


@Database(
        entities = {
                RoomWashSurvey.class,
                RoomGroup.class,
                RoomSubGroup.class,
                RoomQuestion.class,
                RoomAnswer.class,
                RoomPhoto.class
        },
        version = BuildConfig.DATA_BASE_VERSION,
        exportSchema = true)
@TypeConverters({
        Converters.class,
        BaseConverters.class
})
public abstract class WashDatabase extends RoomDatabase {

    public abstract SurveyDao getSurveyDao();

    public abstract GroupDao getGroupDao();

    public abstract SubGroupDao getSubGroupDao();

    public abstract QuestionDao getQuestionDao();

    public abstract PhotoDao getPhotoDao();

    public abstract AnswerDao getAnswerDao();

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE RoomWashSurvey ADD upload_state TEXT"
            );
        }
    };

    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE RoomWashSurvey ADD tablet_id TEXT"
            );
        }
    };

    public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE RoomWashSurvey ADD drive_file_id TEXT"
            );
        }
    };

    public static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE RoomWashSurvey ADD principal_name TEXT"
            );
        }
    };
}
