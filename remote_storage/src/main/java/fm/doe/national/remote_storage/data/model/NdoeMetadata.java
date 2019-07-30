package fm.doe.national.remote_storage.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.api.services.drive.model.File;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.preferences.entities.SurveyType;
import fm.doe.national.core.utils.DateUtils;

public class NdoeMetadata {

    private static final String KEY_SCHOOL_ID = "schoolNo";
    private static final String KEY_COMPLETION = "surveyCompleted";
    private static final String KEY_COMPLETION_DATE = "surveyCompletedDateTime";
    private static final String KEY_SURVEY_TAG = "surveyTag";
    private static final String KEY_CREATION_DATE = "createDateTime";
    private static final String KEY_CREATION_USER = "createUser";
    private static final String KEY_LAST_EDIT_DATE = "lastEditedDateTime";
    private static final String KEY_LAST_EDIT_USER = "lastEditedUser";
    private static final String KEY_TYPE = "type";

    private static final String VALUE_TRUE = "POSITIVE";
    private static final String VALUE_FALSE = "NEGATIVE";

    private String schoolId;
    private String lastEditedUser;
    private boolean isSurveyCompleted;
    private String creator;
    private Date lastEditedDate;
    private String surveyTag;
    private Date creationDate;

    @Nullable
    private SurveyType surveyType;

    @Nullable
    private Date completionDate;

    public static NdoeMetadata extract(File file) {
        NdoeMetadata metadata = new NdoeMetadata();
        Map<String, String> properties = file.getProperties();

        if (properties == null) {
            return metadata;
        }

        metadata.schoolId = properties.get(KEY_SCHOOL_ID);
        metadata.lastEditedUser = properties.get(KEY_LAST_EDIT_USER);
        metadata.isSurveyCompleted = convertMetadataToBoolean(properties.get(KEY_COMPLETION));
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

        return metadata;
    }

    private NdoeMetadata() {
        // private constructor
    }

    public NdoeMetadata(Survey survey, String user) {
        schoolId = survey.getSchoolId();
        isSurveyCompleted = survey.isCompleted();

        if (isSurveyCompleted) {
            completionDate = survey.getCompleteDate();
        }

        surveyTag = survey.getSurveyTag();
        creationDate = survey.getCreateDate();
        creator = user;
        lastEditedDate = new Date();
        lastEditedUser = user;
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
        }

        properties.put(KEY_LAST_EDIT_DATE, DateUtils.formatUtc(lastEditedDate));
        properties.put(KEY_LAST_EDIT_USER, lastEditedUser);

        boolean wasCompleted = convertMetadataToBoolean(properties.get(KEY_COMPLETION));
        if (!wasCompleted) {
            properties.put(KEY_COMPLETION, convertBoolToMetadata(isSurveyCompleted));
            if (isSurveyCompleted) {
                properties.put(KEY_COMPLETION_DATE, DateUtils.formatUtc(completionDate));
            }
        }

        String existingSurveyTag = properties.get(KEY_SURVEY_TAG);
        if (existingSurveyTag == null) {
            properties.put(KEY_SURVEY_TAG, surveyTag);
        }

        String existingCreationDate = properties.get(KEY_CREATION_DATE);
        if (existingCreationDate == null) {
            properties.put(KEY_CREATION_DATE, DateUtils.formatUtc(creationDate));
        }

        String existingCreator = properties.get(KEY_CREATION_USER);
        if (existingCreator == null) {
            properties.put(KEY_CREATION_USER, creator);
        }

        if (surveyType != null) {
            properties.put(KEY_TYPE, surveyType.name());
        }

        return properties;
    }

    private static String convertBoolToMetadata(boolean bool) {
        return bool ? VALUE_TRUE : VALUE_FALSE;
    }

    private static boolean convertMetadataToBoolean(@Nullable String data) {
        return data != null && data.equals(VALUE_TRUE);
    }

    @NonNull
    @Override
    public String toString() {
        return NdoeMetadata.class.getName() + " { " + createMetadataProperties(new File()) + " }";
    }
}
