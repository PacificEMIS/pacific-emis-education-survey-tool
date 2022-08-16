package org.pacific_emis.surveys.core.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;

import org.pacific_emis.surveys.core.data.model.mutable.MutableSurvey;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;
import org.pacific_emis.surveys.core.preferences.entities.SurveyType;
import org.pacific_emis.surveys.core.preferences.entities.UploadState;

public interface Survey extends Progressable, IdentifiedObject {

    int getVersion();

    @NonNull
    SurveyType getSurveyType();

    @Nullable
    Date getCreateDate();

    @Nullable
    String getSurveyTag();

    @Nullable
    Date getCompleteDate();

    @Nullable
    SurveyState getState();

    @Nullable
    String getSchoolName();

    @Nullable
    String getSchoolId();

    @NonNull
    AppRegion getAppRegion();

    int getPhotosCount();

    @Nullable
    String getCreateUser();

    @Nullable
    String getLastEditedUser();

    @Nullable
    UploadState getUploadState();

    @Nullable
    String getTabletId();
    
    MutableSurvey toMutable();
}
