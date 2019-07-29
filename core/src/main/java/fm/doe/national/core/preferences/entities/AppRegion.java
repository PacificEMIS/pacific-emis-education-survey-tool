package fm.doe.national.core.preferences.entities;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;

import java.io.Serializable;

import fm.doe.national.core.R;

public enum AppRegion implements Serializable {
    FSM(0, Text.from(R.string.region_fsm)),
    RMI(1, Text.from(R.string.region_rmi));

    private final int value;
    private final Text name;

    @Nullable
    public static AppRegion createFromValue(int value) {
        for (AppRegion appRegion : AppRegion.values()) {
            if (value == appRegion.getValue()) {
                return appRegion;
            }
        }
        return null;
    }

    AppRegion(int value, Text name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public Text getName() {
        return name;
    }
}
