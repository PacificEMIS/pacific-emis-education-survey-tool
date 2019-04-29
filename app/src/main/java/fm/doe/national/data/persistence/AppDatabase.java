package fm.doe.national.data.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import fm.doe.national.data.persistence.dao.CategoryDao;
import fm.doe.national.data.persistence.dao.CriteriaDao;
import fm.doe.national.data.persistence.dao.PhotoDao;
import fm.doe.national.data.persistence.dao.StandardDao;
import fm.doe.national.data.persistence.dao.SubCriteriaDao;
import fm.doe.national.data.persistence.dao.SurveyDao;
import fm.doe.national.data.persistence.entity.PersistenceAnswer;
import fm.doe.national.data.persistence.entity.PersistenceCategory;
import fm.doe.national.data.persistence.entity.PersistenceCriteria;
import fm.doe.national.data.persistence.entity.PersistencePhoto;
import fm.doe.national.data.persistence.entity.PersistenceStandard;
import fm.doe.national.data.persistence.entity.PersistenceSubCriteria;
import fm.doe.national.data.persistence.entity.PersistenceSurvey;

@Database(
        entities = {
                PersistenceSurvey.class,
                PersistenceCategory.class,
                PersistenceStandard.class,
                PersistenceCriteria.class,
                PersistenceSubCriteria.class,
                PersistenceAnswer.class,
                PersistencePhoto.class
        },
        version = 1,
        exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract SurveyDao getSurveyDao();

    public abstract CategoryDao getCategoryDao();

    public abstract StandardDao getStandardDao();

    public abstract CriteriaDao getCriteriaDao();

    public abstract SubCriteriaDao getSubcriteriaDao();

    public abstract PhotoDao getPhotoDao();
}
