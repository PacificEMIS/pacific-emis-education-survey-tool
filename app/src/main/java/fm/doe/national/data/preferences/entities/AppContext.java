package fm.doe.national.data.preferences.entities;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public enum AppContext implements Serializable {
    FCM(0), RMI(1);

    private int value;

    @Nullable
    public static AppContext createFromValue(int value) {
        switch (value) {
            case 0:
                return AppContext.FCM;
            case 1:
                return AppContext.RMI;
            default:
                return null;
        }
    }

    AppContext(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
