package fm.doe.national.data.data_source.db.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import fm.doe.national.models.survey.Survey;


@DatabaseTable
public class OrmLiteSurvey {

    public interface Column {
        String ID = "id";
        String YEAR = "year";
        String SCHOOL = "school";
    }

    @DatabaseField(generatedId = true, columnName = Column.ID)
    protected long id;
    @DatabaseField(columnName = Column.YEAR)
    protected int year;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = Column.SCHOOL)
    protected OrmLiteSchool school;

    public OrmLiteSurvey() {
    }

    public OrmLiteSurvey(int year, OrmLiteSchool school) {
        this.year = year;
        this.school = school;
    }

    public long getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

}
