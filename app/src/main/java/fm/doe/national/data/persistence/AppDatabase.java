package fm.doe.national.data.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import fm.doe.national.data.persistence.dao.CategoryDao;
import fm.doe.national.data.persistence.dao.SurveyDao;
import fm.doe.national.data.persistence.entity.Category;
import fm.doe.national.data.persistence.entity.Survey;

@Database(entities = { Survey.class, Category.class}, version = 1, exportSchema = false)
@TypeConverters({ Converters.class })
public abstract class AppDatabase extends RoomDatabase {

    public abstract SurveyDao getSurveyDao();
    public abstract CategoryDao getCategoryDao();
}
