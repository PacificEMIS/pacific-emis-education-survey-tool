package org.pacific_emis.surveys.core.data.model;

public enum SurveyState {
    COMPLETED("POSITIVE"),
    NOT_COMPLETED("NEGATIVE"),
    MERGED("MERGED");

    public static SurveyState fromValue(String value) {
        for (SurveyState state : SurveyState.values()) {
            if (state.value.equals(value)) {
                return state;
            }
        }
        return NOT_COMPLETED;
    }

    private final String value;

    SurveyState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
