package org.pacific_emis.surveys.core.data.persistence;

import androidx.room.TypeConverter;

import java.util.Date;

import org.pacific_emis.surveys.core.data.model.SurveyState;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;
import org.pacific_emis.surveys.core.preferences.entities.SurveyType;

public class BaseConverters {

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
    public static AppRegion convertFromNameToAppRegion(String value) {
        return AppRegion.valueOf(value);
    }

    @TypeConverter
    public static String convertFromAppRegionToName(AppRegion appRegion) {
        return appRegion.name();
    }

    @TypeConverter
    public static SurveyState convertFromNameToSurevyState(String value) {
        return SurveyState.valueOf(value);
    }

    @TypeConverter
    public static String convertFromSurveyStateToName(SurveyState surveyState) {
        return surveyState.name();
    }

}
