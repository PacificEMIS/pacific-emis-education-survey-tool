package fm.doe.national.data.data_source.models.db;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

public class OrmLiteSurveyResult {

    public interface Column {
        String ID = "id";
        String YEAR = "version";
        String SCHOOL = "school";
        String SURVEY = "surveyResult";
        String START_DATE = "startDate";
        String END_DATE = "endDate";
    }

    @DatabaseField(generatedId = true, columnName = Column.ID)
    protected long id;

    @DatabaseField(columnName = Column.YEAR)
    protected int year;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, columnName = Column.SCHOOL)
    protected OrmLiteSchool school;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = Column.SURVEY)
    protected OrmLiteBaseSurvey survey;

    @DatabaseField(columnName = Column.START_DATE)
    protected Date startDate;

    @DatabaseField(columnName = Column.END_DATE)
    protected Date endDate;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public OrmLiteSchool getSchool() {
        return school;
    }

    public void setSchool(OrmLiteSchool school) {
        this.school = school;
    }

    public OrmLiteBaseSurvey getSurvey() {
        return survey;
    }

    public void setSurvey(OrmLiteBaseSurvey survey) {
        this.survey = survey;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
