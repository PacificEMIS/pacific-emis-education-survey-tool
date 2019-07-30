package fm.doe.national.accreditation_core.data.persistence.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;

import fm.doe.national.accreditation_core.data.model.AccreditationSurvey;
import fm.doe.national.accreditation_core.data.model.Category;
import fm.doe.national.core.data.model.Progress;
import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.core.preferences.entities.SurveyType;

@Entity
public class RoomAccreditationSurvey implements AccreditationSurvey {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    @ColumnInfo(name = "start_date")
    public Date createDate;

    @ColumnInfo(name = "survey_tag")
    public String surveyTag;

    @ColumnInfo(name = "complete_date")
    public Date completeDate;

    @ColumnInfo(name = "version")
    public int version;

    @ColumnInfo(name = "type")
    public SurveyType type;

    @ColumnInfo(name = "school_name")
    public String schoolName;

    @ColumnInfo(name = "school_id")
    public String schoolId;

    @ColumnInfo(name = "region")
    public AppRegion appRegion;

    public RoomAccreditationSurvey(int version,
                                   SurveyType type,
                                   AppRegion appRegion,
                                   @Nullable String schoolName,
                                   @Nullable String schoolId,
                                   @Nullable Date createDate,
                                   @Nullable String surveyTag) {
        this.version = version;
        this.type = type;
        this.schoolName = schoolName;
        this.schoolId = schoolId;
        this.createDate = createDate;
        this.surveyTag = surveyTag;
        this.appRegion = appRegion;
    }

    public RoomAccreditationSurvey(@NonNull AccreditationSurvey other) {
        this.uid = other.getId();
        this.version = other.getVersion();
        this.type = other.getSurveyType();
        this.createDate = other.getCreateDate();
        this.surveyTag = other.getSurveyTag();
        this.completeDate = other.getCompleteDate();
        this.schoolName = other.getSchoolName();
        this.schoolId = other.getSchoolId();
        this.appRegion = other.getAppRegion();
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
    public Date getCreateDate() {
        return createDate;
    }

    @Nullable
    @Override
    public String getSurveyTag() {
        return surveyTag;
    }

    @Nullable
    @Override
    public Date getCompleteDate() {
        return completeDate;
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

    @NonNull
    @Override
    public AppRegion getAppRegion() {
        return appRegion;
    }
}
