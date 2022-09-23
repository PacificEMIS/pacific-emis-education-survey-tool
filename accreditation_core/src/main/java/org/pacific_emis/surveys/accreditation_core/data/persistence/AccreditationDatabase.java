package org.pacific_emis.surveys.accreditation_core.data.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.pacific_emis.surveys.accreditation_core.BuildConfig;
import org.pacific_emis.surveys.accreditation_core.data.persistence.dao.AnswerDao;
import org.pacific_emis.surveys.accreditation_core.data.persistence.dao.CategoryDao;
import org.pacific_emis.surveys.accreditation_core.data.persistence.dao.CriteriaDao;
import org.pacific_emis.surveys.accreditation_core.data.persistence.dao.ObservationLogRecordDao;
import org.pacific_emis.surveys.accreditation_core.data.persistence.dao.PhotoDao;
import org.pacific_emis.surveys.accreditation_core.data.persistence.dao.StandardDao;
import org.pacific_emis.surveys.accreditation_core.data.persistence.dao.SubCriteriaDao;
import org.pacific_emis.surveys.accreditation_core.data.persistence.dao.SurveyDao;
import org.pacific_emis.surveys.accreditation_core.data.persistence.entity.RoomAccreditationSurvey;
import org.pacific_emis.surveys.accreditation_core.data.persistence.entity.RoomAnswer;
import org.pacific_emis.surveys.accreditation_core.data.persistence.entity.RoomCategory;
import org.pacific_emis.surveys.accreditation_core.data.persistence.entity.RoomCriteria;
import org.pacific_emis.surveys.accreditation_core.data.persistence.entity.RoomObservationLogRecord;
import org.pacific_emis.surveys.accreditation_core.data.persistence.entity.RoomPhoto;
import org.pacific_emis.surveys.accreditation_core.data.persistence.entity.RoomStandard;
import org.pacific_emis.surveys.accreditation_core.data.persistence.entity.RoomSubCriteria;
import org.pacific_emis.surveys.core.data.persistence.BaseConverters;

@Database(
        entities = {
                RoomAccreditationSurvey.class,
                RoomCategory.class,
                RoomStandard.class,
                RoomCriteria.class,
                RoomSubCriteria.class,
                RoomAnswer.class,
                RoomPhoto.class,
                RoomObservationLogRecord.class
        },
        version = BuildConfig.DATA_BASE_VERSION,
        exportSchema = true)
@TypeConverters({
        Converters.class,
        BaseConverters.class
})
public abstract class AccreditationDatabase extends RoomDatabase {

    public abstract SurveyDao getSurveyDao();

    public abstract CategoryDao getCategoryDao();

    public abstract StandardDao getStandardDao();

    public abstract CriteriaDao getCriteriaDao();

    public abstract SubCriteriaDao getSubcriteriaDao();

    public abstract PhotoDao getPhotoDao();

    public abstract AnswerDao getAnswerDao();

    public abstract ObservationLogRecordDao getObservationLogRecordDao();

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE RoomCategory ADD observation_info_teacher_id INTEGER"
            );
        }
    };

    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE RoomAccreditationSurvey ADD upload_state TEXT"
            );
        }
    };

    public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE RoomAccreditationSurvey ADD tablet_id TEXT"
            );
        }
    };

    public static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE RoomAccreditationSurvey ADD drive_file_id TEXT"
            );
        }
    };

    public static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE RoomAccreditationSurvey ADD principal_name TEXT"
            );
        }
    };
}
