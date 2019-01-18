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
import fm.doe.national.data.persistence.entity.Category;
import fm.doe.national.data.persistence.entity.Criteria;
import fm.doe.national.data.persistence.entity.PhotoEntity;
import fm.doe.national.data.persistence.entity.Standard;
import fm.doe.national.data.persistence.entity.SubCriteria;
import fm.doe.national.data.persistence.entity.Survey;

@Database(
        entities = {
                Survey.class,
                Category.class,
                Standard.class,
                Criteria.class,
                SubCriteria.class,
                PhotoEntity.class
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
