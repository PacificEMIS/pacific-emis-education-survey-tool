package org.pacific_emis.surveys.accreditation_core.data.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

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
        version = 1,
        exportSchema = false)
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

}
