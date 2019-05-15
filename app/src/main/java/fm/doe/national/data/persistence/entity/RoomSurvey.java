package fm.doe.national.data.persistence.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;

import fm.doe.national.data.model.Category;
import fm.doe.national.data.model.Progress;
import fm.doe.national.data.model.Survey;
import fm.doe.national.data.model.SurveyType;

@Entity
public class RoomSurvey implements Survey {

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

    public RoomSurvey(int version,
                      SurveyType type,
                      @Nullable String schoolName,
                      @Nullable String schoolId,
                      @Nullable Date startDate) {
        this.version = version;
        this.type = type;
        this.schoolName = schoolName;
        this.schoolId = schoolId;
        this.startDate = startDate;
    }

    public RoomSurvey(@NonNull Survey other) {
        this.uid = other.getId();
        this.version = other.getVersion();
        this.type = other.getSurveyType();
        this.startDate = other.getDate();
        this.schoolName = other.getSchoolName();
        this.schoolId = other.getSchoolId();
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

    @Nullable
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

    @Nullable
    @Override
    public List<? extends Category> getCategories() {
        return null;
    }

    @Override
    public long getId() {
        return uid;
    }

    @NonNull
    @Override
    public Progress getProgress() {
        throw new IllegalStateException();
    }
}
