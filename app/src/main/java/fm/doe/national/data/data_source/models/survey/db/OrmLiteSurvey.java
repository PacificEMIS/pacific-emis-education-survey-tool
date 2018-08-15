package fm.doe.national.data.data_source.models.survey.db;

import android.support.annotation.Nullable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

import fm.doe.national.data.data_source.models.survey.Answer;
import fm.doe.national.data.data_source.models.survey.School;
import fm.doe.national.data.data_source.models.survey.Survey;


@DatabaseTable
public class OrmLiteSurvey implements Survey {

    public interface Column {
        String ID = "id";
        String YEAR = "year";
        String SCHOOL = "school";
        String ANSWERS = "answers";
    }

    @DatabaseField(generatedId = true, columnName = Column.ID)
    protected long id;
    @DatabaseField(columnName = Column.YEAR)
    protected int year;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = Column.SCHOOL)
    protected OrmLiteSchool school;
    @Nullable
    @ForeignCollectionField(eager = true, columnName = Column.ANSWERS)
    protected Collection<OrmLiteAnswer> answers;

    public OrmLiteSurvey() {
    }

    public OrmLiteSurvey(int year, OrmLiteSchool school) {
        this.year = year;
        this.school = school;
    }

    public long getId() {
        return id;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public School getSchool() {
        return school;
    }

    @Nullable
    @Override
    public Collection<? extends Answer> getAnswers() {
        return answers;
    }
}
