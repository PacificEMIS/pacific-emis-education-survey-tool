package fm.doe.national.remote_storage.utils;

import androidx.annotation.NonNull;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.preferences.entities.SurveyType;
import fm.doe.national.core.utils.DateUtils;

public class TextUtil {

    @NonNull
    public static String createSurveyFileName(Survey survey) {
        return convertSurveyTypeToExportPrefix(survey.getSurveyType()) + "-" +
                survey.getSchoolName() + "-" +
                survey.getSchoolId() + "-" +
                DateUtils.format(survey.getDate()) + ".xml";
    }

    private static String convertSurveyTypeToExportPrefix(SurveyType surveyType) {
        switch (surveyType) {
            case SCHOOL_ACCREDITATION:
                return "SA";
            case WASH:
                return "WASH";
        }
        return "";
    }

}
