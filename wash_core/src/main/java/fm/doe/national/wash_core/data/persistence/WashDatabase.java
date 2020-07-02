package fm.doe.national.wash_core.data.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import fm.doe.national.core.data.persistence.BaseConverters;
import fm.doe.national.wash_core.data.persistence.dao.AnswerDao;
import fm.doe.national.wash_core.data.persistence.dao.GroupDao;
import fm.doe.national.wash_core.data.persistence.dao.PhotoDao;
import fm.doe.national.wash_core.data.persistence.dao.QuestionDao;
import fm.doe.national.wash_core.data.persistence.dao.SubGroupDao;
import fm.doe.national.wash_core.data.persistence.dao.SurveyDao;
import fm.doe.national.wash_core.data.persistence.entity.RoomAnswer;
import fm.doe.national.wash_core.data.persistence.entity.RoomGroup;
import fm.doe.national.wash_core.data.persistence.entity.RoomPhoto;
import fm.doe.national.wash_core.data.persistence.entity.RoomQuestion;
import fm.doe.national.wash_core.data.persistence.entity.RoomSubGroup;
import fm.doe.national.wash_core.data.persistence.entity.RoomWashSurvey;


@Database(
        entities = {
                RoomWashSurvey.class,
                RoomGroup.class,
                RoomSubGroup.class,
                RoomQuestion.class,
                RoomAnswer.class,
                RoomPhoto.class
        },
        version = 1,
        exportSchema = false)
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
}
