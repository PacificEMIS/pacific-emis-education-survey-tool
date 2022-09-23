package org.pacific_emis.surveys.core.preferences.entities;

import com.omega_r.libs.omegatypes.Text;

import org.pacific_emis.surveys.core.R;

public enum LogAction {
    CREATED(Text.from(R.string.log_action_created)),
    EDITED(Text.from(R.string.log_action_edited)),
    DELETED(Text.from(R.string.log_action_deleted));

    private final Text name;

    public static LogAction getOrDefault(String value) {
        try {
            return valueOf(value);
        } catch (IllegalArgumentException | NullPointerException e) {
            return LogAction.DELETED;
        }
    }

    LogAction(Text name) {
        this.name = name;
    }

    public Text getName() {
        return name;
    }
}
