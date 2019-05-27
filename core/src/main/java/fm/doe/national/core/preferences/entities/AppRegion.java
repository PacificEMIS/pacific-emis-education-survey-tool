package fm.doe.national.core.preferences.entities;

import androidx.annotation.Nullable;

import java.io.Serializable;

public enum AppRegion implements Serializable {
    FCM(0), RMI(1);

    private int value;

    @Nullable
    public static AppRegion createFromValue(int value) {
        for (AppRegion appRegion : AppRegion.values()) {
            if (value == appRegion.getValue()) {
                return appRegion;
            }
        }
        return null;
    }

    AppRegion(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
