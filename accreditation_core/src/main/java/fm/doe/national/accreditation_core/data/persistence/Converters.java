package fm.doe.national.accreditation_core.data.persistence;

import androidx.room.TypeConverter;

import java.util.Date;

import fm.doe.national.accreditation_core.data.model.AnswerState;
import fm.doe.national.accreditation_core.data.model.EvaluationForm;
import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.core.preferences.entities.SurveyType;

public class Converters {
    @TypeConverter
    public static Date convertFromTimestampToDate(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long convertFromDateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static SurveyType convertFromNameToSurveyType(String value) {
        return SurveyType.valueOf(value);
    }

    @TypeConverter
    public static String convertFromSurveyTypeToName(SurveyType surveyType) {
        return surveyType.name();
    }

    @TypeConverter
    public static AnswerState convertFromNameToAsnwerState(String value) {
        return AnswerState.valueOf(value);
    }

    @TypeConverter
    public static String convertFromAnswerStateToName(AnswerState surveyType) {
        return surveyType.name();
    }

    @TypeConverter
    public static EvaluationForm convertFromNameToEvaluationForm(String value) {
        return EvaluationForm.valueOf(value);
    }

    @TypeConverter
    public static String convertFromEvaluationFormToName(EvaluationForm surveyType) {
        return surveyType.name();
    }

    @TypeConverter
    public static AppRegion convertFromNameToAppRegion(String value) {
        return AppRegion.valueOf(value);
    }

    @TypeConverter
    public static String convertFromAppRegionToName(AppRegion appRegion) {
        return appRegion.name();
    }
}