package org.pacific_emis.surveys.core.data.exceptions;


import androidx.annotation.NonNull;

public class PickerDeclinedException extends ReasonableException {
    @NonNull
    @Override
    protected String getMainMessage() {
        return "Picker declined";
    }
}
