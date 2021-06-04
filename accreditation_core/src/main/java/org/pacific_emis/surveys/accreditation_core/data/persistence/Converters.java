package org.pacific_emis.surveys.accreditation_core.data.persistence;

import androidx.room.TypeConverter;

import org.pacific_emis.surveys.accreditation_core.data.model.AnswerState;
import org.pacific_emis.surveys.accreditation_core.data.model.EvaluationForm;

public class Converters {

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

}