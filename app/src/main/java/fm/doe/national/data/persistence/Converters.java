package fm.doe.national.data.persistence;

import java.util.Date;

import androidx.room.TypeConverter;

import fm.doe.national.data.persistence.new_model.Answer;
import fm.doe.national.data.persistence.new_model.AnswerState;
import fm.doe.national.data.persistence.new_model.SurveyType;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static SurveyType fromRawSurveyType(int value) {
        return SurveyType.valueOf(value);
    }

    @TypeConverter
    public static int surveyTypeToRaw(SurveyType surveyType) {
        return surveyType.getValue();
    }


    @TypeConverter
    public static AnswerState fromRawAnswerState(int value) {
        return AnswerState.valueOf(value);
    }

    @TypeConverter
    public static int answerStateToRaw(AnswerState surveyType) {
        return surveyType.getValue();
    }
}