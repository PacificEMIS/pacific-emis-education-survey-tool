package fm.doe.national.core.preferences.entities;

import androidx.annotation.Nullable;

import java.io.Serializable;

public enum AccreditationType implements Serializable {
    SCHOOL(0), WASH(1);

    private int value;

    @Nullable
    public static AccreditationType createFromValue(int value) {
        for (AccreditationType type : AccreditationType.values()) {
            if (value == type.getValue()) {
                return type;
            }
        }
        return null;
    }

    AccreditationType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
