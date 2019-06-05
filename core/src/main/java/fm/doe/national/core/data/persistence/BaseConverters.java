package fm.doe.national.core.data.persistence;

import androidx.room.TypeConverter;

import java.util.Date;

import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.core.preferences.entities.SurveyType;

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

}
