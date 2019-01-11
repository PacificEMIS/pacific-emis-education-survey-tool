package fm.doe.national.data.cloud.exceptions;


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
