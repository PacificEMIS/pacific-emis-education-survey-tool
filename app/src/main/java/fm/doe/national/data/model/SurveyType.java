package fm.doe.national.data.model;

import android.util.SparseArray;

public enum SurveyType {
    SCHOOL_ACCREDITATION(0), WASH(1);

    private int value;

    private static SparseArray<SurveyType> map = new SparseArray<>();

    SurveyType(int value) {
        this.value = value;
    }

    static {
        for (SurveyType surveyType : SurveyType.values()) {
            map.put(surveyType.value, surveyType);
        }
    }

    public static SurveyType valueOf(int raw) {
        return map.get(raw);
    }

    public int getValue() {
        return value;
    }
}
