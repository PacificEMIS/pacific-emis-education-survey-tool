package fm.doe.national.data.persistence.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import fm.doe.national.data.persistence.new_model.Category;
import fm.doe.national.data.persistence.new_model.School;
import fm.doe.national.data.persistence.new_model.Survey;
import fm.doe.national.data.persistence.new_model.SurveyType;

@Entity
public class PersistenceSurvey implements Survey {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    @ColumnInfo(name = "start_date")
    public Date startDate;

    @ColumnInfo(name = "version")
    public int version;

    @ColumnInfo(name = "type")
    public SurveyType type;

    @ColumnInfo(name = "school_name")
    public String schoolName;

    @ColumnInfo(name = "school_id")
    public String schoolId;

    @Ignore
    private List<Category> categories = new ArrayList<>();

    public PersistenceSurvey(int version,
                             SurveyType type,
                             @Nullable String schoolName,
                             @Nullable String schoolId,
                             Date startDate) {
        this.version = version;
        this.type = type;
        this.schoolName = schoolName;
        this.schoolId = schoolId;
        this.startDate = startDate;
    }

    public PersistenceSurvey(@NonNull Survey otherSurvey) {
        uid = otherSurvey.getId();
        startDate = otherSurvey.getDate();
        version = otherSurvey.getVersion();
        type = otherSurvey.getSurveyType();
        categories = otherSurvey.getCategories();
    }

    @Override
    public int getVersion() {
        return version;
    }

    @NonNull
    @Override
    public SurveyType getSurveyType() {
        return type;
    }

    @NonNull
    @Override
    public Date getDate() {
        return startDate;
    }

    @Nullable
    @Override
    public String getSchoolName() {
        return schoolName;
    }

    @Nullable
    @Override
    public String getSchoolId() {
        return schoolId;
    }

    @NonNull
    @Override
    public List<Category> getCategories() {
        return categories;
    }

    @Override
    public long getId() {
        return uid;
    }

}
