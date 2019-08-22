package fm.doe.national.core.data.exceptions;


import androidx.annotation.NonNull;

public class PickException extends ReasonableException {
    @NonNull
    @Override
    protected String getMainMessage() {
        return "Failed to perform pick action";
    }

    public PickException(String reason) {
        setReason(reason);
    }
}
