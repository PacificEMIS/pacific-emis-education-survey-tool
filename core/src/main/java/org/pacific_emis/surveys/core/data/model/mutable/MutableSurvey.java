package org.pacific_emis.surveys.core.data.model.mutable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.core.data.model.SurveyState;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;
import org.pacific_emis.surveys.core.preferences.entities.SurveyType;
import org.pacific_emis.surveys.core.preferences.entities.UploadState;
import org.pacific_emis.surveys.core.utils.ObjectUtils;

import java.util.Date;

public abstract class MutableSurvey extends BaseMutableEntity implements Survey {

    private int version;
    private SurveyType surveyType;
    private AppRegion appRegion;
    private Date createDate;
    private String surveyTag;
    private Date completeDate;
    private String schoolName;
    private String schoolId;
    private MutableProgress progress = MutableProgress.createEmptyProgress();
    private SurveyState state;
    private UploadState uploadState;
    private String tabletId;

    @Nullable
    private String createUser;

    @Nullable
    private String lastEditedUser;
    @Nullable
    private String driveFileId;


    public MutableSurvey() {
        // nothing
    }

    public MutableSurvey(@NonNull Survey other) {
        super(other.getId());

        this.version = other.getVersion();
        this.surveyType = other.getSurveyType();
        this.createDate = other.getCreateDate();
        this.surveyTag = other.getSurveyTag();
        this.completeDate = other.getCompleteDate();
        this.schoolName = other.getSchoolName();
        this.schoolId = other.getSchoolId();
        this.appRegion = other.getAppRegion();
        this.state = ObjectUtils.orElse(other.getState(), SurveyState.NOT_COMPLETED);
        this.createUser = other.getCreateUser();
        this.lastEditedUser = other.getLastEditedUser();
        this.uploadState = ObjectUtils.orElse(other.getUploadState(), UploadState.NOT_UPLOAD);
        this.tabletId = other.getTabletId();
        this.driveFileId = other.getDriveFileId();
    }

    @Override
    public int getVersion() {
        return version;
    }

    @NonNull
    @Override
    public SurveyType getSurveyType() {
        return surveyType;
    }

    @Nullable
    @Override
    public Date getCreateDate() {
        return createDate;
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

    public void setVersion(int version) {
        this.version = version;
    }

    public void setSurveyType(SurveyType surveyType) {
        this.surveyType = surveyType;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    @NonNull
    public MutableProgress getProgress() {
        return progress;
    }

    public abstract MutableProgress calculateProgress();

    public void setProgress(MutableProgress progress) {
        this.progress = progress;
    }

    @NonNull
    @Override
    public AppRegion getAppRegion() {
        return appRegion;
    }

    public void setAppRegion(AppRegion appRegion) {
        this.appRegion = appRegion;
    }

    @Override
    public String getSurveyTag() {
        return surveyTag;
    }

    @Nullable
    @Override
    public Date getCompleteDate() {
        return completeDate;
    }

    public void setSurveyTag(String surveyTag) {
        this.surveyTag = surveyTag;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    @Override
    public SurveyState getState() {
        return state;
    }

    public void setState(SurveyState state) {
        this.state = state;
    }

    @Nullable
    @Override
    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(@Nullable String createUser) {
        this.createUser = createUser;
    }

    @Nullable
    @Override
    public String getLastEditedUser() {
        return lastEditedUser;
    }

    public void setLastEditedUser(@Nullable String lastEditedUser) {
        this.lastEditedUser = lastEditedUser;
    }

    @Nullable
    @Override
    public UploadState getUploadState() {
        return uploadState;
    }

    public void setUploadState(@Nullable UploadState uploadState) {
        this.uploadState = uploadState;
    }

    @Nullable
    @Override
    public String getTabletId() {
        return tabletId;
    }

    public void setTabletId(String tabletId) {
        this.tabletId = tabletId;
    }

    @Nullable
    @Override
    public String getDriveFileId() {
        return driveFileId;
    }

    public void setDriveFileId(String driveFileId) {
        this.driveFileId = driveFileId;
    }

    public abstract MutableSurvey toMutable();

}