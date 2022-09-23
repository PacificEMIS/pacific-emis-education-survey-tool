package org.pacific_emis.surveys.core.data.persistence.model;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.core.data.model.abstract_implementations.SurveyLogImpl;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;
import org.pacific_emis.surveys.core.preferences.entities.LogAction;
import org.pacific_emis.surveys.core.preferences.entities.SurveyType;

@Entity
public class RoomSurveyLog extends SurveyLogImpl {

    @PrimaryKey(autoGenerate = true)
    public long id;

    SurveyType surveyType;
    String createUser;
    String schoolName;
    String schoolId;
    String surveyTag;
    LogAction logAction;
    AppRegion appRegion;

    public RoomSurveyLog(SurveyType surveyType, String createUser, String schoolName, String schoolId, String surveyTag, LogAction logAction, AppRegion appRegion) {
        this.surveyType = surveyType;
        this.createUser = createUser;
        this.schoolName = schoolName;
        this.schoolId = schoolId;
        this.surveyTag = surveyTag;
        this.logAction = logAction;
        this.appRegion = appRegion;
    }

    public RoomSurveyLog(Survey survey, LogAction logAction) {
        this.surveyType = survey.getSurveyType();
        this.createUser = survey.getCreateUser();
        this.schoolName = survey.getSchoolName();
        this.schoolId = survey.getSchoolId();
        this.surveyTag = survey.getSurveyTag();
        this.logAction = logAction;
        this.appRegion = survey.getAppRegion();
    }

    @NonNull
    @Override
    public SurveyType getSurveyType() {
        return surveyType;
    }

    @NonNull
    @Override
    public String getCreateUser() {
        return createUser;
    }

    @NonNull
    @Override
    public String getSchoolName() {
        return schoolName;
    }

    @NonNull
    @Override
    public String getSchoolId() {
        return schoolId;
    }

    @NonNull
    @Override
    public String getSurveyTag() {
        return surveyTag;
    }

    @Override
    public LogAction getLogAction() {
        return logAction;
    }

    @NonNull
    @Override
    public AppRegion getAppRegion() {
        return appRegion;
    }
}
