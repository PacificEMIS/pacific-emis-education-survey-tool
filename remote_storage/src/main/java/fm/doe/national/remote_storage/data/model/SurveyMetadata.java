package fm.doe.national.remote_storage.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.api.services.drive.model.File;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.data.model.SurveyState;
import fm.doe.national.core.preferences.entities.SurveyType;
import fm.doe.national.core.utils.DateUtils;

public class SurveyMetadata {

    private static final String KEY_SCHOOL_ID = "schoolNo";
    private static final String KEY_SCHOOL_NAME = "schoolName";
    private static final String KEY_COMPLETION = "surveyCompleted";
    private static final String KEY_COMPLETION_DATE = "surveyCompletedDateTime";
    private static final String KEY_SURVEY_TAG = "surveyTag";
    private static final String KEY_CREATION_DATE = "createDateTime";
    private static final String KEY_CREATION_USER = "createUser";
    private static final String KEY_LAST_EDIT_DATE = "lastEditedDateTime";
    private static final String KEY_LAST_EDIT_USER = "lastEditedUser";
    private static final String KEY_TYPE = "type";
    private static final String SEPARATOR_CREATE_USER = ", ";

    private String schoolId;
    private String schoolName;
    private String lastEditedUser;
    private SurveyState surveyState;
    private String creator;
    private Date lastEditedDate;
    private String surveyTag;
    private Date creationDate;

    @Nullable
    private SurveyType surveyType;

    @Nullable
    private Date completionDate;

    public static SurveyMetadata extract(File file) {
        SurveyMetadata metadata = new SurveyMetadata();
        Map<String, String> properties = file.getProperties();

        if (properties == null) {
            return metadata;
        }

        metadata.schoolId = properties.get(KEY_SCHOOL_ID);
        metadata.schoolName = properties.get(KEY_SCHOOL_NAME);

        metadata.lastEditedUser = properties.get(KEY_LAST_EDIT_USER);
        metadata.creator = properties.get(KEY_CREATION_USER);

        String surveyTypeString = properties.get(KEY_TYPE);
        if (surveyTypeString != null) {
            metadata.surveyType = SurveyType.valueOf(surveyTypeString);
        }

        String editDateString = properties.get(KEY_LAST_EDIT_DATE);
        if (editDateString != null) {
            metadata.lastEditedDate = DateUtils.parseUtc(editDateString);
        }

        String existingSurveyTag = properties.get(KEY_SURVEY_TAG);
        if (existingSurveyTag != null) {
            metadata.surveyTag = existingSurveyTag;
        }

        String createDateString = properties.get(KEY_CREATION_DATE);
        if (createDateString != null) {
            metadata.creationDate = DateUtils.parseUtc(createDateString);
        }

        String completionDateString = properties.get(KEY_COMPLETION_DATE);
        if (completionDateString != null) {
            metadata.completionDate = DateUtils.parseUtc(completionDateString);
        }

        String surveyStateAsString = properties.get(KEY_COMPLETION);
        if (surveyStateAsString != null) {
            metadata.surveyState = SurveyState.fromValue(surveyStateAsString);
        }

        return metadata;
    }

    private SurveyMetadata() {
        // private constructor
    }

    public SurveyMetadata(Survey survey, String userEmail) {
        schoolId = survey.getSchoolId();
        schoolName = survey.getSchoolName();
        surveyState = survey.getState();
        completionDate = survey.getCompleteDate();
        surveyTag = survey.getSurveyTag();
        creationDate = survey.getCreateDate();
        creator = getUpdatedCreateUser(survey.getCreateUser(), userEmail);
        lastEditedDate = new Date();
        lastEditedUser = getUpdatedLastEditedUser(survey.getLastEditedUser(), userEmail);
        surveyType = survey.getSurveyType();
    }

    public File applyToDriveFile(File file) {
        File updatedFile = new File();
        updatedFile.setName(file.getName());
        updatedFile.setParents(file.getParents());
        updatedFile.setMimeType(file.getMimeType());
        Map<String, String> properties = createMetadataProperties(file);
        updatedFile.setProperties(properties);
        return updatedFile;
    }

    private Map<String, String> createMetadataProperties(File file) {
        Map<String, String> properties = file.getProperties();

        if (properties == null) {
            properties = new HashMap<>();
        }

        if (!properties.containsKey(KEY_SCHOOL_ID)) {
            properties.put(KEY_SCHOOL_ID, schoolId);
            properties.put(KEY_SCHOOL_NAME, schoolName);
        }

        if (!properties.containsKey(KEY_SCHOOL_NAME)) {
            properties.put(KEY_SCHOOL_NAME, schoolName);
        }

        properties.put(KEY_LAST_EDIT_DATE, DateUtils.formatUtc(lastEditedDate));
        properties.put(KEY_LAST_EDIT_USER, lastEditedUser);
        properties.put(KEY_CREATION_USER, creator);

        properties.put(KEY_COMPLETION, surveyState.getValue());
        if (surveyState == SurveyState.COMPLETED) {
            completionDate = new Date();
            properties.put(KEY_COMPLETION_DATE, DateUtils.formatUtc(completionDate));
        } else {
            properties.remove(KEY_COMPLETION_DATE);
        }

        String existingSurveyTag = properties.get(KEY_SURVEY_TAG);
        if (existingSurveyTag == null) {
            properties.put(KEY_SURVEY_TAG, surveyTag);
        }

        String existingCreationDate = properties.get(KEY_CREATION_DATE);
        if (existingCreationDate == null) {
            properties.put(KEY_CREATION_DATE, DateUtils.formatUtc(creationDate));
        }

        if (surveyType != null) {
            properties.put(KEY_TYPE, surveyType.name());
        }

        return properties;
    }

    private String getUpdatedCreateUser(String existingCreateUser, String existingExternalUser) {
        String updatedCreateUser = "";

        if (existingCreateUser != null) {
            updatedCreateUser += existingCreateUser;
        }

        if (existingExternalUser != null && !updatedCreateUser.contains(existingExternalUser)) {
            updatedCreateUser += SEPARATOR_CREATE_USER + existingExternalUser;
        }

        return updatedCreateUser;
    }

    private String getUpdatedLastEditedUser(String lastEditedUser, String userEmail) {
        return (userEmail != null) ? userEmail : lastEditedUser;
    }

    @NonNull
    @Override
    public String toString() {
        return SurveyMetadata.class.getName() + " { " + createMetadataProperties(new File()) + " }";
    }
}
