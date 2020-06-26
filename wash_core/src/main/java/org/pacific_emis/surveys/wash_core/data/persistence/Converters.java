package org.pacific_emis.surveys.wash_core.data.persistence;

import androidx.annotation.Nullable;
import androidx.room.TypeConverter;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import org.pacific_emis.surveys.wash_core.data.model.BinaryAnswerState;
import org.pacific_emis.surveys.wash_core.data.model.Location;
import org.pacific_emis.surveys.wash_core.data.model.QuestionType;
import org.pacific_emis.surveys.wash_core.data.model.TernaryAnswerState;
import org.pacific_emis.surveys.wash_core.data.serialization.model.Relation;
import org.pacific_emis.surveys.wash_core.data.model.Variant;

public class Converters {

    private final static Gson sGson = new Gson();

    @TypeConverter
    public static QuestionType convertFromNameToQuestionType(String name) {
        return QuestionType.valueOf(name);
    }

    @TypeConverter
    public static String convertFromQuestionTypeToName(QuestionType questionType) {
        return questionType.name();
    }

    @Nullable
    @TypeConverter
    public static String convertFromRelationToJson(@Nullable Relation relation) {
        return relation == null ? null : sGson.toJson(relation);
    }

    @Nullable
    @TypeConverter
    public static Relation convertFromJsonToRelation(@Nullable String json) {
        return json == null ? null : sGson.fromJson(json, Relation.class);
    }

    @Nullable
    @TypeConverter
    public static List<Variant> convertFromJsonToVariantsList(@Nullable String json) {
        return json == null ? null : Arrays.asList(sGson.fromJson(json, Variant[].class));
    }

    @Nullable
    @TypeConverter
    public static String convertFromVariantsListToJson(@Nullable List<Variant> list) {
        return list == null ? null : sGson.toJson(list);
    }

    @Nullable
    @TypeConverter
    public static List<String> convertFromJsonToStringList(@Nullable String json) {
        return json == null ? null : Arrays.asList(sGson.fromJson(json, String[].class));
    }

    @Nullable
    @TypeConverter
    public static String convertFromStringListToJson(@Nullable List<String> list) {
        return list == null ? null : sGson.toJson(list);
    }

    @Nullable
    @TypeConverter
    public static Location convertFromJsonToLocation(@Nullable String json) {
        return json == null ? null : sGson.fromJson(json, Location.class);
    }

    @Nullable
    @TypeConverter
    public static String convertFromLocationToJson(@Nullable Location location) {
        return location == null ? null : sGson.toJson(location);
    }

    @Nullable
    @TypeConverter
    public static BinaryAnswerState convertFromNameToBinaryAnswerState(@Nullable String name) {
        return name == null ? null : BinaryAnswerState.valueOf(name);
    }

    @Nullable
    @TypeConverter
    public static String convertFromBinaryAnswerStateToName(@Nullable BinaryAnswerState binaryAnswerState) {
        return binaryAnswerState == null ? null : binaryAnswerState.name();
    }

    @Nullable
    @TypeConverter
    public static TernaryAnswerState convertFromNameToTernaryAnswerState(@Nullable String name) {
        return name == null ? null : TernaryAnswerState.valueOf(name);
    }

    @Nullable
    @TypeConverter
    public static String convertFromTernaryAnswerStateStateToName(@Nullable TernaryAnswerState ternaryAnswerState) {
        return ternaryAnswerState == null ? null : ternaryAnswerState.name();
    }

}