package fm.doe.national.data.cloud.exceptions;

import android.support.annotation.NonNull;

public class PickerDeclinedException extends ReasonableException {
    @NonNull
    @Override
    protected String getMainMessage() {
        return "Picker declined";
    }
}
