package org.pacific_emis.surveys.core.preferences.entities;

import androidx.annotation.Nullable;

import java.io.Serializable;

public enum SurveyType implements Serializable {
    SCHOOL_ACCREDITATION(0), WASH(1);

    private int value;

    @Nullable
    public static SurveyType createFromValue(int value) {
        for (SurveyType type : SurveyType.values()) {
            if (value == type.getValue()) {
                return type;
            }
        }
        return null;
    }

    SurveyType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
