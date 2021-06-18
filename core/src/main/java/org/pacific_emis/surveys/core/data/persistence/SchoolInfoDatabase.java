package org.pacific_emis.surveys.core.data.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

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
        version = 1,
        exportSchema = false)
@TypeConverters({
        BaseConverters.class
})
public abstract class SchoolInfoDatabase extends RoomDatabase {

    public abstract SchoolDao getSchoolDao();
    public abstract TeacherDao getTeacherDao();
    public abstract SubjectDao getSubjectDao();

}
