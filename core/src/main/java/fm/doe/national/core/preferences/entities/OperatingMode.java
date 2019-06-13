package fm.doe.national.core.preferences.entities;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.core.R;

public enum OperatingMode {
    DEV(0, Text.from(R.string.op_mode_dev)),
    PROD(1, Text.from(R.string.op_mode_prod));

    private final Text name;
    private final int value;

    public static OperatingMode createFromValue(int value) {
        for (OperatingMode operatingMode : OperatingMode.values()) {
            if (operatingMode.value == value) {
                return operatingMode;
            }
        }
        return DEV;
    }

    OperatingMode(int value, Text name) {
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
