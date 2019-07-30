package fm.doe.national.remote_storage.utils;

import androidx.annotation.NonNull;

import fm.doe.national.core.data.exceptions.NotImplementedException;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.preferences.entities.SurveyType;

public class SurveyTextUtil {

    private static final String PREFIX_SCHOOL_ACCREDITATION = "SA";
    private static final String PREFIX_WASH = "WASH";

    @NonNull
    public static String createSurveyFileName(Survey survey, String userEmail) {
        return convertSurveyTypeToExportPrefix(survey.getSurveyType()) + "-" +
                userEmail + "-" +
                survey.getSchoolName() + "-" +
                survey.getSchoolId() + "-" +
                survey.getSurveyTag() + ".xml";
    }

    @NonNull
    public static String createSurveySheetName(Survey survey) {
        return survey.getSchoolId() + "-" + survey.getSurveyTag();
    }

    public static String convertSurveyTypeToExportPrefix(SurveyType surveyType) {
        switch (surveyType) {
            case SCHOOL_ACCREDITATION:
                return PREFIX_SCHOOL_ACCREDITATION;
            case WASH:
                return PREFIX_WASH;
            default:
                throw new NotImplementedException();
        }
    }

}
