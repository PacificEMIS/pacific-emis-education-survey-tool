package fm.doe.national.core.preferences.entities;

import androidx.annotation.Nullable;

import java.io.Serializable;

public enum AppContext implements Serializable {
    FCM(0), RMI(1);

    private int value;

    @Nullable
    public static AppContext createFromValue(int value) {
        for (AppContext appContext : AppContext.values()) {
            if (value == appContext.getValue()) {
                return appContext;
            }
        }
        return null;
    }

    AppContext(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
