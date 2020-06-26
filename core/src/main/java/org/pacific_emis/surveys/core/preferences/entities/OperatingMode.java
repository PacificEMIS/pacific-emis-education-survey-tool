package org.pacific_emis.surveys.core.preferences.entities;

import com.omega_r.libs.omegatypes.Text;

import org.pacific_emis.surveys.core.R;

public enum OperatingMode {
    DEV(0, Text.from(R.string.op_mode_dev), "Development"),
    PROD(1, Text.from(R.string.op_mode_prod), "Production");

    private final Text name;
    private final String serializedName;
    private final int value;

    public static OperatingMode createFromValue(int value) {
        for (OperatingMode operatingMode : OperatingMode.values()) {
            if (operatingMode.value == value) {
                return operatingMode;
            }
        }
        return DEV;
    }

    public static OperatingMode createFromSerializedName(String name) {
        for (OperatingMode operatingMode : OperatingMode.values()) {
            if (operatingMode.serializedName.equals(name)) {
                return operatingMode;
            }
        }
        return DEV;
    }

    OperatingMode(int value, Text name, String serializedName) {
        this.value = value;
        this.name = name;
        this.serializedName = serializedName;
    }

    public int getValue() {
        return value;
    }

    public Text getName() {
        return name;
    }
}
