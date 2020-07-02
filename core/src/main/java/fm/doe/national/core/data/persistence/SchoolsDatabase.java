package fm.doe.national.core.data.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import fm.doe.national.core.data.persistence.dao.SchoolDao;
import fm.doe.national.core.data.persistence.model.RoomSchool;

@Database(
        entities = {
                RoomSchool.class
        },
        version = 1,
        exportSchema = false)
@TypeConverters({
        BaseConverters.class
})
public abstract class SchoolsDatabase extends RoomDatabase {

    public abstract SchoolDao getSchoolDao();

}
