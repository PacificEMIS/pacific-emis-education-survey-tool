package fm.doe.national.wash_core.data.persistence;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import fm.doe.national.wash_core.data.model.BinaryAnswerState;
import fm.doe.national.wash_core.data.model.Location;
import fm.doe.national.wash_core.data.model.QuestionType;
import fm.doe.national.wash_core.data.model.TernaryAnswerState;
import fm.doe.national.wash_core.data.serialization.model.Relation;
import fm.doe.national.wash_core.data.serialization.model.Variant;

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

    @TypeConverter
    public static String convertFromRelationToJson(Relation relation) {
        return sGson.toJson(relation);
    }

    @TypeConverter
    public static Relation convertFromJsonToRelation(String json) {
        return sGson.fromJson(json, Relation.class);
    }

    @TypeConverter
    public static List<Variant> convertFromJsonToVariantsList(String name) {
        return Arrays.asList(sGson.fromJson(name, Variant[].class));
    }

    @TypeConverter
    public static String convertFromVariantsListToJson(List<Variant> list) {
        return sGson.toJson(list);
    }

    @TypeConverter
    public static List<String> convertFromJsonToStringList(String name) {
        return Arrays.asList(sGson.fromJson(name, String[].class));
    }

    @TypeConverter
    public static String convertFromStringListToJson(List<String> list) {
        return sGson.toJson(list);
    }

    @TypeConverter
    public static Location convertFromJsonToLocation(String name) {
        return sGson.fromJson(name, Location.class);
    }

    @TypeConverter
    public static String convertFromLocationToJson(Location location) {
        return sGson.toJson(location);
    }

    @TypeConverter
    public static BinaryAnswerState convertFromNameToBinaryAnswerState(String name) {
        return BinaryAnswerState.valueOf(name);
    }

    @TypeConverter
    public static String convertFromBinaryAnswerStateToName(BinaryAnswerState binaryAnswerState) {
        return binaryAnswerState.name();
    }


    @TypeConverter
    public static TernaryAnswerState convertFromNameToTernaryAnswerState(String name) {
        return TernaryAnswerState.valueOf(name);
    }

    @TypeConverter
    public static String convertFromTernaryAnswerStateStateToName(TernaryAnswerState ternaryAnswerState) {
        return ternaryAnswerState.name();
    }

}