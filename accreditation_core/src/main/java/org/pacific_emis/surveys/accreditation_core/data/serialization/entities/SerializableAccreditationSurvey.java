package org.pacific_emis.surveys.accreditation_core.data.serialization.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.pacific_emis.surveys.core.data.serialization.converters.UploadStateConverter;
import org.pacific_emis.surveys.core.preferences.entities.UploadState;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.pacific_emis.surveys.accreditation_core.data.model.AccreditationSurvey;
import org.pacific_emis.surveys.accreditation_core.data.model.Category;
import org.pacific_emis.surveys.core.data.model.Progress;
import org.pacific_emis.surveys.core.data.model.SurveyState;
import org.pacific_emis.surveys.core.data.serialization.converters.AppRegionConverter;
import org.pacific_emis.surveys.core.data.serialization.converters.SurveyStateConverter;
import org.pacific_emis.surveys.core.data.serialization.converters.SurveyTypeConverter;
import org.pacific_emis.surveys.core.data.serialization.converters.UtcDateConverter;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;
import org.pacific_emis.surveys.core.preferences.entities.SurveyType;
import org.pacific_emis.surveys.core.utils.ObjectUtils;

@Root(name = "survey")
public class SerializableAccreditationSurvey implements AccreditationSurvey {

    @Element
    @Convert(SurveyTypeConverter.class)
    SurveyType type;

    @Element(name = "country")
    @Convert(AppRegionConverter.class)
    AppRegion region;

    @Element
    int version;

    @Nullable
    @Element(required = false)
    @Convert(UtcDateConverter.class)
    Date createDate;

    @Nullable
    @Element(required = false)
    String surveyTag;

    @Nullable
    @Element(required = false)
    @Convert(UtcDateConverter.class)
    Date completeDate;

    @Nullable
    @Element(required = false, name = "schoolNo")
    String schoolId;

    @Nullable
    @Element(required = false)
    String schoolName;

    @Nullable
    @Element(required = false, name = "surveyCompleted")
    @Convert(SurveyStateConverter.class)
    SurveyState state;

    @ElementList(inline = true)
    List<SerializableCategory> categories;

    @Nullable
    @Element(required = false)
    String createUser;

    @Nullable
    @Element(required = false)
    String lastEditedUser;

    @Nullable
    @Element(required = false, name = "surveyUpload")
    @Convert(UploadStateConverter.class)
    UploadState uploadState;

    @Nullable
    @Element(required = false)
    String tabletId;

    @Nullable
    @Element(required = false)
    String driveFileId;

    public SerializableAccreditationSurvey() {
    }

    public SerializableAccreditationSurvey(@NonNull AccreditationSurvey other) {
        this.version = other.getVersion();
        this.type = other.getSurveyType();
        this.region = other.getAppRegion();
        this.createDate = other.getCreateDate();
        this.surveyTag = other.getSurveyTag();
        this.completeDate = other.getCompleteDate();
        this.schoolName = other.getSchoolName();
        this.schoolId = other.getSchoolId();
        this.state = ObjectUtils.orElse(other.getState(), SurveyState.NOT_COMPLETED);
        this.createUser = other.getCreateUser();
        this.lastEditedUser = other.getLastEditedUser();
        this.uploadState = ObjectUtils.orElse(other.getUploadState(), UploadState.NOT_UPLOAD);
        this.tabletId = other.getTabletId();
        this.driveFileId = other.getDriveFileId();

        if (other.getCategories() != null) {
            this.categories = other.getCategories().stream().map(SerializableCategory::from).collect(Collectors.toList());
        }
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
        return categories;
    }

    @Override
    public long getId() {
        return 0;
    }

    @NonNull
    @Override
    public Progress getProgress() {
        throw new IllegalStateException();
    }

    @NonNull
    @Override
    public AppRegion getAppRegion() {
        return region;
    }

    @Override
    public SurveyState getState() {
        return state;
    }

    @Nullable
    @Override
    public String getCreateUser() {
        return createUser;
    }

    @Nullable
    @Override
    public String getLastEditedUser() {
        return lastEditedUser;
    }

    @Nullable
    @Override
    public UploadState getUploadState() {
        return uploadState;
    }

    @Nullable
    @Override
    public String getTabletId() {
        return tabletId;
    }

    @Nullable
    @Override
    public String getDriveFileId() {
        return driveFileId;
    }
}
