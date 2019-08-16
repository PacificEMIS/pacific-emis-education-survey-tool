package fm.doe.national.remote_storage.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.api.services.drive.model.File;

import java.util.HashMap;
import java.util.Map;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.preferences.entities.SurveyType;

public class PhotoMetadata {
    private static final String KEY_SCHOOL_ID = "schoolNo";
    private static final String KEY_SURVEY_TAG = "surveyTag";
    private static final String KEY_TYPE = "type";

    private String schoolId;
    private String surveyTag;

    @Nullable
    private SurveyType surveyType;

    private PhotoMetadata() {
        // private constructor
    }

    public PhotoMetadata(Survey survey) {
        schoolId = survey.getSchoolId();
        surveyTag = survey.getSurveyTag();
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

        String existingSurveyTag = properties.get(KEY_SURVEY_TAG);
        if (existingSurveyTag == null) {
            properties.put(KEY_SURVEY_TAG, surveyTag);
        }

        if (surveyType != null) {
            properties.put(KEY_TYPE, surveyType.name());
        }

        return properties;
    }

    @NonNull
    @Override
    public String toString() {
        return PhotoMetadata.class.getName() + " { " + createMetadataProperties(new File()) + " }";
    }
}
