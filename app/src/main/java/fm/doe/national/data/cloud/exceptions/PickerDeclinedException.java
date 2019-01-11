package fm.doe.national.data.cloud.exceptions;


import androidx.annotation.NonNull;

public class PickerDeclinedException extends ReasonableException {
    @NonNull
    @Override
    protected String getMainMessage() {
        return "Picker declined";
    }
}
