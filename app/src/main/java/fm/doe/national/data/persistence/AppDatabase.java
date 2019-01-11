package fm.doe.national.data.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import fm.doe.national.data.persistence.dao.SurveyDao;
import fm.doe.national.data.persistence.entity.Survey;

@Database(entities = { Survey.class }, version = 1, exportSchema = false)
@TypeConverters({ Converters.class })
public abstract class AppDatabase extends RoomDatabase {

    public abstract SurveyDao getSurveyDao();
}
