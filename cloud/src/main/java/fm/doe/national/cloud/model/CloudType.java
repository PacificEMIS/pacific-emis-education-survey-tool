package fm.doe.national.cloud.model;

import androidx.annotation.Nullable;

public enum CloudType {
    EMPTY(0), DRIVE(1), DROPBOX(2);

    private int value;

    @Nullable
    public static CloudType createFromValue(int value) {
        for (CloudType type : CloudType.values()) {
            if (value == type.getValue()) {
                return type;
            }
        }
        return null;
    }

    CloudType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
