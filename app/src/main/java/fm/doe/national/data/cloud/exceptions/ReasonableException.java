package fm.doe.national.data.cloud.exceptions;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class ReasonableException extends Exception {
    @Nullable
    private String reason;

    @Nullable
    public String getReason() {
        return reason;
    }

    public void setReason(@Nullable String reason) {
        this.reason = reason;
    }

    @Override
    public String getMessage() {
        StringBuilder builder = new StringBuilder(getMainMessage());
        if (reason != null) {
            builder.append(" : ").append(reason);
        }
        return builder.toString();
    }

    @NonNull
    protected abstract String getMainMessage();
}
