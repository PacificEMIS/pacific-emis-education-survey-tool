package fm.doe.national.data.persistence;

import androidx.room.TypeConverter;

import java.util.Date;

import fm.doe.national.data.model.AnswerState;
import fm.doe.national.data.model.SurveyType;

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
}